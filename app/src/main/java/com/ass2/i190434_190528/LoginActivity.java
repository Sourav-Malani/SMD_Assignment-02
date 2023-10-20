package com.ass2.i190434_190528;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword, loginEmail;
    Button loginButton;
    TextView signupRedirectText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.User_Email);
        loginPassword = findViewById(R.id.User_Password);
        signupRedirectText = findViewById(R.id.txt_signUp);
        loginButton = findViewById(R.id.btn_signIn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()){

                } else {
                    checkUser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateUsername(){
        String val = loginEmail.getText().toString();
        if (val.isEmpty()){
            loginEmail.setError("Username or Email cannot be empty");
            return false;
        } else {
            loginEmail.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()){
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userEmail = loginEmail.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        String safeUserEmail = userEmail.replace(".", "_").replace("@", "_");

        Query checkUserDatabase = reference.orderByChild("email").equalTo(userEmail);
        Log.d("HomeFragment", "Emails "+safeUserEmail+" "+userEmail);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String safeUserEmail = userEmail.replace(".", "_").replace("@", "_");

                if (snapshot.exists()){
                    loginEmail.setError(null);
                    String passwordFromDB = snapshot.child(safeUserEmail).child("password").getValue(String.class);
                    Log.d("HomeFragment", "Passwords "+passwordFromDB+" "+userPassword);

                    if (passwordFromDB.equals(userPassword)){
                        loginEmail.setError(null);

                        //Pass the data using intent
                        String nameFromDB = snapshot.child(safeUserEmail).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(safeUserEmail).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(safeUserEmail).child("username").getValue(String.class);


                        Intent intent = new Intent(LoginActivity.this, bottomnavigation.class);
                        intent.putExtra("name", nameFromDB);
                        Log.d("HomeFragment", "nameFromDB LA " + nameFromDB);

                        startActivity(intent);

                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                } else {

                    loginEmail.setError("User does not exist");
                    loginEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}