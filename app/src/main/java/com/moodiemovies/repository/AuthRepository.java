package com.moodiemovies.repository;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.model.LoginRequest;
import com.moodiemovies.model.UserRegistrationRequestDTO; // Doğru DTO import edildi
import com.moodiemovies.network.ApiClient;
import com.moodiemovies.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.IOException;

public class AuthRepository {

    private final ApiService apiService;
    private static AuthRepository instance;

    public static AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    private AuthRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<AuthResponse> loginUser(String username, String password) {

        MutableLiveData<AuthResponse> data = new MutableLiveData<>();

        LoginRequest loginRequest = new LoginRequest(username, password);



        apiService.loginUser(loginRequest).enqueue(new Callback<AuthResponse>() {

            @Override

            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if (response.isSuccessful()) {

                    data.setValue(response.body());

                } else {

                    AuthResponse errorResponse = new AuthResponse();

                    errorResponse.setMessage("Giriş başarısız (Kod: " + response.code() + ")");

                    data.setValue(errorResponse);

                }

            }



            @Override

            public void onFailure(Call<AuthResponse> call, Throwable t) {

                Log.e("AuthRepository", "Giriş işlemi ağ hatası", t);

                AuthResponse errorResponse = new AuthResponse();

                errorResponse.setMessage("Sunucuya bağlanılamadı: " + t.getMessage());

                data.setValue(errorResponse);

            }

        });

        return data;

    }

    // registerUser metodu doğru DTO'yu kullanacak şekilde güncellendi
    public LiveData<AuthResponse> registerUser(String name, String email, String password) {
        MutableLiveData<AuthResponse> data = new MutableLiveData<>();
        // Artık backend'in beklediği doğru nesneyi oluşturuyoruz.
        UserRegistrationRequestDTO registrationRequest = new UserRegistrationRequestDTO(name, email, password);

        apiService.registerUser(registrationRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    AuthResponse errorResponse = new AuthResponse();
                    errorResponse.setMessage("Kayıt başarısız (Kod: " + response.code() + ")");
                    data.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("AuthRepository", "Kayıt işlemi ağ hatası", t);
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.setMessage("Sunucuya bağlanılamadı: " + t.getMessage());
                data.setValue(errorResponse);
            }
        });
        return data;
    }
}