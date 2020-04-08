package net.verplanmich.bot.game.zombicide;

import net.verplanmich.bot.game.Deck;
import net.verplanmich.bot.game.Game;
import net.verplanmich.bot.game.GameMethod;
import net.verplanmich.bot.game.GameResult;
import net.verplanmich.bot.game.zombicide.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

@Component
public class Zombicide implements Game {

    static final String NAME = "zombicide";
    static final String GEAR = "gear";
    static final String SPAWN = "spawn";
    static final String CHARS = "chars";
    private Deck gear;
    private Deck spawn;
    private Deck chars;
    private Map<String, User> users = new HashMap();
    private DangerLevel dangerLevel = DangerLevel.BLUE;;

    public Zombicide() throws IOException {
        gear = Deck.getFor(NAME, GEAR);
        spawn = Deck.getFor(NAME, SPAWN);
        chars = Deck.getFor(NAME, CHARS);
    }

    public String getName(){
        return NAME;
    }

    public User getUser(String userId){
        if(users.get(userId)==null) {
            users.put(userId, new User(chars.drawCard()));
        }
        return users.get(userId);
    }

    @GameMethod()
    public GameResult gear(String userId){
        return getUser(userId).gear();
    }

    @GameMethod()
    public GameResult add(String userId,String cardId) {
        return getUser(userId).add(cardId);
    }

    @GameMethod()
    public GameResult drop(String userId,String cardId) {
        return getUser(userId).drop(cardId);
    }

    @GameMethod
    public GameResult search(String userId) {
        String cardId = gear.drawCard();
        return getUser(userId).add(cardId);
    }

    @GameMethod
    public GameResult info(String userId) {
        return getUser(userId).info();
    }

    @GameMethod
    public GameResult xp(String userId, String xp) {
        return getUser(userId).addXp(xp);
    }

    @GameMethod
    public GameResult die(String userId) {
        return getUser(userId).die();
    }

    @GameMethod
    public GameResult roll(String amount) {
        GameResult gameResult = new GameResult();
        Integer intAmount = Integer.valueOf(amount);
        String text = "[";
        List<String> list = new ArrayList();
        for(int i=0; i<Math.min(intAmount,100); i++){
            list.add(String.valueOf((int)(1+Math.floor(Math.random()*6))));
        }
        Collections.sort(list);
        text += String.join(",",list);
        gameResult.setText(text+"]");
        return gameResult;
    }

    @GameMethod
    public GameResult spawn() {
        GameResult gameResult = new GameResult();
        if(spawn.isEmpty()){
            spawn.initialize();
        }
        String cardId = spawn.drawCard();
        spawn.discardCard(cardId);
        users.forEach((s,user)->dangerLevel = dangerLevel.highest(user.getDangerLevel()));
        gameResult.setText("{dangerLevel:"+dangerLevel+"}");
        gameResult.addImageId("/"+NAME+"/"+SPAWN+"/"+cardId);
        return gameResult;
    }


}
