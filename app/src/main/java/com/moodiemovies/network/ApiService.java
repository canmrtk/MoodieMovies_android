package com.moodiemovies.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/v1/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @POST("/api/v1/auth/register")
    Call<Void> registerUser(@Body RegisterRequest request);

    @GET("/api/v1/films/films")
    Call<List<Film>> getLastWatchedFilms();



}
