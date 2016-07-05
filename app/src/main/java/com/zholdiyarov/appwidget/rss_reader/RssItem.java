package com.zholdiyarov.appwidget.rss_reader;

/**
 * Created by szholdiyarov on 6/28/16.
 * This class is the basic entity of rss item. It is used for saving "item" from rss.
 */
public class RssItem {
    /* Variables */
    String title;
    String description;
    String link;
    String imageUrl;

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    /**
     * Instead of saving description directly this method should be used. Because it will avoid symbols such as " etc.
     **/
    public void appendDescription(String newDescription) {
        if (description == null || description.isEmpty()) { // if we do not have any description yes, then save this.
            setDescription(newDescription);
        } else {
            description = description + newDescription; // just append new description to the existing one.
        }
    }

    /**
     * Instead of saving title directly this method should be used. Because it will avoid symbols such as " etc.
     **/
    public void appendTitle(String newTitle) {
        if (title == null || title.isEmpty()) {  // if we do not have any description yes, then save this.
            setTitle(newTitle);
        } else {
            title = title + newTitle; // just append new title to the existing one.
        }
    }


}