package com.example.tourapp.fragment;

import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.example.tourapp.R;
import com.example.tourapp.ServiceCreator_app01;
import com.example.tourapp.ServiceCreator_user;
import com.example.tourapp.activity.MainActivity;
import com.example.tourapp.activity.MyLoveActivity2;
import com.example.tourapp.data.Click_love;
import com.example.tourapp.data.NearlyAttra;
import com.example.tourapp.data.RecommendPlaceData;
import com.example.tourapp.data.RouteData;
import com.example.tourapp.service.ClickLoveService;
import com.example.tourapp.service.GetNearlyAttra;
import com.example.tourapp.service.GetRecommendService;
import com.example.tourapp.service.GetRouteService;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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
    //保存点击marker的信息
    public Click_love clickMarker = new Click_love();

    //当前我的位置
    private double[] currentPoint = new double[2];


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
                List<String> poiStarts = new ArrayList<String>();
                poiStarts = getPoiStarts();
                GetRecommendService recommendService = (GetRecommendService) ServiceCreator_app01.creatService(GetRecommendService.class);
                String json = new Gson().toJson(poiStarts);
                RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), json);
                recommendService.getRecommendData(requestBody).enqueue(new Callback<List<double[]>>() {
                    @Override
                    public void onResponse(Call<List<double[]>> call, Response<List<double[]>> response) {
                        List<double[]> data = response.body();
                        List<double[]> realData = filterPoints(currentPoint, data);
                        for (int i = 0; i < realData.size(); i++) {
                            double[] doubles = realData.get(i);
                            LatLng point = new LatLng(doubles[0], doubles[1]);
                            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.baidumarker2);
                            OverlayOptions option = new MarkerOptions()
                                    .position(point)
                                    .icon(bitmapDescriptor)
                                    .animateType(MarkerOptions.MarkerAnimateType.none);
                            mBaiduMap.addOverlay(option);

                        }
                    }
                    @Override
                    public void onFailure(Call<List<double[]>> call, Throwable t) {
                        Toast.makeText(getContext(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        System.out.println(t.toString());
                    }
                });
                break;
            //生成路线
            case R.id.Lypath:
                mBaiduMap.clear();
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker);
                GetRouteService getRouteService = (GetRouteService) ServiceCreator_app01.creatService(GetRouteService.class);
                getRouteService.getRoute().enqueue(new Callback<List<RouteData>>() {
                    @Override
                    public void onResponse(Call<List<RouteData>> call, Response<List<RouteData>> response) {
                        List<RouteData> routeDataList = response.body();
                        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
                        List<LatLng> latLngList = new ArrayList<LatLng>();
                        List<RecommendPlaceData> placeDataList;
                        for (int i = 0; i < routeDataList.size(); i++) {
                            latLngList.clear();
                            options.clear();
                            placeDataList = routeDataList.get(i).getRoute();
                            for (int j = 0; j < placeDataList.size(); j++) {
                                LatLng point = new LatLng(Double.parseDouble(placeDataList.get(j).getLatitude()), Double.parseDouble(placeDataList.get(j).getLongitde()));
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
                mBaiduMap.clear();
                currentPoint[0] = 30.263607974299997;
                currentPoint[1] = -97.7395677567;
                GetNearlyAttra getNearlyAttra = (GetNearlyAttra) ServiceCreator_user.creatService(GetNearlyAttra.class);
                getNearlyAttra.getNearlyAttra(String.valueOf(currentPoint[0]), String.valueOf(currentPoint[1])).enqueue(new Callback<NearlyAttra>() {
                    @Override
                    public void onResponse(Call<NearlyAttra> call, Response<NearlyAttra> response) {
                        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
                        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.baidumarker2);
                        NearlyAttra nearlyAttra = response.body();
                        String[] data = nearlyAttra.getData();
                        List<double[]> realPoints = new ArrayList<double[]>();
                        for (int i = 0; data[i] != null; i++) {
                            String[] place = data[i].split("\\s+");
                            double[] doublepoint = new double[3];
                            doublepoint[0] = Double.parseDouble(place[0]);
                            doublepoint[1] = Double.parseDouble(place[1]);
                            doublepoint[2] = Double.parseDouble(place[2]);
                            realPoints.add(doublepoint);
                        }
                        List<double[]> nearlypoint = filterPoints(currentPoint, realPoints);
                        double[] nearlypoint1 = new double[3];
                        for (int i = 0; i < nearlypoint.size(); i++) {
                            nearlypoint1 = nearlypoint.get(i);
                            LatLng point = new LatLng(nearlypoint1[0], nearlypoint1[1]);
                            Bundle bundle = new Bundle();
                            bundle.putLong("poi", Double.doubleToLongBits(nearlypoint1[2]));
                            OverlayOptions options1 = new MarkerOptions()
                                    .animateType(MarkerOptions.MarkerAnimateType.none)
                                    .position(point)
                                    .icon(bitmapDescriptor)
                                    .extraInfo(bundle);
                            options.add(options1);
                        }
                        mBaiduMap.addOverlays(options);
                    }

                    @Override
                    public void onFailure(Call<NearlyAttra> call, Throwable t) {
                        Toast.makeText(getContext(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        System.out.println(t.toString());
                    }
                });
                break;
            //点赞
            case R.id.heart:
                ClickLoveService clickLoveService = (ClickLoveService) ServiceCreator_app01.creatService(ClickLoveService.class);
                if (flag) {
                    heartiv.setImageDrawable(getResources().getDrawable(R.drawable.heart1, null));
                    clickMarker.setTimestamp(getTime());
                    clickMarker.setStar(1);
                    //post请求传输点赞数据
                    String clickMarkerJson = new Gson().toJson(clickMarker);
                    RequestBody requestBodyClickLove = RequestBody.create(MediaType.get("application/json; charset=utf-8"), clickMarkerJson);
                    clickLoveService.clickLove(requestBodyClickLove);
                    flag = false;
                } else {
                    heartiv.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                    clickMarker.setTimestamp(getTime());
                    clickMarker.setStar(0);
                    //post传输点赞数据
                    String clickMarkerJson1 = new Gson().toJson(clickMarker);
                    RequestBody requestBodyClickLove2 = RequestBody.create(MediaType.get("application/json; charset=utf-8"), clickMarkerJson1);
                    clickLoveService.clickLove(requestBodyClickLove2);
                    flag = true;
                }
            default:
                break;
        }

        //设置marker监听事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                //记录marker信息
                clickMarker.setNickname(MainActivity.nickname);
                clickMarker.setLat(marker.getPosition().latitude);
                clickMarker.setLon(marker.getPosition().longitude);
                long poi = marker.getExtraInfo().getLong("poi");
                clickMarker.setPoi(poi);
                return true;
            }
        });

        //设置地图监听事件
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });
    }


    //获取我的点赞中景点的POI序号
    private List<String> getPoiStarts() {
        List<String> poiStarts = new ArrayList<String>();
        //更新我的点赞的数据
        if (MainActivity.nickname != null) {
            MyLoveActivity2.getData(MainActivity.nickname);
            for (int i = 0; i < MyLoveActivity2.mData.size(); i++) {
                poiStarts.add(String.valueOf(MyLoveActivity2.mData.get(i).getPoi()));
            }
        } else {
            Toast.makeText(getContext(), "您未登录,请您登录再执行此操作", Toast.LENGTH_SHORT).show();
        }
        return poiStarts;
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

    //获取时间
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        return time;
    }

    //过滤地点,舍去过远的地点
    public List<double[]> filterPoints(double[] currentPoint, List<double[]> points) {
        List<double[]> realPoints = new ArrayList<double[]>();
        double[] point ;
        for (int i = 0; i < points.size(); i++) {
            point = points.get(i);
            if((point[0]-currentPoint[0])>(-0.25)&&(point[0]-currentPoint[0])<0.25&&(point[1]-currentPoint[1])<0.4&&(point[1]-currentPoint[1])>(-0.4)){
                realPoints.add(point);
            }
        }
        return realPoints;
    }
}