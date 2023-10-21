package com.ass2.i190434_190528;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.ass2.i190434_190528.Helper.HelperClass;
import com.ass2.i190434_190528.Helper.UserDatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    String email;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(5); // Display splash screen for 5 seconds
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //Check if user is loggedIn Already.
                    SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
                    final boolean[] isLogged = {sharedPrefs.getBoolean("isLogged", false)};
                    String userID = sharedPrefs.getString("userID", "");







                    Intent intent = null;
                    if(isLogged[0]){
                        //get email from db using sharedPrefs.
                        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper();
                        String userEmail = mAuth.getCurrentUser().getEmail().toString();
                        userDatabaseHelper.getUserData(userEmail, new UserDatabaseHelper.UserDataCallback() {
                            @Override
                            public void onUserDataReceived(HelperClass userData) {
                                email= userData.getEmail();
                            }
                            @Override
                            public void onUserDataError(String error) {
                            }
                        });
                        intent.putExtra("userEmail", email);
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
