package com.example.taskmaster;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.taskmaster.GameObjects.CirclePlayer;
import com.example.taskmaster.GameObjects.Enemy;
import com.example.taskmaster.GameObjects.Player;
import com.example.taskmaster.GameObjects.Weapons;

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

    public Game(Context context) {
        super(context);

        //Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        //this.context = context;

         gameLoop = new GameLoop(this, surfaceHolder);

         //Initialise game objects
        joystick = new Joystick(275, 800, 70, 40);
         player = new Player(getContext(), joystick,2*500, 500, 30);
        // enemy = new Enemy(getContext(), player,2*500, 200, 30);

        setFocusable(true);
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
        drawFPS(canvas);
        drawUPS(canvas);

        player.draw(canvas);
        joystick.draw(canvas);
        for (Enemy enemy : enemyList){
            enemy.draw(canvas);
        }

        for (Weapons weapons : weaponsList) {
            weapons.draw(canvas);
        }
    }

    public void drawUPS (Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.purple_500);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    public void drawFPS (Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.purple_500);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageUPS, 100, 200, paint);
    }

    public void update() {
       joystick.update();
        player.update();


        //spawn enemy when available
        if(Enemy.readyToSpawn()) {
            enemyList.add(new Enemy(getContext(), player));
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
                continue;
            }

            Iterator<Weapons> iteratorWeapons = weaponsList.iterator();
            while(iteratorWeapons.hasNext()) {
                CirclePlayer weapon = iteratorWeapons.next();

                if(CirclePlayer.isCollide(weapon, enemy)) {
                    iteratorWeapons.remove();
                    iteratorEnemy.remove();
                    break;
                }
            }
        }
    }
}
