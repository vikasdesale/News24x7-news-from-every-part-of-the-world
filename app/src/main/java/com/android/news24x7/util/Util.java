package com.android.news24x7.util;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.android.news24x7.R;
import com.android.news24x7.activities.SettingsActivity;

/**
 * Created by Dell on 6/2/2017.
 */
public class Util {

    public static void setupDrawerContent(NavigationView navigationView, final DrawerLayout mDrawerLayout, final Context context) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home: //do what you want to do;
                                break;
                            case R.id.nav_channel: //do what you want to do;
                                break;
                            case R.id.nav_saved: //do what you want to do;
                                break;
                            case R.id.nav_settings: //do what you want to do;
                                context.startActivity(new Intent(context, SettingsActivity.class));
                                // fragment = AboutFragment.newInstance("About", "arg2");
                                // Snackbar.make(rootLayout, "HU AM I?!", Snackbar.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_help: // etc,
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


}
