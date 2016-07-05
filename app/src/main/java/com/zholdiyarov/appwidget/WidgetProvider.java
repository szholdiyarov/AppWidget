package com.zholdiyarov.appwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
    private static final String FORWARD_CLICK = "android.appwidget.action.GO_FORWARD";
    private static final String BACK_CLICK = "android.appwidget.action.GO_BACK";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //You can do the processing here update the widget/remote views.
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        if(RssItemsCurrent.getInstance().getRssItemList() != null){
            if (FORWARD_CLICK.equals(intent.getAction())) {
                RssItemsCurrent.getInstance().moveForward();
            } else if (BACK_CLICK.equals(intent.getAction())) {
                RssItemsCurrent.getInstance().moveBack();
            }

            remoteViews.setTextViewText(R.id.textView_title, RssItemsCurrent.getInstance().getCurrentRssItem().getTitle());
            remoteViews.setTextViewText(R.id.textView_text, RssItemsCurrent.getInstance().getCurrentRssItem().getDescription());

            ComponentName thiswidget = new ComponentName(context, WidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(thiswidget, remoteViews);
        }


    }


    @Override
    public void onDisabled(Context context) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        super.onDisabled(context);
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 100 * 3, 1000, pi);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    }
}