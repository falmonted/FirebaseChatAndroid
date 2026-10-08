package com.example.firebasechat.model;


import com.google.firebase.Timestamp;


public class Message {
    private String messageId;
    private String chatId;
    private String senderId;
    private String content;
    private Timestamp timestamp;
    private String mediaUrl;
    private String mediaPath;
    private MediaType mediaType;
    private Long mediaSizeBytes;

    public enum MediaType {
        IMAGE,
        VIDEO,
        AUDIO,
        DOCUMENT
    }

    public Message() {
    }

    public Message(String messageId, String chatId, String senderId, String content, Timestamp timestamp) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Long getMediaSizeBytes() {
        return mediaSizeBytes;
    }

    public void setMediaSizeBytes(Long mediaSizeBytes) {
        this.mediaSizeBytes = mediaSizeBytes;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
