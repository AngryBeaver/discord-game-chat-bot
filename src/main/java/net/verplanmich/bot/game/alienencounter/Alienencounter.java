package net.verplanmich.bot.game.alienencounter;

import net.verplanmich.bot.game.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
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
    static final String EVENT_REFRESH_GAME = "game";
    static final String EVENT_REFRESH_USER_WOUND = "wound";
    static final String EVENT_INFO = "info";


    static final String MAP_KEY_USER_ID = "userId";
    static final String MAP_KEY_MISSION = "mission";
    static final String MAP_KEY_OBJECTIVE = "objective";
    static final String MAP_KEY_CARD_ID = "cardId";
    static final String CARD_IDS = "cardIds";


    private GameDecks gameDecks;

    private Mission mission;
    private Deck barracksDeck;
    private Map<String, String> headQuarter = new HashMap();
    private Map<String, User> users = new HashMap();
    private Deck chars;
    private String objective;

    @Autowired
    public Alienencounter(GameDecks gameDecks){
        this.gameDecks = gameDecks;
        chars = gameDecks.getChars();
    }

    public String getName() {
        return NAME;
    }

    public User getUser(String userId) throws IOException {
        if(users.get(userId)==null) {
            users.put(userId, new User(chars.drawCard()));
        }
        return users.get(userId);
    }

    @GameMethod()
    public GameResult start(String mission) {
        this.mission = Mission.valueOf(mission.toUpperCase());
        barracksDeck = gameDecks.barracksFor(this.mission);
        headQuarter = new HashMap();
        fillHq("1");
        fillHq("2");
        fillHq("3");
        fillHq("4");
        fillHq("5");
        objective = "1";
        return getGameInfo()
                .addEvent(EVENT_START)
                .addEvent(EVENT_REFRESH_HQ)
                .addEvent(EVENT_REFRESH_BARRACKS)
                .addEvent(EVENT_REFRESH_GAME);
    }

    @GameMethod()
    public GameResult hqToDiscard(String userId, String position) throws IOException {
        GameResult gameResult = hqCardToUser(position);
        getUser(userId).toDiscard((String)gameResult.get(MAP_KEY_CARD_ID));
        return gameResult;
    }

    @GameMethod()
    public GameResult hqToHand(String userId, String position) throws IOException {
        GameResult gameResult = hqCardToUser(position);
        getUser(userId).toHand((String)gameResult.get(MAP_KEY_CARD_ID));
        return gameResult.addEvent(EVENT_REFRESH_USER_HAND);
    }

    @GameMethod()
    public GameResult hqToDraw(String userId, String position) throws IOException {
        GameResult gameResult = hqCardToUser(position);
        getUser(userId).toDraw((String)gameResult.get(MAP_KEY_CARD_ID));
        return gameResult.addEvent(EVENT_REFRESH_USER_HAND);
    }

    @GameMethod()
    public GameResult hqKill(String position) throws IOException {
        GameResult gameResult = getHqCardAt(position);
        gameResult.setText("killed in HQ"+ gameResult.get(MAP_KEY_CARD_ID));
        return gameResult;
    }

    @GameMethod()
    public GameResult hqToBarracksBottom(String position) throws IOException {
        GameResult gameResult = getHqCardAt(position);
        String cardId =  (String) gameResult.get(MAP_KEY_CARD_ID);
        gameResult.setText("send to Barracks from HQ"+ cardId);
        barracksDeck.toDrawPileBottom(cardId);
        return gameResult.addEvent(EVENT_REFRESH_HQ);
    }

    @GameMethod()
    public GameResult handShow(String userId, String cardId) throws IOException {
        validateHandCard(userId,cardId);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .setText("shows" + cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }

    @GameMethod()
    public GameResult handToDiscard(String userId, String cardId) throws IOException {
        validateHandCard(userId,cardId);
        getUser(userId).discard(cardId);
        return new GameResult()
                .addEvent(EVENT_REFRESH_USER_HAND)
                .setText("discards" + cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }

    @GameMethod()
    public GameResult handToBarracksTop(String userId, String cardId) throws IOException {
        validateHandCard(userId,cardId);
        getUser(userId).fromHand(cardId);
        barracksDeck.toDrawPileTop(cardId);
        return new GameResult()
                .addEvent(EVENT_REFRESH_USER_HAND)
                .addEvent(EVENT_REFRESH_BARRACKS)
                .setText("discards" + cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }

    @GameMethod()
    public GameResult handToBarracksBottom(String userId, String cardId) throws IOException {
        validateHandCard(userId,cardId);
        getUser(userId).fromHand(cardId);
        barracksDeck.toDrawPileBottom(cardId);
        return new GameResult()
                .addEvent(EVENT_REFRESH_USER_HAND)
                .addEvent(EVENT_REFRESH_BARRACKS)
                .setText("discards" + cardId)
                .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId);
    }

    @GameMethod()
    public GameResult drawCard(String userId) throws IOException {
        getUser(userId).draw(1);
        return new GameResult()
                .addEvent(EVENT_REFRESH_USER_HAND)
                .setText("draws 1 Card");
    }

    @GameMethod()
    public GameResult getBarracks(String amount) {
        GameResult gameResult = new GameResult();
        int limit = getLimit(amount, barracksDeck);
        barracksDeck.getAvailableCards().stream()
                .limit(limit)
                .forEach(card -> gameResult.addImageId("/" + NAME + "/" + DIRECTORY_CREW + "/" + card));
        return gameResult
                .setText("request " + amount + " Barracks");
    }

    @GameMethod()
    public GameResult getUserInfo(String targetUserId) throws IOException {
        String charName = getUser(targetUserId).getCharName();
        return new GameResult()
                .setText(charName)
                .addImageId("/"+NAME+"/"+DIRECTORY_CHAR+"/"+charName+"-char.png");
    }

    @GameMethod()
    public GameResult getUserWounds(String targetUserId) throws IOException {
        GameResult gameResult = new GameResult().setText("wounds");
        getUser(targetUserId).getStrikes().forEach(cardId->
                gameResult.addImageId("/"+NAME+"/"+DIRECTORY_STRIKES+"/"+cardId));
        return gameResult;
    }

    @GameMethod()
    public GameResult getUserHand(String targetUserId) throws IOException {
        GameResult gameResult = new GameResult().setText("hand");
        getUser(targetUserId).getStrikes().forEach(cardId->
                gameResult.addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+cardId));
        return gameResult;
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
        if(getUser(userId).getHand().contains(cardId)){
            throw new GameResultException(new GameResult()
                    .setText("Card not found")
                    .addEvent(EVENT_REFRESH_USER_HAND));
        }
    }



    private int getLimit(String amount, Deck deck) {
        if (amount == "all") {
            return deck.getAvailableCards().size();
        } else {
            return Integer.valueOf(amount);
        }
    }

    private GameResult getHqCardAt(String position){
        GameResult gameResult = new GameResult();
        String cardId = headQuarter.get(position);
        if(cardId == null) {
            return gameResult.setText("not found in hq");
        }
        fillHq(position);
        gameResult.set(MAP_KEY_CARD_ID,cardId);
        gameResult.addEvent(EVENT_REFRESH_HQ);
        gameResult.addEvent(EVENT_REFRESH_BARRACKS);
        return gameResult.addImageId("/" + NAME + "/" + DIRECTORY_CREW + "/" + cardId);
    }

    private GameResult hqCardToUser(String position) {
        GameResult gameResult = getHqCardAt(position);
        String cardId = (String) gameResult.get(MAP_KEY_CARD_ID);
        if(cardId != null) {
            gameResult
                    .setText("recruit from HQ "+cardId);
        }
        return gameResult;
    }

    private void fillHq(String position){
        try{
            barracksDeck.drawCard();
            headQuarter.put(position,barracksDeck.drawCard());
        }catch(Exception e){
            headQuarter.put(position,null);
        }
    }

}
