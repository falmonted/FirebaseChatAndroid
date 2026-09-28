package com.example.firebasechat.data.model;

import java.time.LocalDateTime;

public class Chat {
    private String chatId;
    private LocalDateTime createdAt;
    private ChatType chatType;

    public enum ChatType {
        INDIVIDUAL,
        GROUP
    }

    public Chat(String chatId, LocalDateTime createdAt, ChatType chatType) {
        this.chatId = chatId;
        this.createdAt = createdAt;
        this.chatType = chatType;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }
}
