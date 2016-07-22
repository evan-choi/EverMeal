package com.example.devel.evermeal.Widget.listviewfeed.data;

import android.support.v7.widget.RecyclerView;

import com.example.devel.evermeal.Widget.listviewfeed.adapter.FeedViewHolder;

public class FeedItem
{
    private FeedItemValidated listener;

    private int type;
    private String content, dependency, image_url, upload_date, uploader, name, aid, rate, u_rate;
    private Boolean textCenter;
    private FeedViewHolder ViewHolder;


    public FeedItem()
    {
    }

    public String getAid()
    {
        return aid;
    }

    public void setAid(String id)
    {
        this.aid = id;
    }


    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getDependency()
    {
        return dependency;
    }

    public void setDependency(String dependency)
    {
        this.dependency = dependency;
    }

    public String getImage_url()
    {
        return image_url;
    }

    public void setImage_url(String image_url)
    {
        this.image_url = image_url;
    }

    public String getUpload_date()
    {
        return upload_date;
    }

    public void setUpload_date(String upload_date)
    {
        this.upload_date = upload_date;
    }

    public String getUploader()
    {
        return uploader;
    }

    public void setUploader(String uploader)
    {
        this.uploader = uploader;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getTextCenter()
    {
        return textCenter;
    }

    public void setTextCenter(Boolean textCenter)
    {
        this.textCenter = textCenter;
    }

    public String getRate()
    {
        return rate;
    }

    public void setRate(String rate)
    {
        this.rate = rate;
    }

    public String getU_rate()
    {
        return u_rate;
    }

    public void setU_rate(String u_rate)
    {
        this.u_rate = u_rate;
    }

    public void setFeedItemValidatedListener(FeedItemValidated listener)
    {
        this.listener = listener;
    }

    public void Validate(String... args)
    {
        if (listener != null)
            listener.onValidated(this, args);
    }

    public FeedViewHolder getViewHolder()
    {
        return ViewHolder;
    }

    public void setViewHolder(FeedViewHolder viewHolder)
    {
        ViewHolder = viewHolder;
    }
}
