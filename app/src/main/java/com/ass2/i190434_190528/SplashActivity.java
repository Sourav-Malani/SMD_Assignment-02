package com.ass2.i190434_190528;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import androidx.appcompat.app.AppCompatActivity;

import com.ass2.i190434_190528.config.Config;

public class SplashActivity extends AppCompatActivity {

    //private String apiUrl = "http://192.168.18.114/Ass02API/get_user_data.php"; // Replace with your API URL
    private String apiUrl = Config.API_BASE_URL + "get_user_data.php"; // Use the API base URL from Config class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        checkLoggedInUser();
    }

    private void checkLoggedInUser() {
        SharedPreferences sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        boolean isLogged = sharedPrefs.getBoolean("isLogged", false);

        if (isLogged) {
            String userEmail = sharedPrefs.getString("email", ""); // Get user email from shared preferences

            // Prepare data for sending
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("email", userEmail);

            // Send data to server
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(apiUrl); // Use your API URL here
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
                                    handleUserDataResponse(sb.toString());
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startLoginActivity();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startLoginActivity();
                            }
                        });
                    }
                }
            }).start();
        } else {
            startLoginActivity();
        }
    }

    private void handleUserDataResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String status = jsonResponse.getString("status");
            if ("success".equals(status)) {
                // User data is retrieved successfully
                String name = jsonResponse.getString("name");
                String city = jsonResponse.getString("city");
                String country = jsonResponse.getString("country");
                String email = jsonResponse.getString("email");
                String phone = jsonResponse.getString("phone");
                String profilePhotoUrl = jsonResponse.optString("profile_photo_url", "");
                String coverPhotoUrl = jsonResponse.optString("cover_photo_url", "");

                // Start the new activity with user data
                Intent intent = new Intent(SplashActivity.this, bottomnavigation.class);
                intent.putExtra("userEmail", email);
                intent.putExtra("userName", name);
                intent.putExtra("userCity", city);
                intent.putExtra("userCountry", country);
                intent.putExtra("userPhone", phone);
                intent.putExtra("userProfilePhotoUrl", profilePhotoUrl);
                intent.putExtra("userCoverPhotoUrl", coverPhotoUrl);
                startActivity(intent);
                finish();
            } else {
                // User data retrieval failed, start LoginActivity
                startLoginActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        // Start LoginActivity
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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
}