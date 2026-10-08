package com.example.firebasechat.repository.message;

import com.example.firebasechat.model.Message;

import java.util.List;

public interface MessagesCallback {
    void onSuccess(List<Message> messages);
    void onFailure(Exception exception);
}
