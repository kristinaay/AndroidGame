package com.kristinaay.bounce;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Ghost {
    public int speed;
    int x, y, width, height, counter;
    Bitmap ghost1, ghost2, ghost3;

    Ghost(Resources res) {
        ghost1 = BitmapFactory.decodeResource(res, R.drawable.ghost1);
        ghost2 = BitmapFactory.decodeResource(res, R.drawable.ghost2);
        ghost3 = BitmapFactory.decodeResource(res, R.drawable.ghost3);

        width = ghost1.getWidth() / 4;
        height = ghost2.getHeight() / 4;

        width *= (int) GameView.screenRatioX;
        height *= (int) GameView.screenRatioY;

        ghost1 = Bitmap.createScaledBitmap(ghost1, width, height, false);
        ghost2 = Bitmap.createScaledBitmap(ghost2, width, height, false);
        ghost3 = Bitmap.createScaledBitmap(ghost3, width, height, false);

        y = -height;
        counter = 1;

        speed = 20;
    }
    public Bitmap getGhost() {
        if (counter == 1) {
            counter++;
            return ghost1;
        }
        /*if (counter == 2) {
            counter++;
            return ghost2;
        }

         */
        counter = 1;
        return ghost3;
    }

    public Rect getCollisionShape() {
        return new Rect(x + 25, y + 25, x + width - 25, y + height - 25);
    }
}
