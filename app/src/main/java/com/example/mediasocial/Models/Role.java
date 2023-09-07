package com.example.mediasocial.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;

import com.example.mediasocial.DBconfig.DatabaseHelper;

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

    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }


    public static void insertRolesData(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Chèn dữ liệu role admin
        values.put("role_id", 1);
        values.put("role_name", "admin");
        values.put("created_at", getCurrentDateTime());
        db.insert("roles", null, values);

        //Chèn dữ liệu role user
        values.clear();
        values.put("role_id", 2);
        values.put("role_name", "user");
        values.put("created_at", getCurrentDateTime());
        db.insert("roles", null, values);

        // Đóng kết nối với cơ sở dữ liệu sau khi chèn dữ liệu
        db.close();
    }

}

