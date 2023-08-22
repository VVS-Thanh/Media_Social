package com.example.mediasocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mediasocial.DBconfig.DatabaseHelper;

public class LogInActivity extends AppCompatActivity {
    public static final String KEY_USERID = "userId";
    private static final String PREF_NAME = "user_session";
    private static final String KEY_EMAIL = "email";
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        db = new DatabaseHelper(LogInActivity.this);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();



                    if (checkLogin(email, password)) {
                    int userId = db.getUserId(email);
                    int roleId = db.getUserRoleId(userId);
                    if (roleId == 1) {
                        Intent AdminIntent = new Intent(LogInActivity.this, AdminPostActivity.class);
                        startActivity(AdminIntent);
                        finish();
                    }
                    else {
                    if (!db.isProfileExists(userId)) {
                        boolean isInserted = db.insertProfile(userId);
                        if (isInserted) {
                            Log.d("LogInActivity", "Đã thêm thành công profile cho userID: " + userId);
                        } else {
                            Log.e("LogInActivity", "Profile đã tồn tại: " + userId);
                        }
                    }

                    saveSession(email, userId);

                    // Chuyển hướng tới HomePageActivity
                    Intent homePageIntent = new Intent(LogInActivity.this, HomePageActivity.class);
                    startActivity(homePageIntent);
                    finish();
                    }
                } else {
                    Toast.makeText(LogInActivity.this, "Đăng nhập thất bai! Vui lòng kiểm tra lại thông tin.", Toast.LENGTH_SHORT).show();
                    edtPassword.setText("");
                }
            }
        });
    }

    private void saveSession(String email, int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putInt(KEY_USERID, userId);
        editor.apply();
        Log.d("LogInActivity", "Saved UserID: " + userId);
    }

    private boolean checkLogin(String email, String password) {
        return db.checkLogin(email, password);
    }
}
