package com.example.careerhub30;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        textViewWelcome.setText("Welcome, " + username + "!");


        // Find buttons by their IDs
        allJobsButton = findViewById(R.id.AllJobs);
        savedJobsButton = findViewById(R.id.SavedJobs);


        Fragment allJobsFragment = new AllJobs(); // Create an instance of your AllJobsFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.jobListView, allJobsFragment);
        transaction.addToBackStack(null);  // Optional: Add to back stack to allow back navigation
        transaction.commit();
        // Set OnClickListener for "All Jobs" button
        allJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "All Jobs button clicked");
                v.setBackgroundColor(ContextCompat.getColor(Index.this, R.color.light_purple));
                // Replace the current fragment with a new fragment for "All Jobs"
                Fragment allJobsFragment = new AllJobs(); // Create an instance of your AllJobsFragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.jobListView, allJobsFragment);
                transaction.addToBackStack(null);  // Optional: Add to back stack to allow back navigation
                transaction.commit();
            }
        });
        savedJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with a new fragment for "All Jobs"
                Fragment SavedFragment = new SavedJobs(); // Create an instance of your AllJobsFragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.jobListView, SavedFragment);
                transaction.addToBackStack(null);  // Optional: Add to back stack to allow back navigation
                transaction.commit();
            }
        });

        // Set OnClickListener for other buttons (if needed)
        // ...

        Button addJobButton = findViewById(R.id.addJobButton); // Replace with actual view ID
        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the JobFormActivity
                Intent intent = new Intent(getApplicationContext(), JobFormActivity.class);
                startActivity(intent); // Start activity for result
            }
        });
    }


}
