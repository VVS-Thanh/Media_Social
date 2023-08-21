package com.example.mediasocial.Share;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Home.HomePageActivity;
import android.Manifest;
import com.example.mediasocial.R;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";
    private static final int REQUEST_PERMISSION = 1;
    //constants
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageView imagePreview;
    private EditText captionEditText;
    private Bitmap selectedImage;
    private Button postButton;
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USERID = "userId";
    private SharedPreferences sharedPreferences;
    private Context mContext = ShareActivity.this;
    private DatabaseHelper db;  private int userId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started.");

        imagePreview = findViewById(R.id.imagePreview);
        captionEditText = findViewById(R.id.captionEditText);
        postButton = findViewById(R.id.postButton);
        db = new DatabaseHelper(ShareActivity.this);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getInt(KEY_USERID, -1);

        imagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        selectedImage = (Bitmap) extras.get("data");
                        imagePreview.setImageBitmap(selectedImage);
                    }
                });
    }
    private void selectImage() {
        if (checkPermission()) {
            showImagePicker();
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
    }

    private void showImagePicker() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }
    private void post() {
        String caption = captionEditText.getText().toString().trim();
       createPost(1, caption);
    }

    private void createPost(int id, String caption){
        if ( !caption.isEmpty()) {
            boolean isInsertedPosting = db.insertPosting(id, caption);
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImagePicker();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}