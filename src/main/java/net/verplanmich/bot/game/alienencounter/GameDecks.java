package net.verplanmich.bot.game.alienencounter;

import net.verplanmich.bot.game.Deck;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static net.verplanmich.bot.game.alienencounter.Mission.NOSTROMO;
import static net.verplanmich.bot.game.alienencounter.Mission.SULACI;

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
    private HashMap<Mission, List<List<String>>> objectiveCards = new HashMap();
    private HashMap<Integer,HashMap<Integer,Integer>> difficutly = new HashMap();

    private List<String> hiveAndCrew = Arrays.asList("jonesy.png","newt.png");

    private Deck chars;
    private Deck strikes;
    private Deck sergeant;
    private Deck drone;

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
        objectiveCards.put(NOSTROMO,new ArrayList());
        objectiveCards.get(NOSTROMO).add( Arrays.asList("event.png","event.png","egg.png","egg.png","egg.png","egg.png","hazard.png","game-objective1-sos.png","game-objective1-sos.png"));
        objectiveCards.get(NOSTROMO).add( Arrays.asList("jonesy.png","event.png","event.png","objective2-sos.png","hazard.png","shedding-skin-xenomorph.png","shedding-skin-xenomorph.png","shedding-skin-xenomorph.png","skittering-xenomorph.png","skittering-xenomorph.png","skittering-xenomorph.png"));
        objectiveCards.get(NOSTROMO).add( Arrays.asList("the-perfect-organism.png","event.png","event.png","hazard.png","xenomorph-lance-tail.png","xenomorph-lance-tail.png","xenomorph-lance-tail.png","twin-mouth-xenomorph.png","twin-mouth-xenomorph.png","twin-mouth-xenomorph.png","twin-mouth-xenomorph.png","ash.png","objective3-sos.png"));
        objectiveCards.put(SULACI, new ArrayList<>());
        objectiveCards.get(SULACI).add( Arrays.asList("xenomorph-swarmer.png","xenomorph-swarmer.png","xenomorph-swarmer.png","hazard.png","event","event","colonist-host.png","colonist-host.png","colonist-host.png"));
        objectiveCards.get(SULACI).add( Arrays.asList("hazard.png","event.png","event.png","acid-blood-xenomorph.png","acid-blood-xenomorph.png","acid-blood-xenomorph.png","facehugger.png","facehugger.png","newt.png","sentry-guns.png","sentry-guns.png"));
        objectiveCards.get(SULACI).add( Arrays.asList("hazard.png","event.png","event.png","howling-xenomorph.png","howling-xenomorph.png","howling-xenomorph.png","howling-xenomorph.png","xenomorph-snatcher.png","xenomorph-snatcher.png","xenomorph-snatcher.png","xenomorph-snatcher.png","carter-burke.png","the-queen.png"));

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
        crews.put(SULACI,new ArrayList());
        crews.get(SULACI).add(new Deck(Arrays.asList("shes-here-as-a-consultant.png","shes-here-as-a-consultant.png","shes-here-as-a-consultant.png","shes-here-as-a-consultant.png","shes-here-as-a-consultant.png","power-loader.png","i-will-never-leave-you.png","i-will-never-leave-you.png","i-will-never-leave-you.png","i-will-never-leave-you.png","i-will-never-leave-you.png","ripley-enraged.png","ripley-enraged.png","ripley-enraged.png")));
        crews.get(SULACI).add(new Deck(Arrays.asList("stay-frosty.png","stay-frosty.png","stay-frosty.png","stay-frosty.png","stay-frosty.png","close-encounters.png","close-encounters.png","close-encounters.png","close-encounters.png","close-encounters.png","lets-go-marines.png","weapon-training.png","weapon-training.png","weapon-training.png")));
        crews.get(SULACI).add(new Deck(Arrays.asList("split-in-two.png","knife-games.png","knife-games.png","knife-games.png","critical-analysis.png","critical-analysis.png","critical-analysis.png","critical-analysis.png","critical-analysis.png","get-him-to-medical.png","get-him-to-medical.png","get-him-to-medical.png","get-him-to-medical.png","get-him-to-medical.png")));
        crews.get(SULACI).add(new Deck(Arrays.asList("they-are-all-around-us.png","they-are-all-around-us.png","they-are-all-around-us.png","they-are-all-around-us.png","they-are-all-around-us.png","this-aint-happening-man.png","body-armor.png","body-armor.png","body-armor.png","you-want-some-of-this.png","you-want-some-of-this.png","you-want-some-of-this.png","you-want-some-of-this.png","you-want-some-of-this.png")));
        crews.get(SULACI).add(new Deck(Arrays.asList("wont-give-up.png","wont-give-up.png","wont-give-up.png","by-the-numbers.png","by-the-numbers.png","by-the-numbers.png","by-the-numbers.png","by-the-numbers.png","fall-back.png","fall-back.png","fall-back.png","fall-back.png","fall-back.png","always-were-an-a-hole.png")));
        crews.get(SULACI).add(new Deck(Arrays.asList("head-mounted-sight.png","head-mounted-sight.png","head-mounted-sight.png","head-mounted-sight.png","head-mounted-sight.png","m56-smartgun.png","m56-smartgun.png","m56-smartgun.png","smartgunners.png","smartgunners.png","smartgunners.png","smartgunners.png","smartgunners.png","they-aint-paying-us-enough.png")));
    }



    public Deck getHive(Mission mission, int players){
        List<String> hiveCards = new ArrayList();
        Deck droneShuffled = new Deck(drone.getDrawPile()).shuffle();
        for(int objective= 0; objective < 3; objective ++){
            List<String> cards = new ArrayList(objectiveCards.get(mission).get(objective));
            IntStream.range(0,difficutly.get(players).get(objective)).forEach(
                    i->cards.add(droneShuffled.drawCard())
            );
            Collections.shuffle(cards);
            hiveCards.addAll(cards);
        }
        return new Deck(hiveCards);
    }

    public Deck barracksFor(Mission mission){
        List<String> barrackCards = new ArrayList();
        List<Deck> nostromoCrew = new ArrayList();
        nostromoCrew.addAll(crews.get(mission));
        Collections.shuffle(nostromoCrew);
        nostromoCrew.stream().limit(4).forEach(
            d-> barrackCards.addAll(d.getDrawPile())
        );
        return new Deck(barrackCards).shuffle();
    }

    public boolean isHiveCardAlsoCrewCard(String cardId) {
        return hiveAndCrew.contains(cardId);
    }

    public Deck getSergeant(){
        return new Deck(sergeant.getDrawPile()).shuffle();
    }

    public Deck getChars(){
        return new Deck(chars.getDrawPile()).shuffle();
    }

    public Deck getStrikes(){
        return new Deck(strikes.getDrawPile()).shuffle();
    }

}
