package com.example.mediasocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.MenuItem;
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
    private static final String PREF_NAME = "SessionPref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USERID = "userid";
    private int userId;
    private Profile userProfile;
    private Class<?> currentActivityClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_newsfeed_page);
        tvUsername = findViewById(R.id.tvUsername);
        db = new DatabaseHelper(HomePageActivity.this);

        Intent intent = getIntent();
        if (intent != null) {
            String username = intent.getStringExtra("username");
            userId = intent.getIntExtra("userId", -1);
            tvUsername.setText(username);
        }

        Log.d("HomePageActivity", "Username: " + tvUsername.getText());
        Log.d("HomePageActivity", "UserID: " + userId);

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_item_home) {
                    // Do something for home
                } else if (id == R.id.nav_item_search) {
                    navigateToPage(SearchActivity.class);
                } else if (id == R.id.nav_item_share) {
                    // Do something for share
                } else if (id == R.id.nav_item_likes) {
                    // Do something for likes
                } else if (id == R.id.nav_item_profile) {
                    Intent userProfileIntent = new Intent(HomePageActivity.this, UserProfileActivity.class);
                    userProfileIntent.putExtra(KEY_USERID, userId);
                    startActivity(userProfileIntent);
                }
                return true;
            }
        });
    }

    private Profile getProfile(int userId) {
        return db.getProfile(userId);
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
