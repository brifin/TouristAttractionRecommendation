package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.tourapp.R;
import com.example.tourapp.adapter.LoveAdapter;
import com.example.tourapp.data.MyLoveData;
import com.example.tourapp.httpInterface.GroupInterface;
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

public class MyLoveActivity2 extends AppCompatActivity {

    public static List<LoveItem> mData = new ArrayList<LoveItem>();
    ;
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
        getData(nickname);
        //注意list view的item布局和mData数据暂时不匹配
        adapter = new LoveAdapter(mData);
        listView = (ListView) findViewById(R.id.love_listView);
        listView.setAdapter(adapter);
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


    //网络请求获取或者更新数据
    public static void getData(String nickname) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://121.37.67.235:8000/app01/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupInterface groupInterface = retrofit.create(GroupInterface.class);
        Gson gson = new Gson();
        String nicknameJson = gson.toJson(nickname);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), nicknameJson);
        Call<List<MyLoveData>> historyStarCall = groupInterface.HistoryStar(requestBody);
        historyStarCall.enqueue(new Callback<List<MyLoveData>>() {
            @Override
            public void onResponse(Call<List<MyLoveData>> call, Response<List<MyLoveData>> response) {
                List<MyLoveData> data = response.body();

                for (int i = 0; i< Objects.requireNonNull(data).size(); i++){
                    LoveItem loveItem = new LoveItem();
                    loveItem.setLatitude(data.get(i).getLatitude());
                    loveItem.setLongitude(data.get(i).getLongitude());
                    loveItem.setPoi(data.get(i).getPoi());
                    loveItem.setTimestamp(data.get(i).getTimestamp());
                    mData.add(loveItem);
                }

            }

            @Override
            public void onFailure(Call<List<MyLoveData>> call, Throwable t) {
                System.out.println("请求失败！");
                System.out.println(t.getMessage());
            }
        });


    }

}