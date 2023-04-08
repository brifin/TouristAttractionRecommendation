package com.example.tourapp.data;

import java.util.List;

public class RecommendReturn {
    private List<double[]> recommends;
    private long[] map_poi;

    public long[] getMap_poi() {
        return map_poi;
    }

    public void setMap_poi(long[] map_poi) {
        this.map_poi = map_poi;
    }

    public List<double[]> getRecommends() {
        return recommends;
    }

    public void setRecommends(List<double[]> recommends) {
        this.recommends = recommends;
    }
}
