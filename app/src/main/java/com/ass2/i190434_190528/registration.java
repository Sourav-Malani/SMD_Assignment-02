package com.ass2.i190434_190528;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        // Add any additional initialization or logic for this activity here
    }
    public void onLoginClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void registration_SignUp_Clicked(View view) {
        Intent intent = new Intent(this, EmailVerification.class);
        startActivity(intent);
    }


}
