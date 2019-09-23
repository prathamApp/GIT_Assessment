package com.pratham.assessment.custom.customFont;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class SansEditText  extends EditText {

    public SansEditText(Context context) {
            super(context);
            setFont();
        }

    public SansEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
//        CustomFontHelper.setCustomFont(this, context, attrs);
            setFont();
        }

    public SansEditText(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
//        CustomFontHelper.setCustomFont(this, context, attrs);
            setFont();
        }

        private void setFont() {
            Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/GlacialIndifference-Regular.otf");
            setTypeface(font, Typeface.NORMAL);
        }

}
