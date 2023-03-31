package com.example.tourapp.viewAndItem;

import android.provider.ContactsContract;

import java.util.Date;

public class RecordItem {
    private Integer id;
    private String place;
    private String time;
    private Integer peoples;

    public RecordItem() {
    }

    public RecordItem(Integer id, String place, String time, Integer peoples) {
        this.id = id;
        this.place = place;
        this.time = time;
        this.peoples = peoples;
    }

    /**
     * 获取
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(Integer id) {
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
    public String getTime() {
        return time;
    }

    /**
     * 设置
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 获取
     * @return peoples
     */
    public Integer getPeoples() {
        return peoples;
    }

    /**
     * 设置
     * @param peoples
     */
    public void setPeoples(Integer peoples) {
        this.peoples = peoples;
    }

    public String toString() {
        return "RecordItem{id = " + id + ", place = " + place + ", time = " + time + ", peoples = " + peoples + "}";
    }
}
