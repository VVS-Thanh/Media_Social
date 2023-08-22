package com.example.mediasocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {

    private TextView tvUsername;
    private DatabaseHelper db;
    private SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavigationView;
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USERID = "userId";
    private int userId;
    private Profile userProfile;
    private Class<?> currentActivityClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_newsfeed_page);

        tvUsername = findViewById(R.id.tvUsername);
        db = new DatabaseHelper(HomePageActivity.this);

        // Lấy userId từ SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getInt(KEY_USERID, -1);

        // Hiển thị tên người dùng
        String username = db.getUserName(userId);
        if (username != null) {
            tvUsername.setText(username);
        }

        bottomNavigationView = findViewById(R.id.navigation);

        // Sự kiện khi chọn các mục trên Bottom Navigation View
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_item_home) {
                    // Đã ở trang chủ
                } else if (id == R.id.nav_item_search) {
                    Log.d("SearchPageActivity", "Navigating to SearchActivity");
                    // Chuyển tới trang tìm kiếm
                    Intent SearchIntent = new Intent(HomePageActivity.this, SearchActivity.class);
                    SearchIntent.putExtra(KEY_USERID, userId);
                    startActivity(SearchIntent);
                    Log.d("SearchPageActivity", "onCreate finished");
                    finish();

                } else if (id == R.id.nav_item_share) {
                    Intent ShareIntent = new Intent(HomePageActivity.this, ShareActivity.class);
                    ShareIntent.putExtra(KEY_USERID, userId);
                    startActivity(ShareIntent);
                    Log.d("ShareIntent", "onCreate finished");
                    finish();
                } else if (id == R.id.nav_item_likes) {
                    // Xem danh sách yêu thích
                } else if (id == R.id.nav_item_profile) {
                    // Chuyển tới trang hồ sơ người dùng
                    Log.d("UserProfileActivity", "Navigating to UserProfileActivity");
                    Intent userProfileIntent = new Intent(HomePageActivity.this, UserProfileActivity.class);
                    userProfileIntent.putExtra(KEY_USERID, userId);
                    startActivity(userProfileIntent);
                    finish();
                }
                return true;
            }
        });

        Log.d("HomePageActivity", "onCreate finished");
    }

}

