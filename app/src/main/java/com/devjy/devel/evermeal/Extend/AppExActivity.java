package com.devjy.devel.evermeal.Extend;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by devel on 2016-07-13.
 */
public class AppExActivity extends AppCompatActivity
{
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private boolean backpress = false;

    protected void Delay(int ms, Runnable callback)
    {
        Handler handler = new Handler();

        handler.postDelayed(callback, ms);
    }

    protected void enableBackPressPrevent()
    {
        backpress = true;
    }

    protected void disableBackPressPrevent()
    {
        backpress = false;
    }

    @Override
    public void onBackPressed()
    {
        if (!backpress)
            return;

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}