package com.example.tourapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    public View view;
    public int flag;
    public OverlayOptions moverlayOptions;
    public BitmapDescriptor bitmapDescriptor;
    public List<OverlayOptions> options;
    public List<RoutePlace> route ;
    public Timer timer ;

    public Task1(List<List<RoutePlace>> routes, BaiduMap baiduMap, Timer timer, MapView mapView,View view,BitmapDescriptor bitmapDescriptor) {
        this.routes = routes;
        this.baiduMap = baiduMap;
        this.timer = timer;
        this.mapView = mapView;
        this.bitmapDescriptor = bitmapDescriptor;
        route = routes.get(0);
        points = new ArrayList<LatLng>();
        flag = 0;
        this.view = view;
        options = new ArrayList<OverlayOptions>();
    }


    @Override
    public void run() {
        if (flag > 5) {
            timer.cancel();
        }
        LatLng latLng = new LatLng(route.get(flag).getLatitude(), route.get(flag).getLongitude());
        System.out.println(flag+"#"+route.get(flag).getLatitude()+"$"+route.get(flag).getLongitude());
        Bundle bundle = new Bundle();
        bundle.putLong("poi", -route.get(flag).getPoi());
        System.out.println("poi1"+route.get(flag).getPoi());

        bundle.putLong("stars", route.get(flag).getStars());
        bundle.putDouble("latitude", route.get(flag).getLatitude());
        bundle.putDouble("longitude", route.get(flag).getLongitude());


        TextView textView = view.findViewById(R.id.population);
        textView.setText("人数："+route.get(flag).getStars());
        bitmapDescriptor  = BitmapDescriptorFactory.fromView(view);

        OverlayOptions options1 = new MarkerOptions()
                .extraInfo(bundle)
                .icon(bitmapDescriptor)
                .position(latLng);
        options.add(options1);
        points.add(latLng);
        if (points.size()>1){
            moverlayOptions = new PolylineOptions()
                    .width(10)
                    .color(0xAA00DD00)
                    .points(points);
        }
        mapView.post(new Runnable() {
            @Override
            public void run() {
                baiduMap.addOverlays(options);
                if (points.size()>1){
                    Overlay mPolyline = baiduMap.addOverlay(moverlayOptions);
                }
            }
        });
        flag = flag + 1;

    }
}
