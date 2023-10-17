package com.ass2.i190434_190528;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onSignUpClicked(View view) {
        Intent intent = new Intent(this, registration.class);
        startActivity(intent);
    }
    public void onLogin_mainClicked(View view) {
        Intent intent = new Intent(this, bottomnavigation.class);
        startActivity(intent);
    }

//    public void onSigninClicked(View view) {
//        Intent intent = new Intent(this, activity_main.class);
//        startActivity(intent);
//    }
}