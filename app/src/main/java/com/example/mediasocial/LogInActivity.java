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
import com.example.mediasocial.Models.Profile;

public class LogInActivity extends AppCompatActivity {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "name";
    private static final String KEY_USERID = "userId";
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
                    String userName = db.getUserName(email);
                    int userId = db.getUserId(email);
                    int roleId = db.getUserRoleId(userId);
                    if (!db.isProfileExists(userId)) {
                        db.insertProfile(userId, userName);
                    }
                    saveSession(email, userName, userId);
                    Toast.makeText(LogInActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent;
                    if (roleId == 2) {
                        intent = new Intent(LogInActivity.this, HomePageActivity.class);
                    } else {
                        intent = new Intent(LogInActivity.this, AdminPostActivity.class);
                        Log.d("AdminPage", "RoleID: " + roleId);
                    }
                    intent.putExtra("username", userName);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LogInActivity.this, "Đăng nhập thất bai! Vui lòng kiểm tra lại thông tin.", Toast.LENGTH_SHORT).show();
                    edtPassword.setText("");
                }
            }
        });
    }

    private void saveSession(String email, String username, int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USERNAME, username);
        editor.putInt(KEY_USERID, userId);
        editor.apply();
    }

    private boolean checkLogin(String email, String password) {
        return db.checkLogin(email, password);
    }


}
