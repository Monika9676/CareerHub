package com.example.careerhub30;

import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Index extends AppCompatActivity {

    private Button allJobsButton;
    private Button savedJobsButton;
    private TextView textViewWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        textViewWelcome = findViewById(R.id.textViewExplore);
        String username = getIntent().getStringExtra("USERNAME");
        String password = getIntent().getStringExtra("PASSWORD");
        textViewWelcome.setText("Welcome, " + username + "!");

        // Find buttons by their IDs
        allJobsButton = findViewById(R.id.AllJobs);
        savedJobsButton = findViewById(R.id.SavedJobs);
        Button addJobButton = findViewById(R.id.addJobButton);
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

        // Set OnClickListener for Add Job button
         // Replace with actual view ID

        //condition for admin

        // Set OnClickListener for Add Job button
        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.equals("monika") && password.equals("monika")) {
                    // Start the JobFormActivity for admins
                    Intent intent = new Intent(getApplicationContext(), JobFormActivity.class);
                    startActivity(intent);
                } else {
                    // Show a message for non-admin users
                    Toast.makeText(Index.this, "Only admins can add jobs.", Toast.LENGTH_SHORT).show();
                }
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
