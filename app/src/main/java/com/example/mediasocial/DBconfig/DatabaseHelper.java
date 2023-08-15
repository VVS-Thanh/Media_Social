package com.example.mediasocial.DBconfig;

import static com.example.mediasocial.Models.Role.getCurrentDateTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mediasocial.Models.Like;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

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
                "name text NOT NULL," +
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
                + "topic text NOT NULL,"
                + "thumbnail_image text NOT NULL,"
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
                "last_name text NOT NULL," +
                "first_name text NOT NULL," +
                "user_name text NOT NULL UNIQUE," +
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



    //Xử lý insert Data

    public Boolean insertUser(String email, String phone, String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("phone", phone);
        values.put("name", name);
        values.put("password", password);
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
    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", null, "email=?", new String[]{email}, null, null, null);
        boolean emailExists = cursor.moveToFirst();
        cursor.close();
        return emailExists;
    }

    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", null, "name=?", new String[]{username}, null, null, null);
        boolean usernameExists = cursor.moveToFirst();
        cursor.close();
        return usernameExists;
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
