package com.example.taskmaster.GameObjects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class CirclePlayer extends GameObject {
    protected double radius;
    protected Paint paint;

    public CirclePlayer(Context context, int color, double positionX, double positionY, double radius) {
        super(positionX, positionY);

        this.radius = radius;

        paint = new Paint();
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float)positionX, (float)positionY, (float)radius, paint);

    }
}
