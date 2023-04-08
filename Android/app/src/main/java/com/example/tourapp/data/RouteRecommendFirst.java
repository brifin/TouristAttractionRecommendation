package com.example.tourapp.data;

import java.util.List;

public class RouteRecommendFirst {
    private List<List<double[]>> allRoute;
    private List<long[]> map_poi;

    public List<List<double[]>> getAllRoute() {
        return allRoute;
    }

    public void setAllRoute(List<List<double[]>> allRoute) {
        this.allRoute = allRoute;
    }

    public List<long[]> getMap_poi() {
        return map_poi;
    }

    public void setMap_poi(List<long[]> map_poi) {
        this.map_poi = map_poi;
    }
}
