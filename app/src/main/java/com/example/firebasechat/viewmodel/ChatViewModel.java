package com.example.firebasechat.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasechat.model.Chat;
import com.example.firebasechat.repository.chat.ChatListCallback;
import com.example.firebasechat.repository.chat.FireStoreChatRepository;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

public class ChatViewModel {
    private static final String TAG = "ChatViewModel";
    private final FireStoreChatRepository chatRepository = FireStoreChatRepository.getInstance();
    private final MutableLiveData<List<Chat>> chats = new MutableLiveData<>();
    private final MutableLiveData<Exception> error = new MutableLiveData<>();
    private ListenerRegistration chatListener;

    public LiveData<List<Chat>> getChats() {
        return chats;
    }
    public LiveData<Exception> getError() {
        return error;
    }

    public void listenToUserChats(String userId) {
        // Stop previous listener if there is one
        stopListeningToChats();
        chatListener = chatRepository.listenToUserChats(userId, new ChatListCallback() {
            @Override
            public void onSuccess(List<Chat> chatList) {
                chats.setValue(chatList);
            }
            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG,"Error listening to user chats", exception);
                error.setValue(exception);
            }
        });
    }

    public void stopListeningToChats() {
        if (chatListener != null) {
            chatListener.remove();
            chatListener = null;
        }
    }
}
