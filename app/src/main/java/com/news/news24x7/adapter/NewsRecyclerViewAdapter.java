package com.news.news24x7.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.news.news24x7.R;
import com.news.news24x7.interfaces.ColumnsNews;
import com.news.news24x7.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.news.news24x7.adapter.CursorRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.news.news24x7.R.drawable.placeholder;

/**
 * Created by Dell on 6/3/2017.
 */


public class NewsRecyclerViewAdapter extends CursorRecyclerViewAdapter<NewsRecyclerViewAdapter.ViewHolder> {

    public ClickListener clickListener;
    Cursor mCursor;
    private Context mContext;
    private View view;

    public NewsRecyclerViewAdapter(Context context, Cursor cursor) {
        super(cursor);
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder viewHolder, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(ColumnsNews.TITLE));
        String url = cursor.getString(cursor.getColumnIndex(ColumnsNews.URL_TO_IMAGE));
        String publishedAt = cursor.getString(cursor.getColumnIndex(ColumnsNews.PUBLISHED_AT));
        viewHolder.imageView.setImageDrawable(null);


        if (title != null || url != null) {
            viewHolder.textView.setText(title);
            viewHolder.textView.setContentDescription(mContext.getString(R.string.content_desc_title)+title);
            viewHolder.dates.setText(Util.manipulateDateFormat(publishedAt));
            viewHolder.dates.setContentDescription(mContext.getString(R.string.content_description_dates)+Util.manipulateDateFormat(publishedAt));
            //Got Advantages why to use Glide over picasso that's why replaced picasso.
            Glide.with(mContext).load(url)
                    .thumbnail(0.1f)
                    .error(placeholder)
                    .crossFade() //animation
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .skipMemoryCache(true)
                    .into(viewHolder.imageView);
            viewHolder.imageView.setContentDescription(mContext.getString(R.string.article_image));
        } else {
            viewHolder.imageView.setImageDrawable(null);
            viewHolder.textView.setText(R.string.no_image_title);
            viewHolder.imageView.setImageResource(R.drawable.placeholder);
            // this enables better animations. even if we lose state due to a device rotation,
            // the animator can use this to re-find the original view
            ViewCompat.setTransitionName(viewHolder.imageView, "iconView" +cursor.getPosition());

        }


    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_list, viewGroup, false);


        return new ViewHolder(view);
    }

    public void setClickListener(ClickListener clickListener) {

        this.clickListener = clickListener;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public interface ClickListener {
        public void itemClicked(View view, int position,NewsRecyclerViewAdapter.ViewHolder vh);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.news_article_title)
        TextView textView;
        @BindView(R.id.news_published_at)
        TextView dates;
        @BindView(R.id.backdrop)
        public
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, view);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
           ViewHolder vh=new ViewHolder(v);
            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition(), vh);
            }
        }
    }
}