package com.android.news24x7.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class NewsAuthenticatorService extends Service {
    private NewsAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new NewsAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
