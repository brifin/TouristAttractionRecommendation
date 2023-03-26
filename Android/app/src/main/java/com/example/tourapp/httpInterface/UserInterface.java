package com.example.tourapp.httpInterface;

import com.example.tourapp.reception.Result;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserInterface {

    @POST("login")
    @FormUrlEncoded
    Call<Result> login(@Field("account") String username, @Field("password") String password);

    @POST("register")
    @FormUrlEncoded
    Call<Result> register(@Field("account") String username,@Field("password") String password);

    @POST("update")
    @FormUrlEncoded
    Call<Result> updatePwd(@Field("account") String username,@Field("password") String password);

    @POST("upload")
    @Multipart
    Call<Result> uploadFile(@Part MultipartBody.Part file);
}
