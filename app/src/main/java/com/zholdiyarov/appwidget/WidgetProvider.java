package com.zholdiyarov.appwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.zholdiyarov.appwidget.utils.RssIterator;

/**
 * Defines the basic methods that are used to interface with the App Widget, based on broadcast events.
 * Through it, an application receives broadcasts when the App Widget is updated, enabled, disabled and deleted.
 * However, instead of setting updatePeriodMillis, we are using an Alarm Manager to update the widget every minute.
 *
 * @author Sanzhar Zholdiyarov
 * @see <a href="https://developer.android.com/guide/topics/appwidgets/index.html">Android Developer App Widget</a>
 * @since 6/28/16
 */

public class WidgetProvider extends AppWidgetProvider {

    /* Variable declaration*/
    private static final String FORWARD_CLICK = "android.appwidget.action.GO_FORWARD";
    private static final String BACK_CLICK = "android.appwidget.action.GO_BACK";

    /**
     * This method is used to dispatch calls to the various other methods on AppWidgetProvider.
     * In particular, it is overrided because the widget contains two buttons which are used to iterate through the rss items list.
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        if (RssIterator.getInstance().getRssItemList() != null) { // if the application already have downloaded rss
            if (FORWARD_CLICK.equals(intent.getAction())) { // if "forward" button has been pressed => first move to the next rss item
                RssIterator.getInstance().moveForward();
            } else if (BACK_CLICK.equals(intent.getAction())) { // if "back" button has been pressed => first move to the previous rss item
                RssIterator.getInstance().moveBack();
            }

            // and now apply all the changes by displaying requests rss item
            remoteViews.setTextViewText(R.id.textView_title, RssIterator.getInstance().getCurrentRssItem().getTitle());
            remoteViews.setTextViewText(R.id.textView_text, RssIterator.getInstance().getCurrentRssItem().getDescription());

            // now simply update widget
            ComponentName currentWidget = new ComponentName(context, WidgetProvider.class);
            AppWidgetManager.getInstance(context).updateAppWidget(currentWidget, remoteViews);
        }


    }

    /**
     * This method is  called in response to the ACTION_APPWIDGET_DISABLED broadcast,
     * which is sent when the last AppWidget instance for this provider is deleted.
     * In this method we are cancelling Alarm Manager to stop updating the widget.
     *
     * @param context The Context in which the receiver is running.
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    /**
     * This method is  called in response to the ACTION_APPWIDGET_ENABLED broadcast when the a AppWidget for this provider is instantiated.
     * In this method we are creating the Alarm Manager to update widget by using Real Time clock principle.
     *
     * @param context The Context in which the receiver is running.
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 100 * 3, 1000, pi);
    }

    /**
     * This method is Called in response to the ACTION_APPWIDGET_UPDATE and ACTION_APPWIDGET_RESTORED broadcasts
     * when this AppWidget provider is being asked to provide RemoteViews for a set of AppWidgets.
     *
     * @param context          The Context in which the receiver is running.
     * @param appWidgetManager A AppWidgetManager object
     * @param appWidgetIds     The appWidgetIds for which an update is needed.
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Nothing to do here as we are updating the widget by using Alarm Manager
    }
}