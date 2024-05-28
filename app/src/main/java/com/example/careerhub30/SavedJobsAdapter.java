package com.example.careerhub30;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SavedJobsAdapter extends BaseAdapter {

    private Context context;
    private List<SavedJobPost> savedJobPosts;

    public SavedJobsAdapter(Context context, List<SavedJobPost> savedJobPosts) {
        this.context = context;
        this.savedJobPosts= savedJobPosts;
    }

    @Override
    public int getCount() {
        return savedJobPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return savedJobPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_saved_post, parent, false);
            Log.d("SavedJobsAdapter", "getView: Inflating new view for position " + position);
        }
        SavedJobPost savedJobPost = savedJobPosts.get(position);
        Log.d("SavedJobsAdapter", "getView: Binding data for position " + position + ", Title: " + savedJobPost.getTitle() + ", Description: " + savedJobPost.getDescription());

        TextView titleTextView = convertView.findViewById(R.id.titleTextView2);
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView2);
        titleTextView.setText(savedJobPost.getTitle());
        descriptionTextView.setText(savedJobPost.getDescription());

        return convertView;
    }


}

