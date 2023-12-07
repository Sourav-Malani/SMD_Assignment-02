package com.ass2.i190434_190528;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ass2.i190434_190528.HttpService.HttpService;
import com.ass2.i190434_190528.HttpService.RetrofitBuilder;
import com.ass2.i190434_190528.HttpService.UserProfileModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    EditText editName, editEmail, editUsername, editPassword;
    Button saveButton;
    String nameUser, emailUser, usernameUser, passwordUser;
    HttpService httpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        httpService = RetrofitBuilder.getClient().create(HttpService.class);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);

        //showData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNameChanged() || isEmailChanged() || isPasswordChanged()) {
                    // Call the API to update the user's profile
                    updateUserProfile();
                } else {
                    Toast.makeText(EditProfileActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Check if the name has been changed
// Check if the name has been changed
    public boolean isNameChanged() {
        if (nameUser == null || !nameUser.equals(editName.getText().toString())) {
            if (nameUser == null) {
                nameUser = "";
            }
            nameUser = editName.getText().toString();
            return true;
        } else {
            return false;
        }
    }


    // Check if the email has been changed
    public boolean isEmailChanged() {
        if (!emailUser.equals(editName.getText().toString())) {
            //reference.child(usernameUser).child("email").setValue(editEmail.getText().toString());
            emailUser = editEmail.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    // Check if the password has been changed
    public boolean isPasswordChanged() {
        if (!passwordUser.equals(editPassword.getText().toString())) {
            //reference.child(usernameUser).child("password").setValue(editPassword.getText().toString());
            passwordUser = editPassword.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    // Show user data in EditText fields

    // Method to send a POST request to update the profile
// Method to send a POST request to update the profile
    private void updateUserProfile() {
        // Prepare the request body with updated data
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), editName.getText().toString());
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), editEmail.getText().toString());
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), editPassword.getText().toString());

        // Make the API call to update the profile using the instance of httpService
        Call<UserProfileModel> call = httpService.callUpdateProfile(nameBody, emailBody, passwordBody);
        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfileModel result = response.body();
                    // Handle the API response, e.g., show a success message
                    Toast.makeText(EditProfileActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    // Handle API errors here
                    Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                // Handle network or other errors here
                Log.e("Network Error", t.getMessage(), t); // Log the error
                Toast.makeText(EditProfileActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
