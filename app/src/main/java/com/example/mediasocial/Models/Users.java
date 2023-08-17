package com.example.mediasocial.Models;

import java.util.Date;

public class Users {
    private int user_id;
    private String email;
    private String phone;
    private String name;
    private String password;
    private Date created_at;
    private Date updated_at;
    private int role_id;

    public Users(int user_id, String email, String phone, String name, String password, Date created_at, Date updated_at, int role_id) {
        this.user_id = user_id;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.role_id = role_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    @Override
    public String toString() {
        return "Users{" +
                "user_id=" + user_id +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", role_id=" + role_id +
                '}';
    }
}
