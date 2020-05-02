package com.kristinaay.bounce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.Random;

import javax.xml.transform.SourceLocator;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver;
    public static float screenRatioX, screenRatioY;
    private Background background1, background2;
    private int screenX, screenY, score, sound;
    private Paint paint;
    private Jump jump;
    private LilyPad lilypad;
    private Ghost[] ghosts;
    private Random random;
    private Star[] stars;
    private GameOver gameOver;
    private SharedPreferences prefs;
    private GameActivity activity;
    private SoundPool soundpool;
    private boolean pressed, goingUp, goingDown;
    float jumpUpBy;
    float jumpDownBy;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);
        pressed = false;
        goingDown = false;
        goingUp = false;
        jumpUpBy = 250f;
        jumpDownBy = 30f;


        //creating the sound for the "ding" when a star is obtained
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).
                    setUsage(AudioAttributes.USAGE_GAME).build();

            soundpool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
        }
        else {
            soundpool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        sound = soundpool.load(activity, R.raw.ding, 1);

        this.activity = activity;

        //used to store the score data
        this.prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);
        this.score = 0;

        this.screenX = screenX;
        this.screenY = screenY;

        //used for changing the x/y slightly for different screen sizes
        screenRatioX = 2220f / screenX;
        screenRatioY = 1080f / screenY;

        //using two of the same backgrounds to get the side scrolle effect
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        //set background2 right off the screen after background1
        background2.x = screenX;

        //used to write the score
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        //getting instances of other classes
        jump = new Jump(screenY, getResources());
        lilypad = new LilyPad(screenY, getResources());
        ghosts = new Ghost[4];
        stars = new Star[3];
        random = new Random();
        gameOver = new GameOver(screenX, screenY, getResources());

        //not game over when the game starts
        isGameOver = false;

        //creating two instances of the ghost class
        for (int i = 0; i < 4; i++) {
            Ghost ghost = new Ghost(getResources());
            ghosts[i] = ghost;
        }

        //creating two instances of the star class
        for (int i = 0; i < 3; i++) {
            Star star = new Star(getResources());
            stars[i] = star;
        }

    }

    @Override
    public void run() {
        //repeat these methods while user is playing (game is not paused/stopped)
        while (isPlaying) {
            //we update the position, draw the new position, sleep, and repeat
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        //moving the background from right to left
        //repeating the two backgrounds to simulate movement
        background1.x -= 10;
        background2.x -= 10;
        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }
        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        //each ghost moves at a random speed from a random starting y position
        //moves from right to left

        for (Ghost ghost : ghosts) {
            ghost.x -= ghost.speed;
            if (ghost.x + ghost.width < 0) {
                int bound = 30;
                ghost.speed = random.nextInt(bound);
                if (ghost.speed < 15) {
                    ghost.speed = 20;
                }
                ghost.x = screenX;
                ghost.y = random.nextInt(screenY - (2 * ghost.height));
                if (ghost.y > jump.startingY - 200) {
                    ghost.y = jump.startingY - 200;
                }
            }

            //if the frog hits the ghost, the game is over
            if (Rect.intersects(ghost.getCollisionShape(), jump.getCollisionShape())) {
                isGameOver = true;
            }
        }


        //each star moves at a random speed from a random starting y position
        //moves from right to left
        for (Star star : stars) {
            star.x -= star.speed;
            if (star.x + star.width < 0) {
                int bound = 30;
                star.speed = random.nextInt(bound);
                if (star.speed < 15) {
                    star.speed = 20;
                }
                star.active = true;
                star.x = screenX;
                star.y = random.nextInt(screenY - (2 * star.height));
            }
            //if frog hits star, increment score counter
            //and play "ding" sound if not muted
            //do not show the star image after collision
            if (Rect.intersects(star.getCollisionShape(), jump.getCollisionShape())) {
                if (!prefs.getBoolean("isMute", false)) {
                    soundpool.play(sound, 1, 0, 0, 0, 1);
                }
                score = score + 1;
                star.active = false;
                System.out.println(score);
            }
        }

            if (pressed) {
                if (jump.y > 50 && goingUp) {
                    jumpUpBy = jumpUpBy *.99f;
                    jump.y -= jumpUpBy;
                }
                else if (jump.y <= 50) {
                    goingUp = false;
                    goingDown = true;
                    jump.y += jumpDownBy;
                }
                else if (jump.y <= jump.startingY - 60 && goingDown) {
                    jumpDownBy = jumpDownBy * 4.2f;
                    jump.y += jumpDownBy;
                }
            }
        jumpUpBy = 250f;
        jumpDownBy = 30f;
        }




    private void draw() {
        if (getHolder().getSurface().isValid()) {
            //creating the canvas
            Canvas canvas = getHolder().lockCanvas();
            //drawing both backgrounds
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            //drawing the score
            canvas.drawText(score + "", screenX / 2f, 164, paint);

            //drawing the star and ghosts
            for (Star star: stars) {
                if (star.active) {
                    canvas.drawBitmap(star.getStar(), star.x, star.y, paint);
                }
            }
            for (Ghost ghost : ghosts) {
                canvas.drawBitmap(ghost.getGhost(), ghost.x, ghost.y, paint);
            }


            //drawing the lilypad
            canvas.drawBitmap(lilypad.getLilyPad(), lilypad.x, lilypad.y, paint);

            //game over condition
            //will save the score if it's higher than the saved high score
            //will wait a few seconds and then exit to title screen
            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(jump.getDead(), jump.x, jump.y, paint);
                canvas.drawBitmap(gameOver.getGameOver(), gameOver.x, gameOver.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting();
                return;
            }

            //draws the bouncing animation
            canvas.drawBitmap(jump.getJump(), jump.x, jump.y, paint);


            //painting is finished so now it can be drawn to the view's canvas
            getHolder().unlockCanvasAndPost(canvas);

        }

    }


    /*@Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            if (event.getX() < screenX) {
                while (jump.y > 50) {
                    jump.y -= 100;
                }
            } break;
            case MotionEvent.ACTION_UP:
                while (jump.y < jump.startingY) {
                    jump.y += 90;
                }
                break;
        }
        return true;
    }

     */

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                pressed = true;
                goingUp = true;
            }
        return true;
    }

    //saving the score if it's higher than the current high score
    private void saveIfHighScore() {
        if (prefs.getInt("highscore", 1) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }

    //wait three seconds after game over before exiting
    private void waitBeforeExiting() {
        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void sleep()  {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //starts the thread
    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        //starting the thread will call the run function
        thread.start();
    }

    //stop the thread
    public void pause() {
        try {
            isPlaying = false;
            //terminates the thread
            thread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
