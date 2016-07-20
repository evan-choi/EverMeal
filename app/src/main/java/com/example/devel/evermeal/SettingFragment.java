package com.example.devel.evermeal;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.devel.evermeal.Auth.UserManager;
import com.example.devel.evermeal.Extend.IFragmentTitle;

public class SettingFragment extends PreferenceFragmentCompat implements IFragmentTitle
{
    private OnFragmentInteractionListener mListener;

    private static SharedPreferences prefs;
    private String title;

    public SettingFragment()
    {
    }

    public static SettingFragment newInstance(String title)
    {
        SettingFragment fragment = new SettingFragment();

        fragment.setTitle(title);

        return fragment;
    }

    @Override
    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s)
    {
        addPreferencesFromResource(Integer.valueOf(R.layout.fragment_setting));
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
    }

    public static boolean getAllergy(Context context, int i)
    {
        if (i >= 1 && i <= 18)
        {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("allergy_" + i, false);
        }

        return false;
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

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
