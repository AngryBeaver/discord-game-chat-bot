package net.verplanmich.bot.game.middleearth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.verplanmich.bot.game.middleearth.GameDecks.Ability.*;
import static net.verplanmich.bot.game.middleearth.GameDecks.AttackModifier.*;
import static net.verplanmich.bot.game.middleearth.GameDecks.ItemType.*;

public class GameDecks {

    List<UserEntity> chars = new ArrayList(Arrays.asList(
            new UserEntity("elena", "elf", 2, 3, 2, 4, 3, 6, 4, 5, "Once per test, after you spend 1 inspiration, a nearby hero gains 1 inspiration."),
            new UserEntity("legolas", "elf", 2, 3, 4, 2, 3, 6, 4, 5, "Once per round, during your turn, you may spend 1 inspiration to move 1 space without provoking attacks."),
            new UserEntity("bilbo", "hobit", 2, 2, 3, 3, 4, 7, 3, 6, "Before you interact, look at the top card of your deck. If it is a card with inspiration, gain 1 inspiration. You can equip only 1 hand."),
            new UserEntity("gimli", "dwarf", 4, 2, 2, 4, 2, 4, 6, 3, "After you perform an attack test, you may place 1 card from the test on top of your deck."),
            new UserEntity("aragorn", "human", 3, 4, 2, 2, 3, 5, 5, 4, "When heroes scout during the raklly phase, you and nearby heroes each reveal 1 additional card."),
            new UserEntity("beravor", "human", 3, 3, 3, 3, 2, 5, 5, 4, "Once per round, after you sprint, hide, or strike you may scout 1.")
    ));

    public enum Ability {
        might,
        wisdom,
        agility,
        spirit,
        wit
    }

    public enum Slot {
        hand,
        hands,
        armor,
        trinket
    }

    public enum ItemType {
        dagger,
        sword,
        bow,
        staff,
        axe,
        harp,
        banner,
        cloak,
        ringmail,
        garb,
        ration,
        map,
        book,
        helmet,
        brooch,
        pipe,
        boots,
        rope,
        torch,
        handkerchief
    }

    public enum AttackModifier {
        stun,
        cleave,
        pierce,
        smite,
        lethal,
        sunder,
        inspiration
    }

    public class Attack {
        int success;
        int hits;
        AttackModifier modifier;

        public Attack(int success, int hits, AttackModifier modifier) {
            this.success = success;
            this.hits = hits;
            this.modifier = modifier;
        }
    }

    public class Item {
        private String name;
        private ItemType type;
        private List<Ability> ability;
        private Slot slot;
        private int stage;
        private int number;
        private boolean isRanged;
        private String text;
        private List<Attack> attacks;
        private int depletion;

        public Item(String name, ItemType type, List<Ability> ability, Slot slot, int stage, int number, boolean isRanged, String text, List<Attack> attacks) {
            this.name = name;
            this.type = type;
            this.ability = ability;
            this.slot = slot;
            this.stage = stage;
            this.number = number;
            this.isRanged = isRanged;
            this.text = text;
            this.attacks = attacks;
            this.depletion = 0;
        }

        public Item(String name, ItemType type, Slot slot, int stage, int number, String text, int depletion) {
            this.name = name;
            this.type = type;
            this.ability = new ArrayList();
            this.slot = slot;
            this.stage = stage;
            this.number = number;
            this.isRanged = false;
            this.text = text;
            this.attacks = new ArrayList();
            this.depletion = depletion;
        }

    }


    List<Item> items = new ArrayList(Arrays.asList(
            new Item("Dagger", dagger, Arrays.asList(wit), Slot.hand, 1, 25, false, "", Arrays.asList(new Attack(1, 2, null), new Attack(2, 3, pierce))),
            new Item("Dagger", dagger, Arrays.asList(wit), Slot.hand, 1, 25, false, "", Arrays.asList(new Attack(1, 2, null), new Attack(2, 3, pierce))),
            new Item("Sword", sword, Arrays.asList(might), Slot.hand, 1, 27, false, "", Arrays.asList(new Attack(1, 2, null), new Attack(2, 5, null))),
            new Item("Great Bow", bow, Arrays.asList(agility), Slot.hands, 1, 32, true, "", Arrays.asList(new Attack(1, 2, null), new Attack(2, 5, null))),
            new Item("Staff", staff, Arrays.asList(agility), Slot.hands, 1, 28, false, "", Arrays.asList(new Attack(1, 2, null), new Attack(1, 2, null), new Attack(2, 3, stun))),
            new Item("Ered luin Dagger", dagger, Arrays.asList(wit), Slot.hand, 2, 55, false, "Before you attack, you may spend 1 inspiration to give this weapon range for that attack.", Arrays.asList(new Attack(1, 2, null), new Attack(2, 4, pierce))),
            new Item("Gondolin Dagger",dagger,Arrays.asList(wit),Slot.hand,2,55,false,"When used to attack, if there is a nearby Orc or Goblin, reveal 1 additional card.",Arrays.asList(new Attack(1, 2, null), new Attack(2, 4, pierce))),
            new Item("Blood-Wright",dagger,Arrays.asList(wit),Slot.hand,3,85,false,"Decrease your damage and fear limits by 1.",Arrays.asList(new Attack(1, 2, null),new Attack(1, 3, null), new Attack(2, 4, pierce))),
            new Item("Shade-Breaker",dagger,Arrays.asList(wit),Slot.hand,3,85,false,"After you defeat an enemy group, a hero in your space may discard 1 fear.",Arrays.asList(new Attack(1, 2, null),new Attack(1, 2, smite), new Attack(2, 4, pierce))),
            new Item("Widow's Warning",dagger,Arrays.asList(wit),Slot.hand,3,85,false,"When you test, if there is a nearby Orc or Goblin, reveal 1 additional card.",Arrays.asList(new Attack(1, 2, null),new Attack(1, 3, null), new Attack(2, 4, pierce))),
            new Item("Mirkwood Great Bow", bow, Arrays.asList(agility), Slot.hands, 2, 67, true, "", Arrays.asList(new Attack(1, 2, null), new Attack(2, 5, cleave))),
            new Item("Silver-Fall", bow, Arrays.asList(agility), Slot.hands, 3, 99, true, "When used to attack, you may discard a prepared card to convert all inspiration to success", Arrays.asList(new Attack(1, 3, null),new Attack(1, 2, pierce), new Attack(2, 5, cleave))),
            new Item("Mourning-Song", bow, Arrays.asList(agility), Slot.hands, 3, 99, true, "When used to attack, if you have a song card prepared, add 1 succecss", Arrays.asList(new Attack(1, 2, null),new Attack(1, 0, lethal), new Attack(2, 5, cleave))),
            new Item("Númenórean Sword",sword, Arrays.asList(might),Slot.hand,2,58,false,"After you defeat an enemy group, you may discard 1 facedown damage or fear.",Arrays.asList(new Attack(1,3,null),new Attack(2,5,null))),
            new Item("Sun-Silver",sword, Arrays.asList(might),Slot.hand,3,90,false,"When used to attack reveal 1 additional card.",Arrays.asList(new Attack(1,3,null),new Attack(1,0,pierce),new Attack(2,6,null))),
            new Item("Fate-Bender",sword, Arrays.asList(might),Slot.hand,3,90,false,"When used to attack while you have 3 or mor damage, add stun.",Arrays.asList(new Attack(1,2,smite),new Attack(1,2,null),new Attack(2,5,null))),
            new Item("Lone-Land Staff",staff,Arrays.asList(agility,wisdom),Slot.hands,2,60,false,"",Arrays.asList(new Attack(1,2,null),new Attack(1,3,null),new Attack(2,4,stun))),
            new Item("Ent-Crook",staff,Arrays.asList(agility,wisdom),Slot.hands,3,91,false,"You cannot sprint.",Arrays.asList(new Attack(1,4,null),new Attack(1,3,cleave),new Attack(2,5,stun))),
            new Item("Maiden-Wrath",staff,Arrays.asList(agility),Slot.hands,3,91,false,"When used to attack an exhausted enemy, add 1 success.",Arrays.asList(new Attack(1,4,null),new Attack(1,2,sunder),new Attack(2,3,stun))),
            new Item("Battle Axe",axe,Arrays.asList(might),Slot.hands,1,33,false,"",Arrays.asList(new Attack(1,2,sunder),new Attack(2,6,null))),
            new Item("Iron Hills Battle Axe",axe,Arrays.asList(might),Slot.hands,2,71,false,"",Arrays.asList(new Attack(1,2,sunder),new Attack(1,3,null),new Attack(2,6,null))),
            new Item("Grief-Bearer",axe,Arrays.asList(might),Slot.hands,3,105,false,"",Arrays.asList(new Attack(1,2,lethal),new Attack(1,2,sunder),new Attack(3,9,null))),
            new Item("Honor-Knell",axe,Arrays.asList(might,spirit),Slot.hands,1,105,false,"Increase your inspiration limit by 2.",Arrays.asList(new Attack(1,2,cleave),new Attack(1,2,sunder),new Attack(3,8,null))),
            new Item("Harp",harp,new ArrayList(),Slot.hand,1,24,false,"At the start of your turn, a hero in your space who has 0 inspiration gains 1 inspiration.",new ArrayList()),
            new Item("Forlindon Harp",harp,new ArrayList(),Slot.hand,2,58,false,"At the start of your turn, a hero in your space who has 0 inspiration gains 1 inspiration. If a hero in your space would gain an inspiration, they may discard a facedown fear instead.",new ArrayList()),
            new Item("Heart's-Rest",harp,new ArrayList(),Slot.hand,3,90,false,"At the start of your turn, a hero in your space who has 0 inspiration gains 1 inspiration. If a hero in your space would gain an inspriration, they may discard a facedown fear or damage instead",new ArrayList()),
            new Item("River-Calling",harp,Arrays.asList(spirit),Slot.hand,3,90,false,"At the start of your turn, up to 2 heroes in your space who have 0 inspiration gain 1 inspiration",Arrays.asList(new Attack(1,0,stun),new Attack(1,0,inspiration))),
            new Item("Banner",banner,new ArrayList(),Slot.hand,1,22,false,"After you or a nearby hero rests or guards, that hero gains 1 inspiration.",new ArrayList()),
            new Item("Dúnedaín Banner",banner,new ArrayList(),Slot.hand,2,61,false,"After you or a nearby hero rests ,guards or hides, that hero gains 1 inspiration.",new ArrayList()),
            new Item("War-Haven",banner,new ArrayList(),Slot.hand,3,94,false,"After you or a nearby hero rests, guards or hides, that hero gains 1 inspiration. After you discard a prepared tactic card, you or a nearby hero may discard 1 facedown damage or fear.",new ArrayList()),
            new Item("War-Maker",banner,Arrays.asList(might,wit),Slot.hand,3,94,false,"After you or a nearby hero rests, guards or hides, that hero gains 1 inspiration.",Arrays.asList(new Attack(1,0,cleave),new Attack(1 ,2,null))),

            new Item("Cloak",cloak,new ArrayList(),Slot.armor,1,28,false,"Before you suffer damage or fear, you may spend 1 inspiration to prevent 1 of that damage or fear.",new ArrayList()),
            new Item("Cloak",cloak,new ArrayList(),Slot.armor,1,28,false,"Before you suffer damage or fear, you may spend 1 inspiration to prevent 1 of that damage or fear.",new ArrayList()),
            new Item("Cloak",cloak,new ArrayList(),Slot.armor,1,28,false,"Before you suffer damage or fear, you may spend 1 inspiration to prevent 1 of that damage or fear.",new ArrayList()),
            new Item("Fangorn Cloak",cloak,Arrays.asList(agility),Slot.armor,2,65,true,"Before you suffer damage or fear, you may spend 1 inspiration to prevent 1 of that damage or fear.",Arrays.asList(new Attack(1,2,null))),
            new Item("Slip-Thorn",cloak,Arrays.asList(agility),Slot.armor,3,92,true,"Before you suffer damage or fear, you may spend 1 inspiration to prevent 2 of that damage or fear.",Arrays.asList(new Attack(1,2,pierce))),
            new Item("Tuckborough Cloak",cloak,new ArrayList(),Slot.armor,2,65,false,"Before you suffer damage or fear, you may spend 1 inspiration to prevent 1 of that damage or fear. After setup become hidden.",new ArrayList()),
            new Item("Wind-Walker",cloak,new ArrayList(),Slot.armor,3,92,false,"Before you suffer damage or fear, you may spend 1 inspiration to prevent 1 of that damage or fear. Before you perform a travel action you may move 1 space. After setup become hidden.",new ArrayList()),
            new Item("Ranger Cloak",cloak,new ArrayList(),Slot.armor,2,65,false,"Before you suffer damage or fear, you may spend 1 inspiration to prevent 1 of that damage or fear. Before you suffer facedown damage or fear, you may prevent 1 of that damage or fear.",new ArrayList()),
            new Item("Splendor-Well",cloak,new ArrayList(),Slot.armor,3,92,false,"Before you suffer damage or fear, you may spend 1 inspiration to prevent 1 of that damage or fear. Before you suffer facedown damage or fear, you may prevent 1 of that damage or fear. After setup gain 3 inspiration",new ArrayList()),
            new Item("Ring Mail",ringmail,new ArrayList(),Slot.armor,1,26,false,"Decrease your agility by 1. If you would test wisdom, spirit or wit to negate, you may test might instead.",new ArrayList()),
            new Item("Evendim Ring Mail",ringmail,new ArrayList(),Slot.armor,2,62,false,"Decrease your agility by 1. Increase your fear limit by 1. If you would test wisdom, spirit or wit to negate, you may test might instead.",new ArrayList()),
            new Item("Wrath's-End",ringmail,new ArrayList(),Slot.armor,3,98,false,"Decrease your agility by 1. Increase your spirit by 1. Increase your damage and fear limits by 1. If you would test wisdom, spirit or wit to negate, you may test might instead.",new ArrayList()),
            new Item("Blade-Bane",ringmail,new ArrayList(),Slot.armor,3,98,false,"Decrease your agility by 1. Increase your damage and fear limits by 1. If you would test wisdom, spirit or wit to negate, you may test might instead. Suffer all damage facedown.",new ArrayList()),
            new Item("Travel Garb",garb,new ArrayList(),Slot.armor,1,24,false,"Increase your fear limit by 1. Before you suffer facedown damage, you may prevent 1 of that damage",new ArrayList()),
            new Item("Travel Garb",garb,new ArrayList(),Slot.armor,1,24,false,"Increase your fear limit by 1. Before you suffer facedown damage, you may prevent 1 of that damage",new ArrayList()),
            new Item("Greenwood Garb",garb,new ArrayList(),Slot.armor,2,57,false,"Increase your fear limit by 1. Before you suffer facedown damage, you may prevent 1 of that damage. After you explore a tile, you may spend 1 inspiration to scout 2.",new ArrayList()),
            new Item("Westmarch Garb",garb,new ArrayList(),Slot.armor,2,57,false,"Increase your fear and damage limits by 1. Before you suffer facedown damage, you may prevent 1 of that damage",new ArrayList()),
            new Item("Wanderer's Wish",garb,new ArrayList(),Slot.armor,3,88,false,"Increase your fear limit by 1. Before you suffer facedown damage, you may prevent 2 of that damage. After you explore a tile, you may spend 1 inspiration to scout 4",new ArrayList()),
            new Item("Ever-Bloom",garb,new ArrayList(),Slot.armor,3,88,false,"Increase your fear and damage limits by 1. Before you suffer facedown damage, you may prevent 1 of that damage. When damage or fear instruct you to discard inspiration or prepared cards, ignore those effects.",new ArrayList()),

            new Item("Extra Rations",ration, Slot.trinket, 1, 24,"At the start of the rally phase, you may deplete this item for a hero in your space to discard 1 damage.",2),
            new Item("Extra Rations",ration, Slot.trinket, 1, 24,"At the start of the rally phase, you may deplete this item for a hero in your space to discard 1 damage.",2),
            new Item("Breeland Rations",ration, Slot.trinket, 2, 48,"At the start of the rally phase, you may deplete this item for a hero in your space to discard 2 damage.",2),
            new Item("Hobbit Rations",ration, Slot.trinket, 2, 48,"At the start of the rally phase, you may deplete this item for a hero in your space to discard 1 damage.",4),
            new Item("Butterbur Biscuits",ration, Slot.trinket, 3, 78,"At the start of the rally phase, you may deplete this item for a hero in your space to become determined and discard up to  2 damage and fear",3),
            new Item("Tookish Apple Cakes",ration, Slot.trinket, 3, 78,"At the start of the rally phase, you may deplete this item for a heros in your space to collectively discard up to 2 damage and fear.",4),
            new Item("Old Map",map,Slot.trinket,1,21,"After you explore a tile, you may deplete this item to choose another hero to gain 1 inspiration.",3),
            new Item("Bounder's Map",map,Slot.trinket,2,64,"After you explore a tile or defeat an enemy group, you may deplete this item to choose another hero to gain 1 inspiration.",3),
            new Item("Bullroarer's Course",map,Slot.trinket,3,88,"After you explore a tile or defeat an enemy group, you may deplete this item to choose another hero to gain 1 inspiration. When you attack, you may deplete this item to add lethal",3),
            new Item("Tome",book,Slot.trinket,1,27,"When you scout, you may deplete this item to reveal 2 additional cards.",2),
            new Item("Tome of Battle",book,Slot.trinket,2,59,"When you scout, you may deplete this item to reveal 2 additional cards. If you do and there is an enemy in your space, gain 1 inspiration.",3),
            new Item("Legendarium of Thór",book,Slot.trinket,3,92,"When you scout, you may deplete this item to reveal 2 additional cards and prepare 1 additional card. If you do and there is an enemy in your space, gain 1 inspiration.",2),
            new Item("Brooch",brooch,Slot.trinket,1,26,"If you would suffer any amount of fear, you may deplete this item to suffer that fear facedown instead.",2),
            new Item("Eriador Brooch",brooch,Slot.trinket,2,54,"If you would suffer any amount of fear, you may deplete this item to suffer that fear facedown instead and prevent 1 of that fear.",2),
            new Item("Mark of Arnor",brooch,Slot.trinket,3,82,"If you would suffer any amount of fear, you may deplete this item to suffer that fear facedown instead and prevent 1 of that fear. During your turn, you may deplete this item to flip 1 damage or fear facedown.",3),
            new Item("Helmet", helmet, Slot.trinket,1,23,"If you would suffer any amaount of damage, you may deplete this item to suffer that damage facedown instead.",2),
            new Item("Dwarf-Forged Helmet", helmet, Slot.trinket,2,52,"If you would suffer any amaount of damage, you may deplete this item to suffer that damage facedown instead.",4),
            new Item("Fire-scale", helmet, Slot.trinket,1,23,"Increase your damage limit by 1. If you would suffer any amaount of damage, you may deplete this item to suffer that damage facedown instead.",4),
            new Item("Handkerchief",handkerchief,Slot.trinket,1,20,"After you interact with a person token, you may deplete this item to gain 1 inspiration and perform 1 additional action.",1),
            new Item("Heirloom Handkerchief",handkerchief,Slot.trinket,2,50,"After you interact with a person token, you may deplete this item to gain 1 inspiration, become determined, and perform 1 additional action.",2),
            new Item("Forget-Me-Never",handkerchief,Slot.trinket,3,85,"While there are no nearby heroes, increase your spirit by 1. After you interact with a person token, you may deplete this item to gain 1 inspiration, become determined, and perform 1 additional action.",2),
            new Item("Old Pipe", pipe, Slot.trinket,1,25,"When you scout during the rally phase, if there are no nearby enemies, you may deplete this item to prepare 1 additional card.",2),
            new Item("Long-Stemmed Pipe", pipe, Slot.trinket,2,65,"When you scout during the rally phase, if there are no nearby enemies, you may deplete this item to prepare 1 additional card and discard 2 facedown fear.",2),
            new Item("Storm-Maker", pipe, Slot.trinket,3,90,"When you scout during the rally phase, if there are no nearby enemies, you may deplete this item to prepare 1 additional card, discard 2 facedown fear, and gain a boon of your choice.",2),
            new Item("Rope", rope, Slot.trinket, 1,28, "When you interact with a token and test might or agility, you may deplete this item to convert 1 inspiration to 1 success.",2),
            new Item("Hobson Rope", rope, Slot.trinket, 2,70, "When you interact with a token, if you would test might, you may test agility instead. When you interact with a token and test might or agility, you may deplete this item to convert 1 inspiration to 1 success.",2),
            new Item("Ninnyhammer Braid", rope, Slot.trinket, 3,96, "When you interact with a token, if you would test might, you may test agility instead. When you interact with a token and test might or agility, you may deplete this item to convert 1 inspiration to 2 success.",2),
            new Item("Boots", boots,Slot.trinket, 1,22,"If you would test might or agility, you may deplete this item to test agility of might instead.",2),
            new Item("Greenway Boots", boots,Slot.trinket, 2,48,"If you would test might or agility, you may deplete this item to test agility of might instead. Before you move out of a space, you may deplete this item to not provoke attacks.",2),
            new Item("Dusk-Treaders", boots,Slot.trinket, 3,80,"If you would test might or agility, you may deplete this item to test agility of might instead. Before you move out of a space or interact with a token, you may deplete this item to not provoke attacks.",3),
            new Item("Torch",torch,Slot.trinket, 1,31,"You and other heroes in your space ignore darkness.",1),
            new Item("Weathertop Torch",torch,Slot.trinket, 2,64,"You and other heroes in your space ignore darkness. If you attack an enemy in your space, you may deplete this item to add pierce or smite.",2),
            new Item("Under-Sun",torch,Slot.trinket, 3,86,"You and other heroes in your space ignore darkness. If you attack an enemy in your space, you may deplete this item to add pierce, smite and stun.",2)
            ));


}
