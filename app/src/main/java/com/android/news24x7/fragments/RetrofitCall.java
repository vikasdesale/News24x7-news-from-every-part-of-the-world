package com.android.news24x7.fragments;

import android.content.Context;
import android.util.Log;

import com.android.news24x7.BuildConfig;
import com.android.news24x7.parcelable.Article;
import com.android.news24x7.retrofit.ApiClient;
import com.android.news24x7.retrofit.ApiInterface;
import com.android.news24x7.retrofit.NewsResponse;
import com.android.news24x7.util.NewsUtil;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

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

    public void fetchNews(final Context context, Map<String, String> data) {
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

    public interface RetrofitCallback {
        public void onRetrofitCall(int articlesList);
    }

}
