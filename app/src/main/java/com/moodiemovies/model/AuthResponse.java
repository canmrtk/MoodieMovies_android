package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("tokenType")
    private String tokenType;

    @SerializedName("expiresInMs")
    private long expiresInMs;

    @SerializedName("user")
    private UserDTO user;

    // Backend hata durumlarında döndürülen message
    @SerializedName("message")
    private String message;

    // --- Getters & Setters ---

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiresInMs() {
        return expiresInMs;
    }

    public void setExpiresInMs(long expiresInMs) {
        this.expiresInMs = expiresInMs;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
