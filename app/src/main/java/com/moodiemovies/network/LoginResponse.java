package com.moodiemovies.network;

import com.google.gson.annotations.SerializedName;
import com.moodiemovies.model.User; // User modelini import et

public class LoginResponse {

    @SerializedName("user")
    private User user;

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("tokenType")
    private String tokenType;

    @SerializedName("expiresInMs")
    private Long expiresInMs;

    // Getters
    public User getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getExpiresInMs() {
        return expiresInMs;
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setExpiresInMs(Long expiresInMs) {
        this.expiresInMs = expiresInMs;
    }
}