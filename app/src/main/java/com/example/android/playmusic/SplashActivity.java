package com.example.android.playmusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        Handler handler= new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent= new Intent(SplashActivity.this,MainActivity.class);
               startActivity(intent);
                finish();

            }

        },SPLASH_DISPLAY_LENGTH );
    }
}