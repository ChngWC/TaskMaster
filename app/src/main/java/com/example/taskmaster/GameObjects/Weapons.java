package com.example.taskmaster.GameObjects;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.taskmaster.GameStuff.GameLoop;
import com.example.taskmaster.R;

public class Weapons extends CirclePlayer {

    public static final double SPEED_PIXELS_PER_SEC = 800.0;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SEC / GameLoop.MAX_UPS;
   // private final Player weaponUser;

    public Weapons(Context context, Player weaponUser) {
        super(context,
                ContextCompat.getColor(context, R.color.weapons),
                weaponUser.getPositionX(),
                weaponUser.getPositionY(),
                25);
        //this.weaponUser= weaponUser;
        velocityX = weaponUser.getDirectionX()*MAX_SPEED;
        velocityY = weaponUser.getDirectionY()*MAX_SPEED;
    }

    @Override
    public void update() {
        positionX += velocityX;
        positionY += velocityY;

    }
}
