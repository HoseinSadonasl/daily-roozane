package com.abc.daily.app;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public final class FontOverride {

    public static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        final Typeface newTypeface = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
