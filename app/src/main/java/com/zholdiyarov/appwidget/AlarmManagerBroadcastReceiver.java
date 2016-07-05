package com.zholdiyarov.appwidget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.zholdiyarov.appwidget.rss_reader.RssItem;
import com.zholdiyarov.appwidget.rss_reader.RssReader;
import com.zholdiyarov.appwidget.utils.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by szholdiyarov on 7/4/16.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    private static final String FORWARD_CLICK = "android.appwidget.action.GO_FORWARD";
    private static final String BACK_CLICK = "android.appwidget.action.GO_BACK";

    @Override
    public void onReceive(Context context, Intent intent) {
        print("onReceiveeee");
        RssDownloader task = new RssDownloader(context);
        task.execute();
    }

    private void print(String text) {
        Log.d("AlarmManagerBroadcast", text);
    }


    private class RssDownloader extends AsyncTask<Void, Void, RssReader> {
        private Context mContext;

        public RssDownloader(Context context) {
            mContext = context;
        }

        @Override
        protected RssReader doInBackground(Void... voids) {
            String feed = readFromUrl(Util.getRssUrl(mContext));
            return new RssReader(feed);
        }

        @Override
        protected void onPostExecute(RssReader rssReader) {
            print("onPostExecute");
            List<RssItem> rssItemList = null;
            try {
                rssItemList = rssReader.getItems();
                RssItemsCurrent.getInstance().setRssItemList(rssItemList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
            //Acquire the lock
            wl.acquire();

            Intent intent_forward_click = new Intent(FORWARD_CLICK);
            Intent intent_back_click = new Intent(BACK_CLICK);

            PendingIntent pendingIntent_forward_click = PendingIntent.getBroadcast(mContext,
                    0, intent_forward_click, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntent_back_click = PendingIntent.getBroadcast(mContext,
                    0, intent_back_click, PendingIntent.FLAG_UPDATE_CURRENT);


            //You can do the processing here update the widget/remote views.
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_layout);

            remoteViews.setTextViewText(R.id.textView_title, RssItemsCurrent.getInstance().getCurrentRssItem().getTitle());
            remoteViews.setTextViewText(R.id.textView_text, RssItemsCurrent.getInstance().getCurrentRssItem().getDescription());

            remoteViews.setOnClickPendingIntent(R.id.actionButton_forward, pendingIntent_forward_click);
            remoteViews.setOnClickPendingIntent(R.id.actionButton_back, pendingIntent_back_click);

            ComponentName thiswidget = new ComponentName(mContext, WidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(mContext);
            manager.updateAppWidget(thiswidget, remoteViews);
            //Release the lock
            wl.release();
        }


        private String readFromUrl(String uri) {
            BufferedReader bufferedReader = null;
            try {
                // Create a URL for the desired page
                URL url = new URL(uri);

                // Read all the text returned by the server
                bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                String result = null;
                while ((line = bufferedReader.readLine()) != null) {
                    result = result + line;
                }
                bufferedReader.close();

                if (result.substring(0, 4).equalsIgnoreCase("null")) {
                    result = result.substring(4, result.length());
                }

                return result;

            } catch (MalformedURLException e) {
                Log.d("ReadFromUrl", "Exception " + e);
            } catch (IOException e) {
                Log.d("ReadFromUrl", "Exception " + e);
            }
            return null;
        }
    }


}
