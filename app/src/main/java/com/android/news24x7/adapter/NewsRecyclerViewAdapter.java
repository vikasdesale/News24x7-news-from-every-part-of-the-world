package com.android.news24x7.adapter;

/**
 * Created by Dell on 6/3/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.news24x7.R;
import com.android.news24x7.parcelable.Article;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Dell on 12/19/2016.
 */

public class NewsRecyclerViewAdapter extends ArrayAdapter {

    private final ArrayList<Article> articles;
    private Context mContext;
    private int resource;

    public NewsRecyclerViewAdapter(Context context, int resource,ArrayList<Article> articlesList) {
        super(context, resource);
        this.resource = resource;
        this.mContext = context;
        this.articles=articlesList;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return articles.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.news_list, null);
            TextView textView = (TextView) grid.findViewById(R.id.news_article_title);
            TextView dates = (TextView) grid.findViewById(R.id.news_published_at);

            ImageView imageView = (ImageView) grid.findViewById(R.id.news_thumbnail);

            textView.setText(articles.get(position).getTitle());
            dates.setText(articles.get(position).getPublishedAt());
            //Got Advantages why to use Glide over picasso that's why replaced picasso.
            Glide.with(mContext).load(articles.get(position).getUrl())
                    .thumbnail(0.1f)
                    .error(R.drawable.titled)
                    .crossFade() //animation
                    .into(imageView);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }

}
