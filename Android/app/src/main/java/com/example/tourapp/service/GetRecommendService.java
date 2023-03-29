package com.example.tourapp.service;

import com.example.tourapp.data.PlaceData;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetRecommendService {

    @POST("recommend")
    Call<List<PlaceData>> getRecommendData(@Body RequestBody poiStarts);
}
