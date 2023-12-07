package com.ass2.i190434_190528;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ass2.i190434_190528.config.Config;

import org.json.JSONObject;

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

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private TextView signupRedirectText;

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
            attemptLogin(email, password);
        } else {
            displayError("Please enter valid credentials.");
        }
    }

    private boolean validateEmail() {
        String val = loginEmail.getText().toString();
        if (val.isEmpty()) {
            loginEmail.setError("Email cannot be empty");
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

    private void attemptLogin(String email, String password) {
        // Prepare data for sending
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("email", email);
        postDataParams.put("password", password);

        // Send data to server
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //URL url = new URL("http://192.168.18.114/Ass02API/login.php"); // Use your IP and path
                    URL url = new URL(Config.API_BASE_URL + "login.php");

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
                                handleLoginResponse(sb.toString());
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayError("Login failed");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleLoginResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String status = jsonResponse.getString("status");
            if ("success".equals(status)) {
                // Store user data in SharedPreferences
                setSharedPreference(jsonResponse);
                navigateToHome();
            } else {
                String message = jsonResponse.getString("message");
                displayError(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
            displayError("Error parsing response");
        }
    }

    private void setSharedPreference(JSONObject userData) {
        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        try {
            editor.putBoolean("isLogged", true);
            editor.putString("name", userData.getString("name"));
            editor.putString("city", userData.getString("city"));
            editor.putString("country", userData.getString("country"));
            editor.putString("email", userData.getString("email"));
            editor.putString("phone", userData.getString("phone"));

            // Add other user data as needed
            // Store profile and cover photo URLs if available
            if (userData.has("profile_photo_url")) {
                editor.putString("profile_photo_url", userData.getString("profile_photo_url"));
            }
            if (userData.has("cover_photo_url")) {
                editor.putString("cover_photo_url", userData.getString("cover_photo_url"));
            }

            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void displayError(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToHome() {
        // Navigate to the Home activity
        Intent intent = new Intent(LoginActivity.this, bottomnavigation.class);
        startActivity(intent);
        finish();
    }

    private void navigateToSignup() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}