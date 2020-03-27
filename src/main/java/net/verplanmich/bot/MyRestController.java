package net.verplanmich.bot;

import net.verplanmich.bot.DiscordBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {

    @Autowired
    DiscordBot bot;

    public void msgDiscord() {

        //bot.sendMessage(message.getGuild(), message.getChannel(), message.getMessage());
    }
}
