package net.verplanmich.bot.game.clank;

import net.verplanmich.bot.game.Deck;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class User {

    private List<String> startCards = Arrays.asList("burgle","burgle","burgle","burgle","burgle","burgle","stumble","stumble","sidestep","scramble");


    UserEntity userEntity;
    Deck deck;
    List<String> hand = new ArrayList();
    List<String> playArea =  new ArrayList();

    public User(String userName, String userId,String color){
        userEntity = new UserEntity(userName,userId,color);
        deck = new Deck(startCards).shuffle();
        hand = new ArrayList();
        playArea = new ArrayList();
    }

    public void initialize(){
        deck = new Deck(startCards).shuffle();
        hand = new ArrayList();
        playArea = new ArrayList();
        endTurn();
    }
    public String getId(){
        return userEntity.getUserId();
    }
    public String getName(){
        return userEntity.getUserName();
    }
    public String getChar(){
        return userEntity.getUserChar();
    }
    public List<String> getPlayArea(){
        return playArea;
    }

    public List<String> getHand(){
        return hand;
    }

    public int getCoins(){
        return userEntity.getCoins();
    }

    public UserEntity get(){
        return userEntity;
    }

    public void addItem(String itemId){
        userEntity.getItems().add(itemId);
    }

    public boolean removeItem(String itemId){
        return userEntity.getItems().remove(itemId);
    }

    public void adjustCoins(int amount){
        userEntity.setCoins( userEntity.getCoins()+amount);
    }


    public void heal(int amount){
        int damage =  userEntity.getDamage();
        int min = Math.min(damage,amount);
        addClankCubes(min);
        userEntity.setDamage(damage-min);
    }

    public void damage(int amount){
        int damage = userEntity.getDamage();
        userEntity.setDamage(damage+amount);
    }

    public void addClankCubes(int amount){
        userEntity.setClankCubes(userEntity.getClankCubes()+amount);
    }

    public void removeClankCubes(int amount){
        int clankCubes = userEntity.getClankCubes();
        int min = Math.min(clankCubes,amount);
        userEntity.setClankCubes(clankCubes-min);
    }

    public void startTurn(){
        playArea = new ArrayList(hand);
    }

    public String drawToPlay(){
        String cardId = drawCard();
        playArea.add(cardId);
        hand.add(cardId);
        return cardId;
    }

    public void toPlay(String cardId){
        playArea.add(cardId);
        hand.add(cardId);
    }

    public boolean discardToPlay(String cardId){
        boolean result = discardToVoid(cardId);
        if(result){
            toPlay(cardId);
        }
        return result;
    }

    public boolean discardToVoid(String cardId){
        return deck.fromDiscardPile(cardId);
    }

    public boolean playToDiscard(String cardId){
        boolean result =playToVoid(cardId);
        if(result){
            deck.discardCard(cardId);
        }
        return result;
    }

    public boolean playToVoid(String cardId){
        hand.remove(cardId);
        return playArea.remove(cardId);
    }

    public String drawCard(){
        try {
            return deck.drawCard();
        }catch(Exception e){
            deck.shuffleDiscard();
            return deck.drawCard();
        }
    }

    public void endTurn(){
        hand = new ArrayList();
        try {
            hand.add(drawCard());
            hand.add(drawCard());
            hand.add(drawCard());
            hand.add(drawCard());
            hand.add(drawCard());
        }catch(Exception e){

        }
        playArea.stream().forEach(cardId->deck.discardCard(cardId));
        playArea = new ArrayList();
    }


}
