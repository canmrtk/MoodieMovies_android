package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("token")
    private String token;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user; // Başarılı giriş sonrası kullanıcı bilgileri dönebilir

    // Getter methods
    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    // Setter methods
    public void setToken(String token) {
        this.token = token;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(User user) {
        this.user = user;
    }
}