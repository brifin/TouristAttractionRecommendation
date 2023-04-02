package com.example.tourapp.service;

import com.example.tourapp.data.RouteData;
import com.example.tourapp.data.RouteRecommend;
import com.example.tourapp.data.RouteRecommendData;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetRouteService {
    @POST("route")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<RouteRecommendData> getRoute(@Body RequestBody requestBody);
}
