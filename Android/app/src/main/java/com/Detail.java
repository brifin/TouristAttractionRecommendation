package com;

import java.util.List;

public class Detail {

    private String detail;
    private int[] photos;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Detail(String name, String detail, int[] photos){
        this.detail  = detail;
        this.photos = photos;
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int[] getPhotos() {
        return photos;
    }

    public void setPhotos(int[] photos) {
        this.photos = photos;
    }
}
