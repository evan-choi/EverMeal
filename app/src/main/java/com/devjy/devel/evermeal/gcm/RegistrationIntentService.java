package com.devjy.devel.evermeal.gcm;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.devjy.devel.evermeal.R;
import com.devjy.devel.evermeal.Utils.QuickstartPreferences;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService
{

    private static final String TAG = "RegistrationIntentService";

    public RegistrationIntentService()
    {
        super(TAG);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onHandleIntent(Intent intent)
    {
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(new Intent(QuickstartPreferences.REGISTRATION_GENERATING));

        InstanceID instanceID = InstanceID.getInstance(this);
        String token = null;
        try
        {
            synchronized (TAG)
            {
                String default_senderId = getString(R.string.gcm_defaultSenderId);
                String scope = GoogleCloudMessaging.INSTANCE_ID_SCOPE;
                token = instanceID.getToken(default_senderId, scope, null);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}