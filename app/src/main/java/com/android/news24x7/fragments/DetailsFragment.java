package com.android.news24x7.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.news24x7.R.drawable.placeholder;
import static com.android.news24x7.util.NewsUtil.FAVORITE;

public class DetailsFragment extends Fragment implements View.OnClickListener {
    String mTitle;
    String mAuthor;
    String mDescription;
    String mUrl;
    String mUrlToImage;
    String mPublishedAt;
    View rootView;
    int n = -1;
    @BindView(R.id.news_thumbnail)
    ImageView mToolbarImage;
    NewsUtil mNewsUtil;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.article_title)
    TextView mTitleText;
    @BindView(R.id.article_byline)
    TextView mByText;
    @BindView(R.id.article_read_more)
    TextView mArticleReadMore;
    @BindView(R.id.article_body)
    TextView mArticleBody;
    @BindView(R.id.save)
    ImageView myFavoriteNews;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_save)
    FloatingActionButton fab;
    @BindView(R.id.main_content)
    CoordinatorLayout contLayout;
    private Intent intent;
    private Unbinder unbinder;


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
        unbinder = ButterKnife.bind(this, rootView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //  .addTestDevice(R.string.deviceId)   Add you device id
                .build();
        adView.loadAd(adRequest);
        myFavoriteNews.setOnClickListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType(getString(R.string.setType))
                        .setText(getString(R.string.title_share) + mTitle + getString(R.string.by_share) + mAuthor + getString(R.string.description_share) + mDescription)
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return rootView;

    }


    //this is on direct call
    private void initalizeInt() {
        mNewsUtil = new NewsUtil();
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString(NewsWidgetProvider.EXTRA_TITLE);
            mAuthor = arguments.getString(NewsWidgetProvider.EXTRA_AUTHOR);
            mDescription = arguments.getString(NewsWidgetProvider.EXTRA_DESCRIPTION);
            mUrl = arguments.getString(NewsWidgetProvider.EXTRA_URL);
            mUrlToImage = arguments.getString(NewsWidgetProvider.EXTRA_IMAGE_URL);
            mPublishedAt = arguments.getString(NewsWidgetProvider.EXTRA_DATE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setData() {
        n = NewsUtil.CheckisFavourite(getActivity(), mTitle);
        if (n == 1) {
            n = 0;
            myFavoriteNews.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            n = 1;
            myFavoriteNews.setImageResource(android.R.drawable.btn_star_big_off);

        }

        //Got Advantages why to use Glide over picasso that's why replaced picasso.
        Glide.with(this).load(mUrlToImage)
                .thumbnail(0.1f)
                .error(placeholder)
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
                article = new Article(mTitle, mAuthor, mDescription, mUrl, mUrlToImage, mPublishedAt);

                ArrayList<Article> m = new ArrayList<Article>();
                m.add(0, article);
                mNewsUtil.insertData(getActivity(), m, FAVORITE);
                myFavoriteNews.setImageResource(android.R.drawable.btn_star_big_on);
                Snackbar snackbar = Snackbar
                        .make(contLayout, R.string.article_saved, Snackbar.LENGTH_LONG);
                snackbar.show();

            } catch (Exception e) {
            }
        } else {
            n = 1;
            myFavoriteNews.setImageResource(android.R.drawable.btn_star_big_off);
            NewsUtil.FavouriteDelete(getActivity(), mTitle);
            Toast.makeText(getActivity(), R.string.article_deleted, Toast.LENGTH_SHORT).show();
        }
    }


}
