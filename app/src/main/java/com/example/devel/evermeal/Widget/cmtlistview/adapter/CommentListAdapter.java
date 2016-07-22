package com.example.devel.evermeal.Widget.cmtlistview.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.devel.evermeal.R;
import com.example.devel.evermeal.Widget.cmtlistview.data.CommentItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by devel on 2016-07-21.
 */
public class CommentListAdapter extends ArrayAdapter<CommentItem>
{
    private Context context;

    public CommentListAdapter(Context context, int resource, List<CommentItem> objects)
    {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        CommentViewHolder viewHolder;
        CommentItem item = getItem(position);

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.comment_item, parent, false);

            viewHolder = new CommentViewHolder();
            viewHolder.tvText = (TextView)convertView.findViewById(R.id.tvText);
            viewHolder.timestamp = (TextView)convertView.findViewById(R.id.timestamp);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (CommentViewHolder)convertView.getTag();
        }

        viewHolder.tvText.setText(item.getComment());

        Date d = new Date(1970, 1, 1);
        d.setTime(Long.parseLong(item.getTimestamp()) * 1000);
        String dstr = new SimpleDateFormat("M월 dd일 h시 m분").format(d);

        viewHolder.timestamp.setText(dstr);

        return convertView;
    }
}
