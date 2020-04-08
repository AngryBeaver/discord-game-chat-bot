package net.verplanmich.bot.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GameResult {

    private List<String> imageIds = new ArrayList();    //default imgs for chat
    private String text = "";                           //default msg for chat
    private Map<String,Object> map = new HashMap();     //key value pairs data for client trigger.
    private List<String> events = new ArrayList();       //event identifcation for client trigger.

    public List<String> getImageIds() {
        return imageIds;
    }

    public GameResult addImageId(String imageId) {
        this.imageIds.add(imageId);
        return this;
    }

    public GameResult addImageIds(List<String> imageIds) {
        this.imageIds.addAll(imageIds);
        return this;
    }

    public String getText() {
        return text;
    }

    public GameResult setText(String text) {
        this.text = text;
        return this;
    }

    public Map<String,Object> getMap() { return map; }

    public Object get(String key) {
        return map.get(key);
    }

    public GameResult set(String key,Object value) {
        map.put(key,value);
        return this;
    }

    public List<String> getEvents() {
        return events;
    }

    public GameResult addEvent(String event) {
        events.add(event);
        return this;
    }
}
