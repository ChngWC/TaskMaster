package com.example.taskmaster.GamePanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.taskmaster.GameStuff.Game;
import com.example.taskmaster.R;

/**
 * GameOver is a panel which draws text gameover to the screen and possibly score?
 */
public class GameOver {

    private Context context;

    public GameOver(Context context){
        this.context = context;
    }

    public void draw(Canvas canvas) {
        //canvas.drawText(text, x, y, paint);
        String text = "Game Over";

        float x = 800;
        float y = 200;

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.gameOver);
        paint.setColor(color);
        float textSize = 150;
        paint.setTextSize(textSize);
        canvas.drawText(text, x, y, paint);

        String results = Integer.toString(Game.score);
        canvas.drawText("Total Score = "+ results, 600, 400, paint);
    }
}
