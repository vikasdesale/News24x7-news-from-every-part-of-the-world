package com.android.news24x7.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.news24x7.R;
import com.android.news24x7.adapter.NewsRecyclerViewAdapter;
import com.android.news24x7.fragments.NewsFragment;
import com.android.news24x7.util.NewsSyncUtils;
import com.android.news24x7.util.Util;
import com.android.news24x7.widget.NewsWidgetProvider;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends AppCompatActivity implements NewsFragment.CallbackDetails {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private ActionBar ab;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Declare a new thread to do a preference check. Library to invoke Intro slides
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(HomeActivity.this, IntroActivity.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
                else {
                    //  Launch Mainactivity
                //    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                  //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                   // startActivity(intent);
                    // mActivity=true;

                }
            }
        });
        // Start the thread
        t.start();
        if (Build.VERSION.SDK_INT >= 21) {

            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.transition.details_window_enter_transition);
            getWindow().setExitTransition(transition);
        }
        // setUpWindowAnimations();
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
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
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
    public void onItemSelected(String mTitle, String mAuthor, String mDescription, String mUrl, String mUrlToImage, String mPublishedAt, NewsRecyclerViewAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, DetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putString(NewsWidgetProvider.EXTRA_TITLE, mTitle);
        extras.putString(NewsWidgetProvider.EXTRA_AUTHOR, mAuthor);
        extras.putString(NewsWidgetProvider.EXTRA_DESCRIPTION, mDescription);
        extras.putString(NewsWidgetProvider.EXTRA_IMAGE_URL, mUrlToImage);
        extras.putString(NewsWidgetProvider.EXTRA_URL, mUrl);
        extras.putString(NewsWidgetProvider.EXTRA_DATE, mPublishedAt);
        intent.putExtras(extras);
        ActivityOptionsCompat activityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,new Pair<View, String>(vh.imageView, getString(R.string.detail_icon_transition_name)));

        ActivityCompat.startActivity(this, intent, activityOptions.toBundle());    }

}
