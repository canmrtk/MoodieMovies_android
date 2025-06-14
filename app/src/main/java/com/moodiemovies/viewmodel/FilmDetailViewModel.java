package com.moodiemovies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.moodiemovies.model.FilmDetail;
import com.moodiemovies.network.ApiClient;
import com.moodiemovies.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmDetailViewModel extends ViewModel {
    private MutableLiveData<FilmDetail> filmDetail = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private ApiService apiService;

    public FilmDetailViewModel() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<FilmDetail> getFilmDetail() {
        return filmDetail;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchFilmDetails(String filmId) {
        apiService.getFilmDetails(filmId).enqueue(new Callback<FilmDetail>() {
            @Override
            public void onResponse(Call<FilmDetail> call, Response<FilmDetail> response) {
                if (response.isSuccessful()) {
                    filmDetail.postValue(response.body());
                } else {
                    errorMessage.postValue("Film detayı alınamadı: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<FilmDetail> call, Throwable t) {
                errorMessage.postValue("Ağ hatası: " + t.getMessage());
            }
        });
    }
}