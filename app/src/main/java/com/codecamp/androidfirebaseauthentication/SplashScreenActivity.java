package com.codecamp.androidfirebaseauthentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import static java.lang.Thread.sleep;

public class SplashScreenActivity extends AppCompatActivity {
    private ConstraintLayout mConstraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        mConstraintLayout = findViewById(R.id.constraintLayoutID);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                    startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));
    }
}