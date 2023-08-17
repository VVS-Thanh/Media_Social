package com.example.mediasocial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.mediasocial.LogInActivity.KEY_USERID;
import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Profile;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvUserName;
    private TextView tvStatus;
    private Button btnEditPfrofile;
    private CircleImageView profileImage;
    private DatabaseHelper db;
    private static final String PREF_NAME = "SessionPref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USERID = "userid";

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

        int userIdFromIntent = getIntent().getIntExtra(KEY_USERID, -1);
        Profile userProfile = getProfile(userIdFromIntent);

        if (userProfile != null) {
            if (userProfile.hasAvatar()) {
                int avatarResId = userProfile.getAvatarResId();
                profileImage.setImageResource(avatarResId);
                Log.d("UserProfileActivity", "Set custom avatar image");
            } else {
                Log.d("UserProfileActivity", "User does not have a custom avatar");
            }
        } else {
            Log.d("UserProfileActivity", "User profile is null");
        }
        Log.d("UserProfileActivity", "UserID_Check: " + userIdFromIntent);


        displayUserProfile(userIdFromIntent);

        btnEditPfrofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayUserProfile(int userId) {
        Profile userProfile = db.getProfile(userId);
        if (userProfile != null) {
            Log.d("UserProfileActivity", "User ID: " + userId);
            Log.d("UserProfileActivity", "User Name: " + userProfile.getUserName());
            Log.d("UserProfileActivity", "First Name: " + userProfile.getFirstName());
            Log.d("UserProfileActivity", "Last Name: " + userProfile.getLastName());
            Log.d("UserProfileActivity", "Profile ID: " + userProfile.getProfileId());

            tvName.setText(userProfile.getUserName());
            tvUserName.setText(userProfile.getUserName());
            tvStatus.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
        } else {
            Log.e("UserProfileActivity", "User Profile is null");
        }
    }

    private Profile getProfile(int userId) {
        return db.getProfile(userId);
    }
}