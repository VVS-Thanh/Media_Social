package com.example.mediasocial.Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class User {
    private int userId;
    private String email;
    private String phone;
    private String name;
    private String password;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private int roleId;
    private List<Post> userPosts;

    public User(int userId, String email, String phone, String name, String password,
                Date createdAt, Date updatedAt, Date deletedAt, int roleId) {
        this.userId = userId;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.roleId = roleId;

        this.userPosts = new ArrayList<>();
    }


    public List<Post> getUserPosts() {
        return userPosts;
    }

    public void addUserPost(Post post) {
        userPosts.add(post);
    }

    public void addPost(int postId, String content, String topic, String thumbnailImage,
                        Date createdAt, Date updatedAt, Date deletedAt) {
        Post post = new Post(postId, content, topic, thumbnailImage,
                createdAt, updatedAt, deletedAt, getUserId());
        userPosts.add(post);
    }

    public void editPost(int postId, String content, String topic, String thumbnailImage,
                         Date updatedAt) {
        for (Post post : userPosts) {
            if (post.getPostId() == postId) {
                post.setContent(content);
                post.setTopic(topic);
                post.setThumbnailImage(thumbnailImage);
                post.setUpdatedAt(updatedAt);
                break;
            }
        }
    }

    public void deletePost(int postId) {
        Iterator<Post> iterator = userPosts.iterator();
        while (iterator.hasNext()) {
            Post post = iterator.next();
            if (post.getPostId() == postId) {
                iterator.remove();
                break;
            }
        }
    }

    public String getFormattedCreatedAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(createdAt);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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


    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + userId + '\'' +
                ", phone_number='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", username='" + name + '\'' +
                '}';
    }
}
