package com.example.mediasocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Profile;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvUserName;
    private TextView tvStatus;
    private Button btnEditPfrofile;
    private CircleImageView profileImage;
    private DatabaseHelper db;
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USERID = "userId";
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        tvName = findViewById(R.id.tvName);
        tvUserName = findViewById(R.id.tvUsername);
        tvStatus = findViewById(R.id.tvStatus);
        profileImage = findViewById(R.id.profileImage);
        btnEditPfrofile = findViewById(R.id.btnEditPfrofile);

        db = new DatabaseHelper(UserProfileActivity.this);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int userId = sharedPreferences.getInt(KEY_USERID, -1);

        Log.d("UserProfile", "UserID_PRofile: " + userId);

        displayUserProfile(userId);


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