package com.example.firebasechat.repository.chat;

import com.example.firebasechat.model.Chat;

public interface GetChatCallback {
    void onSuccess(Chat chat);
    void onFailure(Exception exception);
}
