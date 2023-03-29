package com.example.tourapp;

import static com.example.tourapp.application.MyApplication.getContext;
import static com.example.tourapp.application.MyApplication.getInstance;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.example.tourapp.application.MyApplication;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

public class TestActivity extends AppCompatActivity {

    PanoramaView panoramaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initBMapManger();
        setContentView(R.layout.activity_test);
        hideStable();
//        panoramaView = findViewById(R.id.panoramaView);
//        panoramaView.setPanorama("0100220000130817164838355J5");
//        //加载全景图,设置用户是否同意隐私策略
//        BMapManager mapManager = new BMapManager(getContext());
//        mapManager.setAgreePrivacy(getContext(), true);
//        //设置初始化监听
//        //常用事件监听,用来处理常用网络错误,授权验证错误,监听成功后加载全景
////        mapManager.init(new MKGeneralListener() {
////            @Override
////            public void onGetPermissionState(int i) {
////                //非零值表示验证未通过
////                if(i != 0){
////                        //授权key错误
////                    Toast.makeText(TestActivity.this, "请在AndroidManfiest.xml中输入正确的key,并检查您的网络是否正常!errror"+i, Toast.LENGTH_SHORT).show();
////                    }else {
////                    Toast.makeText(TestActivity.this, "key认证成功", Toast.LENGTH_SHORT).show();
////                    //加载全景
////                    panoramaView = findViewById(R.id.panoramaView);
////                    panoramaView.setPanorama("0100220000130817164838355J5");
////                }
////            }
////        });



    }

//    private void initBMapManger() {
//    MyApplication app = (MyApplication) getInstance();
//    if(app.mapManager==null){
//        app.mapManager = new BMapManager(app);
//
//    }
//    if(!app.mapManager.init(new MyApplication.MyGeneralListener())){
//        app.mapManager.init(new MyApplication.MyGeneralListener());
//    }
//    }


    @Override
    protected void onPause() {
        panoramaView.onPause();
        super.onPause();


    }


    @Override
    protected void onResume() {
        panoramaView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        panoramaView.destroy();
        super.onDestroy();
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
}

