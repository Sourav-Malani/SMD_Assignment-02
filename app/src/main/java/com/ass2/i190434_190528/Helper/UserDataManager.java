//package com.ass2.i190434_190528.Helper;
//
//import com.ass2.i190434_190528.Helper.UserDatabaseHelper;
//
//public class UserDataManager {
//    private UserDatabaseHelper userDatabaseHelper;
//
//    public UserDataManager(String safeUserEmail){
//        userDatabaseHelper = new UserDatabaseHelper(safeUserEmail);
//    }
//
//    public void getUserData(String safeUserEmail, final DataCallback dataCallback) {
//        // Pass the safeUserEmail to the UserDatabaseHelper directly
//        userDatabaseHelper = new UserDatabaseHelper(safeUserEmail);
//
//        userDatabaseHelper.getUserData(new UserDatabaseHelper.DataCallback() {
//            @Override
//            public void onDataReceived(UserDatabaseHelper user) {
//                dataCallback.onDataReceived(user);
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                dataCallback.onError(errorMessage);
//            }
//        });
//    }
//
//    public interface DataCallback {
//        void onDataReceived(UserDatabaseHelper user);
//        void onError(String errorMessage);
//    }
//}
