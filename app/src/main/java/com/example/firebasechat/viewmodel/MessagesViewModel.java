package com.example.firebasechat.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.firebasechat.model.Message;
import com.example.firebasechat.repository.message.FireStoreMessageRepository;
import com.example.firebasechat.repository.message.MessageRepositoryCallback;
import com.example.firebasechat.repository.message.MessagesCallback;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

public class MessagesViewModel extends ViewModel {
    private static final String TAG = "MessageViewModel";
    private final FireStoreMessageRepository messageRepository = FireStoreMessageRepository.getInstance();
    private final MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isMessageOperationSuccessful = new MutableLiveData<>();
    private final MutableLiveData<Exception> error = new MutableLiveData<>();
    private ListenerRegistration messageListener;


    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<Boolean> isMessageOperationSuccessful() {
        return isMessageOperationSuccessful;
    }

    public LiveData<Exception> getError() {
        return error;
    }

    public void sendMessage(Message message) {
        messageRepository.sendMessage(message, new MessageRepositoryCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Message sent successfully");
                isMessageOperationSuccessful.setValue(true);
            }
            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "Error sending message", exception);
                isMessageOperationSuccessful.setValue(false);
                error.setValue(exception);
            }
        });
    }

    public void updateMessage(Message message) {
        messageRepository.updateMessage(message, new MessageRepositoryCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Message updated successfully");
                isMessageOperationSuccessful.setValue(true);
            }
            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "Error updating message", exception);
                isMessageOperationSuccessful.setValue(false);
                error.setValue(exception);
            }
        });
    }

    public void deleteMessage(String chatId, String messageId) {
        messageRepository.deleteMessage(chatId, messageId, new MessageRepositoryCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Message deleted successfully");
                isMessageOperationSuccessful.setValue(true);
            }
            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG,"Error deleting message", exception);
                isMessageOperationSuccessful.setValue(false);
                error.setValue(exception);
            }
        });
    }

    public void listenToMessages(String chatId) {
        stopListeningToMessages();
        messageListener = messageRepository.listenToMessages(chatId, new MessagesCallback() {
            @Override
            public void onSuccess(List<Message> messageList) {
                messages.setValue(messageList);
            }
            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG,"Error listening to messages", exception);
                error.setValue(exception);
            }
        });
    }
    public void stopListeningToMessages() {
        if (messageListener != null) {
            messageListener.remove();
            messageListener = null;
        }
    }
}