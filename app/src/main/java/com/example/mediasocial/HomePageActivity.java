package com.example.mediasocial;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Profile;

public class HomePageActivity extends AppCompatActivity {

    private TextView tvUsername;
    private DatabaseHelper db;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "SessionPref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USERID = "userid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_newsfeed_page);
        tvUsername = findViewById(R.id.tvUsername);
        db = new DatabaseHelper(HomePageActivity.this);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        String username = sharedPreferences.getString(KEY_USERNAME, "");
        int userId = sharedPreferences.getInt(KEY_USERID, -1);
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("username");
            tvUsername.setText(username);
        }

        Log.d("HomePageActivity", "Username: " + username);
    }

    private Profile getProfile(int userId) {
        return db.getProfile(userId);
    }
}
