package com.example.mediasocial;


import static com.example.mediasocial.Models.Role.getCurrentDateTime;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.User;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail;
    private EditText edtUsername;
    private EditText edtPhone;
    private EditText edtPassword;
    private EditText edtComfirmPassword;
    private Button btnSignUp;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtComfirmPassword = findViewById(R.id.edtComfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        db = new DatabaseHelper(SignUpActivity.this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String username = edtUsername.getText().toString();
                String phone = edtPhone.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPassword = edtComfirmPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng điền đầy đủ các thông tin!", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu không trùng nhau!", Toast.LENGTH_SHORT).show();
                } else if (db.isEmailRegistered(email)) {
                    Toast.makeText(SignUpActivity.this, "Email này đã tồn tại, hãy chọn email khác!", Toast.LENGTH_SHORT).show();
                } else if (db.isUsernameTaken(username)) {
                    Toast.makeText(SignUpActivity.this, "Tên người dùng đã tồn tại, hãy chọn tên khác!", Toast.LENGTH_SHORT).show();
                } else {
                    createUser(email, username,phone, password);
                    Intent loginIntent = new Intent(SignUpActivity.this, LogInActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        });
    }

    private void createUser(String email, String username , String phone, String password) {
        String currentDateTime = getCurrentDateTime();

        boolean checkUser = db.insertUser(email,username, phone, password);

        if (checkUser) {
            Toast.makeText(SignUpActivity.this, "Thêm người dùng thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(SignUpActivity.this, "Thêm không thành công, hãy thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
}

