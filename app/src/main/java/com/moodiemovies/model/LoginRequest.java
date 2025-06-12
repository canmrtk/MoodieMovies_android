package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("Username")
    private String username;

    @SerializedName("Password")
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setter methods
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}