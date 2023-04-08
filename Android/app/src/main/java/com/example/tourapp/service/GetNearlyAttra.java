package com.example.tourapp.service;

import com.example.tourapp.data.NearlyAttra;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetNearlyAttra {


    @POST("nearbyAttractions")
    @FormUrlEncoded
    Call<NearlyAttra> getNearlyAttra(@Field("latitude") String latitude, @Field("longitude") String longitude );
}
