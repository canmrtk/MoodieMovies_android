package com.moodiemovies.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.model.LoginRequest;
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
     * Kullanıcıyı email+password ile login eder. Sonucu verilen MutableLiveData'ya postalar.
     * @param email    Kullanıcının e-posta adresi
     * @param password Kullanıcının şifresi
     * @param liveData Sonucun postalanacağı MutableLiveData nesnesi.
     */
    public void loginUser(String email, String password, MutableLiveData<AuthResponse> liveData) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        apiService.loginUser(loginRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    AuthResponse errorResponse = new AuthResponse();
                    try {
                        // Hata mesajını backend'den okumaya çalış
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "{}";
                        errorResponse.setMessage("Giriş başarısız. Lütfen bilgilerinizi kontrol edin. (Kod: " + response.code() + ")");
                    } catch (Exception e) {
                        errorResponse.setMessage("Giriş başarısız (Kod: " + response.code() + ")");
                    }
                    liveData.postValue(errorResponse);
                    Log.e(TAG, "loginUser onResponse failure: code=" + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.setMessage("Sunucuya bağlanılamadı: " + t.getMessage());
                liveData.postValue(errorResponse);
                Log.e(TAG, "loginUser onFailure", t);
            }
        });
    }

    /**
     * Yeni kullanıcı kaydı yapar. Sonucu verilen MutableLiveData'ya postalar.
     * @param name     Kullanıcının adı
     * @param email    Kullanıcının e-posta adresi
     * @param password Kullanıcının şifresi
     * @param liveData Sonucun postalanacağı MutableLiveData nesnesi.
     */
    public void registerUser(String name, String email, String password, MutableLiveData<AuthResponse> liveData) {
        UserRegistrationRequestDTO registrationRequest = new UserRegistrationRequestDTO(name, email, password);

        apiService.registerUser(registrationRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    AuthResponse errorResponse = new AuthResponse();
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "{}";
                        // Backend'den gelen spesifik hata mesajını yakalamaya çalış
                        if (errorBody.contains("EmailAlreadyExistsException")) {
                            errorResponse.setMessage("Bu e-posta adresi zaten kullanımda.");
                        } else {
                            errorResponse.setMessage("Kayıt başarısız (Kod: " + response.code() + ")");
                        }
                    } catch (Exception e) {
                        errorResponse.setMessage("Kayıt başarısız (Kod: " + response.code() + ")");
                    }
                    liveData.postValue(errorResponse);
                    Log.e(TAG, "registerUser onResponse failure: code=" + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.setMessage("Sunucuya bağlanılamadı: " + t.getMessage());
                liveData.postValue(errorResponse);
                Log.e(TAG, "registerUser onFailure", t);
            }
        });
    }
}