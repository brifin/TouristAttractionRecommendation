package com.example.tourapp.data;

public class Attraction1 {
    private Object id;
    private double latitude;
    private double longitude;
    private long poi;
    //记录该景点人数
    private long stars;

    public Object getId() { return id; }
    public void setId(Object value) { this.id = value; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double value) { this.latitude = value; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double value) { this.longitude = value; }

    public long getPoi() { return poi; }
    public void setPoi(long value) { this.poi = value; }

    public long getStars() { return stars; }
    public void setStars(long value) { this.stars = value; }
}
