package net.verplanmich.bot;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class Bot {

    private static final Logger LOG = LoggerFactory.getLogger(MyBotListener.class);
    @Value("${discord.secret_token}")
    private String secretToken;
    private JDA jda;

    @Autowired
    MyBotListener myBotListener;

    @PostConstruct
    public void init() {
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(secretToken)
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(myBotListener)
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String guild, String channel, String message) {
        jda
                .getGuildById(guild)
                .getTextChannelById(channel)
                .sendMessage(message)
                .queue();

    }

    public void sendEmbeddedImageMessage(MessageReceivedEvent event, String message, List<String> imagePaths) {
        EmbedBuilder result = new EmbedBuilder();
        result.setTitle(message);
        imagePaths.forEach(imagePath-> {
                    result.setImage("attachment://" + imagePath);
        });
        MessageAction messageAction = event.getChannel()
                .sendMessage(result.build());
        Map<String, InputStream> inputStreams = new HashMap();
        imagePaths.forEach(imagePath->{
            inputStreams.put(imagePath,getClass().getClassLoader().getResourceAsStream(imagePath));
            messageAction.addFile(inputStreams.get(imagePath), imagePath);
        });
        messageAction.queue(m ->
            inputStreams.forEach(
                    (key,inputStream) -> {
                        try {
                            inputStream.close();
                        } catch (Exception e) {
                            LOG.error(key, e);
                        }
                    }
            ), error -> inputStreams.forEach(
                (key,inputStream) -> {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        LOG.error(key, e);
                    }
                }
            )
        );
    }
}
