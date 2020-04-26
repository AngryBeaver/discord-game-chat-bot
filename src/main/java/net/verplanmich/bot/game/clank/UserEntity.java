package net.verplanmich.bot.game.clank;

import java.util.ArrayList;
import java.util.List;

public class UserEntity {
    private final String userName;
    private final String userId;
    private final String userChar;
    private int coins = 0;
    private int clankCubes = 30;
    private int damage = 0;
    private List<String> items = new ArrayList();

    public UserEntity(String userName,String userId,String userChar) {
        this.userName = userName;
        this.userId = userId;
        this.userChar = userChar;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserChar() {
        return userChar;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getClankCubes() {
        return clankCubes;
    }

    public void setClankCubes(int clankCubes) {
        this.clankCubes = clankCubes;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }



}
