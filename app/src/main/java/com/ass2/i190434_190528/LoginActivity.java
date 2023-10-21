package com.ass2.i190434_190528;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private TextView signupRedirectText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.User_Email);
        loginPassword = findViewById(R.id.User_Password);
        signupRedirectText = findViewById(R.id.txt_signUp);
        loginButton = findViewById(R.id.btn_signIn);

        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToHome();
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSignup();
            }
        });
    }

    private void loginUser() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if (validateEmail() && validatePassword()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful() && checkUser()) {
                                setSharedPreference();
                                navigateToHome();
                            } else {
                                displayError("Authentication failed.");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            displayError("Authentication failed.");
                        }
                    });
        } else {
            displayError("Please enter valid credentials.");
        }
    }

    private boolean validateEmail() {
        String val = loginEmail.getText().toString();
        if (val.isEmpty()) {
            loginEmail.setError("Username or Email cannot be empty");
            return false;
        } else {
            loginEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    private boolean checkUser() {
        final String userEmail = loginEmail.getText().toString().trim();
        final String userPassword = loginPassword.getText().toString().trim();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        final String safeUserEmail = userEmail.replace(".", "_").replace("@", "_");

        Query checkUserDatabase = reference.orderByChild("email").equalTo(userEmail);
        final boolean[] userExists = {false};

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String passwordFromDB = snapshot.child(safeUserEmail).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)) {
                        userExists[0] = true;
                    } else {
                        loginPassword.setError("Invalid Credentials");
                    }
                } else {
                    loginEmail.setError("User does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        return userExists[0];
    }
    private void setSharedPreference() {
        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        editor.putBoolean("isLogged", true);
        editor.putString("userID", userId);
        editor.apply();
    }

    private void displayError(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, bottomnavigation.class);
        startActivity(intent);
    }

    private void navigateToHomeWithUsername(String name) {
        Intent intent = new Intent(LoginActivity.this, bottomnavigation.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    private void navigateToSignup() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}