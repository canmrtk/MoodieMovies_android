package com.moodiemovies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.repository.AuthRepository;

public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<AuthResponse> loginResult;

    public LoginViewModel() {
        authRepository = AuthRepository.getInstance();
        loginResult = new MutableLiveData<>();
    }

    // UI (Activity) bu metodu çağırarak sonucu gözlemleyecek.
    public LiveData<AuthResponse> getLoginResult() {
        return loginResult;
    }

    // Login işlemini tetikleyen metod.
    public void login(String email, String password) {
        // Repository'ye isteği yapmasını ve sonucu loginResult'a postalamasını söylüyoruz.
        authRepository.loginUser(email, password, loginResult);
    }
}