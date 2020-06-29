package net.verplanmich.bot.game.spiritisland;

import net.verplanmich.bot.game.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static net.verplanmich.bot.website.MyRestController.EVENT_INFO;

@Component
@Scope("prototype")
public class Spiritisland implements Game {

    public static final String SPIRITISLAND = "spiritisland";


    public static final String EVENT_USER = "user";
    public static final String EVENT_SKILL = "skill";


    public static final String MAP_KEY_SKILL = "skill";
    public static final String MAP_KEY_USERS = "users";
    public static final String MAP_KEY_USER = "user";


    private GameDecks gameDecks;
    private Map<String, UserEntity> users = new HashMap();
    private List<UserEntity> userList = new ArrayList();
    private List<String> colors = new ArrayList(Arrays.asList("red","green","blue","yellow"));
    private Map<String,Map<String,UserEntity>> stats = new HashMap<>();

    @Override
    public String getName() {
        return SPIRITISLAND;
    }

    public Spiritisland(){
        this.gameDecks = new GameDecks();
        Collections.shuffle(colors);
    }

    @GameMethod
    public GameResult adjustEnergy(String userId, String energy){
        int energyInt = Integer.parseInt(energy);
        UserEntity user = users.get(userId);
        user.setEnergy(user.getEnergy()+energyInt);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .setText(user.getName()+" adjustEnergy "+energy)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user);
    }

    @GameMethod
    public GameResult adjustPresence1(String userId, String presence){
        int presenceInt = Integer.parseInt(presence);
        UserEntity user = users.get(userId);
        List<String> presence1 = user.getPresence1();
        if(presenceInt > 0){
            presence1.add("");
        }else if(!presence.isEmpty()){
            presence1.remove(0);
        }
        return new GameResult()
                .addEvent(EVENT_INFO)
                .setText(user.getName()+" adjustPresence1 "+presenceInt)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user);
    }

    @GameMethod
    public GameResult adjustPresence2(String userId, String presence){
        int presenceInt = Integer.parseInt(presence);
        UserEntity user = users.get(userId);
        List<String> presence2 = user.getPresence2();
        if(presenceInt > 0){
            presence2.add("");
        }else if(!presence.isEmpty()){
            presence2.remove(0);
        }
        return new GameResult()
                .addEvent(EVENT_INFO)
                .setText(user.getName()+" adjustPresence2 "+presenceInt)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user);
    }


    @GameMethod
    public GameResult addSkill(String userId,String cardId){
        UserEntity user = users.get(userId);
        if(!gameDecks.removeCard(cardId)){
            throw new GameResultException(new GameResult().addEvent(EVENT_INFO).setText("no such card"+cardId));
        }
        user.getHand().add(cardId);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .setText(user.getName()+" addSkill ")
                .addImageId("./assets/deck/"+cardId+".jpg")
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user);
    }

    @GameMethod
    public GameResult advanceSkill(String userId,String type){
        UserEntity user = users.get(userId);
        List<String> skills = new ArrayList();
        skills.add(gameDecks.drawDeck(type));
        skills.add(gameDecks.drawDeck(type));
        skills.add(gameDecks.drawDeck(type));
        skills.add(gameDecks.drawDeck(type));
        return new GameResult()
                .addEvent(EVENT_SKILL)
                .addEvent(EVENT_INFO)
                .setText(user.getName()+" advanceSkill ")
                .set(MAP_KEY_SKILL,skills)
                .set(MAP_KEY_USER,user);
    }

    @GameMethod
    public GameResult use(String userId,String cardId){
        UserEntity user = users.get(userId);
        if(!user.getHand().remove(cardId)){
            throw new GameResultException(new GameResult().addEvent(EVENT_INFO).setText("no such card"+cardId));
        }
        user.getPlay().add(cardId);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .setText(user.getName()+" use "+cardId)
                .addImageId("./assets/deck/"+cardId+".jpg")
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user);
    }

    @GameMethod
    public GameResult trash(String userId,String cardId){
        UserEntity user = users.get(userId);
        if(!user.getHand().remove(cardId)
            && !user.getPlay().remove(cardId)
            && !user.getDiscard().remove(cardId)){
            throw new GameResultException(new GameResult().addEvent(EVENT_INFO).setText("no such card"+cardId));
        }
        gameDecks.returnCard(cardId);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .setText(user.getName()+" trash "+cardId)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user);
    }

    @GameMethod
    public GameResult discard(String userId){
        UserEntity user = users.get(userId);
        user.getPlay().forEach(
                cardId->user.getDiscard().add(cardId)
        );
        user.getPlay().clear();
        return new GameResult()
                .addEvent(EVENT_INFO)
                .setText(user.getName()+" discards play")
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user);
    }

    @GameMethod
    public GameResult regain(String userId,String cardId){
        UserEntity user = users.get(userId);
        if(!user.getDiscard().remove(cardId)) {
            throw new GameResultException(new GameResult().addEvent(EVENT_INFO).setText("no such card" + cardId));
        }
        user.getHand().add(cardId);
        return new GameResult()
                .addEvent(EVENT_INFO)
                .setText(user.getName()+" regain "+cardId)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user);
    }

    @GameMethod
    public GameResult refresh(String userId){
        UserEntity user = users.get(userId);
        user.getDiscard().forEach(cardId ->{
            user.getHand().add(cardId);
        });
        user.getDiscard().clear();
        return new GameResult()
                .addEvent(EVENT_INFO)
                .setText(user.getName()+" refresh ")
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER,user);
    }


    @GameMethod
    public GameResult getUsers(GameData gameData){
        return join(gameData)
                .set(MAP_KEY_USERS,userList);
    }

    @GameMethod
    public GameResult join(GameData gameData) {
        GameResult gameResult = new GameResult();
        if(!users.containsKey(gameData.getUserId()) && colors.size()>0){
            UserEntity user = gameDecks.getAvatar();
            user.setName(gameData.getUserName());
            user.setId(gameData.getUserId());
            user.setColor(colors.remove(0));
            user.setHand(gameDecks.getHand(user.getAvatar()));
            userList.add(user);
            users.put(gameData.getUserId(),user);
            gameResult.addEvent(EVENT_INFO)
                    .addEvent(EVENT_USER)
                    .setText(gameData.getUserName()+" joined")
                    .set(MAP_KEY_USER,user);
        }
        return gameResult;
    }
}
