package com.example.taskmaster.GameObjects;

import android.graphics.Canvas;

import com.example.taskmaster.GameStuff.GameDisplay;

public abstract class GameObject {
    public double positionX;
    public double positionY;
    public double velocityX = 0;
    public double velocityY = 0;
    public double directionX = 1;
    public double directionY = 0;

    public GameObject(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    protected static double getDist(GameObject obj1, GameObject obj2) {
        return Math.sqrt(
                Math.pow(obj2.getPositionX() - obj1.getPositionX(),2) +
                        Math.pow(obj2.getPositionY() - obj1.getPositionY(),2)
        );
    }

    public abstract void draw(Canvas canvas, GameDisplay gameDisplay);
    public abstract void update();

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getDirectionY() {
        return directionY;
    }

    public double getDirectionX() {
        return directionX;
    }


}
