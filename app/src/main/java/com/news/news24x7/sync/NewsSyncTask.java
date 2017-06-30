package com.news.news24x7.sync;

/**
 * Created by Dell on 6/6/2017.
 */

import android.content.Context;
import android.util.Log;

import com.news.news24x7.BuildConfig;
import com.news.news24x7.parcelable.Article;
import com.news.news24x7.prefs.NewsPreferences;
import com.news.news24x7.retrofit.ApiClient;
import com.news.news24x7.retrofit.ApiInterface;
import com.news.news24x7.retrofit.NewsResponse;
import com.news.news24x7.util.NewsUtil;
import com.news.news24x7.util.NotificationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.news.news24x7.fragments.NewsFragment.LATEST;
import static com.news.news24x7.fragments.NewsFragment.SORTBY;
import static com.news.news24x7.fragments.NewsFragment.SOURCE;
import static com.news.news24x7.fragments.RetrofitCall.APIKEY;
import static com.news.news24x7.fragments.RetrofitCall.NO_SAVE;

public class NewsSyncTask {

    static NewsUtil mNewsUtil;
    static ArrayList<Article> articlesList;

    synchronized public static void syncNews(final Context context) {
        mNewsUtil = new NewsUtil();
        Map<String, String> data = new HashMap<>();
        data.put(SOURCE, "the-hindu");
        data.put(SORTBY, LATEST);
        fetchData(context, data);

        boolean notificationsEnabled = NewsPreferences.areNotificationsEnabled(context);

        if (notificationsEnabled) {
            NotificationUtils.notifyUserOfNewWeather(context);
        }

    }

    public static void fetchData(final Context context, Map<String, String> data) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        data.put(APIKEY, BuildConfig.NEWS_API_ORG_KEY);
        Call<NewsResponse> call = null;
        call = apiService.getNews(data);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    articlesList = (ArrayList<Article>) response.body().getArticles();
                }

                mNewsUtil.insertData(context, articlesList, NO_SAVE);

            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(TAG, t.toString());

            }
        });


    }

}