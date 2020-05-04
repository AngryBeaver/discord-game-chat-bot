package net.verplanmich.bot.game.zombicide;

public enum Action {
    ACTION ("The Survivor has an extra Action he may use as he pleases"),
    COMBAT_ACTION ("The Survivor has an extra Combat Action"),
    MELEE_ACTION("The Survivor has an extra Melee Action"),
    MOVE_ACTION ("The Survivor has an extra Move Action"),
    RANGED_ACTION ("The Survivor has an extra Ranged Action"),

    MELEE_BLOODLUST ("Spend one Action with the Survivor: He moves up to two Zones to a Zone containing at least one Zombie. He then benefits of a free Melee Action"),

    MELEE_DAMAGE ("The Survivor gets a +1 Damage bonus with the specified type of Action"),

    COMBAT_DIE ("The Survivor’s weapons roll an extra die in Combat (Melee or Ranged). Dual weapons gain a die each, for a total of +2 dice per Dual Combat Action"),
    MELEE_DIE("The Survivor’s weapons roll an extra die in Melee Combat. Dual weapons gain a die each, for a total of +2 dice per Dual Combat Action"),


    COMBAT_RESULT (" The Survivor adds 1 to the result of each die he rolls on a Combat Action (Melee or Ranged).The maximum result is always 6."),
    MELEE_RESULT (" The Survivor adds 1 to the result of each die he rolls on a Melee Action.The maximum result is always 6."),
    RANGED_RESULT  (" The Survivor adds 1 to the result of each die he rolls on a Ranged Action.The maximum result is always 6."),

    AMBIDEXTROUS("The Survivor treats all Melee and Ranged weapons as if they had the Dual symbol"),
    BARBARIAN ("When resolving a Melee Action, the Survivor can replace the Dice number of the Melee weapon(s) he uses by the number of Actors standing in his Zone (including other Survivors and himself). Skills affecting the dice value, like +1 die: Melee, still apply."),
    HOARD ("The Survivor can carry one extra Equipment card in reserve"),

    LOW_PROFILE("The Survivor can’t be targeted by allied Ranged Attacks and can’t be hit by car attacks. Ignore him when shooting in or driving through the Zone he stands in. Weapons that kill everything in the targeted Zone, like the Molotov, still kill him."),
    LIVESAVER("The Survivor can use this Skill for free, once\n" +
            "during each of his turns. Select a Zone containing at least\n" +
            "one Zombie at Range 1 from your Survivor. All friendly Survivors in the selected Zone can be dragged to your Survivor’s\n" +
            "Zone without penalty. This is not a Movement. A Survivor\n" +
            "can decline the rescue and stay in the selected Zone if his\n" +
            "controller wants to. Both Zones need to share a clear path:\n" +
            "a Survivor can’t cross barricades, fences, or walls. Lifesaver\n" +
            "can’t be used by a Survivor in a car or in an observation\n" +
            "tower, nor can it be used to drag Survivors out of a car or an\n" +
            "observation tower"),
    MEDIC(" Once per turn, the Survivor can freely remove one\n"+
                  "Wounded card from a Survivor in the same Zone. He may\n"+
                  "also heal himself."),
    REGENERATION(" At the end of each game round, discard all\n" +
            "Wounds the Survivor received. Regeneration doesn’t work if\n" +
            "the Survivor has been eliminated."),
    ROTTEN("At the end of his turn, if the Survivor has not taken a\n" +
            "Combat Action, driven a car (passengers are not concerned),\n" +
            "and has not produced a Noise token, place a Rotten token\n" +
            "next to his base. As long as he has this token, he is totally\n" +
            "ignored by any and all types of Zombies (except Zombivors)\n" +
            "and is not considered a Noise token. Zombies don’t attack\n" +
            "him and will even walk past him. The Survivor loses his Rotten token if he takes any Combat Action or makes noise.\n" +
            "Even with the Rotten token, the Survivor still has to spend\n" +
            "extra Actions to move out of a Zone crowded with Zombies."),
    SHOVE (" The Survivor can use this Skill for free, once during\n" +
            "each of his turns. Select a Zone at Range 1 from your Survivor. All Zombies standing in your Survivor’s Zone are pushed\n" +
            "to the selected Zone. This is not a Movement. Both Zones\n" +
            "need to share a clear path: a Zombie can’t cross barricades,\n" +
            "fences, or walls. Shove can’t be used by Survivors in a car."),
    TOUGH ("The Survivor ignores the first Attack he receives\n" +
            "from a single Zombie every Zombies’ Phase."),
    TOXIC_IMUNITY("The Survivor is immune to Toxic Blood\n" +
            "Spray"),
    TRICK_SHOT("When the Survivor is equipped with Dual\n" +
            "Ranged weapons, he can aim at targets in different Zones\n" +
            "with each weapon in the same Action"),
    WEBBING( "All equipment in the Survivor’s inventory is considered equipped in hand."),

    ZOMBIE_LINK("The Survivor plays an extra turn each time an\n" +
            "extra activation card is drawn in the Zombie pile. He plays\n" +
            "before the extra-activated Zombies. If several Survivors\n" +
            "benefit from this Skill at the same time, the players choose\n" +
            "their activation order");

    String text;

    Action(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }
}
