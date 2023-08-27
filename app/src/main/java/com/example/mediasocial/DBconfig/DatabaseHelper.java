package com.example.mediasocial.DBconfig;

import static com.example.mediasocial.Models.Role.getCurrentDateTime;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.icu.text.SimpleDateFormat;

import com.example.mediasocial.Models.Comment;
import com.example.mediasocial.Models.Post;
import com.example.mediasocial.Models.Profile;
import com.example.mediasocial.Models.User;

import org.mindrot.jbcrypt.BCrypt;

import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {
    //Khai bao tag
    private static String TAG = "DataBaseHelper";
    //Khai bao ten va version db.
    public static final String DB_NAME = "socialmedia.db";
    public static final int DB_VERSION = 1;
    private String databasePath;
    private String TABLE_NAME = "roles";
    private SQLiteDatabase mDefaultWritableDatabase;
    //Khai báo biến context
//    private final Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
//        this.mContext = context;
//        this.databasePath = context.getDatabasePath(DB_NAME).getPath();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        final SQLiteDatabase db;
        if(mDefaultWritableDatabase != null){
            db = mDefaultWritableDatabase;
        }else{
            db = super.getWritableDatabase();
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.mDefaultWritableDatabase = sqLiteDatabase;

        String sql8 = "CREATE TABLE roles (" +
                "role_id integer PRIMARY KEY NOT NULL," +
                "role_name text NOT NULL,"+
                "created_at DATETIME DEFAULT NULL,"
                + "updated_at DATETIME DEFAULT NULL,"
                + "deleted_at DATETIME DEFAULT NULL)";
        sqLiteDatabase.execSQL(sql8);

        String sql7 = "CREATE TABLE users (" +
                "user_id integer PRIMARY KEY NOT NULL," +
                "email text UNIQUE NOT NULL," +
                "phone text NOT NULL UNIQUE," +
                "name text NOT NULL UNIQUE," +
                "password text NOT NULL," +
                "created_at DATETIME DEFAULT NULL,"
                + "updated_at DATETIME DEFAULT NULL,"
                + "deleted_at DATETIME DEFAULT NULL," +
                "role_id integer NOT NULL," +
                "FOREIGN KEY (role_id) REFERENCES roles ( role_id))";
        sqLiteDatabase.execSQL(sql7);
        //khai bao bang bai dang
        String sql1 = "CREATE TABLE posts ("
                + "post_id integer PRIMARY KEY NOT NULL,"
                + "content text NOT NUll,"
                + "thumbnail_image text DEFAULT NULL,"
                + "created_at DATETIME DEFAULT NULL,"
                + "updated_at DATETIME DEFAULT NULL,"
                + "deleted_at DATETIME DEFAULT NULL,"
                + "user_id integer not null,"
                + "FOREIGN KEY (user_id) REFERENCES users (user_id))";
        sqLiteDatabase.execSQL(sql1);
        //Khai bao bang comments
        String sql2 = "CREATE TABLE comments ("
                + "comment_id integer PRIMARY KEY NOT NULL,"
                + "content text NOT NUll,"
                + "created_at DATETIME DEFAULT NULL,"
                + "updated_at DATETIME DEFAULT NULL,"
                + "deleted_at DATETIME DEFAULT NULL,"
                + "post_id integer not null,"
                + "FOREIGN KEY (post_id) REFERENCES posts (post_id))";
        sqLiteDatabase.execSQL(sql2);

        String sql3 = "CREATE TABLE comment_of_user ("
                + "id integer PRIMARY KEY NOT NULL,"
                + "post_id integer not null,"
                + "user_id integer not null,"
                + "FOREIGN KEY (user_id) REFERENCES users (user_id),"
                + "FOREIGN KEY (post_id) REFERENCES posts (post_id))";
        sqLiteDatabase.execSQL(sql3);

        String sql4 = "CREATE TABLE  likes ("
                + "id integer PRIMARY KEY NOT NULL,"
                + "post_id integer not null,"
                + "user_id integer not null," +
                "created_time DATETIME," +
                "updated_time DATETIME,"
                + "FOREIGN KEY (user_id) REFERENCES users (user_id),"
                + "FOREIGN KEY (post_id) REFERENCES posts (post_id))";
        sqLiteDatabase.execSQL(sql4);

        String sql5 = "CREATE TABLE post_of_user ("
                + "id integer PRIMARY KEY NOT NULL,"
                + "post_id integer not null,"
                + "user_id integer not null,"
                + "FOREIGN KEY (user_id) REFERENCES users (user_id),"
                + "FOREIGN KEY (post_id) REFERENCES posts (post_id))";
        sqLiteDatabase.execSQL(sql5);

        String sql6 = "CREATE TABLE profiles(" +
                "profile_id integer NOT NULL PRIMARY KEY," +
                "last_name text DEFAULT NULL," +
                "first_name text DEFAULT NULL," +
                "user_name text DEFAULT NULL UNIQUE," +
                "image_lib text DEFAULT NULL," +
                "avatar text DEFAULT NULL," +
                "birthday DATETIME DEFAULT NULL," +
                "created_at DATETIME DEFAULT NULL," +
                "updated_at DATETIME DEFAULT NULL," +
                "deleted_at DATETIME DEFAULT NULL," +
                "user_id integer NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users (user_id))";
        sqLiteDatabase.execSQL(sql6);

        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db,int i, int i1){
        this.mDefaultWritableDatabase = db;
        db.execSQL("DROP TABLE IF EXISTS roles");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS posts");
        db.execSQL("DROP TABLE IF EXISTS likes");
        db.execSQL("DROP TABLE IF EXISTS comments");
        db.execSQL("DROP TABLE IF EXISTS comment_of_user");
        db.execSQL("DROP TABLE IF EXISTS post_of_user");
        db.execSQL("DROP TABLE IF EXISTS profiles");

    }
    @Override
    public synchronized void close(){
        if(mDefaultWritableDatabase!=null){
            mDefaultWritableDatabase.close();
        }
        super.close();
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    //Kiem tra neu chua co db thi tao moi con co roi thi copy db vao asset.
    public void createDatabase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
//            copyDataBase();
        }
    }
    //Kiem tra thu db co ton tai chua
    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY);
            Log.e(TAG, "Database is exist.");
        } catch (Exception e) {
            Log.e(TAG, "Database does not exist.");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }


    //Lấy roleID
    @SuppressLint("Range")
    public int getUserRoleId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int roleId = -1;
        String query = "SELECT role_id FROM users WHERE user_id = " + userId;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            roleId = cursor.getInt(cursor.getColumnIndex("role_id"));
            cursor.close();
        }
        return roleId;
    }

    public long insertUserAdmin(String email, String phone, String name, String password, int roleId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("phone", phone);
        values.put("name", name);

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        values.put("password", hashedPassword);
        values.put("role_id", roleId);
        long userId = db.insert("users", null, values);

        db.close();

        return userId;
    }

    //Xử lý insert Data
    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"user_id", "password"};
        String selection = "email = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
        boolean loggedIn = false;

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String hashedPassword = cursor.getString(cursor.getColumnIndex("password"));
            if (BCrypt.checkpw(password, hashedPassword)) {
                loggedIn = true;
            }
        }
        cursor.close();
        db.close();

        return loggedIn;
    }

    // Hàm băm mật khẩu
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public Boolean insertUser(String email, String name, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("name", name);
        values.put("phone", phone);
        String hashedPassword = hashPassword(password);
        values.put("password", hashedPassword);
        values.put("role_id", 2);
        String currentTime = getCurrentDateTime();
        values.put("created_at", currentTime);

        long userId = db.insert("users", null, values);
        db.close();
        if (userId == -1)
            return false;
        else {
            return true;
        }
    }
    //Check Email
    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", null, "email=?", new String[]{email}, null, null, null);
        boolean emailExists = cursor.moveToFirst();
        cursor.close();
        return emailExists;
    }

    //Check UserName
    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", null, "name=?", new String[]{username}, null, null, null);
        boolean usernameExists = cursor.moveToFirst();
        cursor.close();
        return usernameExists;
    }
    @SuppressLint("Range")
    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"user_id"};
        String selection = "email = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);

        int userId = -1;

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("user_id"));
        }

        cursor.close();
        db.close();

        return userId;
    }


    //Lấy username
    @SuppressLint("Range")
    public String getUserName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"name"};
        String selection = "user_id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);

        String userName = null;

        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex("name"));
        }

        cursor.close();
        db.close();

        return userName;
    }


    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"user_id", "email", "phone", "name", "password", "role_id", "created_at"};

        Cursor cursor = db.query("users", projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            int roleId = cursor.getInt(cursor.getColumnIndexOrThrow("role_id"));
            long createdAtMillis = cursor.getLong(cursor.getColumnIndexOrThrow("created_at"));

            Date createdAt = new Date(createdAtMillis);

            User user = new User(id, email, phone, name, password, createdAt, null, null, roleId);
            userList.add(user);
        }

        cursor.close();
        db.close();

        return userList;
    }


    //Insert Data Profiles

    //Lấy ra dữ liệu render
    public boolean insertProfile(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


        values.put("user_id", userId);
        values.put("first_name", ""); // Leave empty for now
        values.put("last_name", "");  // Leave empty for now
        values.put("user_name", "profile" + userId);  // Leave empty for now
        String formattedCurrentDate = sdf.format(new Date());
        values.put("birthday", formattedCurrentDate);
        values.put("created_at", getCurrentDateTime());
//        values.put("avatar", "");

        long profileId = db.insert("profiles", null, values);
        db.close();

        return profileId != -1;
    }




    // Lấy dữ liệu render
    @SuppressLint("Range")
    public Profile getProfile(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                "profile_id",
                "last_name",
                "first_name",
                "user_name",
                "image_lib",
                "avatar",
                "birthday",
                "created_at",
                "updated_at",
                "user_id"
        };
        String selection = "user_id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query("profiles", columns, selection, selectionArgs, null, null, null);

        Profile profile = null;

        if (cursor.moveToFirst()) {
            int profileId = cursor.getInt(cursor.getColumnIndex("profile_id"));
            String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
            String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
            String userName = cursor.getString(cursor.getColumnIndex("user_name"));
            String imageLib = cursor.getString(cursor.getColumnIndex("image_lib"));
            String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
            String birthdayStr = cursor.getString(cursor.getColumnIndex("birthday"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date birthday = null;
            try {
                birthday = sdf.parse(birthdayStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            long birthdayStr = cursor.getLong(cursor.getColumnIndex("birthday"));
            long createdAtMillis = cursor.getLong(cursor.getColumnIndex("created_at"));
            long updatedAtMillis = cursor.getLong(cursor.getColumnIndex("updated_at"));

            profile = new Profile(profileId, lastName, firstName, userName, imageLib, avatar, birthday, new Date(createdAtMillis), new Date(updatedAtMillis), null, userId);
        }
//        else{
//            Log.e(TAG, "No profile found for userId: " + userId);
//        }

        cursor.close();
        db.close();

        return profile;
    }

    //Check profiles
    public boolean isProfileExists(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"profile_id"};
        String selection = "user_id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query("profiles", columns, selection, selectionArgs, null, null, null);
        boolean profileExists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return profileExists;
    }

//    public boolean updateProfile(int userId, String newUsername, String newFirstName,String newLastName, Date newBirthday) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put("user_name", newUsername);
//        values.put("first_name", newFirstName);
//        values.put("last_name", newLastName);
////        values.put("avatar", newAvatarPath);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//        String formattedBirthday = sdf.format(newBirthday);
//        values.put("birthday", formattedBirthday);
//
//        String whereClause = "user_id = ?";
//        String[] whereArgs = {String.valueOf(userId)};
//
//        int rowsAffected = db.update("profiles", values, whereClause, whereArgs);
//        db.close();
//
//        return rowsAffected > 0;
//    }
public boolean updateProfileWithAvatar(int userId, String newUsername, String newFirstName, String newLastName, Date newBirthday, String newAvatarPath) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put("user_name", newUsername);
    values.put("first_name", newFirstName);
    values.put("last_name", newLastName);

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    String formattedBirthday = sdf.format(newBirthday);
    values.put("birthday", formattedBirthday);

    values.put("avatar", newAvatarPath); // Cập nhật đường dẫn avatar mới

    String whereClause = "user_id = ?";
    String[] whereArgs = {String.valueOf(userId)};

    int rowsAffected = db.update("profiles", values, whereClause, whereArgs);
    db.close();

    return rowsAffected > 0;
}


//    public boolean updateAvatar(int userId, String newAvatarPath) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("avatar", newAvatarPath);
//
//        String whereClause = "user_id = ?";
//        String[] whereArgs = {String.valueOf(userId)};
//
//        int rowsAffected = db.update("profiles", values, whereClause, whereArgs);
//        db.close();
//
//        return rowsAffected > 0;
//    }

    @SuppressLint("Range")
    public String getAvatar(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"avatar"};
        String selection = "user_id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query("profiles", columns, selection, selectionArgs, null, null, null);

        String avatarPath = null;

        if (cursor.moveToFirst()) {
            avatarPath = cursor.getString(cursor.getColumnIndex("avatar"));
        }

        cursor.close();
        db.close();

        return avatarPath;
    }

    //Post

    public boolean insertPosting(int userId, String content, String imageurl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("content", content);
        values.put("thumbnail_image", imageurl);// Leave empty for now
        values.put("created_at", getCurrentDateTime());

        long post_id = db.insert("posts", null, values);
        db.close();
        if (post_id == -1)
            return false;
        else {
            return true;
        }
    }


    @SuppressLint("Range")
    public List<Post> getAllPosts() {
        List<Post> postList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM posts";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int postId = cursor.getInt(cursor.getColumnIndex("post_id"));
                int userId = cursor.getInt(cursor.getColumnIndex("user_id"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String imageUrl = cursor.getString(cursor.getColumnIndex("thumbnail_image"));
                String createdAtMillis = cursor.getString(cursor.getColumnIndex("created_at"));
//                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                Date createdAt = convertStringToDate(createdAtMillis);
                Post post = new Post(postId,content,imageUrl, createdAt , null,null,userId);
                postList.add(post);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return postList;
    }
    @SuppressLint("Range")
    public String getUserNameFromPost(int postUserID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String userName = null;

        String query = "SELECT users.name FROM posts INNER JOIN users ON posts.user_id = users.user_id WHERE posts.user_id = ?";
        String[] selectionArgs = {String.valueOf(postUserID)};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex("name"));
            cursor.close();
        }

        db.close();
        return userName;
    }


    public int getLikes(int postId){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"like_id"};
        String selection = "post_id = ?";
        String[] selectionArgs = {String.valueOf(postId)};

        Cursor cursor = db.query("likes", columns, selection, selectionArgs, null, null, null);
        int count =0;
        if (cursor.moveToFirst()) {
            count++;
//            = cursor.getString(cursor.getColumnIndex("avatar"));
        }

        cursor.close();
        db.close();

        return count;
    }

    public boolean deletePost(int postId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("posts", "post_id = ?", new String[]{String.valueOf(postId)});
        return result > 0;
    }


//    public List<String> getUserLikePost(int postId){
//        SQLiteDatabase db = this.getReadableDatabase();
//        String[] columns = {"like_id"};
//        String selection = "post_id = ?";
//        String[] selectionArgs = {String.valueOf(postId)};
//
//        Cursor cursor = db.query("likes", columns, selection, selectionArgs, null, null, null);
//        int count =0;
//        if (cursor.moveToFirst()) {
//            count++;
////            = cursor.getString(cursor.getColumnIndex("avatar"));
//        }
//
//        cursor.close();
//        db.close();
//
//        return count;
//    }



public Date convertStringToDate(String s){
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    try {
        return dateFormat.parse(s);
    } catch (ParseException e) {
        e.printStackTrace();
        return null;
    }
}
    public boolean insertComment( String content, int postId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("post_id", postId);
        values.put("content", content);
        values.put("created_at", getCurrentDateTime());

        long comment_id = db.insert("comments", null, values);
        db.close();
        if (comment_id == -1)
            return false;
        else {
            return true;
        }
    }
    public List<Post> getAllPostsByUserId(int userId) {
        List<Post> postList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"post_id", "content", "thumbnail_image", "created_at"};

        String selection = "user_id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query("posts", projection, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            int postId = cursor.getInt(cursor.getColumnIndexOrThrow("post_id"));
            String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
            String thumbnailImage = cursor.getString(cursor.getColumnIndexOrThrow("thumbnail_image"));
            long createdAtMillis = cursor.getLong(cursor.getColumnIndexOrThrow("created_at"));

            Date createdAt = new Date(createdAtMillis);

            Post post = new Post(postId, content, thumbnailImage, createdAt, null, null, userId);
            postList.add(post);
        }

        cursor.close();
        db.close();

        return postList;
    }


    public boolean updateUserRoleWithRoleId(int roleId, int userId) {
        if (roleId != 1 && roleId != 2) {
            Log.e(TAG, "Invalid role ID. Only role IDs 1 (admin) and 2 (user) are allowed.");
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("role_id", roleId);

        int rowsAffected = db.update("users", values, "user_id = ?",
                new String[]{String.valueOf(userId)});

        return rowsAffected > 0;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();

            // Xoá các bài đăng của người dùng
            db.delete("posts", "user_id = ?", new String[]{String.valueOf(userId)});

            // Xoá các bình luận của người dùng
            db.delete("comments", "post_id IN (SELECT post_id FROM posts WHERE user_id = ?)", new String[]{String.valueOf(userId)});

            // Xoá các lượt thích của người dùng
            db.delete("likes", "post_id IN (SELECT post_id FROM posts WHERE user_id = ?)", new String[]{String.valueOf(userId)});

            // Xoá thông tin liên quan đến bình luận của người dùng
            db.delete("comment_of_user", "user_id = ?", new String[]{String.valueOf(userId)});

            // Xoá thông tin liên quan đến bài đăng của người dùng
            db.delete("post_of_user", "user_id = ?", new String[]{String.valueOf(userId)});

            // Xoá thông tin liên quan đến hồ sơ cá nhân của người dùng
            db.delete("profiles", "user_id = ?", new String[]{String.valueOf(userId)});

            // Xoá người dùng
            db.delete("users", "user_id = ?", new String[]{String.valueOf(userId)});

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    @SuppressLint("Range")
    public List<Comment> getCommentOfPost(int postId) {
        List<Comment> commentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                "comment_id",
                "content",
                "created_at",
                "updated_at",
                "post_id"
        };
        String query = "SELECT * FROM comment";
        String selection = "post_id = ?";
        String[] selectionArgs = {String.valueOf(postId)};

        Cursor cursor = db.query("comments",columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int comment_id = cursor.getInt(cursor.getColumnIndex("comment_id"));
                int post_id = cursor.getInt(cursor.getColumnIndex("post_id"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String createdAtMillis = cursor.getString(cursor.getColumnIndex("created_at"));
//                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                Date createdAt = convertStringToDate(createdAtMillis);
                Comment comment = new Comment(comment_id, content, createdAt , null,null,post_id);
                commentList.add(comment);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Collections.sort(commentList, new Comparator<Comment>() {
            @Override
            public int compare(Comment c1, Comment c2) {
                return c2.getCreatedAt().compareTo(c1.getCreatedAt());
            }
        });
        return commentList;
    }

    @SuppressLint("Range")
    public String getUserNameFromComment(int postUserID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String userName = null;

        String query = "SELECT users.name FROM posts INNER JOIN users ON posts.user_id = users.user_id INNER JOIN comments ON comments.post_id = posts.post_id WHERE posts.user_id = ?";
        String[] selectionArgs = {String.valueOf(postUserID)};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex("name"));
            cursor.close();
        }

        db.close();
        return userName;
    }

}




