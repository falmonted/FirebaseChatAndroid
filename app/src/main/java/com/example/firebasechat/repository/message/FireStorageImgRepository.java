package com.example.firebasechat.repository.message;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireStorageImgRepository {
    private static final String TAG = "FireStorageImgRepository";

    private static FireStorageImgRepository instance = null;

    private final FirebaseStorage storage;

    private FireStorageImgRepository() {
        storage = FirebaseStorage.getInstance();
    }

    public static synchronized FireStorageImgRepository getInstance() {
        if (instance == null) {
            instance = new FireStorageImgRepository();
        }
        return instance;
    }

    public void uploadImgMessage(String chatId, String messageId, Uri imageUri, FireStorageRepositoryCallBack callback) {
        if (imageUri == null) {
            callback.onFailure(new IllegalArgumentException("Image Uri cannot be null"));
            return;
        }

        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("chats/" + chatId + "/images/" + messageId + ".jpg");

        imgRef.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful() && task.getException() != null) {
                        throw task.getException();
                    }
                    // Fetch the actual HTTP download URL
                    return imgRef.getDownloadUrl();
                })
                .addOnSuccessListener(downloadUri -> {
                    String mediaUrl = downloadUri.toString();
                    String mediaPath = imgRef.getPath();
                    Long mediaSizeBytes = 0L;
                    Log.d(TAG, "Success uploading image: " + mediaUrl);
                    callback.onSuccess(mediaUrl, mediaPath, mediaSizeBytes);
                })
                .addOnFailureListener(exception -> {
                    Log.e(TAG, "Error uploading image", exception);
                    callback.onFailure(exception);
                });
    }
}
