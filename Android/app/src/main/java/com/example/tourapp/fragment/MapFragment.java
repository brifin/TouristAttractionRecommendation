package com.example.tourapp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Detail;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.base.LanguageType;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
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
import com.example.tourapp.NumberFlag;
import com.example.tourapp.Photo;
import com.example.tourapp.PoiPhoto;
import com.example.tourapp.R;
import com.example.tourapp.ServiceCreator_app01;
import com.example.tourapp.ServiceCreator_attractions;
import com.example.tourapp.ServiceCreator_user;
import com.example.tourapp.Task1;
import com.example.tourapp.Task2;
import com.example.tourapp.Task3;
import com.example.tourapp.activity.LoginActivity;
import com.example.tourapp.activity.MainActivity;
import com.example.tourapp.activity.MyLoveActivity2;
import com.example.tourapp.application.MyApplication;
import com.example.tourapp.data.Click_love;
import com.example.tourapp.data.MyBrowse;
import com.example.tourapp.data.MyLoveData;
import com.example.tourapp.data.MyLoveDataArray;
import com.example.tourapp.data.NearlyAttra;
import com.example.tourapp.data.Place;
import com.example.tourapp.data.RecommendReturn;
import com.example.tourapp.data.RecommendStars;
import com.example.tourapp.data.Result;
import com.example.tourapp.data.RouteData;
import com.example.tourapp.data.RouteFirstPlace;
import com.example.tourapp.data.RouteFirstResponse;
import com.example.tourapp.data.RouteListData;
import com.example.tourapp.data.RoutePlace;
import com.example.tourapp.data.RouteRecommendFirst;
import com.example.tourapp.data.RouteRespose;
import com.example.tourapp.data.UserData;
import com.example.tourapp.httpInterface.GroupInterface;
import com.example.tourapp.interceptor.AddCookiesInterceptor;
import com.example.tourapp.interceptor.ReceivedCookiesInterceptor;
import com.example.tourapp.service.ClickLoveService;
import com.example.tourapp.service.ClickMarkerService;
import com.example.tourapp.service.GetNearlyAttra;
import com.example.tourapp.service.GetRecommendService;
import com.example.tourapp.service.GetRouteRecommend;
import com.example.tourapp.service.GetRouteService;
import com.example.tourapp.viewAndItem.LoveItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MapFragment extends Fragment implements View.OnClickListener {


    private static final String LEE = "Lee";
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
    public MyBrowse browseData = new MyBrowse();

    public String nickname = null;

    public Integer red = 0xAAFF0000;
    public Integer yellow = 0xAAFFFF00;
    public Integer green = 0xAA00FF00;

    //当前我的位置
    private double[] currentPoint = new double[2];



    public MapFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nickname = getActivity().getIntent().getStringExtra("username");
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
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                MapStatus.Builder builder1 = new MapStatus.Builder();
                builder1.zoom(5.0f);
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://121.37.67.235:8000/app01/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                GroupInterface groupInterface = retrofit.create(GroupInterface.class);
                UserData userData = new UserData();
                userData.setNickname(nickname);

                Gson gson = new Gson();
                RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/ json;charset=utf-8"), gson.toJson(userData));
                System.out.println(gson.toJson(userData));
                groupInterface.HistoryStar(requestBody1).enqueue(new Callback<List<MyLoveData>>() {
                    @Override
                    public void onResponse(Call<List<MyLoveData>> call, Response<List<MyLoveData>> response) {
                        System.out.println("请求1成功" + gson.toJson(response.body()));
                        List<MyLoveData> response1;
                        response1 = response.body();
                        long[] poiStars = new long[response1.size()];
                        if (response1 != null) {
                            if (response1.size() != 0) {
                                poiStars = new long[response1.size()];
                                for (int i = 0; i < response1.size(); i++) {
                                    poiStars[i] = response1.get(i).getPoi();
                                    System.out.println(poiStars[i] + " ");
                                }
                            } else {

                            }
                        } else {

                        }
                        RecommendStars recommendStars = new RecommendStars();
                        recommendStars.setStars(poiStars);
                        System.out.println(gson.toJson(poiStars)+"##");
                        GetRecommendService getRecommendService = retrofit.create(GetRecommendService.class);
                        System.out.println(gson.toJson(recommendStars)+"$$$$");
                        RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), gson.toJson(recommendStars));
                        getRecommendService.getRecommendData(requestBody1).enqueue(new Callback<RecommendReturn>() {
                            @Override
                            public void onResponse(Call<RecommendReturn> call, Response<RecommendReturn> response) {
                                System.out.println("请求2成功");
                                RecommendReturn recommendReturn = response.body();
                                System.out.println(gson.toJson(recommendReturn));

                                List<double[]> places = recommendReturn.getRecommends();
                                long[] pois = recommendReturn.getMap_poi();

                                //筛选地点
                                for (int i = 1; i < places.size(); i++) {
                                    for (int j = 0; j < places.size() - i; j++) {
                                        double first = (places.get(j)[0]-currentPoint[0]) * (places.get(j)[0]-currentPoint[0]) + (places.get(j)[1]-currentPoint[1]) * (places.get(j)[1]-currentPoint[1]);
                                        double second = (places.get(j+1)[0]-currentPoint[0]) * (places.get(j+1)[0]-currentPoint[0]) + (places.get(j+1)[1]-currentPoint[1]) * (places.get(j+1)[1]-currentPoint[1]);
                                        if(first>second){
                                            double[] point = places.get(j+1);
                                            places.set(j+1,places.get(j));
                                            places.set(j, point);
                                            long poi = pois[j+1];
                                            pois[j+1] = pois[j];
                                            pois[j] = poi;
                                        }
                                    }
                                }
                                System.out.println(pois.length);
                                List<Long> poiss = new ArrayList<Long>();
                                for (int i = 0; i < pois.length; i++) {
                                    poiss.add(i,pois[i]);
                                }
                                if (places.size()>20){
                                    for (int i = places.size()-1; i > 20 ; i--) {
                                        System.out.println("#");
                                        places.remove(i);
                                        poiss.remove(i);
                                    }
                                }

                                for (int i = 0; i < places.size(); i++) {
                                    double[] place = places.get(i);
                                    System.out.println("#+" + places.get(i)[0] + places.get(i)[1]);
                                    LatLng point = new LatLng(place[0], place[1]);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("stars", -1);
                                    bundle.putDouble("latitude", place[0]);
                                    bundle.putDouble("longitude", place[1]);
                                    bundle.putLong("poi", poiss.get(i));
                                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(getActivity().getLayoutInflater().inflate(R.layout.marker, null));
                                    OverlayOptions option = new MarkerOptions()
                                            .position(point)
                                            .icon(bitmapDescriptor)
                                            .extraInfo(bundle)
                                            .animateType(MarkerOptions.MarkerAnimateType.none);
                                    mBaiduMap.addOverlay(option);
                                }
                            }

                            @Override
                            public void onFailure(Call<RecommendReturn> call, Throwable t) {
                                System.out.println("2" + t.toString());
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<MyLoveData>> call, Throwable t) {
                        System.out.println("1" + t.toString());
                    }
                });
                break;

            //生成路线
            case R.id.Lypath:
                mBaiduMap.clear();
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                MapStatus.Builder builder2 = new MapStatus.Builder();
                builder2.zoom(5.0f);
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder2.build()));
                GetRouteService getRouteService = (GetRouteService) ServiceCreator_app01.creatService(GetRouteService.class);
                RouteData routeData = new RouteData();
                routeData.setLatitude(currentPoint[0]);
                routeData.setLongitude(currentPoint[1]);
                GroupInterface groupInterface1 = (GroupInterface) ServiceCreator_app01.creatService(GroupInterface.class);
                UserData userData1 = new UserData(nickname);
                Gson gson1 = new Gson();
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), gson1.toJson(userData1));
                groupInterface1.HistoryStar(requestBody).enqueue(new Callback<List<MyLoveData>>() {
                    @Override
                    public void onResponse(Call<List<MyLoveData>> call, Response<List<MyLoveData>> response) {
                        System.out.println("我的点赞在路线成功");
                        System.out.println(gson1.toJson(response.body()) + "%%");
                        List<MyLoveData> response1 = new ArrayList<MyLoveData>();
                        response1 = response.body();
                        long[] poiStars = null;
                        if (response1 != null) {
                            if (response1.size() != 0) {
                                poiStars = new long[response1.size()];
                                for (int i = 0; i < response1.size(); i++) {
                                    poiStars[i] = response1.get(i).getPoi();
                                }
                            }
                        }
                        routeData.setStars(poiStars);
                        Gson gson = new Gson();
                        String s = gson.toJson(routeData);
                        System.out.println("##" + s);
                        RequestBody requestBodye = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), s);
                        getRouteService.getRoute(requestBodye).enqueue(new Callback<RouteRecommendFirst>() {
                            @Override
                            public void onResponse(Call<RouteRecommendFirst> call, Response<RouteRecommendFirst> response) {
                                System.out.println("生成路线请求1" + gson.toJson(response.body()));
                                //if(response != null){
                                List<List<double[]>> response1 = response.body().getAllRoute();
                                List<long[]> map_poi = response.body().getMap_poi();
                                RouteFirstResponse routeFirstResponse = new RouteFirstResponse();
                                routeFirstResponse.setRoute(new ArrayList<List<RouteFirstPlace>>());
                                List<List<RouteFirstPlace>> route = routeFirstResponse.getRoute();
                                if (response1 != null) {
                                    if (response1.size() != 0) {
                                        for (int i = 0; i < response1.size(); i++) {
                                            List<RouteFirstPlace> places = new ArrayList<RouteFirstPlace>();
                                            for (int j = 0; j < response1.get(i).size(); j++) {
                                                RouteFirstPlace routeFirstPlace = new RouteFirstPlace();
                                                routeFirstPlace.setLatitude(response1.get(i).get(j)[0]);
                                                routeFirstPlace.setLongitude(response1.get(i).get(j)[1]);
                                                routeFirstPlace.setPoi(map_poi.get(i)[j]);
                                                places.add(routeFirstPlace);
                                            }
                                            route.add(places);
                                        }
                                        Retrofit retrofit1 = new Retrofit.Builder()
                                                .baseUrl("http://47.107.38.208:8090/attractions/")
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        GetRouteRecommend getRouteRecommend = retrofit1.create(GetRouteRecommend.class);
                                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), gson.toJson(routeFirstResponse));
                                        System.out.println(gson.toJson(routeFirstResponse));
                                        getRouteRecommend.getRouteRecommend(requestBody).enqueue(new Callback<RouteRespose>() {
                                            @Override
                                            public void onResponse(Call<RouteRespose> call, Response<RouteRespose> response) {
                                                System.out.println("生成路线请求2"+gson.toJson(response.body()));
                                                List<OverlayOptions> options = new ArrayList<OverlayOptions>();
                                                List<LatLng> latLngList = new ArrayList<LatLng>();

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
                                                        DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
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

                                                if (response.body() != null) {
                                                    RouteRespose routeRespose = response.body();
                                                    System.out.println(response.body());
                                                    RouteListData data1 = routeRespose.getData();
                                                    List<List<RoutePlace>> routes = data1.getRoute();
                                                    List<PlanNode> planNodeList = new ArrayList<PlanNode>();

                                                    long num1 = 0;
                                                    long num2 = 0;
                                                    long num3 = 0;
                                                    for (int i = 0; i < routes.get(0).size(); i++) {
                                                        num1 = num1 + routes.get(0).get(i).getStars();
                                                    }
                                                    for (int i = 0; i < routes.get(1).size(); i++) {
                                                        num2 = num1 + routes.get(1).get(i).getStars();
                                                    }
                                                    for (int i = 0; i < routes.get(2).size(); i++) {
                                                        num3 = num1 + routes.get(2).get(i).getStars();
                                                    }
                                                    List<RoutePlace> routePlaceList = new ArrayList<RoutePlace>();
                                                    long[] stars = new long[3];
                                                    stars[0] = num1;
                                                    stars[1] = num2;
                                                    stars[2] = num3;
                                                    if(stars[0]>stars[1]){
                                                        long s = stars[0];
                                                        stars[0] = stars[1];
                                                        stars[1] = s;
                                                        routePlaceList = routes.get(0);
                                                        routes.set(0, routes.get(1));
                                                        routes.set(1, routePlaceList);
                                                    }
                                                    if(stars[1]>stars[2]){
                                                        long s = stars[1];
                                                        stars[1] = stars[2];
                                                        stars[2] = s;
                                                        routePlaceList = routes.get(1);
                                                        routes.set(1, routes.get(2));
                                                        routes.set(2, routePlaceList);
                                                    }
                                                    if(stars[1]>stars[2]){
                                                        long s = stars[1];
                                                        stars[1] = stars[2];
                                                        stars[2] = s;
                                                        routePlaceList = routes.get(1);
                                                        routes.set(1, routes.get(2));
                                                        routes.set(2, routePlaceList);
                                                    }

                                                    View view1 = getActivity().getLayoutInflater().inflate(R.layout.routemarker,null);
                                                    View view2 = getActivity().getLayoutInflater().inflate(R.layout.routemarker,null);
                                                    View view3 = getActivity().getLayoutInflater().inflate(R.layout.routemarker,null);

                                                    BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.fromView(view1);
                                                    BitmapDescriptor bitmapDescriptor2 = BitmapDescriptorFactory.fromView(view2);
                                                    BitmapDescriptor bitmapDescriptor3 = BitmapDescriptorFactory.fromView(view3);

                                                    List<LatLng> points = new ArrayList<LatLng>();
                                                    Timer timer1 = new Timer();
                                                    TimerTask task1 = new Task1(routes, mBaiduMap, timer1, mapView,view1,bitmapDescriptor1);
                                                    timer1.schedule(task1, 0, 400);

                                                    Timer timer2 = new Timer();
                                                    TimerTask task2 = new Task2(routes, mBaiduMap, timer2, mapView,view2,bitmapDescriptor2);
                                                    timer2.schedule(task2, 0, 400);

                                                    Timer timer3 = new Timer();
                                                    TimerTask task3 = new Task3(routes, mBaiduMap, timer3, mapView, view3,bitmapDescriptor3);
                                                    timer3.schedule(task3, 0, 400);
                                                } else {
                                                    Toast.makeText(getContext(), "生成路线为null", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<RouteRespose> call, Throwable t) {
                                                System.out.println("生成路线请求2失败");
                                                System.out.println(t.toString());
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getContext(), "无推荐路线", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "无推荐路线", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RouteRecommendFirst> call, Throwable t) {
                                System.out.println("生成路线请求1失败");
                                System.out.println(t.toString());
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<MyLoveData>> call, Throwable t) {
                        System.out.println("我的点赞在生成路线中失败");
                        System.out.println(t.toString());
                    }
                });
                break;

            //生成附近景点
            case R.id.LyAttra:
                mBaiduMap.clear();
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }

                GetNearlyAttra getNearlyAttra = (GetNearlyAttra) ServiceCreator_user.creatService(GetNearlyAttra.class);
                getNearlyAttra.getNearlyAttra(String.valueOf(currentPoint[0]), String.valueOf(currentPoint[1])).
                        enqueue(new Callback<NearlyAttra>() {
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
                                List<double[]> nearlypoint = realPoints;
                                double[] nearlypoint1 = new double[3];
                                MapStatus.Builder builder = new MapStatus.Builder();
                                builder.zoom(16.0f);
                                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                                for (int i = 0; i < nearlypoint.size(); i++) {
                                    nearlypoint1 = nearlypoint.get(i);
                                    LatLng point = new LatLng(nearlypoint1[0], nearlypoint1[1]);
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("poi", (long) (nearlypoint1[2]));
                                    bundle.putLong("stars", -1);
                                    bundle.putDouble("latitude", nearlypoint1[0]);
                                    bundle.putDouble("longitude", nearlypoint1[1]);
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
                                Toast.makeText(getContext(), "附近景点网络请求失败", Toast.LENGTH_SHORT).show();
                                System.out.println(t.toString());
                            }
                        });
                break;
            //点赞
            case R.id.heart:
                ClickLoveService clickLoveService = (ClickLoveService) ServiceCreator_app01.creatService(ClickLoveService.class);
                if (flag) {
                    //点赞
                    heartiv.setImageDrawable(getResources().getDrawable(R.drawable.heart1, null));
                    clickMarker.setTimestamp(getTime());
                    clickMarker.setStar(1);
                    //post请求传输点赞数据
                    String clickMarkerJson = new Gson().toJson(clickMarker);
                    System.out.println(clickMarkerJson + "点赞json");

                    RequestBody requestBodyClickLove = RequestBody.create(MediaType.get("application/json; charset=utf-8"), clickMarkerJson);
                    clickLoveService.clickLove(requestBodyClickLove).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            flag = false;
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                        }
                    });

                } else {
                    //取消点赞
                    heartiv.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                    clickMarker.setTimestamp(getTime());
                    clickMarker.setStar(0);
                    //post传输点赞数据
                    String clickMarkerJson1 = new Gson().toJson(clickMarker);
                    System.out.println(clickMarkerJson1 + "点赞json");

                    RequestBody requestBodyClickLove2 = RequestBody.create(MediaType.get("application/json; charset=utf-8"), clickMarkerJson1);
                    clickLoveService.clickLove(requestBodyClickLove2).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            flag = true;
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                        }
                    });
                }
                break;
            default:
                break;
        }

        //设置marker监听事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    pop_upsView.setVisibility(View.GONE);
                } else {
                    pop_upsView.setVisibility(View.VISIBLE);
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                heartiv.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                flag = true;

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new AddCookiesInterceptor())
                        .addInterceptor(new ReceivedCookiesInterceptor())
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://121.37.67.235:8000/app01/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();

                GroupInterface groupInterface = retrofit.create(GroupInterface.class);
                Gson gson = new Gson();
                String nicknameJson = gson.toJson(nickname);

                UserData userData = new UserData();
                userData.setNickname(nickname);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), gson.toJson(userData));
                Call<List<MyLoveData>> historyStarCall = groupInterface.HistoryStar(requestBody);
                historyStarCall.enqueue(new Callback<List<MyLoveData>>() {
                    @Override
                    public void onResponse(Call<List<MyLoveData>> call, Response<List<MyLoveData>> response) {
                        List<MyLoveData> data = response.body();
                        long poi = marker.getExtraInfo().getLong("poi");
                        System.out.println(poi+"@");
                        if (data != null) {
                            long[] myLovepoi = new long[data.size()];
                            for (int i = 0; i < data.size(); i++) {
                                myLovepoi[i] = data.get(i).getPoi();
                                double lan = (double) marker.getExtraInfo().get("latitude");
                                double lon = (double) marker.getExtraInfo().get("longitude");
                                if (lan == data.get(i).getLatitude() && lon == data.get(i).getLongitude()) {
                                    heartiv.setImageDrawable(getResources().getDrawable(R.drawable.heart1, null));
                                    flag = false;
                                }
                            }

                        }
                        System.out.println("我的点赞数据请求成功");
                    }

                    @Override
                    public void onFailure(Call<List<MyLoveData>> call, Throwable t) {
                        System.out.println("我的点赞数据请求失败！");
                        System.out.println(t.getMessage());
                    }
                });
                //记录marker信息
                long poi = marker.getExtraInfo().getLong("poi");
                clickMarker.setNickname(nickname);
                clickMarker.setLat(marker.getPosition().latitude);
                clickMarker.setLon(marker.getPosition().longitude);
                clickMarker.setPoi(poi);

                browseData.setNickname(MainActivity.instance.getNickname());
                browseData.setLat(marker.getPosition().latitude);
                browseData.setLon(marker.getPosition().longitude);
                browseData.setPoi(poi);
                browseData.setTimestamp(getTime());
                String browDataJson = new Gson().toJson(browseData);

                ClickMarkerService clickMarkerService = (ClickMarkerService) ServiceCreator_app01.creatService(ClickMarkerService.class);
                RequestBody requestBody1 = RequestBody.create(MediaType.get("application/json; charset=utf-8"), browDataJson);
                clickMarkerService.sendMyBrowse(requestBody1).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        System.out.println("marker点击网络请求成功");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println("marker点击网络请求失败:" + t.toString());
                    }
                });
                com.github.siyamed.shapeimageview.RoundedImageView imageView1 = pop_upsView.findViewById(R.id.detailImage1);
                com.github.siyamed.shapeimageview.RoundedImageView imageView2 = pop_upsView.findViewById(R.id.detailImage2);
                com.github.siyamed.shapeimageview.RoundedImageView imageView3 = pop_upsView.findViewById(R.id.detailImage3);
                com.github.siyamed.shapeimageview.RoundedImageView imageView4 = pop_upsView.findViewById(R.id.detailImage4);
                com.github.siyamed.shapeimageview.RoundedImageView imageView5 = pop_upsView.findViewById(R.id.detailImage5);
                TextView textView = pop_upsView.findViewById(R.id.detailtext);
                TextView textViewName = pop_upsView.findViewById(R.id.place_tv);

                //设置地点和详细信息和图片
                PoiPhoto poiPhoto = new PoiPhoto();
                Long poi1 = marker.getExtraInfo().getLong("poi");
                String poiStr = poi1.toString();
                Detail detail1 = (Detail) poiPhoto.detail.get(poiStr);
                if(detail1 != null){
                    System.out.println(detail1.getName());
                    textViewName.setText(detail1.getName());
                    textView.setText(detail1.getDetail());
                    imageView1.setImageResource(detail1.getPhotos()[0]);
                    imageView2.setImageResource(detail1.getPhotos()[1]);
                    imageView3.setImageResource(detail1.getPhotos()[2]);
                    imageView4.setImageDrawable(null);
                    imageView5.setImageDrawable(null);
                }else {
                    GeoCoder geoCoder = GeoCoder.newInstance();
                    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
                        @Override
                        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                        }

                        @Override
                        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                            TextView textView = pop_upsView.findViewById(R.id.place_tv);
                            textView.setText(reverseGeoCodeResult.getAddress());
                        }
                    };
                    geoCoder.setOnGetGeoCodeResultListener(listener);
                    geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                            .language(LanguageType.LanguageTypeChinese)
                            .location(marker.getPosition())
                            .newVersion(1));
                    long stars = (long) marker.getExtraInfo().get("stars");
//                    if (stars != -1) {
//                        TextView number_tv = pop_upsView.findViewById(R.id.number_tv);
//                        number_tv.setText("人数 : " + stars);
//                        number_tv.setVisibility(View.VISIBLE);
//                    }
                    Photo photo = new Photo();
                    Random random = new Random();
                    int number = random.nextInt(100);
                    int num = number % 4;
                    imageView1.setImageResource(photo.getListGroup().get(num)[0]);
                    imageView2.setImageResource(photo.getListGroup().get(num)[1]);
                    imageView3.setImageResource(photo.getListGroup().get(num)[2]);
                    imageView4.setImageResource(photo.getListGroup().get(num)[3]);
                    imageView5.setImageResource(photo.getListGroup().get(num)[4]);
                    textView.setText(photo.getTexts().get(num));
                    geoCoder.destroy();
                }
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
        //禁止地图旋转
        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setRotateGesturesEnabled(false);
        //开启地图定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //显示当前的位置
        currentPoint[0] = 30.263607974299997;
        currentPoint[1] = -97.7395677567;
        LatLng latLng = new LatLng(currentPoint[0], currentPoint[1]);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(update);
        MyLocationData myLocationData = new MyLocationData.Builder().latitude(currentPoint[0]).longitude(currentPoint[1]).build();
        mBaiduMap.setMyLocationData(myLocationData);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(5.0f).target(latLng);
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
        //设置双击放大
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
            if (isAdded()) {

            }
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
        int num = 0;
        List<double[]> realPoints = new ArrayList<double[]>();
        double[] point;
        for (int i = 0; i < points.size(); i++) {
            point = points.get(i);
            if ((point[0] - currentPoint[0]) < (-0.25) || (point[0] - currentPoint[0]) > 0.25 || (point[1] - currentPoint[1]) > 0.4 || (point[1] - currentPoint[1]) < (-0.4)) {
                points.remove(i);
                num = num + 1;
            }
            if (num > 10){
                break;
            }
        }
        realPoints.addAll(points);
        return realPoints;
    }

    //定位到某一位置
    public void currentLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(update);
    }
}