package com.android.news24x7.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.android.news24x7.BuildConfig;
import com.android.news24x7.R;
import com.android.news24x7.activities.HomeActivity;
import com.android.news24x7.database.NewsUtil;
import com.android.news24x7.interfaces.ColumnsNews;
import com.android.news24x7.parcelable.Article;
import com.android.news24x7.retrofit.ApiClient;
import com.android.news24x7.retrofit.ApiInterface;
import com.android.news24x7.retrofit.NewsResponse;
import com.bumptech.glide.Glide;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.android.news24x7.R.drawable.titled;

public class NewsSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = NewsSyncAdapter.class.getSimpleName();
    public static final String ACTION_DATA_UPDATED =
            "com.android.news24x7.ACTION_DATA_UPDATED";
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 5;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;
    NewsUtil mNewsUtil;
    private ArrayList<Article> articlesList=null;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NEWS_STATUS_OK, NEWS_STATUS_SERVER_DOWN, NEWS_STATUS_SERVER_INVALID, NEWS_STATUS_UNKNOWN, NEWS_STATUS_INVALID})
    public @interface Newstatus {}

    public static final int NEWS_STATUS_OK = 0;
    public static final int NEWS_STATUS_SERVER_DOWN = 1;
    public static final int NEWS_STATUS_SERVER_INVALID = 2;
    public static final int NEWS_STATUS_UNKNOWN = 3;
    public static final int NEWS_STATUS_INVALID = 4;

    public NewsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mNewsUtil=new NewsUtil();
    }
    private void fetchNews(Map<String, String> data) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        data.put("apiKey", BuildConfig.NEWS_API_ORG_KEY);
        Call<NewsResponse> call = null;
        call = apiService.getNews(data);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if(response.isSuccessful()) {
                    articlesList = (ArrayList<Article>) response.body().getArticles();
                }
                else if(response.code()==404){
                    Log.d("dddddddddd","vvvvvvvv"+response.body());
                    Log.d("dddddddddd","vvvvvvvv"+response.errorBody());
                }
                  mNewsUtil.insertData(getContext(), articlesList, "no");

            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                Log.d(TAG, "server contacted at: " + call.request().url());

            }
        });
    }
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");


       Context context = getContext();
        Map<String, String> data = new HashMap<>();
            data.put("source", "the-hindu");
            data.put("sortBy", "latest");
            fetchNews(data);
//            if (articlesList.size() == 0) {
                // Stream was empty.  No point in parsing.
                //setNewsStatus(getContext(), NEWS_STATUS_SERVER_DOWN);
  //              return;
    //        }
//        updateWidgets();
        notifyWeather();

        return;
    }



    private void updateWidgets() {
        Context context = getContext();
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
         context.sendBroadcast(dataUpdatedIntent);
    }


    private void notifyWeather() {
        Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if ( displayNotifications ) {

            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            long lastSync = prefs.getLong(lastNotificationKey, 0);

           // if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {

                // we'll query our contentProvider, as always
                Cursor cursor = null;

                    cursor = mNewsUtil.allNewsCursor(getContext());
                if (cursor.moveToFirst()) {
                    String args[]=mNewsUtil.getData(cursor);



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
                                .error(titled)
                                .fitCenter()
                                .into(largeIconWidth, largeIconHeight).get();
                    } catch (InterruptedException | ExecutionException e) {
                        Log.e(LOG_TAG, "Error retrieving large icon from " +cursor.getString(cursor.getColumnIndex(ColumnsNews.URL_TO_IMAGE)) , e);
                        largeIcon = BitmapFactory.decodeResource(resources, titled);
                    }
                    String title = context.getString(R.string.app_name);

                    // Define the text of the forecast.
                    String contentText = String.format(context.getString(R.string.format_notification),
                            cursor.getString(cursor.getColumnIndex(ColumnsNews.TITLE)));

                    // NotificationCompatBuilder is a very convenient way to build backward-compatible
                    // notifications.  Just throw in some data.
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getContext())
                                    .setColor(resources.getColor(R.color.primary_light))
                                    .setSmallIcon(titled)
                                    .setLargeIcon(largeIcon)
                                    .setContentTitle(title)
                                    .setContentText(contentText);

                    // Make something interesting happen when the user clicks on the notification.
                    // In this case, opening the app is sufficient.
                    Intent resultIntent = new Intent(context, HomeActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager =
                            (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
                    mNotificationManager.notify(WEATHER_NOTIFICATION_ID, mBuilder.build());

                    //refreshing last sync
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong(lastNotificationKey, System.currentTimeMillis());
                    editor.commit();
                }
            }
        //}
    }



    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        NewsSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);


        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


}