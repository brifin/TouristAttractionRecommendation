package com.example.tourapp;

import com.example.tourapp.interceptor.AddCookiesInterceptor;
import com.example.tourapp.interceptor.ReceivedCookiesInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceCreator_user {
    private final static String BASE_URL = "http://47.107.38.208:8090/user/";
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new AddCookiesInterceptor())
            .addInterceptor(new ReceivedCookiesInterceptor())
            .writeTimeout(20000, TimeUnit.SECONDS)
            .readTimeout(20000,TimeUnit.SECONDS)
            .connectTimeout(20000,TimeUnit.SECONDS)
            .build();
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

    private ServiceCreator_user() {

    }

    public static Object creatService(Class serviceClass) {
        return retrofit.create(serviceClass);
    }
}
