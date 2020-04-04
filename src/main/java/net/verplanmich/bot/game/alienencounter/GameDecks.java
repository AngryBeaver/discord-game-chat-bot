package net.verplanmich.bot.game.alienencounter;

import net.verplanmich.bot.game.Deck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameDecks {

    static final String NAME = "alienencounter";
    static final String WEYLAND_CORPORATION = "Weyland Corporation";

    static final String CHIEF_ENGINEER_PARKER = "chiefengineerparker";
    static final String ENGINEER_BRETT = "engineerbrett";
    static final String EXECUTIV_OFFICER_KANE = "executivofficerkane";
    static final String WARRANT_OFFICER_RIPLEY = "warrantofficerripley";
    static final String NAVIGATOR_LAMBERT = "navigatorlambert";
    static final String CAPTAIN_DALLAS = "captaindallas";

    private Deck cepDeck;
    private Deck ebDeck;

    public enum Location {
        NOSTROMO,
        SULACI,
        FURI,
        AURIGA;
    }

    private HashMap<Location, List<Deck>> crews = new HashMap();

    public GameDecks() throws IOException {
        List nostromoCrew = new ArrayList();
        crews.put(Location.NOSTROMO,nostromoCrew);
        nostromoCrew.add(Deck.getFor(NAME, CHIEF_ENGINEER_PARKER));
        nostromoCrew.add(Deck.getFor(NAME, ENGINEER_BRETT));
        nostromoCrew.add(Deck.getFor(NAME, EXECUTIV_OFFICER_KANE));
        nostromoCrew.add(Deck.getFor(NAME, WARRANT_OFFICER_RIPLEY));
        nostromoCrew.add(Deck.getFor(NAME, NAVIGATOR_LAMBERT));
        nostromoCrew.add(Deck.getFor(NAME,CAPTAIN_DALLAS));

    }

}
