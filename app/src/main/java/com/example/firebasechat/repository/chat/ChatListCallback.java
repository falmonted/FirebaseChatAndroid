package com.example.firebasechat.repository.chat;

import com.example.firebasechat.model.Chat;

import java.util.List;

public interface ChatListCallback {
    void onSuccess(List<Chat> chats);
    void onFailure(Exception exception);
}
