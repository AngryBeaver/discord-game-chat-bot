package net.verplanmich.bot.game.gaia;

import net.verplanmich.bot.game.spiritisland.Skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserEntity {
    private String id;
    private String name;
    private String color;
    private String avatar;
    private List<String> mines = new ArrayList<>(Arrays.asList("","","","","","","",""));
    private List<String> trades = new ArrayList<>(Arrays.asList("","","",""));
    private List<String> laboratories = new ArrayList<>(Arrays.asList("","",""));
    private boolean academy1;
    private boolean academy2;
    private boolean station;
    private List<String> terraformers = new ArrayList<>(Arrays.asList("","",""));
    private int victory;
    private int ore;
    private int credit;
    private int knowledge;
    private int qic;
    private int might0;
    private int might1;
    private int might2;
    private int might3;
    private int brainstone0;
    private int brainstone1;
    private int brainstone2;
    private int brainstone3;
    private List<Integer> tech = Arrays.asList(0,0,0,0,0,0);
    private List<String> techCards = new ArrayList();
    private List<String> alliances = new ArrayList();
    private String statsAxis;
    private boolean hasPassed;
    private boolean startPlayer;
    private int finalVictory;
    public UserEntity(){

    }


    public UserEntity(UserEntity userEntity){
        setId(userEntity.getId());
        setName(userEntity.getName());
        setColor(userEntity.getColor());
        setAvatar(userEntity.getAvatar());
        setVictory(userEntity.getVictory());
        setOre(userEntity.getOre());
        setCredit(userEntity.getCredit());
        setKnowledge(userEntity.getKnowledge());
        setQic(userEntity.getQic());
        setMight0(userEntity.getMight0());
        setMight1(userEntity.getMight1());
        setMight2(userEntity.getMight2());
        setMight3(userEntity.getMight3());
        setStatsAxis(userEntity.getStatsAxis());
        setHasPassed(userEntity.isHasPassed());
        setStartPlayer(userEntity.isStartPlayer());
        List<Integer> tech = userEntity.getTech();
        setFinalVictory(victory+(int)(Math.floor((credit+knowledge+ore+qic+might3+might2/2)/3)+brainstone3)
        +Math.max(0,(tech.get(0)-2)*4)+Math.max(0,(tech.get(1)-2)*4)+Math.max(0,(tech.get(2)-2)*4)
                +Math.max(0,(tech.get(3)-2)*4)+Math.max(0,(tech.get(4)-2)*4)+Math.max(0,(tech.get(5)-2)*4));
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

    public UserEntity setId(String id) {
        this.id = id;
        return this;
    }


    public String getAvatar() {
        return avatar;
    }

    public UserEntity setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }


    public int getVictory() {
        return victory;
    }

    public UserEntity setVictory(int victory) {
        this.victory = victory;
        return this;
    }

    public int getOre() {
        return ore;
    }

    public UserEntity setOre(int ore) {
        this.ore = ore;
        return this;
    }

    public int getCredit() {
        return credit;
    }

    public UserEntity setCredit(int credit) {
        this.credit = credit;
        return this;
    }

    public int getKnowledge() {
        return knowledge;
    }

    public UserEntity setKnowledge(int knowledge) {
        this.knowledge = knowledge;
        return this;
    }

    public int getQic() {
        return qic;
    }

    public UserEntity setQic(int qic) {
        this.qic = qic;
        return this;
    }

    public int getMight0() {
        return might0;
    }

    public UserEntity setMight0(int might0) {
        this.might0 = might0;
        return this;
    }

    public int getMight1() {
        return might1;
    }

    public UserEntity setMight1(int might1) {
        this.might1 = might1;
        return this;
    }

    public int getMight2() {
        return might2;
    }

    public UserEntity setMight2(int might2) {
        this.might2 = might2;
        return this;
    }

    public int getMight3() {
        return might3;
    }

    public UserEntity setMight3(int might3) {
        this.might3 = might3;
        return this;
    }

    public int getBrainstone0() {
        return brainstone0;
    }

    public UserEntity setBrainstone0(int brainstone0) {
        this.brainstone0 = brainstone0;
        return this;
    }

    public int getBrainstone1() {
        return brainstone1;
    }

    public UserEntity setBrainstone1(int brainstone1) {
        this.brainstone1 = brainstone1;
        return this;
    }

    public int getBrainstone2() {
        return brainstone2;
    }

    public UserEntity setBrainstone2(int brainstone2) {
        this.brainstone2 = brainstone2;
        return this;
    }

    public int getBrainstone3() {
        return brainstone3;
    }

    public UserEntity setBrainstone3(int brainstone3) {
        this.brainstone3 = brainstone3;
        return this;
    }


    public boolean isAcademy1() {
        return academy1;
    }

    public UserEntity setAcademy1(boolean academy1) {
        this.academy1 = academy1;
        return this;
    }

    public boolean isAcademy2() {
        return academy2;
    }

    public UserEntity setAcademy2(boolean academy2) {
        this.academy2 = academy2;
        return this;
    }

    public List<String> getMines() {
        return mines;
    }

    public UserEntity setMines() {
        this.mines = mines;
        return this;
    }

    public List<String> getTrades() {
        return trades;
    }

    public UserEntity setTrades(List<String> trades) {
        this.trades = trades;
        return this;
    }

    public List<String> getLaboratories() {
        return laboratories;
    }

    public UserEntity setLaboratories(List<String> laboratories) {
        this.laboratories = laboratories;
        return this;
    }

    public List<Integer> getTech() {
        return tech;
    }

    public UserEntity setTech(List<Integer> tech) {
        this.tech = tech;
        return this;
    }

    public List<String> getTechCards() {
        return techCards;
    }

    public UserEntity addTechCard(String cardId) {
        this.techCards.add(cardId);
        return this;
    }

    public UserEntity setTechCards(List<String> techCards) {
        this.techCards = techCards;
        return this;
    }

    public List<String> getAlliances() {
        return alliances;
    }

    public UserEntity addAlliance(String alliance) {
        alliances.add(alliance);
        return this;
    }

    public UserEntity setAlliances(List<String> alliances) {
        this.alliances = alliances;
        return this;
    }

    public List<String> getTerraformers() {
        return terraformers;
    }

    public UserEntity setTerraformers(List<String> terraformers) {
        this.terraformers = terraformers;
        return this;
    }

    public String getStatsAxis() {
        return statsAxis;
    }

    public void setStatsAxis(String statsAxis) {
        this.statsAxis = statsAxis;
    }

    public boolean isHasPassed() {
        return hasPassed;
    }

    public void setHasPassed(boolean hasPassed) {
        this.hasPassed = hasPassed;
    }

    public boolean isStartPlayer() {
        return startPlayer;
    }

    public void setStartPlayer(boolean startPlayer) {
        this.startPlayer = startPlayer;
    }

    public boolean isStation() {
        return station;
    }

    public void setStation(boolean station) {
        this.station = station;
    }

    public int getFinalVictory() {
        return finalVictory;
    }

    public void setFinalVictory(int finalVictory) {
        this.finalVictory = finalVictory;
    }
}
