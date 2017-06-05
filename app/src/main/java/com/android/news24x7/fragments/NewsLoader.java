package com.android.news24x7.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.GridView;
import com.android.news24x7.adapter.NewsRecyclerViewAdapter;
import com.android.news24x7.database.NewsProvider;


/**
 * Created by Dell on 1/28/2017.
 */

public class NewsLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int NEWS_LOADER = 0;
    private static final String SELECTED_KEY = "selected_position";
    private static int favflag = -1;
    private Fragment mAttachedFragment;
    private NewsRecyclerViewAdapter gridAdapter;


    private NewsLoader() {
    }

    public static NewsLoader newInstance(int flag, @NonNull Fragment fragment, @NonNull NewsRecyclerViewAdapter adapter) {
        NewsLoader element = new NewsLoader();
        element.mAttachedFragment = fragment;
        element.gridAdapter = adapter;
        element.favflag = flag;
        return element;
    }

    public void initLoader() {
        if (mAttachedFragment != null)
            mAttachedFragment.getLoaderManager().initLoader(NEWS_LOADER, null, this);
    }

    public void restartLoader() {
        if (mAttachedFragment != null)
            mAttachedFragment.getLoaderManager().restartLoader(NEWS_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //checking on rotate
        if (mAttachedFragment != null && mAttachedFragment.getActivity() != null) {


            if (favflag == 0) {
                return new CursorLoader(mAttachedFragment.getActivity(), NewsProvider.MyNews.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            } else {
                if (favflag == 1) {
                    return new CursorLoader(mAttachedFragment.getActivity(), NewsProvider.NewsFavourite.CONTENT_URI_FAVOURITE,
                            null,
                            null,
                            null,
                            null);

                }
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (gridAdapter != null) {
            gridAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        gridAdapter.swapCursor(null);
    }

}
