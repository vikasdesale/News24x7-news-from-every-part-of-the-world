package com.android.news24x7.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Dell on 6/6/2017.
 */

public class NewsSyncIntentService extends IntentService {

    public NewsSyncIntentService() {
        super("NewsSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NewsSyncTask.syncNews(this);
    }
}