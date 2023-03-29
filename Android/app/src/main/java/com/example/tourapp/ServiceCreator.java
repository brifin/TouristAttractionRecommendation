package com.example.tourapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceCreator {

    private final static String BASE_URL = "http://121.37.67.235:8000/app01/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private ServiceCreator() {

    }

    public static Object creatService(Class serviceClass) {
        return retrofit.create(serviceClass);
    }

}
