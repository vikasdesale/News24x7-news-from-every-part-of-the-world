package com.android.news24x7.sync;

/**
 * Created by Dell on 6/6/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.news24x7.R;


public final class NewsPreferences {

    public static boolean areNotificationsEnabled(Context context) {
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);

        boolean shouldDisplayNotificationsByDefault = context
                .getResources()
                .getBoolean(R.bool.show_notifications_by_default);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        boolean shouldDisplayNotifications = sp
                .getBoolean(displayNotificationsKey, shouldDisplayNotificationsByDefault);

        return shouldDisplayNotifications;
    }

    public static void saveLastNotificationTime(Context context, long timeOfNotification) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        editor.putLong(lastNotificationKey, timeOfNotification);
        editor.apply();
    }
}