package com.example.tourapp;

import com.example.tourapp.interceptor.AddCookiesInterceptor;
import com.example.tourapp.interceptor.ReceivedCookiesInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceCreator_app01 {

    private final static String BASE_URL = "http://121.37.67.235:8000/app01/";
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build();
//    private static Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
//            .build();

    private ServiceCreator_app01() {

    }

    public static Object creatService(Class serviceClass) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(serviceClass);
        }
        catch (Exception e){
            System.out.println(e.fillInStackTrace());
            return null;
        }
    }

}
