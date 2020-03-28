package net.verplanmich.bot.website;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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


    public void sendImagesToChat(String channelId, String text, List<String> imagePaths){
        text = text + "<br/>";
        for (String imagePath:imagePaths) {
            text += "<img width='150' src='"+imagePath+"' />";
        }
        sendToChat(channelId,text);
    }

    public void sendToChat(String channelId, String text) {
        Message message = new Message(text);
        try {
            template.convertAndSend("/topic/" + channelId, mapper.writeValueAsString( message));
        } catch (JsonProcessingException e) {
            LOG.error("",e);
        }
    }
}