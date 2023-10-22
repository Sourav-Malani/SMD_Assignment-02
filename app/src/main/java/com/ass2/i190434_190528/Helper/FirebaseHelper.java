//package com.ass2.i190434_190528.Helper;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import androidx.annotation.NonNull;
//
//public class FirebaseHelper {
//    public static void checkUser(String userEmail, String userPassword, ValueEventListener eventListener) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
//        String safeUserEmail = userEmail.replace(".", "_").replace("@", "_");
//        Query checkUserDatabase = reference.orderByChild("email").equalTo(userEmail);
//        checkUserDatabase.addListenerForSingleValueEvent(eventListener);
//    }
//}
