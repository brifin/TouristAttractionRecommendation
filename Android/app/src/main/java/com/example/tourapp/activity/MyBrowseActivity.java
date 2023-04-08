package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.base.LanguageType;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.tourapp.Photo;
import com.example.tourapp.R;
import com.example.tourapp.adapter.BrowseAdapter;
import com.example.tourapp.data.MyLoveData;
import com.example.tourapp.data.MyLoveDataArray;
import com.example.tourapp.data.User;
import com.example.tourapp.data.UserData;
import com.example.tourapp.httpInterface.GroupInterface;
import com.example.tourapp.interceptor.AddCookiesInterceptor;
import com.example.tourapp.interceptor.ReceivedCookiesInterceptor;
import com.example.tourapp.viewAndItem.BrowseItem;
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

public class MyBrowseActivity extends AppCompatActivity {

    private List<BrowseItem> mData = new ArrayList<BrowseItem>();;
    private BrowseAdapter adapter;
    private ListView listView;
    private String nickname;

    private GeoCoder mgeoCoder = GeoCoder.newInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_browse);
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
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupInterface groupInterface = retrofit.create(GroupInterface.class);
        Gson gson = new Gson();

        UserData user = new UserData();
        user.setNickname(nickname);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),gson.toJson(user));
        Call<List<MyLoveData>> historyViewCall = groupInterface.HistoryView(requestBody);
        historyViewCall.enqueue(new Callback<List<MyLoveData>>() {
            @Override
            public void onResponse(Call<List<MyLoveData>> call, Response<List<MyLoveData>> response) {
                System.out.println("我的浏览请求成功");
                List<MyLoveData> response1 = new ArrayList<MyLoveData>();
                response1 = response.body();
                Photo photo = new Photo();
                if (response1 != null) {
                    if (response1.size() != 0) {
                        for (int i = 0; i < response1.size(); i++) {
                            BrowseItem browseItem = new BrowseItem();
                            browseItem.setLatitude(response1.get(i).getLatitude());
                            browseItem.setLongitude(response1.get(i).getLongitude());
                            browseItem.setPoi(response1.get(i).getPoi());
                            browseItem.setTimestamp(response1.get(i).getTimestamp());
                            browseItem.setPhoto(photo.getListGroup().get(i%4)[i%5]);
                            mData.add(browseItem);
                        }
                    }
                }
                OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                    }

                    //逆地理编码
                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                        for (int i = 0; i < mData.size(); i++) {
                            if (String.format("%.5f",mData.get(i).getLatitude()).equals(String.format("%.5f",reverseGeoCodeResult.getLocation().latitude)) &&String.format("%.5f",mData.get(i).getLongitude()).equals(String.format("%.5f", reverseGeoCodeResult.getLocation().longitude))) {
                                mData.get(i).setPlace(reverseGeoCodeResult.getAddress());
                                System.out.println("++");
                            }
                            if (i==(mData.size()-1)){
                                adapter = new BrowseAdapter(mData);
                                listView = (ListView) findViewById(R.id.browse_listView);
                                listView.setAdapter(adapter);
                            }
                        }

                    }
                };
                mgeoCoder.setOnGetGeoCodeResultListener(listener);
                for (int i = 0; i < mData.size(); i++) {
                    LatLng latLng = new LatLng(mData.get(i).getLatitude(), mData.get(i).getLongitude());
                    mgeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng).newVersion(1).language(LanguageType.LanguageTypeChinese));
                }
            }

            @Override
            public void onFailure(Call<List<MyLoveData>> call, Throwable t) {
                System.out.println("请求失败！");
                Log.e("YANG", t.getMessage());
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mgeoCoder.destroy();
    }
}