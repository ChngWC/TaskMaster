package com.example.taskmaster.GameObjects;

import android.content.Context;
import android.graphics.Canvas;

import androidx.core.content.ContextCompat;

import com.example.taskmaster.GamePanel.HealthBar;
import com.example.taskmaster.GamePanel.Joystick;
import com.example.taskmaster.GameStuff.GameDisplay;
import com.example.taskmaster.GameStuff.GameLoop;
import com.example.taskmaster.R;
import com.example.taskmaster.Utilities;
import com.example.taskmaster.graphics.Sprite;

public class Player extends CirclePlayer {
    public static final double SPEED_PIXELS_PER_SEC = 400.0;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SEC / GameLoop.MAX_UPS;
    private final Joystick joystick;
    public static final int max_health_points = 10;
    private HealthBar healthBar;
    private int healthPoints;
    private Sprite sprite;


    public Player(Context context,Joystick joystick, double positionX, double positionY, double radius, Sprite sprite) {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
        this.healthBar = new HealthBar(context,this);
        this.healthPoints =  max_health_points;
        this.sprite = sprite;
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

    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        sprite.draw(canvas,
                (int) gameDisplay.gameToDisplayCoordinatesX(getPositionX()) - sprite.getWidth()/2,
                (int) gameDisplay.gameToDisplayCoordinatesY(getPositionY()) - sprite.getHeight()/2);
        healthBar.draw(canvas, gameDisplay);
    }

    public float getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        if (healthPoints >=0)
            this.healthPoints = healthPoints;
    }
}
