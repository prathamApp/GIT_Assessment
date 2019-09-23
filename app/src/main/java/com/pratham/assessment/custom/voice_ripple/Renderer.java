package com.pratham.assessment.custom.voice_ripple;

import android.graphics.Canvas;

public abstract class Renderer {
    public void render(Canvas canvas, int x, int y, int buttonRadius, int rippleRadius, int rippleBackgroundRadius) {

    }

    public abstract void changeColor(int color);
}

