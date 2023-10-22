package com.ass2.i190434_190528;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    String passwordFromDB, nameFromDB, cityFromDB, countryFromDB, emailFromDB, phoneFromDB, coverphotoURLFromDB, profilephotoURLFromDB;

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
        final String email = loginEmail.getText().toString();
        final String password = loginPassword.getText().toString();

        if (validateEmail() && validatePassword()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                checkUser(email, password);
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

    private void checkUser(final String email, final String password) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        final String safeUserEmail = emailToSafeKey(email);

        Query checkUserDatabase = reference.orderByChild("email").equalTo(email);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    passwordFromDB = snapshot.child(safeUserEmail).child("password").getValue(String.class);
                    if (passwordFromDB.equals(password)) {
                        // Data retrieval and shared preference updates
                        getDataFromDBAndSetPreferences();
                    } else {
                        displayError("Invalid Credentials");
                    }
                } else {
                    displayError("User does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayError("Database error");
            }
        });
    }

    private void getDataFromDBAndSetPreferences() {
        getDataFromDB(new DataCallback() {
            @Override
            public void onDataReceived() {
                setSharedPreference();
                navigateToHome();
            }
        });
    }

    private interface DataCallback {
        void onDataReceived();
    }

    private void getDataFromDB(final DataCallback callback) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        final String safeUserEmail = emailToSafeKey(loginEmail.getText().toString());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.child(safeUserEmail);
                    nameFromDB = userSnapshot.child("name").getValue(String.class);
                    cityFromDB = userSnapshot.child("city").getValue(String.class);
                    countryFromDB = userSnapshot.child("country").getValue(String.class);
                    emailFromDB = userSnapshot.child("email").getValue(String.class);
                    phoneFromDB = userSnapshot.child("phone").getValue(String.class);
                    coverphotoURLFromDB = userSnapshot.child("coverPhotoUrl").getValue(String.class);
                    profilephotoURLFromDB = userSnapshot.child("profilePhotoUrl").getValue(String.class);
                }

                callback.onDataReceived();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayError("Database error");
            }
        });
    }

    private String emailToSafeKey(String email) {
        return email.replace(".", "_").replace("@", "_");
    }

    private void setSharedPreference() {
        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        editor.putBoolean("isLogged", true);
        editor.putString("userID", userId);
        editor.putString("name", nameFromDB);
        editor.putString("city", cityFromDB);
        editor.putString("country", countryFromDB);
        editor.putString("email", emailFromDB);
        editor.putString("phone", phoneFromDB);
        editor.putString("coverPhotoUrl", coverphotoURLFromDB);
        editor.putString("profilePhotoUrl", profilephotoURLFromDB);
        editor.apply();
    }

    private void displayError(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, bottomnavigation.class);
        startActivity(intent);
        finish();
    }

    private void navigateToSignup() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}