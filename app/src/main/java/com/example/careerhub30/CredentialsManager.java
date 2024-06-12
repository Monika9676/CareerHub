package com.example.careerhub30;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CredentialsManager {
    private static CredentialsManager instance;
    private SQLiteDatabase database;

    private CredentialsManager(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public static CredentialsManager getInstance(Context context) {
        if (instance == null) {
            instance = new CredentialsManager(context);
        }
        return instance;
    }

    public boolean containsUsername(String username) {
        Cursor cursor = database.query("users", null, "username = ?", new String[]{username}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean containsEmail(String email) {
        Cursor cursor = database.query("users", null, "email = ?", new String[]{email}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean checkCredentials(String username, String password) {
        Cursor cursor = database.query("users", null, "username = ? AND password = ?", new String[]{username, password}, null, null, null);
        boolean valid = cursor.moveToFirst();
        cursor.close();
        return valid;
    }

    public void registerUser(String username, String password,String email) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("email",email);
        database.insert("users", null, values);
    }
    public String getEmailByUsername(String username) {
        Cursor cursor = database.query("users", new String[]{"email"}, "username = ?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
            cursor.close();
            return email;
        }
        return null;
    }
}
