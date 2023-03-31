package com.example.tourapp.viewAndItem;

public class BrowseItem {
    private int photo;
    private String place;
    private String timestamp;

    private double latitude;
    private double longitude;
    private long poi;

    public BrowseItem() {
    }

    public BrowseItem(int photo, String place, String timestamp, double latitude, double longitude, long poi) {
        this.photo = photo;
        this.place = place;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.poi = poi;
    }

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

    public String toString() {
        return "BrowseItem{photo = " + photo + ", place = " + place + ", timestamp = " + timestamp + ", latitude = " + latitude + ", longitude = " + longitude + ", poi = " + poi + "}";
    }
}
