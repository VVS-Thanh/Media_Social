package com.example.mediasocial.Models;

import static androidx.core.content.res.TypedArrayUtils.getResourceId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Profile {
    private int profileId;
    private String lastName;
    private String firstName;
    private String userName;
    private String imageLib;
    private String avatar;
    private Date birthday;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private int userId;

    public Profile(int profileId, String lastName, String firstName, String userName,
                   String imageLib, String avatar, Date birthday, Date createdAt,
                   Date updatedAt, Date deletedAt, int userId) {
        this.profileId = profileId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.userName = userName;
        this.imageLib = imageLib;
        this.avatar = avatar;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.userId = this.userId;
    }


    public boolean hasAvatar() {
        return avatar != null && !avatar.isEmpty();
    }

    public String getAvatarResName() {
        if (hasAvatar()) {
            return avatar;
        } else {
            return "user"; // Default avatar resource name
        }
    }

    public int getAvatarResId() {
        // Assuming that the avatar name in resources matches the image file name
        return getResourceId(getAvatarResName(), "drawable");
    }


    public String getFormattedCreatedAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(createdAt);
    }

    public String getFormattedUpdatedAt() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        return dateFormat.format(updatedAt);
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageLib() {
        return imageLib;
    }

    public void setImageLib(String imageLib) {
        this.imageLib = imageLib;
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

    private int getResourceId(String resourceName, String resourceType) {
        return 0;
    }
}
