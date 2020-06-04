package net.verplanmich.bot.game.waterdeep;

import net.verplanmich.bot.game.GameResult;
import net.verplanmich.bot.game.GameResultException;

import static net.verplanmich.bot.game.waterdeep.Waterdeep.*;
import static net.verplanmich.bot.website.MyRestController.EVENT_INFO;

public class Quest {
    private String name;
    private Type type;
    private boolean isPlot;
    private Reward reward;
    private UserEntity costs;

    enum Type{
        ARCANE,
        COMMERCE,
        PIETY,
        SKULLDUGGERY,
        WAREFARE,
        MANDATORY,
    };

    public Quest(String name, Type type, boolean isPlot,UserEntity costs, Reward reward){
        this.name = name;
        this.reward = reward;
        this.type = type;
        this.isPlot = isPlot;
        this.costs = costs;
    }

    public GameResult complete(User user, GameDecks gameDecks){
        checkCosts(user.getUserEntity());
        payCost(user);
        //todo set quest to completed
        return reward.consume(user,gameDecks)
                .addEvent(EVENT_INFO)
                .addEvent(EVENT_USER)
                .set(MAP_KEY_USER, user.getUserEntity())
                .addImageId(WATERDEEP+"/quests+/"+name+".jpg");
    }

    private void checkCosts(UserEntity userEntity){
        if(costs.getCleric() > userEntity.getCleric()){
            throw new GameResultException(new GameResult()
                    .setText(userEntity.getName()+" can't complete<br/>"+ name+ "<br/>you need "+costs.getCleric()+" clerics"));
        }
        if(costs.getRogue() > userEntity.getRogue()){
            throw new GameResultException(new GameResult()
                    .setText(userEntity.getName()+" can't complete<br/>"+ name+ "<br/>you need "+costs.getRogue()+" rogues"));
        }
        if(costs.getWizard() > userEntity.getWizard()){
            throw new GameResultException(new GameResult()
                    .setText(userEntity.getName()+" can't complete<br/>"+ name+  "<br/>you need "+costs.getWizard()+" wizards"));
        }
        if(costs.getFighter() > userEntity.getFighter()){
            throw new GameResultException(new GameResult()
                    .setText(userEntity.getName()+" can't complete<br/>"+ name+ "<br/>you need "+costs.getFighter()+" fighters"));
        }
        if(costs.getRogue() > userEntity.getGold()){
            throw new GameResultException(new GameResult()
                    .setText(userEntity.getName()+" can't complete<br/>"+ name+ "<br/>you need "+costs.getGold()+" gold"));
        }
    }
    private void payCost(User user){
        user.adjustCleric("-"+costs.getCleric());
        user.adjustRogue("-"+costs.getRogue());
        user.adjustFighter("-"+costs.getFighter());
        user.adjustWizard("-"+costs.getWizard());
        user.adjustGold("-"+costs.getGold());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
