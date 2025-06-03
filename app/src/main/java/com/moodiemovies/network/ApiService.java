// com.moodiemovies.network.ApiService.java
package com.moodiemovies.network;

import com.moodiemovies.model.Film;           // Android'deki FilmSummaryDTO'ya karşılık gelen model
import com.moodiemovies.model.FilmDetail;    // Android'deki FilmDetailDTO için
import com.moodiemovies.model.FilmPage;      // Backend'deki Page<FilmSummaryDTO> için Android modeli

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // --- AUTH ENDPOINTS ---
    @POST("api/v1/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @POST("api/v1/auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request); // RegisterResponse'u tanımla

    // --- FILM ENDPOINTS ---
    @GET("api/v1/films") // Doğru endpoint
    Call<FilmPage> getAllFilms( // Dönen tip FilmPage (içinde List<Film> content ve sayfalama bilgileri olacak)
                                @Header("Authorization") String authToken, // Token gerekiyorsa
                                @Query("page") int page,
                                @Query("size") int size,
                                @Query("sort") String sort // Örn: "id,asc" veya "releaseDate,desc"
    );

    @GET("api/v1/films/{id}")
    Call<FilmDetail> getFilmDetails(
            @Path("id") String filmId,
            @Header("Authorization") String authToken // Token gerekiyorsa
    );

    @GET("api/v1/films/suggestions")
    Call<List<Film>> getFilmSuggestions( // FilmSummaryDTO için Film modelini kullanıyoruz
                                         @Query("query") String query,
                                         @Header("Authorization") String authToken // Token gerekiyorsa
    );

    // --- LISTE (KULLANICI LİSTELERİ) ENDPOINTS (listapidocs.md'ye göre) ---
    @GET("api/v1/lists") // Kullanıcının kendi listeleri
    Call<List<com.moodiemovies.model.FilmListSummary>> getMyLists(@Header("Authorization") String authToken);
    // Not: FilmListSummary modelini (com.moodiemovies.model.FilmListSummary) oluşturduğundan emin ol.
}