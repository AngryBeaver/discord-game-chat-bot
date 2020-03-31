package net.verplanmich.bot.website;


import net.verplanmich.bot.Bot;
import net.verplanmich.bot.game.GameData;
import net.verplanmich.bot.game.GameEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class MyRestController {

    @Autowired
    GameEngine gameEngine;

    public void msgDiscord() {
        //bot.sendMessage(message.getGuild(), message.getChannel(), message.getMessage());
    }

    @GetMapping("/games/{gameId}/")
    public void game(@PathVariable String gameId) {

    }

    @GetMapping("/games/new/{gameName}")
    public void newGame(@PathVariable String gameName) throws Exception {
        gameEngine.newGame(gameName,UUID.randomUUID().toString());
    }


    @GetMapping("/games")
    private Map<String,String> getGames(){
        Map<String,String> result = new HashMap();
        gameEngine.getGames().forEach(
                (s,game)->result.put(s,game.getName())
         );
        return result;
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes();
    }

}
