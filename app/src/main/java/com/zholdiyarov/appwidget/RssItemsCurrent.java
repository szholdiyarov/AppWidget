package com.zholdiyarov.appwidget;

import com.zholdiyarov.appwidget.rss_reader.RssItem;

import java.util.List;

/**
 * Created by szholdiyarov on 7/5/16.
 */
public class RssItemsCurrent {
    private static RssItemsCurrent instance = new RssItemsCurrent();
    private static List<RssItem> rssItemList;
    private static int currentShowedRssItemNumber = 0;
    private static String lastBuildDate = "";

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        RssItemsCurrent.lastBuildDate = lastBuildDate;
    }

    public int getCurrentShowedRssItemNumber() {
        return currentShowedRssItemNumber;
    }

    public RssItem moveForward() {
        if ((currentShowedRssItemNumber + 1) != rssItemList.size()) {
            currentShowedRssItemNumber++;
        } else {
            currentShowedRssItemNumber = 0;
        }
        return getCurrentRssItem();
    }

    public RssItem moveBack() {
        if (currentShowedRssItemNumber == 0) {
            currentShowedRssItemNumber = rssItemList.size() - 1;
        } else {
            currentShowedRssItemNumber--;
        }
        return getCurrentRssItem();
    }

    public RssItem getCurrentRssItem() {
        return rssItemList.get(currentShowedRssItemNumber);
    }

    private RssItemsCurrent() {
    }

    //Get the only object available
    public static RssItemsCurrent getInstance() {
        return instance;
    }

    public void setRssItemList(List<RssItem> rssItems) {
        rssItemList = rssItems;
    }

    public List<RssItem> getRssItemList() {
        return rssItemList;
    }


}
