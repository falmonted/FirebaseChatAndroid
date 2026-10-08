package com.example.firebasechat.model;

import com.google.firebase.Timestamp;

public class User {
    private String userId;
    private String username;
    private String email;
    private boolean isOnline;
    private Timestamp  last_seen;

    private String profilePicUrl;
    private String profilePicPath;

    public User() {
    }

    public User(String userId, String username, String email,
                boolean isOnline, Timestamp last_seen, String profilePicUrl, String profilePicPath) {

        this.userId = userId;
        this.username = username;
        this.email = email;
        this.isOnline = isOnline;
        this.last_seen = last_seen;
        this.profilePicUrl = profilePicUrl;
        this.profilePicPath = profilePicPath;
    }


    public User(String userId, String email, boolean isOnline, Timestamp  last_seen) {
        this.userId = userId;
        this.email = email;
        this.isOnline = isOnline;
        this.last_seen = last_seen;
    }

    public User(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Timestamp  getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(Timestamp  last_seen) {
        this.last_seen = last_seen;
    }

}
