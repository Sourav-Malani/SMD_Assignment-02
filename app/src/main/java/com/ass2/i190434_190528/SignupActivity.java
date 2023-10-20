package com.ass2.i190434_190528;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword, signupCountry, signupCity, signupPhone;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

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

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();
                String country = signupCountry.getText().toString();
                String city = signupCity.getText().toString();
                String phone = signupPhone.getText().toString();

                HelperClass helperClass = new HelperClass(name, email, username, password, country, city, phone);
                //HelperClass helperClass = new HelperClass(name, email, username, password, country, city, phone);
                //String safeUserEmail = email.replace(".", "_").replace("@", "_");

                // This will create a new child in the database with the username as the key
                String safeUserEmail = email.replace(".", "_").replace("@", "_");
                //String passwordFromDB = snapshot.child(safeUserEmail).child("password").getValue(String.class);

                reference.child(safeUserEmail).setValue(helperClass); // This will create a new child in the database with the Email as the key


                Toast.makeText(SignupActivity.this, "You have signup successfully!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
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
}