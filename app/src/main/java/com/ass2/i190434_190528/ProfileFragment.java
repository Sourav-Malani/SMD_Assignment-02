package com.ass2.i190434_190528;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.SharedPreferences;

public class ProfileFragment extends Fragment {

    Button btn_signOut;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Find the editProfileButton
        //Button editProfileButton = view.findViewById(R.id.editProfileButton);
        ImageView editProfileButton = view.findViewById(R.id.editProfileButton);
        // Set an OnClickListener for the editProfileButton
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to open the Edit_Profile.xml (layout) activity
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });
        btn_signOut = view.findViewById(R.id.btn_signOut);
        btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out the user from Firebase Auth
                FirebaseAuth.getInstance().signOut();



//                FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                FirebaseUser user = mAuth.getCurrentUser();
//                String userId = user.getUid();

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

    public void EditProfileClicked(View view) {

    }
}
