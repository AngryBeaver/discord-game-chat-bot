package net.verplanmich.bot.game;

import java.util.ArrayList;
import java.util.List;

public class GameResult {

    private List<String> imageIds = new ArrayList();
    private String text = "";

    public List<String> getImageIds() {
        return imageIds;
    }

    public void addImageId(String imageId) {
        this.imageIds.add(imageId);
    }

    public void addImageIds(List<String> imageIds) {
        this.imageIds.addAll(imageIds);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
