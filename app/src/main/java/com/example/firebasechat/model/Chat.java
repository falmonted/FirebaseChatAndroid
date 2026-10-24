package com.example.firebasechat.model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;


public class Chat {
    private String chatId;
    private Timestamp createdAt;
    private ChatType chatType;
    private String title;
    private List<String> participantIds = new ArrayList<>();
    private String lastMessage;
    private Timestamp lastMessageTimestamp;

    public Chat() {
    }

    public enum ChatType {
        INDIVIDUAL,
        GROUP
    }

    public Chat(String chatId, Timestamp createdAt, ChatType chatType, List<String> participantIds) {
        this.chatId = chatId;
        this.createdAt = createdAt;
        this.chatType = chatType;
        this.participantIds = participantIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void addParticipant(String idParticipant) {
        this.participantIds.add(idParticipant);
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }
}
