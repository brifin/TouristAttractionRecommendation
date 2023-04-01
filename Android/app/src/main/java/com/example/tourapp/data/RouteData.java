package com.example.tourapp.data;

import java.util.List;

public class RouteData {
    private double latitude;
    private double longitude;
    private long[] stars;

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

    public long[] getStars() {
        return stars;
    }

    public void setStars(long[] stars) {
        this.stars = stars;
    }
}
