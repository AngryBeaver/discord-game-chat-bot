package net.verplanmich.bot.game.alienencounter;

import net.verplanmich.bot.game.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.verplanmich.bot.game.alienencounter.User.USER_CHAR;
import static net.verplanmich.bot.game.alienencounter.User.USER_NAME;

@Component
@Scope("prototype")
public class Alienencounter implements Game {

    static final String NAME = "alienencounter";
    static final String DIRECTORY_CREW = "crew";
    static final String DIRECTORY_MISSION = "mission";
    static final String DIRECTORY_CHAR = "char";
    static final String DIRECTORY_STRIKES = "strikes";

    static final String EVENT_START = "start";
    static final String EVENT_REFRESH_HQ = "hq";
    static final String EVENT_REFRESH_BARRACKS = "barracks";
    static final String EVENT_REFRESH_USER_HAND = "hand";
    static final String EVENT_REFRESH_USER_INFO = "user";
    static final String EVENT_REFRESH_GAME = "game";
    static final String EVENT_REFRESH_USER_STRIKE = "strike";
    static final String EVENT_REFRESH_USER_DRAW = "draw";
    static final String EVENT_REFRESH_USER_DISCARD = "discard";
    static final String EVENT_INFO = "info";


    static final String MAP_KEY_USER_ID = "userId";
    static final String MAP_KEY_MISSION = "mission";
    static final String MAP_KEY_OBJECTIVE = "objective";
    static final String MAP_KEY_CARD_ID = "cardId";
    static final String MAP_KEY_HQ = "hq";
    static final String MAP_KEY_BARRACKS = "barracks";
    static final String MAP_KEY_USER_MAP = "userMap";
    static final String MAP_KEY_SERGEANT = "sergeant";


    private GameDecks gameDecks;

    private Mission mission;
    private List<String> headQuarter;
    private Map<String, User> users = new HashMap();
    private Deck chars;
    private Deck strikes;
    private Deck sergeant;
    private Deck barracksDeck;
    private Deck hive;
    private String objective;
    private List<String> complex;

    @Autowired
    public Alienencounter(GameDecks gameDecks){
        this.gameDecks = gameDecks;
        chars = gameDecks.getChars();
        strikes = gameDecks.getStrikes();
    }

    public String getName() {
        return NAME;
    }

    public User getUser(String userId)  {
        return users.get(userId);
    }

    @GameMethod()
    public GameResult join(GameData gameData) throws IOException {
        if(!users.containsKey(gameData.getUserId())) {
            String charName = chars.drawCard();
            users.put(gameData.getUserId(), new User(gameData.getUserName(),gameData.getUserId(),charName));
            return new GameResult()
                    .setText(gameData.getUserName() + " joined the game")
                    .addImageId("/"+NAME+"/"+DIRECTORY_CHAR+"/"+charName+"-char.png")
                    .addEvent(EVENT_INFO)
                    .addEvent(EVENT_REFRESH_USER_INFO);
        }
        return new GameResult()
                .setText(gameData.getUserName() + "is highly interested in this game");
    }

    @GameMethod()
    public GameResult start(String mission) {
        this.mission = Mission.valueOf(mission.toUpperCase());
        barracksDeck = gameDecks.barracksFor(this.mission);
        headQuarter = new ArrayList();
        strikes = gameDecks.getStrikes();
        sergeant = gameDecks.getSergeant();
        fillHq(0);
        fillHq(1);
        fillHq(2);
        fillHq(3);
        fillHq(4);
        objective = "1";
        hive = gameDecks.getAliens(this.mission,users.size());
        complex = new ArrayList();
        return getGameInfo()
                .addEvent(EVENT_START);
    }

    @GameMethod()
    public GameResult complexAdd(){
        return new GameResult();
    }

    @GameMethod()
    public GameResult barracksToHand(String userId, String cardId) throws IOException {
        if(barracksDeck.fromDrawPile(cardId)){
            getUser(userId).toHand(cardId);
        }
        return new GameResult()
                .setText("barracks to hand")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_BARRACKS)
                .addEvent(EVENT_REFRESH_USER_HAND);
    }

    @GameMethod()
    public GameResult barracksToDiscard(String userId, String cardId) throws IOException {
        if(barracksDeck.fromDrawPile(cardId)){
            getUser(userId).toDiscard(cardId);
        }
        return new GameResult()
                .setText("barracks to hand")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_BARRACKS)
                .addEvent(EVENT_REFRESH_USER_DISCARD);
    }

    @GameMethod()
    public GameResult barracksDown(String userId, String cardId) throws IOException {
        if(barracksDeck.fromDrawPile(cardId)){
            barracksDeck.toDrawPileBottom(cardId);
        }
        return new GameResult()
                .setText("barracks to bottom")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId)
                .addEvent(EVENT_REFRESH_BARRACKS)
                .addEvent(EVENT_INFO);
    }

    @GameMethod()
    public GameResult barracksKill(String userId, String cardId) throws IOException {
        barracksDeck.fromDrawPile(cardId);
        return new GameResult()
                .setText("kills")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_BARRACKS);
    }


    @GameMethod()
    public GameResult hqToDiscard(String userId, String position) throws IOException {
        GameResult gameResult = hqCardToUser(position);
        getUser(userId).toDiscard((String)gameResult.get(MAP_KEY_CARD_ID));
        return gameResult.setText("recruit")
                .addEvent(EVENT_REFRESH_USER_DISCARD)
                .addEvent(EVENT_INFO);
    }

    @GameMethod()
    public GameResult hqToHand(String userId, String position) throws IOException {
        GameResult gameResult = hqCardToUser(position);
        getUser(userId).toHand((String)gameResult.get(MAP_KEY_CARD_ID));
        return gameResult
                .setText("hq to hand")
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_HAND);
    }

    @GameMethod()
    public GameResult hqToDraw(String userId, String position) throws IOException {
        GameResult gameResult = hqCardToUser(position);
        getUser(userId).toDraw((String)gameResult.get(MAP_KEY_CARD_ID));
        return gameResult
                .setText("hq to draw")
                .addEvent(EVENT_REFRESH_USER_DRAW)
                .addEvent(EVENT_INFO);
    }

    @GameMethod()
    public GameResult hqKill(String position) throws IOException {
        GameResult gameResult = getHqCardAt(position);
        return gameResult
                .setText("killed in hq")
                .addEvent(EVENT_INFO);
    }

    @GameMethod()
    public GameResult hqToBarracksDown(String position) throws IOException {
        GameResult gameResult = getHqCardAt(position);
        String cardId =  (String) gameResult.get(MAP_KEY_CARD_ID);
        barracksDeck.toDrawPileBottom(cardId);
        return gameResult
                .addEvent(EVENT_REFRESH_BARRACKS)
                .setText("hq to barracks"+ cardId)
                .addEvent(EVENT_INFO);
    }

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
                .addImageId("/"+NAME+"/"+DIRECTORY_STRIKES+"/"+cardId);
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
                .addImageId("/"+NAME+"/"+DIRECTORY_STRIKES+"/"+cardId);
    }

    @GameMethod()
    public GameResult endTurn(String userId) throws IOException {
        User user = getUser(userId);
        user.refreshHand();
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_DISCARD)
                .addEvent(EVENT_REFRESH_USER_HAND)
                .setText(user.getUserInfo().get(USER_NAME)+" ends Turn" );
    }

    @GameMethod()
    public GameResult handShow(String userId, String cardId) throws IOException {
        validateHandCard(userId,cardId);
        User user = getUser(userId);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .setText(user.getUserInfo().get(USER_NAME)+" shows")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }

    @GameMethod()
    public GameResult handToDiscard(String userId, String cardId) throws IOException {
        validateHandCard(userId,cardId);
        User user = getUser(userId);
        user.discard(cardId);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_HAND)
                .addEvent(EVENT_REFRESH_USER_DISCARD)
                .setText(user.getUserInfo().get(USER_NAME)+ " discards")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }

    @GameMethod()
    public GameResult handToBarracksUp(String userId, String cardId) throws IOException {
        validateHandCard(userId,cardId);
        User user = getUser(userId);
        user.fromHand(cardId);
        barracksDeck.toDrawPileTop(cardId);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_HAND)
                .addEvent(EVENT_REFRESH_BARRACKS)
                .setText(user.getUserInfo().get(USER_NAME)+ " to barracks")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }

    @GameMethod()
    public GameResult handKill(String userId, String cardId) throws IOException {
        validateHandCard(userId,cardId);
        User user = getUser(userId);
        user.fromHand(cardId);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_HAND)
                .setText(user.getUserInfo().get(USER_NAME)+" kills")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }

    @GameMethod()
    public GameResult discardKill(String userId, String cardId) throws IOException {
        User user = getUser(userId);
        user.fromDiscard(cardId);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_DISCARD)
                .setText(user.getUserInfo().get(USER_NAME)+" kills")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }

    @GameMethod()
    public GameResult discardToHand(String userId, String cardId) throws IOException {
        User user = getUser(userId);
        if(user.fromDiscard(cardId)) {
            user.toHand(cardId);
        }
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_DISCARD)
                .addEvent(EVENT_REFRESH_USER_HAND)
                .setText(user.getUserInfo().get(USER_NAME)+" reuses")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }

    @GameMethod()
    public GameResult drawKill(String userId, String cardId) throws IOException {
        User user = getUser(userId);
        user.fromDraw(cardId);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_DRAW)
                .setText(user.getUserInfo().get(USER_NAME)+" kills")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }

    @GameMethod()
    public GameResult drawToHand(String userId, String cardId) throws IOException {
        User user = getUser(userId);
        if(user.fromDraw(cardId)){
            user.toHand(cardId);
        }
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_DRAW)
                .addEvent(EVENT_REFRESH_USER_HAND)
                .setText(user.getUserInfo().get(USER_NAME)+" reuses")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }
    @GameMethod()
    public GameResult drawToDiscard(String userId, String cardId) throws IOException {
        User user = getUser(userId);
        if(user.fromDraw(cardId)) {
            user.discard(cardId);
        }
        return new GameResult()
                .setText(user.getUserInfo().get(USER_NAME)+" discards")
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }


    @GameMethod()
    public GameResult draw(String userId) throws IOException {
        User user = getUser(userId);
        String cardId = getUser(userId).draw();
        return new GameResult()
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_REFRESH_USER_HAND)
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId)
                .setText(user.getUserInfo().get(USER_NAME)+" draws 1 Card");
    }

    @GameMethod()
    public GameResult getHq(String amount) {
        GameResult gameResult = new GameResult();
        List<String> hq = headQuarter.stream()
                .map(cardId -> {
                    String card = "/" + NAME + "/" + DIRECTORY_CREW + "/" + cardId;
                    gameResult.addImageId(card);
                    return card;
                })
                .collect(Collectors.toList());
        ArrayList<String> sergeantList = new ArrayList();
        if(!sergeant.isEmpty()){
            sergeantList.add("/"+NAME+"/"+DIRECTORY_CREW+"/"+sergeant.showDrawCard());
        }
        return gameResult
                .setText("request headquarter")
                .set(MAP_KEY_SERGEANT,sergeantList)
                .set(MAP_KEY_HQ,hq);
    }

    @GameMethod()
    public GameResult getBarracks(String amount) {
        GameResult gameResult = new GameResult();
        int limit = getLimit(amount, barracksDeck);
        List<String> barracks = barracksDeck.getDrawPile().stream()
                .map(card -> "/" + NAME + "/" + DIRECTORY_CREW + "/" + card)
                .collect(Collectors.toList());
        barracks.stream().limit(limit)
                .forEach(card -> gameResult.addImageId(card));
        return gameResult
                .set(MAP_KEY_BARRACKS,barracks)
                .setText("request " + amount + " Barracks");
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

    private void validateHandCard(String userId, String cardId) throws IOException {
        if(!getUser(userId).getHand().contains(cardId)){
            throw new GameResultException(new GameResult()
                    .setText("Card not found")
                    .addEvent(EVENT_REFRESH_USER_HAND));
        }
    }

    private int getLimit(String amount, Deck deck) {
        if (amount.equals("all")) {
            return deck.getDrawPile().size();
        } else {
            return Integer.valueOf(amount);
        }
    }

    private GameResult getHqCardAt(String position){
        String cardId;
        if(position.equals("-1")){
            cardId = sergeant.drawCard();
        }else {
            int pos = Integer.valueOf(position);
            cardId = headQuarter.get(pos);
            if(cardId == null) {
                throw new GameResultException(new GameResult().setText("not found in hq"));
            }
            fillHq(pos);
        }
        if(cardId == null) {
            throw new GameResultException(new GameResult().setText("not found in hq"));
        }
        return new GameResult()
            .set(MAP_KEY_CARD_ID,cardId)
            .addEvent(EVENT_REFRESH_HQ)
            .addEvent(EVENT_REFRESH_BARRACKS)
            .addImageId("/" + NAME + "/" + DIRECTORY_CREW + "/" + cardId);
    }

    private GameResult hqCardToUser(String position) {
        GameResult gameResult = getHqCardAt(position);
        String cardId = (String) gameResult.get(MAP_KEY_CARD_ID);
        return gameResult.setText("recruit");
    }

    private void fillHq(int position){
        try{
            try{
                headQuarter.set(position, barracksDeck.drawCard());
            }catch(Exception e){
                headQuarter.add(position,barracksDeck.drawCard());
            }
        }catch(Exception e){
            headQuarter.add(position,null);
        }
    }

}
