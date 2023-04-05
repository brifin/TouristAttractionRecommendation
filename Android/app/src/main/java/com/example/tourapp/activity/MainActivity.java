package com.example.tourapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tourapp.R;
import com.example.tourapp.adapter.ViewPagerAdapter;
import com.example.tourapp.application.MyApplication;
import com.example.tourapp.fragment.MapFragment;
import com.example.tourapp.fragment.TourFragment;
import com.example.tourapp.fragment.UserFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private LinearLayout pop_upsview;
    private TextView itemTextview;
    public static MainActivity instance = null;


    private List<Fragment> fragmentList;
    private TabLayoutMediator mediator;
    private String[] tabText;
    int[] itemLayout;
    int[] itemImgId;
    int[] itemtvId;

    public  String nickname = null;
    int activeColor = R.color.purple_200;
    int normalColor = R.color.black;

    int activeSize = 25;
    int normalSize = 16;

    private final String[] permissionArray = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private ActivityResultLauncher<String[]> requestPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                boolean internet = Boolean.TRUE.equals(result.get(Manifest.permission.INTERNET));
                boolean accessNetWork = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_NETWORK_STATE));
                boolean accessWifistate = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_WIFI_STATE));
                boolean readContacts = Boolean.TRUE.equals(result.get(Manifest.permission.READ_CONTACTS));
                boolean readPhoneState = Boolean.TRUE.equals(result.get(Manifest.permission.READ_PHONE_STATE));
                boolean readAudio = Boolean.TRUE.equals(result.get(Manifest.permission.READ_MEDIA_AUDIO));
                boolean readImages = Boolean.TRUE.equals(result.get(Manifest.permission.READ_MEDIA_IMAGES));
                boolean readVideo = Boolean.TRUE.equals(result.get(Manifest.permission.READ_MEDIA_VIDEO));
                boolean writeStorage = Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                boolean coarseLocation = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION));
                boolean fineLocation = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkVersion();
        initView();
        Intent intent = getIntent();
        nickname = intent.getStringExtra("username");
        instance = this;

    }

    //初始化视图
    public void initView() {
        tabText = new String[]{"地图", "旅游团", "用户"};
        itemLayout = new int[]{R.layout.mapitemlayout, R.layout.tourteamitem, R.layout.useritem};
        itemImgId = new int[]{R.id.mapimg, R.id.tourteamimg, R.id.userimg};
        itemtvId = new int[]{R.id.mapitemtv, R.id.touritemtv, R.id.useritemtv};

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new MapFragment());
        fragmentList.add(new TourFragment());
        fragmentList.add(new UserFragment());

        viewPager2 = (ViewPager2) findViewById(R.id.viewpager2);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        hideStable();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragmentList);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setUserInputEnabled(false);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                hideStable();
            }
        });

        mediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        View view0 = LayoutInflater.from(MyApplication.getContext()).inflate(itemLayout[0], null);
                        tab.setCustomView(view0);
                        break;
                    case 1:
                        View view1 = LayoutInflater.from(MyApplication.getContext()).inflate(itemLayout[1], null);
                        tab.setCustomView(view1);
                        break;
                    case 2:
                        View view2 = LayoutInflater.from(MyApplication.getContext()).inflate(itemLayout[2], null);
                        tab.setCustomView(view2);
                        break;
                }
            }
        });
        mediator.attach();
        viewPager2.setUserInputEnabled(false);
        //选中改变图片颜色和文字颜色
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(itemImgId[tab.getPosition()]).setFocusable(true);
                itemTextview = (TextView) tab.getCustomView().findViewById(itemtvId[tab.getPosition()]);
                itemTextview.setTextColor(getColor(R.color.teal_700));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(itemImgId[tab.getPosition()]).setFocusable(false);
                itemTextview = (TextView) tab.getCustomView().findViewById(itemtvId[tab.getPosition()]);
                itemTextview.setTextColor(getColor(R.color.black));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //版本检测
    private void checkVersion() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            for (int i = 0; i < permissionArray.length; i++) {
                if (checkSelfPermission(permissionArray[i]) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions.launch(permissionArray);
                }
            }
        }
    }

    //设置返回
    @Override
    public void onBackPressed() {
        if (viewPager2.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
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


    @Override
    protected void onDestroy() {
        mediator.detach();
        super.onDestroy();
    }

    public String getNickname(){
        nickname = getIntent().getStringExtra("username");
        return nickname;
    }
}
