package com.devjy.devel.evermeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.devjy.devel.evermeal.Auth.UserManager;
import com.devjy.devel.evermeal.Widget.cmtlistview.adapter.CommentListAdapter;
import com.devjy.devel.evermeal.Widget.cmtlistview.data.CommentItem;
import com.devjy.devel.evermeal.Widget.cmtlistview.data.CommentItemDescComparator;
import com.devjy.devel.evermeal.Widget.listviewfeed.FeedImageView;
import com.devjy.devel.evermeal.Widget.listviewfeed.adapter.FeedListAdapter;
import com.devjy.devel.evermeal.Widget.listviewfeed.adapter.FeedViewHolder;
import com.devjy.devel.evermeal.Widget.listviewfeed.app.AppController;
import com.devjy.devel.evermeal.Widget.listviewfeed.data.FeedItem;
import com.devjy.devel.evermeal.gcm.MyGcmListenerService;
import com.devjy.devel.evermeal.gcm.OnGcmMessageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FeedActivity extends AppCompatActivity
{
    private String URL_COMMENT = "https://evermeal.herokuapp.com/comment";
    private String URL_COMMENT_WRITE = "https://evermeal.herokuapp.com/feed/write";
    private CommentListAdapter cla;
    private List<CommentItem> commentItemList;

    private FeedItem feedItem;

    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        MyGcmListenerService.setOnGcmMessageListener(onGcmMessageListener);

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
        viewHolder.profileView = (TextView) findViewById(R.id.profileView);
        viewHolder.feedImageView = (FeedImageView) findViewById(R.id.feedImage1);
        viewHolder.commentList = (ListView)findViewById(R.id.commentList);

        edit = (EditText)findViewById(R.id.editText);
        Button btn = (Button)findViewById(R.id.btnComment);

        btn.setOnClickListener(writeComment);

        Intent intent = getIntent();
        feedItem = new FeedItem();
        feedItem.setType(intent.getIntExtra("type", 0));
        feedItem.setContent(intent.getStringExtra("content"));
        feedItem.setDependency(intent.getStringExtra("dependency"));
        feedItem.setImage_url(intent.getStringExtra("image_url"));
        feedItem.setUpload_date(intent.getStringExtra("upload_date"));
        feedItem.setUploader(intent.getStringExtra("uploader"));
        feedItem.setName(intent.getStringExtra("name"));
        feedItem.setAid(intent.getStringExtra("aid"));
        feedItem.setTextCenter(intent.getBooleanExtra("textCenter", false));

        commentItemList = new ArrayList<>();
        cla = new CommentListAdapter(this, R.layout.comment_item, commentItemList);
        viewHolder.commentList.setAdapter(cla);

        FeedListAdapter.InitFeedLayout(feedItem, viewHolder);

        Refresh();
    }

    View.OnClickListener writeComment = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            String content = edit.getText().toString();
            edit.setText("");

            if (content.length() > 0)
            {
                JSONObject body = new JSONObject();

                try
                {
                    body.accumulate("type", String.valueOf(feedItem.getType()));
                    body.accumulate("uploader", UserManager.UserInfo.ID);
                    body.accumulate("content", content);
                    body.accumulate("image_url","");
                    body.accumulate("dependency", feedItem.getAid());
                }
                catch (JSONException e)
                {
                }

                JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST,
                        URL_COMMENT_WRITE, body, new Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        boolean result = false;

                        try
                        {
                            result = response.getBoolean("result");
                        }
                        catch (JSONException e)
                        {
                        }
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                    }
                });

                AppController.getInstance().addToRequestQueue(jsonReq);

                CommentItem item = new CommentItem();

                item.setComment(content);
                item.setTimestamp("0");

                commentItemList.add(0, item);
                cla.notifyDataSetChanged();
            }
        }
    };

    OnGcmMessageListener onGcmMessageListener = new OnGcmMessageListener()
    {
        @Override
        public boolean onMessageReceived(String type, Bundle bundle)
        {
            if (type.contentEquals("2") && bundle.containsKey("aid"))
            {
                String aid = bundle.getString("aid");

                if (feedItem.getAid().contentEquals(aid))
                {
                    Refresh();
                    return true;
                }
            }

            return false;
        }
    };

    public void Refresh()
    {
        long date = 0;

        for (int i = 0; i < cla.getCount(); i++)
        {
            long ts = Long.parseLong(cla.getItem(i).getTimestamp());

            if (ts > date)
                date = ts;
        }

        String url = URL_COMMENT + "?aid=" + feedItem.getAid() + "&date=" + date;

        JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                if (response != null)
                {
                    parseJsonComment(response);
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
            }
        });

        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    private void parseJsonComment(JSONArray response)
    {
        for (int i = 0; i < response.length(); i++)
        {
            try
            {
                JSONObject obj = response.getJSONObject(i);

                String aid = obj.getString("aid");
                String content = obj.getString("content");
                String dependency = obj.getString("dependency");
                String upload_date = obj.getString("upload_date");

                CommentItem item = new CommentItem();

                item.setAid(aid);
                item.setComment(content);
                item.setTimestamp(upload_date);

                commentItemList.add(item);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        Collections.sort(commentItemList, new CommentItemDescComparator());

        cla.notifyDataSetChanged();
    }

    Comparator<CommentItem> commentComparator = new Comparator<CommentItem>()
    {
        @Override
        public int compare(CommentItem lhs, CommentItem rhs)
        {
            return lhs.getAid().contentEquals(rhs.getAid()) ? 0 : 1;
        }
    };

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        MyGcmListenerService.setOnGcmMessageListener(null);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}
