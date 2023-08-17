package com.example.mediasocial.Models;

import java.util.Date;

public class Posts {
    private int post_id;
    private String content;
    private String topic;
    private String thumbnail_image;
    private Date created_at;
    private Date updated_at;
    private int user_id;

    public Posts(int post_id, String content, String topic, String thumbnail_image, Date created_at, Date updated_at, int user_id) {
        this.post_id = post_id;
        this.content = content;
        this.topic = topic;
        this.thumbnail_image = thumbnail_image;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user_id = user_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
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
        return "Posts{" +
                "post_id=" + post_id +
                ", content='" + content + '\'' +
                ", topic='" + topic + '\'' +
                ", thumbnail_image='" + thumbnail_image + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", user_id=" + user_id +
                '}';
    }
}
