package com.example.taskmaster;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class Joystick {

    private final Paint outerCirclePaint;
    private final Paint innerCirclePaint;
    private int outerCircleRadius;
    private int innerCircleRadius;
    private int outerCirclePositionX;
    private int outerCirclePositionY;
    private int innerCirclePositionX;
    private int innerCirclePositionY;
    private double joyStickCenter;
    private boolean isPressed;
    private double actuatorX;
    private double actuatorY;

    public Joystick(int centerPositonX, int centerPositonY, int outerCircleRadius, int innerCircleRadius) {
        outerCirclePositionX = centerPositonX;
        outerCirclePositionY = centerPositonY;
        innerCirclePositionX = centerPositonX;
        innerCirclePositionY = centerPositonY;

        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        //Paint of circles
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.BLUE);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }
    public void draw(Canvas canvas) {
        canvas.drawCircle(outerCirclePositionX, outerCirclePositionY, outerCircleRadius, outerCirclePaint);
        canvas.drawCircle(innerCirclePositionX, innerCirclePositionY, innerCircleRadius, innerCirclePaint);
    }

    public void update() {
        updateInnerCirclePosition();
    }

    private void updateInnerCirclePosition() {
        innerCirclePositionX = (int) (outerCirclePositionX + actuatorX*outerCircleRadius);
        innerCirclePositionY = (int) (outerCirclePositionY + actuatorY*outerCircleRadius);
    }

    public boolean isPressed(double touchPositionX, double touchPositionY) {
        joyStickCenter = Math.sqrt(
                Math.pow(outerCirclePositionX - touchPositionX, 2) +
                Math.pow(outerCirclePositionY - touchPositionY, 2)
        );
        return joyStickCenter < outerCircleRadius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean getIsPressed() {
        return isPressed;
    }


    public void setActuator(double touchPositionX, double touchPositionY) {
        double deltaX = touchPositionX - outerCirclePositionX;
        double deltaY = touchPositionY - outerCirclePositionY;
        double deltaDist = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY,2));

        if(deltaDist < outerCircleRadius) {
            actuatorX = deltaX / outerCircleRadius;
            actuatorY = deltaY / outerCircleRadius;
        } else {
            actuatorX = deltaX/deltaDist;
            actuatorY = deltaY/deltaDist;
        }

    }

    public void resetActuator() {
        actuatorX = 0.0;
        actuatorY = 0.0;
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }
}
