package com.example.careerhub30;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "JobPosts.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the job_posts table
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS job_posts (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "title TEXT," +
                        "description TEXT)"
        );

        // Create the saved_jobs table
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS saved_jobs (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "title TEXT," +
                        "description TEXT)"
        );

        // Create the users table
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT," +
                        "password TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS job_posts");
        db.execSQL("DROP TABLE IF EXISTS saved_jobs");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
