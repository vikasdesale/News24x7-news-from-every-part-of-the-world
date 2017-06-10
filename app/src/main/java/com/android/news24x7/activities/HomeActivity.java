package com.android.news24x7.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;

import com.android.news24x7.R;
import com.android.news24x7.fragments.NewsFragment;
import com.android.news24x7.util.NewsSyncUtils;
import com.android.news24x7.util.Util;
import com.android.news24x7.widget.NewsWidgetProvider;
import com.google.firebase.analytics.FirebaseAnalytics;


public class HomeActivity extends AppCompatActivity implements NewsFragment.CallbackDetails {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBar ab;
    private NavigationView navigationView;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {

            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.transition.details_window_enter_transition);
            getWindow().setExitTransition(transition);
        }
       // setUpWindowAnimations();
        setContentView(R.layout.activity_home);
        setupFind();

             // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //Sets whether analytics collection is enabled for this app on this device.
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        if (savedInstanceState == null) {

            NewsFragment fragment = new NewsFragment();
            fragment.setArguments(null);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, "")
                    .commit();
        }
        NewsSyncUtils.initialize(this);

    }

    private void setupFind() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
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

    private void setUpWindowAnimations() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Fade fade = new Fade(2);
            fade.setDuration(3000);
            getWindow().setExitTransition(fade);
        }
    }
    @Override
    public void onItemSelected(String mTitle, String mAuthor, String mDescription, String mUrl, String mUrlToImage, String mPublishedAt) {
        //Log.d("GREAT TO SEE","My News:"+ mTitle+mAuthor+mDescription+mUrl+mUrlToImage+mPublishedAt);
        //Toast.makeText(this,"Clicked"+mTitle,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putString(NewsWidgetProvider.EXTRA_TITLE, mTitle);
        extras.putString(NewsWidgetProvider.EXTRA_AUTHOR, mAuthor);
        extras.putString(NewsWidgetProvider.EXTRA_DESCRIPTION, mDescription);
        extras.putString(NewsWidgetProvider.EXTRA_IMAGE_URL, mUrlToImage);
        extras.putString(NewsWidgetProvider.EXTRA_URL, mUrl);
        extras.putString(NewsWidgetProvider.EXTRA_DATE, mPublishedAt);
        intent.putExtras(extras);
       startActivity(intent);
    }

}
