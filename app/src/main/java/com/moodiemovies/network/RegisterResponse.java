// com.moodiemovies.network.RegisterResponse.java (veya model paketinde)
package com.moodiemovies.network; // veya com.moodiemovies.model

// User.java modelinizin com.moodiemovies.model paketinde olduğunu varsayıyorum
import com.moodiemovies.model.User; // User modelinizi import edin

public class RegisterResponse {
    private String tokenType; // "Bearer"
    private Long expiresInMs;
    private User user; // Backend'deki UserDTO'ya karşılık gelen User modeliniz
    private String accessToken; // Eğer backend doğrudan tokenları dönüyorsa
    private String refreshToken; // Eğer backend doğrudan tokenları dönüyorsa

    // Backend'in tam olarak ne döndürdüğüne göre bu alanları ayarlayın.
    // Örneğin, backend AuthResponse DTO'su direkt tokenları içermiyorsa
    // ve login'deki gibi sadece user objesi ve belki bir mesaj dönüyorsa
    // bu sınıfı LoginResponse'a benzetebilirsiniz.

    // Getters and Setters

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresInMs() {
        return expiresInMs;
    }

    public void setExpiresInMs(Long expiresInMs) {
        this.expiresInMs = expiresInMs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}