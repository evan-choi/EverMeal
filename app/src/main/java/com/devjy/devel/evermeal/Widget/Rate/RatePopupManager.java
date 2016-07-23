package com.devjy.devel.evermeal.Widget.Rate;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.devjy.devel.evermeal.R;
import com.sackcentury.shinebuttonlib.ShineButton;

/**
 * Created by devel on 2016-07-19.
 */
public class RatePopupManager
{
    private static PopupWindow popup = null;
    private static ShineButton[] btns = new ShineButton[5];
    private static boolean isAnimating = false;

    public static int Rate = 0;

    public static PopupWindow getInstance(Context context, final OnCloseListener listener)
    {
        popup = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.rate_popup, null);

        popup.setContentView(view);
        popup.setWindowLayoutMode(AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
        popup.setTouchable(true);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());

        popup.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                if (Rate != 0)
                {
                    listener.onClose(Rate);
                    Rate = 0;
                }
            }
        });

        InitRateAnimation();

        return popup;
    }

    private static void InitRateAnimation()
    {
        if (popup != null)
        {
            for (int i = 1; i <= btns.length; i++)
            {
                final int idx = i - 1;

                int resID = popup.getContentView().getResources().getIdentifier("po_image" + i, "id", "com.devjy.devel.evermeal");
                btns[idx] = (ShineButton) popup.getContentView().findViewById(resID);

                btns[idx].setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(View view, boolean checked)
                    {

                    }
                });
                btns[idx].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (!isAnimating)
                            RatePopupManager.Animate(idx);
                    }
                });
            }
        }
    }

    private static void Animate(int idx)
    {
        Rate = idx + 1;
        isAnimating = true;

        btns[idx].setCancel();

        int delay = 0;

        for (int i = 0; i <= idx; i++)
        {
            if (!btns[i].isChecked())
            {
                Handler handler = new Handler();
                handler.postDelayed(perform_runnables[i], delay);
                delay += 100;
            }
        }

        for (int i = btns.length - 1; i >= idx + 1; i--)
        {
            if (btns[i].isChecked())
            {
                Handler handler = new Handler();
                handler.postDelayed(perform_runnables[i], delay);
                delay += 100;
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(perform_runnables[5], delay - 100);
    }

    private static Runnable[] perform_runnables = new Runnable[]
            {
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btns[0].performClick();
                        }
                    },
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btns[1].performClick();
                        }
                    },
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btns[2].performClick();
                        }
                    },
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btns[3].performClick();
                        }
                    },
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btns[4].performClick();
                        }
                    },
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            isAnimating = false;
                        }
                    }
            };
}
