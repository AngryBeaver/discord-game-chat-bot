package net.verplanmich.bot.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Zombicide implements Game {

    private static final String NAME = "zombicide";
    private static final String GEAR = "gear";
    private static final String SPAWN = "spawn";
    private Deck gear;
    private Deck spawn;
    private Map<String, List<String>> hand = new HashMap();

    public Zombicide() throws IOException {
        gear = Deck.getFor(NAME, GEAR);
        spawn = Deck.getFor(NAME, SPAWN);
    }

    @GameMethod()
    public GameResult hand(String userId){
        GameResult gameResult = new GameResult();
        getHand(userId).forEach(cardId->
                gameResult.addImageId("/"+NAME+"/"+GEAR+"/"+cardId)
        );
        return gameResult;
    }

    @GameMethod()
    public GameResult add(String userId,String cardId) {
        GameResult gameResult = new GameResult();
        getHand(userId).add(cardId);
        gameResult.setText(cardId);
        gameResult.addImageId("/"+NAME+"/"+GEAR+"/"+cardId);
        return gameResult;
    }

    @GameMethod()
    public GameResult drop(String userId,String cardId) {
        GameResult gameResult = new GameResult();
        gameResult.setText("Card not in hand");
        if(getHand(userId).contains(cardId)){
            gameResult.setText("Card dropped");
            getHand(userId).remove(cardId);
        }
        return gameResult;
    }

    @GameMethod
    public GameResult search(String userId) {
        GameResult gameResult = new GameResult();
        String cardId = gear.drawCard();
        getHand(userId).add(cardId);
        gameResult.setText(cardId);
        gameResult.addImageId("/"+NAME+"/"+GEAR+"/"+cardId);
        return gameResult;
    }

    @GameMethod
    public GameResult spawn() {
        GameResult gameResult = new GameResult();
        if(spawn.isEmpty()){
            spawn.initialize();
        }
        String cardId = spawn.drawCard();
        spawn.discardCard(cardId);
        gameResult.addImageId("/"+NAME+"/"+SPAWN+"/"+cardId);
        return gameResult;
    }

    private List<String> getHand(String userId){
        if(hand.get(userId) == null){
            hand.put(userId,new ArrayList());
        }
        return hand.get(userId);
    }
}
