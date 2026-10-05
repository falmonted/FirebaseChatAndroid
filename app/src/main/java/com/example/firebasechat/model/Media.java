package com.example.firebasechat.model;

public class Media {
    private String mediaId;
    private String fileUrl;
    private Integer fileSize;
    private MediaType mediaType;

    public enum MediaType {
        IMAGE,
        VIDEO,
        AUDIO,
        DOCUMENT
    }

    public Media(String mediaId, String fileUrl, MediaType mediaType, Integer fileSize) {
        this.mediaId = mediaId;
        this.fileUrl = fileUrl;
        this.mediaType = mediaType;
        this.fileSize = fileSize;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
