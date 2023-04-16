package com.example.tourapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.example.tourapp.application.MyApplication;
import com.example.tourapp.data.RoutePlace;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Task3 extends TimerTask {
    public List<List<RoutePlace>> routes;
    public List<LatLng> points;
    public BaiduMap baiduMap;
    public MapView mapView;
    public View view;
    public int flag;
    public OverlayOptions moverlayOptions;
    public BitmapDescriptor bitmapDescriptor;
    public List<OverlayOptions> options;
    public List<RoutePlace> route;
    public Timer timer;

    public Task3(List<List<RoutePlace>> routes, BaiduMap baiduMap, Timer timer, MapView mapView, View view, BitmapDescriptor bitmapDescriptor) {
        this.routes = routes;
        this.baiduMap = baiduMap;
        this.timer = timer;
        this.mapView = mapView;
        route = routes.get(2);
        this.bitmapDescriptor = bitmapDescriptor;
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
        System.out.println(flag + "&");
        LatLng latLng = new LatLng(route.get(flag).getLatitude(), route.get(flag).getLongitude());
        System.out.println(flag + "#" + route.get(flag).getLatitude() + "$" + route.get(flag).getLongitude());
        Bundle bundle = new Bundle();
        bundle.putLong("poi", route.get(flag).getPoi());
        System.out.println("poi3"+route.get(flag).getPoi());
        bundle.putLong("stars", route.get(flag).getStars());
        bundle.putDouble("latitude", route.get(flag).getLatitude());
        bundle.putDouble("longitude", route.get(flag).getLongitude());


        TextView textView = view.findViewById(R.id.population);
        textView.setText("人数：" + route.get(flag).getStars());
        bitmapDescriptor  = BitmapDescriptorFactory.fromView(view);


        OverlayOptions options1 = new MarkerOptions()
                .extraInfo(bundle)
                .icon(bitmapDescriptor)
                .position(latLng);
        options.add(options1);
        points.add(latLng);
        if (points.size() > 1) {
            moverlayOptions = new PolylineOptions()
                    .width(10)
                    .color(0xAAFF0000)
                    .points(points);
        }
        mapView.post(new Runnable() {
            @Override
            public void run() {
                baiduMap.addOverlays(options);
                if (points.size() > 1) {
                    Overlay mPolyline = baiduMap.addOverlay(moverlayOptions);
                }
            }
        });
        flag = flag + 1;
    }
}
