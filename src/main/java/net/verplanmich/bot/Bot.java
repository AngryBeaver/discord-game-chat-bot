package net.verplanmich.bot;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.verplanmich.bot.game.GameData;
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


}
