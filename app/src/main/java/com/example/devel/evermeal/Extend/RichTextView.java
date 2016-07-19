package com.example.devel.evermeal.Extend;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RichTextView extends TextView
{
    public RichTextView(Context context)
    {
        super(context);
    }

    public RichTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type)
    {
        if (text.length() == 0)
        {
            super.setText(text, type);
            return;
        }

        Pattern p = Pattern.compile("\\<(#\\w{6})\\>(.*?)\\<\\/c\\>");

        String cleanText = text.toString()
                .replaceAll("(\\<#\\w{6}\\>)", "")
                .replace("</c>", "");

        Spannable.Factory factory = Spannable.Factory.getInstance();
        Spannable span = factory.newSpannable(cleanText);

        int cidx = 0;
        Matcher m = p.matcher(text);

        for (MatchResult mr : allMatches(p, text.toString()))
        {
            try
            {
                int d = Color.parseColor(mr.group(1));
                int a = mr.start() - cidx;
                int b = a + mr.group(2).length();

                span.setSpan(new ForegroundColorSpan(d), a, b, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                cidx += 13;
            }
            catch (Exception ex)
            {
            }
        }

        super.setText(span, type);
    }

    public static Iterable<MatchResult> allMatches(final Pattern p, final CharSequence input)
    {
        return new Iterable<MatchResult>()
        {
            public Iterator<MatchResult> iterator()
            {
                return new Iterator<MatchResult>()
                {
                    final Matcher matcher = p.matcher(input);
                    MatchResult pending;

                    public boolean hasNext()
                    {
                        if (pending == null && matcher.find())
                        {
                            pending = matcher.toMatchResult();
                        }
                        return pending != null;
                    }

                    public MatchResult next()
                    {
                        if (!hasNext())
                        {
                            throw new NoSuchElementException();
                        }

                        MatchResult next = pending;
                        pending = null;
                        return next;
                    }

                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
