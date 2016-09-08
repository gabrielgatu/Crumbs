package com.gabrielgatu.allaround.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gabrielgatu on 12/05/15.
 */
public class Discussion {

    @SerializedName("_id")
    private String id;

    @SerializedName("user")
    private User user;

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;

    @SerializedName("date")
    private String date;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
