package com.news.news24x7.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.MenuItem;

import com.news.news24x7.R;
import com.news.news24x7.activities.SettingsActivity;
import com.news.news24x7.fragments.NewsFragment;
import com.google.android.gms.appinvite.AppInviteInvitation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dell on 6/2/2017.
 */
public class Util {

    public static final String BUSINESS = "Business";
    public static final String TITLE = "title";
    public static final String ENTERTAINMENT = "Entertainment";
    public static final String MUSIC = "Music";
    public static final String SPORT = "Sport";
    public static final String SCIENCE = "Science-and-nature";
    public static final String POLITICS = "Politics";
    public static final String TECHNOLOGY = "Technology";
    public static final String HOME = "top";
    private static final int REQUEST_INVITE = 23;


    public static void setupDrawerContent(NavigationView navigationView, final DrawerLayout mDrawerLayout, final Context context) {
        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        Bundle args = new Bundle();
                        Fragment fragment = null;
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home: //do what you want to do;
                                args = new Bundle();
                                args.putString(TITLE, HOME);
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_business: //do what you want to do;
                                args = new Bundle();
                                args.putString(TITLE, BUSINESS);
                                fragment = new NewsFragment();
                                fragment.setArguments(args);

                                break;
                            case R.id.nav_entertainment: //do what you want to do;
                                args.putString(TITLE, ENTERTAINMENT);
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_music: //do what you want to do;
                                args = new Bundle();
                                args.putString(TITLE, MUSIC);
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_politics: //do what you want to do;
                                args = new Bundle();
                                args.putString(TITLE, POLITICS);
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_sport: //do what you want to do;
                                args = new Bundle();
                                args.putString(TITLE, SPORT);
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_science_and_nature: //do what you want to do;
                                args = new Bundle();
                                args.putString(TITLE, SCIENCE);
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_technology: //do what you want to do;
                                args = new Bundle();
                                args.putString(TITLE, TECHNOLOGY);
                                fragment = new NewsFragment();
                                fragment.setArguments(args);
                                break;
                            case R.id.nav_settings: //do what you want to do;
                                context.startActivity(new Intent(context, SettingsActivity.class));
                                break;
                            case R.id.invite_friends: // etc,
                                inviteFriends(((AppCompatActivity) context), REQUEST_INVITE);

                                break;
                        }
                        //replacing the fragment
                        if (fragment != null) {
                            FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container, fragment);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    public static void inviteFriends(Activity activity, int requestCode) {
        Intent intent = new AppInviteInvitation.IntentBuilder(activity.getString(R.string.invitation_title))
                .setMessage(activity.getString(R.string.detail_share))
                .setDeepLink(Uri.parse("https://w524x.app.goo.gl/WMNf"))
                .setCustomImage(Uri.parse("android.resource://" + activity.getPackageName() + "/mipmap/ic_launcher"))
                .setCallToActionText(activity.getString(R.string.invitation_cta))
                .build();
        activity.startActivityForResult(intent, requestCode);
    }

    // convert date to format as 5 hours ago
    public static String manipulateDateFormat(String post_date) {

        if (post_date == null)
            return "";       //if no date is returned by the API then set corresponding date view to empty

        if (post_date.equals("0001-01-01T00:00:00Z")) //because Times of India is always returning this in time stamp which is Jan1,1 (wrong information they are sending)
            return "";

        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date date = null;
        if(!post_date.equals("")) {
            try {
                date = existingUTCFormat.parse(post_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (date != null) {
            // Converting timestamp into x ago format
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(String.valueOf(date.getTime())),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            return timeAgo + "";
        } else {
            return post_date;
        }
    }
}
