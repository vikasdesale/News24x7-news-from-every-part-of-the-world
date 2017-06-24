package com.android.news24x7.activities;

/**
 * Created by Dell on 6/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.android.news24x7.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

@SuppressWarnings("ALL")
public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDepthAnimation();

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance(getString(R.string.first_screen), getString(R.string.scr1_desc), R.drawable.screen2, ContextCompat.getColor(getApplicationContext(), R.color.primary)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.second_screen), getString(R.string.scr2_desc), R.drawable.screen3, ContextCompat.getColor(getApplicationContext(), R.color.accent)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.third_screen), getString(R.string.scr3_desc), R.drawable.screen4a, ContextCompat.getColor(getApplicationContext(), R.color.primary)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.fourth_screen), getString(R.string.scr4_desc), R.drawable.screen5, ContextCompat.getColor(getApplicationContext(), R.color.accent)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.fifth_screen), getString(R.string.scr5_desc), R.drawable.screen6, ContextCompat.getColor(getApplicationContext(), R.color.primary)));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);
        // SHOW or HIDE the statusbar
        showStatusBar(true);
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        finish();
    }

}