package com.example.firebasechat.repository.message;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.firebasechat.model.Message;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

public class FireStoreMessageRepository {

    private static final String TAG = "FireStoreMessageRepository";

    private static FireStoreMessageRepository instance = null;

    private final FirebaseFirestore db;

    private FireStoreMessageRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized FireStoreMessageRepository getInstance() {
        if (instance == null) {
            instance = new FireStoreMessageRepository();
        }
        return instance;
    }

    // create message
    public void sendMessage(Message message, MessageRepositoryCallback callback) {
        if (message == null) {
            callback.onFailure(new IllegalArgumentException("Message cannot be null"));
            return;
        }

        if (message.getMessageId() == null) {
            callback.onFailure(new IllegalArgumentException("Message ID cannot be null"));
            return;
        }

        if (message.getChatId() == null) {
            callback.onFailure(new IllegalArgumentException("Chat ID cannot be null"));
            return;
        }

        db.collection("chats").document(message.getChatId())
                .collection("messages")
                .document(message.getMessageId())
                .set(message)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Message sent successfully");
                    callback.onSuccess();
                })
                .addOnFailureListener(exception -> {
                    Log.e(TAG,"Error sending message", exception);
                    callback.onFailure(exception);
                });
    }

    public void getMessages(String chatId, MessagesCallback callback) {
        if (chatId == null || chatId.isEmpty()) {
            callback.onFailure(new IllegalArgumentException("Chat ID cannot be empty"));
            return;
        }

        db.collection("chats").document(chatId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Message> messages = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Message message = document.toObject(Message.class);

                        if (message != null) {
                            messages.add(message);
                        }
                    }

                    callback.onSuccess(messages);
                })
                .addOnFailureListener(exception -> {
                    Log.e(TAG, "Error getting messages", exception);
                    callback.onFailure(exception);
                });
    }

    //Message realtime
    public ListenerRegistration listenToMessages(String chatId, MessagesCallback callback) {

        if (chatId == null || chatId.isEmpty()) {
            callback.onFailure(new IllegalArgumentException("Chat ID cannot be empty"));
            return null;
        }

        return db.collection("chats").document(chatId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error listening to messages", error);
                        callback.onFailure(error);
                        return;
                    }

                    if (snapshot == null) {
                        return;
                    }

                    List<Message> messages = new ArrayList<>();

                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        Message message = document.toObject(Message.class);
                        if (message != null) {
                            messages.add(message);
                        }
                    }
                    callback.onSuccess(messages);
                });
    }

    public void updateMessage(Message message, MessageRepositoryCallback callback) {
        if (message == null || message.getMessageId() == null || message.getChatId() == null) {
            callback.onFailure(new IllegalArgumentException("Message, message ID or chat ID is null"));
            return;
        }

        db.collection("chats").document(message.getChatId())
                .collection("messages")
                .document(message.getMessageId())
                .set(message, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Message updated successfully");
                    callback.onSuccess();
                })
                .addOnFailureListener(exception -> {
                    Log.e(TAG, "Error updating message", exception);
                    callback.onFailure(exception);
                });
    }

    public void deleteMessage(String chatId, String messageId, MessageRepositoryCallback callback) {
        if (chatId == null || chatId.isEmpty()) {
            callback.onFailure(new IllegalArgumentException("Chat ID cannot be empty"));
            return;
        }

        if (messageId == null || messageId.isEmpty()) {
            callback.onFailure(new IllegalArgumentException("Message ID cannot be empty"));
            return;
        }

        db.collection("chats").document(chatId).collection("messages")
                .document(messageId)
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Message deleted successfully");
                    callback.onSuccess();
                })
                .addOnFailureListener(exception -> {
                    Log.e(TAG, "Error deleting message", exception);
                    callback.onFailure(exception);
                });
    }
}
