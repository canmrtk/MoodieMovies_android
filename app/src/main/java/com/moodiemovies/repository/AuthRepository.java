package com.moodiemovies.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.model.LoginRequest;
import com.moodiemovies.model.UserDTO;
import com.moodiemovies.model.UserRegistrationRequestDTO;
import com.moodiemovies.network.ApiClient;
import com.moodiemovies.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private static final String TAG = "AuthRepository";
    private final ApiService apiService;
    private static AuthRepository instance;

    private AuthRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public static AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    /**
     * Kullanıcıyı email+password ile login eder.
     * @param email    Kullanıcının e-posta adresi
     * @param password Kullanıcının şifresi
     * @return LiveData içinde AuthResponse (başarılıysa gerçek, değilse hata mesajı)
     */
    public LiveData<AuthResponse> loginUser(String email, String password) {
        MutableLiveData<AuthResponse> data = new MutableLiveData<>();

        // Doğru LoginRequest oluşturuluyor
        LoginRequest loginRequest = new LoginRequest(email, password);

        apiService.login(loginRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Başarılı yanıt gelince doğrudan yayımlıyoruz
                    data.setValue(response.body());
                } else {
                    // HTTP hatası (4xx / 5xx)
                    AuthResponse errorResponse = new AuthResponse();
                    errorResponse.setMessage("Giriş başarısız (Kod: " + response.code() + ")");
                    data.setValue(errorResponse);
                    Log.e(TAG, "loginUser onResponse failure: code=" + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // Ağ veya diğer hatalar
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.setMessage("Sunucuya bağlanılamadı: " + t.getMessage());
                data.setValue(errorResponse);
                Log.e(TAG, "loginUser onFailure", t);
            }
        });

        return data;
    }

    /**
     * Yeni kullanıcı kaydı yapar.
     * @param name     Kullanıcının adı
     * @param email    Kullanıcının e-posta adresi
     * @param password Kullanıcının şifresi
     * @return LiveData içinde AuthResponse (başarılıysa gerçek, değilse hata mesajı)
     */
    public LiveData<AuthResponse> registerUser(String name, String email, String password) {
        MutableLiveData<AuthResponse> data = new MutableLiveData<>();

        // Backend tarafı 'name','email','password' bekliyor
        UserRegistrationRequestDTO registrationRequest =
                new UserRegistrationRequestDTO(name, email, password);

        apiService.registerUser(registrationRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    AuthResponse errorResponse = new AuthResponse();
                    errorResponse.setMessage("Kayıt başarısız (Kod: " + response.code() + ")");
                    data.setValue(errorResponse);
                    Log.e(TAG, "registerUser onResponse failure: code=" + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.setMessage("Sunucuya bağlanılamadı: " + t.getMessage());
                data.setValue(errorResponse);
                Log.e(TAG, "registerUser onFailure", t);
            }
        });

        return data;
    }
}
