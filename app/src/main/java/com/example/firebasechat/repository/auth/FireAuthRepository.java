package com.example.firebasechat.repository.auth;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireAuthRepository {
    private static FireAuthRepository instance = null;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String TAG = "FireAuthRepository";

    public static synchronized FireAuthRepository getInstance() {
        if (instance == null) {
            instance = new FireAuthRepository();
        }
        return instance;
    }

    // Check if user is signed in (non-null)
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void createUserWithEmailAndPassword(String email, String password, AuthCallback authCallback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        authCallback.onSuccess(getCurrentUser());
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        authCallback.onFailure(task.getException());
                    }
                });
    }

    public void signInWithEmailAndPassword(String email, String password, AuthCallback authCallback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        authCallback.onSuccess(getCurrentUser());
                    } else {
                        Log.w(TAG, "signInWithEmail:failure");
                        authCallback.onFailure(task.getException());
                    }
                });
    }



    public void userSignOut () {
        mAuth.signOut();
    }

}
