package com.example.firebasechat.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.firebasechat.model.AuthError;
import com.example.firebasechat.model.Chat;
import com.example.firebasechat.model.User;
import com.example.firebasechat.repository.auth.FireAuthRepository;
import com.example.firebasechat.repository.chat.ChatListCallback;
import com.example.firebasechat.repository.user.FireStoreUserRepository;
import com.example.firebasechat.repository.user.GetUserRepositoryCallback;
import com.example.firebasechat.repository.user.UserRepositoryCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

public class UserViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final FireStoreUserRepository fireStoreUserRepository = FireStoreUserRepository.getInstance();
    private final FireAuthRepository authRepository = FireAuthRepository.getInstance();
    private static final String TAG = "UserViewModel";

    private MutableLiveData<Exception> exceptionMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userByIdMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> userUpdateSuccesful = new MutableLiveData<>();
    private MutableLiveData<User> getCurrentLogUserMutableLiveData = new MutableLiveData<>();
    private ListenerRegistration userListener;

    public LiveData<Exception> userModifiedException(){
        return exceptionMutableLiveData;
    }

    public LiveData<Boolean> isUserUpdateSuccessful(){
        return userUpdateSuccesful;
    }

    public LiveData<User> getUser() {
        return userByIdMutableLiveData;
    }

    public LiveData<User> getCurrentLogUser() {
        return getCurrentLogUserMutableLiveData;
    }

    public void updateUserProfile (User user) {
        String currentUserId = authRepository.getCurrentUser().getUid();
        user.setUserId(currentUserId);
        fireStoreUserRepository.updateUserProfile(user, new UserRepositoryCallback() {
            @Override
            public void onSuccess() {
                userUpdateSuccesful.setValue(true);
            }

            @Override
            public void onFailure(Exception exception) {
                userUpdateSuccesful.setValue(false);
                exceptionMutableLiveData.setValue(exception);
            }
        });
    }

    public void getUserById (String userId) {
        fireStoreUserRepository.getUserById(userId, new GetUserRepositoryCallback() {
            @Override
            public void onSuccess(User user) {
                userByIdMutableLiveData.setValue(user);
            }

            @Override
            public void onFailure(Exception exception) {
                exceptionMutableLiveData.setValue(exception);
            }
        });
    }

    public void getCurrentUserFirestore () {
        String currentUserId = authRepository.getCurrentUser().getUid();
        fireStoreUserRepository.getUserById(currentUserId, new GetUserRepositoryCallback() {
            @Override
            public void onSuccess(User user) {
                getCurrentLogUserMutableLiveData.setValue(user);
            }

            @Override
            public void onFailure(Exception exception) {
                exceptionMutableLiveData.setValue(exception);
            }
        });
    }

    public String getCurrentLogUserId() {
        return authRepository.getCurrentUser().getUid();
    }

    public void listenToUser(String userId) {
        // Stop previous listener if there is one
        stopListeningUser();
        userListener = fireStoreUserRepository.listenToUserById(userId, new GetUserRepositoryCallback() {
            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    userByIdMutableLiveData.setValue(user);
                }
            }
            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "Error listening to user", exception);
                exceptionMutableLiveData.setValue(exception);
            }
        });
    }

    public void stopListeningUser() {
        if (userListener != null) {
            userListener.remove();
            userListener = null;
        }
    }
}