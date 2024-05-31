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
    private EditText etJobRole, etDescription, etLink ,etCompany, etLocation;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_form);
        dbHelper = new DBHelper(this);
        etJobRole = findViewById(R.id.etJobRole);
        etDescription = findViewById(R.id.etDescription);
        etLink=findViewById(R.id.etLink);
        etCompany=findViewById(R.id.etCompany);
        etLocation=findViewById(R.id.etLocation);
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = etJobRole.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String link = etLink.getText().toString().trim();
                String company= etCompany.getText().toString().trim();
                String location = etLocation.getText().toString().trim();

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

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String query = "SELECT COUNT(*) FROM job_posts WHERE LOWER(title) = ? AND LOWER(description) = ?";
                Cursor cursor = db.rawQuery(query, new String[]{roleLowerCase, descriptionLowerCase});

                if (cursor != null) {
                    cursor.moveToFirst();
                    int count = cursor.getInt(0);
                    cursor.close();
                    if (count > 0) {
                        Toast.makeText(JobFormActivity.this, "This job post already exists!", Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("title", role);
                        values.put("description", description);
                        values.put("link", link);
                        values.put("company", company);
                        values.put("location", location);
                        db.insert("job_posts", null, values);
                        Toast.makeText(JobFormActivity.this, "Job post saved successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(JobFormActivity.this, "Error checking job post", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
