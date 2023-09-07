package com.example.mediasocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Post;
import com.example.mediasocial.R;

import java.util.List;

public class PostOfUserActivity extends AppCompatActivity implements PostOfUserAdapter.DeleteClickListener {

    private RecyclerView recyclerView;
    private PostOfUserAdapter adapter;
    private ImageView btnBack;
    private DatabaseHelper databaseHelper;
    private static final String KEY_USERID = "userId";
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manager_postofuser);

        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.post_of_userRecyclerView);
        databaseHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra(KEY_USERID, -1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<Post> userPosts = databaseHelper.getAllPostsByUserId(userId);
        adapter = new PostOfUserAdapter(userPosts, databaseHelper, this, this);
        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostOfUserActivity.this, UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onDeleteClick(Post post) {
        int position = adapter.getPositionForPost(post);
        if (position != RecyclerView.NO_POSITION) {
            boolean deleted = databaseHelper.deletePost(post.getPostId());
            if (deleted) {
                adapter.removePostAtPosition(position);
            }
        }
    }
}
