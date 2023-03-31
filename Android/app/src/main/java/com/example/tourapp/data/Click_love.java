package com.example.tourapp.data;
//点击点赞的post请求的参数
public class Click_love {
    private String nickname;
    private long poi;
    private double lat;
    private double lon;
    //标记是否点赞
    private int star;
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

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
