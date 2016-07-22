package com.example.devel.evermeal.Widget.listviewfeed.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.devel.evermeal.Extend.FeedOnClickListener;
import com.example.devel.evermeal.R;
import com.example.devel.evermeal.Widget.listviewfeed.FeedImageView;
import com.example.devel.evermeal.Widget.listviewfeed.app.AppController;
import com.example.devel.evermeal.Widget.listviewfeed.data.FeedItem;
import com.example.devel.evermeal.Widget.listviewfeed.data.FeedItemValidated;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FeedListAdapter extends BaseAdapter
{
    private FeedOnClickListener btnRateListener = null;
    private FeedOnClickListener btnReviewListener = null;

    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems)
    {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount()
    {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location)
    {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        FeedViewHolder viewHolder = null;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.feed_item, null);

            viewHolder = new FeedViewHolder();

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
            viewHolder.statusMsg = (TextView) convertView.findViewById(R.id.txtStatusMsg);
            viewHolder.url = (TextView) convertView.findViewById(R.id.txtUrl);
            viewHolder.profilePic = (NetworkImageView) convertView.findViewById(R.id.profilePic);
            viewHolder.profileView = (TextView) convertView.findViewById(R.id.profileView);
            viewHolder.feedImageView = (FeedImageView) convertView.findViewById(R.id.feedImage1);

            viewHolder.btnRate = (Button) convertView.findViewById(R.id.btnRate);
            viewHolder.btnReview = (Button) convertView.findViewById(R.id.btnReview);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (FeedViewHolder) convertView.getTag();
        }

        FeedItem item = feedItems.get(position);
        item.setViewHolder(viewHolder);
        item.setFeedItemValidatedListener(itemValidated);

        viewHolder.btnReview.setTag(item);
        viewHolder.btnRate.setTag(item);

        viewHolder.btnRate.setOnClickListener(btnRate_Click);
        viewHolder.btnReview.setOnClickListener(btnReview_Click);

        InitFeedLayout(item, viewHolder);

        return convertView;
    }

    private FeedItemValidated itemValidated = new FeedItemValidated()
    {
        @Override
        public void onValidated(FeedItem item, String... args)
        {
            FeedViewHolder viewHolder = item.getViewHolder();

            if (viewHolder != null)
            {
                for (String e : args)
                {
                    switch (e)
                    {
                        case "rate":
                            if (viewHolder.btnRate != null)
                            {
                                viewHolder.btnRate.setText("별점(" + item.getRate() + ")");
                            }
                            break;
                    }
                }
            }
        }
    };

    private FeedOnClickListener btnRate_Click = new FeedOnClickListener()
    {
        @Override
        public void onClick(View view, FeedItem item)
        {
            btnRateListener.onClick(view, item);
        }

        @Override
        public void onClick(View v)
        {
            this.onClick(v, (FeedItem)v.getTag());
        }
    };

    private FeedOnClickListener btnReview_Click = new FeedOnClickListener()
    {
        @Override
        public void onClick(View view, FeedItem item)
        {
            btnReviewListener.onClick(view, item);
        }

        @Override
        public void onClick(View v)
        {
            this.onClick(v, (FeedItem)v.getTag());
        }
    };

    public void setRateClickListener(FeedOnClickListener listener)
    {
        btnRateListener = listener;
    }

    public void setReviewClickListener(FeedOnClickListener listener)
    {
        btnReviewListener = listener;
    }

    public static void InitFeedLayout(FeedItem item, FeedViewHolder viewHolder)
    {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        viewHolder.name.setText(item.getName());

        Date d = new Date(1970, 1, 1);
        d.setTime(Long.parseLong(item.getUpload_date()) * 1000);
        String dstr = new SimpleDateFormat("yyyy년 MM월 dd일").format(d);

        viewHolder.timestamp.setText(dstr);

        if (viewHolder.btnRate != null)
        {
            Log.v("btn", viewHolder.btnRate.getText().toString());
            viewHolder.btnRate.setText("별점(" + item.getRate() + ")");
        }

        if (!TextUtils.isEmpty(item.getContent()))
        {
            viewHolder.statusMsg.setText(item.getContent());
            viewHolder.statusMsg.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.statusMsg.setVisibility(View.GONE);
        }

        if (item.getTextCenter())
            viewHolder.statusMsg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // 프로필 이미지 설정
        viewHolder.profilePic.setVisibility(View.GONE);
        viewHolder.profileView.setVisibility(View.VISIBLE);

        viewHolder.profileView.setText(item.getName().substring(0, 1));

        if (item.getImage_url() != null)
        {
            viewHolder.url.setText(Html.fromHtml("<a href=\"" + item.getImage_url() + "\">" + item.getImage_url() + "</a> "));

            viewHolder.url.setMovementMethod(LinkMovementMethod.getInstance());
            viewHolder.url.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.url.setVisibility(View.GONE);
        }

/*
        profilePic.setImageUrl(item.getProfilePic(), imageLoader);

        if (item.getImge() != null)
        {
            feedImageView.setImageUrl(item.getImge(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver()
                    {
                        @Override
                        public void onError()
                        {
                        }

                        @Override
                        public void onSuccess()
                        {
                        }
                    });
        }
        else
        {
            feedImageView.setVisibility(View.GONE);
        }
        */
        viewHolder.feedImageView.setVisibility(View.GONE);
    }

}