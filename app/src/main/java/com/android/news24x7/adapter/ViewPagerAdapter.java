package com.android.news24x7.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.news24x7.R;
import com.android.news24x7.fragments.NewsFragment;
import com.android.news24x7.fragments.NewsFragment2;
import com.android.news24x7.fragments.NewsFragment3;

/**
 * Created by Dell on 6/2/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final int ENTERTAINMENT= 4;
    private static final int SPORT = 3;
    private static final int HIGHLIGHT = 2;
    private static final int TRENDING = 1;
    private static final int HEADLINES = 0;
    int TAB_COUNT=5;
    Context mContext;
    public ViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        mContext=context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment=null;
            switch(position){
            case HEADLINES:
                fragment= NewsFragment.newInstance( String.valueOf(getPageTitle(position)), "");
                break;
            case TRENDING:
                fragment= NewsFragment3.newInstance( String.valueOf(getPageTitle(position)), "");
                break;
            case HIGHLIGHT:
                fragment= NewsFragment.newInstance( String.valueOf(getPageTitle(position)), "");
                break;
            case SPORT:
                fragment= NewsFragment2.newInstance( String.valueOf(getPageTitle(position)), "");

                break;
            case ENTERTAINMENT:
                fragment= NewsFragment.newInstance( String.valueOf(getPageTitle(position)), "");

                break;


        }

            return fragment;



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