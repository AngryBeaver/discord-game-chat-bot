package net.verplanmich.bot.game;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class GameData {

    private String userId;
    private String userName;
    private Integer gameId;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
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
