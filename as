package com.android.news24x7;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Dell on 6/2/2017.
 */
public class MyNavigation {

    public static void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch(menuItem.getItemId()){
                            case R.id.nav_home : //do what you want to do;
                                break;
                            case R.id.nav_channel : //do what you want to do;
                                Toast.makeText(HomeActivity.this,"Channel",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.nav_saved : //do what you want to do;
                                break;
                            case R.id.nav_settings : //do what you want to do;
                                break;
                            case R.id.nav_help : // etc,
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    public static void replaceFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.viewpager, fragment);
        transaction.commit();
    }
}
