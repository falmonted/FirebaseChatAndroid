package com.example.firebasechat.repository.user;

import com.example.firebasechat.model.User;

public interface GetUserRepositoryCallback {
    void onSuccess (User user);
    void onFailure(Exception exception);
}
