package net.verplanmich.bot.games;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Zombicide implements Game {
    private Deck gear;
    private Deck spawn;
    private Map<String, List<String>> hand = new HashMap();

    public Zombicide() throws IOException {
        gear = Deck.getFor("zombicide", "equipment");
        spawn = Deck.getFor("zombicide", "spawn");
    }

    @GameMethod(type=GameMethodType.ImageList,isPrivate=true)
    public List<String> hand(String userId){
        return getHand(userId);
    }

    @GameMethod(type=GameMethodType.Image)
    public String add(String userId,String arg1) {
        getHand(userId).add(arg1);
        return arg1;
    }

    @GameMethod(type=GameMethodType.Text)
    public String drop(String userId,String arg1) {
        String message = "Card not in hand";
        if(getHand(userId).contains(arg1)){
            message = "Card dropped";
            getHand(userId).remove(arg1);
        }
        return message;
    }

    @GameMethod
    public String gear(String userId) {
        String cardId = gear.drawCard();
        getHand(userId).add(cardId);
        return cardId;
    }

    @GameMethod
    public String spawn() {
        try {
            String cardId = spawn.drawCard();
            spawn.discardCard(cardId);
            return cardId;
        }catch(Exception e){
            spawn.shuffleDiscard();
            String cardId = spawn.drawCard();
            spawn.discardCard(cardId);
            return cardId;
        }
    }

    @GameMethod
    public String show () {
        return spawn.showDiscardCard();
    }

    private List<String> getHand(String userId){
        if(hand.get(userId) == null){
            hand.put(userId,new ArrayList());
        }
        return hand.get(userId);
    }
}