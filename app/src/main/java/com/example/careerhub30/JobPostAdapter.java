package com.example.careerhub30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class JobPostAdapter extends BaseAdapter {

    private Context context;
    private List<JobPost> jobPosts;
    private JobSaveListener jobSaveListener;

    // Constructor
    public JobPostAdapter(Context context, List<JobPost> jobPosts, JobSaveListener jobSaveListener) {
        this.context = context;
        this.jobPosts = jobPosts;
        this.jobSaveListener = jobSaveListener;
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

        // Get the current job post
        JobPost jobPost = jobPosts.get(position);

        // Bind data to UI components
        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        Button applyButton = convertView.findViewById(R.id.apply);

        titleTextView.setText(jobPost.getTitle());
        descriptionTextView.setText(jobPost.getDescription());

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save job post to the database
                if (jobSaveListener != null) {
                    jobSaveListener.onJobSave(jobPost);
                }
            }
        });

        return convertView;
    }

    // JobSaveListener interface declaration
      public interface JobSaveListener {
        void onJobSave(JobPost jobPost);
    }
}
