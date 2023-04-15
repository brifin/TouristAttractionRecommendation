package com.example.tourapp.viewAndItem;

public class TourItem {
    private String tourName;
    private int imageId;
    private String schedule;
    private boolean isScatteredGroups;
    private String positionName;


    public TourItem() {
    }

    public TourItem(String tourName, int imageId, String schedule, boolean isScatteredGroups, String positionName) {
        this.tourName = tourName;
        this.imageId = imageId;
        this.schedule = schedule;
        this.isScatteredGroups = isScatteredGroups;
        this.positionName = positionName;
    }

    /**
     * 获取
     * @return tourName
     */
    public String getTourName() {
        return tourName;
    }

    /**
     * 设置
     * @param tourName
     */
    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    /**
     * 获取
     * @return imageId
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * 设置
     * @param imageId
     */
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    /**
     * 获取
     * @return schedule
     */
    public String getSchedule() {
        return schedule;
    }

    /**
     * 设置
     * @param schedule
     */
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    /**
     * 获取
     * @return isScatteredGroups
     */
    public boolean isIsScatteredGroups() {
        return isScatteredGroups;
    }

    /**
     * 设置
     * @param isScatteredGroups
     */
    public void setIsScatteredGroups(boolean isScatteredGroups) {
        this.isScatteredGroups = isScatteredGroups;
    }

    /**
     * 获取
     * @return showLine
     */
    public String getPositionName() {
        return positionName;
    }

    /**
     * 设置
     * @param positionName
     */
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String toString() {
        return "TourItem{tourName = " + tourName + ", imageId = " + imageId + ", schedule = " + schedule + ", isScatteredGroups = " + isScatteredGroups + ", showLine = " + positionName + "}";
    }
}
