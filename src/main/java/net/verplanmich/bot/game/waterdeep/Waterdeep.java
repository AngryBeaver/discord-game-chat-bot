package net.verplanmich.bot.game.waterdeep;

import net.verplanmich.bot.game.*;
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
    public static final String EVENT_SCORE = "score";
    public static final String EVENT_USER = "user";
    public static final String EVENT_UPDATE_INTRIGUES = "intrigueCards";
    public static final String EVENT_REWARD = "reward";
    public static final String EVENT_TAVERN = "tavern";
    public static final String EVENT_ROUND = "round";
    public static final String EVENT_END = "end";
    public static final String EVENT_CURRENT_USER = "currentUser";


    public static final String MAP_KEY_INTRIGUES = "intrigueCards";
    public static final String MAP_KEY_USER_DATA = "userData";
    public static final String MAP_KEY_USERS = "users";
    public static final String MAP_KEY_USER = "user";
    public static final String MAP_KEY_CURRENT_USER_ID = "currentUserId";
    public static final String MAP_KEY_ROUND = "round";
    public static final String MAP_KEY_COMPLETED_QUESTS = "completedQuests";
    public static final String MAP_KEY_TAVERN = "tavern";
    public static final String MAP_KEY_CHAR = "char";
    public static final String MAP_KEY_STATS = "stats";


    private GameDecks gameDecks;
    private Map<String, User> users = new HashMap();
    private List<User> userList = new ArrayList();
    private List<String> colors = new ArrayList(Arrays.asList("red","green","blue","yellow"));
    private int round = 0;
    private String currentUserId = "";
    private int turn = 0;
    private boolean isEnd = false;
    private Map<String,Map<String,UserEntity>> stats = new HashMap<>();

    @Override
    public String getName() {
        return "waterdeep";
    }

    public Waterdeep(){
        this.gameDecks = new GameDecks();
        Collections.shuffle(colors);
    }

    private void setStatistic(){
        userList.forEach(user-> {
            String x = round+".";
            if(turn < 10){
                x = x + "0";
            }
            x = x + turn;
            user.getUserEntity().setStatsAxis(x);
            this.stats.get(user.getId()).put(x, new UserEntity(user.getUserEntity()));
        });
    }

    @GameMethod
    public GameResult getStatistic(){
        Map<String,List<UserEntity>> stats = new HashMap();
        setStatistic();
        this.stats.forEach((key,value)->
            stats.put(key,value.values().stream().collect(Collectors.toList()))
        );
        return new GameResult().set(MAP_KEY_STATS,stats);
    }

    @GameMethod
    public GameResult endTurn(String userId){
        User user = this.users.get(userId);
        checkAllHasPassed();
        setNextUser(user);
        return new GameResult()
                .setText(user.getUserEntity().getName()+" ends turn")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_CURRENT_USER)
                .set(MAP_KEY_CURRENT_USER_ID,currentUserId);
    }

    @GameMethod
    public GameResult pass(String userId){
        User user = this.users.get(userId);
        user.getUserEntity().setHasPassed(true);
        return endTurn(userId);
    }

    private void checkAllHasPassed(){
        long amountNonPassedUser =userList.stream()
                .filter(user->
                    !user.getUserEntity().isHasPassed()
                ).count();
        setStatistic();
        if(amountNonPassedUser == 0){
            endRound();
            throw new GameResultException(new GameResult()
                    .setText("Round "+round+" ends")
                    .addEvent(EVENT_INFO)
                    .addEvent(EVENT_CURRENT_USER)
                    .set(MAP_KEY_CURRENT_USER_ID,currentUserId));
        }
    }

    private void endRound(){
        currentUserId="";
        userList.stream().forEach(user->{
            user.getUserEntity().setHasPassed(false);
            user.getUserEntity().setStartPlayer(false);
        });
        if(round >= 8){
            endGame();
        }
    }

    private void endGame(){
        isEnd = true;
        currentUserId = "end";
        this.turn = turn +1;
        Map map = new HashMap();
        userList.forEach(user->{
            Map<String,Object> internal = new HashMap();
            map.put(user.getId(),internal);
            internal.put(MAP_KEY_INTRIGUES,user.getIntrigues());
            internal.put(MAP_KEY_COMPLETED_QUESTS,user.getQuests());
            internal.put(MAP_KEY_CHAR,user.getIdentity());
        });
        throw new GameResultException(new GameResult()
                .setText("Game ends")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_END)
                .addEvent(EVENT_CURRENT_USER)
                .set(MAP_KEY_USER_DATA,map)
                .set(MAP_KEY_CURRENT_USER_ID,currentUserId));
    }

    private User setNextUser(User user){
        int index = userList.indexOf(user);
        int cur = (index+1) % userList.size();
        User currentUser= userList.get(cur);
        if(currentUser.getUserEntity().isStartPlayer()){
            turn = turn +1;
        }
        if(currentUser.getUserEntity().isHasPassed()){
            return setNextUser(currentUser);
        }
        this.currentUserId = currentUser.getId();
        return currentUser;
    }

    @GameMethod
    public GameResult startRound(String userId){
        GameResult gameResult = new GameResult();
        User user = this.users.get(userId);
        this.currentUserId = userId;
        this.round = round +1;
        this.turn = 0;

        user.getUserEntity().setStartPlayer(true);
        if(this.round == 1){
            gameResult = this.startGame(user, "under");
        }
        return gameResult
                .setText(user.getUserEntity().getName()+" startsRound")
                .addEvent(EVENT_CURRENT_USER)
                .set(MAP_KEY_CURRENT_USER_ID,currentUserId)
                .addEvent(EVENT_ROUND)
                .set(MAP_KEY_ROUND,round);
    }

    private GameResult startGame(User user, String type){
        this.gameDecks.setGame(type);
        //set tavern
        int index = userList.indexOf(user);
        for(int i = 0; i<userList.size(); i++){
            int cur = (index+i) % userList.size();
             User currentUser= userList.get(cur);
             currentUser.getUserEntity().setGold(4+i);
             currentUser.setIdentity(gameDecks.getChar());
             currentUser.getUserEntity().addActiveQuests(gameDecks.getQuest());
             currentUser.getUserEntity().addActiveQuests(gameDecks.getQuest());
             currentUser.addIntrigues(gameDecks.getIntrigue());
             currentUser.addIntrigues(gameDecks.getIntrigue());
             Map<String,UserEntity> map = new TreeMap<>();
             map.put(round+".0"+turn,new UserEntity(currentUser.getUserEntity()));
             stats.put(currentUser.getId(),map);
        }
        this.turn = turn +1;
        return new GameResult().addEvent(EVENT_START_GAME)
                .set(MAP_KEY_USERS,userList.stream().map(user2->user2.getUserEntity()).toArray())
                .addEvent(EVENT_TAVERN)
                .set(MAP_KEY_TAVERN,this.gameDecks.getTavern());
    }

    @GameMethod
    public GameResult getChar(String userId){
        User user = this.users.get(userId);
        return new GameResult()
                    .set(MAP_KEY_CHAR,user.getIdentity())
                    .addImageId("./assets/char/"+user.getIdentity()+".jpg")
                    .setText(user.getUserEntity().getName()+" getChar");
    }

    @GameMethod
    public GameResult questToComplete(String userId, String cardId){
        User user = this.users.get(userId);
        if(user.getUserEntity().getActiveQuests().contains(cardId)){
            return this.gameDecks.getQuestMap().get(cardId).complete(user,this.gameDecks);
        }else{
            return new GameResult().addEvent(EVENT_INFO)
                    .setText(user.getUserEntity().getName()+" quest not found");
        }
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
        if(isEnd){
            endGame();
        }
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
    public GameResult discardActiveQuest(String userId, String cardId){
        User user = this.users.get(userId);
        if(user.getUserEntity().getActiveQuests().remove(cardId)){
            gameDecks.discardQuest(cardId);
            return new GameResult()
                    .addEvent(EVENT_USER)
                    .set(MAP_KEY_USER,user.getUserEntity())
                    .setText(user.getUserEntity().getName()+" discards")
                    .addEvent(EVENT_INFO)
                    .addImageId("./assets/quests/"+cardId+".jpg");
        }else{
            return new GameResult().addEvent(EVENT_INFO)
                    .setText(user.getUserEntity().getName()+" quest not found");
        }

    }

    @GameMethod
    public GameResult drawQuest(String userId){
        User user = this.users.get(userId);
        String cardId = gameDecks.getQuest();
        gameDecks.discardQuest(cardId);
        return new GameResult()
                .setText(user.getUserEntity().getName()+" drawQuest")
                .addEvent(EVENT_INFO)
                .addImageId("./assets/quests/"+cardId+".jpg");
    }

    @GameMethod
    public GameResult getQuestFromTavern(String userId, String cardId){
        User user = this.users.get(userId);
        if(gameDecks.getTavernCard(cardId)){
            user.getUserEntity().addActiveQuests(cardId);
            return new GameResult()
                    .setText(user.getUserEntity().getName()+" getQuestFromTavern")
                    .addEvent(EVENT_INFO)
                    .addEvent(EVENT_USER)
                    .set(MAP_KEY_USER,user.getUserEntity())
                    .addEvent(EVENT_TAVERN)
                    .set(MAP_KEY_TAVERN,gameDecks.getTavern())
                    .addImageId("./assets/quests/"+cardId+".jpg");
        }else{
            return new GameResult().addEvent(EVENT_INFO)
                    .setText(user.getUserEntity().getName()+" quest not found");
        }
    }

    @GameMethod
    public GameResult questToActive(String userId, String cardId){
        User user = this.users.get(userId);
        if(gameDecks.isQuest(cardId)){
            user.getUserEntity().addActiveQuests(cardId);
            gameDecks.removeQuestCard(cardId);
            return new GameResult()
                    .setText(user.getUserEntity().getName()+" questToActive")
                    .addEvent(EVENT_INFO)
                    .addEvent(EVENT_USER)
                    .set(MAP_KEY_USER,user.getUserEntity());
        }else{
            return new GameResult().addEvent(EVENT_INFO)
                    .setText(user.getUserEntity().getName()+" quest not found");
        }
    }

    @GameMethod
    public GameResult getCompletedQuests(String userId){
        List<String> quests = this.users.get(userId).getQuests();
        return new GameResult()
                .setText("completedQuests")
                .addImageIds(quests.stream().map(cardId->"./assets/quests/"+cardId+".jpg").collect(Collectors.toList()))
                .set(MAP_KEY_COMPLETED_QUESTS,quests);
    }

    @GameMethod
    public GameResult drawIntrigue(String userId){
        User user = this.users.get(userId);
        String cardId = gameDecks.getIntrigue();
        user.addIntrigues(cardId);
        return new GameResult()
                .setText(user.getUserEntity().getName()+" drawIntrigue")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user.getUserEntity());
    }

    @GameMethod
    public GameResult intriguesToVoid(String userId,String cardId) {
        User user = this.users.get(userId);
        if (user.removeIntrigues(cardId)){
            String type = "intrigues";
            if(gameDecks.isQuest(cardId)){
                type="quests";
            }
            return new GameResult()
                    .setText(user.getUserEntity().getName() + " intrigue used")
                    .addEvent(EVENT_UPDATE_INTRIGUES)
                    .addEvent(EVENT_INFO)
                    .addEvent(EVENT_USER)
                    .set(MAP_KEY_USER,user.getUserEntity())
                    .addImageId("./assets/"+type+"/"+cardId+".jpg");
        }else{
            return new GameResult()
                    .setText("you do not own the card "+cardId);
            }
    }

    @GameMethod
    public GameResult getIntriguesCard(String userId,String cardId){
        User user = this.users.get(userId);
        user.addIntrigues(cardId);
        return new GameResult()
                .setText(user.getUserEntity().getName()+" getIntrigueCard")
                .addEvent(EVENT_UPDATE_INTRIGUES)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user.getUserEntity());
    }


    @GameMethod
    public GameResult getIntrigueCards(String userId){
        List<String> intrigues = this.users.get(userId).getIntrigues();
        return new GameResult()
                .setText("intrigues")
                .addImageIds(intrigues.stream().map(cardId->"./assets/intrigues/"+cardId+".jpg").collect(Collectors.toList()))
                .set(MAP_KEY_INTRIGUES,intrigues);
    }




    @GameMethod
    public GameResult tavernShuffle(GameData gameData){
        gameDecks.refillTavern();
        return new GameResult()
                .setText("tavern")
                .addImageIds(this.gameDecks.getTavern().stream().map(cardId->"./assets/quests/"+cardId+".jpg").collect(Collectors.toList()))
                .addEvent(EVENT_TAVERN)
                .set(MAP_KEY_TAVERN,gameDecks.getTavern());
    }

    @GameMethod
    public GameResult getTavern(GameData gameData){
        return new GameResult()
                .setText("tavern")
                .addImageIds(this.gameDecks.getTavern().stream().map(cardId->"./assets/quests/"+cardId+".jpg").collect(Collectors.toList()))
                .set(MAP_KEY_TAVERN, this.gameDecks.getTavern());
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
                    .addEvent(EVENT_USER)
                    .setText(gameData.getUserName()+" joined")
                    .set(MAP_KEY_USER,user.getUserEntity());
        }
        return gameResult;
    }
}
