package com.example.tourapp.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.common.BaiduMapSDKException;

public class MyApplication extends Application {

    private static Context mcontext;
    private static MyApplication mApp;

    public static MyApplication getInstance() {
        return mApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mcontext = getApplicationContext();
        SDKInitializer.setAgreePrivacy(mcontext, true);
        try{
            SDKInitializer.initialize(mcontext);
            SDKInitializer.setCoordType(CoordType.BD09LL);
        }catch (BaiduMapSDKException e){
            e.printStackTrace();
        }
    }

    public static Context getContext(){
        return mcontext;
    }
}
