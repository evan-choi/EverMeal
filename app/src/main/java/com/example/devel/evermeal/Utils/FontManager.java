package com.example.devel.evermeal.Utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by devel on 2016-07-13.
 */
public class FontManager {
    static HashMap<String, Typeface> cache = new HashMap<>();

    public static Typeface Load(Context context, String assetPath)
    {
        if (!cache.containsKey(assetPath))
        {
            Typeface font = Typeface.createFromAsset(context.getAssets(), assetPath);

            cache.put(assetPath, font);
        }

        return cache.get(assetPath);
    }
}
