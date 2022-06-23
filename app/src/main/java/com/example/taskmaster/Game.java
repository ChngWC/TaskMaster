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
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(joystick.isPressed((double)event.getX(), (double)event.getY())) {
                    joystick.setIsPressed(true);
                }
                //player.setPosition((double)event.getX(), (double)event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()) {
                    joystick.setActuator((double)event.getX(), (double)event.getY());
                    }
                return true;
            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
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
        for(Enemy enemy : enemyList) {
            enemy.update();
        }

        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while (iteratorEnemy.hasNext()) {
            if(CirclePlayer.isCollide(iteratorEnemy.next(), player)) {
                //remove enemy if collides with player
                iteratorEnemy.remove();
            }
        }
    }
}