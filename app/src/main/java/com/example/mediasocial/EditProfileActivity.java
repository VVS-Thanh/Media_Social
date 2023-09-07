package com.example.mediasocial;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mediasocial.UserProfileActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Profile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvFirstname, tvLastName, tvBirthday;
    private ImageButton btnImage;
    private ImageView btnUpdate;
    private ImageView btnBack;
    private CircleImageView profileImage;
    private Calendar calendar;
    private String oldUsername, oldFirstName, oldLastName;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USERID = "userId";
    private SharedPreferences sharedPreferences;
    private DatabaseHelper db;
    private int userId;
    String newAvatarPath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_profile);
        btnImage = findViewById(R.id.btnImage);
        profileImage = findViewById(R.id.profileImage);
        tvUsername = findViewById(R.id.tvUsername);
        tvFirstname = findViewById(R.id.tvFistName);
        tvLastName = findViewById(R.id.tvLastName);
        tvBirthday = findViewById(R.id.tvBirthday);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);

        db = new DatabaseHelper(EditProfileActivity.this);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getInt(KEY_USERID, -1);


        populateProfileInfo();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userProfileIntent = new Intent(EditProfileActivity.this, UserProfileActivity.class);
                userProfileIntent.putExtra(KEY_USERID, userId);
                startActivity(userProfileIntent);
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileInfo();
            }
        });



        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        newAvatarPath = selectedImageUri.toString(); // Lưu đường dẫn hình ảnh mới
                        Glide.with(EditProfileActivity.this)
                                .load(selectedImageUri)
                                .into(profileImage);
                    }
                }
        );

        calendar = Calendar.getInstance();

        tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(tvUsername, "Thay đổi tên người dùng", oldUsername);
            }
        });

        tvFirstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(tvFirstname, "Thay đổi họ", oldFirstName);
            }
        });

        tvLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(tvLastName, "Thay đổi tên", oldLastName);
            }
        });

        tvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
        Log.d("AvatarPath", "Image picker event triggered");
    }


    private void showInputDialog(final TextView textView, String title, String defaultValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        final EditText inputEditText = viewInflated.findViewById(R.id.input);
        inputEditText.setText(defaultValue);
        builder.setView(viewInflated);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newInput = inputEditText.getText().toString();
                if (!newInput.isEmpty()) {
                    textView.setText(newInput);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        Calendar currentDate = Calendar.getInstance();
                        if (selectedDate.after(currentDate)) {
                            selectedDate = currentDate;
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date finalDate = selectedDate.getTime();
                        tvBirthday.setText(sdf.format(finalDate));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void populateProfileInfo() {
        if (userId != -1) {
            Profile profile = db.getProfile(userId);
            String avatarPath = db.getAvatar(userId);
            if (profile != null) {
                if (profile.getAvatar() != null && !profile.getAvatar().isEmpty()){
                    Uri uri = Uri.parse(avatarPath);
                    Log.d("Uri", uri.toString());
                    Glide.with(EditProfileActivity.this)
                            .load(uri) // Đường dẫn đến hình ảnh
                            .centerCrop()
                            .into(profileImage);
                }
                tvFirstname.setText(profile.getFirstName());
                tvLastName.setText(profile.getLastName());
                tvUsername.setText(profile.getUserName());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedBirthday = sdf.format(profile.getBirthday());
                tvBirthday.setText(formattedBirthday);

            }
        }
    }

    private void updateProfileInfo() {
        String newFirstName = tvFirstname.getText().toString().trim();
        String newLastName = tvLastName.getText().toString().trim();
        String newUserName = tvUsername.getText().toString().trim();
        String newBirthdayStr = tvBirthday.getText().toString().trim();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date newBirthday = sdf.parse(newBirthdayStr);

            boolean isUpdated = db.updateProfileWithAvatar(userId, newUserName, newFirstName, newLastName, newBirthday, newAvatarPath);

            if (isUpdated) {
                Toast.makeText(this, "Cập nhật hồ sơ thành công", Toast.LENGTH_SHORT).show();
                tvBirthday.setText(sdf.format(newBirthday));
                tvBirthday.invalidate();

                Intent userProfileIntent = new Intent(EditProfileActivity.this, UserProfileActivity.class);
                userProfileIntent.putExtra(KEY_USERID, userId);
                startActivity(userProfileIntent);
                finish();
            } else {
                Toast.makeText(this, "Không thể cập nhật hồ sơ", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show();
        }

    }


}






