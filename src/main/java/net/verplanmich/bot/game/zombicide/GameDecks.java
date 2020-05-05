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

    private final List<String> ultrared = Arrays.asList("911-special","betty","dougs-dream","flaming-nailbat","jack-and-jill","neds-atomic-flashlight","nico-special","sweet-sisters","the-holy-pan","the-reaper",
            "the-zombicider","ubser-shield","zantetsuken");


    private Deck base;
    private List<String> baseStarting = Arrays.asList("fire-axe", "crowbar", "pistol", "pan", "pan", "pan", "pan");
    private static List<Survivor> charList = Arrays.asList(
            new Survivor("adam", MELEE_DIE, ACTION, COMBAT_DIE, WEBBING, COMBAT_ACTION, COMBAT_RESULT, HOARD, MELEE_DIE, MELEE_ACTION, COMBAT_DIE, REGENERATION, COMBAT_RESULT, HOARD, ZOMBIE_LINK),
            new Survivor("amy", MOVE_ACTION,ACTION,COMBAT_ACTION,MOVE_ACTION,COMBAT_DIE,COMBAT_RESULT,MEDIC,MOVE_ACTION,RANGED_ACTION,MOVE_ACTION,ROTTEN,COMBAT_DIE,LOW_PROFILE,MEDIC),
            new Survivor("bear", SHOVE,ACTION,MELEE_ACTION,MELEE_RESULT,MELEE_DAMAGE,COMBAT_DIE,LIFESAVER,SHOVE,MELEE_ACTION,MELEE_RESULT,BARBARIAN,MELEE_DAMAGE,MELEE_BLOODLUST,LIFESAVER),
            new Survivor("belle",MOVE_ACTION,ACTION,MELEE_ACTION,RANGED_RESULT,COMBAT_DIE,MOVE_ACTION,AMBIDEXTROUS,MOVE_ACTION,RANGED_ACTION,RANGED_RESULT,ZOMBIE_LINK,COMBAT_DIE,AMBIDEXTROUS,REGENERATION),
            new Survivor("cathy",SPRINT,ACTION,MELEE_ACTION,JUMP,COMBAT_RESULT,COMBAT_BLOODLUST,LIFESAVER,SPRINT,MELEE_ACTION,JUMP,MELEE_REAPER,COMBAT_RESULT,COMBAT_BLOODLUST,DREADNOUGHT_WALKER),
            new Survivor("dan",LIFESAVER,ACTION,MOVE_ACTION,JUMP,MELEE_DAMAGE,COMBAT_ACTION,TACTICIAN,LIFESAVER,MELEE_ACTION,DREADNOUGHT_WALKER,JUMP,MELEE_DAMAGE,REGENERATION,TACTICIAN),
            new Survivor("derek",RANGE,ACTION,MELEE_ACTION,TOXIC_IMUNITY,MOVE_ACTION,COMBAT_RESULT,SLIPPERY,RANGE,MELEE_ACTION,REGENERATION,TOXIC_IMUNITY,COMBAT_RESULT,SLIPPERY,ZOMBIE_LINK),
            new Survivor("doug",MATCHING_SET,ACTION,RANGED_DIE,COMBAT_ACTION,COMBAT_RESULT,AMBIDEXTROUS,SLIPPERY,MATCHING_SET,SEARCH_ACTION,RANGED_DIE,RUNNER_COLLECTOR,COMBAT_RESULT,AMBIDEXTROUS,ZOMBIE_LINK),
            new Survivor("elsa",BREAK_IN,ACTION,RANGED_ACTION,SLIPPERY,COMBAT_ACTION,MOVE_ACTION,FAST,BREAK_IN,RANGED_ACTION,ROTTEN,SLIPPERY,COMBAT_ACTION,MOVE_ACTION,SUPER_STRENGTH)
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

    public void enableUltraRed(){
        ultrared.forEach(cardId->gear.toDrawPileTop(cardId));
        gear.shuffle();
    }


}
