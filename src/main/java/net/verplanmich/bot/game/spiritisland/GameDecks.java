package net.verplanmich.bot.game.spiritisland;

import net.verplanmich.bot.game.Deck;

import java.util.*;

public class GameDecks {

    Map<String, Deck> decks = new HashMap();

    List<Skill> skills = new ArrayList<>(Arrays.asList(
            new Skill("wacht-uber-heilende-land", Skill.Type.EARTH),
            new Skill("ein-jahr-absoluter-stille", Skill.Type.EARTH),
            new Skill("ritual-der-zerstorung", Skill.Type.EARTH),
            new Skill("verlockung-fruchtbarer-erde", Skill.Type.EARTH),
            new Skill("schatten-des-schreckens", Skill.Type.SHADE),
            new Skill("im-namen-des-schattens", Skill.Type.SHADE),
            new Skill("schreckgestalt", Skill.Type.SHADE),
            new Skill("verdorrende-ernte", Skill.Type.SHADE),
            new Skill("fruchtbarkeit-des-flusses", Skill.Type.WATER),
            new Skill("energieschub", Skill.Type.WATER),
            new Skill("fortreissende-wasser", Skill.Type.WATER),
            new Skill("uberschwemmung", Skill.Type.WATER),
            new Skill("vorbote-des-blitzes", Skill.Type.LIGHTNING),
            new Skill("blitzschub", Skill.Type.LIGHTNING),
            new Skill("wutender-sturm", Skill.Type.LIGHTNING),
            new Skill("vernichtete-siedlung", Skill.Type.LIGHTNING),
            new Skill("schatten-des-brennenden-waldes", Skill.Type.MINOR),
            new Skill("visionen-vom-feuertod", Skill.Type.MINOR),
            new Skill("lockruf-der-dahan-kultur", Skill.Type.MINOR),
            new Skill("dichter-nachtwald", Skill.Type.MINOR),
            new Skill("glut-und-aschefluch", Skill.Type.MINOR),
            new Skill("reinigende-flamme", Skill.Type.MINOR),
            new Skill("ruf-zur-schlacht", Skill.Type.MINOR),
            new Skill("angstwahn", Skill.Type.MINOR),
            new Skill("selbstheilung-der-natur", Skill.Type.MINOR),
            new Skill("sturz-in-de-tiefen-des-erdreichs", Skill.Type.MINOR),
            new Skill("umfassender-schutz", Skill.Type.MINOR),
            new Skill("unheimliche-schmelze", Skill.Type.MINOR),
            new Skill("gesang-des-heiligen-lebens", Skill.Type.MINOR),
            new Skill("bestien-aus-den-tiefen", Skill.Type.MINOR),
            new Skill("unaufhalsames-wuchern", Skill.Type.MINOR),
            new Skill("ameisenplage", Skill.Type.MINOR),
            new Skill("ruf-zum-aufbruch", Skill.Type.MINOR),
            new Skill("erwachen-der-bäume-und-steine", Skill.Type.MINOR),
            new Skill("elementar-schub", Skill.Type.MINOR),
            new Skill("jagd-im-schutze-der-nacht", Skill.Type.MINOR),
            new Skill("heisser-dampf", Skill.Type.MINOR),
            new Skill("weitrechende-wirkung", Skill.Type.MINOR),
            new Skill("blutregen", Skill.Type.MINOR),
            new Skill("lockruf-des-unbekannten", Skill.Type.MINOR),
            new Skill("nagetierplage", Skill.Type.MINOR),
            new Skill("gabe-der-macht", Skill.Type.MINOR),
            new Skill("gabe-der-lebensenergie", Skill.Type.MINOR),
            new Skill("verlockende-pracht", Skill.Type.MINOR),
            new Skill("gabe-der-stetigkeit", Skill.Type.MINOR),
            new Skill("durre", Skill.Type.MINOR),
            new Skill("den-kampf-der-erde-beflugeln", Skill.Type.MINOR),
            new Skill("ruf-zur-heilung", Skill.Type.MINOR),
            new Skill("schläfrigkeit", Skill.Type.MINOR),
            new Skill("erschöpfung-uns-auszehrung", Skill.Type.MINOR),
            new Skill("ruf-zur-abschottung", Skill.Type.MINOR),
            new Skill("irrlichter", Skill.Type.MINOR),
            new Skill("albtraum-terror", Skill.Type.MAJOR),
            new Skill("der-hunger-des-dschungels", Skill.Type.MAJOR),
            new Skill("sturm-der-fähigkeiten", Skill.Type.MAJOR),
            new Skill("beschleunigte-faulnis", Skill.Type.MAJOR),
            new Skill("tsunami", Skill.Type.MAJOR),
            new Skill("auflösung-der-familienbande", Skill.Type.MAJOR),
            new Skill("unendliche-lebenskraft", Skill.Type.MAJOR),
            new Skill("nebel-des-vergessens", Skill.Type.MAJOR),
            new Skill("unaufhaltsamer-anspruch", Skill.Type.MAJOR),
            new Skill("winde-der-agonnie", Skill.Type.MAJOR),
            new Skill("brand-der-erneuerung", Skill.Type.MAJOR),
            new Skill("schwingen-der-sonne", Skill.Type.MAJOR),
            new Skill("rache-der-toten", Skill.Type.MAJOR),
            new Skill("das-geschundene-land-schlagt-zuruck", Skill.Type.MAJOR),
            new Skill("blitze-wie-krallen", Skill.Type.MAJOR),
            new Skill("schreckensstarre", Skill.Type.MAJOR),
            new Skill("verschrankte-fahigkeiten", Skill.Type.MAJOR),
            new Skill("baume-und-steine-sprechen-von-krieg", Skill.Type.MAJOR),
            new Skill("reinigende-fluten", Skill.Type.MAJOR),
            new Skill("flammensaule", Skill.Type.MAJOR),
            new Skill("angriff-im-morgengrauen", Skill.Type.MAJOR),
            new Skill("vergiftetes-land", Skill.Type.MAJOR)));


    List<UserEntity> user = new ArrayList<>(Arrays.asList(
            new UserEntity(Skill.Type.SHADE,new ArrayList<>(Arrays.asList("","","","","")),new ArrayList<>(Arrays.asList("","","","",""))),
            new UserEntity(Skill.Type.EARTH,new ArrayList(Arrays.asList("","","","","")),new ArrayList(Arrays.asList("","","","",""))),
            new UserEntity(Skill.Type.LIGHTNING,new ArrayList(Arrays.asList("","","","","","","")),new ArrayList(Arrays.asList("","","",""))),
            new UserEntity(Skill.Type.WATER,new ArrayList(Arrays.asList("","","","","","")),new ArrayList(Arrays.asList("","","","","","")))
            ));


    GameDecks() {
        Collections.shuffle(user);
        Collections.shuffle(skills);
        skills.forEach(skill -> {
            String type = skill.getType().name().toLowerCase();
            Deck subSkills = decks.get(type);
            if (subSkills == null) {
                subSkills = new Deck(new ArrayList());
                decks.put(type, subSkills);
            }
            subSkills.toDrawPileTop(skill.getName());
        });
    }

    public UserEntity getAvatar() {
        return user.remove(0);
    }

    public List<String> getHand(String type) {
        return decks.get(type).getDrawPile();
    }

    public String drawDeck(String type) {
        String cardId = decks.get(type).drawOrShuffle();
        decks.get(type).discardCard(cardId);
        return cardId;
    }

    public boolean removeCard(String cardId) {
        for (Skill skill : skills) {
            if (skill.getName().equals(cardId)) {
                Deck deck = decks.get(skill.getType().name().toLowerCase());
                return deck.fromDiscardPile(cardId) || deck.fromDrawPile(cardId);
            }
        }
        ;
        return false;
    }

    public void returnCard(String cardId) {
        skills.forEach(skill -> {
            if (skill.getName().equals(cardId)) {
                decks.get(skill.getType().name().toLowerCase()).discardCard(cardId);
            }
        });
    }

}
