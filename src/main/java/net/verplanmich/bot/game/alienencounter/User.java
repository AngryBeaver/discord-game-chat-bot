package net.verplanmich.bot.game.alienencounter;

import net.verplanmich.bot.game.Deck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.verplanmich.bot.game.alienencounter.Alienencounter.NAME;

public class User {

    static final String STARTER = "starter";
    static final String USER_NAME = "userName";
    static final String USER_ID = "userId";
    static final String USER_CHAR = "userChar";

    private final Deck deck;
    private final List<String> hand;
    private final List<String> strikes;
    private final Map<String, String> user = new HashMap();

    public User(String userName, String userId, String userChar) throws IOException {
        List<String> starterCards = Deck.getFor(NAME, STARTER).getDrawPile();
        if(userChar.equals("security-chief")) {
            starterCards.add(userChar + "1");
        }
        starterCards.add(userChar);
        user.put(USER_NAME, userName);
        user.put(USER_ID, userId);
        user.put(USER_CHAR, userChar);
        hand = new ArrayList();
        strikes = new ArrayList();
        deck = new Deck(starterCards).shuffle();
        refreshHand();
    }

    public  void shuffle(){
        deck.shuffle();
    }

    public void strike(String cardId){
        strikes.add(cardId);
    }

    public boolean heal(String cardId){
        boolean result = strikes.contains(cardId);
        if (result) {
            strikes.remove(cardId);
        }
        return result;
    }


    public List<String> getStrikes() {
        return new ArrayList(strikes);
    }

    public List<String> getHand() {
        return new ArrayList(hand);
    }

    public Map<String, String> getUserInfo() {
        return user;
    }

    public List<String> getDrawPile() {
        return deck.getDrawPile();
    }

    public List<String> getDiscardPile() {
        return deck.getDiscardPile();
    }


    public boolean fromHand(String cardId) {
        boolean result = hand.contains(cardId);
        if (result) {
            hand.remove(cardId);
        }
        return result;
    }

    public boolean fromDiscard(String cardId) {
        return deck.fromDiscardPile(cardId);
    }

    public boolean fromDraw(String cardId) {
        return deck.fromDrawPile(cardId);
    }

    public void toDiscard(String cardId) {
        deck.discardCard(cardId);
    }

    public void toHand(String cardId) {
        hand.add(cardId);
    }

    public void toDraw(String cardId) {
        deck.toDrawPileTop(cardId);
    }

    public void refreshHand() {
        hand.forEach(cardId -> deck.discardCard(cardId));
        hand.clear();
        draw();
        draw();
        draw();
        draw();
        draw();
        draw();
    }

    public String draw() {
        try {
            String cardId = deck.drawCard();
            hand.add(cardId);
            return cardId;
        } catch (Exception e) {
            deck.shuffleDiscard();
            String cardId = deck.drawCard();
            hand.add(cardId);
            return cardId;
        }
    }

}
