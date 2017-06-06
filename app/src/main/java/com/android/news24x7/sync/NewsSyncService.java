package com.android.news24x7.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NewsSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static NewsSyncAdapter sSunshineSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("NewsSyncService", "onCreate - NewsSyncService");
        synchronized (sSyncAdapterLock) {
            if (sSunshineSyncAdapter == null) {
                sSunshineSyncAdapter = new NewsSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSunshineSyncAdapter.getSyncAdapterBinder();
    }
}