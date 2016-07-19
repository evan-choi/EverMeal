package com.example.devel.evermeal.Extend;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.devel.evermeal.R;
import com.example.devel.evermeal.Utils.FontManager;

/**
 * Created by devel on 2016-07-13.
 */
public class TextViewEx extends TextView {
    public TextViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);

        InitAttrs(context, attrs);
    }

    public TextViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        InitAttrs(context, attrs);
    }

    public TextViewEx(Context context) {
        super(context);
    }

    private void InitAttrs(Context context, AttributeSet attrs)
    {
        if (!isInEditMode())
        {
            TypedArray arr = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.TextViewEx);

            String fontName = arr.getString(R.styleable.TextViewEx_font);

            Typeface font = FontManager.Load(this.getContext(), fontName);
            this.setTypeface(font);
        }
    }
}
