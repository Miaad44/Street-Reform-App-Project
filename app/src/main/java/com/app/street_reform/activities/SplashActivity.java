package com.app.street_reform.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.app.street_reform.R;

public class SplashActivity extends AppCompatActivity {
    Handler handler_83;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        handler_83=new Handler();
        handler_83.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent_83=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent_83);
                finish();
            }
        },3000);
    }
}