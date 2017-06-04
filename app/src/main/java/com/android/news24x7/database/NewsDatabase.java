package com.android.news24x7.database;

import com.android.news24x7.interfaces.ColumnsNews;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Dell on 6/4/2017.
 */

@Database(version = NewsDatabase.VERSION)
public class NewsDatabase {
    public static final int VERSION = 1;

    //temporary table
    @Table(ColumnsNews.class)
    public static final String MY_NEWS = "myNews";

    //permanent table
    @Table(ColumnsNews.class)
    public static final String FAVOURITE_NEWS = "myFavouriteNews";


    private NewsDatabase() {
    }
}