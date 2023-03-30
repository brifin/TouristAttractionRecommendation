package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;


import com.example.tourapp.R;
import com.example.tourapp.adapter.TourDetailAdapter;
import com.example.tourapp.httpInterface.GroupInterface;
import com.example.tourapp.viewAndItem.RecordItem;
import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TourDetailActivity extends AppCompatActivity {

    private List<RecordItem> recordItemList = new ArrayList<>();
    private TourDetailAdapter adapter;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        initTourDetailItems();
        iv_back = findViewById(R.id.iv_back);
        ListView listView = findViewById(R.id.list_item_detail);
        adapter = new TourDetailAdapter(this,R.layout.record_item,recordItemList);
        listView.setAdapter(adapter);
        iv_back.setOnClickListener(v->{
            finish();
        });

        hideStable();
    }

    private void initTourDetailItems() {
        //TODO 接受后端数据
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://47.107.38.208:8090/user/")
                .build();



        /*String[] data = response.body();
        for (String string : data) {
            String[] str = string.split("\\s+");
            for (int i = 1; i < str.length; i++) {
                String[] simple = str[i].split(",");
                for (String s : simple) {

                }
            }

        }*/


        String time,place;
        RecordItem recordItem;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for(int i = 1;i < 18;++i) {
            Date nowDate = new Date();
            time = format.format(nowDate);
            int lat = 19 + i;
            int lon = 16 + i;
            place = "(" + lat + "," + lon + ")";
            recordItem = new RecordItem(i,place,time,6+i);
            recordItemList.add(recordItem);
        }
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