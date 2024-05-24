package com.example.careerhub30;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class SignUp extends AppCompatActivity {

    private EditText editTextNewUsername, editTextNewPassword;
    private Button buttonRegister;

    private CredentialsManager credentialsManager; // Shared instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        credentialsManager = credentialsManager.getInstance(this); // Initialize shared instance

        // Initialize UI elements
        editTextNewUsername = findViewById(R.id.editTextUsername);
        editTextNewPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonLogin);

        // Set click listener for the register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = editTextNewUsername.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();

                // Register the new user
                if (credentialsManager.containsUsername(newUsername)) {
                    Toast.makeText(SignUp.this, "User '" + newUsername + "' already exists. Please choose a different username.", Toast.LENGTH_SHORT).show();
                } else {
                    credentialsManager.registerUser(newUsername, newPassword);
                    Toast.makeText(SignUp.this, "User '" + newUsername + "' registered successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Return to MainActivity after successful registration
                }
            }
        });
    }
}
