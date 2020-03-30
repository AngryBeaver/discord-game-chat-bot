package net.verplanmich.bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.verplanmich.bot.game.*;
import net.verplanmich.bot.website.Chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MyBotListener extends ListenerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(MyBotListener.class);

    @Autowired
    Chat chat;

    @Autowired
    GameEngine gameEngine;

    private static Map<String,Integer> gameIdMap = new HashMap();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        GameData gameData = gameDataFrom(event);
        if (event.getAuthor().isBot() && !gameData.getUserName().equals("jabbawookie")){
            GameResult gameResult = new GameResult();
            gameResult.setText(event.getMessage().getContentDisplay());
            chat.sendToChat(gameData,gameResult);
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
            String[] options = Arrays.asList(parts).stream()
                    .skip(1)
                    .map(s->Paths.get(s).normalize().toString()).toArray(String[]::new);
            handleCommand(command, event, options);
        }
    }

    private void handleCommand(String command, MessageReceivedEvent event, String[] optionals) {
        GameData gameData = gameDataFrom(event);
        if (command.equals("new") && optionals.length >= 1 && !optionals[0].trim().isEmpty()) {
            try {
                Integer gameId = gameEngine.newGame(optionals[0]);
                gameIdMap.put(event.getChannel().getId(),gameId);
                event.getChannel().sendMessage("new Game "+optionals[0] ).queue();
            }catch(Exception e){
                event.getChannel().sendMessage(e.getMessage() ).queue();
            }
            return;
        }
        Game game = gameEngine.getGame(gameData);
        if (game == null) {
            describeGameBot(event);
            return;
        }
        if (gameEngine.hasGameMethod(command,gameData)) {
            try {
                gameEngine.callGameMethod(command, gameData, result -> {
                    sendMessage(event, gameData, result);
                }, optionals);
            }catch(Exception e){
                event.getChannel().sendMessage(command + " seems broken plz contact developer").queue();
            }
        } else {
            describeGameBot(gameData, event);
        }

    }

    private void describeGameBot(MessageReceivedEvent event) {
        EmbedBuilder result = new EmbedBuilder();
        result.setTitle("No Game Selected");
        result.setDescription("Howdy, I am Gamebot");
        setFooter(result);
        event.getChannel().sendMessage(result.build()).queue();
    }

    private void describeGameBot(GameData gameData, MessageReceivedEvent event) {
        String gameName = gameEngine.getGameName(gameData);
        EmbedBuilder result = new EmbedBuilder();
        result.setTitle(gameName);
        result.setDescription("type \\g [" + String.join(", ", gameEngine.getAvailableMethodNames(gameData)) + "]");
        setFooter(result);
        event.getChannel().sendMessage(result.build()).queue();
    }

    private void setFooter(EmbedBuilder result) {
        result.setFooter("type \\g new $gamename to start a new game");
    }

    private void sendMessage(MessageReceivedEvent event,GameData gameData, GameResult gameResult) {
        String text = "@"+gameData.getUserName()+":"+gameResult.getText();
        MessageAction messageAction = event.getChannel()
                    .sendMessage(text);
        Map<String, InputStream> inputStreams = new HashMap();
        gameResult.getImageIds().forEach(imagePath->{
            int counter = inputStreams.size();
            inputStreams.put(counter+imagePath,getClass().getClassLoader().getResourceAsStream("static"+imagePath));
            messageAction.addFile(inputStreams.get(counter+imagePath), counter+imagePath);
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
        chat.sendToChat(gameData,gameResult);
    }

    private GameData gameDataFrom(MessageReceivedEvent event){
        GameData gameData = new GameData();
        gameData.setGameId(gameIdMap.get(event.getChannel().getId()));
        gameData.setUserId(event.getAuthor().getId());
        gameData.setUserName(event.getAuthor().getName());

        return gameData;
    };

}
