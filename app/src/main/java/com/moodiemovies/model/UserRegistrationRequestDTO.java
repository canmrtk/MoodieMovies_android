package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

// Bu sınıf, backend'deki UserRegistrationRequestDTO ile birebir aynıdır.
public class UserRegistrationRequestDTO {

    // Backend 'name' bekliyor, 'username' değil.
    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public UserRegistrationRequestDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}