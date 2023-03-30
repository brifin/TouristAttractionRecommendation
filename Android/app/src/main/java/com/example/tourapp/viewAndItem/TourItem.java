package com.example.tourapp.viewAndItem;

public class TourItem {
    private String tourName;
    private int imageId;
    private int tourId;
    private boolean isScatteredGroups;


    public TourItem() {
    }

    public TourItem(String tourName, int imageId, int tourId, boolean isScatteredGroups) {
        this.tourName = tourName;
        this.imageId = imageId;
        this.tourId = tourId;
        this.isScatteredGroups = isScatteredGroups;
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
     * @return tourId
     */
    public int getTourId() {
        return tourId;
    }

    /**
     * 设置
     * @param tourId
     */
    public void setTourId(int tourId) {
        this.tourId = tourId;
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

    public String toString() {
        return "TourItem{tourName = " + tourName + ", imageId = " + imageId + ", tourId = " + tourId + ", isScatteredGroups = " + isScatteredGroups + "}";
    }
}
