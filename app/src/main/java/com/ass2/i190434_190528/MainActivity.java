package com.ass2.i190434_190528;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText UserEmail,UserPassword;

    Button SignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        UserEmail = findViewById(R.id.User_Email);
        UserPassword = findViewById(R.id.User_Password);


        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("HomeFragment", "Current User's ID "+currentUser.getUid());
            // User is already logged in, navigate to the home screen
            startActivity(new Intent(MainActivity.this, bottomnavigation.class));
            finish(); // Close this activity
        }
        SignIn = findViewById(R.id.btn_signIn);

            SignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!UserEmail.getText().toString().isEmpty() && !UserPassword.getText().toString().isEmpty()) {
                        mAuth.signInWithEmailAndPassword(UserEmail.getText().toString(), UserPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Authentication successful.",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            // After successful authentication, navigate to the next activity
                                            Intent intent = new Intent(MainActivity.this, bottomnavigation.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(
                                                    MainActivity.this,
                                                    "Authentication failed.",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(
                                                MainActivity.this,
                                                "Login Failed",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                });
                    }
                    else{
                        Toast.makeText(
                                MainActivity.this,
                                "Please Enter Email and Password",
                                Toast.LENGTH_LONG
                        ).show();
                    }


                }
            });


    }

    @Override
    protected void onStart(){
        super.onStart();
/*
      //Toast User ID
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            Toast.makeText(
                    MainActivity.this,
                    user.getUid()+"",
                    Toast.LENGTH_LONG
            ).show();
        }
*/

    }
//    public void onSigninClicked(View view) {
//        Intent intent = new Intent(this, activity_main.class);
//        startActivity(intent);
//    }
}