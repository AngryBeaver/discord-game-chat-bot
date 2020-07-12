package net.verplanmich.bot.game.gaia;

import net.verplanmich.bot.game.spiritisland.Skill;

import java.util.ArrayList;
import java.util.List;

public class UserEntity {
    private String id;
    private String name;
    private String color;
    private String avatar;
    private int energy;
    private List<String> presence1 = new ArrayList<>();
    private List<String> presence2 = new ArrayList<>();
    private List<String> hand = new ArrayList<>();
    private List<String> play = new ArrayList<>();
    private List<String> discard = new ArrayList<>();

    public UserEntity(){

    }


    public UserEntity(UserEntity userEntity){
        id=userEntity.getId();
        name=userEntity.getName();
        color=userEntity.getColor();
        avatar = userEntity.getAvatar();
        energy = userEntity.getEnergy();
        presence1 = userEntity.getPresence1();
        presence2 = userEntity.getPresence2();
        hand = new ArrayList(userEntity.getHand());
        play = new ArrayList(userEntity.getPlay());
        discard = new ArrayList(userEntity.getDiscard());
    }

    public String getName() {
        return name;
    }

    public UserEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getColor() {
        return color;
    }

    public UserEntity setColor(String color) {
        this.color = color;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public List<String>  getPresence1() {
        return presence1;
    }

    public void setPresence1(List<String> presence1) {
        this.presence1 = presence1;
    }

    public List<String> getPresence2() {
        return presence2;
    }

    public void setPresence2(List<String> presence2) {
        this.presence2 = presence2;
    }

    public List<String> getHand() {
        return hand;
    }

    public void setHand(List<String> hand) {
        this.hand = hand;
    }

    public List<String> getPlay() {
        return play;
    }

    public void setPlay(List<String> play) {
        this.play = play;
    }

    public List<String> getDiscard() {
        return discard;
    }

    public void setDiscard(List<String> discard) {
        this.discard = discard;
    }
}
