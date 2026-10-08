package com.example.firebasechat.repository.message;

public interface MessageRepositoryCallback {
    void onSuccess();
    void onFailure(Exception exception);
}
