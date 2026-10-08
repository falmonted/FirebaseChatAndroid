package com.example.firebasechat.repository.chat;

import android.util.Log;

import com.example.firebasechat.model.Chat;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class FireStoreChatRepository {
    private static FireStoreChatRepository instance = null;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "FireStoreChatRepository";
    private FireStoreChatRepository() {
    }
    public static synchronized FireStoreChatRepository getInstance() {
        if (instance == null) {
            instance = new FireStoreChatRepository();
        }
        return instance;
    }


    public ListenerRegistration listenToUserChats(String userId, ChatListCallback callback) {
        if (userId == null || userId.isEmpty()) {
            callback.onFailure(new IllegalArgumentException("User ID cannot be empty"));
            return null;
        }

        return db.collection("chats")
                .whereArrayContains("participantIds", userId)
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error listening to user chats", error);
                        callback.onFailure(error);
                        return;
                    }
                    if (snapshot == null) {
                        return;
                    }

                    List<Chat> chats = new ArrayList<>();
                    for (var document : snapshot.getDocuments()) {
                        Chat chat = document.toObject(Chat.class);
                        if (chat != null) {
                            chats.add(chat);
                        }
                    }
                    callback.onSuccess(chats);
                });
    }
}
