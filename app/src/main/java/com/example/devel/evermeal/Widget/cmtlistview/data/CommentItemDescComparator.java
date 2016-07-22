package com.example.devel.evermeal.Widget.cmtlistview.data;

import org.w3c.dom.Comment;

import java.util.Comparator;

/**
 * Created by devel on 2016-07-22.
 */
public class CommentItemDescComparator implements Comparator<CommentItem>
{
    @Override
    public int compare(CommentItem lhs, CommentItem rhs)
    {
        Long l1 = Long.parseLong(lhs.getTimestamp());
        Long l2 = Long.parseLong(rhs.getTimestamp());

        return l1 > l2 ? -1 : l1 < l2 ? 1 : 0;
    }
}
