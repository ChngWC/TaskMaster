package com.example.taskmaster.GameObjects;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.taskmaster.GamePanel.HealthBar;
import com.example.taskmaster.GamePanel.Joystick;
import com.example.taskmaster.GameStuff.GameDisplay;
import com.example.taskmaster.GameStuff.GameLoop;
import com.example.taskmaster.R;
import com.example.taskmaster.Utilities;
import com.example.taskmaster.graphics.Sprite;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Player extends CirclePlayer {
    public static final double SPEED_PIXELS_PER_SEC = 400.0;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SEC / GameLoop.MAX_UPS;
    private final Joystick joystick;
    public static int max_health_points = 10;
    private HealthBar healthBar;
    private int healthPoints;
    private Sprite sprite;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private String userID;


    public Player(Context context,Joystick joystick, double positionX, double positionY, double radius, Sprite sprite) {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
        this.healthBar = new HealthBar(context, this);
        //this.healthPoints = 10;
        this.sprite = sprite;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        firestore = FirebaseFirestore.getInstance();

        DocumentReference HPDoc = firestore.collection("Users").document(userID).collection("stats").document("statsMap");
        /*HPDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        max_health_points = documentSnapshot.getLong("userMaxHP").intValue();
                    } else {
                        Map<String, Integer> HPMap = new HashMap<>();
                        HPMap.put("userMaxHP", 10);
                        firestore.collection("Users").document(userID).collection("stats").document("statsMap").set(HPMap);
                        max_health_points = 10;
                    }
                }
            }
        });*/

        HPDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    max_health_points = value.getLong("userMaxHP").intValue();
                } else {
                    Map<String, Integer> HPMap = new HashMap<>();
                    HPMap.put("userMaxHP", 10);
                    firestore.collection("Users").document(userID).collection("stats").document("statsMap").set(HPMap);
                    max_health_points = 10;
                }
            }
        });
        this.healthPoints = max_health_points;

    }



    public void update() {
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;

        //update direction
        if(velocityX != 0 || velocityY != 0) {
            //normalized velocity to get direction
            double distance = Utilities.getDistanceBetweenPoints(0,0, velocityX, velocityY);
            directionX = velocityX/distance;
            directionY = velocityY/distance;
        }

    }

    public void setPosition(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        sprite.draw(canvas,
                (int) gameDisplay.gameToDisplayCoordinatesX(getPositionX()) - sprite.getWidth()/2,
                (int) gameDisplay.gameToDisplayCoordinatesY(getPositionY()) - sprite.getHeight()/2);
        healthBar.draw(canvas, gameDisplay);
    }

    public float getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
       // if (healthPoints >=0)
            this.healthPoints = healthPoints;
    }
}
