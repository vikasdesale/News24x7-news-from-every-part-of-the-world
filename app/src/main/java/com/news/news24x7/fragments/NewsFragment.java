package com.news.news24x7.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.news.news24x7.R;
import com.news.news24x7.adapter.NewsRecyclerViewAdapter;
import com.news.news24x7.interfaces.ScrollViewExt;
import com.news.news24x7.interfaces.ScrollViewListener;
import com.news.news24x7.util.NetworkUtil;
import com.news.news24x7.util.NewsUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.news.news24x7.util.NewsUtil.CacheDelete;
import static com.news.news24x7.util.Util.BUSINESS;
import static com.news.news24x7.util.Util.ENTERTAINMENT;
import static com.news.news24x7.util.Util.HOME;
import static com.news.news24x7.util.Util.MUSIC;
import static com.news.news24x7.util.Util.POLITICS;
import static com.news.news24x7.util.Util.SCIENCE;
import static com.news.news24x7.util.Util.SPORT;
import static com.news.news24x7.util.Util.TECHNOLOGY;


public class NewsFragment extends Fragment implements NewsRecyclerViewAdapter.ClickListener, ScrollViewListener {


    public static final String LATEST = "latest";
    public static final String SOURCE = "source";
    public static final String SORTBY = "sortBy";
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "data";
    private static final String SELECTED_KEY = "selected_position";
    private static final String TOP = "top";
    private boolean mHoldForTransition;

    private static int favflag = 0;
    String nav_menu = null;
    String source[] = null;
    String sourceTopL[] = {"abc-news-au", "al-jazeera-english", "associated-press","bbc-news","bild",
            "cnn","focus","google-news","metro","mirror","newsweek","new-york-magazine","reddit-r-all"
    ,"reuters","spiegel-online","the-guardian-au","the-guardian-uk","the-hindu","the-huffington-post",
    "the-new-york-times","the-telegraph","the-times-of-india","the-washington-post","usa-today"};
    String sourceLatest[] = {"new-york-magazine","reddit-r-all"
            ,"reuters","spiegel-online","the-guardian-au","the-guardian-uk","the-hindu","the-huffington-post",
            "the-new-york-times","the-telegraph","the-times-of-india","the-washington-post","usa-today","abc-news-au", "al-jazeera-english", "associated-press","bbc-news","bild",
            "cnn","focus","google-news","metro","mirror","newsweek"};
    String sourceSport[] = {"bbc-sport","espn","espn-cric-info","football-italia","four-four-two","fox-sports","ign","nfl-news","polygon","the-sport-bible","talksport"};
    String sourceBusiness[] = {"bloomberg", "business-insider", "business-insider-uk","cnbc","die-zeit","financial-times",
    "fortune","handelsblatt","the-economist","the-wall-street-journal","wirtschafts-woche"};
    String sourceTechnology[] = {"ars-technica", "engadget", "gruenderszene","hacker-news","recode",
            "t3n","techcrunch","techradar","the-next-web","the-verge","wired-de"};
    String sourceEntertainment[] = {"buzzfeed", "daily-mail", "entertainment-weekly","mashable","the-lad-bible"};
    String sourceMusic[] = {"mtv-news", "mtv-news-uk"};
    String sourceScience[] = {"national-geographic", "new-scientist"};
    String sourcePolitics[] = {"breitbart-news"};
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.progressbar2)
    ProgressBar progressBar2;
    @BindView(R.id.error)
    LinearLayout errorLayout;
    @BindView(R.id.fragment_news_content)
    LinearLayout contLayout;
    @BindView(R.id.card_recycler_view)
    RecyclerView mRecyclerView;
    int i = 0;
    Map<String, String> data = new HashMap<>();
    NewsUtil mNewsUtil;
    NewsRecyclerViewAdapter gridAdapter;
    private ScrollViewExt scroll;
    private NewsLoader mNewsLoader;
    private int mPosition = ListView.INVALID_POSITION;
    private Cursor cursor;
    private Unbinder unbinder;
    private boolean mAutoSelectView;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsUtil = new NewsUtil();
        setRetainInstance(true);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            nav_menu = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        unbinder = ButterKnife.bind(this, v);
        scroll = (ScrollViewExt) v.findViewById(R.id.scroll);
        scroll.setScrollViewListener(this);
        if (!NetworkUtil.isNetworkConnected(getActivity())) {
            progressBar.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            contLayout.setVisibility(View.GONE);
        } else {
            progressBar2.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            NewsCheck();
            RetrofitCall.onRetrofit(new RetrofitCall.RetrofitCallback() {
                @Override
                public void onRetrofitCall(int article) {
                    // Send update broadcast to update the widget
                    getContext().sendBroadcast(new Intent(getString(R.string.widget_action)));
                    progressBar.setVisibility(View.GONE);
                    progressBar2.setVisibility(View.GONE);
                    allNewsWindow();
                    if (article == 1) {
                        progressBar.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                        Snackbar snackbar = Snackbar
                                .make(contLayout, R.string.network_data_not_found, Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                }
            });
        }


            mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    // Since we know we're going to get items, we keep the listener around until
                    // we see Children.
                    if(mRecyclerView!=null) {
                        if (mRecyclerView.getChildCount() > 0) {
                            mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                            RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForAdapterPosition(mPosition);
                            if (null != vh && mAutoSelectView) {
                            }
                            if (mHoldForTransition) {
                                getActivity().supportStartPostponedEnterTransition();
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });

        return v;
    }
    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.ForecastFragment,
                0, 0);
        mAutoSelectView = a.getBoolean(R.styleable.ForecastFragment_autoSelectView, false);
        mHoldForTransition = a.getBoolean(R.styleable.ForecastFragment_sharedElementTransitions, false);
        a.recycle();
    }

    private void NewsCheck() {
        if (nav_menu == null) {
            if (mNewsUtil.getAllNewsCount(getActivity()) != 0) {
                // Send update broadcast to update the widget
                getContext().sendBroadcast(new Intent(getString(R.string.widget_action)));
                allNewsWindow();
                progressBar.setVisibility(View.GONE);
                i = 0;
                source = sourceTopL;

            } else {
                i = 0;
                data.clear();
                source = sourceTopL;
                data.put(SOURCE, "" + source[i++]);
                data.put(SORTBY, TOP);
                RetrofitCall r = new RetrofitCall();
                r.fetchNews2(getContext(), data);
            }

        } else {
            onNavSelection(nav_menu);
        }
    }

    private void onNavSelection(String nav_menu) {
        favflag = 0;
        i = 0;
        switch (nav_menu) {
            case HOME:
                CacheDelete(getContext());
                source = sourceTopL;
                check(i, sourceTopL, TOP);
                break;
            case BUSINESS:
                CacheDelete(getContext());
                source = sourceBusiness;
                check(i, sourceBusiness, TOP);
                break;
            case TECHNOLOGY:
                CacheDelete(getContext());
                source = sourceTechnology;
                check(i, sourceTechnology, TOP);
                break;
            case SCIENCE:
                CacheDelete(getContext());
                source = sourceScience;
                check(i, sourceScience, TOP);
                break;
            case SPORT:
                CacheDelete(getContext());
                source = sourceSport;
                check(i, sourceSport, TOP);
                break;
            case POLITICS:
                CacheDelete(getContext());
                source = sourcePolitics;
                check(i, sourcePolitics, TOP);
                break;
            case MUSIC:
                CacheDelete(getContext());
                source = sourceMusic;
                check(i, sourceMusic, LATEST);
                break;
            case ENTERTAINMENT:
                CacheDelete(getContext());
                source = sourceEntertainment;
                check(i, sourceEntertainment, LATEST);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ;
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        gridAdapter = new NewsRecyclerViewAdapter(getActivity(), c);
        mNewsLoader = mNewsLoader.newInstance(favflag, this, gridAdapter);
        gridAdapter = new NewsRecyclerViewAdapter(getActivity(), c);
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
        unbinder.unbind();
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
        if (diff == 0 && favflag == 0) {
            {
                if (NetworkUtil.isNetworkConnected(getActivity())) {
                    if (errorLayout != null || progressBar != null || contLayout != null || progressBar2 != null) {
                        errorLayout.setVisibility(View.GONE);
                        contLayout.setVisibility(View.VISIBLE);
                        try {
                            if (i < source.length) {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                progressBar2.setVisibility(View.VISIBLE);
                                data.remove("source");
                                data.put("source", source[i++]);
                                RetrofitCall r = new RetrofitCall();
                                r.fetchNews2(getContext(), data);
                            } else {
                                progressBar2.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar
                                        .make(contLayout, R.string.more_articles_not_available, Snackbar.LENGTH_LONG);

                                snackbar.show();
                            }


                        } catch (Exception e) {
                        }
                        if (source == null) {
                            Snackbar snackbar = Snackbar
                                    .make(contLayout, R.string.more_articles_not_available, Snackbar.LENGTH_LONG);

                            snackbar.show();
                        }
                    }
                } else {
                    errorLayout.setVisibility(View.VISIBLE);
                    contLayout.setVisibility(View.GONE);
                }

            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.newsfragment, menu);

    }

    @Override
    public void itemClicked(View view, int position,NewsRecyclerViewAdapter.ViewHolder vh) {
        cursor = null;
        try {
            if (favflag == 1) {
                cursor = mNewsUtil.favoriteNewsCursor(getActivity());
            } else {
                cursor = mNewsUtil.allNewsCursor(getActivity());
            }
            boolean cursorBoolean = cursor.moveToPosition(position);
            if (cursorBoolean) {
                String args[] = mNewsUtil.getData(cursor);
                ((CallbackDetails) getActivity())
                        .onItemSelected(args[0], args[1], args[2], args[3], args[4], args[5],vh);
            }
        } catch (Exception e) {
        }/*finally {if (onClick != null || !onClick.isClosed()) {onClick.close();}    }*/
        mPosition = vh.getAdapterPosition();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // We hold for transition here just in-case the activity
        // needs to be re-created. In a standard return transition,
        // this doesn't actually make a difference.
        if ( mHoldForTransition ) {
            getActivity().supportPostponeEnterTransition();
        }

        // getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    public void check(int i, String s[], String sortBy) {
        if (i == s.length) {
            i = 0;
        }
        data.clear();
        data.put(SOURCE, "" + s[i++]);
        data.put(SORTBY, "" + sortBy);
        if (!NetworkUtil.isNetworkConnected(getActivity())) {
            progressBar.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            contLayout.setVisibility(View.GONE);
        } else {
            progressBar2.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            RetrofitCall r = new RetrofitCall();
            r.fetchNews2(getContext(), data);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        i = 0;
        CacheDelete(getContext());
        if (id == R.id.action_top) {
            favflag = 0;
            source = sourceTopL;
            check(i, sourceTopL, TOP);
            return true;
        }
        if (id == R.id.action_latest) {
            favflag = 0;
            CacheDelete(getContext());
            source = sourceLatest;
            check(i, sourceLatest, LATEST);
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
        cursor = null;
        try {
            cursor = mNewsUtil.favoriteNewsCursor(getActivity());
            CacheDelete(getContext());
            if (cursor.getCount() == 0) {
                Snackbar snackbar = Snackbar
                        .make(contLayout, R.string.favorite_not_available, Snackbar.LENGTH_LONG);

                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar
                        .make(contLayout, R.string.favorite_available, Snackbar.LENGTH_LONG);
                snackbar.show();
                setUpAdapter(cursor);
                favflag = 1;
            }
        } catch (Exception e) {
        }
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

        public void onItemSelected(String mTitle, String mAuthor, String mDescription, String mUrl, String mUrlToImage, String mPublishedAt,NewsRecyclerViewAdapter.ViewHolder vh);
    }


}


