package com.example.mediasocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Like;
import com.example.mediasocial.Models.Post;
import com.example.mediasocial.Models.Profile;
import com.example.mediasocial.Models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HomePageActivity extends AppCompatActivity {

    private TextView tvUsername, tvOwnName, tvCaption, tvPostDay;
    private ImageView imagePost, avatar;
    private DatabaseHelper db;
    private SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavigationView;
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USERID = "userId";
    private int userId;
    private Profile userProfile;
    private Class<?> currentActivityClass;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_newsfeed_page);
        avatar = findViewById(R.id.avatar);
        tvOwnName = findViewById(R.id.tvOwnname);
        recyclerView = findViewById(R.id.view_list_post);
        db = new DatabaseHelper(HomePageActivity.this);

        // Lấy userId từ SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getInt(KEY_USERID, -1);

        // Hiển thị tên người dùng
        showAccount();
        renderpost();
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
    public void showAccount(){
        String username = db.getUserName(userId);
        tvOwnName.setText(username);
        Log.d("user", username);
        userProfile = db.getProfile(userId);
        String avatarContentUri = userProfile.getAvatar();
        Glide.with(this)
                .load(avatarContentUri)
                .error(R.drawable.user)
                .into(avatar);
//        Glide.with(this)
//                .load(Uri.parse(userProfile.getAvatar()))
//                .into(avatar);
    }

    public void renderpost(){
        List<Post> posts= new ArrayList<>();
        List<String> userlike = new ArrayList<>();
        posts = db.getAllPosts();

        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                return post2.getCreatedAt().compareTo(post1.getCreatedAt());
            }
        });

        PostAdapter adapter = new PostAdapter(posts,db);
        Log.d("testadapter", String.valueOf(adapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }



}

