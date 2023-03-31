package com.example.tourapp.service;

import com.example.tourapp.data.RecommendPlaceData;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetRecommendService {

    @POST("recommend")
    Call<List<Double[]>> getRecommendData(@Body RequestBody poiStarts);
}
