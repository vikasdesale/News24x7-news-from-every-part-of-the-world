package com.android.news24x7.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.news24x7.R;
import com.android.news24x7.adapter.NewsRecyclerViewAdapter;
import com.android.news24x7.interfaces.ScrollViewExt;
import com.android.news24x7.interfaces.ScrollViewListener;
import com.android.news24x7.parcelable.Article;
import com.android.news24x7.util.NewsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.news24x7.util.NewsUtil.CacheDelete;


public class NewsFragment extends Fragment implements NewsRecyclerViewAdapter.ClickListener,ScrollViewListener {


    String tab;
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "data";
    String source[]={"the-times-of-india","the-hindu","usa-today","time","mtv-news"};
    String sourceSport[]={"bbc-sport","espn-cric-info","talksport"};
    String sourceEntertainment[]={"mtv-news","the-lad-bible"};
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

        RetrofitCall.onRetrofit(new RetrofitCall.RetrofitCallback() {
            @Override
            public void onRetrofitCall() {
                allNewsWindow();
            }
        });
        return v;
    }
    //Load All Movies from temporary database
    private void allNewsWindow() {
        favflag = 0;
        Cursor allm = null;
        try {
            allm = mNewsUtil.allNewsCursor(getActivity());
            setUpAdapter(allm);
        } catch (Exception e) {

        }

    }


    private void setUpAdapter(Cursor c) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());;
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        Log.e("start Setup Fragment 1:","Adapter"+c.getCount());
        NewsRecyclerViewAdapter gridAdapter = new NewsRecyclerViewAdapter(getActivity(),c);
        mNewsLoader = mNewsLoader.newInstance(favflag, this, gridAdapter);
        gridAdapter = new NewsRecyclerViewAdapter(getActivity(),c);
        mNewsLoader.initLoader();
        gridAdapter.setClickListener(this);
        Log.e("start Setup Fragment 1:","Adapter"+c.getCount());
        if (mRecyclerView != null)
            mRecyclerView.setAdapter(gridAdapter);
        Log.e("start Setup Fragment 1:","Adapter"+c.getCount());

        //  mRecyclerView.swapAdapter(gridAdapter,true);
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

            data.remove("source");
            data.put("source",source[i++]);
            RetrofitCall r=new RetrofitCall();
            r.fetchNews(getContext(),data);

        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.moviefragment, menu);

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
    public void check(int i,String s[],String sortBy){
        if(i==s.length){
            i=0;
        }
        data.clear();
        data.put("source", ""+s[i++]);
        data.put("sortBy",""+sortBy);
        RetrofitCall r=new RetrofitCall();
        r.fetchNews(getContext(),data);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_highlights) {
            CacheDelete(getContext());
            check(i,source,"top");
            return true;
        }
        if (id == R.id.action_headlines) {
            i = 0;
            CacheDelete(getContext());
            check(i,source,"latest");
            return true;
        }

        if (id == R.id.sport) {
            i = 0;
            CacheDelete(getContext());
            check(i,sourceSport,"");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }








}


