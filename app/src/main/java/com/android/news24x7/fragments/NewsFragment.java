package com.android.news24x7.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.news24x7.BuildConfig;
import com.android.news24x7.R;
import com.android.news24x7.adapter.NewsRecyclerViewAdapter;
import com.android.news24x7.parcelable.Article;
import com.android.news24x7.retrofit.ApiClient;
import com.android.news24x7.retrofit.ApiInterface;
import com.android.news24x7.retrofit.NewsResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class NewsFragment extends Fragment {

    private ArrayList<Article> articlesList;

    String tab;
    String[] web;
    String[] datesd;
    int[] imageId;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView gridView;
    public NewsFragment() {
        // Required empty public constructor
    }

    public NewsFragment(String s) {
        tab=s;
    }
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        if (param1.equalsIgnoreCase("ENGLISH")) {
            //THIS IS INSTANCE OF ENGLISH TYPE DATA
        } else if (param1.equalsIgnoreCase("SCIENCE")) {
            //THIS IS INSTANCE OF SCIENCE TYPE DATA
        }else if (param1.equalsIgnoreCase("SOCIAL")) {
            //THIS IS INSTANCE OF SOCIAL TYPE DATA
        }
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tab = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_news, container, false);

         gridView=(ListView)v.findViewById(R.id.list);
        switch (tab){
            case "HEADLINES":
               fetchNews("",2);
                Toast.makeText(getContext(), "Tab My" +articlesList, Toast.LENGTH_SHORT).show();

            default:  Toast.makeText(getContext(), "Tab My" + tab, Toast.LENGTH_SHORT).show();

        }





        return v;
    }

    private void setUpAdapter(ArrayList<Article> articlesList) {
        NewsRecyclerViewAdapter gridAdapter = new NewsRecyclerViewAdapter(getActivity(), R.layout.news_list,articlesList);
        gridView.setAdapter(gridAdapter);
    }

    private void fetchNews(String type, int page) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> data = new HashMap<>();
        data.put("source","the-times-of-india");
       // data.put("sortBy","latest");
        data.put("apiKey", BuildConfig.NEWS_API_ORG_KEY);
        Call<NewsResponse> call = null;

            call = apiService.getNews(data);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                articlesList = (ArrayList<Article>) response.body().getArticles();
                setUpAdapter(articlesList);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                Log.d(TAG, "server contacted at: " + call.request().url());

            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
