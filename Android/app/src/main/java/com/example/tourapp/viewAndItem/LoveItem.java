package com.example.tourapp.viewAndItem;

public class LoveItem {
    private int photo;
    private String place;
    private String timestamp;

    private double latitude;
    private double longitude;
    private long poi;

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getPoi() {
        return poi;
    }

    public void setPoi(long poi) {
        this.poi = poi;
    }
}
