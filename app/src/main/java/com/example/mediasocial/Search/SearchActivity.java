package com.example.mediasocial.Search;

import android.content.Context;
import android.content.Intent;
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
import com.example.mediasocial.Models.Users;
import com.example.mediasocial.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = SearchActivity.this;

    //widgets
    private EditText mSearchParam;
    private ListView mListView;
    private static List<Users> searchResults;
    private List<Users> mUserList;
     private List<Users> userList ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearchParam = (EditText)findViewById(R.id.search);
        mListView = (ListView) findViewById(R.id.listUser);
        Log.d(TAG, "onCreate: started.");
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
    }
    //filter by keyword
    private void searchForMatch(String keyword){
        Log.d(TAG, "searchForMatch: searching for a match: " + keyword);
        searchResults = new ArrayList<>();
        searchResults.clear();
        mUserList = getAllUsers();

        if(keyword.length()==0){

        }else{
            for (Users user : mUserList) {
                if (user.getName().toLowerCase(Locale.getDefault()).contains(keyword)) {
                        searchResults.add(user);
                        updateUsersList();
                }
            }
            Log.d("list filter", String.valueOf(searchResults.size()));
        }
    }

    //get all user in db.
    public List<Users> getAllUsers() {

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

            Users user = new Users(id, email, null, name, null, null, null, role_id);
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
        for(Users us: searchResults){
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
