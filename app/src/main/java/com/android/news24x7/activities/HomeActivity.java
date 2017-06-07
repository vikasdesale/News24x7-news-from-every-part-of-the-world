package com.android.news24x7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.news24x7.R;
import com.android.news24x7.util.Util;
import com.android.news24x7.adapter.ViewPagerAdapter;
import com.android.news24x7.fragments.NewsFragment;
import com.android.news24x7.util.NewsSyncUtils;


public class HomeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener, NewsFragment.CallbackDetails {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBar ab;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupFind();
        setUpTab();
//        NewsSyncAdapter.initializeSyncAdapter(this);
        NewsSyncUtils.initialize(this);

    }

    private void setUpTab() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
            tabLayout.addTab(
                    tabLayout.newTab()
                            .setText(viewPagerAdapter.getPageTitle(i)));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);
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
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (navigationView != null) {
            Util.setupDrawerContent(navigationView, mDrawerLayout,this);
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
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        TabLayout.Tab tab = tabLayout.getTabAt(position);
        if (tab != null) {
            tab.select();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemSelected( String mTitle,String mAuthor, String mDescription, String mUrl, String mUrlToImage, String mPublishedAt) {
        //Log.d("GREAT TO SEE","My News:"+ mTitle+mAuthor+mDescription+mUrl+mUrlToImage+mPublishedAt);
        //Toast.makeText(this,"Clicked"+mTitle,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("title", mTitle);
        intent.putExtra("author", mAuthor);
        intent.putExtra("overview",mDescription);
        intent.putExtra("url", mUrl);
        intent.putExtra("url_image", mUrlToImage);
        intent.putExtra("published", mPublishedAt);
        startActivity(intent);
    }

}
