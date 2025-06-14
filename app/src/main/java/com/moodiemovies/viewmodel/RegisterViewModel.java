package com.moodiemovies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.repository.AuthRepository;

public class RegisterViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<AuthResponse> registerResult;

    public RegisterViewModel() {
        authRepository = AuthRepository.getInstance();
        registerResult = new MutableLiveData<>();
    }

    public LiveData<AuthResponse> getRegisterResult() {
        return registerResult;
    }

    public void register(String name, String email, String password) {
        authRepository.registerUser(name, email, password, registerResult);
    }
}