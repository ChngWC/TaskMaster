package com.example.taskmaster.GameObjects;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.taskmaster.GameLoop;
import com.example.taskmaster.GameObjects.CirclePlayer;
import com.example.taskmaster.Joystick;
import com.example.taskmaster.R;
import com.example.taskmaster.Utilities;

public class Player extends CirclePlayer {
    public static final double SPEED_PIXELS_PER_SEC = 400.0;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SEC / GameLoop.MAX_UPS;
    private final Joystick joystick;


    public Player(Context context,Joystick joystick, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
    }


    public void update() {
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;

        //update direction
        if(velocityX != 0 || velocityY != 0) {
            //normalized velocity to get direction
            double distance = Utilities.getDistanceBetweenPoints(0,0, velocityX, velocityY);
            directionX = velocityX/distance;
            directionY = velocityY/distance;
        }

    }

    public void setPosition(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
}
