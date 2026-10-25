package com.example.firebasechat.repository.message;

public interface FireStorageRepositoryCallBack {
    void onSuccess(String mediaUrl, String mediaPath, Long mediaSizeBytes);
    void onFailure(Exception exception);
}
