package com.example.firebasechat.repository.user;

import android.util.Log;

import com.example.firebasechat.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

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

    public void getUserByEmail(String userEmail, GetUserRepositoryCallback callback) {
        db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        User user = document.toObject(User.class);
                        Log.d(TAG, "User profile updated successfully");
                        callback.onSuccess(user);
                    } else {
                        Log.d(TAG, "User not found");
                        callback.onFailure(new NoSuchElementException("User not found"));
                    }
                }).addOnFailureListener(exception -> {
                    Log.d(TAG, "User doesn't exist");
                    callback.onFailure(exception);
                });
    }

    public ListenerRegistration listenToUserById(String userId, GetUserRepositoryCallback callback) {
        if (userId == null || userId.isEmpty()) {
            callback.onFailure(new IllegalArgumentException("chat ID cannot be empty"));
            return null;
        }

        return db.collection("users").document(userId)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error listening to user", error);
                        callback.onFailure(error);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        User user = snapshot.toObject(User.class);
                        if (user != null) {
                            callback.onSuccess(user);
                        }
                    } else {
                        Log.w(TAG, "User doesn't exist");
                        callback.onFailure(new Exception("User not found"));
                    }

                });
    }

}
