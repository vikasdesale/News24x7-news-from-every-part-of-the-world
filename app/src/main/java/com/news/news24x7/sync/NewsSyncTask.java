package com.news.news24x7.sync;

/**
 * Created by Dell on 6/6/2017.
 */

import android.content.Context;

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

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
        fetchNews2(context, data);

        boolean notificationsEnabled = NewsPreferences.areNotificationsEnabled(context);

        if (notificationsEnabled) {
            NotificationUtils.notifyUserOfNewWeather(context);
        }

    }
    public static void fetchNews2(final Context context, Map<String, String> data) {
        final NewsUtil mNewsUtil = new NewsUtil();
        ApiInterface apiService =
                ApiClient.getClient2().create(ApiInterface.class);

        data.put(APIKEY, BuildConfig.NEWS_API_ORG_KEY);
        Observable<NewsResponse> news= apiService.getNews2(data);

        news.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsResponse>(){

                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(NewsResponse newsResponse) {
                        articlesList = (ArrayList<Article>) newsResponse.getArticles();
                        mNewsUtil.insertData(context, articlesList, NO_SAVE);

                    }

                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }



}