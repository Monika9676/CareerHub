package com.example.careerhub30;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        ImageView sendIcon=convertView.findViewById(R.id.sendIcon);

        titleTextView.setText(jobPost.getTitle());
        descriptionTextView.setText(jobPost.getDescription());
        applyButton.setImageResource(jobPost.isSaved() ? R.drawable.ic_fill_save : R.drawable.ic_outline_save);
        applyButton.setEnabled(!jobPost.isSaved());

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!jobPost.isSaved() && jobSaveListener != null) {
                    jobSaveListener.onJobSave(jobPost);
                    jobPost.setSaved(true);
                    notifyDataSetChanged();
                }
            }
        });
        sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open link in browser
                String url = jobPost.getLink();
                if (url != null && !url.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(browserIntent);
                } else {
                    Log.e("JobPostAdapter", "No URL provided for this job post");
                }
            }
        });
        return convertView;
    }

    public interface JobSaveListener {
        void onJobSave(JobPost jobPost);
    }
}