package com.example.devel.evermeal.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by devel on 2016-07-14.
 */
public class PrefHolder
{
    private Context context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private boolean IsTransaction;

    public PrefHolder(Context parent)
    {
        this.context = parent;

        this.pref = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        this.editor = pref.edit();
    }

    public String getValue(String key, String defValue)
    {
        return pref.getString(key, defValue);
    }

    public void setValue(String key, String value)
    {
        editor.putString(key, value);
        this.commit();
    }

    public Set<String> getValues(String key, Set<String> defValues)
    {
        return pref.getStringSet(key, defValues);
    }

    public void setValues(String key, Set<String> values)
    {
        editor.putStringSet(key, values);
        this.commit();
    }

    public void remove(String key)
    {
        editor.remove(key);
        this.commit();
    }

    public void clear()
    {
        this.commit();
    }

    public void BeginTransaction()
    {
        IsTransaction = true;
    }

    public void EndTransaction()
    {
        IsTransaction = false;
        this.commit();
    }

    private void commit()
    {
        if (!IsTransaction)
            editor.commit();
    }
}