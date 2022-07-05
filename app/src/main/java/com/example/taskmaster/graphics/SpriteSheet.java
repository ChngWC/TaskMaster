package com.example.taskmaster.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.taskmaster.R;

public class SpriteSheet {
    private static final int SPRITE_WIDTH_PIXELS = 64;
    private static final int SPRITE_HEIGHT_PIXELS = 64;
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

    public Sprite getWaterSprite() {
        return getSpriteByIndex(2,0);
    }
    public Sprite getLavaSprite() {
        return getSpriteByIndex(2,1);
    }
    public Sprite getGroundSprite() {
        return getSpriteByIndex(2,2);
    }
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
