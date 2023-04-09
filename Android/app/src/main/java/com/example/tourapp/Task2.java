package com.example.tourapp;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.tourapp.data.RoutePlace;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Task2 extends TimerTask {
    public List<List<RoutePlace>> routes;
    public List<LatLng> points;
    public BaiduMap baiduMap;
    public MapView mapView;
    public int flag;
    public OverlayOptions moverlayOptions;
    public BitmapDescriptor bitmapDescriptor;
    public List<OverlayOptions> options;
    public List<RoutePlace> route ;
    public Timer timer ;

    public Task2(List<List<RoutePlace>> routes, BaiduMap baiduMap, Timer timer, MapView mapView) {
        this.routes = routes;
        this.baiduMap = baiduMap;
        this.timer = timer;
        this.mapView = mapView;
        route = routes.get(1);
        points = new ArrayList<LatLng>();
        flag = 0;
        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.baidumarker2);
        options = new ArrayList<OverlayOptions>();
    }


    @Override
    public void run() {
        LatLng latLng = new LatLng(route.get(flag).getLatitude(), route.get(flag).getLongitude());
        System.out.println(flag+"#"+route.get(flag).getLatitude()+"$"+route.get(flag).getLongitude());
        Bundle bundle = new Bundle();
        bundle.putLong("poi", -1);
        bundle.putLong("stars", route.get(flag).getStars());
        bundle.putDouble("latitude", route.get(flag).getLatitude());
        bundle.putDouble("longitude", route.get(flag).getLongitude());
        OverlayOptions mTextOptions = new TextOptions()
                .text("人数:"+route.get(flag).getStars())
                .bgColor(0xFFFFFF)
                .fontSize(30)
                .position(latLng)
                .fontColor(0xAA000000);

        OverlayOptions options1 = new MarkerOptions()
                .extraInfo(bundle)
                .icon(bitmapDescriptor)
                .position(latLng);
        options.add(options1);
        points.add(latLng);
        if (points.size()>1){
            moverlayOptions = new PolylineOptions()
                    .width(20)
                    .color(0xAAFFFF00)
                    .points(points);
        }
        mapView.post(new Runnable() {
            @Override
            public void run() {
                baiduMap.addOverlays(options);
                if (points.size()>1){
                    Overlay mPolyline = baiduMap.addOverlay(moverlayOptions);
                }
                Overlay mText = baiduMap.addOverlay(mTextOptions);
            }
        });
        flag = flag + 1;
        if (flag > 3) {
            timer.cancel();
        }
    }
}
