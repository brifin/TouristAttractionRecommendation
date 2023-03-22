package com.example.tourapp.viewAndItem;

import android.provider.ContactsContract;

import java.util.Date;

public class RecordItem {
    private int id;
    private String place;
    private Date time;

    public RecordItem() {
    }

    public RecordItem(int id, String place, Date time) {
        this.id = id;
        this.place = place;
        this.time = time;
    }

    /**
     * 获取
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取
     * @return place
     */
    public String getPlace() {
        return place;
    }

    /**
     * 设置
     * @param place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * 获取
     * @return time
     */
    public Date getTime() {
        return time;
    }

    /**
     * 设置
     * @param time
     */
    public void setTime(Date time) {
        this.time = time;
    }

    public String toString() {
        return "RecordItem{id = " + id + ", place = " + place + ", time = " + time + "}";
    }
}
