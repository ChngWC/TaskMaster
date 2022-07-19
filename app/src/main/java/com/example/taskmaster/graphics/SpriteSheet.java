package com.example.taskmaster.graphics;

import static com.example.taskmaster.ProfileActivity.skintype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.taskmaster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class SpriteSheet {
    private static final int SPRITE_WIDTH_PIXELS = 64;
    private static final int SPRITE_HEIGHT_PIXELS = 64;
    private Bitmap bitmap;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private String userID;
    private int skins;

    // replace monster with sprite_sheet

    public SpriteSheet(Context context) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled =  false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_sheet, bitmapOptions);
    }

    public Sprite getPlayerSprite() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();
        firestore = FirebaseFirestore.getInstance();

        /*DocumentReference collegeSkin = firestore.collection("Users").document(userID).collection("stats").document("CollegeSkin");
        DocumentReference knightSkin = firestore.collection("Users").document(userID).collection("stats").document("KnightSkin");
        DocumentReference wizardSkin = firestore.collection("Users").document(userID).collection("stats").document("WizardSkin");

        collegeSkin.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    boolean selected = value.getBoolean("select");
                    if (selected) {
                        skintype = 0;
                        Log.d("SpriteSheet.java", "College skin");
                    }
                }
            }
        });

        knightSkin.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    boolean selected = value.getBoolean("select");
                    if (selected) {
                        skintype = 1;
                        Log.d("SpriteSheet.java", "knight skin");
                    }
                }
            }
        });
        wizardSkin.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()){
                    boolean selected = value.getBoolean("select");
                    if (selected) {
                        skintype = 2;
                        Log.d("SpriteSheet.java", "wizard skin");
                    }
                }
            }
        });*/
        this.skins = skintype;
        if (skins == 0) {
            Log.d("SpriteSheet.java", "0");
            // returns college sprite
            return getSpriteByIndex(0,3);
        } else if (skins == 1){
            Log.d("SpriteSheet.java", "1");
            // returns knight sprite
            return getSpriteByIndex(0,2);
        } else {
            Log.d("SpriteSheet.java", "2");
            // returns wizard sprite
            return getSpriteByIndex(0,0);
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Sprite getEnemySprite() {return getSpriteByIndex(0,1);}

    public Sprite getWaterSprite() {
        return getSpriteByIndex(2,0);
    }
    public Sprite getLavaSprite() {
        return getSpriteByIndex(2,1);
    }
    //public Sprite getGroundSprite() {return getSpriteByIndex(2,2);}
    public Sprite getGroundSprite(){ return new Sprite (this, new Rect(0,192,128,320));}
    public Sprite getGrassSprite() {
        return getSpriteByIndex(2,3);
    }

    private Sprite getSpriteByIndex(int row, int column) {
        return new Sprite(this,new Rect(
                column*SPRITE_WIDTH_PIXELS,
                row * SPRITE_HEIGHT_PIXELS,
                (column+1)*SPRITE_WIDTH_PIXELS,
                (row+1) * SPRITE_HEIGHT_PIXELS
        ));
    }

}
