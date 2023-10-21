package com.ass2.i190434_190528.Helper;

import com.ass2.i190434_190528.Helper.HelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserDatabaseHelper {
    private DatabaseReference databaseReference;

    public UserDatabaseHelper() {
        // Initialize the database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");
    }

    // Getter and Setter functions for user data
    // You can add more fields as per your database structure
    public void setUserName(String email, String userName) {
        String safeUserEmail = emailToSafeKey(email);
        databaseReference.child(safeUserEmail).child("username").setValue(userName);
    }

    public void setCity(String email, String city) {
        String safeUserEmail = emailToSafeKey(email);
        databaseReference.child(safeUserEmail).child("city").setValue(city);
    }

    public void setCountry(String email, String country) {
        String safeUserEmail = emailToSafeKey(email);
        databaseReference.child(safeUserEmail).child("country").setValue(country);
    }

    // Define a callback interface to get the user data
    public interface UserDataCallback {
        void onUserDataReceived(HelperClass userData);
        void onUserDataError(String error);
    }

    // Retrieve user data for a specific user using their email
    public void getUserData(String email, final UserDataCallback callback) {
        String safeUserEmail = emailToSafeKey(email);
        Query query = databaseReference.child(safeUserEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HelperClass userData = dataSnapshot.getValue(HelperClass.class);
                    callback.onUserDataReceived(userData);
                } else {
                    callback.onUserDataError("User not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onUserDataError(databaseError.getMessage());
            }
        });
    }

    // Helper method to convert email to a safe key for database
    private String emailToSafeKey(String email) {
        return email.replace(".", "_").replace("@", "_");
    }
}
