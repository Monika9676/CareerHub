package com.example.careerhub30;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Index extends AppCompatActivity {

    private Button allJobsButton;
    private Button savedJobsButton;
    private TextView textViewWelcome;
    private SessionManager sessionManager;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        sessionManager = new SessionManager(this);
        textViewWelcome = findViewById(R.id.textViewExplore);

        String username = sessionManager.getUsername();
        boolean isAdmin = sessionManager.isAdmin(); // Check if user is admin
        textViewWelcome.setText("Welcome, " + username + "!");

        // Find buttons by their IDs
        allJobsButton = findViewById(R.id.AllJobs);
        savedJobsButton = findViewById(R.id.SavedJobs);
        Button addJobButton = findViewById(R.id.addJobButton);
        logoutButton = findViewById(R.id.logoutButton);
        // Initial fragment load for All Jobs
        loadFragment(new AllJobs());
        allJobsButton.setBackgroundColor(ContextCompat.getColor(this, R.color.light_purple));

        // Set OnClickListener for "All Jobs" button
        allJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Index", "All Jobs button clicked");
                loadFragment(new AllJobs());
                allJobsButton.setBackgroundColor(ContextCompat.getColor(Index.this, R.color.light_purple));
                savedJobsButton.setBackgroundColor(ContextCompat.getColor(Index.this, R.color.dark_purple)); // Assuming a default color
            }
        });

        // Set OnClickListener for "Saved Jobs" button
        savedJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Index", "Saved Jobs button clicked");
                loadFragment(new SavedJobs());
                savedJobsButton.setBackgroundColor(ContextCompat.getColor(Index.this, R.color.light_purple));
                allJobsButton.setBackgroundColor(ContextCompat.getColor(Index.this, R.color.dark_purple)); // Assuming a default color
            }
        });

        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdmin) { // Check if user is admin
                    // Start the JobFormActivity for admins
                    Intent intent = new Intent(getApplicationContext(), JobFormActivity.class);
                    startActivity(intent);
                } else {
                    // Show a message for non-admin users
                    Toast.makeText(Index.this, "Only admins can add jobs.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout(); // Clear session data
                Intent intent = new Intent(Index.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the Index activity
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.jobListView, fragment);
        transaction.addToBackStack(null);  // Optional: Add to back stack to allow back navigation
        transaction.commit();
    }
}
