package com.android.news24x7.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.news24x7.fragments.NewsFragment;
import com.android.news24x7.R;

/**
 * Created by Dell on 6/2/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int TAB_COUNT=5;
    Context mContext;
    public ViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        mContext=context;
    }

    @Override
    public Fragment getItem(int position) {

             return NewsFragment.newInstance("" +getPageTitle(position), "data");

    }

    @Override
    public CharSequence getPageTitle(int position) {

        return  mContext.getResources().getStringArray(R.array.tabs)[position];
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}