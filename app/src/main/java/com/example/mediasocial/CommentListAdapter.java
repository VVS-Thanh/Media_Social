package com.example.mediasocial;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Comment;
import com.example.mediasocial.Models.Post;

import java.util.Date;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentListViewer> {
    private static final String TAG = "CommentListAdapter";
    private DatabaseHelper db;
    private LayoutInflater mInflater;
    private int layoutResource;
    private Context mContext;
    private List<Comment> commentList;
    private int postId;

    public CommentListAdapter(int postId) {
        this.postId = postId;
    }

    public CommentListAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.mContext = context;
    }
    /**
     * Returns a string representing the number of days ago the post was made
     * @return
     */


    @NonNull
    @Override
    public CommentListAdapter.CommentListViewer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment, parent, false);
        return new CommentListViewer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.CommentListViewer holder, int position) {
        Comment comment = commentList.get(position);
////        int post_id = getArguments().getInt("comment_post_id");
//        int id = comment.getPostId();
//        String name = db.getUserNameFromComment(postId);
//        holder.tvUsername.setText(name);
        holder.tvContent.setText(comment.getContent());
        holder.tvPostDay.setText(getTimestampDifference(comment));

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
    public static class CommentListViewer extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvContent, tvPostDay, tvLike;

        public CommentListViewer(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.comment_username);
            tvContent = itemView.findViewById(R.id.comment);
            tvPostDay = itemView.findViewById(R.id.comment_time_posted);
//            tvLike = itemView.findViewById(R.id.comment_like);

        }

    }
    private String getTimestampDifference(Comment comment){
        Date currentDate = new Date();
        Date creationDate = comment.getCreatedAt();
        long durationInMillis = currentDate.getTime() - creationDate.getTime();
        if (durationInMillis < 0) {
            return "Invalid"; // Xử lý trường hợp ngày tạo bài viết sau thời điểm hiện tại
        } else if (durationInMillis >= 24 * 60 * 60 * 1000) {
            long days = durationInMillis / (24 * 60 * 60 * 1000);
            if(days <2){
                return days + " day ago";
            }else {
                return days + " days ago";
            }
        } else if (durationInMillis >= 2 * 60 * 60 * 1000) {
            long hours = durationInMillis / (60 * 60 * 1000);
            if(hours <2){
                return hours + " hour ago";
            }else {
                return hours + " hours ago";
            }
        } else if (durationInMillis >= 60 * 1000) {
            long minutes = durationInMillis / (60 * 1000);
            if(minutes <2){
                return minutes + " minute ago";
            }else {
                return minutes + " minutes ago";
            }
        } else {
            long seconds = durationInMillis / 1000;
            if(seconds <2){
                return seconds + " second ago";
            }else {
                return seconds + " seconds ago";
            }
        }
    }
}