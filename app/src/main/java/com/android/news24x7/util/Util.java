package com.android.news24x7.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.news24x7.R;
import com.android.news24x7.activities.SettingsActivity;
import com.android.news24x7.fragments.NewsFragment;

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
                        Bundle args = new Bundle();
                        Fragment fragment=null;
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home: //do what you want to do;
                                break;
                            case R.id.nav_business: //do what you want to do;
                                args = new Bundle();
                                args.putString("title","Business");
                                fragment = new NewsFragment();
                                fragment.setArguments(args);

                                break;
                            case R.id.nav_entertainment: //do what you want to do;
                                args.putString("title","Entertainment");
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_music: //do what you want to do;
                                args = new Bundle();
                                args.putString("title","Music");
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_politics: //do what you want to do;
                                args = new Bundle();
                                args.putString("title","Politics");
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_sport: //do what you want to do;
                                args = new Bundle();
                                args.putString("title","Sport");
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_science_and_nature: //do what you want to do;
                                args = new Bundle();
                                args.putString("title","Science-and-nature");
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_technology: //do what you want to do;
                                args = new Bundle();
                                args.putString("title","Technology");
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_settings: //do what you want to do;
                                context.startActivity(new Intent(context, SettingsActivity.class));
                                // fragment = AboutFragment.newInstance("About", "arg2");
                                // Snackbar.make(rootLayout, "HU AM I?!", Snackbar.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_help: // etc,
                                break;
                        }
                        //replacing the fragment
                        if (fragment != null) {
                            FragmentTransaction ft = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container, fragment);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


}
