package com.example.firebasechat.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.firebasechat.model.AuthError;
import com.example.firebasechat.repository.AuthCallback;
import com.example.firebasechat.repository.FireAuthRepository;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {
    private final FireAuthRepository authRepository = FireAuthRepository.getInstance();
    private static final String TAG = "LoginViewModel";
    private MutableLiveData<Boolean> isLoginSuccessful = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSignupSuccessful = new MutableLiveData<>();
    private MutableLiveData<AuthError> authError = new MutableLiveData<>();

    public LiveData<Boolean> isLoginSuccessful() {
        return isLoginSuccessful;
    }

    public LiveData<Boolean> isSignupSuccessful() {
        return isSignupSuccessful;
    }

    public LiveData<AuthError> authError() {
        return authError;
    }

    public boolean isUserLog() {
        return authRepository.getCurrentUser() != null;
    }

    public void createUserWithEmailAndPassword(String email, String password) {
        authRepository.createUserWithEmailAndPassword(email, password, new AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) { // Use user
                isSignupSuccessful.setValue(true); //ForRegister
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, exception.toString());
                isSignupSuccessful.setValue(false); //ForRegister
                if (exception instanceof FirebaseAuthException) {
                    String errorCode = ((FirebaseAuthException) exception).getErrorCode();
                    authError.setValue(getAuthError(errorCode));
                }
            }
        });
    }

    public void signInWithEmailAndPassword(String email, String password) {
        authRepository.signInWithEmailAndPassword(email, password, new AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) { // Use user
                isLoginSuccessful.setValue(true); //ForRegister
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, exception.toString());
                isLoginSuccessful.setValue(false); //ForRegister
                if (exception instanceof FirebaseAuthException) {
                    String errorCode = ((FirebaseAuthException) exception).getErrorCode();
                    authError.setValue(getAuthError(errorCode));
                }
            }
        });
    }

    public void userSignOut() {
        authRepository.userSignOut();
    }

    public AuthError getAuthError(String errorCode) {
        if (errorCode == null) {
            return AuthError.UNKNOWN_ERROR;
        }
        switch (errorCode) {
            case "ERROR_INVALID_CREDENTIAL":
                return AuthError.INVALID_CREDENTIAL;

            case "ERROR_WRONG_PASSWORD":
                return AuthError.WRONG_PASSWORD;

            case "ERROR_USER_NOT_FOUND":
                return AuthError.USER_NOT_FOUND;

            case "ERROR_INVALID_EMAIL":
                return AuthError.INVALID_EMAIL;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                return AuthError.EMAIL_ALREADY_IN_USE;

            case "ERROR_WEAK_PASSWORD":
                return AuthError.WEAK_PASSWORD;

            case "ERROR_USER_DISABLED":
                return AuthError.USER_DISABLED;

            case "ERROR_TOO_MANY_REQUESTS":
                return AuthError.TOO_MANY_REQUESTS;

            default:
                return AuthError.UNKNOWN_ERROR;
        }
    }

}