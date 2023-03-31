package com.example.tourapp.httpInterface;

import com.example.tourapp.data.MyLoveData;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GroupInterface {
    @POST("historyStar")
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    Call<List<MyLoveData>> HistoryStar(@Body RequestBody nickname);

    @POST("historyView")
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    Call<List<MyLoveData>> HistoryView(@Body RequestBody nickname);

    @POST("groupclass")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<String> groupClass(@Body RequestBody data);

}
