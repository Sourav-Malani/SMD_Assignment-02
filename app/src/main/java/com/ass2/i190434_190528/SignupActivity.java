package com.ass2.i190434_190528;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ass2.i190434_190528.Helper.HelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword, signupCountry, signupCity, signupPhone;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    private String[] countryNames = {"Pakistan", "USA"};
    private String[] pakistanCities = {"Lahore", "Karachi", "Islamabad"};
    private String[] usaCities = {"New York", "Los Angeles", "Chicago"};

    private AutoCompleteTextView cityAutoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.User_Name);
        signupEmail = findViewById(R.id.User_Email);
        signupUsername = findViewById(R.id.User_Username);
        signupPassword = findViewById(R.id.User_Password);
        signupButton = findViewById(R.id.btn_signUp);
        signupCountry = findViewById(R.id.drp_Country);
        signupCity = findViewById(R.id.drp_City);
        signupPhone = findViewById(R.id.User_Contact);

        loginRedirectText = findViewById(R.id.txt_login);
        //For Authentication
        mAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();
                String country = signupCountry.getText().toString();
                String city = signupCity.getText().toString();
                String phone = signupPhone.getText().toString();
                if(!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !username.isEmpty() && !country.isEmpty() && !city.isEmpty() && !phone.isEmpty() ){
                mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Add to the database here.
                            //For database
                            database = FirebaseDatabase.getInstance();
                            reference = database.getReference("users");

                            HelperClass helperClass = new HelperClass(name, email, username, password, country, city, phone);

                            // This will create a new child in the database with the username as the key
                            String safeUserEmail = email.replace(".", "_").replace("@", "_");
                            reference.child(safeUserEmail).setValue(helperClass); // This will create a new child in the database with the Email as the key



                            Toast.makeText(SignupActivity.this, "You have signup successfully!", Toast.LENGTH_LONG).show();

                            //TODO: Check if User is already logged in or not

                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
//                            if(isFirstRegistration()){
//                                startActivity(new Intent(SignupActivity.this, EditProfile.class));
//                            }
//                            else{
//                                startActivity(new Intent(SignupActivity.this, bottomnavigation.class));
//                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            if((((FirebaseAuthUserCollisionException) e).getErrorCode().equals("ERROR_EMAIL_ALREADY_IN_USE")))
                            {
                                signupEmail.setError("Email address already in use");
                                signupEmail.requestFocus();
                            }
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            if (((FirebaseAuthInvalidCredentialsException) e).getErrorCode().equals("ERROR_WRONG_PASSWORD"))
                            {
                                signupPassword.setError("Invalid password");
                                signupPassword.requestFocus();
                            }
                            if (((FirebaseAuthInvalidCredentialsException) e).getErrorCode().equals("ERROR_INVALID_EMAIL")) {
                                signupEmail.setError("incorrect email format");
                                signupEmail.requestFocus();

                            }
//                            Toast.makeText(
//                                    SignupActivity.this,
//                                    "Invalid email or password format",
//                                    Toast.LENGTH_LONG
//                            ).show();
                        } else {
                            Toast.makeText(
                                    SignupActivity.this,
                                    "Failed SignUp: " + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                });
            }
            else{
                    if(name.isEmpty()){
                        signupName.setError("Name cannot be empty");
                       // signupName.requestFocus();
                    }
                    if(email.isEmpty()){
                        signupEmail.setError("Email cannot be empty");
                        //signupEmail.requestFocus();
                    }
                    if(username.isEmpty()){
                        signupUsername.setError("Username cannot be empty");
                        //signupUsername.requestFocus();
                    }
                    if(password.isEmpty()){
                        signupPassword.setError("Password cannot be empty");
                        //signupPassword.requestFocus();
                    }
                    if(country.isEmpty()){
                        signupCountry.setError("Country cannot be empty");
                        //signupCountry.requestFocus();
                    }
                    if(city.isEmpty()){
                        signupCity.setError("City cannot be empty");
                        //signupCity.requestFocus();
                    }
                    if(phone.isEmpty()){
                        signupPhone.setError("Phone cannot be empty");
                        //signupPhone.requestFocus();
                    }
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
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(SignupActivity.this, android.R.layout.simple_list_item_1, pakistanCities);
                    cityAutoCompleteTextView.setAdapter(cityAdapter);
                    // If user changes the country then clear the city dropdown
                    cityAutoCompleteTextView.setText(""); // Clear any previously selected city
                    cityAutoCompleteTextView.setHint("Select City"); // Set the hint text

                } else if (selectedCountry.equals("USA")) {
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(SignupActivity.this, android.R.layout.simple_list_item_1, usaCities);
                    cityAutoCompleteTextView.setAdapter(cityAdapter);
                    // If a country other than Pakistan or USA is selected, clear the city dropdown
                    cityAutoCompleteTextView.setText(""); // Clear any previously selected city
                    cityAutoCompleteTextView.setHint("Select City"); // Set the hint text

                }
            }
        });


        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    public Boolean validateEmail(){
        String val = signupEmail.getText().toString();
        if (val.isEmpty()){
            signupEmail.setError("Email cannot be empty");
            return false;
        } else {
            signupEmail.setError(null);

            return true;
        }
    }
//    private boolean isFirstRegistration() {
//        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
//        return !sharedPrefs.getBoolean("profileCompleted", false);
//    }
}