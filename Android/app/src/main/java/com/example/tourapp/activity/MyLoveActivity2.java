package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.base.LanguageType;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.tourapp.R;
import com.example.tourapp.adapter.LoveAdapter;
import com.example.tourapp.data.MyLoveData;
import com.example.tourapp.data.MyLoveDataArray;
import com.example.tourapp.data.UserData;
import com.example.tourapp.httpInterface.GroupInterface;
import com.example.tourapp.interceptor.AddCookiesInterceptor;
import com.example.tourapp.interceptor.ReceivedCookiesInterceptor;
import com.example.tourapp.viewAndItem.LoveItem;
import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyLoveActivity2 extends AppCompatActivity {

    public static List<LoveItem> mData = new ArrayList<LoveItem>();
    ;
    private ListView listView;
    public LoveAdapter adapter;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_love2);
        mData.clear();
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        hideStable();

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

        UserData userData = new UserData();
        userData.setNickname(nickname);

        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/ json;charset=utf-8"), gson.toJson(userData));
        Call<List<MyLoveData[]>> historyStarCall = groupInterface.HistoryStar(userData);
        historyStarCall.enqueue(new Callback<List<MyLoveData[]>>() {
            @Override
            public void onResponse(Call<List<MyLoveData[]>> call, Response<List<MyLoveData[]>> response) {
                // MyLoveDataArray loveData = response.body();
                List<MyLoveData[]> response1 = new ArrayList<MyLoveData[]>();
                response1 = response.body();
                MyLoveData[] data = null;
                if (response1!=null){
                    if (response1.size() != 0) {
                        data = response1.get(0);
                    } else {
                        data = new MyLoveData[0];
                    }
                }
                else {
                    data = new MyLoveData[0];
                }
                for (int i = 0; i < data.length; i++) {

                    LoveItem loveItem = new LoveItem();
                    loveItem.setLatitude(data[i].getLatitude());
                    loveItem.setLongitude(data[i].getLongitude());
                    loveItem.setPoi(data[i].getPoi());
                    loveItem.setTimestamp(data[i].getTimestamp());
                    mData.add(loveItem);
                }

                GeoCoder mgeoCoder = GeoCoder.newInstance();
                OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                    }
                    //逆地理编码
                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                        System.out.println(reverseGeoCodeResult.getAddress());
                        for (int i = 0; i < mData.size(); i++) {
                            if (mData.get(i).getLatitude() == reverseGeoCodeResult.getLocation().latitude && mData.get(i).getLongitude() == reverseGeoCodeResult.getLocation().longitude) {
                                mData.get(i).setPlace(reverseGeoCodeResult.getAddress());
                            }
                        }
                    }
                };
                adapter = new LoveAdapter(mData);
                listView = (ListView) findViewById(R.id.love_listView);
                listView.setAdapter(adapter);
                System.out.println("我的点赞数据请求成功");

            }
            @Override
            public void onFailure(Call<List<MyLoveData[]>> call, Throwable t) {
                System.out.println("我的点赞数据请求失败！");
                System.out.println(t.getMessage());
            }
        });

    }
    //隐藏状态栏
    public void hideStable() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ImmersionBar.with(this)
                .transparentBar()
                .statusBarDarkFont(true)
                .statusBarAlpha(0.0f)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }
}