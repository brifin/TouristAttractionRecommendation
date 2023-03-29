package com.example.tourapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

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
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.example.tourapp.MainActivity;
import com.example.tourapp.MyLoveActivity2;
import com.example.tourapp.R;
import com.example.tourapp.ServiceCreator;
import com.example.tourapp.TestActivity;
import com.example.tourapp.data.PlaceData;
import com.example.tourapp.data.RouteData;
import com.example.tourapp.service.GetRecommendService;
import com.example.tourapp.service.GetRouteService;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
            //推荐
            case R.id.Lyre:
                mBaiduMap.clear();
                getPoiStarts();
                GetRecommendService recommendService = (GetRecommendService) ServiceCreator.creatService(GetRecommendService.class);
                recommendService.getRecommendData().enqueue(new Callback<List<PlaceData>>() {
                    @Override
                    public void onResponse(Call<List<PlaceData>> call, Response<List<PlaceData>> response) {
                        List<PlaceData> data = response.body();
                        for (int i = 0; i < data.size(); i++) {
                            LatLng point = new LatLng(data.get(i).getLatitude(), data.get(i).getLongitde());
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker);
                            OverlayOptions option = new MarkerOptions()
                                    .position(point)
                                    .icon(bitmapDescriptor)
                                    .animateType(MarkerOptions.MarkerAnimateType.drop);
                            mBaiduMap.addOverlay(option);

                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlaceData>> call, Throwable t) {
                        Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            //生成路线
            case R.id.Lypath:
                mBaiduMap.clear();
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker);
                GetRouteService getRouteService = (GetRouteService) ServiceCreator.creatService(GetRouteService.class);
                getRouteService.getRoute().enqueue(new Callback<List<RouteData>>() {
                    @Override
                    public void onResponse(Call<List<RouteData>> call, Response<List<RouteData>> response) {
                        List<RouteData> routeDataList = response.body();
                        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
                        List<LatLng> latLngList = new ArrayList<LatLng>();
                        List<PlaceData> placeDataList;
                        for (int i = 0; i < routeDataList.size(); i++) {
                            latLngList.clear();
                            options.clear();
                            placeDataList = routeDataList.get(i).getRoute();
                            for (int j = 0; j < placeDataList.size(); j++) {
                                LatLng point = new LatLng(placeDataList.get(j).getLatitude(), placeDataList.get(j).getLongitde());
                                latLngList.add(point);
                                OverlayOptions options1 = new MarkerOptions()
                                        .position(point)
                                        .icon(bitmapDescriptor)
                                        .animateType(MarkerOptions.MarkerAnimateType.drop);
                                options.add(options1);

                            }
                            OverlayOptions overlayOptions = new PolylineOptions()
                                    .width(7)
                                    .color(0xAAFF0000)
                                    .points(latLngList);
                            mBaiduMap.addOverlay(overlayOptions);
                            mBaiduMap.addOverlays(options);

                        }
                    }

                    @Override
                    public void onFailure(Call<List<RouteData>> call, Throwable t) {
                        Toast.makeText(getContext(), "网络请求失败", Toast.LENGTH_SHORT).show();
                    }
                });

                break;

            //生成附近景点
            case R.id.LyAttra:
                //测试全景图
                Intent intent = new Intent(getContext(), TestActivity.class);
                startActivity(intent);
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

        //设置地图监听事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBaiduMap.clear();
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                return true;
            }
        });
    }

    //获取我的点赞中景点的POI序号
    private void getPoiStarts() {
        List<String> poiStarts = new ArrayList<String>();
        MyLoveActivity2.getdata();
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