package com.example.mediasocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
//import com.example.mediasocial.Adapters.PostAdapter;
import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Post;
import com.example.mediasocial.Models.Profile;
import com.example.mediasocial.Models.User;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvUserName;
    private TextView tvStatus;
    private Button btnEditPfrofile;
    private CircleImageView profileImage;
    private ImageView btnBack;
    private DatabaseHelper db;
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USERID = "userId";
    private SharedPreferences sharedPreferences;

    private GridView mGridView;
//    private PostAdapter mPostAdapter;
    private List<Post> mPostList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        mGridView = findViewById(R.id.gridView);
//        PostAdapter PostAdapter = new PostAdapter(this);
        tvName = findViewById(R.id.tvName);
        tvUserName = findViewById(R.id.tvUsername);
        tvStatus = findViewById(R.id.tvStatus);
        profileImage = findViewById(R.id.profileImage);
        btnEditPfrofile = findViewById(R.id.btnEditPfrofile);
        btnBack = findViewById(R.id.btnBack);

        db = new DatabaseHelper(UserProfileActivity.this);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int userId = sharedPreferences.getInt(KEY_USERID, -1);
//
//        mPostAdapter = new PostAdapter(this);


        mPostList = db.getAllPostsByUserId(userId);

//        mPostAdapter.setPostList(mPostList);
//        mGridView.setAdapter(mPostAdapter); //

        Log.d("UserProfile", "UserID_PRofile: " + userId);

        displayUserProfile(userId);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent = new Intent(UserProfileActivity.this, HomePageActivity.class);
                HomeIntent.putExtra(KEY_USERID, userId);
                startActivity(HomeIntent);
                finish();
            }
        });


        btnEditPfrofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userEditProfileIntent = new Intent( UserProfileActivity.this, EditProfileActivity.class);
                userEditProfileIntent.putExtra(KEY_USERID, userId);
                startActivity(userEditProfileIntent);
            }
        });
    }

    private void displayUserProfile(int userId) {
        Profile userProfile = db.getProfile(userId);
        String username = db.getUserName(userId);

        if (userProfile != null) {
            Log.d("UserProfileActivity", "User ID: " + userId);
            Log.d("UserProfileActivity", "User Name: " + userProfile.getUserName());
            Log.d("UserProfileActivity", "First Name: " + userProfile.getFirstName());
            Log.d("UserProfileActivity", "Last Name: " + userProfile.getLastName());
            Log.d("UserProfileActivity", "Profile ID: " + userProfile.getProfileId());
            Log.d("AvatarPath", "Selected image absolute path: " + userProfile.getAvatar());
            tvName.setText(username);
            tvUserName.setText(userProfile.getUserName());
            tvStatus.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
            if (userProfile.getAvatar() != null && !userProfile.getAvatar().isEmpty()) {
                Glide.with(this)
                        .load(Uri.parse(userProfile.getAvatar()))
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.drawable.user);
            }
        } else {
            Log.e("UserProfileActivity", "User Profile is null");
        }
    }

    private Profile getProfile(int userId) {
        return db.getProfile(userId);
    }
}