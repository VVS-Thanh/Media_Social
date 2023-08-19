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


        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getInt(KEY_USERID, -1);
        userProfile = db.getProfile(userId);
        Log.d("Check", " UserName from profile: " + userProfile.getUserName());
        if (userId != -1) {
            if (!db.isProfileExists(userId)) {
                boolean isInserted = db.insertProfile(userId);
                if (isInserted) {
                    Log.d("LogInActivity", "Đã thêm thành công profile cho userID: " + userId);
                } else {
                    Log.e("LogInActivity", "Profile đã tồn tại: " + userId);
                }
            }
            String username = db.getUserName(userId);
            if (username != null) {
                tvUsername.setText(username);
            }

            Log.d("HomeActivity", "UserId: " + userId);
        }

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_item_home) {

                } else if (id == R.id.nav_item_search) {
                    navigateToPage(SearchActivity.class);
                } else if (id == R.id.nav_item_share) {

                } else if (id == R.id.nav_item_likes) {

                } else if (id == R.id.nav_item_profile) {
                    Intent userProfileIntent = new Intent(HomePageActivity.this, UserProfileActivity.class);
                    userProfileIntent.putExtra(KEY_USERID, userId);
                    startActivity(userProfileIntent);
                }
                return true;
            }
        });
    }
    private void navigateToPage(Class<?> destinationActivity) {
        if (!isCurrentPage(destinationActivity)) {
            Intent intent = new Intent(HomePageActivity.this, destinationActivity);
            startActivity(intent);
        } else {
            reloadCurrentPage();
        }
    }

    private boolean isCurrentPage(Class<?> destinationActivity) {
        return currentActivityClass != null && currentActivityClass.equals(destinationActivity);
    }

    private void reloadCurrentPage() {
        Intent intent = new Intent(HomePageActivity.this, currentActivityClass);
        startActivity(intent);
        finish();
    }
}
