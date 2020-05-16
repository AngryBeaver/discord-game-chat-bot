package net.verplanmich.bot.game.zombicide;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.verplanmich.bot.game.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.verplanmich.bot.game.zombicide.GameDecks.NAME;
import static net.verplanmich.bot.website.MyRestController.EVENT_INFO;

@Component
@Scope("prototype")
public class Zombicide implements Game {


    static final String DIRECTORY_SPAWN= "spawn";
    static final String DIRECTORY_GEAR = "gear";
    static final String DIRECTORY_CHAR = "char";

    static final String EVENT_JOIN = "join";
    static final String EVENT_START_GAME = "start";
    static final String EVENT_ROLL = "roll";
    static final String EVENT_REFRESH_GAME = "game";
    static final String EVENT_REFRESH_USER = "user";

    static final String MAP_KEY_USER = "user";
    static final String MAP_KEY_USER_MAP = "userMap";
    static final String MAP_KEY_CURRENT_USER = "currentUser";
    static final String MAP_KEY_ROLL = "roll";
    static final String MAP_KEY_ACTIONS = "actions";
    static final String MAP_KEY_DROPS = "drops";


    private ObjectMapper mapper = new ObjectMapper();

    private GameDecks gameDecks;

    private Map<String, User> users = new HashMap();
    private DangerLevel dangerLevel = DangerLevel.BLUE;
    private List<User> userOrder = new ArrayList();
    private int currentUserIndex;
    private boolean gameStarted = false;
    private boolean isZombiTurn = false;
    private boolean isUltraRedMode = false;


    public Zombicide() throws IOException {
        gameDecks = new GameDecks();
    }

    public String getName(){
        return NAME;
    }

    public User getUser(String userId){
        return users.get(userId);
    }

    @GameMethod
    public GameResult join(GameData gameData) {
        if(!gameStarted && !users.containsKey(gameData.getUserId())) {
            User user = new User(gameData,gameDecks.chars.remove(0));
            userOrder.add(user);
            users.put(gameData.getUserId(), user);
            return new GameResult()
                    .addImageId("/"+NAME+"/"+DIRECTORY_CHAR+"/"+user.getChar()+".jpg")
                    .setText(user.getChar()+ " joined")
                    .addEvent(EVENT_INFO)
                    .addEvent(EVENT_JOIN);
        }
        return new GameResult()
                .setText(gameData.getUserName() + " is highly interested in this game");
    }

    @GameMethod
    public GameResult extension(String extension) {
        if(!gameStarted) {
            userOrder.stream().forEach(user ->
                    user.initialize());
            gameDecks.initializeGame(extension, userOrder);
            return new GameResult().setText(extension +"game setUp")
                    .addEvent(EVENT_INFO)
                    .addEvent(EVENT_START_GAME);
        }
        return new GameResult().setText("game is already started");
    }

    @GameMethod
    public GameResult start(String userId) throws JsonProcessingException {
        if(!gameStarted) {
            gameStarted = true;
            getUser(userId).setStartPlayer(true);
            return startTurn(userId);
        }
        return new GameResult().setText("game is already started");
    }

    @GameMethod
    public GameResult endTurn(String userId) throws JsonProcessingException {
        User user = users.get(userId);
        if(currentUserIndex != userOrder.indexOf(user)){
            throw new GameResultException(new GameResult().setText(user.getChar() +" it is not your turn to end"));
        }
        String nextUserId = getNextUser(user);
        User nextUser = getUser(nextUserId);
        if(nextUser.isStartPlayer()){
            isZombiTurn = true;
            nextUser.setStartPlayer(false);
            return startTurn(nextUserId)
                    .setText(nextUser.getChar()+"'s ZombieTime")
                    .addImageId("/"+NAME+"/zombie-time.png");
        }
        if(isZombiTurn){
            isZombiTurn = false;
            nextUser.setStartPlayer(true);
        }
        return startTurn(nextUserId);
    }

    @GameMethod()
    public GameResult take(String userId,String cardId) {
        if(!gameDecks.drops.remove(cardId)){
            return new GameResult().setText("no such gear avaiable "+cardId);
        }
        User user = getUser(userId);
        user.add(cardId);
        return new GameResult()
                .setText(user.getChar() +" takes "+ cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_GEAR+"/"+cardId+".jpg")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_GAME)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod()
    public GameResult drop(String userId,String cardId) {
        User user = getUser(userId);
        if(!user.drop(cardId)){
            return new GameResult().setText("you do not have this card "+cardId);
        }
        gameDecks.drops.add(cardId);
        return new GameResult()
                .setText(user.getChar() +" drops "+ cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_GEAR+"/"+cardId+".jpg")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_GAME)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod()
    public GameResult toGear(String userId,String cardId) {
        User user = getUser(userId);
        user.add(cardId);
        return new GameResult()
                .setText(user.getChar() +" toGear "+ cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_GEAR+"/"+cardId+".jpg")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());

    }

    @GameMethod()
    public GameResult trash(String userId,String cardId) {
        User user = getUser(userId);
        if(!user.drop(cardId)){
            return new GameResult().setText("you do not have this card "+cardId);
        }
        gameDecks.gear.discardCard(cardId);
        return new GameResult()
                .setText(user.getChar() +" trashs "+ cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_GEAR+"/"+cardId+".jpg")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult search(String userId) {
        User user = getUser(userId);
        String cardId = gameDecks.gear.drawOrShuffle();
        user.add(cardId);
        return new GameResult()
                .setText(user.getChar() +" searchs "+ cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_GEAR+"/"+cardId+".jpg")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult xp(String userId, String xp) {
        User user = getUser(userId);
        getUser(userId).addXp(xp);
        users.forEach((s,userx)->dangerLevel = dangerLevel.highest(userx.getDangerLevel()));
        if(dangerLevel.equals(DangerLevel.RED) && !isUltraRedMode){
            isUltraRedMode = true;
            gameDecks.enableUltraRed();
        }
        return new GameResult()
                .setText(user.getChar() +" xp "+ xp)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult heal(String userId) {
        User user = getUser(userId);
        user.drop("wound");
        return new GameResult()
                .setText(user.getChar()+" heals")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult adjustAction(String userId,String action) {
        User user = getUser(userId);
        user.adjustAction(action);
        return new GameResult()
                .setText(user.getChar()+" levelUp")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult damage(String userId) {
        User user = getUser(userId);
        String text = user.getChar() + " is wounded";
        if(!user.damage()){
            boolean isDead = user.die();
            text = user.getChar() + " is zombified";
            if(isDead){
                text = user.getChar() + " is dead";
            }
        }
        return new GameResult()
                .setText(text)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult roll(String userId,String amount) throws JsonProcessingException {
        User user = getUser(userId);
        Integer intAmount = Integer.valueOf(amount);
        List<Integer> rolls = new ArrayList();
        for(int i=0; i<Math.min(intAmount,100); i++){
            rolls.add((int)(1+Math.floor(Math.random()*6)));
        }
        return new GameResult()
                .setText(mapper.writeValueAsString(rolls))
                .addEvent(EVENT_ROLL)
                .set(MAP_KEY_ROLL,rolls)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult spawn() {
        String cardId = gameDecks.spawn.drawOrShuffle();
        gameDecks.spawn.discardCard(cardId);
        users.forEach((s,user)->dangerLevel = dangerLevel.highest(user.getDangerLevel()));
        return  new GameResult()
                .setText("{dangerLevel:"+dangerLevel+"}")
                .addImageId("/"+NAME+"/"+DIRECTORY_SPAWN+"/"+cardId+".jpg")
                .addEvent(EVENT_INFO);
    }


    @GameMethod
    public GameResult getGame() throws JsonProcessingException {
        return new GameResult()
                .setText("game info")
                .addImageIds(gameDecks.drops.stream().map(cardId->"/"+ NAME+"/"+DIRECTORY_GEAR+"/"+cardId+".jpg").collect(Collectors.toList()))
                .set(MAP_KEY_DROPS, gameDecks.drops)
                .set(MAP_KEY_CURRENT_USER, getCurrentUserId());
    }

    @GameMethod
    public GameResult getUserInfo(String targetUserId) throws JsonProcessingException {
        Map<String, UserEntity> userMap= new HashMap();
        users.entrySet().forEach(entry->{
            userMap.put(entry.getKey(), entry.getValue().get());
        });
        User user = users.get(targetUserId);

        return new GameResult()
                .setText(mapper.writeValueAsString(user.get()))
                .addImageIds(user.getGear().stream().map(cardId->"/"+ NAME+"/"+DIRECTORY_GEAR+"/"+cardId+".jpg").collect(Collectors.toList()))
                .set(MAP_KEY_USER_MAP,userMap)
                .set(MAP_KEY_CURRENT_USER, getCurrentUserId());
    }

    @GameMethod
    public GameResult getActions() throws JsonProcessingException {
        Map<String, String> actions = Arrays.stream(Action.values())
                .collect(Collectors.toMap(
                        action->action.name(),
                        Action::getText)
        );
        return new GameResult()
                .setText(mapper.writeValueAsString(actions))
                .set(MAP_KEY_ACTIONS,actions);
    }


    private GameResult startTurn(String userId) throws JsonProcessingException {
        User user = users.get(userId);
        currentUserIndex = userOrder.indexOf(user);

        return new GameResult()
                .setText(user.getChar() +" start turn")
                .addImageId("/"+NAME+"/"+DIRECTORY_CHAR+"/"+user.getChar()+"-card.jpg")
                .addEvent(EVENT_INFO)
                .set(MAP_KEY_CURRENT_USER, getCurrentUserId())
                .set(MAP_KEY_USER,user.get());
    }

    private String getCurrentUserId(){
        String currentUserId = "";
        if(gameStarted) {
            currentUserId = userOrder.get(currentUserIndex).getId();
        }
        return currentUserId;
    }

    private String getNextUser(User user){
        int index = (userOrder.indexOf(user)+1) %userOrder.size();
        return userOrder.get(index).getId();
    }


}
