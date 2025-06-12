package com.moodiemovies.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.moodiemovies.model.TestSubmissionRequest;
import com.moodiemovies.network.ApiClient;
import com.moodiemovies.network.ApiService;

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

    /**
     * Kullanıcının test cevaplarını backend'e gönderir.
     * @param token Yetkilendirme token'ı.
     * @param request Kullanıcının cevaplarını içeren istek nesnesi.
     * @return İşlemin başarılı olup olmadığını belirten bir LiveData.
     */
    public LiveData<Boolean> submitAnswers(String token, TestSubmissionRequest request) {
        MutableLiveData<Boolean> success = new MutableLiveData<>();

        // API servisine isteği gönderiyoruz.
        apiService.submitTestAnswers("Bearer " + token, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // HTTP 2xx kodları başarılı sayılır.
                success.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Ağ hatası veya başka bir sorun olduğunda başarısız olarak işaretle.
                success.setValue(false);
            }
        });

        return success;
    }
}