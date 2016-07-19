package com.example.devel.evermeal.Extend;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.devel.evermeal.R;
import com.example.devel.evermeal.ResProvider.ProviderInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by devel on 2016-07-17.
 */
public class AutoCompleteContactArrayAdapter extends
        ArrayAdapter<ProviderInfo> implements Filterable
{
    private Context mContext;
    private ArrayList<ProviderInfo> fullList;
    private ArrayList<ProviderInfo> mOriginalValues;
    private ArrayFilter mFilter;

    public AutoCompleteContactArrayAdapter(Context context, int resource, List<ProviderInfo> objects)
    {
        super(context, resource, objects);

        mContext = context;

        fullList = (ArrayList<ProviderInfo>) objects;
        mOriginalValues = new ArrayList<>(fullList);
    }

    public AutoCompleteContactArrayAdapter(Context context, int resource)
    {
        super(context, resource);

        mContext = context;

        fullList = new ArrayList<>();
        mOriginalValues = new ArrayList<>(fullList);
    }

    public boolean Contains(String q)
    {
        for (int i = 0; i < getCount(); i++)
        {
            if (getItem(i).Name.contentEquals(q))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getCount()
    {
        return fullList.size();
    }

    @Override
    public ProviderInfo getItem(int position)
    {
        return fullList.get(position);
    }

    @Override
    public void add(ProviderInfo object)
    {
        mOriginalValues.add(object);
    }

    @Override
    public void remove(ProviderInfo object)
    {
        mOriginalValues.remove(object);
    }

    @Override
    public void addAll(Collection<? extends ProviderInfo> collection)
    {
        for (ProviderInfo pi : collection)
        {
            mOriginalValues.add(pi);
        }
    }

    @Override
    public void addAll(ProviderInfo... items)
    {
        for (ProviderInfo pi : items)
        {
            mOriginalValues.add(pi);
        }
    }

    @Override
    public void clear()
    {
        fullList.clear();
        mOriginalValues.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ProviderInfo item = fullList.get(position);

        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        convertView = inflater.inflate(R.layout.provider_item, parent, false);

        if (!item.IsSurface || item.ShowContent)
        {
            TextView tv = (TextView)convertView.findViewById(R.id.tvText);
            tv.setText(item.Name);

            ProgressBar pb = (ProgressBar)convertView.findViewById(R.id.progressBar);
            pb.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public Filter getFilter()
    {
        if (mFilter == null)
        {
            mFilter = new ArrayFilter();
        }

        return mFilter;
    }

    private class ArrayFilter extends Filter
    {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix)
        {
            FilterResults results = new FilterResults();

            if (prefix == null || prefix.length() == 0)
            {
                synchronized (lock)
                {
                    ArrayList<ProviderInfo> list = new ArrayList<>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            }
            else
            {
                final String prefixString = prefix.toString().toLowerCase();
                ArrayList<ProviderInfo> values = mOriginalValues;

                int count = values.size();

                ArrayList<ProviderInfo> newValues = new ArrayList<>(count);

                for (int i = 0; i < count; i++)
                {
                    ProviderInfo item = values.get(i);

                    if (item.IsSurface | item.Name.toLowerCase().contains(prefixString))
                    {
                        newValues.add(item);
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            if (results.values != null)
            {
                fullList = (ArrayList<ProviderInfo>) results.values;
            }
            else
            {
                fullList = new ArrayList<>();
            }

            if (results.count > 0)
            {
                notifyDataSetChanged();
            }
            else
            {
                notifyDataSetInvalidated();
            }
        }
    }
}
