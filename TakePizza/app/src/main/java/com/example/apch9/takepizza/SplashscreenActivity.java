package com.example.apch9.takepizza;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashscreenActivity extends AppCompatActivity {

    ImageView splashImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        splashImageView = findViewById(R.id.splashScreenIv);
        final Animation splashScreenAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_screen_anim);



        final Intent mainActivityIntent = new Intent(this,MainActivity.class);

        Thread timer = new Thread(){
            public void run(){
                try {
                    splashImageView.startAnimation(splashScreenAnimation);
                    sleep(3000);
                    startActivity(mainActivityIntent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.start();
    }
}
