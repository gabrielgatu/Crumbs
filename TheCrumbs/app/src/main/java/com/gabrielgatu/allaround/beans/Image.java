package com.gabrielgatu.allaround.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gabrielgatu on 30/05/15.
 */
public class Image {

    @SerializedName("_id")
    private String id;

    @SerializedName("user")
    private String userId;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("image")
    private String image;

    @SerializedName("date")
    private String date;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }
}
