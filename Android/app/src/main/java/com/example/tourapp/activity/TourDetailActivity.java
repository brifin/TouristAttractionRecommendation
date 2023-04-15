package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.HeterogeneousExpandableList;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tourapp.R;
import com.example.tourapp.adapter.TourDetailAdapter;
import com.example.tourapp.fragment.TourFragment;
import com.example.tourapp.viewAndItem.RecordItem;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TourDetailActivity extends AppCompatActivity {

    private List<RecordItem> recordItemList = new ArrayList<>();
    private TourDetailAdapter adapter;
    private ImageView iv_back;
    //private ImageView iv_ScatteredGroups;
    private TextView tv_introduce;
    private String[] introduce = {
            "遇见最美希腊——雅典+圣托里尼+梅黛奥拉天空之城游玩;希腊跟团游。",
            "冰与火之歌，冰岛欢乐深度游;冰岛跟团游。",
            "美国东部经典豪华游;纽约+费城+华盛顿+尼亚加拉瀑布+波士顿。",
            "美国西海岸;拉斯维加斯+大峡谷+羚羊彩穴+马蹄湾+胡佛水坝+巧克力工厂。",
            "芬兰游;图尔库城堡+赫尔辛基+波尔沃+凯米+罗瓦涅米+莱维。",
            "纵贯拉普兰极光破冰之旅;瑞典+芬兰跟团游。",
            "德国旅游团;新天鹅堡+柏林+科隆大教堂",
            "美国南部;迈阿密+罗德岱堡跟团游。"
    };

    double[] lat = {
            38.52,
            64.09,
            39.56,
            37.10,
            60.27,
            69.02,
            52.30,
            25.47
    };//纬度

    double[] lon = {
            24.52,
            -21.56,
            -75.10,
            -115.08,
            23.14,
            24.08,
            13.25,
            -80.03
    };//精度

    private int[] imageId = {
            R.drawable.view1,
            R.drawable.view2,
            R.drawable.view5,
            R.drawable.view7,
            R.drawable.view6,
            R.drawable.view3,
            R.drawable.view4,
            R.drawable.view8
    };
    private String schedule;
    //private boolean isScatteredGroups;
    private ImageView iv_introduce;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        iv_back = findViewById(R.id.iv_back);
        tv_introduce = findViewById(R.id.tv_introduce);
        iv_introduce = findViewById(R.id.iv_introduce);
        //iv_ScatteredGroups = findViewById(R.id.iv_ScatteredGroups);
        schedule = getIntent().getStringExtra("schedule");
        //isScatteredGroups = getIntent().getBooleanExtra("isScatteredGroups", false);
        id = getIntent().getIntExtra("id", 0);
        initTourDetailItems();
        ListView listView = findViewById(R.id.list_item_detail);
        adapter = new TourDetailAdapter(this, R.layout.record_item, recordItemList);
        listView.setAdapter(adapter);
        iv_back.setOnClickListener(v -> {
            finish();
        });
        if (id < 8) {
            tv_introduce.setText(introduce[id]);
            iv_introduce.setImageResource(imageId[id]);
        }

        hideStable();
    }

    private void initTourDetailItems() {
        //System.out.println(schedule);
        String[] str = schedule.split("\\s+");
        for (int i = 1; i < str.length; i++) {

            String[] simple = str[i].split(",");
            double latStart = lat[id];
            double lonStart = lon[id];
            Date nowDate = new Date();
            long startTime = nowDate.getTime() - 2000 * 60 * 1000;
            RecordItem recordItem;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String timeStr = simple[2];
            double timeOffset = Double.parseDouble(timeStr) * 60 * 1000;
            long time = (long) (startTime + timeOffset);
            Date date = new Date(time);
            String formatTime = format.format(date);
            double lat = latStart + Double.parseDouble(simple[0]);
            double lon = lonStart + Double.parseDouble(simple[1]);
            String latStr;
            String lonStr;
            if (lat >= 0) {
                latStr = String.format("%.2f° N", lat);
            } else {
                latStr = String.format("%.2f° S", -lat);
            }

            if (lon >= 0) {
                lonStr = String.format("%.2f° E", lon);
            } else {
                lonStr = String.format("%.2f° W", -lon);
            }


            String place = "(" + latStr + "," + lonStr + ")";
            recordItem = new RecordItem(i, place, formatTime, Integer.parseInt(simple[3]));
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

    /*@Override
    public void sendSchedule(String str) {
        schedule = str;
    }*/

    /*@Override
    public void sendIsScatteredGroups(Boolean iScatteredGroups) {
        isScatteredGroups = iScatteredGroups;
    }*/
}