package net.verplanmich.bot.game;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.verplanmich.bot.MyBotListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class GameEngine {

    private static Map<String, Game> games = new HashMap();
    private static final Logger LOG = LoggerFactory.getLogger(GameEngine.class);

    private static Map<String, List<Method>> gameMethods = new HashMap();

    public void newGame(String gameName,String gameId) throws Exception {
        try{
            gameName = gameName.substring(0, 1).toUpperCase() + gameName.substring(1);
            Class gameClass = Class.forName("net.verplanmich.bot.game." +gameName.toLowerCase()+"."+gameName);
            if (Game.class.isAssignableFrom(gameClass)) {
                Game game = (Game) gameClass.newInstance();
                games.put(gameId, game);
            } else {
                throw new Exception("smart you think you are? Outsmart me you will not!");
            }
        } catch (ClassNotFoundException e) {
            if (gameName.equals("Dave")) {
                throw new Exception("I am sorry " + gameName + " i can't do this");
            }
            throw new Exception("Game " + gameName + " not found");
        } catch (Exception e) {
            LOG.error(gameName + " seems broken plz contact developer", e);
            throw new Exception(gameName + " seems broken plz contact developer");
        }
    }


    public List<String> getAvailableMethodNames(GameData gameData) {
        return getAvailableMethods(gameData).stream().map(m -> m.getName()).collect(Collectors.toList());
    }

    private List<Method> getAvailableMethods(GameData gameData) {
        if (gameMethods.get(getGameName(gameData)) == null) {
            List<Method> availableMethods = new ArrayList();
            Method[] allMethods = getGame(gameData).getClass().getDeclaredMethods();
            for (Method method : allMethods) {
                if (Modifier.isPublic(method.getModifiers()) && method.isAnnotationPresent(GameMethod.class)) {
                    availableMethods.add(method);
                }
            }
            gameMethods.put(getGameName(gameData), availableMethods);
        }
        return gameMethods.get(getGameName(gameData));
    }

    public boolean hasGameMethod(String command, GameData gameData){
        return getGameMethod(command,gameData).isPresent();
    }

    public Optional<Method> getGameMethod(String command, GameData gameData){
        return getAvailableMethods(gameData).stream().filter(m -> m.getName().equals(command)).findFirst();
    }

    public GameResult callGameMethod(String command, GameData gameData, String... optionals) throws Exception{
        Method method = getGameMethod(command,gameData).get();
        Object[] parameters = getParamters(method,gameData,optionals);
        return (GameResult) method.invoke(getGame(gameData),parameters);
    }

    private Object[] getParamters(Method method, GameData gameData, String... optionals){
        return Arrays.asList(method.getParameters()).stream().map(
                parameter->{
                    if(parameter.getName().equals("userId")) {
                        return gameData.getUserId();
                    }
                    try {
                        return optionals[0];
                    }catch (Exception e){
                        LOG.error("", e);
                    }
                    LOG.error("Declare "+parameter.getName()+" Parameter for GameMethod. " +
                            "Do not use discord specific Objects Games are independent of discord");
                    return Void.class;
                }
        ).toArray();
    }

    public Map<String, Game> getGames(){
        return games;
    }

    public Game getGame(GameData gameData){
        return games.get(gameData.getGameId());
    }

    public String getGameName(GameData gameData){
        return getGame(gameData).getClass().getName();
    }

}
