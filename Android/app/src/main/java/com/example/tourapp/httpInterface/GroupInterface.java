package com.example.tourapp.httpInterface;

import com.example.tourapp.data.GroupResult;
import com.example.tourapp.data.MyLoveData;
import com.example.tourapp.data.MyLoveDataArray;
import com.example.tourapp.data.UserData;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GroupInterface {
    @POST("historyStar")
    Call<List<MyLoveData>> HistoryStar(@Body UserData userData);

    @POST("historyView")

    Call<List<MyLoveData>> HistoryView(@Body UserData userData);

    @POST("groupclass")
    //@Headers("Content-Type: application/x-www-form-urlencoded")
    Call<GroupResult> groupClass(@Body RequestBody data);

}
