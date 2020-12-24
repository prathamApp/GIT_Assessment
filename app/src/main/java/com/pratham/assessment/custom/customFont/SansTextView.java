package com.pratham.assessment.custom.customFont;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pratham.assessment.custom.FastSave;

import static com.pratham.assessment.constants.Assessment_Constants.LANGUAGE;

@SuppressLint("AppCompatCustomView")
public class SansTextView extends TextView {

    public SansTextView(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Bold.ttf");
        this.setTypeface(face);
        setFont();
    }

    public SansTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Bold.ttf");
        this.setTypeface(face);
        setFont();
    }

    public SansTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Bold.ttf");
        this.setTypeface(face);
        setFont();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    private void setFont() {
        String lnag = FastSave.getInstance().getString(LANGUAGE, "1");
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Quicksand-Bold.ttf");
       /* if (lnag.equalsIgnoreCase("punjabi"))
            font = Typeface.createFromAsset(getContext().getAssets(), "fonts/raavi_punjabi.ttf");
        else if (lnag.equalsIgnoreCase("Assamese"))
            font = Typeface.createFromAsset(getContext().getAssets(), "fonts/geetl_assamese.ttf");
        else if (lnag.equalsIgnoreCase("Gujarati"))
            font = Typeface.createFromAsset(getContext().getAssets(), "fonts/muktavaani_gujarati.ttf");
        else*/ if (lnag.equalsIgnoreCase("12") /*|| lnag.equalsIgnoreCase("Oriya")*/)
            font = Typeface.createFromAsset(getContext().getAssets(), "fonts/lohit_oriya.ttf");
       /* if(FastSave.getInstance().getString(FC_Constants.CURRENT_SUBJECT, "English").equalsIgnoreCase("English"))
            font = Typeface.createFromAsset(getContext().getAssets(), "fonts/GlacialIndifference-Regular.otf");
*/
        setTypeface(font);
    }
}
