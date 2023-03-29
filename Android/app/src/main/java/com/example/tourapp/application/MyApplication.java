package com.example.tourapp.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.common.BaiduMapSDKException;

public class MyApplication extends Application {

    private static Context mcontext;
    private static MyApplication mApp = null;
    public  BMapManager mapManager = null;


    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mcontext = this;
//      设置用户同意隐私策略
//      true表示同意
        SDKInitializer.setAgreePrivacy(mcontext, true);
        try {
            SDKInitializer.initialize(mcontext);
            SDKInitializer.setCoordType(CoordType.BD09LL);
        } catch (BaiduMapSDKException e) {
            e.printStackTrace();
        }

        if (mapManager == null) {
            mapManager = new BMapManager(mcontext);
            mapManager.setAgreePrivacy(mcontext,true);
        }
//        if (!mapManager.init(new MyGeneralListener())) {
//            Toast.makeText(mcontext, "初始化错误", Toast.LENGTH_SHORT).show();
//        }


    }

    public static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetPermissionState(int i) {
            if (i != 0) {

            } else {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "key认证成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Context getContext() {
        return mcontext;
    }

    public static MyApplication getInstance() {
        return mApp;
    }


}
