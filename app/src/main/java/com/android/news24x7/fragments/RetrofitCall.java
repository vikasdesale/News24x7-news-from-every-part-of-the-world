package com.android.news24x7.fragments;

import android.content.Context;
import android.util.Log;

import com.android.news24x7.BuildConfig;
import com.android.news24x7.parcelable.Article;
import com.android.news24x7.retrofit.ApiClient;
import com.android.news24x7.retrofit.ApiInterface;
import com.android.news24x7.retrofit.NewsResponse;
import com.android.news24x7.util.NewsUtil;

import java.net.HttpURLConnection;
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
    static ArrayList<Article> articlesList;
    public static RetrofitCallback callBack;
    public interface RetrofitCallback {
        public void  onRetrofitCall();
    }
    public static void onRetrofit(RetrofitCallback call) {

        callBack = call;
    }
    public void fetchNews(final Context context, Map<String, String> data) {
        final NewsUtil mNewsUtil=new NewsUtil();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        data.put("apiKey", BuildConfig.NEWS_API_ORG_KEY);
        Call<NewsResponse> call = null;
        call = apiService.getNews(data);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if(response.isSuccessful()) {
                    articlesList = (ArrayList<Article>) response.body().getArticles();
                    mNewsUtil.insertData(context, articlesList, "no");
                    Log.e("vikas Fragment 2:",""+articlesList.size());
                }
                else {

                    int errorCode = response.code();

                    switch (errorCode) {
                        case HttpURLConnection.HTTP_OK:
                            break;
                        case HttpURLConnection.HTTP_NOT_FOUND:
                            //setNewsStatus(getContext(), NEWS_STATUS_INVALID);
                            return;
                        default:
                            //setNewsStatus(getContext(), NEWS_STATUS_SERVER_DOWN);
                            return;
                    }
                }
                callBack.onRetrofitCall();

            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(TAG,"Error"+t.toString());

            }
        });
    }

}
