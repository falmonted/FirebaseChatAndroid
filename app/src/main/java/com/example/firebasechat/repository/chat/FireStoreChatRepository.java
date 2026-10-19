package com.example.firebasechat.repository.chat;

import android.util.Log;

import com.example.firebasechat.model.Chat;
import com.example.firebasechat.model.User;
import com.example.firebasechat.repository.user.UserRepositoryCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void addChat(Chat chat, ChatRepositoryCallBack callback) {
        CollectionReference collectionReference = db.collection("chats");
        String chatId = collectionReference.document().getId();

        Map<String, Object> chatMap = new HashMap<>();
        chatMap.put("chatId", chatId);
        chatMap.put("chatType", chat.getChatType().name());
        chatMap.put("participantIds", chat.getParticipantIds());
        chatMap.put("lastMessage", chat.getLastMessage());
        chatMap.put("createdAt", FieldValue.serverTimestamp());
        chatMap.put("lastMessageTimestamp", FieldValue.serverTimestamp());


        collectionReference.document(chatId)
                .set(chatMap)
                .addOnSuccessListener(v -> {
                    Log.d(TAG, "Chat added successfully");
                    callback.onSuccess();
                }).addOnFailureListener(exception -> {
                    Log.e(TAG,"Error adding Chat", exception);
                    callback.onFailure(exception);
                });
    }
}
