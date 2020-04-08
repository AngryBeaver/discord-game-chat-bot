package net.verplanmich.bot.website;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.verplanmich.bot.game.GameData;
import net.verplanmich.bot.game.GameResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Chat {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(Chat.class);

    @Autowired
    private SimpMessagingTemplate template;


    private Message getGameResultAsHtml(GameResult gameResult) {
        String msg = gameResult.getText();
        if(!gameResult.getImageIds().isEmpty()){
            msg = msg + "<br/>";
        }
        for (String imagePath:gameResult.getImageIds()) {
            msg += "<img width='150' src='"+imagePath+"' />";
        }
        return new Message(msg);
    }

    public void sendToChat(GameData gameData, GameResult gameResult) {
        Message message = getGameResultAsHtml(gameResult);
        try {
            template.convertAndSend("/chat/" + gameData.getGameId(), mapper.writeValueAsString( message));
        } catch (JsonProcessingException e) {
            LOG.error("",e);
        }
    }
}