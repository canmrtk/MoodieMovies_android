package com.moodiemovies.network;

import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.model.LoginRequest;
import com.moodiemovies.model.TestSubmissionRequest;
import com.moodiemovies.model.UserRegistrationRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/v1/auth/login")
    Call<AuthResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("api/v1/auth/register")
    Call<AuthResponse> registerUser(@Body UserRegistrationRequestDTO registrationRequest);

    @POST("api/test/submit")
    Call<Void> submitTestAnswers(@Header("Authorization") String token, @Body TestSubmissionRequest submissionRequest);
}