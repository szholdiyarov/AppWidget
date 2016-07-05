package com.zholdiyarov.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.zholdiyarov.appwidget.rss_reader.RssItem;
import com.zholdiyarov.appwidget.rss_reader.RssReader;
import com.zholdiyarov.appwidget.utils.RssIterator;
import com.zholdiyarov.appwidget.utils.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * This is the implementation of the Alarm Manager by extending Broadcast Receiver.
 *
 * @author Sanzhar Zholdiyarov
 * @see <a href="https://developer.android.com/reference/android/content/BroadcastReceiver.html">Android Developer Broadcast Receiver</a>
 * @since 6/28/16
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    /* Variable declaration */
    private static final String FORWARD_CLICK = "android.appwidget.action.GO_FORWARD";
    private static final String BACK_CLICK = "android.appwidget.action.GO_BACK";
    private static final String WAKE_LOCK_TAG = "com.zholdiyarov.app_widget.WAKE_LOG_TAG";

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
     * When this method is called we are updating rss item list in the new thread
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        RssDownloader task = new RssDownloader(context);
        task.execute();
    }

    /**
     * This is the inner class which is used as Async Task to update rss feed and to update widget values.
     */
    private class RssDownloader extends AsyncTask<Void, Void, RssReader> {

        // Context where widget is running
        private Context mContext;

        public RssDownloader(Context context) {
            mContext = context;
        }

        @Override
        protected RssReader doInBackground(Void... voids) {
            String feed = readFromUrl(Util.getRssUrl(mContext)); // download rss feed
            return new RssReader(feed);
        }

        @Override
        protected void onPostExecute(RssReader rssReader) {
            List<RssItem> rssItemList = null;
            try {
                rssItemList = rssReader.getItems();
                RssIterator.getInstance().setRssItemList(rssItemList); // update rss feed
            } catch (Exception e) {
                e.printStackTrace();
            }

            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG);
            //Acquire the lock
            wl.acquire();

            // Intents for handling button press on the widget
            Intent intent_forward_click = new Intent(FORWARD_CLICK);
            Intent intent_back_click = new Intent(BACK_CLICK);

            // PendingIntents for handling button press on the widget
            PendingIntent pendingIntent_forward_click = PendingIntent.getBroadcast(mContext,
                    0, intent_forward_click, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntent_back_click = PendingIntent.getBroadcast(mContext,
                    0, intent_back_click, PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_layout);

            // update title and description of the current rss item(displayed)
            if (RssIterator.getInstance().getCurrentRssItem() != null) {
                remoteViews.setTextViewText(R.id.textView_title, RssIterator.getInstance().getCurrentRssItem().getTitle());
                remoteViews.setTextViewText(R.id.textView_text, RssIterator.getInstance().getCurrentRssItem().getDescription());
            } else {
                remoteViews.setTextViewText(R.id.textView_title, "Ошибка");
                remoteViews.setTextViewText(R.id.textView_text, "Ошибка загрузки");
            }


            // setting button click intents
            remoteViews.setOnClickPendingIntent(R.id.actionButton_forward, pendingIntent_forward_click);
            remoteViews.setOnClickPendingIntent(R.id.actionButton_back, pendingIntent_back_click);

            // now simply update widget
            ComponentName currentWidget = new ComponentName(mContext, WidgetProvider.class);
            AppWidgetManager.getInstance(mContext).updateAppWidget(currentWidget, remoteViews);

            //Release the lock
            wl.release();
        }

        /**
         * This method is simply downloading a text
         *
         * @param uri Ths uri from which we should download the text
         * @return String This returns the whole XML of the RSS.
         */
        private String readFromUrl(String uri) {
            BufferedReader bufferedReader = null;
            try {
                // Create a URL for the desired page
                URL url = new URL(uri);

                // Read all the text returned by the server
                bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                String result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result = result + line;
                }
                bufferedReader.close();

                return result;

            } catch (MalformedURLException e) {
                Log.d("ReadFromUrl", "MalformedURLException " + e);
            } catch (IOException e) {
                Log.d("ReadFromUrl", "IOException " + e);
            }
            return null; // something went wrong
        }
    }


}
