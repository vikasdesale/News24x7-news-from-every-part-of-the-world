package com.android.news24x7.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.news24x7.BuildConfig;
import com.android.news24x7.R;
import com.android.news24x7.adapter.NewsRecyclerViewAdapter;
import com.android.news24x7.database.NewsUtil;
import com.android.news24x7.interfaces.ScrollViewExt;
import com.android.news24x7.interfaces.ScrollViewListener;
import com.android.news24x7.parcelable.Article;
import com.android.news24x7.retrofit.ApiClient;
import com.android.news24x7.retrofit.ApiInterface;
import com.android.news24x7.retrofit.NewsResponse;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.android.news24x7.database.NewsUtil.CacheDelete;


public class NewsFragment extends Fragment implements NewsRecyclerViewAdapter.ClickListener,ScrollViewListener {


    String tab;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String source[]={"thetimes-of-india","thehindu","usatoday","time","mtv-news"};
    private String mParam1;
    private String mParam2;
    private static int favflag = 2;
    RecyclerView mRecyclerView;
    int i=0;
    private ScrollViewExt scroll;
    Map<String, String> data = new HashMap<>();
    NewsUtil mNewsUtil;
    private ArrayList<Article> articlesList;
    private NewsLoader mNewsLoader;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";


    public NewsFragment() {
        // Required empty public constructor
    }
    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface CallbackDetails {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */

        public void onItemSelected(String mTitle,String mAuthor,String mDescription,String mUrl,String mUrlToImage,String mPublishedAt);
    }

    public NewsFragment(String s) {
        tab=s;
    }
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mNewsUtil = new NewsUtil();
        setRetainInstance(true);
        setHasOptionsMenu(true);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());;
        mRecyclerView.setLayoutManager(layoutManager);
        switch (tab) {
            case "HEADLINES":
                NewsCheck();
                if(i==source.length){
                    i=0;
                }
                data.clear();
                data.put("source", ""+source[i++]);
                data.put("sortBy", "latest");
                    fetchNews(data);

                Toast.makeText(getContext(), "Tab My" +articlesList, Toast.LENGTH_SHORT).show();

            default:  Toast.makeText(getContext(), "Tab My" + tab, Toast.LENGTH_SHORT).show();

        }

        return v;
    }
    private void NewsCheck() {
        //checking for movies in temporary database
        if (mNewsUtil.getAllNewsCount(getActivity()) != 0) {
            allNewsWindow();
        }
    }
    //Load All Movies from temporary database
    private void allNewsWindow() {
        favflag = 0;
        Cursor allm = null;
        try {
            allm = mNewsUtil.allNewsCursor(getActivity());
            setUpAdapter(allm);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        /*finally {if (allm != null || !allm.isClosed()) {allm.close(); }}*/
    }

    //Method for loading Favorite Movies from database
    private void openFavorite() {

        Cursor c2 = null;
        try {
            c2 = mNewsUtil.favoriteNewsCursor(getActivity());
            CacheDelete(getContext());
            if (c2.getCount() == 0) {
                Toast.makeText(getContext(), "Currently You have not any Favorite News...Add it!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Favorite News", Toast.LENGTH_SHORT).show();
                setUpAdapter(c2);
                favflag = 1;
            }
        } catch (Exception e) {
        }//  finally {   if (c2 != null || !c2.isClosed()) {    c2.close(); }}// if comment is out not shows movies
    }

    private void setUpAdapter(Cursor c) {
        NewsRecyclerViewAdapter gridAdapter = new NewsRecyclerViewAdapter(getActivity(),c);
        mNewsLoader = mNewsLoader.newInstance(favflag, this, gridAdapter);
        gridAdapter = new NewsRecyclerViewAdapter(getActivity(),c);
        mNewsLoader.initLoader();
        gridAdapter.setClickListener(this);

        if (mRecyclerView != null)
            mRecyclerView.setAdapter(gridAdapter);
    }

    private void fetchNews(Map<String, String> data) {
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
                Log.d("dddddddddd","vvvvvvvv"+response.body().toString());
                Log.d("dddddddddd","vvvvvvvv"+response.errorBody().toString());
                mNewsUtil.insertData(getContext(), articlesList, "no");
                allNewsWindow();
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                t.getLocalizedMessage();
                Log.e(TAG,"vikas"+t.toString());
                Log.d(TAG,"vikas"+t.getLocalizedMessage());
                Log.d(TAG, "server contacted at: " + call.request().url());

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mNewsLoader != null)
            mNewsLoader.restartLoader();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                fetchNews(data);
            }
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        Cursor onClick = null;
        try {
            if (favflag == 1) {
                onClick = mNewsUtil.favoriteNewsCursor(getActivity());
            } else {
                onClick = mNewsUtil.allNewsCursor(getActivity());
            }
            boolean cursor = onClick.moveToPosition(position);
            if (cursor) {
                String args[]=mNewsUtil.getData(onClick);
                ((CallbackDetails) getActivity())
                        .onItemSelected(args[0],args[1],args[2],args[3],args[4],args[5]);
            }
        } catch (Exception e) {
        }/*finally {if (onClick != null || !onClick.isClosed()) {onClick.close();}    }*/
    }

}
