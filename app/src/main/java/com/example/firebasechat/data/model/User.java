package com.example.firebasechat.data.model;

import java.time.LocalDateTime;

public class User {
    private String userId;
    private String email;
    private String password;

    private boolean isOnline;
    private LocalDateTime last_seen;

    private Media profilePic = null;

    public User(String userId, String email, String password, boolean isOnline, LocalDateTime last_seen, Media profilePic) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.isOnline = isOnline;
        this.last_seen = last_seen;
        this.profilePic = profilePic;
    }

    public User(String userId, String email, String password, boolean isOnline, LocalDateTime last_seen) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.isOnline = isOnline;
        this.last_seen = last_seen;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public LocalDateTime getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(LocalDateTime last_seen) {
        this.last_seen = last_seen;
    }

    public Media getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Media profilePic) {
        this.profilePic = profilePic;
    }
}
