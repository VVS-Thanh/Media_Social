package com.example.mediasocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Role;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signpage);
//        String adminEmail = "admin1@gmail.com";
//        String adminPhone = "123456789";
//        String adminName = "admin_Thanh";
//        String adminPassword = "123123123";
//        int adminRoleId = 1;
//        db = new DatabaseHelper(MainActivity.this);
//        long adminUserId = db.insertUserAdmin(adminEmail, adminPhone, adminName, adminPassword, adminRoleId);
//        if (adminUserId != -1) {
//            Log.d("AdminCreation", "Admin user added successfully with ID: " + adminUserId);
//        } else {
//            Log.d("AdminCreation", "Failed to add admin user.");
//        }
            Button signUpButton = findViewById(R.id.signUp);
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });

            Button logInButton = findViewById(R.id.logIn);
            logInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(intent);
                }
            });
        }

//        Role.insertRolesData(this);

    }