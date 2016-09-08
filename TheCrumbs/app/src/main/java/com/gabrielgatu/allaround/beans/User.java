package com.gabrielgatu.allaround.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class User {

    @SerializedName("_id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("image")
    private String image;

    @SerializedName("numImages")
    private String numImages;

    @SerializedName("numFriends")
    private String numFriends;

    @SerializedName("numMessages")
    private String numMessages;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getImage() {
        return image;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNumImages() {
        return numImages;
    }

    public String getNumMessages() {
        return numMessages;
    }

    public String getNumFriends() {
        return numFriends;
    }
}