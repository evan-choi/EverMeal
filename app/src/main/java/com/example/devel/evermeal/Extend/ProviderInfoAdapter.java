package com.example.devel.evermeal.Extend;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.devel.evermeal.R;
import com.example.devel.evermeal.ResProvider.ProviderInfo;

import java.util.List;

public class ProviderInfoAdapter extends ArrayAdapter<ProviderInfo>
{
    private Context mContext;

    public ProviderInfoAdapter(Context context, int resource, List<ProviderInfo> objects)
    {
        super(context, resource, objects);

        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ProviderInfo item = getItem(position);

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        convertView = inflater.inflate(R.layout.provider_reg_item, parent, false);

        TextView tv = (TextView) convertView.findViewById(R.id.tvText);

        TextView tv_icon = (TextView) convertView.findViewById(R.id.tvIcon);
        if (item.Name != null && item.Name.length() > 0)
        {
            tv_icon.setText(item.Name.substring(0, 1));
            tv.setText(item.Name);
        }


        return convertView;
    }
}
