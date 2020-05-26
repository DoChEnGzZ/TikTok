package com.example.myapplication.mytiktok.gson_internet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitVideo {
    @GET("api/invoke/video/invoke/video")
    Call<List<VideoMessage>> getvideo();
}
