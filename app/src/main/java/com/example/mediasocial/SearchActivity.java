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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
    private static final String TAG = "SearchActivity";
    private DatabaseHelper db;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USERID = "userId";
    private EditText mSearchParam;
    private ImageView btnBack;
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
        btnBack =findViewById(R.id.btnBack);
        Log.d("SearchActivity", "onCreate: started.");
        db = new DatabaseHelper(SearchActivity.this);
        // Lấy userId từ SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getInt(KEY_USERID, -1);
        initTextListener();

    }

    private void initTextListener(){
        Log.d(TAG, "initTextListener: initializing");

        mSearchParam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = mSearchParam.getText().toString().toLowerCase(Locale.getDefault());
                searchForMatch(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent = new Intent(SearchActivity.this, HomePageActivity.class);
                HomeIntent.putExtra(KEY_USERID, userId);
                startActivity(HomeIntent);
                finish();
            }
        });
    }
    //filter by keyword
    private void searchForMatch(String keyword){
        Log.d(TAG, "searchForMatch: searching for a match: " + keyword);
        searchResults = new ArrayList<>();
        searchResults.clear();
        mUserList = getAllUsers();

        if(keyword.length()==0){

        }else{
            for (User user : mUserList) {
                if (user.getName().toLowerCase(Locale.getDefault()).contains(keyword)) {
                    searchResults.add(user);

                }
            }
            updateUsersList();
            Log.d("list filter", String.valueOf(searchResults.size()));
        }
    }

    //get all user in db.
    public List<User> getAllUsers() {

        userList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"user_id", "name", "email", "role_id"};
        Cursor cursor = db.query("users", projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            int role_id = cursor.getInt(cursor.getColumnIndexOrThrow("role_id"));

            User user = new User(id, email, null, name, null, null, null,null, role_id);
            userList.add(user);
//            Log.d(TAG, String.valueOf(userList.size()));
        }

        cursor.close();
        db.close();

        return userList;

    }

    private void updateUsersList(){
        Log.d(TAG, "updateUsersList: updating users list");
        List<String> u = new ArrayList<>();
        Log.d("filter list", String.valueOf(searchResults.size()));
        for(User us: searchResults){
            u.add(us.getName());
        }
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, u);
        mListView.setAdapter(mAdapter);

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

