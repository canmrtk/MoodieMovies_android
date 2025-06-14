package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

public class UserDTO {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    // --- YENİ EKLENEN ALANLAR ---
    @SerializedName("authType")
    private String authType;

    @SerializedName("providerId")
    private String providerId;

    @SerializedName("createdDate")
    private String createdDate; // Tarihleri String olarak almak daha güvenlidir.

    @SerializedName("lastUpdatedDate")
    private String lastUpdatedDate;

    @SerializedName("ratingCount")
    private Long ratingCount;

    @SerializedName("favoriteCount")
    private Long favoriteCount;

    @SerializedName("listCount")
    private Long listCount;

    @SerializedName("avatarId")
    private String avatarId;

    @SerializedName("avatarImageUrl")
    private String avatarImageUrl;

    // --- Getters & setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // --- YENİ EKLENEN GETTER VE SETTER'LAR ---
    public String getAuthType() { return authType; }
    public void setAuthType(String authType) { this.authType = authType; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getLastUpdatedDate() { return lastUpdatedDate; }
    public void setLastUpdatedDate(String lastUpdatedDate) { this.lastUpdatedDate = lastUpdatedDate; }

    public Long getRatingCount() { return ratingCount != null ? ratingCount : 0L; }
    public void setRatingCount(Long ratingCount) { this.ratingCount = ratingCount; }

    public Long getFavoriteCount() { return favoriteCount != null ? favoriteCount : 0L; }
    public void setFavoriteCount(Long favoriteCount) { this.favoriteCount = favoriteCount; }

    public Long getListCount() { return listCount != null ? listCount : 0L; }
    public void setListCount(Long listCount) { this.listCount = listCount; }

    public String getAvatarId() { return avatarId; }
    public void setAvatarId(String avatarId) { this.avatarId = avatarId; }

    public String getAvatarImageUrl() { return avatarImageUrl; }
    public void setAvatarImageUrl(String avatarImageUrl) { this.avatarImageUrl = avatarImageUrl; }
}