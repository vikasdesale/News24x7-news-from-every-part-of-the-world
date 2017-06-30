package com.news.news24x7.util;

/**
 * Created by Dell on 6/6/2017.
 */

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.news.news24x7.R;
import com.news.news24x7.activities.DetailsActivity;
import com.news.news24x7.interfaces.ColumnsNews;
import com.news.news24x7.prefs.NewsPreferences;
import com.news.news24x7.widget.NewsWidgetProvider;
import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

import static com.news.news24x7.R.drawable.ic_stat_n;
import static com.news.news24x7.R.drawable.placeholder;


public class NotificationUtils {


    /**
     * Constructs and displays a notification for the newly updated weather for today.
     *
     * @param context Context used to query our ContentProvider and use various Utility methods
     */
    public static void notifyUserOfNewWeather(Context context) {
        NewsUtil mNewsUtil = new NewsUtil();

        int WEATHER_NOTIFICATION_ID = 3004;
        int i = 0;
        // we'll query our contentProvider, as always
        Cursor cursor = null;
        cursor = mNewsUtil.allNewsCursor(context);
        int rand = 1 + (int) (Math.random() * ((3 - 1) + 3));
        if (cursor != null) {
            cursor.moveToFirst();

            while (i < rand && i < cursor.getCount()) {

                Resources resources = context.getResources();
                // On Honeycomb and higher devices, we can retrieve the size of the large icon
                // Prior to that, we use a fixed size
                @SuppressLint("InlinedApi")
                int largeIconWidth = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
                        ? resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_width)
                        : resources.getDimensionPixelSize(R.dimen.notification_large_icon_default);
                @SuppressLint("InlinedApi")
                int largeIconHeight = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
                        ? resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_height)
                        : resources.getDimensionPixelSize(R.dimen.notification_large_icon_default);

                // Retrieve the large icon
                Bitmap largeIcon;
                try {
                    largeIcon = Glide.with(context)
                            .load(cursor.getString(cursor.getColumnIndex(ColumnsNews.URL_TO_IMAGE)))
                            .asBitmap()
                            .error(placeholder)
                            .fitCenter()
                            .into(largeIconWidth, largeIconHeight).get();
                } catch (InterruptedException | ExecutionException e) {
                    //  Log.e(LOG_TAG, "Error retrieving large icon from " +cursor.getString(cursor.getColumnIndex(ColumnsNews.URL_TO_IMAGE)) , e);
                    largeIcon = BitmapFactory.decodeResource(resources, placeholder);
                }
                String title = cursor.getString(cursor.getColumnIndex(ColumnsNews.TITLE));

                // Define the text of the forecast.
                String contentText = cursor.getString(cursor.getColumnIndex(ColumnsNews.DESCRIPTION));

// Create the style object with BigPictureStyle subclass.
                NotificationCompat.BigPictureStyle notiStyle = new
                        NotificationCompat.BigPictureStyle();
                notiStyle.setBigContentTitle(title);
                notiStyle.setSummaryText(contentText);

// Add the big picture to the style.
                notiStyle.bigPicture(largeIcon);
            /*
             * NotificationCompat Builder is a very convenient way to build backward-compatible
             * notifications. In order to use it, we provide a context and specify a color for the
             * notification, a couple of different icons, the title for the notification, and
             * finally the text of the notification, which in our case in a summary of today's
             * forecast.
             */
//          COMPLETED (2) Use NotificationCompat.Builder to begin building the notification
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setColor(ContextCompat.getColor(context, R.color.accent))
                        .setSmallIcon(ic_stat_n)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(title)
                        .setContentText(contentText)
                        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                        .setAutoCancel(true)
                        .setStyle(notiStyle);

                Intent intent = new Intent(context, DetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString(NewsWidgetProvider.EXTRA_TITLE, cursor.getString(cursor.getColumnIndex(ColumnsNews.TITLE)));
                extras.putString(NewsWidgetProvider.EXTRA_AUTHOR, cursor.getString(cursor.getColumnIndex(ColumnsNews.AUTHOR)));
                extras.putString(NewsWidgetProvider.EXTRA_DESCRIPTION, cursor.getString(cursor.getColumnIndex(ColumnsNews.DESCRIPTION)));
                extras.putString(NewsWidgetProvider.EXTRA_IMAGE_URL, cursor.getString(cursor.getColumnIndex(ColumnsNews.URL_TO_IMAGE)));
                extras.putString(NewsWidgetProvider.EXTRA_URL, cursor.getString(cursor.getColumnIndex(ColumnsNews.URL)));
                extras.putString(NewsWidgetProvider.EXTRA_DATE, cursor.getString(cursor.getColumnIndex(ColumnsNews.PUBLISHED_AT)));
                intent.putExtras(extras);
                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                taskStackBuilder.addNextIntentWithParentStack(intent);
                PendingIntent resultPendingIntent = taskStackBuilder
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                notificationBuilder.setContentIntent(resultPendingIntent);
                NotificationManager notificationManager = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(WEATHER_NOTIFICATION_ID++, notificationBuilder.build());

                NewsPreferences.saveLastNotificationTime(context, System.currentTimeMillis());
                cursor.moveToNext();
                i++;
            }


        }
        try {

            if (cursor != null || !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
        }


    }

}