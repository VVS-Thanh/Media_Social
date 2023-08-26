package com.example.mediasocial.Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Post {
    private int postId;
    private String content;

    public Post(int postId, String content, String thumbnailImage, Date createdAt, Date updatedAt, Date deletedAt, int userId) {
        this.postId = postId;
        this.content = content;
        this.thumbnailImage = thumbnailImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.userId = userId;
    }

    private String thumbnailImage;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private int userId;


    public String getFormattedCreatedAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(createdAt);
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPostInfo() {
        String info = "Post ID: " + postId + "\n"
                + "Content: " + content + "\n"
                + "Created At: " + getFormattedCreatedAt() + "\n"
                + "Thumbnail Image: " + thumbnailImage + "\n"
                + "User ID: " + userId + "\n";
        return info;
    }
}
