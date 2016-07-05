package com.zholdiyarov.appwidget.utils;

import com.zholdiyarov.appwidget.rss_reader.RssItem;

import java.util.List;

/**
 * Created by szholdiyarov on 7/5/16.
 * This class is followed by the idea of Singleton design pattern. Which means that all the variables are static(but not final).
 * The main idea of this is to use it for saving which rss item is currently being showed and to allow user to move forward or back(iterate through the rss list).
 * However, a disadvantage of such approach is that if user will add several(or more) instances of the app widget then all of them will show exactly same rss item.
 */
public class RssIterator {
    // Variables
    private static RssIterator instance = new RssIterator(); // singleton principle
    private static List<RssItem> rssItemList; // rss items
    private static int currentShowedRssItemNumber = 0; // position index(in the list) of rss item which is currently showed on the widget.

    /**
     * Just to hide a constructor so that it can not be initialized. (singleton)
     **/
    private RssIterator() {
    }

    /**
     * Get the only object available. (singleton)
     **/
    public static RssIterator getInstance() {
        return instance;
    }

    /**
     * This method is used to update rss item list.
     * Ideally, before calling this method, an application should compare current saved list from the new list
     *
     * @param rssItems This is the parameter which will be saved as a rss item list(new one).
     */
    public void setRssItemList(List<RssItem> rssItems) {
        rssItemList = rssItems;
    }

    /**
     * This method is used to move current position of the rss item forward.
     * For example, if widget displays news in the position 1 then this method will show news in the position 2.
     *
     * @return RssItem This returns an updated item to be showed on the widget.
     */
    public RssItem moveForward() {
        if ((currentShowedRssItemNumber + 1) != rssItemList.size()) {
            currentShowedRssItemNumber++;
        } else {
            currentShowedRssItemNumber = 0;
        }
        return getCurrentRssItem();
    }

    /**
     * This method is used to move current position of the rss item backward.
     * For example, if widget displays news in the position 1 then this method will show news in the position 0.
     * If it reaches 0 then it will go to the last object in the Rss Items list.
     *
     * @return RssItem This returns an updated item to be showed on the widget.
     */
    public RssItem moveBack() {
        if (currentShowedRssItemNumber == 0) {
            currentShowedRssItemNumber = rssItemList.size() - 1;
        } else {
            currentShowedRssItemNumber--;
        }
        return getCurrentRssItem();
    }

    /**
     * @return RssItem This returns a current rss item which is displayed in the widget.
     */
    public RssItem getCurrentRssItem() {
        return rssItemList.get(currentShowedRssItemNumber);
    }


    /**
     * @return List<RssItem> This returns a saved rss items list.
     */
    public List<RssItem> getRssItemList() {
        return rssItemList;
    }


}
