package com.ass2.i190434_190528;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.controls.actions.FloatAction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.ass2.i190434_190528.Helper.HelperClass;
import com.ass2.i190434_190528.Helper.UserDatabaseHelper;
import com.ass2.i190434_190528.HttpService.FileUtils;
import com.ass2.i190434_190528.HttpService.HttpService;
import com.ass2.i190434_190528.HttpService.RetrofitBuilder;
import com.ass2.i190434_190528.HttpService.UserProfileModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    Button btn_signOut;
    FloatingActionButton btn_editCoverPhoto,btn_editProfilePhoto;
    ImageView editProfileButton,img_CoverPhoto,img_ProfilePhoto;
    TextView userName;
    //FirebaseAuth mAuth;
    int COVER_PHOTO_REQUEST_CODE=100;
    int PROFILE_PHOTO_REQUEST_CODE = 200;
    String url_ProfilePhoto,url_CoverPhoto;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
    String safeUserEmail,nameFromDB;
    String name, city, country, email, phone, coverPhotoUrl, profilePhotoUrl;

    Boolean isloggedin;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Find the editProfileButton
        //Button editProfileButton = view.findViewById(R.id.editProfileButton);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        btn_editCoverPhoto = view.findViewById(R.id.btn_editCoverPhoto);
        btn_editProfilePhoto = view.findViewById(R.id.btn_editProfilePhoto);
        img_CoverPhoto = view.findViewById(R.id.img_CoverPhoto);
        img_ProfilePhoto = view.findViewById(R.id.img_ProfileImage);

        requirePermission();
        retrieveUserData();

        userName = view.findViewById(R.id.txt_UserName);
        userName.setText("Daud");


        //mAuth = FirebaseAuth.getInstance();

        //Check if instance is not null
        if (isloggedin) {
            //Get user email and replace all the "." and "@" with "_"
            //String userEmail = mAuth.getCurrentUser().getEmail().toString();
            //retrieveUserData();
            userName.setText(name);

            //getandSetUserNameFromDB(mAuth);
        }
        else {
            Toast.makeText(getContext(), "User Instance null", Toast.LENGTH_LONG).show();
        }
        //userName.setText(nameFromDB);
        //retrieveUserData();

        // Use Picasso to load and display the cover photo URL
        if (!coverPhotoUrl.isEmpty()) {
            Picasso.get().load(coverPhotoUrl).into(img_CoverPhoto);
        }

        // Use Picasso to load and display the profile photo URL
        if (!profilePhotoUrl.isEmpty()) {
            Picasso.get().load(profilePhotoUrl).into(img_ProfilePhoto);
        }
        btn_signOut = view.findViewById(R.id.btn_signOut);

        // Set an OnClickListener for the editProfileButton
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the EditProfileActivity
                Intent intent = new Intent(getContext(), EditProfileActivity.class);

                // Put the data as extras in the intent
                intent.putExtra("name", name); // Replace with the actual data you want to pass
                intent.putExtra("city", city);
                intent.putExtra("country", country);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);

                // Start the EditProfileActivity with the intent
                startActivity(intent);


            }
        });

        btn_editCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,COVER_PHOTO_REQUEST_CODE);
            }
        });
        btn_editProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //Intent intent = new Intent();
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery, PROFILE_PHOTO_REQUEST_CODE); // Define PROFILE_PHOTO_REQUEST_CODE
            }
        });

        btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out the user from Firebase Auth
                //mAuth.signOut();

                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userID", ""); // Make user id empty.
                editor.putBoolean("isLogged", false); //logout user.
                editor.apply();

                // Navigate to the login screen
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                if (requestCode == COVER_PHOTO_REQUEST_CODE) {
                    img_CoverPhoto.setImageURI(selectedImageUri);
                    coverPhotoUrl = FileUtils.getPath(getContext(), selectedImageUri);
                    saveImageUrlsToSharedPreferences();
                    // Upload the cover photo
                    uploadFileToServer(coverPhotoUrl, "cover");
                } else if (requestCode == PROFILE_PHOTO_REQUEST_CODE) {
                    img_ProfilePhoto.setImageURI(selectedImageUri);
                    profilePhotoUrl = FileUtils.getPath(getContext(), selectedImageUri);
                    saveImageUrlsToSharedPreferences();
                    // Upload the profile photo
                    uploadFileToServer(profilePhotoUrl, "profile");
                }
            }
        }
    }

    public Uri getImageUri(Context context, Bitmap bitmap){
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "myImage","");

        return Uri.parse(path);
    }


    public void requirePermission(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{READ_EXTERNAL_STORAGE},1);
    }

    public void uploadFileToServer(String url, String imageType) {
        File file = new File(Uri.parse(url).getPath());

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("sendimage", file.getName(), requestBody);

        // Add user email and image type to the request
        RequestBody typeBody = RequestBody.create(MediaType.parse("text/plain"), imageType);
        // Add user email to the request
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);

        HttpService service = RetrofitBuilder.getClient().create(HttpService.class);

        Call<UserProfileModel> call = service.callUploadApi(filePart, emailBody, typeBody);
        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfileModel fileModel = response.body();
                    Toast.makeText(getContext(), fileModel.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the case where the response body is null
                    Toast.makeText(getContext(), "Response is not successful or is empty", Toast.LENGTH_SHORT).show();
                    if (response.errorBody() != null) {
                        try {
                            // Log or display the error body for debugging
                            String errorString = response.errorBody().string();
                            Toast.makeText(getContext(), errorString, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                if (t instanceof IOException) {
                    // Handle network or server error
                    Toast.makeText(getContext(), "Network or server error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (t instanceof HttpException) {
                    // Handle HTTP errors
                    Response<?> response = ((HttpException) t).response();
                    try {
                        if (response != null && response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Toast.makeText(getContext(), "Server response: " + errorBody, Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle other types of errors
                    Toast.makeText(getContext(), "Unknown error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void EditProfileClicked(View view) {

    }
//    private void updateCoverPhotoURLInDatabase(String URL) {
//        //Add Url to the Firebase Realtime Database.
//        mAuth = FirebaseAuth.getInstance();
//        String userEmail = mAuth.getCurrentUser().getEmail().toString();
//
//        safeUserEmail = userEmail.replace(".", "_").replace("@", "_");
//
//        String uniqueUserId = safeUserEmail; // Adjust this based on your user identifier
//
//        // After successfully obtaining the cover photo URL
//        String coverPhotoUrl = URL; // The cover photo URL
//
//        // Update the user's data in the Realtime Database. (creates a new field for cover PhotoUrl)
//        reference.child(uniqueUserId).child("coverPhotoUrl").setValue(coverPhotoUrl);
//
//    }
//    private void updateProfilePhotoURLInDatabase(String URL) {
//        //Add Url to the Firebase Realtime Database.
//        mAuth = FirebaseAuth.getInstance();
//        String userEmail = mAuth.getCurrentUser().getEmail().toString();
//
//        safeUserEmail = userEmail.replace(".", "_").replace("@", "_");
//
//        String uniqueUserId = safeUserEmail; // Adjust this based on your user identifier
//
//        // After successfully obtaining the profile photo URL
//        String profilePhotoUrl = URL; // The profile photo URL
//
//        // Update the user's data in the Realtime Database. (creates a new field for profile PhotoUrl)
//        reference.child(uniqueUserId).child("profilePhotoUrl").setValue(profilePhotoUrl);
//    }

//    private String getandSetUserNameFromDB(FirebaseAuth mAuth){
//        //FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper();
//        String userEmail = mAuth.getCurrentUser().getEmail().toString();
//        userDatabaseHelper.getUserData(userEmail, new UserDatabaseHelper.UserDataCallback() {
//            @Override
//            public void onUserDataReceived(HelperClass userData) {
//                nameFromDB= userData.getName().toString();
//                userName.setText(nameFromDB);
//            }
//            @Override
//            public void onUserDataError(String error) {
//            }
//        });
//        return nameFromDB;
//
//    }

    private void retrieveUserData() {
        SharedPreferences sharedPrefs = requireActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);

        // Retrieve the data you stored
        isloggedin = sharedPrefs.getBoolean("isLogged", false);
        name = sharedPrefs.getString("name", "");
        city = sharedPrefs.getString("city", "");
        country = sharedPrefs.getString("country", "");
        email = sharedPrefs.getString("email", "");
        phone = sharedPrefs.getString("phone", "");
        coverPhotoUrl = sharedPrefs.getString("cover_photo_url", "");
        profilePhotoUrl = sharedPrefs.getString("profile_photo_url", "");
    }

    // Function to save image URLs to SharedPreferences
    private void saveImageUrlsToSharedPreferences() {
        SharedPreferences sharedPrefs = requireActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("coverPhotoUrl", coverPhotoUrl);
        editor.putString("profilePhotoUrl", profilePhotoUrl);
        editor.apply();
    }
}
