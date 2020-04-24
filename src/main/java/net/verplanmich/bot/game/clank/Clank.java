package net.verplanmich.bot.game.clank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.verplanmich.bot.game.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static net.verplanmich.bot.game.clank.GameDecks.NAME;
import static net.verplanmich.bot.website.MyRestController.EVENT_INFO;

@Component
@Scope("prototype")
public class Clank implements Game {

    static final String DIRECTORY_ITEMS = "items";
    static final String DIRECTORY_IMAGES = "images";

    static final String EVENT_JOIN = "join";
    static final String EVENT_START_GAME = "startGame";
    static final String EVENT_DRAGON_ATTACK = "dragonAttack";
    static final String EVENT_REFRESH_USER = "user";
    static final String EVENT_REFRESH_CLANK_AREA = "clankArea";
    static final String EVENT_REFRESH_PLAY_AREA = "playArea";
    static final String EVENT_REFRESH_HAND = "hand";
    static final String EVENT_REFRESH_SKILL_MARKET = "skillMarket";
    static final String EVENT_REFRESH_ITEMS = "items";


    static final String MAP_KEY_USER = "user";
    static final String MAP_KEY_CLANK_AREA = "clankArea";
    static final String MAP_KEY_PLAY_AREA = "playArea";
    static final String MAP_KEY_HAND = "hand";
    static final String MAP_KEY_SKILL_MARKET = "skillMarket";


    private GameDecks gameDecks;
    private Map<String, User> users = new HashMap();
    private List<User> userOrder;
    private int currentUserIndex;

    private ObjectMapper objectMapper = new ObjectMapper();
    private List<String> colors = new ArrayList(Arrays.asList("red","green","blue","yellow","orange","purple"));



    @Override
    public String getName() {
        return "clank";
    }

    public Clank(){
        this.gameDecks = new GameDecks();
        Collections.shuffle(colors);
        this.userOrder = new ArrayList();
        this.currentUserIndex = 0;
    }

    @GameMethod
    public GameResult join(GameData gameData) {
        if(!users.containsKey(gameData.getUserId())) {
            String color = colors.remove(0);
            User user = new User(gameData.getUserName(),gameData.getUserId(),color);
            userOrder.add(user);
            users.put(gameData.getUserId(), user);
            int amount = 5-userOrder.size();
            return adjustClank(gameData.getUserId(),String.valueOf(amount))
                    .setText(gameData.getUserName() + " joined as "+ color)
                    .addEvent(EVENT_JOIN);
        }
        return new GameResult()
                .setText(gameData.getUserName() + " is highly interested in this game");
    }

    @GameMethod
    public GameResult startGame() {
        gameDecks.initializeBaseGame();
        userOrder.stream().forEach(user->
                user.initialize());
        currentUserIndex = 0;
        return startTurn(userOrder.get(currentUserIndex).getId()).addEvent(EVENT_START_GAME);
    }

    @GameMethod
    public GameResult adjustClank(String userId,String value){
        int amount = Integer.valueOf(value);
        User user = users.get(userId);
        if(amount > 0){
            gameDecks.addClankToArea(user,amount);
        }else{
            gameDecks.removeClankFromArea(user,amount);
        }
        return new GameResult()
                .setText(user.getName() + " clanks "+amount)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_CLANK_AREA)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_CLANK_AREA,gameDecks.getClankArea())
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult adjustCoins(String userId,String value){
        int amount = Integer.valueOf(value);
        User user = users.get(userId);
        user.adjustCoins(amount);
        return new GameResult()
                .setText(user.getName() + " adjustCoins "+amount)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult heal(String userId,String value){
        int amount = Math.max(0,Integer.valueOf(value));
        User user = users.get(userId);
        user.heal(amount);
        return new GameResult()
                .setText(user.getName() + " heal "+amount)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult addItem(String userId, String itemId){
        User user = users.get(userId);
        user.addItem(itemId);
        return new GameResult()
                .setText(user.getName() + " addItem "+itemId)
                .addImageId("/"+NAME+"/"+DIRECTORY_ITEMS+"/"+itemId+".png")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult removeItem(String userId, String itemId){
        User user = users.get(userId);
        if(!user.removeItem(itemId)){
            throw new IllegalArgumentException();
        }
        return new GameResult()
                .setText(user.getName() + " removeItem "+itemId)
                .addImageId("/"+NAME+"/"+DIRECTORY_ITEMS+"/"+itemId+".png")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult endTurn(String userId){
        User user = users.get(userId);
        if(currentUserIndex != userOrder.indexOf(user)){
            throw new GameResultException(new GameResult().setText(user.getName() +"its not your turn to end"));
        }
        user.endTurn();
        return startTurn(getNextUser(user));
    }

    @GameMethod
    public GameResult discardToPlay(String userId, String cardId){
        User user = users.get(userId);
        if(!user.discardToPlay(cardId)){
            throw new GameResultException(new GameResult().setText(user.getName() +"not in your discardPile"));
        }
        return new GameResult()
                .setText(user.getName() + " discardToPlay "+cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_IMAGES+"/"+cardId+".png")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_HAND)
                .addEvent(EVENT_REFRESH_PLAY_AREA)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult discardToVoid(String userId, String cardId){
        User user = users.get(userId);
        if(!user.discardToVoid(cardId)){
            throw new GameResultException(new GameResult().setText(user.getName() +"not in your discardPile"));
        }
        return new GameResult()
                .setText(user.getName() + " discardToVoid "+cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_IMAGES+"/"+cardId+".png")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_HAND)
                .addEvent(EVENT_REFRESH_PLAY_AREA)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult toPlay(String userId, String cardId){
        User user = users.get(userId);
        user.toPlay(cardId);
        return new GameResult()
                .setText(user.getName() + " toPlay "+cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_IMAGES+"/"+cardId+".png")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_HAND)
                .addEvent(EVENT_REFRESH_PLAY_AREA)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult drawToPlay(String userId){
        User user = users.get(userId);
        String cardId = user.drawToPlay();
        return new GameResult()
                .setText(user.getName() + " drawtoPlay "+cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_IMAGES+"/"+cardId+".png")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_HAND)
                .addEvent(EVENT_REFRESH_PLAY_AREA)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult playToVoid(String userId, String cardId){
        User user = users.get(userId);
        if(!user.playToVoid(cardId)){
            throw new GameResultException(new GameResult().setText(user.getName() +"not in your playArea"));
        }
        return new GameResult()
                .setText(user.getName() + " playToVoid "+cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_IMAGES+"/"+cardId+".png")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_HAND)
                .addEvent(EVENT_REFRESH_PLAY_AREA)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult playToDiscard(String userId, String cardId){
        User user = users.get(userId);
        if(user.playToDiscard(cardId)){
            throw new GameResultException(new GameResult().setText(user.getName() +"not in your playArea"));
        }
        return new GameResult()
                .setText(user.getName() + " toPlay "+cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_IMAGES+"/"+cardId+".png")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_HAND)
                .addEvent(EVENT_REFRESH_PLAY_AREA)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult skillMarketToUser(String userId, String cardId) {
        User user = users.get(userId);
        gameDecks.skillMarketToUser(cardId,user);
        return new GameResult()
                .setText("skillMarkettoUser "+cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_IMAGES+"/"+cardId+".png")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_SKILL_MARKET)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult itemToHand(String userId, String itemId) {
        User user = users.get(userId);
        gameDecks.itemToUser(itemId,user);
        return new GameResult()
                .setText("itemToHand "+itemId)
                .addImageId("/"+NAME+"/"+DIRECTORY_ITEMS+"/"+itemId+".png")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_ITEMS)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult getMinorSecret(String userId) {
        User user = users.get(userId);
        String itemId = gameDecks.minorSecrets.get(0);
        gameDecks.itemToUser(itemId,user);
        return new GameResult()
                .setText("getMinorSecret")
                .addImageId("/"+NAME+"/"+DIRECTORY_ITEMS+"/"+itemId+".png")
                .addEvent(EVENT_INFO)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult getMajorSecret(String userId) {
        User user = users.get(userId);
        String itemId = gameDecks.secrets.get(0);
        gameDecks.itemToUser(itemId,user);
        return new GameResult()
                .setText("getMajorSecret")
                .addImageId("/"+NAME+"/"+DIRECTORY_ITEMS+"/"+itemId+".png")
                .addEvent(EVENT_INFO)
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult dragonAttack(String value){
        int limit = Integer.valueOf(value);
        return new GameResult()
                .setText(gameDecks.dragonAttack(limit).stream().map(c->"<div class='clankCube "+c+"></div>").collect(Collectors.joining()))
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_DRAGON_ATTACK);
    }

    @GameMethod
    public GameResult getPlayArea(String userId){
        User user = users.get(userId);
        return new GameResult()
                .addImageIds(user.getPlayArea().stream().map(cardId->"/"+NAME+"/"+DIRECTORY_IMAGES+"/"+cardId+".png").collect(Collectors.toList()))
                .set(MAP_KEY_PLAY_AREA,user.getPlayArea())
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult getHand(String userId){
        User user = users.get(userId);
        return new GameResult()
                .addImageIds(user.getHand().stream().map(cardId->"/"+NAME+"/"+DIRECTORY_IMAGES+"/"+cardId+".png").collect(Collectors.toList()))
                .set(MAP_KEY_HAND,user.getHand())
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult getUserInfo(String targetUserId) throws JsonProcessingException {
        User user = users.get(targetUserId);
        return new GameResult()
                .setText(objectMapper.writeValueAsString(user.get()))
                .set(MAP_KEY_USER,user.get());
    }

    @GameMethod
    public GameResult getSkillMarket() {
        return new GameResult()
                .setText("getSkillMarket")
                .addImageIds(gameDecks.getSkillMarket().stream()
                        .map(cardId->"/"+NAME+"/"+DIRECTORY_IMAGES+"/"+cardId+".png")
                        .collect(Collectors.toList()))
                .set(MAP_KEY_SKILL_MARKET,gameDecks.getSkillMarket());
    }

    @GameMethod
    public GameResult getClankArea(){
        return new GameResult()
                .setText("getClankArea: "+gameDecks.getClankArea())
                .set(MAP_KEY_CLANK_AREA,gameDecks.getClankArea());
    }

    @GameMethod
    private GameResult startTurn(String userId){
        User user = users.get(userId);
        user.startTurn();
        return new GameResult()
                .setText(user.getName() + " startTurn ")
                .addImageIds(user.getPlayArea().stream().map(cardId->"/"+NAME+"/"+DIRECTORY_IMAGES+"/"+cardId+".png").collect(Collectors.toList()))
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_PLAY_AREA)
                .set(MAP_KEY_PLAY_AREA,user.getPlayArea())
                .set(MAP_KEY_USER,user.get());
    }



    private String getNextUser(User user){
        int index = (userOrder.indexOf(user)+1) %userOrder.size();
        return userOrder.get(index).getId();
    }
/*


    public GameResult getMarketItems(){

    }

    public GameResult getCardMarket(){

    }

    public GameResult getPlayArea(){

    }


    */

}
