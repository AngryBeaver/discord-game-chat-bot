package net.verplanmich.bot.game.alienencounter;

import net.verplanmich.bot.game.Deck;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static net.verplanmich.bot.game.alienencounter.Mission.NOSTROMO;

@Service
public class GameDecks {

    static final String NAME = "alienencounter";

    static final String CHIEF_ENGINEER_PARKER = "chiefengineerparker";
    static final String ENGINEER_BRETT = "engineerbrett";
    static final String EXECUTIV_OFFICER_KANE = "executivofficerkane";
    static final String WARRANT_OFFICER_RIPLEY = "warrantofficerripley";
    static final String NAVIGATOR_LAMBERT = "navigatorlambert";
    static final String CAPTAIN_DALLAS = "captaindallas";

    static final String CHARS = "chars";
    static final String STRIKES = "strikes";
    static final String SERGEANT = "sergeant";
    static final String DRONE = "drone";

    private HashMap<Mission, List<Deck>> crews = new HashMap();
    private HashMap<Mission, List<List<String>>> missionCards = new HashMap();
    private HashMap<Integer,HashMap<Integer,Integer>> difficutly = new HashMap();

    private Deck chars;
    private Deck strikes;
    private Deck sergeant;
    private Deck drone;

    private List<String> theSos = Arrays.asList("event.png","event.png","egg.png","egg.png","egg.png","egg.png","hazard.png","game-objective1-sos.png","game-objective1-sos.png");


    public GameDecks() throws IOException {
        difficutly.put(1,new HashMap());
        difficutly.put(2,new HashMap());
        difficutly.put(3,new HashMap());
        difficutly.put(4,new HashMap());
        difficutly.put(5,new HashMap());
        difficutly.get(1).put(0,0);
        difficutly.get(1).put(1,0);
        difficutly.get(1).put(2,0);
        difficutly.get(2).put(0,0);
        difficutly.get(2).put(1,1);
        difficutly.get(2).put(2,2);
        difficutly.get(3).put(0,2);
        difficutly.get(3).put(1,3);
        difficutly.get(3).put(2,4);
        difficutly.get(4).put(0,4);
        difficutly.get(4).put(1,5);
        difficutly.get(4).put(2,6);
        difficutly.get(5).put(0,4);
        difficutly.get(5).put(1,6);
        difficutly.get(5).put(2,7);
        missionCards.put(NOSTROMO,new ArrayList());
        missionCards.get(NOSTROMO).add(theSos);

        chars = Deck.getFor(NAME,CHARS);
        strikes = Deck.getFor(NAME,STRIKES);
        sergeant = Deck.getFor(NAME,SERGEANT);
        drone = Deck.getFor(NAME,DRONE);
        crews.put(NOSTROMO,new ArrayList());
        crews.get(NOSTROMO).add(Deck.getFor(NAME, CHIEF_ENGINEER_PARKER));
        crews.get(NOSTROMO).add(Deck.getFor(NAME, ENGINEER_BRETT));
        crews.get(NOSTROMO).add(Deck.getFor(NAME, EXECUTIV_OFFICER_KANE));
        crews.get(NOSTROMO).add(Deck.getFor(NAME, WARRANT_OFFICER_RIPLEY));
        crews.get(NOSTROMO).add(Deck.getFor(NAME, NAVIGATOR_LAMBERT));
        crews.get(NOSTROMO).add(Deck.getFor(NAME, CAPTAIN_DALLAS));
    }


    private Deck getAliens(Mission mission,int players){
        Deck result = new Deck();
        Deck droneShuffled = new Deck(drone.getDrawPile());
        for(int objective= 0; objective < 3; objective ++){
            List<String> objectiveCards = missionCards.get(mission).get(objective);
            IntStream.range(0,difficutly.get(players).get(objective)).forEach(
                    i->objectiveCards.add(droneShuffled.drawCard())
            );
            Collections.shuffle(objectiveCards);
            result.toDrawPileBottom(objectiveCards);
        }
        return result;
    }

    public Deck barracksFor(Mission mission){
        List<String> barrackCards = new ArrayList();
        List<Deck> nostromoCrew = new ArrayList();
        nostromoCrew.addAll(crews.get(mission));
        Collections.shuffle(nostromoCrew);
        nostromoCrew.stream().limit(4).forEach(
            d-> barrackCards.addAll(d.getDrawPile())
        );
        return new Deck(barrackCards);
    }

    public Deck getSergeant(){
        return new Deck(sergeant.getDrawPile());
    }

    public Deck getChars(){
        return new Deck(chars.getDrawPile());
    }

    public Deck getStrikes(){
        return new Deck(strikes.getDrawPile());
    }

}
