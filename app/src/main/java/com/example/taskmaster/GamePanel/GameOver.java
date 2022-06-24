package com.example.taskmaster.GamePanel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;

import androidx.core.content.ContextCompat;

import com.example.taskmaster.GameStuff.Game;
import com.example.taskmaster.GameStuff.MainGameActivity;
import com.example.taskmaster.ProfileActivity;
import com.example.taskmaster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * GameOver is a panel which draws text gameover to the screen and possibly score?
 */
public class GameOver {

    private Context context;
   // private MainGameActivity mainGameActivity;
    private FirebaseFirestore firestore;


    public GameOver(Context context){
        this.context = context;
    }

    public void draw(Canvas canvas) {
        firestore = FirebaseFirestore.getInstance();
        //canvas.drawText(text, x, y, paint);
        String text = "Game Over";

        float x = 800;
        float y = 200;

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.gameOver);
        paint.setColor(color);
        float textSize = 100;
        paint.setTextSize(textSize);
        canvas.drawText(text, x, y, paint);

        int results = Game.getGameScore();

        canvas.drawText("Total Score = "+ results, 600, 400, paint);
        canvas.drawText("Please press back button", 600, 600, paint);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Map<String, Integer> gameScoreMap = new HashMap<>();
        gameScoreMap.put("Highest", results);
        firestore.collection("Users").document(currentFirebaseUser.getUid()).collection("HighestScore").document("HighScoreMap").set(gameScoreMap);
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mainGameActivity.goBack();;
            }
        }, 5000);*/
    }
}
