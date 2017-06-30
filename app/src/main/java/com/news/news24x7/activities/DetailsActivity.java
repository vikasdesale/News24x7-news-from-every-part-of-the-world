package com.news.news24x7.activities;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.MenuItem;

import com.news.news24x7.R;
import com.news.news24x7.fragments.DetailsFragment;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {

            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.transition.details_window_return_transition);
            getWindow().setExitTransition(transition);
        }
        // setUpWindowAnimations();
        setContentView(R.layout.activity_article_detail);
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle extras = getIntent().getExtras();

            DetailsFragment fragment = new DetailsFragment();
             extras.putBoolean(DetailsFragment.DETAIL_TRANSITION_ANIMATION, true);

            fragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();


            // Being here means we are in animation mode
            supportPostponeEnterTransition();
        }
    }

    private void setUpWindowAnimations() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Slide slide = new Slide(Gravity.START);
            slide.setDuration(3000);
            getWindow().setEnterTransition(slide);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
