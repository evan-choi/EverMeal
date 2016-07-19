package com.example.devel.evermeal.Extend;


import android.support.v4.app.Fragment;

/**
 * Created by devel on 2016-07-14.
 */

public class FragmentEx extends Fragment implements IFragmentTitle
{
    private String title;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
