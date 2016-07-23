package com.devjy.devel.evermeal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devjy.devel.evermeal.Auth.UserManager;
import com.devjy.devel.evermeal.Extend.FeedOnClickListener;
import com.devjy.devel.evermeal.Extend.FragmentEx;
import com.devjy.devel.evermeal.ResProvider.ProviderInfo;
import com.devjy.devel.evermeal.ResProvider.ProviderManager;
import com.devjy.devel.evermeal.Widget.Rate.OnCloseListener;
import com.devjy.devel.evermeal.Widget.Rate.RatePopupManager;
import com.devjy.devel.evermeal.Widget.listviewfeed.adapter.FeedListAdapter;
import com.devjy.devel.evermeal.Widget.listviewfeed.app.AppController;
import com.devjy.devel.evermeal.Widget.listviewfeed.data.FeedItem;
import com.devjy.devel.evermeal.Widget.listviewfeed.data.FeedItemDescComparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedFragment extends FragmentEx
{
    private OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout refreshLayout;
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;

    private String URL_FEED_RATE = "https://evermeal.herokuapp.com/feed/rate";
    private String URL_FEED = "https://evermeal.herokuapp.com/feed";//"http://api.androidhive.info/feed/feed.json";

    public FeedFragment()
    {
    }

    public static FeedFragment newInstance(String title)
    {
        FeedFragment fragment = new FeedFragment();

        fragment.setTitle(title);

        return fragment;
    }

    FeedOnClickListener feedReview = new FeedOnClickListener()
    {
        @Override
        public void onClick(View view, FeedItem item)
        {
            startFeedActivity(item);
        }

        @Override
        public void onClick(View v)
        {

        }
    };

    FeedOnClickListener feedRate = new FeedOnClickListener()
    {
        @Override
        public void onClick(final View v, final FeedItem item)
        {
            if (item.getU_rate().contentEquals("0"))
                RatePopupManager.getInstance(getContext(), new OnCloseListener()
                {
                    @Override
                    public void onClose(int rate)
                    {
                        SendRate(rate, item);
                    }
                }).showAsDropDown(v);
            else
                Toast.makeText(getContext(), "이미 참여하셨습니다", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(View v)
        {

        }
    };

    private void SendRate(final int rate, final FeedItem item)
    {
        JSONObject post = new JSONObject();

        try
        {
            post.accumulate("aid", item.getAid());
            post.accumulate("sid", UserManager.UserInfo.ID);
            post.accumulate("rate", rate);
        }
        catch (Exception ex)
        {
        }

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST,
                URL_FEED_RATE, post, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    if (response.getBoolean("result"))
                    {
                        item.setU_rate(String.valueOf(rate));
                        item.setRate(response.getString("rate"));

                        item.Validate("rate");
                    }
                    else
                    {
                        Toast.makeText(getContext(), "이미 참여하셨습니다", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex)
                {
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(), "별점을 주는도중 오류가 발생하였습니다", Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    private void InitFeedList(View view)
    {
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refreshLayout);
        listView = (ListView) view.findViewById(R.id.list);

        refreshLayout.setOnRefreshListener(onRefresh);

        feedItems = new ArrayList<>();

        listAdapter = new FeedListAdapter((Activity) view.getContext(), feedItems);
        listView.setAdapter(listAdapter);

        listAdapter.setRateClickListener(feedRate);
        listAdapter.setReviewClickListener(feedReview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                FeedItem item = feedItems.get(position);
                startFeedActivity(item);
            }
        });

        Refresh();
    }

    private void Refresh()
    {
        long maxDate = 0;

        for (FeedItem item : feedItems)
        {
            long cDate = Long.parseLong(item.getUpload_date());
            if (cDate > maxDate)
                maxDate = cDate;
        }

        String url = URL_FEED + "?date=" + String.valueOf(maxDate) + "&sid=" + UserManager.UserInfo.ID;

        JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>()
        {

            @Override
            public void onResponse(JSONArray response)
            {
                if (response != null)
                {
                    parseJsonFeed(response);
                }

                refreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                refreshLayout.setRefreshing(false);
            }
        });

        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    SwipeRefreshLayout.OnRefreshListener onRefresh = new SwipeRefreshLayout.OnRefreshListener()
    {
        @Override
        public void onRefresh()
        {
            Refresh();
        }
    };

    private String parseJsonFeedContent(JSONObject meal, ProviderInfo uploader)
    {
        try
        {
            StringBuilder builder = new StringBuilder();

            JSONObject lunch = meal.getJSONObject("lunch");
            JSONArray dishes = lunch.getJSONArray("dishes");

            for (int i = 0; i < dishes.length(); i++)
            {
                JSONObject dish = dishes.getJSONObject(i);
                JSONArray types = dish.optJSONArray("types");

                String name = dish.getString("name");
                Boolean highlight = false;

                for (int j = 0; j < types.length(); ++j)
                {
                    Boolean a = SettingFragment.getAllergy(this.getContext(), types.optInt(j));

                    if (a)
                    {
                        highlight = true;
                        break;
                    }
                }

                if (highlight)
                {
                    builder.append("<#e74c3c>" + name + "</c>\r\n");
                }
                else
                {
                    builder.append(name + "\r\n");
                }
            }

            return builder.toString();

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private void startFeedActivity(FeedItem item)
    {
        Intent intent = new Intent(getActivity(), FeedActivity.class);

        intent.putExtra("type", item.getType());
        intent.putExtra("content", item.getContent());
        intent.putExtra("dependency", item.getDependency());
        intent.putExtra("image_url", item.getImage_url());
        intent.putExtra("upload_date", item.getUpload_date());
        intent.putExtra("uploader", item.getUploader());
        intent.putExtra("name", item.getName());
        intent.putExtra("aid", item.getAid());
        intent.putExtra("textCenter", item.getTextCenter());

        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    private void parseJsonFeed(JSONArray arr)
    {
        try
        {
            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject obj = arr.getJSONObject(i);
                String token = obj.getString("uploader");

                ProviderInfo pi = ProviderManager.ProviderFromToken(token);

                if (pi != null)
                {
                    FeedItem item = new FeedItem();

                    item.setName(pi.Name);
                    item.setAid(obj.getString("aid"));
                    item.setDependency(obj.getString("dependency"));
                    item.setImage_url(obj.getString("image_url"));
                    item.setType(obj.getInt("type"));
                    item.setUpload_date(obj.getString("upload_date"));
                    item.setUploader(obj.getString("uploader"));
                    item.setRate(obj.getString("rate"));
                    item.setU_rate(obj.getString("u_rate"));

                    JSONObject mealObj = new JSONObject(obj.getString("content"));

                    item.setContent(parseJsonFeedContent(mealObj, pi));
                    item.setTextCenter(true);

                    feedItems.add(item);
                }

                Collections.sort(feedItems, new FeedItemDescComparator());
            }

            listAdapter.notifyDataSetChanged();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View layout = inflater.inflate(R.layout.fragment_feed, container, false);

        // 피드 리스트 설정
        InitFeedList(layout);
/*
        FloatingActionButton btnWrite = (FloatingActionButton) layout.findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity activity = getActivity();

                Intent upload = new Intent(activity, UploadActivity.class);
                activity.startActivity(upload);
            }
        });
*/
        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
