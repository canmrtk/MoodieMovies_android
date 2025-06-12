package com.moodiemovies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.repository.AuthRepository;

public class RegisterViewModel extends ViewModel {
    private AuthRepository authRepository;

    public RegisterViewModel() {
        authRepository = AuthRepository.getInstance();
    }

    // Metod imzası 'name' kullanacak şekilde güncellendi
    public LiveData<AuthResponse> register(String name, String email, String password) {
        return authRepository.registerUser(name, email, password);
    }
}