package com.example.careerhub30;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "CareerHubSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String LOGIN_TIME = "loginTime";
    private static final long FOUR_DAYS_IN_MILLIS = 4 * 24 * 60 * 60 * 1000L; // 4 days in milliseconds
    private static final String IS_ADMIN = "isAdmin";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Create a login session with current timestamp
    public void createLoginSession(String username, String email, boolean isAdmin) {
        long currentTimeMillis = System.currentTimeMillis();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putLong(LOGIN_TIME, currentTimeMillis);
        editor.putBoolean(IS_ADMIN, isAdmin);
        editor.commit();
    }

    // Check if the session is still valid (not expired)
    public boolean isSessionValid() {
        if (!isLoggedIn()) {
            return false;
        }
        long loginTime = sharedPreferences.getLong(LOGIN_TIME, 0);
        long currentTimeMillis = System.currentTimeMillis();
        return (currentTimeMillis - loginTime) < FOUR_DAYS_IN_MILLIS;
    }

    // Get the logged-in user's username
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    // Get the logged-in user's email
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    // Check if the user is logged in
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }
    public boolean isAdmin() {
        return sharedPreferences.getBoolean(IS_ADMIN, false);
    }
    // Log out the user
    public void logout() {
        editor.clear();
        editor.commit();
    }
}
