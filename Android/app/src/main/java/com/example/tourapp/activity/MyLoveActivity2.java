package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.tourapp.R;
import com.example.tourapp.adapter.LoveAdapter;
import com.example.tourapp.httpInterface.GroupInterface;
import com.example.tourapp.viewAndItem.LoveItem;
import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyLoveActivity2 extends AppCompatActivity {

    private List<LoveItem> mData;
    private ListView listView;
    private LoveAdapter adapter;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_love2);
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        initView();

    }

    public void initView() {
        hideStable();
        mData = new ArrayList<LoveItem>();
        getData();
        adapter = new LoveAdapter(mData);
        listView = (ListView) findViewById(R.id.love_listView);
        listView.setAdapter(adapter);
    }

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
        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://121.37.67.235:8000/app01/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupInterface groupInterface = retrofit.create(GroupInterface.class);
        Gson gson = new Gson();
        String nicknameJson = gson.toJson(nickname);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), nicknameJson);
        Call<String> historyStarCall = groupInterface.HistoryStar(requestBody);
        historyStarCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                Log.e("TAG",data);//待测试
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("请求失败！");
                System.out.println(t.getMessage());
            }
        });*/
        for (int i = 0; i < 15; i++) {
            LoveItem item = new LoveItem();
            item.setPhoto(R.drawable.tiananmen_test);
            item.setPlace("天安门");
            item.setTime("2022年3月24日");
            mData.add(item);
        }

    }
}