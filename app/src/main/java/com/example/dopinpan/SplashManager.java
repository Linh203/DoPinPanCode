package com.example.dopinpan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.dopinpan.Manager.HomeManagerActivity;

public class SplashManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_manager);
        Handler handler = new Handler();
        ProgressDialog dialog = new ProgressDialog(SplashManager.this);
        dialog.setMessage("Vui Lòng Đợi ...");
        dialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashManager.this, HomeManagerActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finishAffinity();
            }
        }, 3000);
    }
}