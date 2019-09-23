package com.pratham.assessment.custom.customFont;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

@SuppressLint("AppCompatCustomView")
public class SansButton extends android.support.v7.widget.AppCompatButton {

    public SansButton(Context context) {
            super(context);
            setFont();
        }

    public SansButton(Context context, AttributeSet attrs) {
            super(context, attrs);
//        CustomFontHelper.setCustomFont(this, context, attrs);
            setFont();
        }

    public SansButton(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
//        CustomFontHelper.setCustomFont(this, context, attrs);
            setFont();
        }

        private void setFont() {
            Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/GlacialIndifference-Bold.otf");
            setTypeface(font, Typeface.NORMAL);
        }

}
