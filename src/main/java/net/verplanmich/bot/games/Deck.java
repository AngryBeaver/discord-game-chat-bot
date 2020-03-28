package net.verplanmich.bot.games;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Deck {

    private List<String> availableCards;
    private List<String> drawPile = new ArrayList();
    private List<String> discardPile = new ArrayList();

    Deck(List availableCards) {
        this.availableCards = availableCards;
        initialize();
    }

    public static Deck getFor(String gamename, String deckName) throws IOException {
        try (
                InputStream inputStream = Deck.class.getClassLoader().getResourceAsStream(gamename + "." + deckName + ".list");
                Scanner sc = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            List<String> cards = new ArrayList();
            while(sc.hasNext()) {
                List moreCards = Arrays.asList(sc.nextLine().split(","));
                cards.addAll(moreCards);
            }
            return new Deck(cards);
        }
    }

    private void initialize() {
        discardPile = new ArrayList();
        drawPile = new ArrayList(availableCards);
        Collections.shuffle(drawPile);
    }

    public String showDrawCard() {
        return drawPile.get(drawPile.size() - 1);
    }

    public String showDiscardCard() {
        return discardPile.get(discardPile.size() - 1);
    }

    public String drawCard() {
        String cardId = showDrawCard();
        drawPile.remove(drawPile.size() - 1);
        return cardId;
    }

    public void discardCard(String cardId) {
        discardPile.add(cardId);
    }

    public void shuffleDiscard() {
        drawPile = new ArrayList(discardPile);
        discardPile = new ArrayList();
        Collections.shuffle(drawPile);
    }


}
