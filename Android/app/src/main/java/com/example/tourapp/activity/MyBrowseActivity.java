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
import com.example.tourapp.R;
import com.example.tourapp.adapter.BrowseAdapter;
import com.example.tourapp.data.MyLoveData;
import com.example.tourapp.data.MyLoveDataArray;
import com.example.tourapp.data.User;
import com.example.tourapp.data.UserData;
import com.example.tourapp.httpInterface.GroupInterface;
import com.example.tourapp.viewAndItem.BrowseItem;
import com.example.tourapp.viewAndItem.LoveItem;
import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyBrowseActivity extends AppCompatActivity {

    private List<BrowseItem> mData;
    private BrowseAdapter adapter;
    private ListView listView;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_browse);
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        initView();
    }

    public void initView() {
        hideStable();
        mData = new ArrayList<BrowseItem>();
        getData();
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

    //网络请求获取数据
    public void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://121.37.67.235:8000/app01/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupInterface groupInterface = retrofit.create(GroupInterface.class);
        Gson gson = new Gson();

        UserData user = new UserData();
        user.setNickname(nickname);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),gson.toJson(user));
        Call<MyLoveDataArray> historyViewCall = groupInterface.HistoryView(requestBody);
        historyViewCall.enqueue(new Callback<MyLoveDataArray>() {
            @Override
            public void onResponse(Call<MyLoveDataArray> call, Response<MyLoveDataArray> response) {
                MyLoveDataArray loveDataArray = response.body();
                List<MyLoveData> data = loveDataArray.getLoveplace();

                if (data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        BrowseItem browseItem = new BrowseItem();
                        browseItem.setLatitude(data.get(i).getLatitude());
                        browseItem.setLongitude(data.get(i).getLongitude());
                        browseItem.setPoi(data.get(i).getPoi());
                        browseItem.setTimestamp(data.get(i).getTimestamp());
                        mData.add(browseItem);
                    }

                }
                GeoCoder mgeoCoder = GeoCoder.newInstance();
                OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                    }

                    //逆地理编码
                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                        for (int i = 0; i < mData.size(); i++) {
                            if(mData.get(i).getLatitude()==reverseGeoCodeResult.getLocation().latitude&&mData.get(i).getLongitude()==reverseGeoCodeResult.getLocation().longitude){
                                mData.get(i).setPlace(reverseGeoCodeResult.getAddress());
                            }
                        }

                    }
                };
                mgeoCoder.setOnGetGeoCodeResultListener(listener);
                for (int i = 0; i < mData.size(); i++) {
                    LatLng latLng = new LatLng(mData.get(i).getLatitude(), mData.get(i).getLongitude());
                    mgeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng).newVersion(1).language(LanguageType.LanguageTypeChinese));
                }
                mgeoCoder.destroy();
                adapter = new BrowseAdapter(mData);
                listView = (ListView) findViewById(R.id.browse_listView);
                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<MyLoveDataArray> call, Throwable t) {
                System.out.println("请求失败！");
                Log.e("YANG", t.getMessage());
            }
        });
    }
}