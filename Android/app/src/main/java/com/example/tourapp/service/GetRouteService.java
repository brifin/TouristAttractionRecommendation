package com.example.tourapp.service;

import com.example.tourapp.data.RouteData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetRouteService {
    @POST("route")

    Call<List<RouteData>> getRoute();
}
