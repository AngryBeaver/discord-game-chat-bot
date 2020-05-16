package net.verplanmich.bot.game.zombicide;

import net.verplanmich.bot.game.GameData;

import java.util.ArrayList;
import java.util.List;

public class User {

    private UserEntity userEntity;

    public User(GameData gameData, Survivor userChar){
        userEntity = new UserEntity(gameData,userChar);
    }

    void initialize(){
        userEntity.setGear(new ArrayList());
        userEntity.setZombified(false);
        userEntity.setXp(0);
    }

    public String getId(){
        return userEntity.getUserId();
    }

    public String getChar(){
        return userEntity.getUserChar();
    }

    public List<String> getGear(){
        return userEntity.getGear();
    }
    public boolean isStartPlayer(){
        return userEntity.isStartPlayer();
    }
    public void setStartPlayer(boolean isStartPlayer){
        userEntity.setStartPlayer(isStartPlayer);
    }

    public DangerLevel getDangerLevel(){
        return userEntity.getDangerLevel();
    }

    public UserEntity get(){
        return userEntity;
    }

    public void addXp(String xp){
        int nextXp = userEntity.getXp()+Integer.valueOf(xp);
        userEntity.setXp(nextXp);
        userEntity.setDangerLevel(DangerLevel.getForXp(nextXp));
    }

    public void add(String cardId) {
        userEntity.getGear().add(cardId);
    }

    public boolean drop(String cardId) {
        return userEntity.getGear().remove(cardId);
    }

    public boolean damage(){
        userEntity.getGear().add("wound");
        long damage = userEntity.getGear().stream().filter(cardId->cardId.equals("wound")).count();
        return (damage < 5);
    }

    public boolean die(){
        boolean result = userEntity.isZombified();
        if(!result) {
            userEntity.setZombified(true);
            userEntity.setGear(new ArrayList());
        }
        return result;
    }

    public void adjustAction(String action){
        Integer value = Integer.valueOf(action);
        if(value > 0){
            userEntity.setAction(userEntity.getAction() | value);
        }else{
            userEntity.setAction(userEntity.getAction() & (~(-128+value*-1)));
        }
    }


}
