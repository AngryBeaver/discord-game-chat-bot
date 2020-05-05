package net.verplanmich.bot.game.zombicide;

public enum Action {
    ACTION ("The Survivor has an extra Action he may use as he pleases"),
    COMBAT_ACTION ("The Survivor has an extra Combat Action"),
    MELEE_ACTION("The Survivor has an extra Melee Action"),
    MOVE_ACTION ("The Survivor has an extra Move Action"),
    RANGED_ACTION ("The Survivor has an extra Ranged Action"),
    SEARCH_ACTION ("The Survivor has one free extra\n" +
            "Search Action. This Action may only be used to Search, and\n" +
            "the Survivor can still only Search once per turn"),

    COMBAT_BLOODLUST ("Spend one Action with the Survivor:\n" +
            "He moves up to two Zones to a Zone containing at least one\n" +
            "Zombie. He then benefits of a free Combat Action."),
    MELEE_BLOODLUST ("Spend one Action with the Survivor: He moves up to two Zones to a Zone containing at least one Zombie. He then benefits of a free Melee Action"),
    RANGED_BLOODLUST (" Spend one Action with the Survivor:\n" +
            "He moves up to two Zones to a Zone containing at least one\n" +
            "Zombie. He then benefits of a free Ranged Action."),

    MELEE_DAMAGE ("The Survivor gets a +1 Damage bonus with the specified type of Action"),
    RANGED_DAMAGE ("The Survivor gets a +1 Damage bonus with the specified type of Action"),
    COMBAT_DAMAGE ("The Survivor gets a +1 Damage bonus with the specified type of Action"),

    COMBAT_DIE ("The Survivor’s weapons roll an extra die in Combat (Melee or Ranged). Dual weapons gain a die each, for a total of +2 dice per Dual Combat Action"),
    MELEE_DIE("The Survivor’s weapons roll an extra die in Melee Combat. Dual weapons gain a die each, for a total of +2 dice per Dual Combat Action"),
    RANGED_DIE(" The Survivor’s Ranged weapons roll an\n" +
            "extra die in Combat. Dual Ranged weapons gain a die each,\n" +
            "for a total of +2 dice per Dual Ranged Combat Action"),

    COMBAT_RESULT (" The Survivor adds 1 to the result of each die he rolls on a Combat Action (Melee or Ranged).The maximum result is always 6."),
    MELEE_RESULT (" The Survivor adds 1 to the result of each die he rolls on a Melee Action.The maximum result is always 6."),
    RANGED_RESULT  (" The Survivor adds 1 to the result of each die he rolls on a Ranged Action.The maximum result is always 6."),

    AMBIDEXTROUS("The Survivor treats all Melee and Ranged weapons as if they had the Dual symbol"),
    BARBARIAN ("When resolving a Melee Action, the Survivor can replace the Dice number of the Melee weapon(s) he uses by the number of Actors standing in his Zone (including other Survivors and himself). Skills affecting the dice value, like +1 die: Melee, still apply."),

    RANGE("The Survivor’s Ranged weapons’ maximum Range is increased by 1."),

    RE_ROLL(" Once per turn, you can re-roll all the dice\n" +
            "related to the resolution of an Action made by the Survivor.\n" +
            "The new result takes the place of the previous one. This Skill\n" +
            "stacks with the effects of Equipment that allow re-rolls."),
    FAST("When the Survivor spends one\n" +
            "Action to Move, he can move one or two Zones instead of\n" +
            "one"),
    BLITZ("Each time your Survivor kills the last Zombie of a\n" +
            "Zone, he gets 1 free Move Action to use immediately"),
    BORN_LEADER(" During the Survivor’s turn, he may give one free\n" +
            "Action to another Survivor, to use as he pleases. This Action\n" +
            "must be used during the recipient’s next turn or it is lost."),
    BREAK_IN("The Survivor doesn’t need any Equipment to\n" +
            "open doors. He doesn’t make Noise while using this Skill.\n" +
            "However, other prerequisites are still mandatory (such as\n" +
            "taking a designated Objective). Moreover, the Survivor has\n" +
            "one extra, free Door opening Action. This Action can only be\n" +
            "used to open doors.\n" +
            "Please note the Break-in Skill doesn’t apply to removing barricades (see Toxic City Mall)."),
    DANGER_LEVEL("The Survivor can begin the\n" +
            "game at yellow Danger Level (first experience point of\n" +
            "the indicated Danger Level). All players have to agree."),
    RUNNER_COLLECTOR("The Survivor doubles the experience gained each time he kills a Zombie of the specified\n" +
            "type."),
    FATTY("The Survivor doubles the experience gained each time he kills a Zombie of the specified\n" +
            "type."),
    WALKER_COLLECTOR("The Survivor doubles the experience gained each time he kills a Zombie of the specified\n" +
            "type."),
    DEATH_GRASP("Don’t discard an Equipment card when the\n" +
            "Survivor receives a Wounded card. This Skill is ignored if\n" +
            "there’s no space left in the Inventory to receive the Wounded\n" +
            "card."),
    DESTINY("The Survivor can use this Skill once per turn when\n" +
            "he reveals an Equipment card he drew. Discard that card\n" +
            "and draw another Equipment card."),
    DISTRIBUTOR(" Using this Skill alters the Spawn step during\n" +
            "the Zombies’ Phase. When resolving the Spawn step during\n" +
            "the Zombies’ Phase, draw as many Zombie cards as there\n" +
            "are Spawn Zones. Assign each of them freely to a Spawn\n" +
            "Zone, then spawn Zombies as indicated."),
    DREADNOUGHT_RUNNER("– All weapons the Survivor carries gain +1\n" +
            "die per Wound the Survivor suffers. Dual weapons gain a die\n" +
            "each, for a total of +2 dice per Wound and per Dual Combat\n" +
            "Action."),
    DREADNOUGHT_FATTY("– All weapons the Survivor carries gain +1\n" +
            "die per Wound the Survivor suffers. Dual weapons gain a die\n" +
            "each, for a total of +2 dice per Wound and per Dual Combat\n" +
            "Action."),
    DREADNOUGHT_WALKER(" All weapons the Survivor carries gain +1\n" +
            "die per Wound the Survivor suffers. Dual weapons gain a die\n" +
            "each, for a total of +2 dice per Wound and per Dual Combat\n" +
            "Action."),

    COMBAT_FRENZY("All weapons the Survivor carries gain +1\n" +
            "die per Wound the Survivor suffers. Dual weapons gain a die\n" +
            "each, for a total of +2 dice per Wound and per Dual Combat\n" +
            "Action"),
    MELEE_FRENZY("Melee weapons the Survivor carries gain\n" +
            "+1 die per Wound the Survivor suffers. Dual Melee weapons\n" +
            "gain a die each, for a total of +2 dice per Wound and per Dual\n" +
            "Melee Combat Action."),
    RANGED_FRENZY("Ranged weapons the Survivor carries\n" +
            "gain +1 die per Wound the Survivor suffers. Dual Ranged\n" +
            "weapons gain a die each, for a total of +2 dice per Wound\n" +
            "and per Dual Ranged Combat Action"),
    GUNSLINGER("The Survivor treats all Ranged weapons as if\n" +
            "they had the Dual symbo"),
    HOARD ("The Survivor can carry one extra Equipment card in reserve"),
    VULTURE (" This Skill can be used once per turn. The\n" +
            "Survivor gets a free Search Action in the Zone if he has eliminated a Zombie (even outside a building) the very same turn.\n" +
            "This Action may only be used to Search and the Survivor can\n" +
            "still only Search once per turn"),
    JUMP(" The Survivor can use this Skill once during each Activation. The Survivor spends one Action: He moves two Zones into a Zone to which he has Line of Sight. Movement related Skills (like +1 Zone per Move Action or Slippery) are ignored, but Movement penalties (like having Zombies in the starting Zone) apply. Ignore all Actors, barricades, and holes in the intervening Zone (see the Angry Neighbors expansion). Any obstacles other than Actors, barricades, and holes in the intervening Zone prevent the use of this Skill."),
    THICK_SKIN("You can use this Skill any time the\n" +
            "Survivor is about to get Wounded cards. Discard one Equipment card in your Survivor’s inventory for each Wound he’s\n" +
            "about to receive. Negate a Wounded card per discarded\n" +
            "Equipment card."),
    LIFESAVER("The Survivor can use this Skill for free, once\n" +
            "during each of his turns. Select a Zone containing at least\n" +
            "one Zombie at Range 1 from your Survivor. All friendly Survivors in the selected Zone can be dragged to your Survivor’s\n" +
            "Zone without penalty. This is not a Movement. A Survivor\n" +
            "can decline the rescue and stay in the selected Zone if his\n" +
            "controller wants to. Both Zones need to share a clear path:\n" +
            "a Survivor can’t cross barricades, fences, or walls. Lifesaver\n" +
            "can’t be used by a Survivor in a car or in an observation\n" +
            "tower, nor can it be used to drag Survivors out of a car or an\n" +
            "observation tower"),
    LOCK_IT (" At the cost of one Action, the Survivor can\n" +
            "close an open door on his Zone. Opening it again later does\n" +
            "not trigger a new Zombie Spawn"),
    LOUD(" Once per turn, the Survivor can make a huge amount\n" +
            "of noise! Until this Survivor’s next turn, the Zone he used this\n" +
            "Skill in is considered to have the highest number of Noise tokens on the entire map. If different Survivors have this Skill,\n" +
            "only the last one who used it applies the effects."),
    LOW_PROFILE("The Survivor can’t be targeted by allied Ranged Attacks and can’t be hit by car attacks. Ignore him when shooting in or driving through the Zone he stands in. Weapons that kill everything in the targeted Zone, like the Molotov, still kill him."),
    LUCKY(" The Survivor can re-roll once all the dice of each Action he takes. The new result takes the place of the previous\n" +
            "one. This Skill stacks with the effects of other Skills (“1 re-roll\n" +
            "per turn”, for example) and Equipment that allows re-rolls."),
    MATCHING_SET(" When a Survivor performs a Search Action\n" +
            "and draws a weapon card with the Dual symbol\n" +
            ", he can immediately take a second card\n" +
            "of the same type from the Equipment deck. Shuffle the deck"),
    MEDIC(" Once per turn, the Survivor can freely remove one\n"+
                  "Wounded card from a Survivor in the same Zone. He may\n"+
                  "also heal himself."),
    NINJA("The Survivor makes no Noise. At all. His miniature\n" +
            "does not count as a Noise token, and his use of Equipment\n" +
            "or weapons produces no Noise tokens either! The Survivor\n" +
            "may choose not to use this Skill at any time, if he wishes to\n" +
            "be noisy."),
    POINT_BLANK ("When firing at Range 0, the Survivor chooses\n" +
            "freely the targets of his Ranged Combat Actions and can\n" +
            "kill any type of Zombies (including Berserker Zombies). His\n" +
            "Ranged weapons still need to inflict enough Damage to kill\n" +
            "his targets."),
    COMBAT_REAPER ("Use this Skill when assigning hits while\n" +
            "resolving a Combat Action. One of these hits can freely kill an\n" +
            "additional identical Zombie in the same Zone. Only a single additional Zombie can be killed per Action when using this Skill."),
    MELEE_REAPER ("Use this Skill when assigning hits while\n" +
            "resolving a Melee Action. One of these hits can freely kill an\n" +
            "additional identical Zombie in the same Zone. Only a single additional Zombie can be killed per Action when using this Skill"),
    RANGED_REAPER ("Use this Skill when assigning hits while\n" +
            "resolving a Ranged Action. One of these hits can freely kill\n" +
            "an additional identical Zombie in the same Zone. Only a\n" +
            "single additional Zombie can be killed per Action when using\n" +
            "this Skill."),
    COMBAT_CRITICAL ("You may roll an additional die for\n" +
            "each “6” rolled on any attack, Melee or Ranged. Keep on\n" +
            "rolling additional dice as long as you keep getting “6”. Game\n" +
            "effects that allow re-rolls (the “1 re-roll per turn” Skill or the\n" +
            "“Plenty of ammo” Equipment card, for example) must be\n" +
            "used before rolling any additional dice for this Skill."),
    MELEE_CRITICAL ("You may roll an additional die for each\n" +
            "“6” rolled on a Melee attack. Keep on rolling additional dice\n" +
            "as long as you keep getting “6”. Game effects that allow rerolls (the “1 re-roll per turn” Skill, for example) must be used\n" +
            "before rolling any additional dice for this Skill."),
    RANGED_CRITICAL ("You may roll an additional die for\n" +
            "each “6” rolled on a Ranged attack. Keep on rolling additional dice as long as you keep getting “6”. Game effects\n" +
            "that allow re-rolls (the “1 re-roll per turn” Skill or the “Plenty\n" +
            "of ammo” Equipment card, for example) must be used before rolling any additional dice for this Skill."),
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
    SCAVENGER("The Survivor can Search in any Zone. This includes street Zones, indoor alleys, hospital Zones, helipads, tents, etc…"),
    SEARCH ("+1 Card Draw an extra card when searching with the survivor"),
    SHOVE (" The Survivor can use this Skill for free, once during\n" +
            "each of his turns. Select a Zone at Range 1 from your Survivor. All Zombies standing in your Survivor’s Zone are pushed\n" +
            "to the selected Zone. This is not a Movement. Both Zones\n" +
            "need to share a clear path: a Zombie can’t cross barricades,\n" +
            "fences, or walls. Shove can’t be used by Survivors in a car."),
    SLIPPERY (" The Survivor does not spend extra Actions when\n" +
            "he performs a Move Action through a Zone where there are\n" +
            "Zombies."),
    SPRINT ("The Survivor can use this Skill once during each of his Activations. Spend one Move Action with the Survivor: He may move one, two, or three Zones instead of one. Entering a Zone containing Zombies ends the Survivor’s Move Action."),
    SNIPER (" The Survivor may freely choose the targets of all his\n" +
            "Ranged Combat Actions"),
    SUPER_STRENGTH("Consider the Damage value of Melee\n" +
            "            weapons used by the Survivor to be 3."),
    STEADY_HAND("Allied miniatures are ignored when the Survivor uses a Ranged Attack or drives a car through a Zone. The\n" +
            "Skill does not apply to a Ranged weapon killing everything in\n" +
            "the targeted Zone (such as a Molotov cocktail, for example)."),
    SWORDMASTER(" The Survivor treats all Melee weapons as if they had the Dual symbol "),
    TACTICIAN ("The Survivor’s turn can be resolved anytime\n" +
            "during the Players’ Phase, before or after any other Survivor’s turn."),
    TAUNT (" The Survivor can use this Skill for free, once during\n" +
            "each of his turns. Select a Zone your Survivor can see. All\n" +
            "Zombies standing in the selected Zone immediately gain an\n" +
            "extra activation: they try to reach the taunting Survivor by\n" +
            "all means available. Taunted Zombies ignore all other Survivors: they do not attack them and cross the Zone they stand\n" +
            "in if needed to reach the taunting Survivor."),

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
