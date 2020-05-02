package com.kristinaay.bounce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean isMute;
    public static MediaPlayer mediaPlayer;
    TextView highScoreText;
    private Button help;
    Dialog myDialogue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting this activity's look to our activity_main.xml
        setContentView(R.layout.activity_main);
        //set the app to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //initializing the dialog window to be used for the instruction screen
        myDialogue = new Dialog(this);
        //getting and displaying the stored high score value
        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highScoreText = findViewById(R.id.highscoretext);
        int int1 = prefs.getInt("highscore", 0);
        highScoreText.setText(Integer.toString(int1));

        //starts the game activity when "play" is pressed
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }

        });

        //starts the music
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bg);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        isMute = prefs.getBoolean("isMute", false);

        //allows the player to mute and unmute the sound
        //also changes the icon of the sound button depending
        final ImageView volume = findViewById(R.id.music);
        if (isMute) {
            volume.setImageResource(R.drawable.frogmute);
        } else {
            volume.setImageResource(R.drawable.frogmusic);
        }
        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMute = !isMute;
                if (isMute) {
                    volume.setImageResource(R.drawable.frogmute);
                    mediaPlayer.pause();
                } else {
                    volume.setImageResource(R.drawable.frogmusic);
                    mediaPlayer.start();
                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();
            }


        });




    }

    //method to open and close the instructions dialog
    public void showPopUp(View view) {
        TextView close;
        myDialogue.setContentView(R.layout.popup);
        close = myDialogue.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogue.dismiss();
            }
        });
        myDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogue.show();
    }

}

