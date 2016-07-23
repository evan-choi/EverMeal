package com.devjy.devel.evermeal.gcm;

import android.os.AsyncTask;

import com.devjy.devel.evermeal.Auth.UserManager;
import com.devjy.devel.evermeal.Utils.PrefHolder;
import com.devjy.devel.evermeal.Utils.StringUtils;

import org.json.JSONObject;

import java.net.URI;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by devel on 2016-07-18.
 */
public class GcmManager
{
    private static String token;

    public static PrefHolder Holder;

    public static void SetPrefHolder(PrefHolder holder)
    {
        GcmManager.Holder = holder;
    }

    public static String getToken()
    {
        return Holder.getValue("gcm_token", "-1");
    }

    public static void setToken(String token)
    {
        GcmManager.token = token;
        Holder.setValue("gcm_token", token);
    }

    public static void register()
    {
        RegisterGcmTask task = new RegisterGcmTask();

        task.execute(token);
    }

    private static class RegisterGcmTask extends AsyncTask<String, Integer, Boolean>
    {
        private final int timeout_time = 5000;

        private boolean canceled = false;

        private HttpClient client;
        private HttpPost http_post;

        @Override
        protected Boolean doInBackground(String... params)
        {
            String q = params[0];

            if (q != null)
            {
                String url = "https://evermeal.herokuapp.com/api/gcm";

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
                    post_json.accumulate("token", q);

                    http_post.setEntity(new StringEntity(post_json.toString()));
                    http_post.setHeader("Content-type", "application/json");

                    HttpResponse response = client.execute(http_post);
                    HttpEntity entity = response.getEntity();

                    String str = StringUtils.getStringFromInputStream(entity.getContent());

                    JSONObject json = new JSONObject(str);

                    return (json.has("token") && json.getString("token").contentEquals(q));
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

        }
    }
}
