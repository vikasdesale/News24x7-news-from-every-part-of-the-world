package com.android.news24x7.adapter;

/**
 * Created by Dell on 6/3/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.news24x7.R;
import com.android.news24x7.interfaces.ColumnsNews;
import com.bumptech.glide.Glide;

/**
 * Created by Dell on 12/19/2016.
 */

public class NewsRecyclerViewAdapter extends CursorRecyclerViewAdapter<NewsRecyclerViewAdapter.ViewHolder> {

    public ClickListener clickListener;
    Cursor mCursor;
    private Context mContext;


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
    private View view;

    public NewsRecyclerViewAdapter(Context context, Cursor cursor) {
        super(cursor);
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_list, viewGroup, false);

        return new ViewHolder(view);
    }

    public void setClickListener(ClickListener clickListener) {

        this.clickListener = clickListener;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder viewHolder, Cursor cursor) {
       int viewType = getItemViewType(cursor.getPosition());
        String title = cursor.getString(cursor.getColumnIndex(ColumnsNews.TITLE));
        String url = cursor.getString(cursor.getColumnIndex(ColumnsNews.URL_TO_IMAGE));
        String publishedAt = cursor.getString(cursor.getColumnIndex(ColumnsNews.PUBLISHED_AT));
        viewHolder.imageView.setImageDrawable(null);


        if (title != null || url != null)
        {
            viewHolder.textView.setText(title);
            viewHolder.dates.setText(publishedAt);
            //Got Advantages why to use Glide over picasso that's why replaced picasso.
            Glide.with(mContext).load(url)
                    .thumbnail(0.1f)
                    .error(R.drawable.titled)
                    .crossFade() //animation
                    .into(viewHolder.imageView);
        }else {
            viewHolder.imageView.setImageDrawable(null);
            viewHolder.textView.setText("No Title");
            viewHolder.imageView.setImageResource(R.drawable.titled);
        }
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