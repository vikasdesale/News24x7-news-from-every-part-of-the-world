package com.android.news24x7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.news24x7.R;
import com.android.news24x7.adapter.ViewPagerAdapter;
import com.android.news24x7.fragments.NewsFragment;
import com.android.news24x7.util.NewsSyncUtils;
import com.android.news24x7.util.Util;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class HomeActivity extends AppCompatActivity implements MaterialTabListener, ViewPager.OnPageChangeListener, NewsFragment.CallbackDetails {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBar ab;
    //private ViewPager viewPager;
    private NavigationView navigationView;
    //private TabLayout tabLayout;
    //private ViewPagerAdapter viewPagerAdapter;
    private MaterialTabHost mTabHost;
    private ViewPager mPager;
    private ViewPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupFind();
        setUpTab();
        NewsSyncUtils.initialize(this);

    }

    private void setUpTab() {

            mTabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
            mPager = (ViewPager) findViewById(R.id.viewPager);
            mAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
            mPager.setAdapter(mAdapter);
            //when the page changes in the ViewPager, update the Tabs accordingly
            mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    mTabHost.setSelectedNavigationItem(position);
                    mAdapter.notifyDataSetChanged();
                }
            });
            //Add all the Tabs to the TabHost
            for (int i = 0; i < mAdapter.getCount(); i++) {
                mTabHost.addTab(
                        mTabHost.newTab()
                                .setText(mAdapter.getPageTitle(i))
                                .setTabListener(this));
            }
    }

    private void setupFind() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            Util.setupDrawerContent(navigationView, mDrawerLayout, this);
        }
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onTabSelected(MaterialTab materialTab) {
        //when a Tab is selected, update the ViewPager to reflect the changes
        mPager.setCurrentItem(materialTab.getPosition());


    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {
    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
       mTabHost.setSelectedNavigationItem(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemSelected(String mTitle, String mAuthor, String mDescription, String mUrl, String mUrlToImage, String mPublishedAt) {
        //Log.d("GREAT TO SEE","My News:"+ mTitle+mAuthor+mDescription+mUrl+mUrlToImage+mPublishedAt);
        //Toast.makeText(this,"Clicked"+mTitle,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("title", mTitle);
        intent.putExtra("author", mAuthor);
        intent.putExtra("overview", mDescription);
        intent.putExtra("url", mUrl);
        intent.putExtra("url_image", mUrlToImage);
        intent.putExtra("published", mPublishedAt);
        startActivity(intent);
    }

}
