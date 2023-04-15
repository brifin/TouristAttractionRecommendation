package com.example.tourapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.UiThread;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.tourapp.activity.MainActivity;
import com.example.tourapp.application.MyApplication;
import com.example.tourapp.data.RoutePlace;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Task1 extends TimerTask {
    public List<List<RoutePlace>> routes;
    public List<LatLng> points;
    public BaiduMap baiduMap;
    public MapView mapView;

    private RoutePlanSearch mSearch;
    public int flag;
    public BitmapDescriptor bitmapDescriptor;


    public List<OverlayOptions> options;
    public List<RoutePlace> route;
    public Timer timer;

    public Task1(List<List<RoutePlace>> routes, BaiduMap baiduMap, Timer timer, MapView mapView) {
        this.routes = routes;
        this.baiduMap = baiduMap;
        this.timer = timer;
        this.mapView = mapView;
        route = routes.get(0);
        points = new ArrayList<LatLng>();
        flag = 0;
        mSearch = RoutePlanSearch.newInstance();
        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.baidumarker2);
        options = new ArrayList<OverlayOptions>();
    }

    @Override
    public void run() {
        LatLng latLng = new LatLng(route.get(flag).getLatitude(), route.get(flag).getLongitude());
        Bundle bundle = new Bundle();
        bundle.putLong("poi", -1);
        bundle.putLong("stars", route.get(flag).getStars());
        bundle.putDouble("latitude", route.get(flag).getLatitude());
        bundle.putDouble("longitude", route.get(flag).getLongitude());
        Bundle bundle1 = new Bundle();

        OverlayOptions options1 = new MarkerOptions()
                .extraInfo(bundle)
                .icon(bitmapDescriptor)
                .position(latLng);
        options.add(options1);


        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
                if(drivingRouteResult.getRouteLines().size() > 0){
                    overlay.setData(drivingRouteResult.getRouteLines().get(0));
                    overlay.addToMap();
                }

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };
        mSearch.setOnGetRoutePlanResultListener(listener);
        LatLng latLngStart = new LatLng(route.get(0).getLatitude(), route.get(0).getLongitude());
        LatLng latLngEnd = new LatLng(route.get(route.size()-1).getLatitude(), route.get(route.size()-1).getLongitude());
        PlanNode stNode = PlanNode.withLocation(latLngStart);
        PlanNode endNode = PlanNode.withLocation(latLngEnd);
        mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(endNode));
        baiduMap.addOverlays(options);
        mSearch.destroy();

        flag = flag + 1;
        if (flag > 7) {
            timer.cancel();
        }
    }
}
