package com.example.mediasocial.DBconfig;

import static com.example.mediasocial.Models.Role.getCurrentDateTime;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import androidx.annotation.FloatRange;

import org.mindrot.jbcrypt.BCrypt;

import com.example.mediasocial.Models.Profile;

import java.text.ParseException;
import java.util.Date;
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
public boolean updateProfile(int userId, String newUsername, String newFirstName, String newLastName, Date newBirthday, String newAvatarPath) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put("user_name", newUsername);
    values.put("first_name", newFirstName);
    values.put("last_name", newLastName);
    values.put("avatar", newAvatarPath); // Truyền đường dẫn ảnh mới

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    String formattedBirthday = sdf.format(newBirthday);
    values.put("birthday", formattedBirthday);

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























    // Copy the database from assets
//    public void copyDataBase () {
//        try {
//            InputStream inputStream = mContext.getAssets().open(DB_NAME);
//            OutputStream outputStream = new FileOutputStream(databasePath);
//
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//
//            outputStream.flush();
//            outputStream.close();
//            inputStream.close();
//        } catch (IOException e) {
//            Log.e(TAG, "Error copying database: " + e.getMessage());
//        }
//    }
    // kiem tra ket noi thanh cong hay chua
//    public void checkConnection(){
//        try {
//            // Open the database
//            SQLiteDatabase db = getWritableDatabase();
//
//            // Check if the database is open
//            if (db != null && db.isOpen()) {
//                Toast.makeText(mContext, "Database connection successful", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(mContext, "Database connection failed", Toast.LENGTH_SHORT).show();
//            }
//
//            // Close the database
//            db.close();
//        } catch (SQLiteException e) {
//            // Handle any exceptions that occurred during database opening
//            Toast.makeText(mContext, "Database connection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

//    public Cursor getdata(){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        Cursor c = sqLiteDatabase.rawQuery("select * from "+ TABLE_NAME, null);
//        return c;
//    }
//    public boolean isTableExists(String tableName) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table' AND name='" + TABLE_NAME + "'", null);
//        boolean tableExists = cursor.moveToFirst();
//        cursor.close();
//        Log.e(TAG, String.valueOf(tableExists));
//        return tableExists;
//
//    }


}
