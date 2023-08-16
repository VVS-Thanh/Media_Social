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
    private Class<?> currentActivityClass;
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

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_item_home) {
                } else if (id == R.id.nav_item_search) {
                    navigateToPage(SearchActivity.class);
                } else if (id == R.id.nav_item_share) {
//                    navigateToPage(ShareActivity.class);
                } else if (id == R.id.nav_item_likes) {
//                    navigateToPage(LikesActivity.class);
                } else if (id == R.id.nav_item_profile) {
                    navigateToPage(UserProfileActivity.class);
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
