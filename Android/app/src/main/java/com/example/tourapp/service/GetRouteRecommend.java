package com.example.tourapp.service;

import com.example.tourapp.data.RouteRecommend;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetRouteRecommend {
    @POST("routeRecommend")
    Call<RouteRecommend> getRouteRecommend(@Body RequestBody requestBody);
}
