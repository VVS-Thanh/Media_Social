package com.example.mediasocial.Models;
import java.util.ArrayList;
import java.util.List;
public class PostService {

        private List<Post> posts;

        public PostService() {
            this.posts = new ArrayList<>();
        }

        public void addPost(Post post) {
            posts.add(post);
        }

        public List<Post> getUserPosts(int userId) {
            List<Post> userPosts = new ArrayList<>();
            for (Post post : posts) {
                if (post.getUserId() == userId) {
                    userPosts.add(post);
                }
            }
            return userPosts;
        }
}
