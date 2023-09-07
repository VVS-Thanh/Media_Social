package com.example.mediasocial.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Post;
import com.example.mediasocial.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminPostAdapter extends RecyclerView.Adapter<AdminPostAdapter.AdminPostViewHolder> {

    private List<Post> postList;
    private DatabaseHelper db;
    private Context context;
    private Set<Integer> markedPositions;
    private DeleteClickListener deleteClickListener;

    public AdminPostAdapter(List<Post> postList, DatabaseHelper db, Context context, DeleteClickListener deleteClickListener) {
        this.postList = postList;
        this.db = db;
        this.context = context;
        this.markedPositions = new HashSet<>();
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public AdminPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_manager_post_admin, parent, false);
        return new AdminPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminPostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.textViewPostTitle.setText(post.getContent());
        String username = db.getUserNameFromPost(post.getUserId());
        holder.textViewUsername.setText(username);

        if (markedPositions.contains(holder.getAdapterPosition())) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        // Nút đánh dấu
        holder.buttonMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    if (markedPositions.contains(clickedPosition)) {
                        markedPositions.remove(clickedPosition);
                    } else {
                        markedPositions.add(clickedPosition);
                    }
                    notifyDataSetChanged();
                }
            }
        });

        // Nút xoá
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    deleteClickListener.onDeleteClick(postList.get(clickedPosition));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public interface DeleteClickListener {
        void onDeleteClick(Post post);
    }

    public class AdminPostViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPostTitle;
        TextView textViewUsername;
        ImageButton buttonMark;
        ImageButton buttonDelete;

        public AdminPostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPostTitle = itemView.findViewById(R.id.textViewPostTitle);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            buttonMark = itemView.findViewById(R.id.btnMark);
            buttonDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
