package net.verplanmich.bot.game.spiritisland;

import net.verplanmich.bot.game.waterdeep.Quest;

public class Skill {


    enum Type{
        MINOR,
        MAJOR,
        EARTH,
        WATER,
        LIGHTNING,
        SHADE
    };
    private String name;
    private Skill.Type type;

    public Skill(String name,Skill.Type type){
        this.name = name;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public Skill.Type getType() {
        return type;
    }

}
