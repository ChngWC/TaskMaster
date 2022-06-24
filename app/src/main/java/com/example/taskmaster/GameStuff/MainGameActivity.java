package com.example.taskmaster.GameStuff;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmaster.ProfileActivity;

public class MainGameActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainGameActivity.java", "onCreate()");
        super.onCreate(savedInstanceState);

        game = new Game(this);
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
        super.onResume();
    }


    @Override
    protected void onPause() {
        Log.d("MainGameActivity.java", "onPause()");
        game.pause();
        super.onPause();
    }


    @Override
    protected void onStop() {
        Log.d("MainGameActivity.java", "onStop()");
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        Log.d("MainGameActivity.java", "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.d("MainGameActivity.java", "onBackPressed");
        //super.onBackPressed();
        startActivity(new Intent(MainGameActivity.this, ProfileActivity.class));
    }

    public void goBack() {
                startActivity(new Intent(MainGameActivity.this, ProfileActivity.class));
    }
}