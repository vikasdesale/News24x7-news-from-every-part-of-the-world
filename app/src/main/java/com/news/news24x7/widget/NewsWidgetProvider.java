package com.news.news24x7.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.news.news24x7.R;
import com.news.news24x7.activities.DetailsActivity;
import com.news.news24x7.activities.HomeActivity;

/**
 * Created by Dell on 6/10/2017.
 */

public class NewsWidgetProvider extends AppWidgetProvider {

    public static String EXTRA_TITLE = "_TITLE";
    public static String EXTRA_AUTHOR = "_AUTHOR";
    public static String EXTRA_IMAGE_URL = "_IMAGE_URL";
    public static String EXTRA_URL = "_URL";
    public static String EXTRA_DESCRIPTION = "_DESCRIPTION";
    public static String EXTRA_DATE = "_DATE";

    public static String ACTION_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        int[] appWidgetIDs;
        if (intent.getAction().equals(ACTION_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetIDs = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIDs, R.id.newsList);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.news_widget);
            Intent svcIntent = new Intent(context, NewsWidgetService.class);
            svcIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
            widgetViews.setRemoteAdapter(R.id.newsList, svcIntent);
            widgetViews.setEmptyView(R.id.newsList, R.id.emptyMessage);

            Intent clickAppIntent = new Intent(context, HomeActivity.class);
            Intent clickIntent = new Intent(context, DetailsActivity.class);
            PendingIntent appPI = PendingIntent.getActivity(context, 0, clickAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent clickPI = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            widgetViews.setOnClickPendingIntent(R.id.newsTitle, appPI);
            widgetViews.setPendingIntentTemplate(R.id.newsList, clickPI);

            appWidgetManager.updateAppWidget(appWidgetId, widgetViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}