package com.example.mediasocial.Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Comment {
    private int commentId;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private int postId;

    public Comment(int commentId, String content, Date createdAt, Date updatedAt,
                   Date deletedAt, int postId) {
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.postId = postId;
    }

    public String getFormattedCreatedAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(createdAt);
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment='" + content + '\'' +
                ", post_id='" + postId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
