package com.example.firebasechat.repository.chat;

import com.example.firebasechat.model.Chat;

import java.util.List;

public interface ChatRepositoryCallBack {
    void onSuccess();
    void onFailure(Exception exception);
}
