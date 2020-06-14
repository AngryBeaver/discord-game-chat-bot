package net.verplanmich.bot.game.waterdeep;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<String> quests = new ArrayList();
    private List<String> intrigues = new ArrayList();
    private String identity;
    private UserEntity userEntity;

    public User(String id, String name,String color){
        userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setName(name);
        userEntity.setColor(color);
    }

    public String getId(){
        return userEntity.getId();
    }

    public UserEntity getUserEntity(){return userEntity;}


    public List<String> getIntrigues() {
        return intrigues;
    }

    public void addIntrigues(String intrigue) {
        this.intrigues.add(intrigue);
        this.userEntity.setIntrigues(this.intrigues.size());
    }

    public boolean removeIntrigues(String intrigue) {
        if(this.intrigues.remove(intrigue)) {
            this.userEntity.setIntrigues(this.intrigues.size());
            return true;
        }
        return false;
    }

    public void adjustGold(String value){
        int intValue = Integer.valueOf(value);
        this.userEntity.setGold(this.userEntity.getGold()+intValue);
    }
    public void adjustCleric(String value){
        int intValue = Integer.valueOf(value);
        this.userEntity.setCleric(this.userEntity.getCleric()+intValue);
    }
    public void adjustFighter(String value){
        int intValue = Integer.valueOf(value);
        this.userEntity.setFighter(this.userEntity.getFighter()+intValue);
    }
    public void adjustWizard(String value){
        int intValue = Integer.valueOf(value);
        this.userEntity.setWizard(this.userEntity.getWizard()+intValue);
    }
    public void adjustRogue(String value){
        int intValue = Integer.valueOf(value);
        this.userEntity.setRogue(this.userEntity.getRogue()+intValue);
    }
    public void adjustVictory(String value){
        int intValue = Integer.valueOf(value);
        this.userEntity.setVictory(this.userEntity.getVictory()+intValue);
    }

    public List<String> getQuests() {
        return quests;
    }

    public void addQuests(String quest) {
        this.quests.add(quest);
        this.userEntity.setCompletedQuests(this.quests.size());
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
