package com.example.careerhub30;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class JobPostAdapter extends BaseAdapter {

    private Context context;
    private List<JobPost> jobPosts;
    private JobSaveListener jobSaveListener;
    private SQLiteDatabase database;
    public JobPostAdapter(Context context, List<JobPost> jobPosts, JobSaveListener jobSaveListener, SQLiteDatabase database) {
        this.context = context;
        this.jobPosts = jobPosts;
        this.jobSaveListener = jobSaveListener;
        this.database = database;
        for (JobPost jobPost : jobPosts) {
            Cursor cursor = this.database.rawQuery("SELECT COUNT(*) FROM saved_jobs WHERE title = ? AND description = ?",
                    new String[]{jobPost.getTitle(), jobPost.getDescription()});
            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                cursor.close();
                jobPost.setSaved(count > 0);
            } else {
                Log.e("JobPostAdapter", "Error checking if job is saved");
            }
        }
    }
    @Override
    public int getCount() {
        return jobPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return jobPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_job_post, parent, false);
        }

        JobPost jobPost = jobPosts.get(position);
        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        ImageButton applyButton = convertView.findViewById(R.id.apply);

        titleTextView.setText(jobPost.getTitle());
        descriptionTextView.setText(jobPost.getDescription());
        if (jobPost.isSaved()) {
            applyButton.setImageResource(R.drawable.ic_fill_save);
            applyButton.setEnabled(false);
        } else {
            applyButton.setImageResource(R.drawable.ic_outline_save);
            applyButton.setEnabled(true); // Enable button if not saved
        }

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save job post to the database
                if (!jobPost.isSaved() && jobSaveListener != null) {
                    jobSaveListener.onJobSave(jobPost);
                    jobPost.setSaved(true); // Update saved state
                    applyButton.setImageResource(R.drawable.ic_fill_save); // Update icon
                    applyButton.setEnabled(false); // Disable button
                }
            }
        });

        return convertView;
    }

      public interface JobSaveListener {
        void onJobSave(JobPost jobPost);
    }
}
