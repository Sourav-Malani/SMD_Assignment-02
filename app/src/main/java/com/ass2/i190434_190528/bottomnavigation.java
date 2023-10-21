
package com.ass2.i190434_190528;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ass2.i190434_190528.Helper.HelperClass;
//import com.ass2.i190434_190528.Helper.UserDataManager;
import com.ass2.i190434_190528.Helper.UserDatabaseHelper;
import com.ass2.i190434_190528.databinding.BottomNavigationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class bottomnavigation extends AppCompatActivity {
    BottomNavigationBinding binding;
    //private UserDataManager userDataManager;
    String nameFromDB;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private boolean dataLoaded = false; // Flag to track if user data has been loaded
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BottomNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        HomeFragment fragment = HomeFragment.newInstance(nameFromDB);

        // Get User's name from DB.
        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper();
        String userEmail = mAuth.getCurrentUser().getEmail().toString();
        userDatabaseHelper.getUserData(userEmail, new UserDatabaseHelper.UserDataCallback() {
            @Override
            public void onUserDataReceived(HelperClass userData) {
                nameFromDB = userData.getName();

                // Once you have the nameFromDB, you can set up your HomeFragment here.
                fragment.setName(nameFromDB); // Assuming you have a method to set the name in HomeFragment

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .commit();

                Log.d("HomeFragment", "Bottom: nameFromDB " + nameFromDB);
            }

            @Override
            public void onUserDataError(String error) {
                // Handle error here.
            }
        });

        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                // You can access nameFromDB here after it's been set in the callback.
                Log.d("HomeFragment", "Bottom: nameFromDB " + nameFromDB);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .commit();
            } else if (item.getItemId() == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if (item.getItemId() == R.id.chat) {
                replaceFragment(new ChatFragment());
            } else if (item.getItemId() == R.id.user) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    public void onPlusClicked(View view) {
        Intent intent = new Intent(this, FragmentAddItem.class);
        startActivity(intent);
    }
    private void updateUI() {
        if (dataLoaded) {
            HomeFragment fragment = HomeFragment.newInstance(nameFromDB);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
        }
    }
}
