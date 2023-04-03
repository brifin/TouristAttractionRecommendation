package com.example.tourapp.fragment;

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
import androidx.fragment.app.Fragment;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
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
import com.example.tourapp.R;
import com.example.tourapp.ServiceCreator_app01;
import com.example.tourapp.ServiceCreator_attractions;
import com.example.tourapp.ServiceCreator_user;

import com.example.tourapp.activity.LoginActivity;
import com.example.tourapp.activity.MainActivity;
import com.example.tourapp.activity.MyLoveActivity2;
import com.example.tourapp.data.Attraction1;
import com.example.tourapp.data.Click_love;
import com.example.tourapp.data.Data;
import com.example.tourapp.data.MyBrowse;
import com.example.tourapp.data.MyLoveData;
import com.example.tourapp.data.MyLoveDataArray;
import com.example.tourapp.data.NearlyAttra;
import com.example.tourapp.data.Place;
import com.example.tourapp.data.RecommendReturn;
import com.example.tourapp.data.RecommendStars;
import com.example.tourapp.data.Result;
import com.example.tourapp.data.Route1;
import com.example.tourapp.data.Route1Attraction1;
import com.example.tourapp.data.Route1Attraction2;
import com.example.tourapp.data.Route1Attraction3;
import com.example.tourapp.data.Route1Attraction4;
import com.example.tourapp.data.Route2;
import com.example.tourapp.data.Route2Attraction1;
import com.example.tourapp.data.Route2Attraction2;
import com.example.tourapp.data.Route2Attraction3;
import com.example.tourapp.data.Route2Attraction4;
import com.example.tourapp.data.Route3;
import com.example.tourapp.data.Route3Attraction1;
import com.example.tourapp.data.Route3Attraction2;
import com.example.tourapp.data.Route3Attraction3;
import com.example.tourapp.data.Route3Attraction4;
import com.example.tourapp.data.RouteData;
import com.example.tourapp.data.RouteRe;
import com.example.tourapp.data.RouteRecommend;
import com.example.tourapp.data.RouteRecommendData;
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
import java.util.Date;
import java.util.List;
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

    public String nickname = MainActivity.nickname;

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
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://121.37.67.235:8000/app01/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                GroupInterface groupInterface = retrofit.create(GroupInterface.class);
                UserData userData = new UserData();
                userData.setNickname(nickname);

                Gson gson = new Gson();
                //RequestBody requestBody = RequestBody.create(MediaType.parse("application/ json;charset=utf-8"), gson.toJson(userData));
                groupInterface.HistoryStar(userData).enqueue(new Callback<List<MyLoveData>>() {
                    @Override
                    public void onResponse(Call<List<MyLoveData>> call, Response<List<MyLoveData>> response) {
                        System.out.println("请求1成功");
                        //MyLoveDataArray loveDataArray = response.body();
                        List<MyLoveData> response1;
                        response1 = response.body();
                        //MyLoveData[] data = null;
                        long[] poiStars = new long[response1.size()];
                        if (response1 != null) {
                            if (response1.size() != 0) {

                                for (int i = 0; i < response1.size(); i++) {
                                    poiStars[i] = response1.get(i).getPoi();
                                }
                            } else {

                            }
                        } else {

                        }

//                        long[] poiStars = new long[6];
//                        poiStars[0] = 1;
//                        poiStars[1] = 2;
//                        poiStars[2] = 3;
//                        poiStars[3] = 4;
//                        poiStars[4] = 5;
//                        poiStars[5] = 6;
                        RecommendStars recommendStars = new RecommendStars();
                        recommendStars.setStars(poiStars);
                        GetRecommendService getRecommendService = retrofit.create(GetRecommendService.class);
                        RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), gson.toJson(recommendStars));
                        getRecommendService.getRecommendData(requestBody1).enqueue(new Callback<RecommendReturn>() {
                            @Override
                            public void onResponse(Call<RecommendReturn> call, Response<RecommendReturn> response) {
                                System.out.println("请求2成功");
                                RecommendReturn recommendReturn = response.body();
                                List<double[]> places = recommendReturn.getRecommends();
                                for (int i = 0; i < places.size(); i++) {
                                    double[] place = places.get(i);
                                    LatLng point = new LatLng(place[0], place[1]);
                                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.baidumarker2);
                                    OverlayOptions option = new MarkerOptions()
                                            .position(point)
                                            .icon(bitmapDescriptor)
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
                GetRouteService getRouteService = (GetRouteService) ServiceCreator_app01.creatService(GetRouteService.class);
                RouteData routeData = new RouteData();
                routeData.setLatitude(currentPoint[0]);
                routeData.setLongitude(currentPoint[1]);

                //----
                GroupInterface groupInterface1 = (GroupInterface) ServiceCreator_app01.creatService(GroupInterface.class);
                UserData userData1 = new UserData(nickname);
                groupInterface1.HistoryStar(userData1).enqueue(new Callback<List<MyLoveData>>() {
                    @Override
                    public void onResponse(Call<List<MyLoveData>> call, Response<List<MyLoveData>> response) {
                        List<MyLoveData> response1 = new ArrayList<MyLoveData>();
                        response1 = response.body();
                        long[] poiStars = new long[response1.size()];
                        if (response1 != null) {
                            if (response1.size() != 0) {
                                for (int i = 0; i < response1.size(); i++) {
                                    poiStars[i] = response1.get(i).getPoi();
                                }
                            }
                        }
//                        long[] poiStars = new long[6];
//                        poiStars[0] = 1;
//                        poiStars[1] = 2;
//                        poiStars[2] = 3;
//                        poiStars[3] = 4;
//                        poiStars[4] = 5;
//                        poiStars[5] = 6;
                        routeData.setStars(poiStars);
                        Gson gson = new Gson();
                        String s = gson.toJson(routeData);
                        RequestBody requestBodye = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), s);
                        getRouteService.getRoute(requestBodye).enqueue(new Callback<List<List<double[]>>>() {
                            @Override
                            public void onResponse(Call<List<List<double[]>>> call, Response<List<List<double[]>>> response) {
                                List<List<double[]>> response1 = response.body();
                                if (response1 != null) {
                                    if (response1.size() != 0) {
                                        RouteRe routeRe = new RouteRe();
                                        Route1 route1 = new Route1();
                                        Route2 route2 = new Route2();
                                        Route3 route3 = new Route3();
                                        Route1Attraction1 route1Attraction1 = new Route1Attraction1();
                                        Route1Attraction2 route1Attraction2 = new Route1Attraction2();
                                        Route1Attraction3 route1Attraction3 = new Route1Attraction3();
                                        Route1Attraction4 route1Attraction4 = new Route1Attraction4();
                                        Route2Attraction1 route2Attraction1 = new Route2Attraction1();
                                        Route2Attraction2 route2Attraction2 = new Route2Attraction2();
                                        Route2Attraction3 route2Attraction3 = new Route2Attraction3();
                                        Route2Attraction4 route2Attraction4 = new Route2Attraction4();
                                        Route3Attraction1 route3Attraction1 = new Route3Attraction1();
                                        Route3Attraction2 route3Attraction2 = new Route3Attraction2();
                                        Route3Attraction3 route3Attraction3 = new Route3Attraction3();
                                        Route3Attraction4 route3Attraction4 = new Route3Attraction4();
                                        route1Attraction1.setLatitude(String.valueOf(response1.get(0).get(0)[0]));
                                        route1Attraction1.setLongitude(String.valueOf(response1.get(0).get(0)[1]));

                                        route1Attraction2.setLatitude(String.valueOf(response1.get(0).get(1)[0]));
                                        route1Attraction2.setLongitude(String.valueOf(response1.get(0).get(1)[1]));

                                        route1Attraction3.setLatitude(String.valueOf(response1.get(0).get(2)[0]));
                                        route1Attraction3.setLongitude(String.valueOf(response1.get(0).get(2)[1]));

                                        route1Attraction4.setLatitude(String.valueOf(response1.get(0).get(3)[0]));
                                        route1Attraction4.setLongitude(String.valueOf(response1.get(0).get(3)[1]));
                                        //
                                        route2Attraction1.setLatitude(String.valueOf(response1.get(1).get(0)[0]));
                                        route2Attraction1.setLongitude(String.valueOf(response1.get(1).get(0)[1]));

                                        route2Attraction2.setLatitude(String.valueOf(response1.get(1).get(1)[0]));
                                        route2Attraction2.setLongitude(String.valueOf(response1.get(1).get(1)[1]));

                                        route2Attraction3.setLatitude(String.valueOf(response1.get(1).get(2)[0]));
                                        route2Attraction3.setLongitude(String.valueOf(response1.get(1).get(2)[1]));

                                        route2Attraction4.setLatitude(String.valueOf(response1.get(1).get(3)[0]));
                                        route2Attraction4.setLongitude(String.valueOf(response1.get(1).get(3)[1]));
                                        //
                                        route3Attraction1.setLatitude(String.valueOf(response1.get(2).get(0)[0]));
                                        route3Attraction1.setLongitude(String.valueOf(response1.get(2).get(0)[1]));

                                        route3Attraction2.setLatitude(String.valueOf(response1.get(2).get(1)[0]));
                                        route3Attraction2.setLongitude(String.valueOf(response1.get(2).get(1)[1]));

                                        route3Attraction3.setLatitude(String.valueOf(response1.get(2).get(2)[0]));
                                        route3Attraction3.setLongitude(String.valueOf(response1.get(2).get(2)[1]));

                                        route3Attraction4.setLatitude(String.valueOf(response1.get(2).get(3)[0]));
                                        route3Attraction4.setLongitude(String.valueOf(response1.get(2).get(3)[1]));
                                        //
                                        route1.setAttraction1(route1Attraction1);
                                        route1.setAttraction2(route1Attraction2);
                                        route1.setAttraction3(route1Attraction3);
                                        route1.setAttraction4(route1Attraction4);

                                        route2.setAttraction1(route2Attraction1);
                                        route2.setAttraction2(route2Attraction2);
                                        route2.setAttraction3(route2Attraction3);
                                        route2.setAttraction4(route2Attraction4);

                                        route3.setAttraction1(route3Attraction1);
                                        route3.setAttraction2(route3Attraction2);
                                        route3.setAttraction3(route3Attraction3);
                                        route3.setAttraction4(route3Attraction4);

                                        routeRe.setRoute1(route1);
                                        routeRe.setRoute2(route2);
                                        routeRe.setRoute3(route3);

                                        Retrofit retrofit1 = new Retrofit.Builder()
                                                .baseUrl("http://47.107.38.208:8090/attractions/")
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        GetRouteRecommend getRouteRecommend = retrofit1.create(GetRouteRecommend.class);
                                        System.out.println(gson.toJson(routeRe) + "#");
                                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), gson.toJson(routeRe));
                                        getRouteRecommend.getRouteRecommend(requestBody).enqueue(new Callback<RouteRecommend>() {
                                            @Override
                                            public void onResponse(Call<RouteRecommend> call, Response<RouteRecommend> response) {
                                                System.out.println(gson.toJson(response.body()));
                                                if (response.body() != null) {
                                                    RouteRecommend routeRecommend = response.body();
                                                    Data data1 = routeRecommend.getData();
                                                    List<OverlayOptions> options = new ArrayList<OverlayOptions>();
                                                    List<LatLng> latLngList = new ArrayList<LatLng>();
                                                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.baidumarker2);
//
                                                    LatLng latLng1 = new LatLng(data1.getAttraction1().getLatitude(), data1.getAttraction1().getLongitude());
                                                    latLngList.add(latLng1);
                                                    OverlayOptions options1 = new MarkerOptions().position(latLng1).icon(bitmapDescriptor);
                                                    options.add(options1);

                                                    LatLng latLng2 = new LatLng(data1.getAttraction2().getLatitude(), data1.getAttraction2().getLongitude());
                                                    latLngList.add(latLng2);
                                                    OverlayOptions options2 = new MarkerOptions().position(latLng2).icon(bitmapDescriptor);
                                                    options.add(options2);

                                                    LatLng latLng3 = new LatLng(data1.getAttraction3().getLatitude(), data1.getAttraction3().getLongitude());
                                                    latLngList.add(latLng3);
                                                    OverlayOptions options3 = new MarkerOptions().position(latLng3).icon(bitmapDescriptor);
                                                    options.add(options3);

                                                    LatLng latLng4 = new LatLng(data1.getAttraction4().getLatitude(), data1.getAttraction4().getLongitude());
                                                    latLngList.add(latLng4);
                                                    OverlayOptions options4 = new MarkerOptions().position(latLng4).icon(bitmapDescriptor);
                                                    options.add(options4);

                                                    mBaiduMap.addOverlays(options);
                                                    OverlayOptions mOverlayOptions = new PolylineOptions().width(5).color(0xAAFF0000).points(latLngList);
                                                    Overlay overlay = mBaiduMap.addOverlay(mOverlayOptions);

                                                } else {
                                                    Toast.makeText(getContext(), "生成路线为null", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<RouteRecommend> call, Throwable t) {
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
                            public void onFailure(Call<List<List<double[]>>> call, Throwable t) {
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
                            bundle.putLong("poi", (long) (nearlypoint1[2]));
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
                }

                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                heartiv.setImageDrawable(getResources().getDrawable(R.drawable.heart));

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
                Call<List<MyLoveData>> historyStarCall = groupInterface.HistoryStar(userData);
                historyStarCall.enqueue(new Callback<List<MyLoveData>>() {
                    @Override
                    public void onResponse(Call<List<MyLoveData>> call, Response<List<MyLoveData>> response) {
                        List<MyLoveData> data = response.body();
                        long poi = marker.getExtraInfo().getLong("poi");
                        if (data != null) {
                            long[] myLovepoi = new long[data.size()];
                            for (int i = 0; i < data.size(); i++) {
                                myLovepoi[i] = data.get(i).getPoi();
                                if (poi == data.get(i).getPoi()) {
                                    heartiv.setImageDrawable(getResources().getDrawable(R.drawable.heart1, null));
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
                clickMarker.setNickname(MainActivity.nickname);
                clickMarker.setLat(marker.getPosition().latitude);
                clickMarker.setLon(marker.getPosition().longitude);
                clickMarker.setPoi(poi);

                browseData.setNickname(MainActivity.nickname);
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
        builder.zoom(15.0f).target(latLng);
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
        double[] point;
        for (int i = 0; i < points.size(); i++) {
            point = points.get(i);
            if ((point[0] - currentPoint[0]) > (-0.25) && (point[0] - currentPoint[0]) < 0.25 && (point[1] - currentPoint[1]) < 0.4 && (point[1] - currentPoint[1]) > (-0.4)) {
                realPoints.add(point);
            }
        }
        return realPoints;
    }

    //定位到某一位置
    public void currentLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(update);
    }
}