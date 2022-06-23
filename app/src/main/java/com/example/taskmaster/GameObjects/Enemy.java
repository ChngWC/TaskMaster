package com.example.taskmaster.GameObjects;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.taskmaster.GameLoop;
//import com.example.taskmaster.Player;
import com.example.taskmaster.R;

/***
 * enemy is a character which always moves towards to player
 * extension of circle which is extension of gameobject
 */
public class Enemy extends CirclePlayer {
    private static final double SPEED_PIXELS_PER_SEC = Player.SPEED_PIXELS_PER_SEC * 0.6;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SEC / GameLoop.MAX_UPS;
    private final Player player;

    public Enemy(Context context, Player player , double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);
        this.player = player;
    }

    @Override
    public void update() {
        //update velocity of enemy so that velocity is in the direction of player
        //calculate vector of the enemy to player (in x and y)
        double distanceToPlayerX = player.getPositionX() - positionX;
        double distanceToPlayerY = player.getPositionY() - positionY;

        //calculate absolute distance between enemy to player
        double distanceToPlayer = GameObject.getDist(this, player);

        //calculate the direction from enemy to player
        double directionX = distanceToPlayerX/distanceToPlayer;
        double directionY = distanceToPlayerY/distanceToPlayer;

        //set velocity in the direction of the player
        if(distanceToPlayer >0) {
            velocityX = directionX*MAX_SPEED;
            velocityY = directionY*MAX_SPEED;
        } else {
            velocityX = 0;
            velocityY = 0;
        }

        //update the position of the enemy
        positionX += velocityX;
        positionY += velocityY;

    }
}
