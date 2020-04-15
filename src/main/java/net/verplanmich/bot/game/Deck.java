package net.verplanmich.bot.game;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Deck {

    private List<String> availableCards;
    private List<String> drawPile = new ArrayList();
    private List<String> discardPile = new ArrayList();

    public Deck(List availableCards) {
        this.availableCards = availableCards;
        initialize();
    }

    public static Deck getFor(String gameName, String deckName) throws IOException {
        try (
                InputStream inputStream = Deck.class.getClassLoader().getResourceAsStream(gameName + "." + deckName + ".list");
                Scanner sc = new Scanner(inputStream, StandardCharsets.UTF_8.name())
        ) {
            List<String> cards = new ArrayList();
            while (sc.hasNext()) {
                List moreCards = Arrays.asList(sc.nextLine().split(","));
                cards.addAll(moreCards);
            }
            List<String> cardIds = cards.stream().map(s-> Paths.get(s).normalize().toString()).collect(Collectors.toList());
            return new Deck(cardIds);
        }
    }

    public Deck initialize() {
        discardPile = new ArrayList();
        drawPile = new ArrayList(availableCards);
        return this;
    }

    public Deck shuffle(){
        Collections.shuffle(drawPile);
        return this;
    }

    public String showDrawCard() {
        return drawPile.get(0);
    }

    public String drawCard() {
        String cardId = showDrawCard();
        drawPile.remove(0);
        return cardId;
    }

    public void toDrawPileTop(String cardId) {
        drawPile.add(0,cardId);
    }

    public void toDrawPileBottom(String cardId) {
        drawPile.add(cardId);
    }

    public boolean fromDrawPile(String cardId){
        boolean result = drawPile.contains(cardId);
        drawPile.remove(cardId);
        return result;
    }

    public String showDiscardCard() {
        return discardPile.get(discardPile.size() - 1);
    }

    public void discardCard(String cardId) {
        discardPile.add(cardId);
    }

    public boolean fromDiscardPile(String cardId){
        boolean result = discardPile.contains(cardId);
        discardPile.remove(cardId);
        return result;
    }

    public void shuffleDiscard() {
        drawPile = new ArrayList(discardPile);
        discardPile = new ArrayList();
        Collections.shuffle(drawPile);
    }

    public boolean isEmpty() {
        return drawPile.isEmpty();
    }

    public List<String> getDrawPile() {
        return new ArrayList<>(drawPile);
    }

    public List<String> getDiscardPile() {
        return new ArrayList<>(discardPile);
    }
}
