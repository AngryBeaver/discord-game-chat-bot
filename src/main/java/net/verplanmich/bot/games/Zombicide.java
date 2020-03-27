package net.verplanmich.bot.games;

import java.io.IOException;

public class Zombicide implements Game{
    private Deck equipment;
    private Deck spawn;

    public Zombicide() throws IOException {
        equipment = Deck.getFor("zombicide", "equipment");
        spawn = Deck.getFor("zombicide", "spawn");
    }

    @GameMethod
    public String drawEquipmentCard(){
        return equipment.drawCard();
    }

    @GameMethod
    public String drawSpawnCard(){
        String cardId = spawn.drawCard();
        spawn.discardCard(cardId);
        return cardId;
    }
}
