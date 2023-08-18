package com.example.mediasocial;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvFirstname, tvLastName, tvBirthday;
    private ImageButton btnImage;
    private CircleImageView profileImage;
    private Calendar calendar;
    private String oldUsername, oldFirstName, oldLastName;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

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


        oldUsername = tvUsername.getText().toString();
        oldFirstName = tvFirstname.getText().toString();
        oldLastName = tvLastName.getText().toString();

        calendar = Calendar.getInstance();

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        profileImage.setImageURI(selectedImageUri);
                    }
                }
        );

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

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
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            profileImage.setImageURI(selectedImageUri);
        }
    }

    private void showInputDialog(final TextView textView, String title, String defaultValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        final EditText inputEditText = viewInflated.findViewById(R.id.input);
        inputEditText.setText(defaultValue); // Hiển thị giá trị cũ trong hộp thoại
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
}
