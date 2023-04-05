package com.example.tourapp.service;



import com.example.tourapp.data.RouteRespose;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetRouteRecommend {
    @POST("routeRecommend")
    Call<RouteRespose> getRouteRecommend(@Body RequestBody requestBody);
}
