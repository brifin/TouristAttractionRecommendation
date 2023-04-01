package com.example.tourapp.data;

public class MyBrowse {
    private String nickname;
    private long poi;
    private double lat;
    private double lon;
    private String timestamp;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getPoi() {
        return poi;
    }

    public void setPoi(long poi) {
        this.poi = poi;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
