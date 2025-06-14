package com.moodiemovies.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.moodiemovies.model.TestSubmissionRequest;
import com.moodiemovies.network.ApiClient;
import com.moodiemovies.network.ApiService;

import okhttp3.ResponseBody; // ResponseBody import edildi
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestRepository {

    private ApiService apiService;
    private static TestRepository instance;

    public static TestRepository getInstance() {
        if (instance == null) {
            instance = new TestRepository();
        }
        return instance;
    }

    private TestRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<Boolean> submitAnswers(String token, TestSubmissionRequest request) {
        MutableLiveData<Boolean> success = new MutableLiveData<>();

        // HATA DÜZELTİLDİ: Callback<Void> yerine Callback<ResponseBody> kullanıldı.
        apiService.submitTestAnswers("Bearer " + token, request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // HTTP 2xx kodları başarılı sayılır.
                success.postValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Ağ hatası veya başka bir sorun olduğunda başarısız olarak işaretle.
                success.postValue(false);
            }
        });

        return success;
    }
}