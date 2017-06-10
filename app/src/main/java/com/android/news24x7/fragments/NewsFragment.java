package com.android.news24x7.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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


    String nav_menu=null;
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "data";
    String source[]=null;
    String sourceTopL[]={"business-insider","bbc-sport","time"};
    String sourceSport[]={"espn-cric-info","talksport"};
    String sourceBusiness[]={"business-insider","bloomberg","cnbc"};
    String sourceEntertainment[]={"business-insider","bloomberg","cnbc"};
    String sourceMusic[]={"business-insider","bloomberg","cnbc"};
    String sourceScience[]={"business-insider","bloomberg","cnbc"};
    String sourceTechnology[]={"business-insider","bloomberg","cnbc"};
    String sourcePolitics[]={"business-insider","bloomberg","cnbc"};
    private String mParam1;
    private String mParam2;
    private static int favflag = 0;
    RecyclerView mRecyclerView;
    int i=0;
    private ScrollViewExt scroll;
    Map<String, String> data = new HashMap<>();
    NewsUtil mNewsUtil;
    private ArrayList<Article> articlesList;
    private NewsLoader mNewsLoader;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";
    Cursor cursor;
    NewsRecyclerViewAdapter gridAdapter;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mNewsUtil = new NewsUtil();
        setRetainInstance(true);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            nav_menu = getArguments().getString(ARG_PARAM1);
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

          NewsCheck();

        RetrofitCall.onRetrofit(new RetrofitCall.RetrofitCallback() {
            @Override
            public void onRetrofitCall() {
                // Send update broadcast to update the widget
                getContext().sendBroadcast(new Intent("android.appwidget.action.APPWIDGET_UPDATE"));
                allNewsWindow();
            }
        });
        return v;
    }



    private void NewsCheck() {
       if(nav_menu==null) {
           if (mNewsUtil.getAllNewsCount(getActivity()) != 0) {
               // Send update broadcast to update the widget
               getContext().sendBroadcast(new Intent("android.appwidget.action.APPWIDGET_UPDATE"));
               allNewsWindow();

           } else {
               i = 0;
               data.clear();
               source=sourceTopL;
               data.put("source", "" + source[i++]);
               data.put("sortBy", "top");
               RetrofitCall r = new RetrofitCall();
               r.fetchNews(getContext(), data);
           }

       }else{
           onNavSelection(nav_menu);
       }
    }

    private void onNavSelection(String nav_menu) {
        favflag = 0;
        i=0;
        switch (nav_menu)
        {
            case "Business":
                CacheDelete(getContext());
                source=sourceBusiness;
                check(i,sourceBusiness,"latest");
                break;
            case "Technology":
                CacheDelete(getContext());
                source=sourceTechnology;
                check(i,sourceBusiness,"latest");
                break;
            case "Science-and-nature":
                CacheDelete(getContext());
                source=sourceScience;
                check(i,sourceScience,"latest");
                break;
            case "Sport":
                CacheDelete(getContext());
                source=sourceSport;
                check(i,sourceSport,"latest");
                break;
            case "Politics":
                CacheDelete(getContext());
                source=sourcePolitics;
                check(i,sourcePolitics,"latest");
                break;
            case "Music":
                CacheDelete(getContext());
                source=sourceMusic;
                check(i,sourceMusic,"latest");
                break;
            case "Entertainment":
                CacheDelete(getContext());
                source=sourceEntertainment;
                check(i,sourceEntertainment,"latest");
                break;
        }
    }

    //Load All Movies from temporary database
    private void allNewsWindow() {
        try {
            cursor = mNewsUtil.allNewsCursor(getActivity());
            setUpAdapter(cursor);
        } catch (Exception e) {

        }

    }


    private void setUpAdapter(Cursor c) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());;
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        gridAdapter = new NewsRecyclerViewAdapter(getActivity(),c);
        mNewsLoader = mNewsLoader.newInstance(favflag, this, gridAdapter);
        gridAdapter = new NewsRecyclerViewAdapter(getActivity(),c);
        mNewsLoader.initLoader();
        gridAdapter.setClickListener(this);
        if (mRecyclerView != null)
            mRecyclerView.setAdapter(gridAdapter);
        // Send update broadcast to update the widget
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
        if (diff == 0 && favflag == 0 ) {
            if (i < source.length)
            {      data.remove("source");
            data.put("source", source[i++]);
            RetrofitCall r = new RetrofitCall();
            r.fetchNews(getContext(), data);
        }
        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.moviefragment, menu);

    }
    @Override
    public void itemClicked(View view, int position) {
         cursor = null;
        try {
            if (favflag == 1) {
                cursor = mNewsUtil.favoriteNewsCursor(getActivity());
            } else {
                cursor = mNewsUtil.allNewsCursor(getActivity());
            }
            boolean cursorBoolean = cursor.moveToPosition(position);
            if (cursorBoolean) {
                String args[]=mNewsUtil.getData(cursor);
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

        if (id == R.id.action_top) {
            favflag = 0;
            i=0;
            CacheDelete(getContext());
            source=sourceTopL;
            check(i,sourceTopL,"top");
            return true;
        }
        if (id == R.id.action_latest) {
            i = 0;
            favflag = 0;
            CacheDelete(getContext());
            source=sourceTopL;
            check(i,sourceTopL,"latest");
            return true;
        }
        if (id == R.id.action_save) {
            openFavorite();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    //Method for loading Favorite Movies from database
    private void openFavorite() {
        cursor= null;
        try {
            cursor = mNewsUtil.favoriteNewsCursor(getActivity());
            CacheDelete(getContext());
            if (cursor.getCount() == 0) {
                Toast.makeText(getContext(), "Currently You have not any Favorite News...Add it!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Favorite News", Toast.LENGTH_SHORT).show();
               // errorLayout.setVisibility(View.GONE);
               // contLayout.setVisibility(View.VISIBLE);
                setUpAdapter(cursor);
                favflag = 1;
            }
        } catch (Exception e) {
        }
    }





}


