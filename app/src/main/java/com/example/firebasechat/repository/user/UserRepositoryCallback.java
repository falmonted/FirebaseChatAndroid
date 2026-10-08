package com.example.firebasechat.repository.user;

public interface UserRepositoryCallback {
    void onSuccess();
    void onFailure(Exception exception);
}
