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
                    // Chuyển tới trang tìm kiếm
//                    navigateToPage(SearchActivity.class);
                } else if (id == R.id.nav_item_share) {
                    // Chia sẻ nội dung
                } else if (id == R.id.nav_item_likes) {
                    // Xem danh sách yêu thích
                } else if (id == R.id.nav_item_profile) {
                    // Chuyển tới trang hồ sơ người dùng
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

    // Hàm chuyển tới trang nào đó (nếu đã mở thì reload)
//    private void navigateToPage(Class<?> destinationActivity) {
//        if (!isCurrentPage(destinationActivity)) {
//            Intent intent = new Intent(HomePageActivity.this, destinationActivity);
//            startActivity(intent);
//        } else {
//            reloadCurrentPage();
//        }
//    }
//
//    // Kiểm tra xem đã ở trang nào đó chưa
//    private boolean isCurrentPage(Class<?> destinationActivity) {
//        return currentActivityClass != null && currentActivityClass.equals(destinationActivity);
//    }
//
//    // Tải lại trang hiện tại
//    private void reloadCurrentPage() {
//        Intent intent = new Intent(HomePageActivity.this, currentActivityClass);
//        startActivity(intent);
//        finish();
//    }
}

