package com.example.tourapp.data;

public class RoutePlace {
    private double latitude;
    private double longitude;
    private long stars;
    private long poi;

    public long getPoi() {
        return poi;
    }

    public void setPoi(long poi) {
        this.poi = poi;
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

    public long getStars() {
        return stars;
    }

    public void setStars(long stars) {
        this.stars = stars;
    }
}
