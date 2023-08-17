package com.example.mediasocial.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediasocial.Models.Users;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private List<Users> userList;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(android.R.id.text1);
        }
    }

    public UserListAdapter(List<Users> users) {
        this.userList = users;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View userView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(userView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Users user = userList.get(position);
        holder.nameTextView.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        Log.d("user_count", String.valueOf(userList.size()));
        return userList.size();

    }
}
