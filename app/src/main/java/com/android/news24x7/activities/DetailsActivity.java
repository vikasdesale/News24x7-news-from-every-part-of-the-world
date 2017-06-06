package com.android.news24x7.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.news24x7.R;
import com.android.news24x7.fragments.DetailsFragment;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailsFragment())
                    .commit();
        }
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(null);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_view, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}