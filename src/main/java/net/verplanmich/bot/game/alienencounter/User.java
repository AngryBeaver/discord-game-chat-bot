package net.verplanmich.bot.game.alienencounter;

import net.verplanmich.bot.game.Deck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.verplanmich.bot.game.alienencounter.Alienencounter.NAME;

public class User {

    private static final String STARTER = "starter";

    private final Deck deck;
    private final List<String> hand;
    private final List<String> strikes;
    private final String charName;

    public User(String charName) throws IOException {
        List<String> starterCards = Deck.getFor(NAME, STARTER).getAvailableCards();
        starterCards.add(charName+".png");
        this.charName = charName;
        hand = new ArrayList();
        strikes = new ArrayList();
        deck = new Deck(starterCards);
        draw(5);
    }

    public List<String> getStrikes(){
        return new ArrayList(strikes);
    }

    public List<String> getHand(){
        return new ArrayList(hand);
    }

    public String getCharName(){
        return charName;
    }

    public boolean discard(String cardId){
        boolean result = fromHand(cardId);
        if(result){
            deck.discardCard(cardId);
        }
        return result;
    }

    public boolean fromHand(String cardId){
        boolean result = hand.contains(cardId);
        if(result){
            hand.remove(cardId);
        }
        return result;
    }

    public void toDiscard(String cardId){
        deck.discardCard(cardId);
    }

    public void toHand(String cardId){
        hand.add(cardId);
    }

    public void toDraw(String cardId){
        deck.toDrawPileTop(cardId);
    }

    public void draw(int amount){
        if(amount > 0){
            hand.add(deck.drawCard());
            amount--;
            draw(amount);
        }
    }

}
