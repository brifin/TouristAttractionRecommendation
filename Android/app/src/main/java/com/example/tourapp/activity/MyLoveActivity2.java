package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.tourapp.R;
import com.example.tourapp.adapter.LoveAdapter;
import com.example.tourapp.viewAndItem.LoveItem;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

public class MyLoveActivity2 extends AppCompatActivity {

    private List<LoveItem> mdatd;
    private ListView listView;
    private LoveAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_love2);
        initView();

    }

    public void initView() {
        hideStable();
        mdatd = new ArrayList<LoveItem>();
        getdata();
        adapter = new LoveAdapter(mdatd);
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
    public void getdata() {
        for (int i = 0; i < 15; i++) {
            LoveItem item = new LoveItem();
            item.setPhoto(R.drawable.tiananmen_test);
            item.setPlace("天安门");
            item.setTime("2022年3月24日");
            mdatd.add(item);
        }

    }
}