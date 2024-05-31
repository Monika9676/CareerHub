package com.example.careerhub30;
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
import android.content.ContentValues;
public class AllJobs extends Fragment implements JobPostAdapter.JobSaveListener {

    private List<JobPost> jobPosts;
    private JobPostAdapter adapter;
    private SQLiteDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_jobs, container, false);
        DBHelper dbHelper = new DBHelper(requireContext());
        database = dbHelper.getWritableDatabase();

        jobPosts = new ArrayList<>();
        jobPosts.add(new JobPost("Android Developer", "Join our team!", "https://example.com", "Company A", "Location A"));
        jobPosts.add(new JobPost("Web Designer", "Create stunning websites.", "https://example.com", "Company B", "Location B"));
        jobPosts.add(new JobPost("Data Analyst", "Analyze data like a pro.", "https://example.com", "Company C", "Location C"));

        Collections.shuffle(jobPosts);
        retrieveJobPosts();
        ListView jobListView = view.findViewById(R.id.jobList);
        adapter = new JobPostAdapter(requireContext(), jobPosts, this, database);
        jobListView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onJobSave(JobPost jobPost) {
        String query = "SELECT COUNT(*) FROM saved_jobs WHERE title = ? AND description = ?";
        Cursor cursor = database.rawQuery(query, new String[]{jobPost.getTitle(), jobPost.getDescription()});

        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();

            if (count > 0) {
                Toast.makeText(requireContext(), "This job is already saved!", Toast.LENGTH_SHORT).show();
            } else {
                // Job post does not exist, save it
                ContentValues values = new ContentValues();
                values.put("title", jobPost.getTitle());
                values.put("description", jobPost.getDescription());
                values.put("link", jobPost.getLink());
                values.put("company", jobPost.getCompany());
                values.put("location", jobPost.getLocation());
                long result = database.insert("saved_jobs", null, values);
                if (result != -1) {
                    jobPost.setSaved(true);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(requireContext(), "Job saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to save job", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(requireContext(), "Error checking if job is already saved", Toast.LENGTH_SHORT).show();
        }
    }
    private void retrieveJobPosts() {
        Cursor cursor = database.rawQuery("SELECT title, description, link, company, location FROM job_posts", null);
        if (cursor != null && cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex("title");
            int descriptionIndex = cursor.getColumnIndex("description");
            int linkIndex = cursor.getColumnIndex("link");
            int companyIndex = cursor.getColumnIndex("company");
            int locationIndex = cursor.getColumnIndex("location");
            do {
                if (titleIndex != -1 && descriptionIndex != -1 && linkIndex != -1 && companyIndex != -1 && locationIndex != -1) {
                    String title = cursor.getString(titleIndex);
                    String description = cursor.getString(descriptionIndex);
                    String link = cursor.getString(linkIndex);
                    String company = cursor.getString(companyIndex);
                    String location = cursor.getString(locationIndex);
                    jobPosts.add(new JobPost(title, description, link, company, location));
                } else {
                    Log.e("AllJobs", "Invalid column indices");
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
