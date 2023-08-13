package com.example.mediasocial.Models;

import java.util.Date;

public class Role {

        private int roleId;
        private String roleName;
        private Date createdAt;
        private Date updatedAt;
        private Date deletedAt;

    public Role(int roleId, String roleName, Date createdAt, Date updatedAt, Date deletedAt) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}

