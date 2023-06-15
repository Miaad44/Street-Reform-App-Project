package com.app.street_reform.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.app.street_reform.R;
import com.app.street_reform.activities.LoginActivity;

public class AdminActivity extends AppCompatActivity {

    Button View_butt_83,Logout_butt_83;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        View_butt_83 = findViewById(R.id.btnView);
        Logout_butt_83=findViewById(R.id.btnLogout);

        View_butt_83.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewComplainsActivity.class));
            }
        });

        Logout_butt_83.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Butt_intent_83 = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(Butt_intent_83);
            }
        });
    }
}