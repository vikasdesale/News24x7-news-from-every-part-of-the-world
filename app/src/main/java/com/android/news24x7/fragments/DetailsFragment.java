package com.android.news24x7.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.news24x7.R.drawable.placeholder;
import static com.android.news24x7.util.NewsUtil.FAVORITE;

public class DetailsFragment extends Fragment implements View.OnClickListener,TextToSpeech.OnInitListener {
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
    @BindView(R.id.speak)
    ImageView Speech;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_save)
    FloatingActionButton fab;
    @BindView(R.id.main_content)
    CoordinatorLayout contLayout;
    private Intent intent;
    private Unbinder unbinder;
    public static final String DETAIL_TRANSITION_ANIMATION = "DTA";
    private boolean mTransitionAnimation;
    private TextToSpeech tts;

    public DetailsFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInt();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        tts = new TextToSpeech(getContext(), this);

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
                ScreenDelete();
                adView.setVisibility(View.GONE);
                Bitmap bitmap=getScreenShot(rootView);
                store(bitmap,"temp.png");
                adView.setVisibility(View.VISIBLE);

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
        mArticleReadMore.setContentDescription(getString(R.string.cotent_desc_read_more));
        fab.setContentDescription(getString(R.string.content_desc_share_article));
        Speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Speech.setImageResource(R.drawable.speak_on);
                tts.setPitch((float) 0.6);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mTitleText.setContentDescription(getString(R.string.content_desc_title) + mTitle);
                    mByText.setContentDescription(getString(R.string.content_desc_author) + mAuthor);
                    mArticleBody.setContentDescription(getString(R.string.content_desc_article) + mDescription);
                    tts.speak(getString(R.string.content_desc_title) + "  " + mTitle + "  " +
                            getString(R.string.content_desc_author) + "  " + mAuthor + "  " +
                            getString(R.string.content_desc_article) + "  " + mDescription, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    tts.speak(getString(R.string.content_desc_title) + "  " + mTitle + "  " +
                            getString(R.string.content_desc_author) + "  " + mAuthor + "  " +
                            getString(R.string.content_desc_article) + "  " + mDescription, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        return rootView;
    }

    public void ScreenDelete(){
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.android.news24x7/Screenshots/";
        File file = new File(dirPath);
       file.delete();

    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }
    public void store(Bitmap bm, String fileName){
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.android.news24x7/Screenshots/";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
             shareImage(file);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.detail_share)
                +"\nhttps://w524x.app.goo.gl/PeSx");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share News Article"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Not App Available", Toast.LENGTH_SHORT).show();
        }
    }
    //this is on direct call
    private void initializeInt() {
        mNewsUtil = new NewsUtil();
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString(NewsWidgetProvider.EXTRA_TITLE);
            mAuthor = arguments.getString(NewsWidgetProvider.EXTRA_AUTHOR);
            mDescription = arguments.getString(NewsWidgetProvider.EXTRA_DESCRIPTION);
            mUrl = arguments.getString(NewsWidgetProvider.EXTRA_URL);
            mUrlToImage = arguments.getString(NewsWidgetProvider.EXTRA_IMAGE_URL);
            mPublishedAt = arguments.getString(NewsWidgetProvider.EXTRA_DATE);
            mTransitionAnimation = arguments.getBoolean(DetailsFragment.DETAIL_TRANSITION_ANIMATION, false);
        }
    }

    @Override
    public void onDestroyView() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setData() {
        n = NewsUtil.CheckisFavourite(getActivity(), mTitle);
        if (n == 1) {
            n = 0;
            myFavoriteNews.setImageResource(R.drawable.star);
        } else {
            n = 1;
            myFavoriteNews.setImageResource(R.drawable.star_off);

        }

        //Got Advantages why to use Glide over picasso that's why replaced picasso.
        Glide.with(this).load(mUrlToImage)
                .thumbnail(0.1f)
                .error(placeholder)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .into(mToolbarImage);
        mTitleText.setText(mTitle);
        mByText.setText(mAuthor);
        mArticleBody.setText(mDescription);
        myFavoriteNews.setContentDescription(getString(R.string.my_favorite_save));
        mTitleText.setContentDescription(getString(R.string.content_desc_title)+mTitle);
        mByText.setContentDescription(getString(R.string.content_desc_author)+mAuthor);
        mArticleBody.setContentDescription(getString(R.string.content_desc_article)+mDescription);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        // We need to start the enter transition after the data has loaded
        if ( mTransitionAnimation ) {
            activity.supportStartPostponedEnterTransition();

            if ( null != toolbar ) {
                activity.setSupportActionBar(toolbar);

                activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

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
                myFavoriteNews.setImageResource(R.drawable.star);
                Snackbar snackbar = Snackbar
                        .make(contLayout, R.string.article_saved, Snackbar.LENGTH_LONG);
                snackbar.show();

            } catch (Exception e) {
            }
        } else {
            n = 1;
            myFavoriteNews.setImageResource(R.drawable.star_off);
            NewsUtil.FavouriteDelete(getActivity(), mTitle);
            Snackbar snackbar = Snackbar
                    .make(contLayout, R.string.article_deleted, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


    @Override
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(tts.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                tts.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(getActivity(), "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}
