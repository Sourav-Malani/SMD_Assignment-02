package com.ass2.i190434_190528;

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
import androidx.fragment.app.Fragment;

import com.ass2.i190434_190528.Helper.HelperClass;
import com.ass2.i190434_190528.Helper.UserDatabaseHelper;
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

public class ProfileFragment extends Fragment {

    Button btn_signOut;
    FloatingActionButton btn_editCoverPhoto,btn_editProfilePhoto;
    ImageView editProfileButton,img_CoverPhoto,img_ProfilePhoto;
    TextView userName;
    FirebaseAuth mAuth;
    int COVER_PHOTO_REQUEST_CODE=100;
    int PROFILE_PHOTO_REQUEST_CODE = 200;
    String url_ProfilePhoto,url_CoverPhoto;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
    String safeUserEmail,nameFromDB;
    String name, city, country, email, phone, coverPhotoUrl, profilePhotoUrl;

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

        userName = view.findViewById(R.id.txt_UserName);
        userName.setText("Daud");


        mAuth = FirebaseAuth.getInstance();

        //Check if instance is not null
        if (mAuth.getCurrentUser() != null) {
            //Get user email and replace all the "." and "@" with "_"
            String userEmail = mAuth.getCurrentUser().getEmail().toString();
            safeUserEmail = userEmail.replace(".", "_").replace("@", "_");
            retrieveUserData();
            userName.setText(name);

            //getandSetUserNameFromDB(mAuth);
        }
        else {
            Toast.makeText(getContext(), "User Instance null", Toast.LENGTH_LONG).show();
        }
        userName.setText(nameFromDB);
        retrieveUserData();

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
                // Create an intent to open the Edit_Profile.xml (layout) activity
                Intent intent = new Intent(getActivity(), EditProfile.class);
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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PROFILE_PHOTO_REQUEST_CODE); // Define PROFILE_PHOTO_REQUEST_CODE
            }
        });

        btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out the user from Firebase Auth
                mAuth.signOut();

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
        if (requestCode == COVER_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri image = data.getData();
            img_CoverPhoto.setImageURI(image);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference("users").child(safeUserEmail + "/CoverPhoto.png");
            reference.putFile(image)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(getContext(), "Cover Photo updated", Toast.LENGTH_LONG).show();
                                    url_CoverPhoto = uri.toString();
                                    // Update the cover photo URL in SharedPreferences
                                    coverPhotoUrl = url_CoverPhoto;
                                    saveImageUrlsToSharedPreferences();

                                    // Add to the Firebase Realtime Database
                                    updateCoverPhotoURLInDatabase(url_CoverPhoto);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
        if (requestCode == PROFILE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri image = data.getData();
            img_ProfilePhoto.setImageURI(image); // Update the profile photo ImageView

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference("users").child(safeUserEmail + "/ProfilePhoto.png");
            reference.putFile(image)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(getContext(), "Profile Photo updated", Toast.LENGTH_LONG).show();
                                    url_ProfilePhoto = uri.toString();
                                    // Update the profile photo URL in SharedPreferences
                                    profilePhotoUrl = url_ProfilePhoto;
                                    saveImageUrlsToSharedPreferences();

                                    // Add to the Firebase Realtime Database
                                    updateProfilePhotoURLInDatabase(url_ProfilePhoto);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    public void EditProfileClicked(View view) {

    }
    private void updateCoverPhotoURLInDatabase(String URL) {
        //Add Url to the Firebase Realtime Database.
        mAuth = FirebaseAuth.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail().toString();

        safeUserEmail = userEmail.replace(".", "_").replace("@", "_");

        String uniqueUserId = safeUserEmail; // Adjust this based on your user identifier

        // After successfully obtaining the cover photo URL
        String coverPhotoUrl = URL; // The cover photo URL

        // Update the user's data in the Realtime Database. (creates a new field for cover PhotoUrl)
        reference.child(uniqueUserId).child("coverPhotoUrl").setValue(coverPhotoUrl);

    }
    private void updateProfilePhotoURLInDatabase(String URL) {
        //Add Url to the Firebase Realtime Database.
        mAuth = FirebaseAuth.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail().toString();

        safeUserEmail = userEmail.replace(".", "_").replace("@", "_");

        String uniqueUserId = safeUserEmail; // Adjust this based on your user identifier

        // After successfully obtaining the profile photo URL
        String profilePhotoUrl = URL; // The profile photo URL

        // Update the user's data in the Realtime Database. (creates a new field for profile PhotoUrl)
        reference.child(uniqueUserId).child("profilePhotoUrl").setValue(profilePhotoUrl);
    }

    private String getandSetUserNameFromDB(FirebaseAuth mAuth){
        //FirebaseAuth mAuth = FirebaseAuth.getInstance();
        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper();
        String userEmail = mAuth.getCurrentUser().getEmail().toString();
        userDatabaseHelper.getUserData(userEmail, new UserDatabaseHelper.UserDataCallback() {
            @Override
            public void onUserDataReceived(HelperClass userData) {
                nameFromDB= userData.getName().toString();
                userName.setText(nameFromDB);
            }
            @Override
            public void onUserDataError(String error) {
            }
        });
        return nameFromDB;

    }

    private void retrieveUserData() {
        SharedPreferences sharedPrefs = requireActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);

        // Retrieve the data you stored
        name = sharedPrefs.getString("name", "");
        city = sharedPrefs.getString("city", "");
        country = sharedPrefs.getString("country", "");
        email = sharedPrefs.getString("email", "");
        phone = sharedPrefs.getString("phone", "");
        coverPhotoUrl = sharedPrefs.getString("coverPhotoUrl", "");
        profilePhotoUrl = sharedPrefs.getString("profilePhotoUrl", "");
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
