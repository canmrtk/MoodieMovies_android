package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter methods
    public String getUsername() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setter methods
    public void setUsername(String username) {
        this.email = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}