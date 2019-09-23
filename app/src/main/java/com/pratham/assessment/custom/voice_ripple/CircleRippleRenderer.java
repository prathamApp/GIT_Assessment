package com.pratham.assessment.custom.voice_ripple;


import android.graphics.Canvas;
import android.graphics.Paint;

public class CircleRippleRenderer extends Renderer {
    private Paint buttonPaint;
    private Paint ripplePaint;
    private Paint rippleBackgroundPaint;

    public CircleRippleRenderer(Paint ripplePaint, Paint rippleBackgroundPaint, Paint buttonPaint) {
        this.ripplePaint = ripplePaint;
        this.rippleBackgroundPaint = rippleBackgroundPaint;
        this.buttonPaint = buttonPaint;
    }

    @Override
    public void render(Canvas canvas, int x, int y, int buttonRadius, int rippleRadius, int rippleBackgroundRadius) {
        super.render(canvas, x, y, buttonRadius, rippleRadius, rippleBackgroundRadius);

        canvas.drawCircle(x, y, rippleRadius, ripplePaint);
        canvas.drawCircle(x, y, rippleBackgroundRadius, rippleBackgroundPaint);
        canvas.drawCircle(x, y, buttonRadius, buttonPaint);
    }

    @Override
    public void changeColor(int color) {
        ripplePaint.setColor(color);
        rippleBackgroundPaint.setColor((color & 0x00FFFFFF) | 0x40000000);
    }
}
