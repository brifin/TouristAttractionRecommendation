package com.example.tourapp.httpInterface;

import com.example.tourapp.data.Result;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserInterface {

    @POST("login")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Call<Result> login(@Body RequestBody user);

    @POST("login")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Call<Result> register(@Body RequestBody user);

    @POST("update")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Call<Result> updatePwd(@Body RequestBody user);

    @POST("upload")
    @Multipart
    Call<Result> uploadFile(@Part MultipartBody.Part file);
}
