package com.example.firebasechat.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.firebasechat.model.Chat;
import com.example.firebasechat.model.User;
import com.example.firebasechat.repository.auth.FireAuthRepository;
import com.example.firebasechat.repository.chat.ChatListCallback;
import com.example.firebasechat.repository.chat.ChatRepositoryCallBack;
import com.example.firebasechat.repository.chat.FireStoreChatRepository;
import com.example.firebasechat.repository.user.FireStoreUserRepository;
import com.example.firebasechat.repository.user.GetUserRepositoryCallback;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

public class ChatsViewModel extends ViewModel {
    private static final String TAG = "ChatViewModel";
    private final FireStoreChatRepository chatRepository = FireStoreChatRepository.getInstance();
    private final FireAuthRepository authRepository = FireAuthRepository.getInstance();
    private final FireStoreUserRepository userRepository = FireStoreUserRepository.getInstance();
    private final MutableLiveData<List<Chat>> chats = new MutableLiveData<>();
    private final MutableLiveData<Exception> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isChatAdded = new MutableLiveData<>();
    private ListenerRegistration chatListener;

    public LiveData<List<Chat>> getChats() {
        return chats;
    }
    public LiveData<Boolean> isChatAdded() {
        return isChatAdded;
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
                Log.e(TAG, "Error listening to user chats", exception);
                error.setValue(exception);
            }
        });
    }

    public void addChat(String emailContact) {
        // Search for user (emailContact) to find id
        userRepository.getUserByEmail(emailContact, new GetUserRepositoryCallback() {
            @Override
            public void onSuccess(User user) {
                Chat chat = new Chat();
                FirebaseUser currentFireUser = authRepository.getCurrentUser();
                if (currentFireUser == null) {
                    error.setValue(
                            new IllegalStateException("No hay un usuario autenticado")
                    );
                    return;
                }
                String currentUserId = currentFireUser.getUid();
                String contactId = user.getUserId();

                //SetChat values
                chat.addParticipant(currentUserId);
                chat.addParticipant(contactId);
                chat.setChatType(Chat.ChatType.INDIVIDUAL); //TODO: edit when add groupChats
                chat.setLastMessage("");

                chatRepository.addChat(chat, new ChatRepositoryCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "Chat added");
                        isChatAdded.setValue(true);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Log.e(TAG, "Error Adding Chat", exception);
                        error.setValue(exception);
                    }
                });

            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "Cannot Find User", exception);
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

    public void resetAddChatState() {
        isChatAdded.setValue(false);
        error.setValue(null);
    }
}
