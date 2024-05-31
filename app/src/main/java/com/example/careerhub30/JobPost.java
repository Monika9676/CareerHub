package com.example.careerhub30;

import android.os.Parcel;
import android.os.Parcelable;

public class JobPost implements Parcelable {
    private String title;
    private String description;
    private String link;
    private String company;
    private String location;
    private boolean isSaved;

    public JobPost(String title, String description, String link, String company, String location) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.company = company;
        this.location = location;
        this.isSaved = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    // Parcelable implementation

    protected JobPost(Parcel in) {
        title = in.readString();
        description = in.readString();
        link = in.readString();
        company = in.readString();
        location = in.readString();
        isSaved = in.readByte() != 0;
    }

    public static final Creator<JobPost> CREATOR = new Creator<JobPost>() {
        @Override
        public JobPost createFromParcel(Parcel in) {
            return new JobPost(in);
        }

        @Override
        public JobPost[] newArray(int size) {
            return new JobPost[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeString(company);
        dest.writeString(location);
        dest.writeByte((byte) (isSaved ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}