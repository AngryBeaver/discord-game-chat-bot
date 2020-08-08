package net.verplanmich.bot.game.gaia;

import net.verplanmich.bot.game.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static net.verplanmich.bot.website.MyRestController.EVENT_INFO;

@Component
@Scope("prototype")
public class Gaia implements Game {

    public static final String GAIA = "gaia";

    public static final String EVENT_USER = "user";
    public static final String EVENT_CHAR_SELECTION = "charSelection";
    public static final String EVENT_ALLIANCES = "alliances";
    public static final String EVENT_TECHS = "techs";
    public static final String EVENT_ROUND_BOOSTER = "roundBooster";
    public static final String EVENT_USER_BOOSTER = "userBooster";
    public static final String EVENT_SHARED_ACTIONS = "userActions";
    public static final String EVENT_CURRENT_USER = "currentUser";
    public static final String EVENT_ROUND = "round";
    public static final String EVENT_END = "end";
    public static final String EVENT_USER_ORDER = "userOrder";
    public static final String EVENT_EXPLORED = "explored";
    public static final String EVENT_SECTORS = "sectors";
    public static final String EVENT_SCORE = "score";
    public static final String EVENT_END_SCORE = "endScore";


    public static final String MAP_KEY_USERS = "users";
    public static final String MAP_KEY_ALLIANCES = "alliances";
    public static final String MAP_KEY_TECHS = "techs";
    public static final String MAP_KEY_CHAR_SELECTION = "charSelection";
    public static final String MAP_KEY_USER = "user";
    public static final String MAP_KEY_ROUND_BOOSTER = "roundBooster";
    public static final String MAP_KEY_USER_BOOSTER = "userBooster";
    public static final String MAP_KEY_SHARED_ACTIONS = "userActions";
    public static final String MAP_KEY_CURRENT_USER = "currentUser";
    public static final String MAP_KEY_ROUND = "round";
    public static final String MAP_KEY_STATS = "stats";
    public static final String MAP_KEY_USER_ORDER = "userOrder";
    public static final String MAP_KEY_EXPLORED = "explored";
    public static final String MAP_KEY_SECTORS = "sectors";
    public static final String MAP_KEY_SCORE = "score";
    public static final String MAP_KEY_END_SCORE = "endScore";



    private GameDecks gameDecks;
    private Map<String, UserEntity> users = new HashMap();
    private List<UserEntity> userList = new ArrayList();

    private String currentUser = "";
    private List<UserEntity> nextRoundOrder = new ArrayList<>();
    private int round;
    private int turn = 0;
    private Map<String,Map<String, UserEntity>> stats = new HashMap<>();

    private Map<String,String> userActions = new HashMap<>();
    private Map<String,String> userBooster = new HashMap<>();

    public Gaia(){
        this.gameDecks = new GameDecks();
    }

    @Override
    public String getName() {
        return GAIA;
    }

    @GameMethod
    public GameResult getStatistic(){
        Map<String,List<UserEntity>> stats = new HashMap();
        userList.forEach(user-> {
            user.setStatsAxis(round + "." + turn);
            this.stats.get(user.getId()).put(round + "." + turn, new UserEntity(user));
        });
        this.stats.forEach((key,value)->
                stats.put(key, value.values().stream().collect(Collectors.toList()))
        );
        return new GameResult().set(MAP_KEY_STATS,stats);
    }

    @GameMethod
    public GameResult endTurn(String userId){
        UserEntity user = this.users.get(userId);
        checkAllHasPassed();
        setNextUser(user);
        return new GameResult()
                .setText(user.getName()+" ends turn")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_CURRENT_USER)
                .set(MAP_KEY_CURRENT_USER,currentUser);
    }

    @GameMethod
    public GameResult pass(String userId){
        UserEntity userEntity = this.users.get(userId);
        userEntity.setHasPassed(true);
        nextRoundOrder.add(userEntity);
        return endTurn(userId);
    }

    private void checkAllHasPassed(){
        long amountNonPassedUser =userList.stream()
                .filter(user-> {
                    user.setStatsAxis(round + "." + turn);
                    stats.get(user.getId()).put(round + "." + turn, new UserEntity(user));
                    return !user.isHasPassed();
                }).count();
        if(amountNonPassedUser == 0){
            throw new GameResultException(endRound());
        }
    }

    private GameResult endRound(){
        userList.stream().forEach(user->{
            user.setHasPassed(false);
            user.setStartPlayer(false);
        });
        if(round >= 6){
            return endGame();
        }
        userList = nextRoundOrder;
        nextRoundOrder = new ArrayList<>();
        currentUser = userList.get(0).getId();
        return startRound(currentUser)
                .addEvent(EVENT_USER_ORDER)
                .set(MAP_KEY_USER_ORDER,userList.stream().map(user->user.getId()).toArray());
    }

    private GameResult endGame(){
        currentUser = "end";
        this.turn = turn +1;
        return new GameResult()
                .setText("Game ends")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_END)
                .addEvent(EVENT_CURRENT_USER)
                .set(MAP_KEY_CURRENT_USER,currentUser);
    }

    private UserEntity setNextUser(UserEntity userEntity){
        int index = userList.indexOf(userEntity);
        int cur = (index+1) % userList.size();
        UserEntity currentUser= userList.get(cur);
        if(currentUser.isStartPlayer()){
            turn = turn +1;
        }
        if(currentUser.isHasPassed()){
            return setNextUser(currentUser);
        }
        this.currentUser = currentUser.getId();
        return currentUser;
    }

    @GameMethod
    public GameResult startRound(String userId){
        GameResult gameResult = new GameResult();
        UserEntity user = this.users.get(userId);
        this.currentUser = userId;
        this.round = round +1;
        this.turn = 0;
        userActions = new HashMap<>();
        user.setStartPlayer(true);
        if(this.round == 1){
            gameResult = this.startGame(user);
        }
        return gameResult
                .setText(user.getName()+" startsRound")
                .addEvent(EVENT_CURRENT_USER)
                .set(MAP_KEY_CURRENT_USER,currentUser)
                .addEvent(EVENT_SHARED_ACTIONS)
                .set(MAP_KEY_SHARED_ACTIONS,userActions)
                .addEvent(EVENT_ROUND)
                .set(MAP_KEY_ROUND,round);
    }

    private GameResult startGame(UserEntity user){
        int index = userList.indexOf(user);
        gameDecks.startGame(userList.size());
        List<UserEntity> startList = new ArrayList();
        for(int i = 0; i<userList.size(); i++){
            int cur = (index+i) % userList.size();
            UserEntity currentUser= userList.get(cur);
            startList.add(currentUser);
            Map<String, UserEntity> map = new TreeMap<>();
            map.put(round+"."+turn,new UserEntity(currentUser));
            stats.put(currentUser.getId(),map);
        }
        userList = startList;
        this.turn = turn +1;
        return new GameResult()
                .addEvent(EVENT_TECHS)
                .set(MAP_KEY_TECHS,gameDecks.getTechs())
                .set(MAP_KEY_USERS,userList)
                .addEvent(EVENT_SECTORS)
                .set(MAP_KEY_SECTORS,gameDecks.getSectors(userList.size()))
                .addEvent(EVENT_USER_ORDER)
                .set(MAP_KEY_USER_ORDER,userList.stream().map(u->u.getId()).toArray())
                .addEvent(EVENT_SCORE)
                .set(MAP_KEY_SCORE,gameDecks.score)
                .addEvent(EVENT_END_SCORE)
                .set(MAP_KEY_END_SCORE,gameDecks.endScore);
    }

    @GameMethod
    public GameResult explore(GameData gameData,String explorInfo){
        UserEntity userEntity = users.get(gameData.getUserId());
        gameDecks.explore(explorInfo,userEntity);
        return new GameResult()
                .addEvent(EVENT_EXPLORED)
                .set(MAP_KEY_EXPLORED,gameDecks.getMap());
    }

    @GameMethod
    public GameResult adjustTech0(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getTech().get(0)+Integer.parseInt(amount);
        userEntity.getTech().set(0,next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustTech1(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getTech().get(1)+Integer.parseInt(amount);
        userEntity.getTech().set(1,next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustTech2(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getTech().get(2)+Integer.parseInt(amount);
        userEntity.getTech().set(2,next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustTech3(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getTech().get(3)+Integer.parseInt(amount);
        userEntity.getTech().set(3,next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustTech4(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getTech().get(4)+Integer.parseInt(amount);
        userEntity.getTech().set(4,next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustTech5(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getTech().get(5)+Integer.parseInt(amount);
        userEntity.getTech().set(5,next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }

    @GameMethod
    public GameResult adjustVictory(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getVictory()+Integer.parseInt(amount);
        userEntity.setVictory(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustOre(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getOre()+Integer.parseInt(amount);
        userEntity.setOre(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustCredit(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getCredit()+Integer.parseInt(amount);
        userEntity.setCredit(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustKnowledge(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getKnowledge()+Integer.parseInt(amount);
        userEntity.setKnowledge(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustQic(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getQic()+Integer.parseInt(amount);
        userEntity.setQic(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustMight0(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getMight0()+Integer.parseInt(amount);
        userEntity.setMight0(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustMight1(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getMight1()+Integer.parseInt(amount);
        userEntity.setMight1(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustMight2(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getMight2()+Integer.parseInt(amount);
        userEntity.setMight2(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustMight3(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getMight3()+Integer.parseInt(amount);
        userEntity.setMight3(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustBrainstone0(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getBrainstone0()+Integer.parseInt(amount);
        userEntity.setBrainstone0(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustBrainstone1(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getBrainstone1()+Integer.parseInt(amount);
        userEntity.setBrainstone1(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustBrainstone2(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getBrainstone2()+Integer.parseInt(amount);
        userEntity.setBrainstone2(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustBrainstone3(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        int next = userEntity.getBrainstone3()+Integer.parseInt(amount);
        userEntity.setBrainstone3(next);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustTerraformer(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        if(amount.startsWith("-")){
            userEntity.getTerraformers().remove(0);
        }else{
            userEntity.getTerraformers().add("");
        }
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }

    @GameMethod
    public GameResult adjustAcademy1(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        if(amount.startsWith("-")){
            userEntity.setAcademy1(true);
        }else{
            userEntity.setAcademy1(false);
        }
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustAcademy2(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        if(amount.startsWith("-")){
            userEntity.setAcademy2(true);
        }else{
            userEntity.setAcademy2(false);
        }
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }
    @GameMethod
    public GameResult adjustStation(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        if(amount.startsWith("-")){
            userEntity.setStation(true);
        }else{
            userEntity.setStation(false);
        }
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }

    @GameMethod
    public GameResult adjustLaboratory(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        if(amount.startsWith("-")){
            userEntity.getLaboratories().remove(0);
        }else{
            userEntity.getLaboratories().add("");
        }
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }

    @GameMethod
    public GameResult adjustTrade(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        if(amount.startsWith("-")){
            userEntity.getTrades().remove(0);
        }else{
            userEntity.getTrades().add("");
        }
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }

    @GameMethod
    public GameResult adjustMine(GameData gameData, String amount) {
        UserEntity userEntity = users.get(gameData.getUserId());
        if(amount.startsWith("-")){
            userEntity.getMines().remove(0);
        }else{
            userEntity.getMines().add("");
        }
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }

    @GameMethod
    public GameResult trashTech(GameData gameData, String cardId) {
        UserEntity userEntity = users.get(gameData.getUserId());
        if(!userEntity.getTechCards().remove(cardId)){
          throw new GameResultException("no such tech available "+cardId);
        }
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity)
                .addEvent(EVENT_TECHS)
                .set(MAP_KEY_TECHS,gameDecks.getTechs());
    }

    @GameMethod
    public GameResult getTech(GameData gameData, String cardId) {
        UserEntity userEntity = users.get(gameData.getUserId());
        gameDecks.getTech(cardId);
        userEntity.addTechCard(cardId);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity)
                .addEvent(EVENT_TECHS)
                .set(MAP_KEY_TECHS,gameDecks.getTechs());
    }

    @GameMethod
    public GameResult flipAlliance(GameData gameData, String alliance) {
        UserEntity userEntity = users.get(gameData.getUserId());
        if(!userEntity.getAlliances().remove(alliance)){
            throw new GameResultException("no such alliance found "+alliance);
        }
        String[] parts = alliance.split("front");
        String end = "back";
        if(parts.length <= 1){
            parts = alliance.split("back");
            end = "front";
        }
        userEntity.getAlliances().add(parts[0]+end+parts[1]);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity);
    }

    @GameMethod
    public GameResult getAlliance(GameData gameData, String alliance) {
        UserEntity userEntity = users.get(gameData.getUserId());
        gameDecks.getAlliance(alliance);
        userEntity.addAlliance(alliance);
        return new GameResult()
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,userEntity)
                .addEvent(EVENT_ALLIANCES)
                .set(MAP_KEY_ALLIANCES,gameDecks.getAlliances());

    }

    @GameMethod
    public GameResult useSharedAction(GameData gameData, String sharedAction){
        UserEntity userEntity = users.get(gameData.getUserId());
        if(userActions.containsKey(sharedAction)){
            throw new GameResultException("sharedAction not avaiable");
        }
        userActions.put(sharedAction,userEntity.getAvatar());
        return new GameResult()
                .addEvent(EVENT_SHARED_ACTIONS)
                .set(MAP_KEY_SHARED_ACTIONS,userActions);
    }

    @GameMethod
    public GameResult takeRoundBooster(GameData gameData, String roundBooster){
        UserEntity userEntity = users.get(gameData.getUserId());
        if(userBooster.containsKey(roundBooster)){
            throw new GameResultException("round booster not avaiable");
        }
        userBooster.values().removeIf(value->value.equals(userEntity.getAvatar()));
        userBooster.put(roundBooster,userEntity.getAvatar());
        return new GameResult()
                .addEvent(EVENT_USER_BOOSTER)
                .set(MAP_KEY_USER_BOOSTER,userBooster);
    }

    @GameMethod
    public GameResult getScores(){
        return new GameResult()
                .set(MAP_KEY_SCORE,gameDecks.score)
                .set(MAP_KEY_END_SCORE,gameDecks.endScore);
    }

    @GameMethod
    public GameResult getExplored(GameData gameData){
        return new GameResult()
                .set(MAP_KEY_EXPLORED,gameDecks.getMap());
    }

    @GameMethod
    public GameResult getRound(){
        return new GameResult()
                .set(MAP_KEY_ROUND,round);
    }

    @GameMethod
    public GameResult getCurrentUser(){
        return new GameResult()
                .set(MAP_KEY_CURRENT_USER,currentUser);
    }

    @GameMethod
    public GameResult getUserActions(){
        return new GameResult()
                .set(MAP_KEY_SHARED_ACTIONS,userActions);
    }

    @GameMethod
    public GameResult getUserBooster(){
        return new GameResult()
                .set(MAP_KEY_USER_BOOSTER,userBooster);
    }

    @GameMethod
    public GameResult getRoundBooster(){
        return new GameResult()
                .set(MAP_KEY_ROUND_BOOSTER,gameDecks.gameRoundBooster);
    }

    @GameMethod
    public GameResult getCharSelection(){
        return new GameResult()
                .set(MAP_KEY_CHAR_SELECTION,gameDecks.getAvatars());
    }

    @GameMethod
    public GameResult getTechs(){
        return new GameResult()
                .set(MAP_KEY_TECHS,gameDecks.getTechs());
    }

    @GameMethod
    public GameResult getAlliances(){
        return new GameResult()
                .set(MAP_KEY_ALLIANCES,gameDecks.getAlliances());
    }

    @GameMethod
    public GameResult getUsers(GameData gameData){
        return new GameResult()
                .set(MAP_KEY_USERS,userList);
    }

    @GameMethod
    public GameResult join(GameData gameData, String avatar) {
        GameResult gameResult = new GameResult();
        if(!users.containsKey(gameData.getUserId())){
            UserEntity user = gameDecks.join(avatar);
            user.setName(gameData.getUserName());
            user.setId(gameData.getUserId());
            userList.add(user);
            users.put(gameData.getUserId(),user);
            gameResult.addEvent(EVENT_INFO)
                    .setText(gameData.getUserName()+" joined")
                    .addEvent(EVENT_USER)
                    .set(MAP_KEY_USER,user)
                    .addEvent(EVENT_CHAR_SELECTION)
                    .set(MAP_KEY_CHAR_SELECTION,gameDecks.getAvatars())
                    .addEvent(EVENT_ROUND_BOOSTER)
                    .set(MAP_KEY_ROUND_BOOSTER,gameDecks.gameRoundBooster);
        }
        return gameResult;
    }

}
