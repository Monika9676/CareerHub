
package com.example.careerhub30;

import android.os.Parcel;
import android.os.Parcelable;

public class JobPost implements Parcelable {
    private String title;
    private String description;
    private boolean isSaved;
    public JobPost(String title, String description) {
        this.title = title;
        this.description = description;
        this.isSaved = false;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public static final Parcelable.Creator<JobPost> CREATOR = new Parcelable.Creator<JobPost>() {
        @Override
        public JobPost createFromParcel(Parcel in) {
            return new JobPost(in);
        }

        @Override
        public JobPost[] newArray(int size) {
            return new JobPost[size];
        }
    };

    private JobPost(Parcel in) {
        title = in.readString();
        description = in.readString();
        isSaved = in.readByte() != 0; // Read saved status
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeByte((byte) (isSaved ? 1 : 0)); // Write saved status
    }
    @Override
    public int describeContents() {
        return 0;
    }
}
