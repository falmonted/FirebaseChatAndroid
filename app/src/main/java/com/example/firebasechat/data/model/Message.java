package com.example.firebasechat.data.model;


import java.time.LocalDateTime;

public class Message {
    private String messageID;
    private String chatId;
    private String senderId;
    private String content;
    private LocalDateTime timestamp;
    private Media media = null;

    public Message(String messageID, String chatId, String senderId, String content, LocalDateTime timestamp, Media media) {
        this.messageID = messageID;
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
        this.media = media;
    }

    public Message(String messageID, String chatId, String senderId, String content, LocalDateTime timestamp) {
        this.messageID = messageID;
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
