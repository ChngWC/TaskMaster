package com.example.taskmaster.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.taskmaster.R;

public class SpriteSheet {
    private Bitmap bitmap;

    // replace monster with sprite_sheet

    public SpriteSheet(Context context) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled =  false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_sheet, bitmapOptions);
    }

    public Sprite getPlayerSprite() {
        return new Sprite(this, new Rect( 0,0,64,64));
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Sprite getEnemySprite() {return new Sprite(this, new Rect(64, 0, 128, 64));}
}
