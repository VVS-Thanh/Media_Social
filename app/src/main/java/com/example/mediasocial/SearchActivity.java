package com.example.mediasocial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.User;
import com.example.mediasocial.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    //widgets
    private DatabaseHelper db;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USERID = "userId";
    private EditText mSearchParam;
    private int userId;
    private ListView mListView;
    private static List<User> searchResults;
    private List<User> mUserList;
    private List<User> userList ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearchParam = (EditText)findViewById(R.id.search);
        mListView = (ListView) findViewById(R.id.listUser);
        Log.d("SearchActivity", "onCreate: started.");
        db = new DatabaseHelper(SearchActivity.this);
        // Lấy userId từ SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getInt(KEY_USERID, -1);
        initTextListener();

    }

    private void initTextListener() {
        Log.d("SearchActivity", "initTextListener: initializing");

        mSearchParam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String keyword = charSequence.toString().toLowerCase(Locale.getDefault());
                searchForMatch(keyword);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void searchForMatch(String keyword){
        Log.d("SearchActivity", "searchForMatch: searching for a match: " + keyword);
        searchResults = new ArrayList<>();
        if (keyword.isEmpty()) {
            searchResults.addAll(mUserList);
        } else {
            for (User user : mUserList) {
                if (user.getName().toLowerCase(Locale.getDefault()).startsWith(keyword)) {
                    searchResults.add(user);
                }
            }
        }
        updateUsersList();
    }

//    public List<User> getAllUsers() {
//        List<User> userList = new ArrayList<>();
//        DatabaseHelper dbHelper = new DatabaseHelper(this);
//        userList = dbHelper.get();
//        return userList;
//    }

    private void updateUsersList(){
        Log.d("SearchActivity", "updateUsersList: updating users list");
        List<String> userNames = new ArrayList<>();

        for(User user : searchResults){
            userNames.add(user.getName());
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
        mListView.setAdapter(mAdapter);
    }
}

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, "onItemClick: selected user: " + mUserList.get(position).toString());

        //navigate to profile activity
//                Intent intent =  new Intent(SearchActivity.this, ProfileActivity.class);
//                intent.putExtra(getString(R.string.), getString(R.string.search_activity));
//                intent.putExtra(getString(R.string.intent_user), mUserList.get(position));
//                startActivity(intent);
//            }
//        });

