package net.verplanmich.bot;

public interface DiscordBot {
    void sendMessage(String guild, String channel, String message);
}
