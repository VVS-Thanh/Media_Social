package com.example.mediasocial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Post;
import com.example.mediasocial.Models.User;

import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private DatabaseHelper db;
    private static List<Post> postList;

    public PostAdapter(List<Post> postList, DatabaseHelper dbHelper) {
        this.postList = postList;
        this.db = dbHelper;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        Log.d("postinfotest", post.getPostInfo());
        holder.tvCaption.setText(post.getContent());
//        holder.imagePost.setImageResource();
        int post_of_user = post.getUserId();
        String UserName = db.getUserNameFromPost(post_of_user);

        Log.d("postinfotest"," "+ UserName);
        holder.tvUsername.setText(UserName);

        String imageUrl= post.getThumbnailImage();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .error(R.drawable.user)
                .into(holder.imagePost);
        holder.tvPostDay.setText(countDaysSincePostCreation(post));

//        if (post.isLiked()) {
//            holder.likeImageView.setImageResource(R.drawable.ic_like_filled);
//        } else {
//            holder.likeImageView.setImageResource(R.drawable.ic_like_empty);
//        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername, tvCaption, tvPostDay, viewcomment;
        private ImageView  actionLike, actionComment, imagePost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCaption = itemView.findViewById(R.id.post_caption);
            imagePost = itemView.findViewById(R.id.imagePost);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPostDay = itemView.findViewById(R.id.post_date);
            viewcomment = itemView.findViewById(R.id.image_comments_link);
            actionLike = itemView.findViewById(R.id.action_like);
            actionComment = itemView.findViewById(R.id.action_comment);
            DestinationFragment destinationFragment = DestinationFragment.newInstance();

//            FragmentManager fragmentManager = getSupportFragmentManager(); // For support library
// FragmentManager fragmentManager = getFragmentManager(); // For non-support library

//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(ViewCommentFragment, destinationFragment);
//            transaction.addToBackStack(null); // Optional: Adds the transaction to the back stack
//            transaction.commit();

            actionComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post post = postList.get(position);
                        Fragment fragment = new ViewCommentFragment();
                        Bundle args = new Bundle();
                        args.putInt("post_id", post.getPostId());
                        Log.d("test args", args.toString());
                        fragment.setArguments(args);
                        FragmentManager fragmentManager = ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, fragment);
                        itemView.setVisibility(view.GONE);
                        fragmentTransaction.commit();
                    }
                }
            });
            actionLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post post = postList.get(position);

                    }
                }
            });

            viewcomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post post = postList.get(position);
                        Fragment fragment = new ViewCommentFragment();
                        Bundle args = new Bundle();
                        args.putInt("comment_post_id", post.getPostId());
                        Log.d("test args", args.toString());
                        fragment.setArguments(args);
                        FragmentManager fragmentManager = ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        itemView.setVisibility(view.GONE);
                        fragmentTransaction.replace(R.id.container, fragment);
                        fragmentTransaction.commit();


                    }
                }
            });
        }
    }
    // Calculate the number of days since the post was created
    public String countDaysSincePostCreation( Post post) {
        Date currentDate = new Date();
        Date creationDate = post.getCreatedAt();
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
