package com.zholdiyarov.appwidget.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zholdiyarov.appwidget.WidgetConfigurationActivity;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by szholdiyarov on 7/4/16.
 */
public class Util {

    public static final String CONFIGURATION_PREF_NAME = "WidgetConfigDetails";
    public static final String CONFIGURATION_RSS_NAME = "rss";

    public static String getCurrentTime(String timeformat) {
        Format formatter = new SimpleDateFormat(timeformat);
        return formatter.format(new Date());
    }

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
