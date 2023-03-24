package com.example.tourapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;


import com.example.tourapp.adapter.TourDetailAdapter;
import com.example.tourapp.viewAndItem.RecordItem;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        String time,place;
        RecordItem recordItem;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for(int i = 1;i < 18;++i) {
            Date nowDate = new Date();
            time = format.format(nowDate);
            int lat = 19 + i;
            int lon = 16 + i;
            place = "(" + lat + "," + lon + ")";
            recordItem = new RecordItem(i,place,time);
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