package com.example.mediasocial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.mediasocial.R;

import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.HomePageActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;


public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";
    //constants
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageView imagePreview;
    private ImageView btnBack;
    private EditText captionEditText;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    String newAvatarPath = null;
    private Bitmap selectedImage;
    private Button postButton;
    private DatabaseHelper db;
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USERID = "userId";
    private SharedPreferences sharedPreferences;

    private ViewPager2 mViewPager;
    private int userId;
    Uri selectedImageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started.");

        imagePreview = findViewById(R.id.imagePreview);
        captionEditText = findViewById(R.id.captionEditText);
        postButton = findViewById(R.id.postButton);
        btnBack = findViewById(R.id.btnBack);
        db = new DatabaseHelper(ShareActivity.this);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getInt(KEY_USERID, -1);

        imagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromDevice();
            }
        });
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d("imagepath", result.getData().getData().toString());
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        newAvatarPath = selectedImageUri.toString();
                        Log.d("imagepath", newAvatarPath);
                        Glide.with(ShareActivity.this)
                                .load(selectedImageUri)
                                .into(imagePreview);
                    }
                }
        );
        Log.d("imagepath", "newAvatarPath");

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShareActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void selectImageFromDevice() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }
    private void post() {
        String caption = captionEditText.getText().toString().trim();
        String urlimage = newAvatarPath;
        createPost(userId, caption, urlimage);
    }

    private void createPost(int id, String caption, String urlimage){
        if ( !caption.isEmpty()) {
            boolean isInsertedPosting = db.insertPosting(id, caption, urlimage);
            if(isInsertedPosting==true){
                Toast.makeText(this, "Post successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShareActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "Posting failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select an image and enter a caption", Toast.LENGTH_SHORT).show();
        }
    }

}