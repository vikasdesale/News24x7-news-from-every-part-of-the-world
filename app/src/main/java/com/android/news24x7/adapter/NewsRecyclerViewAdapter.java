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

/**
 * Created by Dell on 12/19/2016.
 */

public class NewsRecyclerViewAdapter extends ArrayAdapter {

    private Context mContext;
    private int resource;
    private int imageId[];
    private String web[];
    private String datesd[];

    public NewsRecyclerViewAdapter(Context context, int resource, String[] web, int[] imageId,String dates[]) {
        super(context, resource);
        this.resource = resource;
        this.mContext = context;
        this.imageId = imageId;
        this.web = web;
        this.datesd=dates;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.length;
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
            textView.setText(web[position]);
            dates.setText(datesd[position]);

            imageView.setImageResource(imageId[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }

}
