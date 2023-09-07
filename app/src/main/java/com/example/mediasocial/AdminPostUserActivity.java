package com.example.mediasocial;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediasocial.Adapters.AdminPostAdapter;
import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Post;

import java.util.List;

public class AdminPostUserActivity extends AppCompatActivity implements AdminPostAdapter.DeleteClickListener {

    private RecyclerView recyclerView;
    private AdminPostAdapter adminPostAdapter;
    private ImageView btnBack;
    private List<Post> postList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manager_post_admin);

        db = new DatabaseHelper(this);
        postList = db.getAllPosts();

        recyclerView = findViewById(R.id.recyclerViewAdminPost);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.btnBack);
        adminPostAdapter = new AdminPostAdapter(postList, db, this, this);
        recyclerView.setAdapter(adminPostAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AdminPage = new Intent(AdminPostUserActivity.this, AdminPostActivity.class);
                startActivity(AdminPage);
                finish();
            }
        });
    }

    @Override
    public void onDeleteClick(Post post) {
        showDeleteConfirmationDialog(post);
    }

    private void showDeleteConfirmationDialog(final Post post) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete")
                .setMessage("Bạn muốn xoá bài viết này không?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deletePost(post.getPostId());
                        postList.remove(post);
                        adminPostAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Huỷ bỏ", null);

        builder.create().show();
    }
}
