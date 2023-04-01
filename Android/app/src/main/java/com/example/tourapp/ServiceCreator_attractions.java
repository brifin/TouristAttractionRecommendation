package com.example.tourapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceCreator_attractions {
    private final static String BASE_URL = "http://47.107.38.208:8090/attractions/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private ServiceCreator_attractions() {

    }

    public static Object creatService(Class serviceClass) {
        return retrofit.create(serviceClass);
    }
}
