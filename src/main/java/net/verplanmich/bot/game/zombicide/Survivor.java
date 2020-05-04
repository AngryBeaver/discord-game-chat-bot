package net.verplanmich.bot.game.zombicide;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Survivor {

    private final String name;

    private Map<String, List<Action>> actions = new HashMap();
    private Map<String, List<Action>> z_actions = new HashMap();

    Survivor(String name, Action blue, Action yellow, Action orange1, Action orange2, Action red1, Action red2, Action red3, Action zblue, Action zyellow, Action zorange1, Action zorange2, Action zred1, Action zred2, Action zred3){
        this.name = name;
        actions.put("blue", Arrays.asList(blue));
        actions.put("yellow", Arrays.asList(yellow));
        actions.put("orange", Arrays.asList(orange1,orange2));
        actions.put("red", Arrays.asList(red1,red2,red3));
        z_actions.put("blue", Arrays.asList(zblue));
        z_actions.put("yellow", Arrays.asList(zyellow));
        z_actions.put("orange", Arrays.asList(zorange1,zorange2));
        z_actions.put("red", Arrays.asList(zred1,zred2,zred3));
    }

    public String getName() {
        return name;
    }

    public Map<String, List<Action>> getActions() {
        return actions;
    }

    public void setActions(Map<String, List<Action>> actions) {
        this.actions = actions;
    }

    public Map<String, List<Action>> getZ_actions() {
        return z_actions;
    }

    public void setZ_actions(Map<String, List<Action>> z_actions) {
        this.z_actions = z_actions;
    }
}
