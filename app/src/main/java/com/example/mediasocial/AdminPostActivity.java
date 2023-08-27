package com.example.mediasocial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediasocial.Adapters.adminAdapter;
import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.User;
import java.util.List;

public class AdminPostActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private Button btnManageUsers, btnManagePosts;
    private List<User> userList;
    private ListView userListView;
    private adminAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_post);
        Log.d("Admin Page", "Login" );
        db = new DatabaseHelper(this);
        userList = db.getAllUsers();
        btnManagePosts = findViewById(R.id.btnManagePosts);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        userListView = findViewById(R.id.userListView);
        adapter = new adminAdapter(this, userList, db);
        userListView.setAdapter(adapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showOptionsDialog(position);
            }
        });

        btnManagePosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPostActivity.this, AdminPostUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showOptionsDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa quyền")
                .setItems(new CharSequence[]{"Edit Role", "Delete User"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showEditRoleDialog(position);
                        } else if (which == 1) {
                            showDeleteConfirmationDialog(position);
                        }
                    }
                })
                .setNegativeButton("Huỷ bỏ", null);

        builder.create().show();
    }

    private void showEditRoleDialog(final int position) {
        final User user = userList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa quyền")
                .setSingleChoiceItems(new CharSequence[]{"Admin", "User"}, user.getRoleId() - 1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int newRoleId = which + 1; // Adjust index to roleId values
                                boolean updated = db.updateUserRoleWithRoleId(newRoleId, user.getUserId());
                                if (updated) {
                                    userList.get(position).setRoleId(newRoleId);
                                    adapter.notifyDataSetChanged();
                                }
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Huỷ bỏ", null);

        builder.create().show();
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xoá user")
                .setMessage("Bạn muốn xoá user này không?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean deleted = db.deleteUser(userList.get(position).getUserId());
                        if (deleted) {
                            userList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton("Huỷ bỏ", null);

        builder.create().show();
    }
}

