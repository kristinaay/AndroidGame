package com.kristinaay.bounce;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Jump {
    public int x, y, screenY, startingY;
    public boolean isJumping;
    public boolean canJump;
    public float speed;
    public float maxSpeed;
    public boolean isFalling;
    protected float gravity;
    public int width, height, w2, h2, w3, h3, pos;

    Bitmap jump1, jump2, jump3, jump4, jump5, jump6, dead;
    public Jump(int screenY, Resources res) {
        pos = 0;
        isJumping = false;
        canJump = true;
        isFalling = false;
        gravity = 1.2f;
        speed = 0f;
        maxSpeed = 7f;

        jump1 = BitmapFactory.decodeResource(res, R.drawable.frogwithshadow);
        jump2 = BitmapFactory.decodeResource(res, R.drawable.frog2withshadow);
        jump3 = BitmapFactory.decodeResource(res, R.drawable.frog3withshadow);
        jump4 = BitmapFactory.decodeResource(res, R.drawable.frog4andhalf2);
        jump5 = BitmapFactory.decodeResource(res, R.drawable.frog5);
        jump6 = BitmapFactory.decodeResource(res, R.drawable.frog1);
        dead = BitmapFactory.decodeResource(res, R.drawable.dead);

        width = jump1.getWidth() / 4;
        height = jump1.getHeight() / 4;

        /*w2 = jump3.getWidth() / 4;
        h2 = jump3.getHeight() / 4;

        w3 = jump4.getWidth() / 4;
        h3 = jump4.getHeight() / 4
         */

        width *= (int) GameView.screenRatioX;
        height *= (int) GameView.screenRatioY;

        jump1 = Bitmap.createScaledBitmap(jump1, width, height, false);
        jump2 = Bitmap.createScaledBitmap(jump2, width, height, false);
        jump3 = Bitmap.createScaledBitmap(jump3, width, height,false);
        jump4 = Bitmap.createScaledBitmap(jump4, width, height,false);
        jump5 = Bitmap.createScaledBitmap(jump5, width, height, false);
        jump6 = Bitmap.createScaledBitmap(jump6, width, height, false);
        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        this.screenY = screenY;
        //setting starting position
        y = (int) ((screenY - screenY / 2.5) * GameView.screenRatioY);
        x = 300 * (int) GameView.screenRatioX;

        startingY = y;

    }

    public boolean isCanJump() {
        if (pos == 0 && y == startingY) {
            canJump = true;
            return canJump;
        }
        else {
            canJump = false;
            return canJump;
        }
    }

    Bitmap getJump() {
        if (pos % 6 == 0) {
            pos++;
            return jump1;
        }
        if (pos % 6 == 1) {
            pos++;
            return jump2;
        }
        if (pos % 6 == 2) {
            pos++;
            return jump3;
        }
        if (pos % 6 == 3) {
            pos++;
            return jump4;
        }
        if (pos % 6 == 4) {
            pos++;
            return jump5;
        }
        if (pos % 6 == 5) {
            pos++;
            return jump6;
        }
        return jump6;
    }

    public void fall() {
        while (y >= 0) {
            if (isFalling) {
            speed += gravity;
            y -= 1 * gravity;
                if (speed > maxSpeed) {
                    speed = maxSpeed;
                }
            }
        }
        isFalling = false;
    }

    public void leap() {
        int maxHeight = screenY / 2;
        if (isCanJump()) {
            canJump = false;
            while (y > maxHeight) {
                y -= 50;
            }
        }
    }

    public Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead() {
        return dead;
    }
}
