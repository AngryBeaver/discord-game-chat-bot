package net.verplanmich.bot.game.alienencounter;

import net.verplanmich.bot.game.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static net.verplanmich.bot.game.alienencounter.User.USER_CHAR;
import static net.verplanmich.bot.game.alienencounter.User.USER_NAME;
import static net.verplanmich.bot.website.MyRestController.EVENT_INFO;

@Component
@Scope("prototype")
public class Alienencounter implements Game {

    static final String NAME = "alienencounter";
    static final String DIRECTORY_CREW = "crew";
    static final String DIRECTORY_MISSION = "mission";
    static final String DIRECTORY_CHAR = "char";
    static final String DIRECTORY_STRIKES = "strikes";
    static final String DIRECTORY_HIVE = "hive";

    static final String EVENT_START = "start";
    static final String EVENT_REFRESH_HQ = "hq";
    static final String EVENT_REFRESH_BARRACKS = "barracks";
    static final String EVENT_REFRESH_USER_HAND = "hand";
    static final String EVENT_REFRESH_USER_INFO = "user";
    static final String EVENT_REFRESH_GAME = "game";
    static final String EVENT_REFRESH_USER_STRIKE = "strike";
    static final String EVENT_REFRESH_USER_DRAW = "draw";
    static final String EVENT_REFRESH_USER_DISCARD = "discard";
    static final String EVENT_REFRESH_OPERATIONS = "operations";

    static final String EVENT_JOIN = "join";
    static final String EVENT_REFRESH_HIVE = "hive";
    static final String EVENT_REFRESH_COMPLEX = "complex";
    static final String EVENT_REFRESH_ATTACHMENT = "attachment";
    static final String EVENT_REFRESH_COMBAT = "combat";


    static final String MAP_KEY_USER_ID = "userId";
    static final String MAP_KEY_MISSION = "mission";
    static final String MAP_KEY_OBJECTIVE = "objective";
    static final String MAP_KEY_CARD_ID = "cardId";
    static final String MAP_KEY_HQ = "hq";
    static final String MAP_KEY_BARRACKS = "barracks";
    static final String MAP_KEY_USER_MAP = "userMap";
    static final String MAP_KEY_SERGEANT = "sergeant";
    static final String MAP_KEY_HIVE_SIZE = "hiveSize";
    static final String MAP_KEY_IS_REVEALED = "revealed";
    static final String MAP_KEY_OPERATIONS = "operations";

    private GameDecks gameDecks;

    private Mission mission;
    private List<String> hq;
    private Map<String, User> users = new HashMap();
    private Deck chars;
    private Deck strikes;
    private Deck sergeant;
    private Deck barracks;
    private Deck hive;
    private String objective;
    private List<Location> complex;
    private List<String> combat;
    private boolean gameStarted = false;
    private List<String> operations = new ArrayList<>();

    @Autowired
    public Alienencounter(GameDecks gameDecks){
        this.gameDecks = gameDecks;
        chars = gameDecks.getChars();
        strikes = gameDecks.getStrikes();
    }

    public String getName() {
        return NAME;
    }

    User getUser(String userId)  {
        return users.get(userId);
    }

    Deck getHive(){ return hive;}
    Deck getBarracks(){ return barracks;}
    Deck getSergeant(){ return sergeant;}
    Deck getStrikes(){ return strikes;}
    Deck getChars(){ return chars;}
    List<String> getHq() { return hq;}
    List<Location> getComplex() {return complex;}
    List<String> getCombat() {return combat;}
    List<String> getOperations() {return operations;}



    @GameMethod()
    public GameResult join(GameData gameData) throws IOException {
        if(!users.containsKey(gameData.getUserId())) {
            String charName = chars.drawCard();
            users.put(gameData.getUserId(), new User(gameData.getUserName(),gameData.getUserId(),charName));
            return new GameResult()
                    .setText(gameData.getUserName() + " joined the game")
                    .addImageId("/"+NAME+"/"+DIRECTORY_CHAR+"/"+charName+"-char.png")
                    .addEvent(EVENT_INFO)
                    .addEvent(EVENT_JOIN)
                    .set(MAP_KEY_USER_ID,gameData.getUserId());
        }
        return new GameResult()
                .setText(gameData.getUserName() + "is highly interested in this game");
    }

    @GameMethod()
    public GameResult start(String mission) {
        if(!gameStarted) {
            this.mission = Mission.valueOf(mission.toUpperCase());
            barracks = gameDecks.barracksFor(this.mission);
            hq = new ArrayList();
            operations = new ArrayList<>();
            strikes = gameDecks.getStrikes();
            sergeant = gameDecks.getSergeant();
            fillHq(0);
            fillHq(1);
            fillHq(2);
            fillHq(3);
            fillHq(4);
            objective = "1";
            hive = gameDecks.getHive(this.mission, users.size());
            complex = Arrays.asList(new Location(),new Location(), new Location(), new Location(), new Location());
            gameStarted = true;
            return getGameInfo()
                    .addEvent(EVENT_START);
        }
        return getGameInfo()
                .setText("Game started already")
                .addEvent(EVENT_INFO);
    }

    @GameMethod()
    public GameResult setObjective(String objective){
        try {
            int i = Integer.valueOf(objective);
            if(i >3){
                objective = "1";
            }
        }catch(Exception e){
            objective = "1";
        }
        this.objective = objective;
        return new GameResult()
                .setText("Objective: "+objective)
                .set(MAP_KEY_OBJECTIVE,objective)
                .addImageId("/"+NAME+"/"+DIRECTORY_MISSION+"/"+mission+"-objective"+objective+".png")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_GAME);
    }

    //USER
    @GameMethod()
    public GameResult toHand(String userId, String cardId){
        return GameResultBuilder.fromCrewCardId(this,cardId)
                .toHand(userId);
    }
    @GameMethod()
    public GameResult toDiscard(String userId, String cardId){
        return GameResultBuilder.fromCrewCardId(this,cardId)
                .toDiscard(userId);
    }
    @GameMethod()
    public GameResult toDraw(String userId, String cardId){
        return GameResultBuilder.fromCrewCardId(this,cardId)
                .toDraw(userId);
    }
    @GameMethod()
    public GameResult toBarracksBottom(String cardId){
        return GameResultBuilder.fromCrewCardId(this,cardId)
                .toBarracksBottom();
    }

    //BARRACKS
    @GameMethod()
    public GameResult barracksToVoid(String cardId){
        return GameResultBuilder.fromBarracks(this,cardId)
                .toVoid();
    }

    @GameMethod()
    public GameResult barracksToHand(String userId, String cardId)  {
        return GameResultBuilder.fromBarracks(this,cardId)
                .toHand(userId);
    }

    @GameMethod()
    public GameResult barracksToDiscard(String userId, String cardId) {
        return GameResultBuilder.fromBarracks(this,cardId)
                .toDiscard(userId);
    }

    @GameMethod()
    public GameResult barracksToBarracksBottom(String cardId) {
        return GameResultBuilder.fromBarracks(this,cardId)
                .toBarracksBottom();
    }

    //HQ
    @GameMethod()
    public GameResult hqToDiscard(String userId, String position) {
        return GameResultBuilder.fromHq(this,position)
                .toDiscard(userId);
    }

    @GameMethod()
    public GameResult hqToHand(String userId, String position) {
        return GameResultBuilder.fromHq(this,position)
                .toHand(userId);
    }

    @GameMethod()
    public GameResult hqToDraw(String userId, String position) {
        return GameResultBuilder.fromHq(this,position)
                .toDraw(userId);
    }

    @GameMethod()
    public GameResult hqToVoid(String position) {
        return GameResultBuilder.fromHq(this,position)
                .toVoid();
    }

    @GameMethod()
    public GameResult hqToBarracksBottom(String userId, String position) {
        return GameResultBuilder.fromHq(this,position)
                .toBarracksBottom();
    }

    @GameMethod()
    public GameResult hqToOperations(String userId, String position) {
        return GameResultBuilder.fromHq(this, position)
                .toOperations();
    }

    //HAND
    @GameMethod()
    public GameResult handShow(String userId, String cardId) {
        return GameResultBuilder.showHand(this,userId,cardId)
                .setText("shows");
    }

    @GameMethod()
    public GameResult handToDraw(String userId, String cardId) {
        return GameResultBuilder.fromHand(this,userId,cardId)
                .toDraw(userId);
    }

    @GameMethod()
    public GameResult handToDiscard(String userId, String cardId) {
        return GameResultBuilder.fromHand(this,userId,cardId)
                .toDiscard(userId);
    }

    @GameMethod()
    public GameResult handToBarracksBottom(String userId, String cardId) {
        return GameResultBuilder.fromHand(this, userId, cardId)
                .toBarracksBottom();
    }

    @GameMethod()
    public GameResult handToOperations(String userId, String cardId) {
        return GameResultBuilder.fromHand(this, userId, cardId)
                .toOperations();
    }

    @GameMethod()
    public GameResult handToVoid(String userId, String cardId) {
        return GameResultBuilder.fromHand(this, userId, cardId)
                .toVoid();
    }

    //DISCARD
    @GameMethod()
    public GameResult discardToVoid(String userId, String cardId) {
        return GameResultBuilder.fromDiscard(this, userId, cardId)
                .toVoid();
    }

    @GameMethod()
    public GameResult discardToHand(String userId, String cardId) {
        return GameResultBuilder.fromDiscard(this, userId, cardId)
                .toHand(userId);
    }

    @GameMethod()
    public GameResult discardToDraw(String userId, String cardId) {
        return GameResultBuilder.fromDiscard(this, userId, cardId)
                .toDraw(userId);
    }
    @GameMethod()
    public GameResult discardToBarracksBottom(String userId, String cardId) {
        return GameResultBuilder.fromDiscard(this, userId, cardId)
                .toBarracksBottom();
    }

    //Draw
    @GameMethod()
    public GameResult drawToVoid(String userId, String cardId){
        return GameResultBuilder.fromDraw(this, userId, cardId)
                .toVoid();
    }

    @GameMethod()
    public GameResult drawToHand(String userId, String cardId) {
        return GameResultBuilder.fromDraw(this, userId, cardId)
                .toHand(userId);
    }
    @GameMethod()
    public GameResult drawToDiscard(String userId, String cardId) {
        return GameResultBuilder.fromDraw(this, userId, cardId)
                .toDiscard(userId);
    }
    @GameMethod()
    public GameResult drawToBarracksBottom(String userId, String cardId) {
        return GameResultBuilder.fromDraw(this, userId, cardId)
                .toBarracksBottom();
    }

    //Operations
    @GameMethod()
    public GameResult operationsToVoid(String cardId){
        return GameResultBuilder.fromOperations(this, cardId)
                .toVoid();
    }

    @GameMethod()
    public GameResult operationsToHand(String userId, String cardId) {
        return GameResultBuilder.fromOperations(this, cardId)
                .toHand(userId);
    }
    @GameMethod()
    public GameResult operationsToDiscard(String userId, String cardId) {
        return GameResultBuilder.fromOperations(this, cardId)
                .toDiscard(userId);
    }


    //HIVE
    @GameMethod()
    public GameResult hiveToVoid(){
        return GameResultBuilder.fromHive(this)
                .toVoid();
    }

    @GameMethod()
    public GameResult hiveToComplex(String position){
        return GameResultBuilder.fromHive(this)
                .toComplex(position);
    }

    @GameMethod()
    public GameResult hiveToCombat(){
        return GameResultBuilder.fromHive(this)
                .toCombat();
    }

    //COMPLEX
    @GameMethod()
    public GameResult complexToVoid(String position){
        return GameResultBuilder.fromComplex(this, position)
                .toVoid();
    }

    @GameMethod()
    public GameResult complexToHive(String position){
        return GameResultBuilder.fromComplex(this, position)
                .toHiveTop();
    }

    @GameMethod()
    public GameResult complexToAttachment(String from, String to){
        GameResultBuilder gameResultBuilder = GameResultBuilder.fromComplex(this, from);
        Location location =  gameResultBuilder.getLocationAt(from);
        if(location.isHidden()){
            return gameResultBuilder.toComplex(from).setText("unrevealed complex can not be attached");
        }
        String  targetCardId = gameResultBuilder.getLocationAt(to).getAttachment();
        if(targetCardId != null || !targetCardId.equals("")){
            GameResultBuilder.fromHiveCardId(this,targetCardId,false)
                    .toComplex(from);
        }
        return gameResultBuilder.toAttachment(to);
    }

    @GameMethod()
    public GameResult complexToComplex(String from, String to){
        GameResultBuilder gameResultBuilder = GameResultBuilder.fromComplex(this,from);
        Location location = gameResultBuilder.getLocationAt(to);
        String  targetCardId = location.getGround();

        if(targetCardId != null || !targetCardId.equals("")){
            GameResultBuilder.fromHiveCardId(this,targetCardId,location.isHidden())
                    .toComplex(from);
        }
        return gameResultBuilder.toComplex(to);
    }
    @GameMethod()
    public GameResult complexToCombat(String from){
        GameResultBuilder gameResultBuilder = GameResultBuilder.fromComplex(this, from);
        Location location = gameResultBuilder.getLocationAt(from);
        if(location.isHidden()){
            return gameResultBuilder.toComplex(from).setText("unrevealed complex can not enter combat");
        }
         return gameResultBuilder.toCombat();
    }

    @GameMethod()
    public GameResult complexToDiscard(String userId, String from){
        GameResultBuilder gameResultBuilder = GameResultBuilder.fromComplex(this, from);
        String cardId = gameResultBuilder.getCardId();
        if(gameDecks.isHiveCardAlsoCrewCard(cardId)){
            return gameResultBuilder.toDiscard(userId);
        }else{
            return gameResultBuilder.toComplex(from).setText("Card is no crew member");
        }
    }

    @GameMethod()
    public GameResult revealComplex(String from){
        GameResultBuilder gameResultBuilder = GameResultBuilder.fromComplex(this, from);
        Location location =  gameResultBuilder.getLocationAt(from);
        location.setHidden(false);
        String cardId = location.getGround();
        //TODO do cardAction here e.g. facehugger, event, hazard etc...
        return gameResultBuilder.toComplex(from)
                .setText("reveal")
                .addImageId("/"+NAME+"/"+DIRECTORY_HIVE+"/"+cardId);
    }


    // COMBAT
    @GameMethod()
    public GameResult combatToVoid(String cardId){
        return GameResultBuilder.fromCombat(this, cardId)
                .toVoid();
    }
    @GameMethod()
    public GameResult combatToComplex(String cardId, String to){
        GameResultBuilder gameResultBuilder = GameResultBuilder.fromCombat(this, cardId);
        Location location =  gameResultBuilder.getLocationAt(to);
        if(location.getGround() != null && !location.getGround().equals("")){
            if(location.isHidden()){
                return gameResultBuilder.toCombat().setText("unrevealed complex can not enter combat");
            }else{
                GameResultBuilder.fromComplex(this, to).toCombat();
            }
        }
        return gameResultBuilder.toComplex(to);
    }



    //USER
    @GameMethod()
    public GameResult heal(String userId, String cardId) throws IOException {
        User user = getUser(userId);
        if(user.heal(cardId)){
            strikes.discardCard(cardId);
        }
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_STRIKE)
                .setText(user.getUserInfo().get(USER_NAME)+" heals")
                .addImageId("/"+NAME+"/"+DIRECTORY_STRIKES+"/"+cardId)
                .set(MAP_KEY_USER_ID,userId);
    }

    @GameMethod()
    public GameResult strike(String userId) throws IOException {
        User user = getUser(userId);
        String cardId;
        try {
            cardId = strikes.drawCard();
        }catch(Exception e){
            strikes.shuffleDiscard();
            cardId =  strikes.drawCard();
        }
        user.strike(cardId);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_STRIKE)
                .setText(user.getUserInfo().get(USER_NAME)+" gets")
                .addImageId("/"+NAME+"/"+DIRECTORY_STRIKES+"/"+cardId)
                .set(MAP_KEY_USER_ID,userId);
    }

    @GameMethod()
    public GameResult endTurn(String userId) throws IOException {
        User user = getUser(userId);
        user.refreshHand();
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_DISCARD)
                .addEvent(EVENT_REFRESH_USER_HAND)
                .setText(user.getUserInfo().get(USER_NAME)+" ends Turn" )
                .set(MAP_KEY_USER_ID,userId);
    }

    @GameMethod()
    public GameResult draw(String userId) throws IOException {
        User user = getUser(userId);
        String cardId = getUser(userId).draw();
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_HAND)
                .addEvent(EVENT_REFRESH_USER_DRAW)
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId)
                .setText(user.getUserInfo().get(USER_NAME)+" draws 1 Card")
                .set(MAP_KEY_USER_ID,userId);
    }


    //GET_INFORMATION
    @GameMethod()
    public GameResult getHq(String amount) {
        GameResult gameResult = new GameResult();
        List<String> hqList = hq.stream()
                .map(cardId -> {
                    String card = "";
                    if(!cardId.equals("")){
                        card = "/" + NAME + "/" + DIRECTORY_CREW + "/" + cardId;
                    }
                    gameResult.addImageId(card);
                    return card;
                })
                .collect(Collectors.toList());
        ArrayList<String> sergeantList = new ArrayList();
        String cardId = "";
        if(!sergeant.isEmpty()){
            cardId = "/"+NAME+"/"+DIRECTORY_CREW+"/"+sergeant.showDrawCard();
        }
        sergeantList.add(cardId);
        return gameResult
                .setText("request headquarter")
                .set(MAP_KEY_SERGEANT,sergeantList)
                .set(MAP_KEY_HQ,hqList);
    }

    @GameMethod()
    public GameResult getBarracks(String amount) {
        GameResult gameResult = new GameResult();
        int limit = getLimit(amount, barracks);
        List<String> barrackList = barracks.getDrawPile().stream()
                .map(card -> "/" + NAME + "/" + DIRECTORY_CREW + "/" + card)
                .collect(Collectors.toList());
        barrackList.stream().limit(limit)
                .forEach(card -> gameResult.addImageId(card));
        return gameResult
                .set(MAP_KEY_BARRACKS,barrackList)
                .setText("request " + amount + " Barracks");
    }

    @GameMethod()
    public GameResult getOperationsInfo() {
        GameResult gameResult = new GameResult();
        List<String> operationsList = operations.stream()
                .map(card -> {
                    String cardId = "/" + NAME + "/" + DIRECTORY_CREW + "/" + card;
                    gameResult.addImageId(cardId);
                    return cardId;
                } )
                .collect(Collectors.toList());
        return gameResult
                .set(MAP_KEY_OPERATIONS,operationsList)
                .setText("request Operations Info");
    }

    @GameMethod()
    public GameResult getUserInfo(String targetUserId) throws IOException {
        Map<String,Map<String,String>> userMap= new HashMap();
        users.entrySet().forEach(entry->{
            userMap.put(entry.getKey(),entry.getValue().getUserInfo());
        });
        String charName = userMap.get(targetUserId).get(USER_CHAR);
        return new GameResult()
                .setText(charName)
                .addImageId("/"+NAME+"/"+DIRECTORY_CHAR+"/"+charName+"-char.png")
                .set(MAP_KEY_USER_MAP,userMap);
    }

    @GameMethod()
    public GameResult getUserStrikes(String targetUserId) {
        GameResult gameResult = new GameResult().setText("strikes");
        getUser(targetUserId).getStrikes().forEach(cardId->
                gameResult.addImageId("/"+NAME+"/"+DIRECTORY_STRIKES+"/"+cardId));
        return gameResult.set(MAP_KEY_USER_ID,targetUserId);
    }

    @GameMethod()
    public GameResult getUserHand(String targetUserId) {
        GameResult gameResult = new GameResult().setText("hand");
        getUser(targetUserId).getHand().forEach(cardId->
                gameResult.addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId));
        return gameResult.set(MAP_KEY_USER_ID,targetUserId);
    }

    @GameMethod()
    public GameResult getUserDiscard(String targetUserId) {
        GameResult gameResult = new GameResult().setText("discardPile");
        getUser(targetUserId).getDiscardPile().forEach(cardId->
                gameResult.addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId));
        return gameResult.set(MAP_KEY_USER_ID,targetUserId);
    }

    @GameMethod()
    public GameResult getUserDraw(String targetUserId) {
        GameResult gameResult = new GameResult().setText("drawPile");
        getUser(targetUserId).getDrawPile().forEach(cardId->
                gameResult.addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId));
        return gameResult.set(MAP_KEY_USER_ID,targetUserId);
    }

    @GameMethod()
    public GameResult getGameInfo() {
        return new GameResult()
                .addImageId("/" + NAME + "/" + DIRECTORY_MISSION + "/" + mission + "-location.png")
                .setText("Mission: " + mission)
                .set(MAP_KEY_OBJECTIVE,objective)
                .set(MAP_KEY_MISSION, mission.name().toLowerCase());
    }


    private int getLimit(String amount, Deck deck) {
        if (amount.equals("all")) {
            return deck.getDrawPile().size();
        } else {
            return Integer.valueOf(amount);
        }
    }


    void fillHq(int position){
        try{
            try{
                hq.set(position, barracks.drawCard());
            }catch(Exception e){
                hq.add(position,barracks.drawCard());
            }
        }catch(Exception e){
            hq.set(position,"");
        }
    }

}
