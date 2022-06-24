package com.example.taskmaster.GameStuff;

import android.animation.Animator;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainGameActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainGameActivity.java", "onCreate()");
        super.onCreate(savedInstanceState);

        game = new Game (this);
        setContentView(game);
    }

    @Override
    protected void onStart() {
        Log.d("MainGameActivity.java", "onStart()");
        super.onStart();
    }


    @Override
    protected void onResume() {
        Log.d("MainGameActivity.java", "onResume()");
        super.onStart();
    }


    @Override
    protected void onPause() {
        Log.d("MainGameActivity.java", "onPause()");
        game.pause();
        super.onStart();
    }


    @Override
    protected void onStop() {
        Log.d("MainGameActivity.java", "onStop()");
        super.onStart();
    }


    @Override
    protected void onDestroy() {
        Log.d("MainGameActivity.java", "onDestroy()");
        super.onStart();
    }
}