package com.android.news24x7.database;

import android.net.Uri;

import com.android.news24x7.interfaces.ColumnsNews;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Dell on 6/4/2017.
 */

@ContentProvider(authority = NewsProvider.AUTHORITY, database = NewsDatabase.class)
public class NewsProvider {

    public static final String AUTHORITY = "com.android.news24x7";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    interface Path {
        String MY_NEWS = "myNews";
        String FAVOURITE_NEWS = "myFavouriteNews";
    }

    @TableEndpoint(table = NewsDatabase.MY_NEWS)
    public static class MyNews {
        @ContentUri(
                path = Path.MY_NEWS,
                type = "vnd.android.cursor.dir/myNews",
                defaultSort = ColumnsNews._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.MY_NEWS);

    }

    @TableEndpoint(table = NewsDatabase.FAVOURITE_NEWS)
    public static class NewsFavourite {
        @ContentUri(
                path = Path.FAVOURITE_NEWS,
                type = "vnd.android.cursor.dir/myFavouriteNews",
                defaultSort = ColumnsNews._ID + " ASC")
        public static final Uri CONTENT_URI_FAVOURITE = buildUri(Path.FAVOURITE_NEWS);

    }
}
