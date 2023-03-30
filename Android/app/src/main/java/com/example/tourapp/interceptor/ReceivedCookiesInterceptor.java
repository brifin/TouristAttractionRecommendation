package com.example.tourapp.interceptor;

import android.content.SharedPreferences;

import com.example.tourapp.application.MyApplication;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response proceed = chain.proceed(chain.request());

        if(!proceed.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : proceed.headers("Set-Cookie")) {
                cookies.add(header);
            }

            SharedPreferences.Editor config = MyApplication.getContext().getSharedPreferences("config", MyApplication.getContext().MODE_PRIVATE)
                    .edit();
            config.putStringSet("cookie", cookies);
            config.apply();
        }

        return proceed;
    }
}
