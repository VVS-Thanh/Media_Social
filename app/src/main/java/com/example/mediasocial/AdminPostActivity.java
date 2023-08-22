package com.example.mediasocial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediasocial.Adapters.adminAdapter;
import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.User;
import java.util.List;

public class AdminPostActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private List<User> userList;
    private ListView userListView;
    private adminAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_post);

        db = new DatabaseHelper(this);
        userList = db.getAllUsers();

        userListView = findViewById(R.id.userListView);
        adapter = new adminAdapter(this, userList, db);
        userListView.setAdapter(adapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showOptionsDialog(position);
            }
        });
    }

    private void showOptionsDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Action")
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
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private void showEditRoleDialog(final int position) {
        final User user = userList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Role")
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
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean deleted = db.deleteUser(userList.get(position).getUserId());
                        if (deleted) {
                            userList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }
}