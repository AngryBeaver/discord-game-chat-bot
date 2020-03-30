package net.verplanmich.bot.game.zombicide;

public enum DangerLevel {
    BLUE(0),
    YELLOW(1),
    ORANGE(2),
    RED(3);

    private int level;
    DangerLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }
    public DangerLevel highest(DangerLevel dangerLevel){
        if(this.level > dangerLevel.getLevel()){
            return this;
        }else{
            return dangerLevel;
        }
    }

    public static DangerLevel getForXp(Integer xp){
        if(xp<7){
            return BLUE;
        }
        if(xp<19){
            return YELLOW;
        }
        if(xp<43){
            return ORANGE;
        }
        return RED;
    }
}
