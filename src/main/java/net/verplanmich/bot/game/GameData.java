package net.verplanmich.bot.game;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class GameData {

    private String userId;
    private String userName;
    private String gameId;

    public GameData(MessageReceivedEvent event){
        this.userId = event.getAuthor().getId();
        this.userName = event.getAuthor().getName();
        this.gameId = event.getChannel().getId();
    };


    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
