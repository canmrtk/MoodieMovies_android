package com.moodiemovies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.repository.AuthRepository;

public class LoginViewModel extends ViewModel {

    private AuthRepository authRepository;

    public LoginViewModel() {
        // Repository'nin bir örneğini alıyoruz.
        authRepository = AuthRepository.getInstance();
    }

    /**
     * Giriş işlemini başlatır ve sonucu LiveData olarak döndürür.
     * @param username Kullanıcı adı
     * @param password Şifre
     * @return AuthResponse içeren LiveData nesnesi.
     */
    public LiveData<AuthResponse> login(String username, String password) {
        return authRepository.loginUser(username, password);
    }
}