package net.verplanmich.bot.game.clank;

import net.minidev.json.JSONObject;
import net.verplanmich.bot.game.Game;

import static net.verplanmich.bot.game.clank.CardType.NORMAL;

public class Card {

    public Card(String name, CardType type){
        this.name = name;
        this.type = type;
    }

    String name;
    CardType type;

    GameAttributes cost = new GameAttributes();
    GameAttributes use = new GameAttributes();

    public String getName() {
        return name;
    }

    public Card setName(String name) {
        this.name = name;
        return this;
    }

    public CardType getType() {
        return type;
    }

    public Card setType(CardType type) {
        this.type = type;
        return this;
    }

    public GameAttributes getCost() {
        return cost;
    }

    public Card setCost(GameAttributes cost) {
        this.cost = cost;
        return this;
    }

    public GameAttributes getUse() {
        return use;
    }

    public Card setUse(GameAttributes use) {
        this.use = use;
        return this;
    }
}
