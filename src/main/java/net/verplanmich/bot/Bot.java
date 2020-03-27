package net.verplanmich.bot;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;
import java.io.InputStream;


@Service
public class Bot implements DiscordBot {

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

    public void sendEmbeddedImageMessage(MessageReceivedEvent event, String message, String imagePath) {
        EmbedBuilder result = new EmbedBuilder();
        result.setTitle(message);
        String extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);
        result.setImage("attachment://image." + extension); // Use same file name from attachment
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            event.getChannel()
                    .sendMessage(result.build())
                    .addFile(inputStream, "image." + extension)
                    .queue(m -> {
                        try{
                            inputStream.close();
                        }catch(Exception e){
                            LOG.error("",e);
                        }
                    }, error -> {
                        try {
                            inputStream.close();
                        } catch (Exception e) {
                            LOG.error("inputStream Closed error", error);
                        }
                    });


    }
}
