package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.tourapp.R;
import com.example.tourapp.adapter.BrowseAdapter;
import com.example.tourapp.viewAndItem.BrowseItem;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

public class MyBrowseActivity extends AppCompatActivity {

    private List<BrowseItem> mdata;
    private BrowseAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_browse);
        initView();
    }

    public void initView(){
        hideStable();
        mdata = new ArrayList<BrowseItem>();
        getData();
        adapter = new BrowseAdapter(mdata);
        listView = (ListView) findViewById(R.id.browse_listView);
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
    public Boolean getData(){

        return false;
    }
}