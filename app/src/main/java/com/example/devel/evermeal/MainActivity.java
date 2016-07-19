package com.example.devel.evermeal;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devel.evermeal.Auth.UserManager;
import com.example.devel.evermeal.Extend.AppExActivity;
import com.example.devel.evermeal.Extend.FragmentEx;
import com.example.devel.evermeal.Extend.IFragmentTitle;
import com.example.devel.evermeal.ResProvider.ProviderManager;
import com.example.devel.evermeal.Utils.PrefHolder;
import com.example.devel.evermeal.Utils.QuickstartPreferences;
import com.example.devel.evermeal.gcm.GcmManager;
import com.example.devel.evermeal.gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppExActivity
        implements FeedFragment.OnFragmentInteractionListener,
        ProviderFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener
{

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableBackPressPrevent();

        // 툴바 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // 뷰 페이저 설정
        InitViewPager();

        // 유저 데이터 컨트롤
        UserManager.SetPrefHolder(new PrefHolder(this));

        // gcm 컨트롤
        GcmManager.SetPrefHolder(UserManager.Holder);

        // 프로바이더 컨트롤
        ProviderManager.SetPrefHolder(UserManager.Holder);
        ProviderManager.Load();

        startActivityForResult(new Intent(this, SplashActivity.class), 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 200)
        {
            if (!UserManager.HasUserData())
                finish();
            else
                UserManager.LoadUserData();

            registBroadcastReceiver();
            getInstanceIdToken();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);

        bm.registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        bm.registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        bm.registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    @Override
    protected void onPause()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public void getInstanceIdToken()
    {
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }


    public void registBroadcastReceiver()
    {
        final MainActivity activity = this;

        mRegistrationBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();

                if (action.equals(QuickstartPreferences.REGISTRATION_READY))
                {
                }
                else if (action.equals(QuickstartPreferences.REGISTRATION_GENERATING))
                {
                }
                else if (action.equals(QuickstartPreferences.REGISTRATION_COMPLETE))
                {
                    String token = intent.getStringExtra("token");

                }

            }
        };
    }

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                //Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void InitViewPager()
    {
        final List<IFragmentTitle> fragments = new ArrayList<>();
        fragments.add(FeedFragment.newInstance("급식", null, null));
        fragments.add(ProviderFragment.newInstance("제공업체", null, null));
        fragments.add(SettingFragment.newInstance("설정", null, null));

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(this.getSupportFragmentManager())
        {
            @Override
            public CharSequence getPageTitle(int position)
            {
                return fragments.get(position).getTitle();
            }

            @Override
            public Fragment getItem(int position)
            {
                return (Fragment)fragments.get(position);
            }

            @Override
            public int getCount()
            {
                return fragments.size();
            }
        };

        ViewPager pager = (ViewPager) findViewById(R.id.main_viewpager);
        SmartTabLayout tab = (SmartTabLayout) findViewById(R.id.main_viewpagertab);

        assert pager != null;
        assert tab != null;

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        pager.setAdapter(adapter);
        tab.setViewPager(pager);
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}