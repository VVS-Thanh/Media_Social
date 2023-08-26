package com.example.mediasocial;

import static com.example.mediasocial.Models.Role.getCurrentDateTime;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mediasocial.DBconfig.DatabaseHelper;
import com.example.mediasocial.Models.Comment;
import com.example.mediasocial.Models.User;

import java.util.ArrayList;
import java.util.List;

public class ViewCommentFragment extends Fragment {
    private ImageView mBackArrow, mCheckMark;
    private EditText mComment;
    private ListView mListView;
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USERID = "userId";
    private SharedPreferences sharedPreferences;

    private int userId;
    private DatabaseHelper db;
    //vars
    private ArrayList<Comment> mComments;
    private Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_comments, container, false);
        mBackArrow = (ImageView) view.findViewById(R.id.backArrow);
        mCheckMark = (ImageView) view.findViewById(R.id.ivPostComment);
        mComment = (EditText) view.findViewById(R.id.comment);
        mListView = (ListView) view.findViewById(R.id.listView);
        mComments = new ArrayList<>();
        mContext = getActivity();
        db = new DatabaseHelper(mContext);
        mCheckMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = mComment.getText().toString().trim();
                if (!commentText.isEmpty()) {
                    // Perform comment submission action
                    addComment(commentText);
                }
            }
        });
        return view;
    }

    private String getCommentText() {
        EditText commentEditText = getView().findViewById(R.id.comment);
        return commentEditText.getText().toString().trim();
    }
    private void addComment(String commentText) {
        // Get the post ID or other relevant information passed to the fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            String postId = bundle.getString("post_id");
            Log.d("post", postId);
            Toast.makeText(getActivity(), "Comment added: " + commentText, Toast.LENGTH_SHORT).show();
        }
    }

}
