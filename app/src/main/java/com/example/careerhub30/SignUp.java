package com.example.careerhub30;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class SignUp extends AppCompatActivity {

    private EditText editTextNewUsername, editTextNewPassword, editTextEmail;
    private Button buttonRegister, buttonLogin;

    private CredentialsManager credentialsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        credentialsManager = credentialsManager.getInstance(this);

        editTextNewUsername = findViewById(R.id.editTextUsername);
        editTextNewPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonRegister = findViewById(R.id.buttonSignup);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = editTextNewUsername.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();
                String email = editTextEmail.getText().toString().trim();

                if (newUsername.isEmpty() || newPassword.isEmpty() || email.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please enter username ,email and password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(email)) {
                    Toast.makeText(SignUp.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (credentialsManager.containsEmail(email)) {
                    Toast.makeText(SignUp.this, "Email '" + email + "' is already registered. Please use a different email address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (credentialsManager.containsUsername(newUsername)) {
                    Toast.makeText(SignUp.this, "User '" + newUsername + "' already exists. Please choose a different username.", Toast.LENGTH_SHORT).show();
                } else {
                    sendRegistrationEmail(email);
                    credentialsManager.registerUser(newUsername, newPassword,email);
                    Toast.makeText(SignUp.this, "User '" + newUsername + "' registered successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void sendRegistrationEmail(String userEmail) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{userEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Welcome to CareerHub"); // Set your desired subject
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear user,\n\nThank you for registering with CareerHub. Your account has been successfully created!"); // Customize the email content

        // Start the email activity
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
