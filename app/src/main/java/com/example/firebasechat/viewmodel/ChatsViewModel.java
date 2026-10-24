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
import com.example.firebasechat.repository.chat.GetChatCallback;
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
    private final MutableLiveData<Chat> chatMutableLiveData = new MutableLiveData<>();
    private ListenerRegistration chatListener;

    public LiveData<List<Chat>> getChats() {
        return chats;
    }
    public LiveData<Chat> getChat() {
        return chatMutableLiveData;
    }

    public LiveData<Boolean> isChatAdded() {
        return isChatAdded;
    }

    public LiveData<Exception> getError() {
        return error;
    }

    public void listenToUserChats() {
        // Stop previous listener if there is one
        String logUserID = authRepository.getCurrentUser().getUid();
        stopListeningToChats();
        chatListener = chatRepository.listenToUserChats(logUserID, new ChatListCallback() {
            @Override
            public void onSuccess(List<Chat> chatList) {
                // Assign the chat title base on the other participant name
                for (Chat chat : chatList) {
                    String otherParticipantId = getOtherParticipantId(chat.getParticipantIds());
                    userRepository.getUserById(otherParticipantId, new GetUserRepositoryCallback() {

                        @Override
                        public void onSuccess(User user) {
                            if (user.getUsername() != null) {
                                chat.setTitle(user.getUsername());
                            } else {
                                chat.setTitle("Unknown User");
                            }
                            chats.setValue(chatList);
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            chat.setTitle("Unknown User");
                            chats.setValue(chatList);
                        }
                    });
                }
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



    public String getOtherParticipantId (List<String> chatParticipantsId){
        String currentUserId = authRepository.getCurrentUser().getUid();

        if (currentUserId.equals(chatParticipantsId.get(0))) {
            return chatParticipantsId.get(1);
        } else {
            return chatParticipantsId.get(0);
        }
    };

    public void getChatById(String chatId) {
        chatRepository.getChatById(chatId, new GetChatCallback() {
            @Override
            public void onSuccess(Chat chat) {
                if (chat != null) {
                    Log.i(TAG, "chat found");
                    chatMutableLiveData.setValue(chat);
                }
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
