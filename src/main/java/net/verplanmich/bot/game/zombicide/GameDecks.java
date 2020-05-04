package net.verplanmich.bot.game.zombicide;

import net.verplanmich.bot.game.Deck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.verplanmich.bot.game.zombicide.Action.*;

public class GameDecks {
    static final String NAME = "zombicide";

    static final String GEAR = "gear";
    static final String SPAWN = "spawn";


    private Deck base;
    private List<String> baseStarting = Arrays.asList("fire-axe", "crowbar", "pistol", "pan", "pan", "pan", "pan");
    private static List<Survivor> charList = Arrays.asList(
            new Survivor("adam", MELEE_DIE, ACTION, COMBAT_DIE, WEBBING, COMBAT_ACTION, COMBAT_RESULT, HOARD, MELEE_DIE, MELEE_ACTION, COMBAT_DIE, REGENERATION, COMBAT_RESULT, HOARD, ZOMBIE_LINK),
            new Survivor("amy", MOVE_ACTION,ACTION,COMBAT_ACTION,MOVE_ACTION,COMBAT_DIE,COMBAT_RESULT,MEDIC,MOVE_ACTION,RANGED_ACTION,MOVE_ACTION,ROTTEN,COMBAT_DIE,LOW_PROFILE,MEDIC),
            new Survivor("bear", SHOVE,ACTION,MELEE_ACTION,MELEE_RESULT,MELEE_DAMAGE,COMBAT_DIE,LIVESAVER,SHOVE,MELEE_ACTION,MELEE_RESULT,BARBARIAN,MELEE_DAMAGE,MELEE_BLOODLUST,LIVESAVER),
            new Survivor("belle",MOVE_ACTION,ACTION,MELEE_ACTION,RANGED_RESULT,COMBAT_DIE,MOVE_ACTION,AMBIDEXTROUS,MOVE_ACTION,RANGED_ACTION,RANGED_RESULT,ZOMBIE_LINK,COMBAT_DIE,AMBIDEXTROUS,REGENERATION)
            );


    Deck gear;
    Deck spawn;
    List<Survivor> chars = new ArrayList();
    List<String> drops = new ArrayList();


    public GameDecks() throws IOException {
        gear = Deck.getFor(NAME, GEAR).shuffle();
        base = Deck.getFor(NAME, SPAWN).shuffle();
        chars = new ArrayList(charList);
        Collections.shuffle(chars);

    }

    public void initializeGame(String extension, List<User> users) {
        spawn = new Deck(base.getDrawPile());
        drops = new ArrayList();
        List<String> startingEquipment = new ArrayList(baseStarting);

        if (extension.toLowerCase().equals("toxic")) {

        }
        if (extension.toLowerCase().equals("prison")) {

        }
        List<User> orderdUsers = new ArrayList(users);
        Collections.shuffle(orderdUsers);
        orderdUsers.forEach(user -> user.add(startingEquipment.remove(0)));

    }


}
