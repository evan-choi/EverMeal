package com.example.devel.evermeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.devel.evermeal.Widget.listviewfeed.FeedImageView;
import com.example.devel.evermeal.Widget.listviewfeed.adapter.FeedListAdapter;
import com.example.devel.evermeal.Widget.listviewfeed.adapter.FeedViewHolder;
import com.example.devel.evermeal.Widget.listviewfeed.data.FeedItem;

public class FeedActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // 툴바 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // 피드 설정
        FeedViewHolder viewHolder = new FeedViewHolder();
        viewHolder.name = (TextView) findViewById(R.id.name);
        viewHolder.timestamp = (TextView) findViewById(R.id.timestamp);
        viewHolder.statusMsg = (TextView) findViewById(R.id.txtStatusMsg);
        viewHolder.url = (TextView) findViewById(R.id.txtUrl);
        viewHolder.profilePic = (NetworkImageView) findViewById(R.id.profilePic);
        viewHolder.feedImageView = (FeedImageView) findViewById(R.id.feedImage1);

        Intent intent = getIntent();

        FeedItem item = new FeedItem();
        /*
        item.setProfilePic(intent.getStringExtra("profilePic"));
        item.setName(intent.getStringExtra("name"));
        item.setTimeStamp(intent.getStringExtra("timestamp"));
        item.setStatus(intent.getStringExtra("txtStatusMsg"));
        item.setUrl(intent.getStringExtra("txtUrl"));
        item.setImge(intent.getStringExtra("feedImage1"));
        */
        FeedListAdapter.InitFeedLayout(item, viewHolder);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}
