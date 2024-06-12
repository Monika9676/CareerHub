package com.example.careerhub30;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
        checkSavedJobs();
    }

    private void checkSavedJobs() {
        for (JobPost jobPost : jobPosts) {
            Cursor cursor = null;
            try {
                cursor = database.rawQuery("SELECT COUNT(*) FROM saved_jobs WHERE title = ? AND description = ?",
                        new String[]{jobPost.getTitle(), jobPost.getDescription()});
                if (cursor != null && cursor.moveToFirst()) {
                    int count = cursor.getInt(0);
                    jobPost.setSaved(count > 0);
                }
            } catch (Exception e) {
                Log.e("JobPostAdapter", "Error checking if job is saved", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_job_post, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = convertView.findViewById(R.id.titleTextView);
            holder.companyTextView = convertView.findViewById(R.id.CompanyTextView);
            holder.locationTextView = convertView.findViewById(R.id.locationTextView);
            holder.applyButton = convertView.findViewById(R.id.apply);
            holder.sendIcon = convertView.findViewById(R.id.sendIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JobPost jobPost = jobPosts.get(position);
        holder.titleTextView.setText(jobPost.getTitle());
        holder.locationTextView.setText(jobPost.getLocation());

        SpannableString content = new SpannableString(jobPost.getCompany());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.companyTextView.setText(content);
        holder.companyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = jobPost.getLink();
                if (url != null && !url.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(browserIntent);
                } else {
                    Log.e("JobPostAdapter", "No URL provided for this job post");
                }
            }
        });

        holder.applyButton.setImageResource(jobPost.isSaved() ? R.drawable.ic_fill_save : R.drawable.ic_outline_save);
        holder.applyButton.setEnabled(!jobPost.isSaved());

        holder.applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!jobPost.isSaved() && jobSaveListener != null) {
                    jobSaveListener.onJobSave(jobPost);
                    jobPost.setSaved(true);
                    notifyDataSetChanged();
                }
            }
        });

        holder.sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private static class ViewHolder {
        TextView titleTextView;
        TextView companyTextView;
        TextView locationTextView;
        ImageButton applyButton;
        ImageView sendIcon;
    }

    public interface JobSaveListener {
        void onJobSave(JobPost jobPost);
    }
}
