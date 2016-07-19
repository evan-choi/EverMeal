package com.example.devel.evermeal;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.devel.evermeal.Auth.UserManager;
import com.example.devel.evermeal.Extend.AutoCompleteContactArrayAdapter;
import com.example.devel.evermeal.Extend.FragmentEx;
import com.example.devel.evermeal.Extend.ProviderInfoAdapter;
import com.example.devel.evermeal.Extend.RichTextView;
import com.example.devel.evermeal.ResProvider.ProviderInfo;
import com.example.devel.evermeal.ResProvider.ProviderManager;
import com.example.devel.evermeal.Utils.StringUtils;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class ProviderFragment extends FragmentEx
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ProviderInfo loading_provider = new ProviderInfo(true) {{ Name = "loading"; }};
    private ProviderInfo message_provider = new ProviderInfo(true) {{ ShowContent = true; }};

    private Queue<RegisterProviderTask> provTaskQueue;

    private AutoCompleteTextView actv;
    private SearchTask searchTask;
    private AutoCompleteContactArrayAdapter searchAdapter;
    private boolean isSearching;
    private String lastQuery;

    private ProviderInfoAdapter piAdapter;

    private OnFragmentInteractionListener mListener;

    public ProviderFragment()
    {
        provTaskQueue = new LinkedList<>();
    }

    // TODO: Rename and change types and number of parameters
    public static ProviderFragment newInstance(String title, String param1, String param2)
    {
        ProviderFragment fragment = new ProviderFragment();

        fragment.setTitle(title);

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View layout = inflater.inflate(R.layout.fragment_, container, false);

        InitSearch(layout);
        InitProvList(layout);

        return layout;
    }

    private void InitSearch(View layout)
    {
        actv = (AutoCompleteTextView)layout.findViewById(R.id.editText);
        actv.addTextChangedListener(SearchWatcher);

        searchAdapter = new AutoCompleteContactArrayAdapter(layout.getContext(), android.R.layout.select_dialog_item);
        actv.setAdapter(searchAdapter);

        actv.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                showDropDown();
                return false;
            }
        });

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                SelectProviderInfo(searchAdapter.getItem(position));
            }
        });
    }

    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }

    private void InitProvList(View layout)
    {
        ListView listView = (ListView)layout.findViewById(R.id.provList);

        piAdapter = new ProviderInfoAdapter(layout.getContext(), R.layout.provider_reg_item, ProviderManager.Items);
        listView.setAdapter(piAdapter);
    }

    private final TextWatcher SearchWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            String q = s.toString().trim();
            if (lastQuery != null)
                Log.v("last_q", lastQuery);

            if (q != null && q.length() > 0
                    && (lastQuery == null || !q.contentEquals(lastQuery))
                    && !searchAdapter.Contains(q) && StringUtils.IsCompletionString(q))
            {
                lastQuery = q;
                BeginSearch(q);
            }
        }

        @Override
        public void afterTextChanged(Editable s)
        {
        }
    };

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

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void SelectProviderInfo(final ProviderInfo info)
    {
        final ProviderFragment fragment = this;

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == DialogInterface.BUTTON_POSITIVE)
                {
                    if (!ProviderManager.HasProvider(info))
                    {
                        provTaskQueue.add(new RegisterProviderTask(fragment, info));
                        BeginProviderRegister();

                        actv.setText("");
                        SetSearchAdapterList();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "이미 추가된 업체입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setMessage("'" + info.Name + "'를 추가하시겠습니까?")
                .setPositiveButton("추가", dialogClickListener)
                .setNegativeButton("취소", dialogClickListener)
                .show();
    }

    //region PROVIDER_INFO
    protected void BeginProviderRegister()
    {
        if (provTaskQueue.size() == 1)
        {
            RegisterProviderTask task = provTaskQueue.peek();

            Log.i("Register", "execute");
            task.execute(task.ProviderInfo);
        }
    }

    protected void EndProviderRegister(boolean result)
    {
        if (result)
        {
            ProviderInfo pi = provTaskQueue.poll().ProviderInfo;

            ProviderManager.Add(pi);
            ProviderManager.Commit();

            piAdapter.notifyDataSetChanged();

            if (provTaskQueue.size() > 0)
                BeginProviderRegister();
        }
        else
        {
            Toast.makeText(getContext(), "추가하지 못했습니다. (-1)", Toast.LENGTH_SHORT).show();
        }
    }

    private class RegisterProviderTask extends AsyncTask<ProviderInfo, Integer, Boolean>
    {
        private final int timeout_time = 5000;

        private boolean canceled = false;
        private ProviderFragment parent;

        private HttpClient client;
        private HttpPost http_post;
        public ProviderInfo ProviderInfo;

        public RegisterProviderTask(ProviderFragment parent, ProviderInfo pi)
        {
            canceled = false;

            this.ProviderInfo = pi;
            this.parent = parent;
        }

        @Override
        protected Boolean doInBackground(ProviderInfo... params)
        {
            ProviderInfo q = params[0];
            Log.i("Register", "step 1");
            if (q != null)
            {
                Log.i("Register", "step 2");
                String url = "https://evermeal.herokuapp.com/api/provider";

                client = new DefaultHttpClient();
                client.getParams().setParameter("http.protocol.expect-continue", false);
                client.getParams().setParameter("http.connection.timeout", timeout_time);
                client.getParams().setParameter("http.socket.timeout", timeout_time);

                try
                {
                    URI uri = new URI(url);

                    http_post = new HttpPost(uri);

                    JSONObject post_json = new JSONObject();
                    post_json.accumulate("sid", String.valueOf(UserManager.UserInfo.ID));
                    post_json.accumulate("prov_token", q.Token);

                    http_post.setEntity(new StringEntity(post_json.toString()));
                    http_post.setHeader("Content-type", "application/json");

                    HttpResponse response = client.execute(http_post);
                    HttpEntity entity = response.getEntity();
                    Log.i("Register", "step 3");
                    String str = StringUtils.getStringFromInputStream(entity.getContent());

                    Log.i("Register", str);

                    JSONObject json = new JSONObject(str);

                    return (json.has("prov_token") && json.getString("prov_token").contentEquals(q.Token));
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                    http_post.releaseConnection();
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);
            EndProviderRegister(aBoolean);
        }
    }
    //endregion
//region SEARCH
    protected void BeginSearch(String q)
    {
        Log.v("BeginSearch", q);

        SetSearching();

        if (searchTask != null)
        {
            searchTask.cancel();
        }

        searchTask = new SearchTask(this);
        searchTask.execute(q);
    }

    protected void EndSearch(List<ProviderInfo> results)
    {
        isSearching = false;

        if (results != null && results.size() > 0)
        {
            SetSearchAdapterList(results);
        }
        else
        {
            SetSearchMessage("검색결과가 없습니다");
        }

        searchAdapter.getFilter().filter(actv.getText());
    }

    protected void SetSearchAdapterList(ProviderInfo... items)
    {
        searchAdapter.clear();
        searchAdapter.addAll(items);
        searchAdapter.notifyDataSetChanged();

        showDropDown();
    }

    protected void SetSearchAdapterList(List<ProviderInfo> items)
    {
        searchAdapter.clear();
        searchAdapter.addAll(items);
        searchAdapter.notifyDataSetChanged();

        showDropDown();
    }

    protected void SetSearching()
    {
        if (!isSearching)
        {
            isSearching = true;
            SetSearchAdapterList(loading_provider);
        }
    }

    protected void SetSearchMessage(String text)
    {
        message_provider.Name = text;
        SetSearchAdapterList(message_provider);
    }

    private class SearchTask extends AsyncTask<String, Integer, List<ProviderInfo>>
    {
        private final int timeout_time = 5000;

        private boolean canceled = false;
        private ProviderFragment parent;

        private HttpClient client;
        private HttpGet http_get;

        public SearchTask(ProviderFragment parent)
        {
            canceled = false;
            this.parent = parent;
        }

        @Override
        protected List<ProviderInfo> doInBackground(String... params)
        {
            String q = params[0];

            if (q.length() > 0)
            {
                String query = "{\"filters\":[{\"name\":\"name\",\"op\":\"like\",\"val\":\"%" + q + "%\"}]}";
                String url = "https://evermeal.herokuapp.com/api/provider_info?q=" + URLEncoder.encode(query);

                client = new DefaultHttpClient();
                client.getParams().setParameter("http.protocol.expect-continue", false);
                client.getParams().setParameter("http.connection.timeout", timeout_time);
                client.getParams().setParameter("http.socket.timeout", timeout_time);

                try
                {
                    URI uri = new URI(url);

                    http_get = new HttpGet(uri);

                    HttpResponse response = client.execute(http_get);
                    HttpEntity entity = response.getEntity();

                    String str = StringUtils.getStringFromInputStream(entity.getContent());

                    JSONObject json = new JSONObject(str);

                    if (json.has("objects"))
                    {
                        List<ProviderInfo> results = new ArrayList<>();

                        JSONArray arr = json.getJSONArray("objects");

                        for (int i = 0; i < arr.length(); i++)
                        {
                            JSONObject item = arr.getJSONObject(i);

                            results.add(new ProviderInfo(
                                    item.getString("name"),
                                    item.getString("token"),
                                    item.getInt("type")
                            ));
                        }

                        return results;
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                    http_get.releaseConnection();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<ProviderInfo> providerInfos)
        {
            if (!canceled)
            {
                parent.EndSearch(providerInfos);
            }
        }

        public void cancel()
        {
            try
            {
                http_get.abort();
                http_get.releaseConnection();
                client.getConnectionManager().shutdown();
            }
            catch (Exception ex)
            {
            }

            canceled = true;
        }
    }
    //endregion

    private void showDropDown()
    {
        try
        {
            if (isRunning(getActivity()))
                if (!actv.isPopupShowing()) actv.showDropDown();
        }
        catch (Exception ex)
        {
        }
    }
}
