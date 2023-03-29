package com.example.tourapp.data;

public class PlaceData {

    //纬度
    private double latitude;

    //经度
    private double longitde;


    //poi序号
    private long poi;

    public double getLongitde() {
        return longitde;
    }

    public void setLongitde(double longitde) {
        this.longitde = longitde;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getPoi() {
        return poi;
    }

    public void setPoi(long poi) {
        this.poi = poi;
    }
}
