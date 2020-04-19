package net.verplanmich.bot.game.alienencounter;

import net.verplanmich.bot.game.GameMethod;
import net.verplanmich.bot.game.GameResult;
import net.verplanmich.bot.game.GameResultException;

import java.io.IOException;

import static net.verplanmich.bot.game.alienencounter.Alienencounter.*;
import static net.verplanmich.bot.website.MyRestController.EVENT_INFO;

public final class GameResultBuilder {

    GameResult gameResult = new GameResult();

    Alienencounter ae;

    String from;

    private GameResultBuilder(Alienencounter alienEncounter, String from){
        this.ae = alienEncounter;
        this.from = from;
    }

    public static GameResultBuilder fromCrewCardId(Alienencounter alienencounter, String cardId){
        return new GameResultBuilder(alienencounter,"cardId")
                .fromCrewCardId(cardId);
    }

    public static GameResultBuilder fromBarracks(Alienencounter alienencounter, String cardId){
        return new GameResultBuilder(alienencounter,"barracks")
                .fromBarracks(cardId);
    }

    public static GameResultBuilder fromHq(Alienencounter alienencounter, String position){
        return new GameResultBuilder(alienencounter,"hq")
                .fromHq(position);
    }

    public static GameResultBuilder fromHand(Alienencounter alienencounter,String userId, String cardId){
        return new GameResultBuilder(alienencounter,"hand")
                .fromHand(userId,cardId);
    }

    public static GameResultBuilder fromDiscard(Alienencounter alienencounter,String userId, String cardId){
        return new GameResultBuilder(alienencounter,"discard")
                .fromDiscard(userId,cardId);
    }

    public static GameResultBuilder fromDraw(Alienencounter alienencounter,String userId, String cardId){
        return new GameResultBuilder(alienencounter,"draw")
                .fromDraw(userId,cardId);
    }

    public static GameResultBuilder fromHive(Alienencounter alienencounter){
        return new GameResultBuilder(alienencounter,"hive")
                .fromHive();
    }

    public static GameResultBuilder fromHiveCardId(Alienencounter alienencounter, String cardId, boolean hidden){
        return new GameResultBuilder(alienencounter,"cardId")
                .fromHiveCardId(cardId,hidden);
    }

    public static GameResultBuilder fromComplex(Alienencounter alienencounter,String position){
        return new GameResultBuilder(alienencounter,"complex")
                .fromComplex(position);
    }
    public static GameResultBuilder fromAttachment(Alienencounter alienencounter,String position){
        return new GameResultBuilder(alienencounter,"attachment")
                .fromAttachment(position);
    }

    public static GameResultBuilder fromCombat(Alienencounter alienencounter,String cardId){
        return new GameResultBuilder(alienencounter,"combat")
                .fromCombat(cardId);
    }

    public static GameResultBuilder fromOperations(Alienencounter alienencounter,String cardId){
        return new GameResultBuilder(alienencounter,"operations")
                .fromOperations(cardId);
    }


    public static GameResult showHand(Alienencounter alienencounter,String userId, String cardId){
        return new GameResultBuilder(alienencounter,"show")
                .showHand(userId,cardId);
    }

    public GameResult showHand(String userId,String cardId){
        if(!ae.getUser(userId).getHand().contains(cardId)){
            throw new GameResultException(new GameResult()
                    .setText("Card not found"));
        }
        fromCrewCardId(cardId);
        return addUserToGameResult(userId);
    }

    private GameResultBuilder showComplex(String position) {
        Location location = getLocationAt(position);
        String cardId = location.getGround();
        if (cardId == null || cardId.equals("")) {
            throw new GameResultException(new GameResult()
                    .setText("Card not found")
            );
        }
        return fromHiveCardId(cardId,location.isHidden());
    }


    //TO
    public GameResult toHand(String userId){
        ae.getUser(userId).toHand(getCardId());
        return  addUserToGameResult(userId)
                .addEvent(EVENT_REFRESH_USER_HAND)
                .setText(from+" to hand");
    }


    public GameResult toDiscard(String userId){
        ae.getUser(userId).toDiscard(getCardId());
        return addUserToGameResult(userId)
                .addEvent(EVENT_REFRESH_USER_DISCARD)
                .setText(from+" to discard");
    }

    public GameResult toDraw(String userId){
        ae.getUser(userId).toDraw(getCardId());
        return addUserToGameResult(userId)
                .addEvent(EVENT_REFRESH_USER_DRAW)
                .setText(from+" to draw");
    }

    public GameResult toVoid(){
        return gameResult
                .setText(from+" to void");
    }

    public GameResult toBarracksBottom(){
        ae.getBarracks().toDrawPileBottom(getCardId());
        return gameResult
                .addEvent(EVENT_REFRESH_BARRACKS)
                .setText(from+" to barracksBottom");
    }

    public GameResult toHiveTop(){
        ae.getHive().toDrawPileTop(getCardId());
        return gameResult
                .addEvent(EVENT_REFRESH_HIVE)
                .set(MAP_KEY_HIVE_SIZE,ae.getHive().getDrawPile().size())
                .setText(from+" to hive");
    }

    public GameResult toComplex(String position){
        Location location = getLocationAt(position);
        location.setGround(getCardId()); //overwrites !
        return gameResult
                .addEvent(EVENT_REFRESH_COMPLEX)
                .setText(from+" to Complex");
    }

    public GameResult toAttachment(String position){
        Location location = getLocationAt(position);
        location.setAttachment(getCardId()); //overwrites !
        return gameResult
                .addEvent(EVENT_REFRESH_ATTACHMENT)
                .setText(from+" to Attachment");
    }

    public GameResult toCombat(){
        ae.getCombat().add(getCardId());
        return gameResult
                .addEvent(EVENT_REFRESH_COMBAT)
                .setText(from+" to Combat");
    }

    public GameResult toOperations(){
        ae.getOperations().add(getCardId());
        return gameResult
                .addEvent(EVENT_REFRESH_OPERATIONS)
                .setText(from+" to Operations");
    }

    //FROM
    private GameResultBuilder fromCrewCardId(String cardId){
        gameResult.set(MAP_KEY_CARD_ID,cardId)
              .addImageId("/"+NAME+"/"+DIRECTORY_CREW+"/"+getCardId())
              .addEvent(EVENT_INFO);
        return this;
    }

    private GameResultBuilder fromBarracks( String cardId){
        if(!ae.getBarracks().fromDrawPile(cardId)){
            throw new GameResultException(new GameResult()
                    .setText("not in barracks"));
        }
        fromCrewCardId(cardId);
        gameResult.addEvent(EVENT_REFRESH_BARRACKS);
        return this;
    }

    private GameResultBuilder fromHq(String position){
        String cardId;
        if(position.equals("-1")){
            cardId = ae.getSergeant().drawCard();
        }else {
            int pos = Integer.valueOf(position);
            cardId = ae.getHq().get(pos);
            if(cardId.equals("")) {
                throw new GameResultException(new GameResult().setText("not found in hq"));
            }
            ae.fillHq(pos);
        }
        if(cardId == null) {
            throw new GameResultException(new GameResult().setText("not found in hq"));
        }
        gameResult.addEvent(EVENT_REFRESH_HQ);
        return fromCrewCardId(cardId);
    }

    private GameResultBuilder fromHand(String userId,String cardId){
        showHand(userId,cardId);
        if(!ae.getUser(userId).fromHand(cardId)){
            throw new GameResultException(new GameResult()
                    .setText("Card not found"));
        }
        gameResult.addEvent(EVENT_REFRESH_USER_HAND);
        return this;
    }

    private GameResultBuilder fromDiscard(String userId,String cardId){
        if(!ae.getUser(userId).fromDiscard(cardId)){
            throw new GameResultException(new GameResult()
                    .setText("Card not found"));
        }
        fromCrewCardId(cardId);
        addUserToGameResult(userId).addEvent(EVENT_REFRESH_USER_DISCARD);
        return this;
    }

    private GameResultBuilder fromDraw(String userId,String cardId){
        if(!ae.getUser(userId).fromDraw(cardId)){
            throw new GameResultException(new GameResult()
                    .setText("Card not found"));
        }
        fromCrewCardId(cardId);
        addUserToGameResult(userId).addEvent(EVENT_REFRESH_USER_DRAW);
        return this;
    }

    private GameResultBuilder fromHiveCardId(String cardId, boolean revealed){
        gameResult.set(MAP_KEY_CARD_ID,cardId)
            .set(MAP_KEY_IS_REVEALED, revealed);
        return this;
    }


    private GameResultBuilder fromHive(){
        String cardId = ae.getHive().drawCard();
        gameResult.addEvent(EVENT_REFRESH_HIVE)
                .set(MAP_KEY_HIVE_SIZE,ae.getHive().getDrawPile().size());
        return fromHiveCardId(cardId,false);
    }

    private GameResultBuilder fromComplex(String position){
        showComplex(position);
        getLocationAt(position).setGround(null);
        gameResult.addEvent(EVENT_REFRESH_COMPLEX);
        return this;
    }

    private GameResultBuilder fromAttachment(String position){
        Location location = getLocationAt(position);
        String cardId = location.getAttachment();
        if(cardId == null){
            throw new GameResultException(new GameResult()
                    .setText("Card not found")
            );
        }
        location.setAttachment(null);
        gameResult.addEvent(EVENT_REFRESH_ATTACHMENT);
        return fromHiveCardId(cardId,true);
    }

    private GameResultBuilder fromCombat(String cardId){
        if(!ae.getCombat().remove(cardId)){
            throw new GameResultException(new GameResult()
                    .setText("Card not found"));
        }
        gameResult.addEvent(EVENT_REFRESH_COMBAT);
        return fromHiveCardId(cardId,true);
    }

    private GameResultBuilder fromOperations(String cardId){
        if(!ae.getOperations().remove(cardId)){
            throw new GameResultException(new GameResult()
                    .setText("Card not found"));
        }
        gameResult.addEvent(EVENT_REFRESH_OPERATIONS);
        return fromCrewCardId(cardId);
    }

    public Location getLocationAt(String position){
        int pos = Integer.parseInt(position);
        return ae.getComplex().get(pos);
    }

    private GameResult addUserToGameResult(String userId){
         return gameResult.set(MAP_KEY_USER_ID,userId);
    }

    public String getCardId(){
        return (String) gameResult.get(MAP_KEY_CARD_ID);
    }



}
