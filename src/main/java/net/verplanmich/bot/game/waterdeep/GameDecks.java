package net.verplanmich.bot.game.waterdeep;

import net.verplanmich.bot.game.Deck;
import net.verplanmich.bot.game.GameResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.verplanmich.bot.game.waterdeep.Waterdeep.EVENT_REWARD;
import static net.verplanmich.bot.game.waterdeep.Waterdeep.EVENT_UPDATE_INTRIGUES;

public class GameDecks {
    private List<Quest> baseQuests = Arrays.asList(
            new Quest("ally-with-house-thann", Quest.Type.COMMERCE, false, new UserEntity().setCleric(1).setGold(8).setRogue(3).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("25");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("ambush-artor-morlin", Quest.Type.WAREFARE, false, new UserEntity().setCleric(1).setFighter(3).setRogue(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("8");
                        user.adjustGold("4");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("bolster-city-guard", Quest.Type.WAREFARE, false, new UserEntity().setFighter(9).setRogue(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("25");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("bolster-griffon-cavalry", Quest.Type.WAREFARE, true, new UserEntity().setFighter(4).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("6");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("bribe-the-shipwrights", Quest.Type.COMMERCE, true, new UserEntity().setRogue(4).setWizard(1).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("build-a-reputation-in-skullport", Quest.Type.SKULLDUGGERY, false, new UserEntity().setFighter(1).setRogue(3).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        user.addIntrigues(gameDecks.getIntrigue());
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored")
                                .addEvent(EVENT_UPDATE_INTRIGUES);
                    }),
            new Quest("convert-a-noble-to-lathander", Quest.Type.PIETY, false, new UserEntity().setCleric(2).setFighter(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("8");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " take Quest from Inn")
                                .addEvent(EVENT_REWARD);
                    }),
            new Quest("create-a-shrine-to-oghma", Quest.Type.PIETY, false, new UserEntity().setCleric(5).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("25");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("defeat-uprising-from-undermountain", Quest.Type.WAREFARE, false, new UserEntity().setCleric(1).setFighter(3).setRogue(1).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("11");
                        user.adjustFighter("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("defend-the-tower-of-luck", Quest.Type.PIETY, true, new UserEntity().setCleric(2).setFighter(1).setRogue(1).setWizard(1),
                    (user, gameDecks) -> {
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("deliver-an-ultimatum", Quest.Type.WAREFARE, false, new UserEntity().setFighter(4).setRogue(1).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("11");
                        user.adjustGold("4");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("deliver-weapons-to-selunes-temple", Quest.Type.WAREFARE, false, new UserEntity().setFighter(4).setRogue(1).setWizard(1).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("9");
                        user.adjustCleric("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("discover-hidden-temple-of-lolth", Quest.Type.PIETY, false, new UserEntity().setCleric(2).setFighter(1).setRogue(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " take Quest from Inn")
                                .addEvent(EVENT_REWARD);
                    }),
            new Quest("domesticate-owlbears", Quest.Type.ARCANE, false, new UserEntity().setCleric(1).setWizard(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("8");
                        user.adjustGold("2");
                        user.adjustFighter("1");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("eliminate-vampire-coven", Quest.Type.PIETY, false, new UserEntity().setCleric(2).setFighter(2).setRogue(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("11");
                        user.adjustGold("4");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("establish-harpers-safe-house", Quest.Type.SKULLDUGGERY, false, new UserEntity().setFighter(2).setRogue(3).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("8");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " take Victory for buildings")
                                .addEvent(EVENT_REWARD);
                    }),
            new Quest("establish-new-merchant-guild", Quest.Type.COMMERCE, true, new UserEntity().setCleric(1).setFighter(2).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("8");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("establish-shadow-thieves-guild", Quest.Type.SKULLDUGGERY, false, new UserEntity().setFighter(1).setRogue(8).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("25");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("explore-ahghairons-tower", Quest.Type.ARCANE, true, new UserEntity().setFighter(1).setWizard(2).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("6");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("expose-cult-corruption", Quest.Type.SKULLDUGGERY, false, new UserEntity().setCleric(1).setRogue(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("4");
                        user.adjustCleric("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("expose-red-wizards-spies", Quest.Type.ARCANE, false, new UserEntity().setCleric(1).setFighter(1).setRogue(2).setWizard(2).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("20");
                        user.addIntrigues(gameDecks.getIntrigue());
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored")
                                .addEvent(EVENT_UPDATE_INTRIGUES);
                    }),
            new Quest("fence-goods-for-duke-of-darkness", Quest.Type.SKULLDUGGERY, true, new UserEntity().setFighter(1).setRogue(3).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("6");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("form-an-alliance-with-the-rashemi", Quest.Type.PIETY, false, new UserEntity().setCleric(2).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " take Quest from Inn")
                                .addEvent(EVENT_REWARD);
                    }),
            new Quest("heal-fallen-gray-hand-soldiers", Quest.Type.PIETY, false, new UserEntity().setCleric(2).setWizard(1).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("6");
                        user.adjustFighter("6");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("host-festival-for-sune", Quest.Type.ARCANE, false, new UserEntity().setFighter(2).setWizard(2).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("9");
                        user.adjustCleric("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("impersonate-adarbrent-noble", Quest.Type.COMMERCE, false, new UserEntity().setCleric(1).setFighter(2).setRogue(2).setWizard(1).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("18");
                        user.addIntrigues(gameDecks.getIntrigue());
                        user.addIntrigues(gameDecks.getIntrigue());
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored")
                                .addEvent(EVENT_UPDATE_INTRIGUES);
                    }),
            new Quest("infiltrate-builders-hall", Quest.Type.COMMERCE, true, new UserEntity().setFighter(2).setRogue(2).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("6");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("infiltrate-halasters-circle", Quest.Type.ARCANE, false, new UserEntity().setWizard(5).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("25");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("install-a-spy-in-castle-waterdeep", Quest.Type.SKULLDUGGERY, true, new UserEntity().setRogue(4).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("8");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("investigate-aberrant-infestation", Quest.Type.ARCANE, false, new UserEntity().setCleric(1).setFighter(1).setWizard(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("13");
                        user.addIntrigues(gameDecks.getIntrigue());
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored")
                                .addEvent(EVENT_UPDATE_INTRIGUES);
                    }),
            new Quest("loot-the-crypt-of-chauntea", Quest.Type.COMMERCE, false, new UserEntity().setCleric(1).setRogue(3).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("7");
                        user.addIntrigues(gameDecks.getIntrigue());
                        user.getUserEntity().addActiveQuests(gameDecks.getQuest());
                        return new GameResult()
                                .addEvent(EVENT_UPDATE_INTRIGUES)
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("lure-artisans-of-mirabar", Quest.Type.COMMERCE, false, new UserEntity().setCleric(1).setFighter(1).setRogue(1).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("4");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " build a building")
                                .addEvent(EVENT_REWARD);
                    }),
            new Quest("perform-the-penance-of-duty", Quest.Type.PIETY, false, new UserEntity().setCleric(2).setFighter(2).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("12");
                        user.adjustFighter("1");
                        user.adjustCleric("1");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("placate-the-walking-statue", Quest.Type.COMMERCE, false, new UserEntity().setCleric(2).setRogue(2).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " build a building")
                                .addEvent(EVENT_REWARD);
                    }),
            new Quest("place-a-sleeper-agent-in-skullport", Quest.Type.SKULLDUGGERY, true, new UserEntity().setFighter(1).setRogue(4).setWizard(1),
                    (user, gameDecks) -> {
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("prison-break", Quest.Type.SKULLDUGGERY, false, new UserEntity().setRogue(4).setWizard(2).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("14");
                        user.adjustFighter("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " play intrigue")
                                .addEvent(EVENT_REWARD);
                    }),
            new Quest("procure-stolen-goods", Quest.Type.SKULLDUGGERY, false, new UserEntity().setRogue(3).setGold(6),
                    (user, gameDecks) -> {
                        user.adjustVictory("8");
                        user.addIntrigues(gameDecks.getIntrigue());
                        user.addIntrigues(gameDecks.getIntrigue());
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored")
                                .addEvent(EVENT_UPDATE_INTRIGUES);
                    }),
            new Quest("produce-a-miracle-for-the-masses", Quest.Type.PIETY, true, new UserEntity().setCleric(2).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("5");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("protect-the-house-of-wonder", Quest.Type.PIETY, true, new UserEntity().setCleric(2).setFighter(1).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("8");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("quell-mercenary-uprising", Quest.Type.WAREFARE, true, new UserEntity().setCleric(1).setFighter(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("8");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("raid-on-undermountain", Quest.Type.SKULLDUGGERY, false, new UserEntity().setCleric(1).setFighter(2).setRogue(4).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("20");
                        user.adjustGold("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("raid-orc-stronghold", Quest.Type.WAREFARE, false, new UserEntity().setFighter(4).setRogue(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("8");
                        user.adjustGold("4");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("recover-the-magisters-orb", Quest.Type.ARCANE, true, new UserEntity().setRogue(3).setWizard(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("6");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("recruit-for-blackstaff-academy", Quest.Type.ARCANE, false, new UserEntity().setFighter(1).setRogue(1).setWizard(2).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("6");
                        user.adjustWizard("3");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("recruit-lieutenant", Quest.Type.WAREFARE, true, new UserEntity().setCleric(1).setFighter(5).setRogue(1).setWizard(1),
                    (user, gameDecks) -> {
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("recruit-paladins-for-tyr", Quest.Type.PIETY, false, new UserEntity().setCleric(2).setFighter(4).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        user.adjustCleric("3");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("repel-seawraiths", Quest.Type.WAREFARE, false, new UserEntity().setCleric(1).setFighter(4).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("15");
                        user.adjustGold("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("research-chronomancy", Quest.Type.ARCANE, false, new UserEntity().setWizard(2).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("4");
                        user.adjustWizard("1");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " return 1 agent")
                                .addEvent(EVENT_REWARD);
                    }),
            new Quest("retrieve-ancient-artifacts", Quest.Type.ARCANE, false, new UserEntity().setFighter(2).setRogue(1).setWizard(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("11");
                        user.adjustGold("4");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("safeguard-eltorchul-mage", Quest.Type.COMMERCE, false, new UserEntity().setFighter(1).setRogue(1).setWizard(1).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("4");
                        user.adjustWizard("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("seal-gate-to-cyrics-realm", Quest.Type.PIETY, false, new UserEntity().setCleric(2).setRogue(3).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("20");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("send-aid-to-the-harpers", Quest.Type.COMMERCE, false, new UserEntity().setCleric(1).setFighter(1).setRogue(1).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("15");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " opponent +4 gold")
                                .addEvent(EVENT_REWARD);
                    }),
            new Quest("spy-on-the-house-of-light", Quest.Type.COMMERCE, false, new UserEntity().setFighter(3).setRogue(2).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("6");
                        user.adjustGold("6");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("steal-from-house-adarbrent", Quest.Type.SKULLDUGGERY, false, new UserEntity().setFighter(1).setRogue(4).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        user.adjustGold("6");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("steal-spellbook-from-silverhand", Quest.Type.ARCANE, false, new UserEntity().setFighter(1).setRogue(2).setWizard(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("7");
                        user.adjustGold("4");
                        user.addIntrigues(gameDecks.getIntrigue());
                        user.addIntrigues(gameDecks.getIntrigue());
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored")
                                .addEvent(EVENT_UPDATE_INTRIGUES);
                    }),
            new Quest("study-the-illusk-arch", Quest.Type.ARCANE, true, new UserEntity().setCleric(1).setWizard(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("8");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("take-over-rival-organization", Quest.Type.SKULLDUGGERY, false, new UserEntity().setFighter(1).setRogue(2).setWizard(1).setGold(6),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        user.adjustRogue("4");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("thin-the-city-watch", Quest.Type.COMMERCE, false, new UserEntity().setCleric(1).setFighter(1).setRogue(1).setGold(4),
                    (user, gameDecks) -> {
                        user.adjustVictory("9");
                        user.adjustRogue("4");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("train-bladesingers", Quest.Type.WAREFARE, false, new UserEntity().setFighter(3).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("4");
                        user.adjustWizard("1");
                        user.adjustFighter("1");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("confront-the-xanathar", Quest.Type.WAREFARE, false, new UserEntity().setCleric(1).setFighter(4).setRogue(2).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("20");
                        user.adjustGold("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    })
    );

    private List<Quest> mandatoryQuests = Arrays.asList(
            new Quest("fend-off-bandits", Quest.Type.MANDATORY, false, new UserEntity().setFighter(2).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("foil-the-zhentarim", Quest.Type.MANDATORY, false, new UserEntity().setFighter(1).setRogue(1).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("placate-angry-merchants", Quest.Type.MANDATORY, false, new UserEntity().setCleric(1).setFighter(1).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("4");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("quell-riots", Quest.Type.MANDATORY, false, new UserEntity().setCleric(2).setFighter(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("4");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("repel-drow-invaders", Quest.Type.MANDATORY, false, new UserEntity().setCleric(1).setRogue(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("stamp-out-cultists", Quest.Type.MANDATORY, false, new UserEntity().setCleric(1).setFighter(1).setRogue(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("2");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    })
    );

    private List<Quest> underQuests = new ArrayList(Arrays.asList(
            new Quest("explore-trobriands-graveyard", Quest.Type.ARCANE, false, new UserEntity().setFighter(2).setRogue(3).setWizard(4).setGold(6),
                    (user, gameDecks) -> {
                        user.adjustVictory("40");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("plunder-the-island-temple", Quest.Type.PIETY, false, new UserEntity().setCleric(5).setFighter(2).setRogue(2).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("40");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("ransack-whitehelms-tombe", Quest.Type.COMMERCE, false, new UserEntity().setCleric(2).setFighter(3).setRogue(4).setGold(10),
                    (user, gameDecks) -> {
                        user.adjustVictory("40");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("resurrect-dead-wizards", Quest.Type.ARCANE, false, new UserEntity().setCleric(3).setGold(5),
                    (user, gameDecks) -> {
                        user.adjustVictory("6");
                        user.adjustWizard("3");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("ally-with-the-xanathars-guild", Quest.Type.SKULLDUGGERY, true, new UserEntity().setFighter(2).setRogue(5).setWizard(1).setGold(5),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("survive-arcturias-transformation", Quest.Type.SKULLDUGGERY, false, new UserEntity().setFighter(6).setGold(5),
                    (user, gameDecks) -> {
                        user.adjustVictory("6");
                        user.adjustFighter("6");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("steal-gems-from-the-bone-throne", Quest.Type.COMMERCE, false, new UserEntity().setRogue(4).setWizard(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("7");
                        user.adjustGold("10");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("establish-wizard-academy", Quest.Type.ARCANE, true, new UserEntity().setCleric(1).setFighter(1).setRogue(3).setWizard(3),
                    (user, gameDecks) -> {
                        user.adjustVictory("12");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored");
                    }),
            new Quest("establish-temple-to-ibrandul", Quest.Type.PIETY, false, new UserEntity().setCleric(3).setRogue(4).setGold(5),
                    (user, gameDecks) -> {
                        user.adjustVictory("11");
                        user.adjustCleric("1");
                        user.adjustFighter("1");
                        user.adjustRogue("1");
                        user.adjustWizard("1");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " take all quests from inn");
                    }),
            new Quest("recruit-for-city-watch", Quest.Type.COMMERCE, false, new UserEntity().setCleric(1).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("8");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "can spend 10 gold for reward");
                    }),
            new Quest("study-in-the-librarium", Quest.Type.ARCANE, true, new UserEntity().setRogue(2).setWizard(5).setGold(5),
                    (user, gameDecks) -> {
                        user.adjustVictory("11");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "scored");
                    }),
            new Quest("sanctify-a-desecrated-temple", Quest.Type.PIETY, true, new UserEntity().setCleric(4).setFighter(2).setGold(5),
                    (user, gameDecks) -> {
                        user.adjustVictory("13");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "scored");
                    }),
            new Quest("battle-in-muirals-gauntlet", Quest.Type.WAREFARE, false, new UserEntity().setCleric(2).setFighter(7).setWizard(2).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("40");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "scored");
                    }),
            new Quest("rescue-clerics-of-tymora", Quest.Type.PIETY, false, new UserEntity().setFighter(6).setWizard(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        user.adjustCleric("3");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "scored");
                    }),
            new Quest("defend-the-lanceboard-room", Quest.Type.SKULLDUGGERY, false, new UserEntity().setFighter(3).setRogue(6).setWizard(1).setGold(10),
                    (user, gameDecks) -> {
                        user.adjustVictory("12");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "get 8 random cubes");
                    }),
            new Quest("diplomatic-mission-to-suzail", Quest.Type.PIETY, true, new UserEntity().setCleric(3).setFighter(3).setGold(3),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "you can complete face-up quests ad inn");
                    }),
            new Quest("wake-the-six-sleepers", Quest.Type.WAREFARE, false, new UserEntity().setCleric(3).setRogue(3),
                    (user, gameDecks) -> {
                        user.adjustVictory("6");
                        user.adjustFighter("6");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "scored");
                    }),
            new Quest("threaten-the-builders-guilds", Quest.Type.COMMERCE, false, new UserEntity().setFighter(2).setRogue(4).setWizard(2).setGold(10),
                    (user, gameDecks) -> {
                        user.adjustVictory("13");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "build all face-up buildings");
                    }),
            new Quest("seize-citadel-of-the-bloddy-hand", Quest.Type.WAREFARE, true, new UserEntity().setCleric(1).setFighter(4).setWizard(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("6");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "scored");
                    }),
            new Quest("defend-the-yawning-portal", Quest.Type.WAREFARE, false, new UserEntity().setCleric(3).setFighter(6).setRogue(1).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("15");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "gain 2 random cubes. return 3 agents");
                    }),
            new Quest("survive-a-meeting-with-halaster", Quest.Type.ARCANE, false, new UserEntity().setCleric(1).setFighter(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("15");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "spend 4 more cubes gain intrigue for each wizard spend");
                    }),
            new Quest("recover-the-flame-of-the -north", Quest.Type.WAREFARE, true, new UserEntity().setFighter(5).setRogue(3).setWizard(1).setGold(3),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "scored");
                    }),
            new Quest("deal-with-the-black-viper", Quest.Type.ARCANE, false, new UserEntity().setFighter(1).setRogue(4).setWizard(2).setGold(5),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        user.addIntrigues(gameDecks.getIntrigue());
                        user.addIntrigues(gameDecks.getIntrigue());
                        user.addIntrigues(gameDecks.getIntrigue());
                        user.addIntrigues(gameDecks.getIntrigue());
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " you can play your newly drawn intrigues")
                                .addEvent(EVENT_UPDATE_INTRIGUES);
                    }),
            new Quest("unleash-crime-spree", Quest.Type.SKULLDUGGERY, false, new UserEntity().setCleric(1).setFighter(1).setRogue(1).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("12");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " you can score additional 10 vp for 4 rogues");
                    }),
            new Quest("sponsor-bounty-hunters", Quest.Type.COMMERCE, true, new UserEntity().setCleric(1).setFighter(4).setRogue(3).setGold(6),
                    (user, gameDecks) -> {
                        user.adjustVictory("12");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "scored");
                    }),
            new Quest("obtain-builders-plans", Quest.Type.COMMERCE, true, new UserEntity().setFighter(2).setRogue(2).setWizard(1).setGold(10),
                    (user, gameDecks) -> {
                        user.adjustVictory("13");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "scored");
                    }),
            new Quest("destroy-a-temple-of-selvetarm", Quest.Type.WAREFARE, false, new UserEntity().setCleric(1).setFighter(2).setGold(2),
                    (user, gameDecks) -> {
                        user.adjustVictory("10");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " you can score additional 10 vp for 4 fighters");
                    }),
            new Quest("root-out-loviatars-faithful", Quest.Type.PIETY, false, new UserEntity().setFighter(1).setWizard(1),
                    (user, gameDecks) -> {
                        user.adjustVictory("15");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "spend 4 more cubes gain 2 VP for each cleric spend");
                    }),
            new Quest("break-into-blackstaff-tower", Quest.Type.SKULLDUGGERY, false, new UserEntity().setCleric(1).setRogue(7).setWizard(2).setGold(6),
                    (user, gameDecks) -> {
                        user.adjustVictory("40");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "scored");
                    }),
            new Quest("impersonate-tax-collector", Quest.Type.SKULLDUGGERY, true, new UserEntity().setFighter(1).setRogue(4).setWizard(1).setGold(5),
                    (user, gameDecks) -> {
                        user.adjustVictory("9");
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + "scored");
                    })
    ));
    private List<Quest> mandatoryUnderQuests = new ArrayList(Arrays.asList(
            new Quest("evade-assassin", Quest.Type.MANDATORY, false, new UserEntity().setRogue(2).setWizard(1),
                    (user, gameDecks) -> {
                        user.addIntrigues(gameDecks.getIntrigue());
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored")
                                .addEvent(EVENT_UPDATE_INTRIGUES);
                    }),
            new Quest("subdue-ilithid-menace", Quest.Type.MANDATORY, false, new UserEntity().setCleric(1).setFighter(2),
                    (user, gameDecks) -> {
                        user.addIntrigues(gameDecks.getIntrigue());
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored")
                                .addEvent(EVENT_UPDATE_INTRIGUES);
                    }),
            new Quest("unveil-abyssal-agent", Quest.Type.MANDATORY, false, new UserEntity().setCleric(1).setWizard(1),
                    (user, gameDecks) -> {
                        user.addIntrigues(gameDecks.getIntrigue());
                        return new GameResult()
                                .setText(user.getUserEntity().getName() + " scored")
                                .addEvent(EVENT_UPDATE_INTRIGUES);
                    })
    ));

    private List<String> baseChars = new ArrayList(Arrays.asList("durnan-the-wanderer", "khelben-arunsun", "nymara-scheiron", "larissa-neathal", "mirt-the-moneylender", "nindil-jalbuck", "sammereza-sulphontis", "piergeiron-the-paladinson", "kyriani-agrivar", "brianne-byndraeth", "cladorn-cassalanter"));
    private List<String> underChars = new ArrayList(Arrays.asList("danilo-thann", "halaster-blackcloak", "trobriand"));


    public GameDecks() {
    }

    private List<String> underSkull = new ArrayList(Arrays.asList("evade-assassin", "subdue-ilithid-menace", "unveil-abyssal-agent", "unexpected-success", "unexpected-success", "call-for-assistance", "call-for-assistance", "information-broker", "information-broker",
            "demolish", "recruitment-drive", "tax-revolt", "sponsor-apprentices", "proselytize", "mercenary-contract", "open-lord", "preferential-treatment", "organized-crime", "manipulate", "friendly-loan", "allied-faiths", "honor-among-thieves",
            "unlikely-assistance", "inevitable-betrayal", "architectural-innovation"
    ));


    private List<String> intriguesBase = new ArrayList(Arrays.asList("good-faith", "good-faith", "bidding-war", "bidding-war", "bidding-war", "special-assignment", "special-assignment", "crime-wave", "crime-wave", "call-in-a-favor", "call-in-a-favor", "placate-angry-merchants",
            "lack-of-faith", "lack-of-faith", "recall-agent", "recall-agent", "tax-collection", "tax-collection", "spread-the-wealth", "spread-the-wealth", "repel-drow-invaders", "assassination", "assassination", "arcane-mishap", "arcane-mishap", "sample-wares", "sample-wares",
            "foil-the-zhentarim", "bribe-agent", "bribe-agent", "quell-riots", "fend-off-bandits", "change-of-plans", "summon-the-faithfull", "call-for-adventurers", "call-for-adventurers", "real-estate-deal", "stamp-out-cultists",
            "graduation-day", "graduation-day", "recruit-spies", "ambush", "ambush", "free-drinks", "free-drinks", "accelerate-plans", "conspiration", "conspiration", "research-agreement"
    ));
    private List<String> intiguesSkull = Arrays.asList("clear-rust-monster-nest");

    private Deck intrigues;
    private Deck quests;
    private Deck chars;
    private Map<String, Quest> questMap;
    private List<String> tavern = new ArrayList();

    public void setGame(String type) {
        List<String> intrigueCards = this.intriguesBase;
        List<String> charCards = this.baseChars;
        List<Quest> quests = new ArrayList(this.baseQuests);
        List<Quest> mandatory = new ArrayList(this.mandatoryQuests);

        if (type == "under") {
            intrigueCards.addAll(underSkull);
            charCards.addAll(underChars);
            quests.addAll(underQuests);
            mandatory.addAll(mandatoryUnderQuests);
        }


        this.questMap = quests.stream().collect(Collectors.toMap(Quest::getName, q -> q));
        mandatory.stream().forEach(q -> this.questMap.put(q.getName(), q));
        this.intrigues = new Deck(intrigueCards).shuffle();
        this.chars = new Deck(charCards).shuffle();
        this.quests = new Deck(quests.stream().map(q -> q.getName()).collect(Collectors.toList())).shuffle();
        refillTavern();

    }

    public boolean getTavernCard(String cardId) {
        boolean result = tavern.contains(cardId);
        if (result) {
            int index = tavern.indexOf(cardId);
            tavern.set(index, quests.drawCard());
        }
        return result;
    }

    public boolean isQuest(String cardId) {
        return questMap.containsKey(cardId);
    }

    public String getQuest() {
        return quests.drawOrShuffle();
    }

    public String getIntrigue() {
        return intrigues.drawOrShuffle();
    }

    public String getChar() {
        return chars.drawOrShuffle();
    }

    public void refillTavern() {
        this.tavern.forEach(quest -> quests.discardCard(quest));
        this.tavern = new ArrayList();
        this.tavern.add(this.quests.drawOrShuffle());
        this.tavern.add(this.quests.drawOrShuffle());
        this.tavern.add(this.quests.drawOrShuffle());
        this.tavern.add(this.quests.drawOrShuffle());
    }

    public void removeQuestCard(String cardId) {
        quests.getDiscardPile().remove(cardId);
        quests.getDrawPile().remove(cardId);
    }

    public void discardQuest(String cardId) {
        quests.discardCard(cardId);
    }

    public List<String> getTavern() {
        return tavern;
    }

    public Map<String, Quest> getQuestMap() {
        return questMap;
    }
}