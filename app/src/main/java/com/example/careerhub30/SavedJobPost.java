package com.example.careerhub30;

import android.os.Parcel;
import android.os.Parcelable;

public class SavedJobPost implements Parcelable {
    private String title;
    private String description;

    public SavedJobPost(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Parcelable implementation
    public static final Parcelable.Creator<SavedJobPost> CREATOR = new Parcelable.Creator<SavedJobPost>() {
        @Override
        public SavedJobPost createFromParcel(Parcel in) {
            return new SavedJobPost(in);
        }

        @Override
        public SavedJobPost[] newArray(int size) {
            return new SavedJobPost[size];
        }
    };

    private SavedJobPost(Parcel in) {
        title = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
