package com.example.mediasocial;// ... (same imports as before)

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Post;
import com.example.mediasocial.R;

import java.util.List;

public class PostOfUserAdapter extends RecyclerView.Adapter<PostOfUserAdapter.PostViewHolder> {

    private List<Post> postList;
    private DatabaseHelper databaseHelper;
    private Context context;
    private DeleteClickListener deleteClickListener;

    public PostOfUserAdapter(List<Post> postList, DatabaseHelper databaseHelper, Context context, DeleteClickListener deleteClickListener) {
        this.postList = postList;
        this.databaseHelper = databaseHelper;
        this.context = context;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_of_user, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.tvTitle.setText(post.getContent());
        holder.tvDate.setText(String.valueOf(post.getPostId()));

        holder.deleteButton.setOnClickListener(view -> {
            showDeleteConfirmationDialog(post.getPostId(), position);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDate;
        ImageButton deleteButton;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private void showDeleteConfirmationDialog(int postId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Post");
        builder.setMessage("Bạn muốn xoá bài viết này không?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Implement your delete logic here
                boolean deleted = databaseHelper.deletePost(postId);
                if (deleted) {
                    postList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Xoá bài viết thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Xoá bài viết thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Huỷ", null);

        // Show the confirmation dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public interface DeleteClickListener {
        void onDeleteClick(Post post);
    }

    public int getPositionForPost(Post post) {
        return postList.indexOf(post);
    }

    public void removePostAtPosition(int position) {
        postList.remove(position);
        notifyItemRemoved(position);
    }
}
