package net.verplanmich.bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.verplanmich.bot.games.Game;
import net.verplanmich.bot.games.GameMethod;
import net.verplanmich.bot.games.GameMethodType;
import net.verplanmich.bot.website.Chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class MyBotListener extends ListenerAdapter {


    private static final Logger LOG = LoggerFactory.getLogger(MyBotListener.class);

    private static Map<String, Game> games = new HashMap();
    private static Map<String, List<Method>> gameMethods = new HashMap();

    @Autowired
    Chat chat;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() && !event.getAuthor().getName().equals("jabbawookie")){
            chat.sendToChat(event.getChannel().getId(),event.getMessage().getContentDisplay());
            return;
        }
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (content.startsWith("\\g")) {
            String[] parts = content.substring(2).trim().split(" ");
            String command = "";
            if (parts.length >= 1) {
                command = parts[0];
            }
            handleCommand(command, event, parts);
        }
    }

    private void handleCommand(String command, MessageReceivedEvent event, String[] parts) {
        Game game = games.get(event.getChannel().getId());
        if (command.equals("new") && parts.length >= 2 && !parts[1].trim().isEmpty()) {
            newGame(parts[1], event);
            return;
        }
        if (games.get(event.getChannel().getId()) == null) {
            describeGameBot(event);
            return;
        }
        if (getAvailableMethodNames(game).contains(command)) {
            callGameMethod(command, game, event, parts);
        } else {
            describeGameBot(game, event);
        }

    }

    private void newGame(String gameName, MessageReceivedEvent event) {
        try {
            gameName = gameName.substring(0, 1).toUpperCase() + gameName.substring(1);
            Class gameClass = Class.forName("net.verplanmich.bot.games." + gameName);
            if (Game.class.isAssignableFrom(gameClass)) {
                Game game = (Game) gameClass.newInstance();
                games.put(event.getChannel().getId(), game);
                event.getChannel().sendMessage("new Game " + gameName).queue();
            } else {
                event.getChannel().sendMessage("smart you think you are? Outsmart me you will not!").queue();
            }
        } catch (ClassNotFoundException e) {
            if (gameName.equals("Dave")) {
                event.getChannel().sendMessage("you got 50Xp").queue();
            }
            event.getChannel().sendMessage("I am sorry " + gameName + " i can't do this").queue();
        } catch (Exception e) {
            LOG.error("", e);
            event.getChannel().sendMessage(gameName + " seems broken plz contact developer").queue();
        }
    }

    private Object[] getParamters(Method method,MessageReceivedEvent event, String[] parts){
        return Arrays.asList(method.getParameters()).stream().map(
                parameter->{
                    if(parameter.getName().equals("userId")) {
                        return event.getAuthor().getId();
                    }
                    try {
                        if (parameter.getName().equals("arg1")) {
                            return parts[1];
                        }
                    }catch (Exception e){
                        LOG.error("", e);
                    }
                    LOG.error("Declare "+parameter.getName()+" Parameter for GameMethod. " +
                                "Do not use discord specific Objects Games are independent of discord");
                    return Void.class;
                }
        ).toArray();
    }

    private void callGameMethod(String command, Game game, MessageReceivedEvent event,String[] parts) {
        try {
            Method method = getAvailableMethods(game).stream().filter(m -> m.getName().equals(command)).findFirst().get();
            GameMethodType type = method.getAnnotation(GameMethod.class).type();
            boolean isPrivate = method.getAnnotation(GameMethod.class).isPrivate();
            Object[] parameters = getParamters(method,event,parts);
            if (type.equals(GameMethodType.Image)) {
                String imagePath = (String) method.invoke(game,parameters);
                sendEmbeddedImageMessage(event, "@" + event.getAuthor().getName(), Arrays.asList(imagePath),isPrivate);
                return;
            }
            if (type.equals(GameMethodType.Text)) {
                String text = (String) method.invoke(game,parameters);
                sendTextMessage(event,"@" + event.getAuthor().getName() + " " + text,isPrivate);
                return;
            }
            if (type.equals(GameMethodType.ImageList)) {
                List<String> imagePaths = (List<String>) method.invoke(game,parameters);
                sendEmbeddedImageMessage(event, "@" + event.getAuthor().getName(), imagePaths,isPrivate);
                return;
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOG.error("", e);
        }
        event.getChannel().sendMessage(command + " seems broken plz contact developer").queue();
    }

    private List<String> getAvailableMethodNames(Game game) {
        return getAvailableMethods(game).stream().map(m -> m.getName()).collect(Collectors.toList());
    }

    private List<Method> getAvailableMethods(Game game) {
        if (gameMethods.get(game.getClass().getName()) == null) {
            List<Method> availableMethods = new ArrayList();
            Method[] allMethods = game.getClass().getDeclaredMethods();
            for (Method method : allMethods) {
                if (Modifier.isPublic(method.getModifiers()) && method.isAnnotationPresent(GameMethod.class)) {
                    availableMethods.add(method);
                }
            }
            gameMethods.put(game.getClass().getName(), availableMethods);
        }
        return gameMethods.get(game.getClass().getName());
    }

    private void describeGameBot(MessageReceivedEvent event) {
        EmbedBuilder result = new EmbedBuilder();
        result.setTitle("No Game Selected");
        result.setDescription("Howdy, I am Gamebot");
        setFooter(result);
        event.getChannel().sendMessage(result.build()).queue();
    }

    private void describeGameBot(Game game, MessageReceivedEvent event) {
        String gameName = game.getClass().getSimpleName();
        EmbedBuilder result = new EmbedBuilder();
        result.setTitle(gameName);
        result.setDescription("type \\g [" + String.join(", ", getAvailableMethodNames(game)) + "]");
        setFooter(result);
        event.getChannel().sendMessage(result.build()).queue();
    }

    private void setFooter(EmbedBuilder result) {
        result.setFooter("type \\g new $gamename to start a new game");
    }

    private void sendTextMessage(MessageReceivedEvent event,String message,boolean isPrivate){
        event.getChannel().sendMessage(message).queue();
        chat.sendToChat(event.getChannel().getId(),message);
    }

    private void sendEmbeddedImageMessage(MessageReceivedEvent event, String message, List<String> imagePaths, boolean isPrivate) {
        if(imagePaths.size() == 1){
            message = message+ " "+imagePaths.get(0);
        }
        MessageAction messageAction = event.getChannel()
                    .sendMessage(message);
        Map<String, InputStream> inputStreams = new HashMap();

        imagePaths.forEach(imagePath->{
            inputStreams.put(imagePath,getClass().getClassLoader().getResourceAsStream("static"+imagePath));
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
        chat.sendImagesToChat(event.getChannel().getId(),message,imagePaths);
    }

}
