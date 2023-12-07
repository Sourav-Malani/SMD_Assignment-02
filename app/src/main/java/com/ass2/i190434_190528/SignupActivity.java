package com.ass2.i190434_190528;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ass2.i190434_190528.config.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword, signupCountry, signupCity, signupPhone;
    TextView loginRedirectText;
    Button signupButton;

    private String[] countryNames = {"Pakistan", "USA"};
    private String[] pakistanCities = {"Lahore", "Karachi", "Islamabad"};
    private String[] usaCities = {"New York", "Los Angeles", "Chicago"};

    private AutoCompleteTextView cityAutoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize your views here
        initializeViews();

        // Setup listeners
        setupListeners();

        final Handler handler = new Handler();
        final Runnable checkConnection = new Runnable() {
            @Override
            public void run() {
                checkInternetConnection();
                handler.postDelayed(this, 2000); // Check every 2 seconds
            }
        };
        handler.post(checkConnection);
    }

    private void initializeViews() {
        signupName = findViewById(R.id.User_Name);
        signupEmail = findViewById(R.id.User_Email);
        signupUsername = findViewById(R.id.User_Username);
        signupPassword = findViewById(R.id.User_Password);
        signupCountry = findViewById(R.id.drp_Country);
        signupCity = findViewById(R.id.drp_City);
        signupPhone = findViewById(R.id.User_Contact);
        signupButton = findViewById(R.id.btn_signUp);
        loginRedirectText = findViewById(R.id.txt_login);

        // Initialize the country and city dropdowns
        initializeDropdowns();
    }

    private void initializeDropdowns() {
        AutoCompleteTextView countryAutoCompleteTextView = findViewById(R.id.drp_Country);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countryNames);
        countryAutoCompleteTextView.setAdapter(countryAdapter);

        cityAutoCompleteTextView = findViewById(R.id.drp_City);
        cityAutoCompleteTextView.setHint("Select City");

        countryAutoCompleteTextView = findViewById(R.id.drp_Country);
        countryAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String country = countryNames[position];
            if (country.equals("Pakistan")) {
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pakistanCities);
                cityAutoCompleteTextView.setAdapter(cityAdapter);
            } else if (country.equals("USA")) {
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usaCities);
                cityAutoCompleteTextView.setAdapter(cityAdapter);
            }
        });
    }

    private void setupListeners() {
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
            }
        });
    }

    private void attemptSignup() {
        String name = signupName.getText().toString();
        String email = signupEmail.getText().toString();
        String username = signupUsername.getText().toString();
        String password = signupPassword.getText().toString();
        String country = signupCountry.getText().toString();
        String city = cityAutoCompleteTextView.getText().toString();
        String phone = signupPhone.getText().toString();

        // Placeholder URLs for profile and cover photos (replace with actual URLs)
        String profilePhotoUrl = "https://example.com/profile.jpg";
        String coverPhotoUrl = "https://example.com/cover.jpg";

        if (validateInputs(name, email, username, password, country, city, phone)) {
            // Pass profile and cover photo URLs to registerUser method
            registerUser(name, email, username, password, country, city, phone, profilePhotoUrl, coverPhotoUrl);
        }
    }

    private boolean validateInputs(String... inputs) {
        // Add your validation logic here
        // Return false if any input is invalid
        return true;
    }

    private void registerUser(String name, String email, String username, String password, String country, String city, String phone, String profilePhotoUrl, String coverPhotoUrl) {
        // Prepare data for sending
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("name", name);
        postDataParams.put("email", email);
        postDataParams.put("username", username);
        postDataParams.put("password", password);
        postDataParams.put("country", country);
        postDataParams.put("city", city);
        postDataParams.put("phone", phone);
        postDataParams.put("profile_photo_url", profilePhotoUrl); // Include profile photo URL
        postDataParams.put("cover_photo_url", coverPhotoUrl); // Include cover photo URL

        // Send data to server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //URL url = new URL("http://192.168.18.114/Ass02API/register.php"); // Use your IP and path
                    URL url = new URL(Config.API_BASE_URL + "register.php");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    Log.d("SignupActivity", "Response Code: " + responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line;

                        while ((line = in.readLine()) != null) {
                            sb.append(line);
                            break;
                        }

                        in.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignupActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                                if (sb.toString().contains("successfully")) {
                                    navigateToLogin();
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignupActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("SignupActivity", "Exception: " + e.getMessage());
                }
            }
        }).start();
    }

    private String getPostDataString(HashMap<String, String> params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }



    private void checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        View indicator = findViewById(R.id.internet_status_indicator);
        if (isConnected) {
            indicator.setBackgroundResource(R.drawable.indicator_connected);
        } else {
            indicator.setBackgroundResource(R.drawable.indicator_disconnected);
        }
    }
}