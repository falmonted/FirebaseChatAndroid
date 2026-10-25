package com.example.firebasechat.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.firebasechat.repository.user.FireStoreUserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseNotificationService extends FirebaseMessagingService {
    private static final String TAG = "NotificationService";
    private final FireStoreUserRepository fireStoreUserRepository = FireStoreUserRepository.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            fireStoreUserRepository.setFcmToken(userId, token);
        }
    }
}
