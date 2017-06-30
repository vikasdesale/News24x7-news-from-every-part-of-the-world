package com.news.news24x7.interfaces;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Dell on 6/4/2017.
 */
public interface ColumnsNews {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String AUTHOR = "author";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String TITLE = "title";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String DESCRIPTION = "description";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String URL = "url";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String URL_TO_IMAGE = "urlToImage";
    @DataType(DataType.Type.TEXT)
    @NotNull
    String PUBLISHED_AT = "publishedAt";


}

