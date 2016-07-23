package com.devjy.devel.evermeal.Extend;

import android.view.View;

import com.devjy.devel.evermeal.Widget.listviewfeed.data.FeedItem;

/**
 * Created by devel on 2016-07-19.
 */
public interface FeedOnClickListener extends View.OnClickListener
{
    void onClick(View view, FeedItem item);
}
