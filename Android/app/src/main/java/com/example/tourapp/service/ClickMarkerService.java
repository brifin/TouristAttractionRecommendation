package com.example.tourapp.service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ClickMarkerService {
    @POST("updateView")
    Call<ResponseBody> sendMyBrowse(@Body RequestBody requestBody);
}
