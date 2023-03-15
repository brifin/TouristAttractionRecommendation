package com.example.tourapp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.example.tourapp.DetailActivity;
import com.example.tourapp.R;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements View.OnClickListener {


    private MapView mapView = null;
    private BaiduMap mBaiduMap;
    private Button recommendBtu;
    private Button pathBtu;
    private View pop_upsView;
    private int longAnimationDuration;
    private Button photoBtu;
    private Button placeBtu;
    private Button detailBtu;

    public MapFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        permissionList = new ArrayList<String>();
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.INTERNET);
//        }
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
//        }
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        if (!permissionList.isEmpty()) {
//            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
//            requestPermissions(permissions, 1001);
//        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.mapview);
        mBaiduMap = mapView.getMap();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(18.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mBaiduMap.setIndoorEnable(true);
        UiSettings uiSettings = mBaiduMap.getUiSettings();
        uiSettings.setEnlargeCenterWithDoubleClickEnable(true);
        pop_upsView = (LinearLayout) view.findViewById(R.id.markerPop_ups);
        recommendBtu = (Button) view.findViewById(R.id.recommendbtu);
        pathBtu = (Button) view.findViewById(R.id.pathbtu);
        photoBtu = (Button) view.findViewById(R.id.photobtu);
        placeBtu = (Button) view.findViewById(R.id.placebtu);
        detailBtu = (Button) view.findViewById(R.id.detailbtu);
        recommendBtu.setOnClickListener(this);
        pathBtu.setOnClickListener(this);
        photoBtu.setOnClickListener(this);
        placeBtu.setOnClickListener(this);
        detailBtu.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recommendbtu:
                LatLng point = new LatLng(39.90, 116.28);
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.baidumarker2);
                OverlayOptions option = new MarkerOptions()
                        .icon(bitmap)
                        .position(point);
                mBaiduMap.addOverlay(option);

                break;
            case R.id.pathbtu:
                Toast.makeText(getContext(), "pa", Toast.LENGTH_SHORT).show();

                break;

            case R.id.photobtu:
                break;
            case R.id.placebtu:
                break;
            case R.id.detailbtu:
                Intent intent = new Intent(getContext(), DetailActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                longAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);
                pop_upsView.setAlpha(0f);
                pop_upsView.setVisibility(View.VISIBLE);
                pop_upsView.animate()
                        .alpha(1.0f)
                        .setDuration(longAnimationDuration)
                        .setListener(null);
                return true;
            }
        });
    }


}