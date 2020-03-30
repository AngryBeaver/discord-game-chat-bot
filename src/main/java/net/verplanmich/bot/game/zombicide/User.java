package net.verplanmich.bot.game.zombicide;

import net.verplanmich.bot.game.GameMethod;
import net.verplanmich.bot.game.GameResult;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static net.verplanmich.bot.game.zombicide.Zombicide.*;

public class User {
    private Integer xp = 0;
    private List<String> hand = new ArrayList();
    private String charId;
    private DangerLevel dangerLevel = DangerLevel.getForXp(xp);
    private boolean isDead =false;

    public User(String charId){
        this.charId = charId;
    }

    public DangerLevel getDangerLevel(){
        return dangerLevel;
    }

    public GameResult gear(){
        GameResult gameResult = new GameResult();
        hand.forEach(cardId->
                gameResult.addImageId("/"+NAME+"/"+GEAR+"/"+cardId)
        );
        return gameResult;
    }

    public GameResult info(){
        GameResult gameResult = getTextInfo();
        gameResult.addImageId("/"+NAME+"/"+CHARS+"/"+charId);
        return gameResult;
    }

    private GameResult getTextInfo(){
        GameResult gameResult = new GameResult();
        gameResult.setText("{XP:"+xp+ ",DangerLevel:"+dangerLevel+"}");
        return gameResult;
    }

    public GameResult addXp(String xp){
        this.xp += Integer.valueOf(xp);
        this.dangerLevel = DangerLevel.getForXp(this.xp);
        return getTextInfo();
    }

    public GameResult add(String cardId) {
        hand.add(cardId);
        GameResult gameResult = new GameResult();
        gameResult.setText(cardId);
        gameResult.addImageId("/"+NAME+"/"+GEAR+"/"+cardId);
        return gameResult;
    }

    public GameResult drop(String cardId) {
        String text = "Card not in hand";
        if(hand.remove(cardId)){
            text= "Card dropped";
        }
        GameResult gameResult = new GameResult();
        gameResult.setText(text);
        return gameResult;
    }

    public GameResult die(){
        if(!isDead) {
            isDead = true;
            String part = charId.substring(0, charId.length()-4);
            charId = part + "-z" + charId.substring(charId.length()-4);
            hand = new ArrayList();
            return info();
        }
        GameResult gameResult = new GameResult();
        gameResult.setText("You are dead !!");
        return gameResult;
    }



}
