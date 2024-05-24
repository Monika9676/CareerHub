package com.example.careerhub30;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllJobs extends Fragment implements JobPostAdapter.JobSaveListener{

    private List<JobPost> jobPosts;
    private JobPostAdapter adapter;
    private SQLiteDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_jobs, container, false);

        // Initialize the database
        DBHelper dbHelper = new DBHelper(requireContext());
        database = dbHelper.getWritableDatabase();

        // Retrieve job posts from the database
        jobPosts = new ArrayList<>();
        jobPosts.add(new JobPost("Android Developer", "Join our team!"));
        jobPosts.add(new JobPost("Web Designer", "Create stunning websites."));
        jobPosts.add(new JobPost("Data Analyst", "Analyze data like a pro."));
        // Add more job posts as needed

        // Shuffle the list to display random posts

        retrieveJobPosts();
        Collections.shuffle(jobPosts);
        // Initialize ListView and set the adapter
        ListView jobListView = view.findViewById(R.id.jobList);
        adapter = new JobPostAdapter(requireContext(), jobPosts, this);
        jobListView.setAdapter(adapter);

        return view;
    }

    public void onJobSave(JobPost jobPost) {
        // Check if the job post already exists in the saved_jobs table
        String query = "SELECT COUNT(*) FROM saved_jobs WHERE title = ? AND description = ?";
        Cursor cursor = database.rawQuery(query, new String[]{jobPost.getTitle(), jobPost.getDescription()});

        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();

            if (count > 0) {
                // Job post already exists, show a toast message
                Toast.makeText(requireContext(), "This job is already saved!", Toast.LENGTH_SHORT).show();
            } else {
                // Job post does not exist, save it
                database.execSQL("INSERT INTO saved_jobs (title, description) VALUES (?, ?)",
                        new Object[]{jobPost.getTitle(), jobPost.getDescription()});
                Toast.makeText(requireContext(), "Job saved successfully!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle error
            Toast.makeText(requireContext(), "Error checking if job is already saved", Toast.LENGTH_SHORT).show();
        }
    }


    private void retrieveJobPosts() {
        Cursor cursor = database.rawQuery("SELECT title, description FROM job_posts", null);
        if (cursor != null && cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex("title");
            int descriptionIndex = cursor.getColumnIndex("description");
            do {
                // Check if column indices are valid
                if (titleIndex != -1 && descriptionIndex != -1) {
                    String title = cursor.getString(titleIndex);
                    String description = cursor.getString(descriptionIndex);
                    jobPosts.add(new JobPost(title, description));
                } else {
                    // Handle case where column indices are invalid
                    // Log an error or perform appropriate error handling
                    Log.e("AllJobs", "Invalid column indices");
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }


}
