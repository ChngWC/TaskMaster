package com.example.taskmaster.GameObjects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.taskmaster.GameStuff.GameDisplay;

public abstract class CirclePlayer extends GameObject {
    protected double radius;
    protected Paint paint;

    public CirclePlayer(Context context, int color, double positionX, double positionY, double radius) {
        super(positionX, positionY);

        this.radius = radius;

        paint = new Paint();
        paint.setColor(color);
    }

    public static boolean isCollide(CirclePlayer obj1, CirclePlayer obj2) {
        //checks if 2 circles objs are colliding based on position and radii
        double distance  = getDist(obj1, obj2);
        double distanceToCollision = obj1.getRadius() + obj2.getRadius();
        if(distance < distanceToCollision) {
            return true;
        } else {
            return false;
        }
    }

    private double getRadius() {
        return radius;
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        canvas.drawCircle(
                (float) gameDisplay.gameToDisplayCoordinatesX(positionX),
                (float) gameDisplay.gameToDisplayCoordinatesY(positionY),
                (float)radius,
                paint
        );

    }
}
