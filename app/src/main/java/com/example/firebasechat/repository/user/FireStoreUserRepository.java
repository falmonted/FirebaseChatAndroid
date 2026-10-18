package com.example.firebasechat.repository.user;

import android.util.Log;

import com.example.firebasechat.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FireStoreUserRepository {
    private static FireStoreUserRepository instance = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "FireStoreUserRepository";

    public static synchronized FireStoreUserRepository getInstance() {
        if (instance == null) {
            instance = new FireStoreUserRepository();
        }
        return instance;
    }

    public void registerUser(User user, UserRepositoryCallback callback) {
        db.collection("users").document(user.getUserId())
                .set(user, SetOptions.merge())
                .addOnSuccessListener(v -> {
                    Log.d(TAG, "User successfully register");
                    callback.onSuccess();
                })
                .addOnFailureListener(exception -> {
                    Log.w(TAG, "Error User register", exception);
                    callback.onFailure(exception);
                });
    }

    public void updateUserProfile(User user, UserRepositoryCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("username", user.getUsername());

        db.collection("users").document(user.getUserId())
                .update(updates)
                .addOnSuccessListener(v -> {
                    Log.d(TAG, "User profile updated successfully");
                    callback.onSuccess();
                })
                .addOnFailureListener(exception -> {
                    Log.w(TAG, "Error User profile updated", exception);
                    callback.onFailure(exception);
                });
    }

    public void getUserById(String userId, GetUserRepositoryCallback callback) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    Log.d(TAG, "User profile updated successfully");
                    callback.onSuccess(user);
                }).addOnFailureListener(exception -> {
                    Log.d(TAG, "User doesn't exist");
                    callback.onFailure(exception);
                });
    }

}
