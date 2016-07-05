package com.zholdiyarov.appwidget.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by szholdiyarov on 7/4/16.
 * This class is used for accessing shared preferences to save and to retrieve rss url.
 */
public class Util {

    public static final String CONFIGURATION_PREF_NAME = "WidgetConfigDetails";
    public static final String CONFIGURATION_RSS_NAME = "rss";

    public static void saveRssUrlInSharedPreferences(Context context, String rssUrl) {
        SharedPreferences settings = context.getSharedPreferences(CONFIGURATION_PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CONFIGURATION_RSS_NAME, rssUrl);
        // Commit the edits!
        editor.commit();
    }

    public static String getRssUrl(Context context) {
        SharedPreferences settings = context.getSharedPreferences(CONFIGURATION_PREF_NAME, 0);
        return settings.getString(CONFIGURATION_RSS_NAME, "");
    }

}
