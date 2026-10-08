package com.example.firebasechat.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.firebasechat.model.AuthError;
import com.example.firebasechat.model.User;
import com.example.firebasechat.repository.auth.FireAuthRepository;
import com.example.firebasechat.repository.user.FireStoreUserRepository;
import com.example.firebasechat.repository.user.UserRepositoryCallback;
import com.google.firebase.auth.FirebaseAuth;

public class UserViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final FireStoreUserRepository fireStoreUserRepository = FireStoreUserRepository.getInstance();
    private final FireAuthRepository authRepository = FireAuthRepository.getInstance();
    private static final String TAG = "UserViewModel";
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUserModified = new MutableLiveData<>();

    public LiveData<User> addUser () {
        return user;
    }

    public LiveData<Boolean> isUserModified () {
        return isUserModified;
    }

}