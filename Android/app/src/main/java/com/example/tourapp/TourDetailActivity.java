package com.example.tourapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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
    private TextView introduction_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        initTourDetailItems();
        //introduction_tv = findViewById(R.id.introduction_tv);
        ListView listView = findViewById(R.id.list_item_detail);
        adapter = new TourDetailAdapter(this,R.layout.record_item,recordItemList);
        listView.setAdapter(adapter);
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
        //introduction_tv.setText("天安门（旧称：承天门）位于中国北京市中心，故宫的南端，属于首批全国重点文物保护单位之一，中国国家象征之一。天安门正中门洞上方悬挂着毛泽东画像，两边分别是“中华人民共和国万岁”和“世界人民大团结万岁”的大幅标语。");
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