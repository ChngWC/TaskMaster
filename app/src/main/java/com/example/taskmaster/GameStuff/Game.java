package com.example.taskmaster.GameStuff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.taskmaster.GameObjects.CirclePlayer;
import com.example.taskmaster.GameObjects.Enemy;
import com.example.taskmaster.GameObjects.Player;
import com.example.taskmaster.GameObjects.Weapons;
import com.example.taskmaster.GamePanel.GameOver;
import com.example.taskmaster.GamePanel.Joystick;
import com.example.taskmaster.GamePanel.Performance;
import com.example.taskmaster.MainActivity;
import com.example.taskmaster.ProfileActivity;
import com.example.taskmaster.Register_Page;
import com.example.taskmaster.graphics.SpriteSheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    //private final Enemy enemy;
    private GameLoop gameLoop;
   // private Context context;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Weapons> weaponsList = new ArrayList<Weapons>();
    private int joystickPointerId = 0;
    private int numberOfWeaponsUsed = 0;
    public static int score;
    public static int gameScore; //after player died
    private static final int initialScore = 0;
    private GameOver gameOver;
    private Performance performance;
    private GameDisplay gameDisplay;

    public Game(Context context) {
        super(context);

        //Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        //this.context = context;
        score = initialScore;

         gameLoop = new GameLoop(this, surfaceHolder);

         //Initialise game panels
        performance = new Performance(context, gameLoop);
        gameOver = new GameOver(context);
        joystick = new Joystick(275, 800, 70, 40);


         //Initialise game objects
        SpriteSheet spriteSheet = new SpriteSheet(context);
        player = new Player(context, joystick,2*500, 500, 30, spriteSheet.getPlayerSprite());
        // enemy = new Enemy(getContext(), player,2*500, 200, 30);

        // Initialise game display and center it around the playaer
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameDisplay = new GameDisplay(displayMetrics.widthPixels, displayMetrics.heightPixels, player);

        setFocusable(true);
    }

    public static int getGameScore() {
        return gameScore;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //Handle different touch events
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(joystick.getIsPressed()) {
                    //Joystick was pressed before this event -> use weapon
                    numberOfWeaponsUsed++;
                } else if(joystick.isPressed((double)event.getX(), (double)event.getY())) {
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                } else {
                    numberOfWeaponsUsed++;
                    //joystick was not previously pressed, and is not pressed in this event -> use weapon
                }
                //player.setPosition((double)event.getX(), (double)event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                //joystick was pressed previously and is moved
                if(joystick.getIsPressed()) {
                    joystick.setActuator((double)event.getX(), (double)event.getY());
                    }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerId == event.getPointerId(event.getActionIndex())) {
                    // Joystick was let go of -> setIsPressed(false) and reset Actuator
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                return true;

                }
                //player.setPosition((double)event.getX(), (double)event.getY());
        return super.onTouchEvent(event);
        }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.d("Game.java", "surfaceCreated()");
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            gameLoop = new GameLoop(this, holder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw (Canvas canvas) {
        super.draw(canvas);

        player.draw(canvas, gameDisplay);

        for (Enemy enemy : enemyList){
            enemy.draw(canvas, gameDisplay);
        }

        for (Weapons weapons : weaponsList) {
            weapons.draw(canvas, gameDisplay);
        }

        joystick.draw(canvas);
        performance.draw(canvas);

        // Draw Game over if player dieded health<=0
        if (player.getHealthPoints() <=0) {
            gameScore = score;
            gameOver.draw(canvas);
            //score = 0;
        }
    }


    public void update() {

        // Stop updating the game if dead
        if (player.getHealthPoints()<= 0){
            return;
        }


       joystick.update();
        player.update();


        //spawn enemy when available
        if(Enemy.readyToSpawn()) {
            SpriteSheet spriteSheet = new SpriteSheet(getContext());
            enemyList.add(new Enemy(getContext(), player, spriteSheet.getEnemySprite()));
        }

        //update state of each enemy
        while (numberOfWeaponsUsed > 0) {
            weaponsList.add(new Weapons(getContext(), player));
            numberOfWeaponsUsed--;
        }
        for(Enemy enemy : enemyList) {
            enemy.update();
        }

        //update state of each weapon
        for(Weapons weapons : weaponsList) {
            weapons.update();
        }

        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while (iteratorEnemy.hasNext()) {
            CirclePlayer enemy = iteratorEnemy.next();
            if(CirclePlayer.isCollide(enemy, player)) {
                //remove enemy if collides with player
                iteratorEnemy.remove();
                player.setHealthPoints((int) (player.getHealthPoints() - 5));
                continue;
            }

            Iterator<Weapons> iteratorWeapons = weaponsList.iterator();
            while(iteratorWeapons.hasNext()) {
                CirclePlayer weapon = iteratorWeapons.next();

                if(CirclePlayer.isCollide(weapon, enemy)) {
                    iteratorWeapons.remove();
                    iteratorEnemy.remove();
                    score += 100;
                    break;
                }
            }
        }
        gameDisplay.update();
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
