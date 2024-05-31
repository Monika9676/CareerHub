package com.example.careerhub30;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class SavedJobs extends Fragment {

    private List<SavedJobPost> savedJobPosts;
    private SavedJobsAdapter adapter;
    private SQLiteDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_jobs, container, false);
        DBHelper dbHelper = new DBHelper(requireContext());
        database = dbHelper.getWritableDatabase();
        savedJobPosts = new ArrayList<>();

        retrieveSavedJobPosts();
        ListView savedJobListView = view.findViewById(R.id.savedList);
        adapter = new SavedJobsAdapter(requireContext(), savedJobPosts, database);
        savedJobListView.setAdapter(adapter);

        TextView noSavedJobsTextView = view.findViewById(R.id.noSavedJobsTextView);
        if (savedJobPosts.isEmpty()) {
            noSavedJobsTextView.setVisibility(View.VISIBLE);
        } else {
            noSavedJobsTextView.setVisibility(View.GONE);
        }
        return view;
    }

    private void retrieveSavedJobPosts() {
        Cursor cursor = database.rawQuery("SELECT id, title, description FROM saved_jobs", null);
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int titleIndex = cursor.getColumnIndex("title");
            int descriptionIndex = cursor.getColumnIndex("description");
            do {
                if (idIndex != -1 && titleIndex != -1 && descriptionIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String title = cursor.getString(titleIndex);
                    String description = cursor.getString(descriptionIndex);
                    Log.d("SavedJobs", "Fetched job post: " + title + " - " + description);
                    savedJobPosts.add(new SavedJobPost(id, title, description));
                } else {
                    Log.e("SavedJobs", "Invalid column indices");
                }
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Log.d("SavedJobs", "No saved jobs found");
        }
    }
}
