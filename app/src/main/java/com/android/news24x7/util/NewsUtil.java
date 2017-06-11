package com.android.news24x7.util;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.database.Cursor;

import com.android.news24x7.database.NewsProvider;
import com.android.news24x7.interfaces.ColumnsNews;
import com.android.news24x7.parcelable.Article;

import java.util.ArrayList;

/**
 * Created by Dell on 6/4/2017.
 */

public class NewsUtil {

    public static final String FAVORITE = "favorite";
    Cursor c;
    int count = 0;

    public static void CacheDelete(Context context) {

        try {

            context.getContentResolver().delete(NewsProvider.MyNews.CONTENT_URI,
                    null, null);
        } catch (Exception e) {
        }

    }

    public static void FavouriteDelete(Context context, String title) {

        try {

            context.getContentResolver().delete(NewsProvider.NewsFavourite.CONTENT_URI_FAVOURITE,
                    ColumnsNews.TITLE + "=?", new String[]{title});
        } catch (Exception e) {
        }

    }

    public static int CheckisFavourite(Context context, String title) {
        Cursor me = null;
        int flag = 0;
        try {
            me = context.getContentResolver().query(NewsProvider.NewsFavourite.CONTENT_URI_FAVOURITE,
                    null, ColumnsNews.TITLE + "=?", new String[]{title}, null);
            if (title.equals(me.getString(me.getColumnIndex(ColumnsNews.TITLE)))) {
                flag = 0;
            } else {
                flag = 1;
            }

        } catch (Exception e) {

        } finally {
            if (me != null) {
                me.close();
            }
        }
        if (flag == 1 || me.getCount() == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public Cursor allNewsCursor(Context context) {
        c = null;
        c = context.getContentResolver().query(NewsProvider.MyNews.CONTENT_URI,
                null, null, null, null);
        return c;


    }

    public Cursor favoriteNewsCursor(Context context) {
        c = null;

        c = context.getContentResolver().query(NewsProvider.NewsFavourite.CONTENT_URI_FAVOURITE,
                null, null, null, null);
        return c;
    }

    public String[] getData(Cursor onClick) {

        String mAuthor = onClick.getString(onClick.getColumnIndex(ColumnsNews.TITLE));
        String mTitle = onClick.getString(onClick.getColumnIndex(ColumnsNews.AUTHOR));
        String mDescription = onClick.getString(onClick.getColumnIndex(ColumnsNews.DESCRIPTION));
        String mUrl = onClick.getString(onClick.getColumnIndex(ColumnsNews.URL));
        String mUrlToImage = onClick.getString(onClick.getColumnIndex(ColumnsNews.URL_TO_IMAGE));
        String mPublishedAt = onClick.getString(onClick.getColumnIndex(ColumnsNews.PUBLISHED_AT));

        return new String[]{mAuthor, mTitle, mDescription, mUrl, mUrlToImage, mPublishedAt};
    }

    public int getAllNewsCount(Context context) {
        count = 0;
        count = allNewsCursor(context).getCount();
        return count;
    }

    public int getFavNewsCount(Context context) {
        count = 0;
        count = favoriteNewsCursor(context).getCount();
        return count;
    }

    public int insertData(Context context, ArrayList<Article> article, String storeF) {
        c = null;
        int flag = 0;
        ContentProviderOperation.Builder builder = null;
        try {
            ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(article.size());
            if (storeF.equals(FAVORITE)) {
                c = context.getContentResolver().query(NewsProvider.NewsFavourite.CONTENT_URI_FAVOURITE,
                        null, null, null, null);
            } else {

                c = context.getContentResolver().query(NewsProvider.MyNews.CONTENT_URI,
                        null, null, null, null);
            }


            for (Article news : article) {


                c.moveToFirst();
                while (c.isAfterLast() == false) {
                    if (news.getTitle().equalsIgnoreCase(c.getString(c.getColumnIndex(ColumnsNews.TITLE)))) {
                        // Log.d("POPULAR MOVIES","Movie Title Same"+movie.getTitle().toString()+" ="+c.getString(c.getColumnIndex(ColumnsMovies.TITLE)));
                        flag = 0;
                        break;
                    } else {
                        flag = 1;
                        // Log.d("POPULAR MOVIES","Movie Title Difference"+movie.getTitle().toString()+" ="+c.getString(c.getColumnIndex(ColumnsMovies.TITLE)));

                    }
                    c.moveToNext();
                }


                if (flag == 1 || c.getCount() == 0) {
                    if (news.getTitle() != null && news.getDescription() != null && storeF.equals(FAVORITE)) {
                        builder = ContentProviderOperation.newInsert(
                                NewsProvider.NewsFavourite.CONTENT_URI_FAVOURITE);
                    } else if (news.getTitle() != null && news.getDescription() != null) {
                        builder = ContentProviderOperation.newInsert(
                                NewsProvider.MyNews.CONTENT_URI);
                    }
                    if (builder != null) {
                        builder.withValue(ColumnsNews.TITLE, news.getTitle());
                        builder.withValue(ColumnsNews.AUTHOR, (news.getAuthor() == null) ? "" : news.getAuthor());
                        builder.withValue(ColumnsNews.DESCRIPTION, news.getDescription());
                        builder.withValue(ColumnsNews.URL, (news.getUrl() == null) ? "" : news.getUrl());
                        builder.withValue(ColumnsNews.URL_TO_IMAGE, (news.getUrlToImage() == null) ? "" : news.getUrlToImage());
                        builder.withValue(ColumnsNews.PUBLISHED_AT, (news.getPublishedAt() == null) ? "" : news.getPublishedAt());
                        batchOperations.add(builder.build());
                    }

                }
            }
            context.getContentResolver().applyBatch(NewsProvider.AUTHORITY, batchOperations);
        } catch (Exception e) {
            //Log.e("News24X7", "Error applying batch insert", e);

        }
        try {

            if (c != null || !c.isClosed()) {
                c.close();
            }
        } catch (Exception e) {
        }

        if (flag == 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
