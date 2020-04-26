package net.verplanmich.bot.game.clank;

import net.verplanmich.bot.game.Deck;
import net.verplanmich.bot.game.GameResult;
import net.verplanmich.bot.game.alienencounter.Mission;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.verplanmich.bot.game.alienencounter.Mission.NOSTROMO;
import static net.verplanmich.bot.game.alienencounter.Mission.SULACI;

public class GameDecks {

    static final String NAME = "clank";

    private static final String GOBLIN = "goblin";
    private static final String EXPLORER = "explorer";
    private static final String SECRET_TOME = "secret-tome";
    private static final String MERCENARY = "mercenary";
    private static final String GOLD_FISH = "goldfish";

    private List<String> baseGame = Arrays.asList("cave-troll","watcher","watcher","watcher","animated-door","animated-door","orc-grunt","orc-grunt","orc-grunt","belcher","belcher","kobold","kobold","kobold","overlord","overlord","ogre","ogre","crystal-golem","crystal-golem",
            "coin-purse","coin-purse","ladder","ladder","shrine","shrine","shrine","dragon-shrine","dragon-shrine","treasure-hunter","treasure-hunter","swagger","swagger","teleporter","teleporter",
            "dead-run","dead-run","search","search","pickaxe","sleight-of-hand","sleight-of-hand","cleric-of-the-sun","cleric-of-the-sun","master-burglar","master-burglar","archaeologist","archaeologist","tattle","tattle","wand-of-recall","wand-of-recall",
            "move-silently","move-silently","silver-spear","silver-spear","lucky-coin","lucky-coin","sneak","sneak","bracers-of-agility","bracers-of-agility","brilliance","amulet-of-vigor","underworld-dealing",
            "elven-boots","elven-cloak","scepter-of-the-ape-lord","wand-of-wind","elven-dagger","flying-carpet","boots-of-swiftness","treasure-map","the-vault",
            "singing-sword","dragons-eye","diamond","ruby","ruby","emerald","emerald","sapphire","sapphire","sapphire","wizard","monkeybot-3000","tunnel-guide","the-duke","rebel-miner","apothecary","rebel-captain",
            "kobold-merchant","mister-whiskers","the-mountain-king","dwarven-peddler","the-queen-of-hearts","gem-collector","rebel-soldier","invoker-of-the-ancients");
    private List<String> sunken = Arrays.asList("ancient-invocation","black-pearl","silver-pearl","white-pearl","white-pearl","medic","pickpocket","rebel-scholar","rebel-brawler","alchemist",
            "elven-ranger","pipe-organ","boomerang","fishing-pole","climbing-gear","burglars-boot","aspiration","short-cut","grand-plan","deep-dive","merling","merling",
            "mermaid","mermaid","shrine-of-the-mermaid","shrine-of-the-mermaid","sorcerer","eye-in-the-water","eye-in-the-water","saurian","saurian","crystal-fish","crystal-fish");

    List<String> minorSecrets = new ArrayList(Arrays.asList("treasure","treasure","treasure","treasure","egg","egg","egg","potion-of-swiftness","potion-of-swiftness","magic-spring","potion-of-strength","potion-of-strength","potion-of-healing","potion-of-healing","potion-of-healing","skill-boost","skill-boost"));
    List<String> secrets = new ArrayList(Arrays.asList("greater-skill-boost","greater-skill-boost","chalice","chalice","chalice","flash-of-brilliance","flash-of-brilliance","potion-of-heroism","potion-of-greater-healing","potion-of-greater-healing","greater-treasure","greater-treasure"));
    private List<String> artifacts = new ArrayList(Arrays.asList("artifact-5","artifact-7","artifact-10","artifact-15","artifact-20","artifact-30","artifact-25"));
    private List<String> monkeys = new ArrayList(Arrays.asList("monkey-idol1","monkey-idol2","monkey-idol3"));
    private List<String> market = new ArrayList(Arrays.asList("crown-10","crown-9","crown-8","backpack","backpack","key","key","scuba","scuba"));


    private List<User> clanks;
    private List<User> clankArea;
    private Deck gameDeck;
    private Map<String,Integer> reserve = new HashMap();
    private List<String> dungeonRow = new ArrayList();
    User black;

    public GameDecks(){
        this.clanks = new ArrayList();
        black = new User("blank", "0","black");
        IntStream.range(1,24).forEach(i->
                clanks.add(black)
        );
        Collections.shuffle(minorSecrets);
        Collections.shuffle(secrets);
        this.clankArea = new ArrayList();
    }

    public void initializeBaseGame(String extension){
        List<String> gameCards = new ArrayList(baseGame);
        if(extension.toLowerCase().equals("sunken")){
            gameCards.addAll(new ArrayList(sunken));
            reserve.put(GOLD_FISH,100);
            refillDungeonRow(GOLD_FISH);
        }
        this.gameDeck = new Deck(gameCards).shuffle();
        reserve.put(GOBLIN,100);
        reserve.put(EXPLORER,14);
        reserve.put(SECRET_TOME,9);
        reserve.put(MERCENARY,14);
        refillDungeonRow(GOBLIN);
        refillDungeonRow(EXPLORER);
        refillDungeonRow(SECRET_TOME);
        refillDungeonRow(MERCENARY);
        refillDungeonRow(null);
        refillDungeonRow(null);
        refillDungeonRow(null);
        refillDungeonRow(null);
        refillDungeonRow(null);
    }

    List<String> dragonAttack(int limit){
        clanks.addAll(clankArea);
        clankArea = new ArrayList();
        Collections.shuffle(clanks);
        List<String> result = new ArrayList();
        clanks.stream().limit(limit).forEach(
            user-> {
                user.damage(1);
                result.add(user.getChar());
            }
        );
        clanks = clanks.stream().skip(limit).collect(Collectors.toList());
        return result;
    }

    void removeClankFromArea(User user, int amount) {
        IntStream.range(0, amount).forEach((i)->
        {
            if(clankArea.remove(user)){
                user.addClankCubes(1);
            }
        });
    }

    void addClankToArea(User user, int amount) {
        user.removeClankCubes(amount);
        IntStream.range(0, amount).forEach((i) ->
                clankArea.add(user)
        );
    }

    List<String> getClankArea(){
        return clankArea.stream().map(u->u.getChar()).collect(Collectors.toList());
    }

    public List<String> getDungeonRow(){
        return dungeonRow;
    }

    public boolean itemToUser(String itemId,User user){
        boolean result = minorSecrets.remove(itemId) || secrets.remove(itemId) || artifacts.remove(itemId) || monkeys.remove(itemId) || market.remove(itemId);
        if(result){
            user.addItem(itemId);
        }
        return result;
    }

    public boolean dungeonToDiscard(String cardId,User user){
        boolean result = dungeonRow.remove(cardId);
        if(result){
            user.deck.discardCard(cardId);
            refillDungeonRow(cardId);
        }
        return result;
    }

    private void refillDungeonRow(String cardId){
        Integer count = reserve.get(cardId);
        if (count != null && count > 0){
            reserve.put(cardId, count--);
            dungeonRow.add(cardId);
        }else{
            dungeonRow.add(gameDeck.drawCard());
        }
    }

}
