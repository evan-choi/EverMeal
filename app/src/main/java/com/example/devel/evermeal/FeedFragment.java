package com.example.devel.evermeal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Cache;
import com.example.devel.evermeal.Extend.FeedOnClickListener;
import com.example.devel.evermeal.Extend.FragmentEx;
import com.example.devel.evermeal.ResProvider.ProviderInfo;
import com.example.devel.evermeal.ResProvider.ProviderManager;
import com.example.devel.evermeal.Widget.Rate.RatePopupManager;
import com.example.devel.evermeal.Widget.listviewfeed.adapter.FeedListAdapter;
import com.example.devel.evermeal.Widget.listviewfeed.app.AppController;
import com.example.devel.evermeal.Widget.listviewfeed.data.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends FragmentEx
{
    private OnFragmentInteractionListener mListener;

    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";

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
        public void onClick(View v, FeedItem item)
        {
            RatePopupManager.getInstance(getContext()).showAsDropDown(v);
        }

        @Override
        public void onClick(View v)
        {

        }
    };

    private void InitFeedList(View view)
    {
        listView = (ListView)view.findViewById(R.id.list);

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

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);

        String test_data = "[\n" +
                "  {\n" +
                "    \"aid\": \"1469027276\",\n" +
                "    \"content\": \"{\\\"dinner\\\": {\\\"type\\\": \\\"2\\\", \\\"dishes\\\": [{\\\"name\\\": \\\"\\\\ud604\\\\ubbf8\\\\ubc25\\\", \\\"types\\\": []}, {\\\"name\\\": \\\"\\\\ucc38\\\\uce58\\\\uae40\\\\uce58\\\\ucc0c\\\\uac1c\\\", \\\"types\\\": [5, 6, 9, 13]}, {\\\"name\\\": \\\"\\\\ub3d9\\\\uadf8\\\\ub791\\\\ub561\\\", \\\"types\\\": [1, 5, 6, 10, 13]}, {\\\"name\\\": \\\"\\\\uc624\\\\uc9d5\\\\uc5b4\\\\uc219\\\\ud68c\\\", \\\"types\\\": [5, 6, 13]}, {\\\"name\\\": \\\"\\\\uae4d\\\\ub450\\\\uae30\\\", \\\"types\\\": [9, 13]}, {\\\"name\\\": \\\"\\\\uacfc\\\\uc77c\\\\ud654\\\\ucc44\\\", \\\"types\\\": [5, 11, 13]}]}, \\\"breakfast\\\": {\\\"type\\\": \\\"0\\\", \\\"dishes\\\": []}, \\\"date\\\": \\\"2016-7-18\\\", \\\"lunch\\\": {\\\"type\\\": \\\"2\\\", \\\"dishes\\\": [{\\\"name\\\": \\\"\\\\uadc0\\\\ub9ac\\\\ubc25\\\", \\\"types\\\": []}, {\\\"name\\\": \\\"\\\\ud06c\\\\ub85c\\\\ucf13\\\", \\\"types\\\": [1, 2, 5, 6, 10, 13]}, {\\\"name\\\": \\\"\\\\uc5b4\\\\ubb35\\\\uad6d\\\", \\\"types\\\": [1, 5, 6, 13]}, {\\\"name\\\": \\\"\\\\ub3c8\\\\uc721\\\\ubcf4\\\\uc308/\\\\uc308\\\\uc7a5\\\", \\\"types\\\": [5, 6, 10, 13]}, {\\\"name\\\": \\\"\\\\ubb34\\\\ub9d0\\\\ub7ad\\\\uc774\\\\ubb34\\\\uce68\\\", \\\"types\\\": [5, 6, 13]}, {\\\"name\\\": \\\"\\\\ubc30\\\\ucd94\\\\uae40\\\\uce58\\\", \\\"types\\\": [9, 13]}]}}\",\n" +
                "    \"dependency\": \"\",\n" +
                "    \"image_url\": \"\",\n" +
                "    \"type\": 0,\n" +
                "    \"upload_date\": 1469027276,\n" +
                "    \"uploader\": \"Z29lLmdvSjEwMDAwMDc3OA==\"\n" +
                "  }\n" +
                "]";

        try
        {
            JSONArray arr = new JSONArray(test_data);

            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject obj = arr.getJSONObject(i);
                String token = obj.getString("uploader");

                ProviderInfo pi = ProviderManager.ProviderFromToken(token);

                pi = new ProviderInfo("장곡고등학교", token, 0);

                if (pi != null)
                {
                    FeedItem item = new FeedItem();

                    item.setName(pi.Name);
                    item.setAid(obj.getInt("aid"));
                    item.setDependency(obj.getString("dependency"));
                    item.setImage_url(obj.getString("image_url"));
                    item.setType(obj.getInt("type"));
                    item.setUpload_date(obj.getString("upload_date"));
                    item.setUploader(obj.getString("uploader"));

                    JSONObject mealObj = new JSONObject(obj.getString("content"));

                    item.setContent(mealToContent(mealObj, pi));
                    item.setTextCenter(true);

                    feedItems.add(item);
                }
            }

            listAdapter.notifyDataSetChanged();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        /*
        if (entry != null)
        {
            try
            {
                String data = new String(entry.data, "UTF-8");
                try
                {
                    parseJsonFeed(new JSONObject(data));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>()
            {

                @Override
                public void onResponse(JSONObject response)
                {
                    if (response != null)
                    {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
        */
    }

    private String mealToContent(JSONObject meal, ProviderInfo uploader)
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
                    Boolean a =  SettingFragment.getAllergy(this.getContext(), types.optInt(j));

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
        /*
        Intent intent = new Intent(getActivity(), FeedActivity.class);
        intent.putExtra("profilePic", item.getProfilePic());
        intent.putExtra("name", item.getName());
        intent.putExtra("timestamp", item.getTimeStamp());
        intent.putExtra("txtStatusMsg", item.getStatus());
        intent.putExtra("txtUrl", item.getUrl());
        intent.putExtra("feedImage1", item.getImge());

        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        */
    }

    private void parseJsonFeed(JSONObject response)
    {
        /*
        try
        {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++)
            {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);

                feedItems.add(item);
            }

            listAdapter.notifyDataSetChanged();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        */
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View layout = inflater.inflate(R.layout.fragment_feed, container, false);

        // 피드 리스트 설정
        InitFeedList(layout);

        FloatingActionButton btnWrite = (FloatingActionButton)layout.findViewById(R.id.btnWrite);
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
