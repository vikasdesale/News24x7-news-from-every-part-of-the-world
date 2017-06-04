package com.android.news24x7.adapter;

/**
 * Created by Dell on 6/3/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.news24x7.R;
import com.android.news24x7.parcelable.Article;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Dell on 12/19/2016.
 */

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>  {

    private final ArrayList<Article> articles;
    public ClickListener clickListener;
    private Context mContext;
    private View view;
    private int resource;


    public NewsRecyclerViewAdapter(Context context,int resource, ArrayList<Article> articlesList) {
        this.mContext = context;
        this.resource=resource;
        this.articles=articlesList;
    }
    @Override
    public NewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list, parent, false);

        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(NewsRecyclerViewAdapter.ViewHolder holder, int position) {
        String title = articles.get(position).getTitle();
        String url = articles.get(position).getUrlToImage();


        if (title != null || url != null)
        {
            Log.d("URL FOr me:",""+url);
            holder.textView.setText(title);
            holder.dates.setText(articles.get(position).getPublishedAt());
        //Got Advantages why to use Glide over picasso that's why replaced picasso.
               Glide.with(mContext).load(url)
                .thumbnail(0.1f)
                .error(R.drawable.titled)
                .crossFade() //animation
                .into(holder.imageView);
    }else {
            holder.imageView.setImageDrawable(null);
            holder.textView.setText("No Title");
            holder.imageView.setImageResource(R.drawable.titled);
        }
    }
    public void setClickListener(ClickListener clickListener) {

        this.clickListener = clickListener;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return articles.size();
    }
    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        TextView dates;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
             textView = (TextView)itemView.findViewById(R.id.news_article_title);
             dates = (TextView)itemView.findViewById(R.id.news_published_at);
             imageView = (ImageView)itemView.findViewById(R.id.news_thumbnail);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }
        }
    }
}
