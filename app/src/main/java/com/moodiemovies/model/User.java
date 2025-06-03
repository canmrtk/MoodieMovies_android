// com.moodiemovies.model.User.java
package com.moodiemovies.model;

// Eğer backend LocalDateTime'ı ISO 8601 string formatında gönderiyorsa,
// Retrofit ve Gson genellikle bunu String olarak alır.
// İstersen Gson için özel bir LocalDateTime deserializer da yazılabilir
// veya direkt String olarak alıp ihtiyaç duyulduğunda parse edebilirsin.
// Şimdilik String olarak alalım.
// import java.time.LocalDateTime; // Eğer Gson ile direkt mapleyebiliyorsan veya custom deserializer varsa

public class User {
    private String id;
    private String name;
    private String email;
    private String authType; // LOCAL, GOOGLE, FACEBOOK
    private String providerId; // Opsiyonel, sadece OAuth2 kullanıcıları için
    private String createdDate; // Backend'den gelen LocalDateTime'ı String olarak alalım
    private String lastUpdatedDate; // Backend'den gelen LocalDateTime'ı String olarak alalım

    // Boş constructor (Gson için gerekebilir)
    public User() {
    }

    // Dolu constructor (opsiyonel)
    public User(String id, String name, String email, String authType, String providerId, String createdDate, String lastUpdatedDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.authType = authType;
        this.providerId = providerId;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAuthType() {
        return authType;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", authType='" + authType + '\'' +
                '}';
    }
}