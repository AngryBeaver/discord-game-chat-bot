package net.verplanmich.bot.game.waterdeep;

import net.verplanmich.bot.game.Game;
import net.verplanmich.bot.game.GameData;
import net.verplanmich.bot.game.GameMethod;
import net.verplanmich.bot.game.GameResult;
import net.verplanmich.bot.game.clank.GameDecks;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope("prototype")
public class Waterdeep implements Game {

    private GameDecks gameDecks;
    private Map<String, User> users = new HashMap();
    private List<String> colors = new ArrayList(Arrays.asList("red","green","blue","black","yellow"));

    @Override
    public String getName() {
        return "waterdeep";
    }

    public Waterdeep(){
        this.gameDecks = new GameDecks();
        Collections.shuffle(colors);
    }

    @GameMethod
    public GameResult join(GameData gameData) {
        if(!users.containsKey(gameData.getUserId())) {
            String color = colors.remove(0);
            User user = new User(gameData.getUserName(),gameData.getUserId(),color);
            users.put(gameData.getUserId(), user);
            int amount = 5-userOrder.size();
            return adjustClank(gameData.getUserId(),String.valueOf(amount))
                    .setText(gameData.getUserName() + " joined as "+ color)
                    .addEvent(EVENT_JOIN);
        }
        return new GameResult()
                .setText(gameData.getUserName() + " is highly interested in this game");
    }
}
