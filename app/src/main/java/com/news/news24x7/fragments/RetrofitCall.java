package com.news.news24x7.fragments;

import android.content.Context;

import com.news.news24x7.BuildConfig;
import com.news.news24x7.parcelable.Article;
import com.news.news24x7.retrofit.ApiClient;
import com.news.news24x7.retrofit.ApiInterface;
import com.news.news24x7.retrofit.NewsResponse;
import com.news.news24x7.util.NewsUtil;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dell on 6/9/2017.
 */

public class RetrofitCall {
    public static final String APIKEY = "apiKey";
    public static final String NO_SAVE = "no";
    public static RetrofitCallback callBack;
    static ArrayList<Article> articlesList;

    public static void onRetrofit(RetrofitCallback call) {

        callBack = call;
    }

 /*   public void fetchNewsds(final Context context, Map<String, String> data) {
        final NewsUtil mNewsUtil = new NewsUtil();
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
                    mNewsUtil.insertData(context, articlesList, NO_SAVE);
                }

                callBack.onRetrofitCall(0);

            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(TAG, "Error" + t.toString());
                int i = 1;
                callBack.onRetrofitCall(i);
            }
        });
    }
*/
    public void fetchNews2(final Context context, Map<String, String> data) {
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
                callBack.onRetrofitCall(0);

            }

            @Override
            public void onError(Throwable throwable) {
                int i = 1;
                callBack.onRetrofitCall(i);
            }

            @Override
            public void onComplete() {

            }
        });



    }

    public interface RetrofitCallback {
        public void onRetrofitCall(int articlesList);
    }

}
