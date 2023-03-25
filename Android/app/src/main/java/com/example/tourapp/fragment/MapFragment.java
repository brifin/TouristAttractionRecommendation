package com.example.tourapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.example.tourapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class MapFragment extends Fragment implements View.OnClickListener {


    private MapView mapView = null;
    private BaiduMap mBaiduMap;

    private View pop_upsView;
    private BottomSheetBehavior behavior;
    private TextView detailText;
    private ImageView heartiv;
    private LinearLayout recommendLY;
    private LinearLayout pathLY;
    private LinearLayout attraLY;
    private Boolean flag = true;
    private int longAnimationDuration;

    public MapFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initView(view);
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
            case R.id.Lyre:
                LatLng point = new LatLng(39.90, 116.28);
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.baidumarker2);
                OverlayOptions option = new MarkerOptions()
                        .icon(bitmap)
                        .position(point);
                mBaiduMap.addOverlay(option);
                break;
            case R.id.Lypath:
                break;

            case R.id.LyAttra:
                break;

            case R.id.heart:
                if (flag) {
                    heartiv.setImageDrawable(getResources().getDrawable(R.drawable.heart1, null));
                    flag = false;
                } else {
                    heartiv.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                    flag = true;
                }
            default:
                break;
        }
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return true;
            }
        });
    }

    //设置弹窗动画
    public void setAnimation(View view) {
        TranslateAnimation translateAnimation;
        if (view.getVisibility() == View.VISIBLE) {
            translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        } else {
            translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f);
        }
        translateAnimation.setDuration(250);
        view.setAnimation(translateAnimation);
    }

    //初始化视图
    public void initView(View view) {
        mapView = (MapView) view.findViewById(R.id.mapview);
        mBaiduMap = mapView.getMap();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(18.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mBaiduMap.setIndoorEnable(true);

        BaiduMap.OnMapClickListener listener = new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {
            }
        };
        mBaiduMap.setOnMapClickListener(listener);
        UiSettings uiSettings = mBaiduMap.getUiSettings();
        uiSettings.setEnlargeCenterWithDoubleClickEnable(true);
        pop_upsView = (FrameLayout) view.findViewById(R.id.markerPop_ups);
        behavior = BottomSheetBehavior.from(pop_upsView);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior.setBottomSheetCallback(new MyBottomSheetCallback());
        detailText = pop_upsView.findViewById(R.id.detailtext);
        detailText.setMovementMethod(ScrollingMovementMethod.getInstance());
        heartiv = pop_upsView.findViewById(R.id.heart);
        heartiv.setOnClickListener(this);
        recommendLY = (LinearLayout) view.findViewById(R.id.Lyre);
        pathLY = (LinearLayout) view.findViewById(R.id.Lypath);
        attraLY = (LinearLayout) view.findViewById(R.id.LyAttra);
        recommendLY.setOnClickListener(this);
        pathLY.setOnClickListener(this);
        attraLY.setOnClickListener(this);


    }


    private class MyBottomSheetCallback extends BottomSheetBehavior.BottomSheetCallback {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    }
}