package com.example.mediasocial;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mediasocial.DBconfig.DatabaseHelper;

public class LogInActivity extends AppCompatActivity {
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
                    Toast.makeText(LogInActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LogInActivity.this, "Đăng nhập thất bai! Vui lòng kiểm tra lại thông tin.", Toast.LENGTH_SHORT).show();
                    edtPassword.setText("");
                }
            }
        });
    }

    private boolean checkLogin(String email, String password) {
        return db.checkLogin(email, password);
    }


}
