package com.example.firebasechat.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.firebasechat.model.AuthError;
import com.example.firebasechat.model.User;
import com.example.firebasechat.repository.auth.AuthCallback;
import com.example.firebasechat.repository.auth.FireAuthRepository;
import com.example.firebasechat.repository.user.FireStoreUserRepository;
import com.example.firebasechat.repository.user.UserRepositoryCallback;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {
    private final FireAuthRepository authRepository = FireAuthRepository.getInstance();
    private final FireStoreUserRepository fireStoreUserRepository = FireStoreUserRepository.getInstance();
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
            public void onSuccess(FirebaseUser firebaseUser) { // Use user
                if (firebaseUser == null) {
                    isSignupSuccessful.setValue(false);
                }
                //Creates user on Firestore
                createFirestoreUser(firebaseUser);
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
                Log.i(TAG, "User SignIn Successful");
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

    // Only has LOG onSucess and onFailure
    private void createFirestoreUser(FirebaseUser firebaseUser) {

        User user = new User(firebaseUser.getUid(), firebaseUser.getEmail());
        fireStoreUserRepository.addOrEditNewUser(user, new UserRepositoryCallback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "User Firestore account created Successful");
                isSignupSuccessful.setValue(false);
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "User Firestore account created FAIL");
                Log.e(TAG, exception.toString());
                isSignupSuccessful.setValue(false);
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