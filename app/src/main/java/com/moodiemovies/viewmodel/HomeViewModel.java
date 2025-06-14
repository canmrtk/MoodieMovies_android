package com.moodiemovies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.moodiemovies.model.Film;
import com.moodiemovies.network.ApiClient;
import com.moodiemovies.network.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<Film>> popularFilms = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private ApiService apiService;

    public HomeViewModel() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<List<Film>> getPopularFilms() {
        return popularFilms;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchPopularFilms(int limit) {
        apiService.getPopularFilms(limit).enqueue(new Callback<List<Film>>() {
            @Override
            public void onResponse(Call<List<Film>> call, Response<List<Film>> response) {
                if (response.isSuccessful()) {
                    popularFilms.postValue(response.body());
                } else {
                    errorMessage.postValue("Filmler alınamadı: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Film>> call, Throwable t) {
                errorMessage.postValue("Ağ hatası: " + t.getMessage());
            }
        });
    }
}