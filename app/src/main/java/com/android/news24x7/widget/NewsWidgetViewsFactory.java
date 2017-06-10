package com.android.news24x7.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.news24x7.R;
import com.android.news24x7.activities.DetailsActivity;
import com.android.news24x7.database.NewsProvider;
import com.android.news24x7.interfaces.ColumnsNews;
import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

/**
 * Created by Dell on 6/10/2017.
 */

public class NewsWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Cursor cursor;
    private Context context = null;
    private int appWidgetId;
    long token;
    String mTitle;
    String mAuthor;
    String mDescription;
    String mUrl;
    String mUrlToImage;
    String mPublishedAt;
    public NewsWidgetViewsFactory(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        query();
    }

    @Override
    public void onDataSetChanged() {
        query();
    }

    public void query() {
        token = Binder.clearCallingIdentity();
        cursor =context.getContentResolver().query(NewsProvider.MyNews.CONTENT_URI,
                null, null, null, null);
        Binder.restoreCallingIdentity(token);
    }

    @Override
    public void onDestroy() {
        // Releases the cursor when we are done with it
        if ( cursor != null ) {
            cursor.close();
            cursor = null;
        }
    }

    @Override
    public int getCount() {
        // returns the count of the cursor or 0 if cursor is null
        return cursor != null ? cursor.getCount() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        
        // Check we have data at this view position - If we don't then exit with null
        if ( !cursor.moveToPosition(position) )
            return null;
         mTitle=cursor.getString(cursor.getColumnIndex(ColumnsNews.TITLE));
         mAuthor=cursor.getString(cursor.getColumnIndex(ColumnsNews.AUTHOR));
         mDescription=cursor.getString(cursor.getColumnIndex(ColumnsNews.DESCRIPTION));
         mUrl = cursor.getString(cursor.getColumnIndex(ColumnsNews.URL));
         mUrlToImage = cursor.getString(cursor.getColumnIndex(ColumnsNews.URL_TO_IMAGE));
         mPublishedAt = cursor.getString(cursor.getColumnIndex(ColumnsNews.PUBLISHED_AT));


        final RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.list_item_news);

       Bitmap bitmap = null;
        try {
            bitmap=
                    Glide.
                            with(context).
                            load(mUrlToImage).
                            asBitmap().
                            into(200, 200). // Width and height
                            get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(bitmap!=null) {
        row.setImageViewBitmap(R.id.news_thumbnail, bitmap);

        }
        row.setTextViewText(R.id.news_article_title,mTitle);
        row.setTextViewText(R.id.news_published_at, mPublishedAt);

        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putString(NewsWidgetProvider.EXTRA_TITLE, mTitle);
        extras.putString(NewsWidgetProvider.EXTRA_AUTHOR, mAuthor);
        extras.putString(NewsWidgetProvider.EXTRA_DESCRIPTION, mDescription);
        extras.putString(NewsWidgetProvider.EXTRA_IMAGE_URL, mUrlToImage);
        extras.putString(NewsWidgetProvider.EXTRA_URL, mUrl);
        extras.putString(NewsWidgetProvider.EXTRA_DATE, mPublishedAt);
        intent.putExtras(extras);

        row.setOnClickFillInIntent(android.R.id.text1, intent);
        Intent fillInIntent = new Intent(context, DetailsActivity.class);
        fillInIntent.putExtras(extras);
        row.setOnClickFillInIntent(R.id.newsList, fillInIntent);

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}