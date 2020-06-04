package net.verplanmich.bot.game.waterdeep;

import java.util.ArrayList;
import java.util.List;

public class UserEntity {
    private String name;
    private String color;
    private int victory;
    private int gold;
    private int cleric;
    private int fighter;
    private int rogue;
    private int wizard;
    private boolean hasPassed;
    private List<String> plotQuests = new ArrayList<>();
    private List<String> activeQuests = new ArrayList<>();
    private int completedQuests;
    private int intrigues;

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

    public int getVictory() {
        return victory;
    }

    public UserEntity setVictory(int victory) {
        this.victory = victory;
        return this;
    }

    public int getGold() {
        return gold;
    }

    public UserEntity setGold(int gold) {
        this.gold = gold;
        return this;
    }

    public int getCleric() {
        return cleric;
    }

    public UserEntity setCleric(int cleric) {
        this.cleric = cleric;
        return this;
    }

    public int getFighter() {
        return fighter;
    }

    public UserEntity setFighter(int fighter) {
        this.fighter = fighter;
        return this;
    }

    public int getRogue() {
        return rogue;
    }

    public UserEntity setRogue(int rogue) {
        this.rogue = rogue;
        return this;
    }

    public int getWizard() {
        return wizard;
    }

    public UserEntity setWizard(int wizard) {
        this.wizard = wizard;
        return this;
    }

    public List<String> getPlotQuests() {
        return plotQuests;
    }

    public UserEntity setPlotQuests(List<String> plotQuests) {
        this.plotQuests = plotQuests;
        return this;
    }

    public List<String> getActiveQuests() {
        return activeQuests;
    }

    public UserEntity addActiveQuests(String activeQuest) {
        this.activeQuests.add(activeQuest);
        return this;
    }

    public int getCompletedQuests() {
        return completedQuests;
    }

    public UserEntity setCompletedQuests(int completedQuests) {
        this.completedQuests = completedQuests;
        return this;
    }

    public int getIntigues() {
        return intrigues;
    }

    public UserEntity setIntrigues(int intigues) {
        this.intrigues = intigues;
        return this;
    }

    public boolean isHasPassed() {
        return hasPassed;
    }

    public UserEntity setHasPassed(boolean hasPassed) {
        this.hasPassed = hasPassed;
        return this;
    }
}
