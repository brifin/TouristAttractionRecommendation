package com.example.tourapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.tourapp.adapter.LoveAdapter;
import com.example.tourapp.viewAndItem.LoveItem;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

public class MyLoveActivity2 extends AppCompatActivity {

    public static List<LoveItem> mdatd = new ArrayList<LoveItem>();;
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
        getdata();
        adapter = new LoveAdapter(mdatd);
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

    //网络请求获取数据
    public static void getdata() {



    }
}