package com.example.firebasechat.repository.user;

import android.util.Log;

import com.example.firebasechat.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

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

    public void addOrEditNewUser(User user, UserRepositoryCallback callback) {
        db.collection("users").document(user.getUserId())
                .set(user, SetOptions.merge())
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with forUser");
                    callback.onSuccess();
                })
                .addOnFailureListener(exception -> {
                    Log.w(TAG, "Error adding document", exception);
                    callback.onFailure(exception);
                });
    }

}
