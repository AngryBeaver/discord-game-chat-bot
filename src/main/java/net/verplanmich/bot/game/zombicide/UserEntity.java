package net.verplanmich.bot.game.zombicide;

import net.verplanmich.bot.game.GameData;

import java.util.ArrayList;
import java.util.List;

public class UserEntity {
    private final String userName;
    private final String userId;
    private final String userChar;
    private final Survivor survivor;
    private Integer xp = 0;
    private DangerLevel dangerLevel;

    private boolean isStartPlayer = false;
    private boolean isZombified = false;
    private List<String> gear = new ArrayList();



    public UserEntity(GameData gameData, Survivor survivor) {
        this.userName = gameData.getUserName();
        this.userId = gameData.getUserId();
        this.userChar = survivor.getName();
        this.survivor = survivor;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserChar() {
        if(isZombified){
            return userChar+"-z";
        }
        return userChar;
    }

    public Survivor getSurvivor() {
        return survivor;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    public DangerLevel getDangerLevel() {
        return DangerLevel.getForXp(xp);
    }

    public void setDangerLevel(DangerLevel dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public boolean isZombified() {
        return isZombified;
    }

    public void setZombified(boolean dead) {
        isZombified = dead;
    }

    public List<String> getGear() {
        return gear;
    }

    public void setGear(List<String> gear) {
        this.gear = gear;
    }

    public boolean isStartPlayer() {
        return isStartPlayer;
    }

    public void setStartPlayer(boolean startPlayer) {
        isStartPlayer = startPlayer;
    }

}
