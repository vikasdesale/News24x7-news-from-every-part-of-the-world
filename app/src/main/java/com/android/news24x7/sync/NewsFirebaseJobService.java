package com.android.news24x7.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Dell on 6/6/2017.
 */

public class NewsFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mNewsFetchTask;


    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mNewsFetchTask = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                NewsSyncTask.syncNews(context);
                jobFinished(jobParameters, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobParameters, false);
            }
        };

        mNewsFetchTask.execute();
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mNewsFetchTask != null) {
            mNewsFetchTask.cancel(true);
        }
        return true;
    }
}
