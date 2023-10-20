package com.ass2.i190434_190528;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(5000); // Display splash screen for 5 seconds
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        thread.start();
    }


}
