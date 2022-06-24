package com.example.taskmaster.GameObjects;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.taskmaster.GameStuff.GameLoop;
//import com.example.taskmaster.Player;
import com.example.taskmaster.R;

/***
 * enemy is a character which always moves towards to player
 * extension of circle which is extension of gameobject
 */
public class Enemy extends CirclePlayer {
    private static final double SPEED_PIXELS_PER_SEC = Player.SPEED_PIXELS_PER_SEC * 0.6;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SEC / GameLoop.MAX_UPS;
    private static final double SPAWNS_PER_MIN = 20;
    private static final double SPAWNS_PER_SECOND = SPAWNS_PER_MIN/60.0;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SECOND;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    private final Player player;

    public Enemy(Context context, Player player , double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);
        this.player = player;
    }

    public Enemy(Context context, Player player) {
        super(context,
                ContextCompat.getColor(context, R.color.enemy),
                Math.random()*1000,
                Math.random()*1000,
                30);
        this.player = player;
    }

    public static boolean readyToSpawn() {
        /***
         * readyToSpawn checks if a new enemy should spawn, according to the decided number of spawns
         * per min (see SPAWNS_PER_MIN at the top
         * @return
         */
        if(updatesUntilNextSpawn <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        } else {
            updatesUntilNextSpawn --;
            return false;
        }
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
