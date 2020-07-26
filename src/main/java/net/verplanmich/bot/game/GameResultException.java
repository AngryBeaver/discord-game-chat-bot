package net.verplanmich.bot.game;

public class GameResultException extends RuntimeException {

    private GameResult gameResult;
    public GameResultException(String msg){
        super();
        this.gameResult = new GameResult().setText(msg);
    }

    public GameResultException(GameResult gameResult){
        super();
        this.gameResult = gameResult;
    }

    public GameResult getGameResult(){
        return gameResult;
    }
}
