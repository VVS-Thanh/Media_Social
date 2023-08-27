package com.example.mediasocial.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.User;
import com.example.mediasocial.R;

import java.util.List;

public class adminAdapter extends BaseAdapter {
    private Context context;
    private List<User> userList;
    private DatabaseHelper db;

    public adminAdapter(Context context, List<User> userList, DatabaseHelper db) {
        this.context = context;
        this.userList = userList;
        this.db = db;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public User getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }

        TextView emailTextView = convertView.findViewById(R.id.emailTextView);
        TextView roleTextView = convertView.findViewById(R.id.roleTextView);
        ImageButton editButton = convertView.findViewById(R.id.editButton); // Replace with actual ID
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton); // Replace with actual ID
        User user = getItem(position);
        emailTextView.setText(user.getEmail());
        roleTextView.setText("Role: " + user.getRoleId());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit action here
                showEditRoleDialog(position);
            }
        });

        // Handle Delete button click
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete action here
                showDeleteConfirmationDialog(position);
            }
        });


        return convertView;
    }

    private void showEditRoleDialog(final int position) {
        final User user = userList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chỉnh sửa quyền")
                .setSingleChoiceItems(new CharSequence[]{"Admin", "User"}, user.getRoleId() - 1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int newRoleId = which + 1; // Adjust index to roleId values
                                boolean updated = db.updateUserRoleWithRoleId(newRoleId, user.getUserId());
                                if (updated) {
                                    userList.get(position).setRoleId(newRoleId);
                                    notifyDataSetChanged();
                                }
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Huỷ bỏ", null);

        builder.create().show();
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xoá user")
                .setMessage("Bạn muốn xoá user này không?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int userIdToDelete = userList.get(position).getUserId();
                        Log.d("DeleteUser", "User ID to delete: " + userIdToDelete); // Log giá trị userID
                        boolean deleted = db.deleteUser(userIdToDelete);
                        if (deleted) {
                            userList.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton("Huỷ bỏ", null);

        builder.create().show();
    }

}
