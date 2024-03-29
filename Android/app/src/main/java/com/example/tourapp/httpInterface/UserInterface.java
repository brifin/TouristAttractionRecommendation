package com.example.tourapp.httpInterface;

import com.example.tourapp.data.DataResult;
import com.example.tourapp.data.GetImageResult;
import com.example.tourapp.data.PngResult;
import com.example.tourapp.data.Result;
import com.example.tourapp.data.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserInterface {

    @POST("login")
    Call<Result> login(@Body RequestBody user);

    @POST("save")

    Call<Result> register(@Body RequestBody user);

    @POST("update")
    Call<Result> updatePwd(@Body RequestBody user);

    @POST("updatePng")
    @Multipart
    Call<PngResult> uploadFile(@Part MultipartBody.Part file);

    @GET("getPng")
    Call<GetImageResult> getImage();

    @GET("tourGroup")
    @Headers("Cookie:userId=9")
    Call<DataResult> tourGroup();
}
