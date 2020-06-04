package net.verplanmich.bot.game.waterdeep;

import net.verplanmich.bot.game.Game;
import net.verplanmich.bot.game.GameData;
import net.verplanmich.bot.game.GameMethod;
import net.verplanmich.bot.game.GameResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static net.verplanmich.bot.website.MyRestController.EVENT_INFO;

@Component
@Scope("prototype")
public class Waterdeep implements Game {

    public static final String WATERDEEP = "waterdeep";


    public static final String EVENT_START_GAME = "startGame";
    public static final String EVENT_USER = "user";
    public static final String EVENT_UPDATE_INTRIGUES = "updateIntrigues";
    public static final String EVENT_REWARD = "reward";


    public static final String MAP_KEY_INTRIGUES = "intrigueCards";
    public static final String MAP_KEY_USERS = "users";
    public static final String MAP_KEY_USER = "user";
    public static final String MAP_KEY_CURRENT_USER_ID = "currentUserId";
    public static final String MAP_KEY_ROUND = "round";
    public static final String MAP_KEY_COMPLETED_QUESTS = "completedQuests";


    private GameDecks gameDecks;
    private Map<String, User> users = new HashMap();
    private List<User> userList = new ArrayList();
    private List<String> colors = new ArrayList(Arrays.asList("red","green","blue","yellow"));
    private int round = 0;
    private String currentUserId = "";
    private List<Map<String,UserEntity>> stats = new ArrayList();

    @Override
    public String getName() {
        return "waterdeep";
    }

    public Waterdeep(){
        this.gameDecks = new GameDecks();
        Collections.shuffle(colors);
    }

    @GameMethod
    public GameResult startRound(String userId){
        GameResult gameResult = new GameResult();
        User user = this.users.get(userId);
        this.currentUserId = userId;
        this.round = round +1;
        userList.stream().forEach(user2->{
            user2.getUserEntity().setHasPassed(false);
        });
        if(this.round == 1){
            gameResult = this.startGame(user, "base");
        }
        return new GameResult()
                .setText(user.getUserEntity().getName()+" startsRound")
                .set(MAP_KEY_CURRENT_USER_ID,currentUserId)
                .set(MAP_KEY_ROUND,round);
    }

    private GameResult startGame(User user, String type){
        this.gameDecks.setGame(type);
        //set tavern
        int index = userList.indexOf(user);
        for(int i = 0; i<userList.size(); i++){
            int cur = index+i % userList.size();
             User currentUser= userList.get(cur);
             currentUser.getUserEntity().setGold(4+i);
             currentUser.setIdentity(gameDecks.getChar());
             currentUser.getUserEntity().addActiveQuests(gameDecks.getQuest());
             currentUser.getUserEntity().addActiveQuests(gameDecks.getQuest());
             currentUser.addIntrigues(gameDecks.getIntrigue());
             currentUser.addIntrigues(gameDecks.getIntrigue());
        }
        return new GameResult().addEvent(EVENT_START_GAME)
                .set(MAP_KEY_USERS,userList.stream().map(user2->user2.getUserEntity()).toArray());
    }

    @GameMethod
    public GameResult adjustVictory(String userId, String victory){
        User user = this.users.get(userId);
        user.adjustVictory(victory);
        return new GameResult()
                .setText(user.getUserEntity().getName()+" adjustVictory "+victory)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user.getUserEntity());
    }

    @GameMethod
    public GameResult adjustCleric(String userId, String cleric){
        User user = this.users.get(userId);
        user.adjustCleric(cleric);
        return new GameResult()
                .setText(user.getUserEntity().getName()+" adjustCleric "+cleric)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user.getUserEntity());
    }

    @GameMethod
    public GameResult adjustFighter(String userId, String fighter){
        User user = this.users.get(userId);
        user.adjustFighter(fighter);
        return new GameResult()
                .setText(user.getUserEntity().getName()+" adjustFighter "+fighter)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user.getUserEntity());
    }

    @GameMethod
    public GameResult adjustRogue(String userId, String rogue){
        User user = this.users.get(userId);
        user.adjustRogue(rogue);
        return new GameResult()
                .setText(user.getUserEntity().getName()+" adjustRogue "+rogue)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user.getUserEntity());
    }

    @GameMethod
    public GameResult adjustWizard(String userId, String wizard){
        User user = this.users.get(userId);
        user.adjustWizard(wizard);
        return new GameResult()
                .setText(user.getUserEntity().getName()+" adjustWizard "+wizard)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user.getUserEntity());
    }

    @GameMethod
    public GameResult adjustGold(String userId, String gold){
        User user = this.users.get(userId);
        user.adjustGold(gold);
        return new GameResult()
                .setText(user.getUserEntity().getName()+" adjustGold "+gold)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user.getUserEntity());
    }

    @GameMethod
    public GameResult getCurrentUser(String userId){
        return new GameResult()
                .setText("currentUserId "+currentUserId)
                .set(MAP_KEY_CURRENT_USER_ID,currentUserId);
    }

    @GameMethod
    public GameResult getRound(){
        return new GameResult()
                .setText("round "+this.round)
                .set(MAP_KEY_ROUND,this.round);
    }

    @GameMethod
    public GameResult getCompletedQuests(String userId){
        List<String> quests = this.users.get(userId).getQuests();
        return new GameResult()
                .setText("completedQuests")
                .addImageIds(quests.stream().map(cardId->WATERDEEP+"/quests+/"+cardId+".jpg").collect(Collectors.toList()))
                .set(MAP_KEY_COMPLETED_QUESTS,quests);
    }

    @GameMethod
    public GameResult getIntrigueCards(String userId){
        List<String> intrigues = this.users.get(userId).getIntrigues();
        return new GameResult()
                .setText("intrigues")
                .addImageIds(intrigues.stream().map(cardId->WATERDEEP+"/intrigues+/"+cardId+".jpg").collect(Collectors.toList()))
                .set(MAP_KEY_INTRIGUES,intrigues);
    }

    @GameMethod
    public GameResult getUsers(GameData gameData){
        return join(gameData)
                .set(MAP_KEY_USERS,userList.stream().map(user->user.getUserEntity()).toArray());
    }

    @GameMethod
    public GameResult join(GameData gameData) {
        GameResult gameResult = new GameResult();
        if(!users.containsKey(gameData.getUserId()) && round == 0 && colors.size()>0){
            User user = new User(gameData.getUserId(),gameData.getUserName(),colors.remove(0));
            users.put(gameData.getUserId(),user);
            userList.add(user);
            gameResult.addEvent(EVENT_INFO)
                    .setText(gameData.getUserName()+" joined")
                    .set(MAP_KEY_USER,user.getUserEntity());
        }
        return gameResult;
    }
}
