package com.kristinaay.bounce;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Star {
    public int speed;
    int x, y, width, height;
    Bitmap star;
    public boolean active;

    Star(Resources res) {
        star = BitmapFactory.decodeResource(res, R.drawable.star);

        width = star.getWidth() / 4;
        height = star.getHeight() / 4;

        width *= (int) GameView.screenRatioX;
        height *= (int) GameView.screenRatioY;

        star = Bitmap.createScaledBitmap(star, width, height, false);

        y = -height;

        speed = 10;

        active = true;
    }

    public Bitmap getStar() {
        return star;
    }

    public Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }
}
