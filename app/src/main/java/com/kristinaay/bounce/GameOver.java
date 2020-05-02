package com.kristinaay.bounce;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GameOver {
    public int x, y, width, height;

    private Bitmap gameOver;

    public GameOver(int screenX, int screenY, Resources res) {
        Jump jump = new Jump(screenY, res);
        gameOver= BitmapFactory.decodeResource(res, R.drawable.gameover);

        width =  gameOver.getWidth() / 2;
        height = gameOver.getHeight() / 2;

        width *= (int) GameView.screenRatioX;
        height *= (int) GameView.screenRatioY;

        gameOver = Bitmap.createScaledBitmap(gameOver, width, height, false);

        y = screenY / 3;
        x = screenX / 3;
    }

    public Bitmap getGameOver() {
        return gameOver;
    }
}