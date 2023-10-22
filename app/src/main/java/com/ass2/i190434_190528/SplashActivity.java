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
                    // Check if the user is logged in already.
                    SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
                    boolean isLogged = sharedPrefs.getBoolean("isLogged", false);

                    Intent intent = null;
                    if (isLogged) { // If the user is logged in already.
                        final String[] userEmail = {""}; // Final variable to store email

                        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper();
                        userDatabaseHelper.getUserData(mAuth.getCurrentUser().getEmail(), new UserDatabaseHelper.UserDataCallback() {
                            @Override
                            public void onUserDataReceived(HelperClass userData) {
                                userEmail[0] = userData.getEmail();
                            }

                            @Override
                            public void onUserDataError(String error) {
                                // Handle error if necessary
                            }
                        });

                        // Start the new activity inside this callback
                        intent = new Intent(SplashActivity.this, bottomnavigation.class);
                        intent.putExtra("userEmail", userEmail[0]);
                    } else { // If the user is not logged in.
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
