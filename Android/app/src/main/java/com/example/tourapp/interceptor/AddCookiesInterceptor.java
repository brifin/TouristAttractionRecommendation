package com.example.tourapp.interceptor;

import android.util.Log;

import com.example.tourapp.application.MyApplication;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> cookies = (HashSet) MyApplication.getContext().getSharedPreferences("config",
                MyApplication.getContext().MODE_PRIVATE).getStringSet("cookie", null);

        if(cookies != null) {
            for (String cookie : cookies) {
                builder.addHeader("Cookie",cookie);
                Log.v("YANG","Adding Header cookie: "+cookie);
            }
        }

        return chain.proceed(builder.build());
    }
}
