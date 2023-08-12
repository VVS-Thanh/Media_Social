package com.example.mediasocial;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mediasocial.DBconfig.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    TextView names;
    String tname = "roles";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_view_post);

//        setContentView(R.layout.activity_search);
        names = (TextView)findViewById(R.id.username);
        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        // Retrieve data from the existing table

    }
}