package com.kristinaay.bounce;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LilyPad {
    public int x, y, width, height;

    private Bitmap lilypad;

    public LilyPad(int screenY, Resources res) {
        Jump jump = new Jump(screenY, res);
        lilypad = BitmapFactory.decodeResource(res, R.drawable.lilypadmoving);

        width =  lilypad.getWidth() / 4;
        height = lilypad.getHeight() / 4;

        width *= (int) GameView.screenRatioX;
        height *= (int) GameView.screenRatioY;

        lilypad = Bitmap.createScaledBitmap(lilypad, width, height, false);

        y = jump.y + 40;
        x = jump.x - 250;
    }

    public Bitmap getLilyPad() {
        return lilypad;
    }
}
