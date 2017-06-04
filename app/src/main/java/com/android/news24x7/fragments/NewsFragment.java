package com.android.news24x7.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.news24x7.BuildConfig;
import com.android.news24x7.R;
import com.android.news24x7.adapter.NewsRecyclerViewAdapter;
import com.android.news24x7.interfaces.ScrollViewExt;
import com.android.news24x7.interfaces.ScrollViewListener;
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


public class NewsFragment extends Fragment implements ScrollViewListener {

    private ArrayList<Article> articlesList;

    String tab;
    String[] web;
    String[] datesd;
    int[] imageId;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
     String source[]={"the-times-of-india","the-hindu","usa-today","time","mtv-news"};
    private String mParam1;
    private String mParam2;
    RecyclerView mRecyclerView;
    int i=0;
    private ScrollViewExt scroll;
    Map<String, String> data = new HashMap<>();

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

         mRecyclerView=(RecyclerView) v.findViewById(R.id.card_recycler_view);
        scroll = (ScrollViewExt) v.findViewById(R.id.scroll);
        scroll.setScrollViewListener(this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        switch (tab) {
            case "HEADLINES":
                data.clear();
                data.put("source", ""+source[i++]);
                data.put("sortBy", "latest");
                    fetchNews();

                Toast.makeText(getContext(), "Tab My" +articlesList, Toast.LENGTH_SHORT).show();

            default:  Toast.makeText(getContext(), "Tab My" + tab, Toast.LENGTH_SHORT).show();

        }





        return v;
    }

    private void setUpAdapter(ArrayList<Article> articlesList) {
        NewsRecyclerViewAdapter gridAdapter = new NewsRecyclerViewAdapter(getActivity(), R.layout.news_list,articlesList);
        mRecyclerView.setAdapter(gridAdapter);
    }

    private void fetchNews() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        data.put("apiKey", BuildConfig.NEWS_API_ORG_KEY);
        Call<NewsResponse> call = null;
            call = apiService.getNews(data);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                ArrayList<Article> articleOld = null;

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


    @Override
    public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
        // if diff is zero, then the bottom has been reached
        if (diff == 0 ) {
            while (i < source.length) {
                data.clear();
                data.put("source", "" + source[i++]);
                data.put("sortBy", "latest");
                fetchNews();
            }
        }
    }
}
