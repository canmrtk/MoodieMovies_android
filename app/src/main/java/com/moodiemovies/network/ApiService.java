package com.moodiemovies.network;

import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.model.Film;
import com.moodiemovies.model.FilmDetail;
import com.moodiemovies.model.FilmPage;
import com.moodiemovies.model.FilmRatingRequestDTO;
import com.moodiemovies.model.LoginRequest;
import com.moodiemovies.model.TestSubmissionRequest;
import com.moodiemovies.model.UserDTO;
import com.moodiemovies.model.UserRegistrationRequestDTO;
import com.moodiemovies.model.UserUpdateRequestDTO;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // --- Auth Endpoints ---
    @POST("api/v1/auth/register")
    Call<AuthResponse> registerUser(@Body UserRegistrationRequestDTO registrationRequest);

    // HATA 1 İÇİN DÜZELTME: Bu metodun adı "loginUser" olarak değiştirildi.
    @POST("api/v1/auth/login")
    Call<AuthResponse> loginUser(@Body LoginRequest loginRequest);

    // HATA 3 İÇİN DÜZELTME: Bu metod eklendi.
    // Not: Backend'de "/auth/google" diye bir endpoint yok. OAuth2 akışı backend'de farklı işliyor.
    // Bu endpoint'i backend'e eklemeniz veya mobil için farklı bir akış kurgulamanız gerekebilir.
    // Şimdilik, çalışması için geçici olarak /auth/login'e yönlendirebiliriz veya doğru endpoint'i bekleyebiliriz.
    // Varsayımsal olarak backend'de /auth/google olduğunu varsayalım.
    @POST("api/v1/auth/google")
    Call<AuthResponse> loginWithGoogle(@Body Map<String, String> idToken);

    // --- User Endpoints ---
    @GET("api/v1/users/me")
    Call<UserDTO> getCurrentUser(@Header("Authorization") String token);

    @PUT("api/v1/users/{userId}")
    Call<UserDTO> updateUserProfile(@Header("Authorization") String token, @Path("userId") String userId, @Body UserUpdateRequestDTO updateRequest);

    // --- Film Endpoints ---
    @GET("api/v1/films")
    Call<FilmPage> getAllFilms(@Query("page") int page, @Query("size") int size);

    @GET("api/v1/films/popular/favorites")
    Call<List<Film>> getPopularFilms(@Query("limit") int limit);

    @GET("api/v1/films/{filmId}")
    Call<FilmDetail> getFilmDetails(@Path("filmId") String filmId);

    // --- Interaction Endpoints ---
    @POST("api/v1/interactions/films/{filmId}/rate")
    Call<ResponseBody> rateFilm(@Header("Authorization") String token, @Path("filmId") String filmId, @Body FilmRatingRequestDTO ratingRequest);

    @POST("api/v1/interactions/films/{filmId}/favorite")
    Call<ResponseBody> toggleFavorite(@Header("Authorization") String token, @Path("filmId") String filmId);

    // HATA 2 İÇİN DÜZELTME: Bu metod eklendi.
    @POST("api/v1/tests/submit")
    Call<ResponseBody> submitTestAnswers(@Header("Authorization") String token, @Body TestSubmissionRequest submissionRequest);
}