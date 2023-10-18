package com.ass2.i190434_190528;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class registration extends AppCompatActivity {

    // Define arrays for country names and city names
    private String[] countryNames = {"Pakistan", "USA"};
    private String[] pakistanCities = {"Lahore", "Karachi", "Islamabad"};
    private String[] usaCities = {"New York", "Los Angeles", "Chicago"};

    private AutoCompleteTextView cityAutoCompleteTextView;
    EditText UserName,UserEmail,UserContact,UserCountry,UserCity,UserPassword;
    Button btn_SignUp;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        mAuth = FirebaseAuth.getInstance();
        UserName = findViewById(R.id.User_Name);
        UserEmail = findViewById(R.id.User_Email);
        UserContact = findViewById(R.id.User_Contact);
        UserCountry = findViewById(R.id.drp_Country);
        UserCity = findViewById(R.id.drp_City);
        UserPassword = findViewById(R.id.User_Password);

        btn_SignUp = findViewById(R.id.btn_signUp);



            btn_SignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!UserEmail.getText().toString().isEmpty() && !UserPassword.getText().toString().isEmpty()){ //Email and password not Empty
                        mAuth.createUserWithEmailAndPassword(UserEmail.getText().toString(),UserPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            if (isFirstRegistration()) {
                                                startActivity(new Intent(registration.this, EditProfile.class));
                                            }
                                            else{
                                                startActivity(new Intent(registration.this, bottomnavigation.class));

                                            }
                                            Toast.makeText(
                                                    registration.this,
                                                    "SignUp Successfull",
                                                    Toast.LENGTH_LONG).show();
                                            Toast.makeText(
                                                    registration.this,
                                                    mAuth.getCurrentUser().getUid(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }//omComplete
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        if (e instanceof FirebaseAuthUserCollisionException) {
                                            Toast.makeText(
                                                    registration.this,
                                                    "Email address already in use",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                            Toast.makeText(
                                                    registration.this,
                                                    "Invalid email or password format",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        } else {
                                            Toast.makeText(
                                                    registration.this,
                                                    "Failed SignUp: " + e.getMessage(),
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    }
                                });
                    }//Else
                    else{
                        Toast.makeText(
                                registration.this,
                                "Email or Password is Empty",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
            });



        // Initialize the country dropdown
        AutoCompleteTextView countryAutoCompleteTextView = findViewById(R.id.drp_Country);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countryNames);
        countryAutoCompleteTextView.setAdapter(countryAdapter);

        // Initialize the city dropdown
        cityAutoCompleteTextView = findViewById(R.id.drp_City);
        cityAutoCompleteTextView.setHint("Select City");

        // Set up an item selection listener for the country dropdown
        countryAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCountry = parent.getItemAtPosition(position).toString();

                // Update the city dropdown based on the selected country
                if (selectedCountry.equals("Pakistan")) {
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(registration.this, android.R.layout.simple_list_item_1, pakistanCities);
                    cityAutoCompleteTextView.setAdapter(cityAdapter);
                    // If user changes the country then clear the city dropdown
                    cityAutoCompleteTextView.setText(""); // Clear any previously selected city
                    cityAutoCompleteTextView.setHint("Select City"); // Set the hint text

                } else if (selectedCountry.equals("USA")) {
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(registration.this, android.R.layout.simple_list_item_1, usaCities);
                    cityAutoCompleteTextView.setAdapter(cityAdapter);
                    // If a country other than Pakistan or USA is selected, clear the city dropdown
                    cityAutoCompleteTextView.setText(""); // Clear any previously selected city
                    cityAutoCompleteTextView.setHint("Select City"); // Set the hint text

                }
            }
        });

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
    private boolean isFirstRegistration() {
        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        return !sharedPrefs.getBoolean("profileCompleted", false);
    }
}
