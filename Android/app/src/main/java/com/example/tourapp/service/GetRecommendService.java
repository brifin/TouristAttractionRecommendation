package com.example.tourapp.service;

import com.example.tourapp.data.RecommendPlaceData;
import com.example.tourapp.data.RecommendReturn;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetRecommendService {

    @POST("recommend")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<RecommendReturn> getRecommendData(@Body RequestBody poiStarts);
}
