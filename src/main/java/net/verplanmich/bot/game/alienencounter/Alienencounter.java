package net.verplanmich.bot.game.alienencounter;

import net.verplanmich.bot.game.Deck;
import net.verplanmich.bot.game.Game;
import net.verplanmich.bot.game.GameMethod;
import net.verplanmich.bot.game.GameResult;

import java.io.IOException;
import java.util.HashMap;

public class Alienencounter implements Game {

    static final String NAME = "alienencounter";
    static final String WEYLAND_CORPORATION = "Weyland Corporation";

    static final String CHIEF_ENGINEER_PARKER = "chiefengineerparker";
    static final String ENGINEER_BRETT = "engineerbrett";
    static final String CHIEF_ENGINEER_PARKER = "chiefengineerparker";
    static final String CHIEF_ENGINEER_PARKER = "chiefengineerparker";
    private Deck cepDeck;
    private Deck ebDeck;

    private HashMap<String,Deck> crews = new HashMap();

    public Alienencounter() throws IOException {
        crews.put(WEYLAND_CORPORATION,

        crews.put(WEYLAND_CORPORATION,Deck.getFor(NAME, CHIEF_ENGINEER_PARKER))
        cepDeck = Deck.getFor(NAME, CHIEF_ENGINEER_PARKER);
        cepDeck = Deck.getFor(NAME, CHIEF_ENGINEER_PARKER);
    }

    public String getName(){
        return NAME;
    }

    @GameMethod()
    public GameResult crew(){
        GameResult gameResult = new GameResult();
        String cardId = cepDeck.drawCard();
        gameResult.setText(cardId);
        gameResult.addImageId("/"+NAME+"/"+CHIEF_ENGINEER_PARKER+"/"+cardId);
        return gameResult;
    }


}
