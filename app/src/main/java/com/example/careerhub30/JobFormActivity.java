package com.example.careerhub30;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class JobFormActivity extends AppCompatActivity {

    private EditText etJobRole, etDescription;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_form); // Replace with your layout file

        // Initialize the database helper
        dbHelper = new DBHelper(this);

        // Initialize EditText fields
        etJobRole = findViewById(R.id.etJobRole); // Replace with actual view ID
        etDescription = findViewById(R.id.etDescription); // Replace with actual view ID


        // Initialize submit button
        Button submitButton = findViewById(R.id.submitButton); // Replace with actual view ID
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditText fields
                String role = etJobRole.getText().toString().trim();
                String description = etDescription.getText().toString().trim();

                String roleLowerCase = role.toLowerCase();
                String descriptionLowerCase = description.toLowerCase();
                // Validate input fields
                if (role.isEmpty()) {
                    etJobRole.setError("Job role is required");
                    etJobRole.requestFocus();
                    return;
                }

                if (description.isEmpty()) {
                    etDescription.setError("Description is required");
                    etDescription.requestFocus();
                    return;
                }

                // Check if the job post already exists
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String query = "SELECT COUNT(*) FROM job_posts WHERE LOWER(title) = ? AND LOWER(description) = ?";
                Cursor cursor = db.rawQuery(query, new String[]{roleLowerCase, descriptionLowerCase});

                if (cursor != null) {
                    cursor.moveToFirst();
                    int count = cursor.getInt(0);
                    cursor.close();

                    if (count > 0) {
                        // Job post already exists, show a toast message
                        Toast.makeText(JobFormActivity.this, "This job post already exists!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Job post does not exist, insert the new job post
                        ContentValues values = new ContentValues();
                        values.put("title", role);
                        values.put("description", description);
                        db.insert("job_posts", null, values);

                        // Show success message
                        Toast.makeText(JobFormActivity.this, "Job post saved successfully!", Toast.LENGTH_SHORT).show();

                        // Close the activity
                        finish();
                    }
                } else {
                    // Handle error
                    Toast.makeText(JobFormActivity.this, "Error checking job post", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
