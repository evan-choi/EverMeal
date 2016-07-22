package com.example.devel.evermeal.Widget.listviewfeed.data;

import java.util.Comparator;

/**
 * Created by devel on 2016-07-22.
 */
public class FeedItemAscComparator implements Comparator<FeedItem>
{
    @Override
    public int compare(FeedItem lhs, FeedItem rhs)
    {
        Long l1 = Long.parseLong(lhs.getUpload_date());
        Long l2 = Long.parseLong(rhs.getUpload_date());

        return l1 < l2 ? -1 : l1 > l2 ? 1 : 0;
    }
}
