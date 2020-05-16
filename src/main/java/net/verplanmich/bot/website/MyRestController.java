package net.verplanmich.bot.website;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.verplanmich.bot.game.GameData;
import net.verplanmich.bot.game.GameEngine;
import net.verplanmich.bot.game.GameResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class MyRestController {

    public static final String EVENT_INFO = "info";

    @Autowired
    private GameEngine gameEngine;

    @Autowired
    private SimpMessagingTemplate template;

    private final ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value={"/games/{gameId}/{command}/{optional}"})
    public GameResult game(@PathVariable String gameId, @PathVariable String command, @PathVariable Optional<String> optional, @AuthenticationPrincipal OAuth2User principal) throws Exception {
        GameData gameData =  getGameData(gameId,principal);
        String[] optionals = optional.orElse("").split("/");
        GameResult gameResult = gameEngine.callGameMethod(command,gameData,optionals);
        sendToChat(gameData, gameResult);
        return gameResult;
    }

    public void sendToChat(GameData gameData, GameResult gameResult) throws JsonProcessingException {
        template.convertAndSend("/event/" + gameData.getGameId(), mapper.writeValueAsString(gameResult));
    }

    @RequestMapping("/games/{gameId}/sendToChat/{optional}")
    public GameResult gameChat(@PathVariable String gameId, @AuthenticationPrincipal OAuth2User principal,@PathVariable Optional<String> optional) throws JsonProcessingException {
        GameData gameData =  getGameData(gameId,principal);
        GameResult gameResult = new GameResult().setText(optional.get()).addEvent(EVENT_INFO);
        sendToChat(gameData, gameResult);
        return gameResult;
    }

    private GameData getGameData(String gameId, OAuth2User principal){
        GameData gameData = new GameData();
        gameData.setGameId(gameId);
        gameData.setUserId((String)principal.getAttributes().get("id"));
        gameData.setUserName((String)principal.getAttributes().get("username"));
        return gameData;
    }

    @GetMapping("/games/{gameId}")
    public List<String> game(@PathVariable String gameId, @AuthenticationPrincipal OAuth2User principal) {
        GameData gameData =  getGameData(gameId,principal);
        return gameEngine.getAvailableMethodNames(gameData);
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
