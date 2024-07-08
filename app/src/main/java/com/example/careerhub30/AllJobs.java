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
        jobPosts.add(new JobPost("UX Designer", "Contribute to setting UX guidelines and drive cross-team alignment on design direction", "https://account.amazon.jobs/en-US/login?job=2651283&relay=%2Fen-US%2Fjobs%2F2651283%2Fapply", "Amazon", "USA"));
        jobPosts.add(new JobPost("Application Developer", "Design, build and configure applications to meet business process and application requirements.", "https://www.accenture.com/in-en/careers/jobdetails?id=ATCI-4241444-S1680585_en&title=Application%20Developer", "accenture", "Gurgaon"));
        jobPosts.add(new JobPost("Database Developer", "We are looking for a highly skilled Software Developer who specialises in the design and implementation of application databases.", "https://www.glassdoor.co.in/Job/gurgaon-database-developer-jobs-SRCH_IL.0,7_IC2921225_KO8,26.htm?jl=1008188980804&srs=JV_APPLYPANE", "BondsIndia", "Gurgaon"));
        jobPosts.add(new JobPost("Network Engineer", "Hands-on experience in networking, routing, and switching technologies.\n" +
                "Good understanding of OSI Model, TCP/IP protocol suite", "https://intapidm.infosysapps.com/auth/realms/careersite/protocol/openid-connect/auth?client_id=careersite&redirect_uri=https%3A%2F%2Fcareer.infosys.com%2Fjobs%2Fprivacy-consent&state=ab9e74b9-ed45-40ab-857f-571e1142081f&response_mode=fragment&response_type=code&scope=openid&nonce=968cfc53-3c10-421e-8e24-6e3dc56e045a", "Infosys Limited", "Hyderabad"));

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
