package com.android.news24x7.retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Dell on 6/3/2017.
 */

public interface ApiInterface {


    @GET("articles")
    Call<NewsResponse> getNews(@QueryMap Map<String, String> options);

}
