package net.verplanmich.bot.game.clank;

public class GameAttributes {
    int move = 0;
    int skill = 0;
    int attack = 0;
    int coins = 0;
    int health = 0;
    int clank = 0;
    int value = 0;

    public int getMove() {
        return move;
    }

    public GameAttributes setMove(int move) {
        this.move = move;
        return this;
    }

    public int getSkill() {
        return skill;
    }

    public GameAttributes setSkill(int skill) {
        this.skill = skill;
        return this;
    }

    public int getAttack() {
        return attack;
    }

    public GameAttributes setAttack(int attack) {
        this.attack = attack;
        return this;
    }

    public int getCoins() {
        return coins;
    }

    public GameAttributes setCoins(int coins) {
        this.coins = coins;
        return this;
    }

    public int getHealth() {
        return health;
    }

    public GameAttributes setHealth(int health) {
        this.health = health;
        return this;
    }

    public int getClank() {
        return clank;
    }

    public GameAttributes setClank(int clank) {
        this.clank = clank;
        return this;
    }
}
