package com.ass2.i190434_190528;

import android.content.Intent;
import android.content.SharedPreferences;
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
                    SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
                    boolean isLogged = sharedPrefs.getBoolean("isLogged", false);

                    Intent intent;
                    if(isLogged){
                        intent = new Intent(SplashActivity.this, bottomnavigation.class);
                    }
                    else{
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }


}
