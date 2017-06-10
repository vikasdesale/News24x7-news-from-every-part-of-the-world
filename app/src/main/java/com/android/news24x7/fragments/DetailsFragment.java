package com.android.news24x7.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.news24x7.R;
import com.android.news24x7.parcelable.Article;
import com.android.news24x7.util.NewsUtil;
import com.android.news24x7.widget.NewsWidgetProvider;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class DetailsFragment extends Fragment implements View.OnClickListener {
    public static final String ID = "ID";
    private static final String NEWS_SHARE_HASHTAG = " #MyNEWS";
    String mTitle;
    String mAuthor;
    String mDescription;
    String mUrl;
    String mUrlToImage;
    String mPublishedAt;
    View rootView;
    int n = -1;
    int i = 0;
    private LinearLayoutManager mLinearLayoutManager;
    private Intent intent;
    private TextView mTitleText;
    private TextView mByText;
    private TextView mArticleReadMore;
    private TextView mArticleBody;
    private ImageView myFavoriteNews;
    ImageView mToolbarImage;
    NewsUtil mNewsUtil;
    AdView adView;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private Toolbar toolbar;
    public DetailsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initalizeInt();


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
           mTitleText= (TextView) rootView.findViewById(R.id.article_title);
           mByText= (TextView) rootView.findViewById(R.id.article_byline);
           mArticleBody=(TextView) rootView.findViewById(R.id.article_body);
           mArticleReadMore=(TextView)  rootView.findViewById(R.id.article_read_more);
           mToolbarImage = (ImageView) rootView.findViewById(R.id.backdrop);
           myFavoriteNews=(ImageView)rootView.findViewById(R.id.save);
           adView = (AdView) rootView.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdView adView = (AdView)rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("0123456789ABCDEF")
                .build();
        adView.loadAd(adRequest);
        myFavoriteNews.setOnClickListener(this);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mCollapsingToolbar =
                (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText("Title: " + mTitle + "\n By " + mAuthor+"\n\nDescription: \n"+mDescription)
                        .getIntent(), getString(R.string.action_share)));
            }
        });
        mArticleReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(mUrl));
                startActivity(webIntent);
            }
        });
        setData();
        Toast.makeText(getContext(), "News Inserted in Favourite", Toast.LENGTH_SHORT).show();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().finish();
            }
        });
        return rootView;

    }
    private void setUp() {

            }

    //this is on direct call
    private void initalizeInt() {
        mNewsUtil=new NewsUtil();
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle=arguments.getString(NewsWidgetProvider.EXTRA_TITLE);
            mAuthor=arguments.getString(NewsWidgetProvider.EXTRA_AUTHOR);
            mDescription=arguments.getString(NewsWidgetProvider.EXTRA_DESCRIPTION);
            mUrl=arguments.getString(NewsWidgetProvider.EXTRA_URL);
            mUrlToImage=arguments.getString(NewsWidgetProvider.EXTRA_IMAGE_URL);
            mPublishedAt=arguments.getString(NewsWidgetProvider.EXTRA_DATE);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setData() {
        n = NewsUtil.CheckisFavourite(getActivity(), mTitle);
        if (n == 1) {
            n = 0;
            myFavoriteNews.setImageResource(android.R.drawable.btn_star_big_on);
        }
           else{ n = 1;
            myFavoriteNews.setImageResource(android.R.drawable.btn_star_big_off);

        }

        //Got Advantages why to use Glide over picasso that's why replaced picasso.
        Glide.with(this).load(mUrlToImage)
                .thumbnail(0.1f)
                .error(R.drawable.titled)
                .crossFade() //animation
                .into(mToolbarImage);
            mTitleText.setText(mTitle);
            mByText.setText(mAuthor);
            mArticleBody.setText(mDescription);

        }




    //on favorite click
    @Override
    public void onClick(View view) {
        if (n == 1) {
            n = 0;
            try {
                Article article;
                article = new Article(mTitle, mAuthor, mDescription, mUrl, mUrlToImage,mPublishedAt);

                ArrayList<Article> m = new ArrayList<Article>();
                m.add(0, article);
                mNewsUtil.insertData(getActivity(), m, "favourite");
                myFavoriteNews.setImageResource(android.R.drawable.btn_star_big_on);
                Toast.makeText(getContext(), "News Inserted in Favourite", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        } else {
            n = 1;
            myFavoriteNews.setImageResource(android.R.drawable.btn_star_big_off);
            NewsUtil.FavouriteDelete(getActivity(), mTitle);
            Toast.makeText(getActivity(), "News deleted from  favourite", Toast.LENGTH_SHORT).show();
        }
    }




}
