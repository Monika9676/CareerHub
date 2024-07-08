package com.example.careerhub30;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonSignup;

    private CredentialsManager credentialsManager;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        credentialsManager = CredentialsManager.getInstance(this);
        sessionManager = new SessionManager(this);

        // Check if the session is valid
        if (sessionManager.isLoggedIn()) {
            if (sessionManager.isSessionValid()) {
                // Redirect to Index if session is still valid
                redirectToIndex();
                return;
            } else {
                // Log out user if session has expired
                sessionManager.logout();
                Toast.makeText(MainActivity.this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            }
        }

        // Initialize UI elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the entered username and password match stored credentials
                if (credentialsManager.checkCredentials(username, password)) {
                    String email = credentialsManager.getEmailByUsername(username);

                    // Determine if the user is admin (replace with your admin logic)
                    boolean isAdmin = "admin".equals(username); // Example admin check

                    // Create a session for the logged-in user
                    sessionManager.createLoginSession(username, email, isAdmin);

                    Toast.makeText(MainActivity.this, "User " + username + " logged in successfully!", Toast.LENGTH_SHORT).show();
                    redirectToIndex();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid username or password. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void redirectToIndex() {
        Intent intent = new Intent(MainActivity.this, Index.class);
        startActivity(intent);
        finish();
    }
}
