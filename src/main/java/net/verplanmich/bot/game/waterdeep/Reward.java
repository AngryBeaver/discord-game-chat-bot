package net.verplanmich.bot.game.waterdeep;

import net.verplanmich.bot.game.GameResult;

public interface Reward {

    GameResult consume(User user,GameDecks gameDecks);

}
