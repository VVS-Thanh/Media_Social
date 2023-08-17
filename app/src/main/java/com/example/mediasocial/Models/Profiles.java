package com.example.mediasocial.Models;

import java.util.Date;

public class Profiles {
    private int profile_id;
    private String last_name;
    private String first_name;
    private String image_lib;
    private String avatar;
    private Date birthday;
    private Date created_at;
    private Date updated_at;
    private int user_id;



    public Profiles(int profile_id, String last_name, String first_name, String image_lib, String avatar, Date birthday, Date created_at, Date updated_at, int user_id) {
        this.profile_id = profile_id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.image_lib = image_lib;
        this.avatar = avatar;
        this.birthday = birthday;
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
    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getImage_lib() {
        return image_lib;
    }

    public void setImage_lib(String image_lib) {
        this.image_lib = image_lib;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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
        return "Profiles{" +
                "profile_id=" + profile_id +
                ", last_name='" + last_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", image_lib='" + image_lib + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birthday=" + birthday +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", user_id=" + user_id +
                '}';
    }
}
