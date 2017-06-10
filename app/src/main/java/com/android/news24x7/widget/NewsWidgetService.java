package com.android.news24x7.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Dell on 6/10/2017.
 */

public class NewsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return( new NewsWidgetViewsFactory( this.getApplicationContext(), intent ) );
    }
}