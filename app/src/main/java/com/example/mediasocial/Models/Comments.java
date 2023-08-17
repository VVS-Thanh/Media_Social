package com.example.mediasocial.Models;

import androidx.annotation.NonNull;

import java.util.Date;

public class Comments {
    private int comment_id;
    private String content;
    private Date created_at;
    private Date updated_at;
    private int user_id;
    private int post_id;

    public Comments(int comment_id, String content, Date created_at, Date updated_at, int user_id, int post_id) {
        this.comment_id = comment_id;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user_id = user_id;
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "comment_id=" + comment_id +
                ", content='" + content + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", user_id=" + user_id +
                ", post_id=" + post_id +
                '}';
    }
}
