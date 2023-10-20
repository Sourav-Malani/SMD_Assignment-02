package com.ass2.i190434_190528;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileManager {

    private DatabaseReference userReference;

    public UserProfileManager(String userEmail) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userReference = database.getReference("users").child(userEmail.replace(".", "_"));
    }

    public void getUserName(final NameCallback callback) {
        userReference.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                if (callback != null) {
                    callback.onNameReceived(name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (callback != null) {
                    callback.onError(databaseError.toException());
                }
            }
        });
    }

    // NameCallback interface for asynchronous database operations
    public interface NameCallback {
        void onNameReceived(String name);

        void onError(Exception e);
    }
}
