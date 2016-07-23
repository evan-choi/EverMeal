package com.devjy.devel.evermeal.gcm;

import android.os.Bundle;

/**
 * Created by devel on 2016-07-22.
 */
public interface OnGcmMessageListener
{
    boolean onMessageReceived(String type, Bundle bundle);
}
