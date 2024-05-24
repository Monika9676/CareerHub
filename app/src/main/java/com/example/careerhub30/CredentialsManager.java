package com.example.careerhub30;

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

    public boolean checkCredentials(String username, String password) {
        Cursor cursor = database.query("users", null, "username = ? AND password = ?", new String[]{username, password}, null, null, null);
        boolean valid = cursor.moveToFirst();
        cursor.close();
        return valid;
    }

    public void registerUser(String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        database.insert("users", null, values);
    }
}
