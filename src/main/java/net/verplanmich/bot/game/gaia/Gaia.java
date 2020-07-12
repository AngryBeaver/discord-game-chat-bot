package net.verplanmich.bot.game.gaia;

import net.verplanmich.bot.game.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

import static net.verplanmich.bot.website.MyRestController.EVENT_INFO;

@Component
@Scope("prototype")
public class Gaia implements Game {

    public static final String GAIA = "gaia";


    @Override
    public String getName() {
        return GAIA;
    }

}
