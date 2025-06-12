package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("UserID")
    private int userId;

    @SerializedName("Username")
    private String username;

    @SerializedName("Email")
    private String email;

    @SerializedName("PasswordHash")
    private String passwordHash;

    @SerializedName("FirstName")
    private String firstName;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("ProfilePictureURL")
    private String profilePictureUrl;

    @SerializedName("CreatedAt")
    private String createdAt;

    // Getter Methods
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public String getCreatedAt() { return createdAt; }

    // Setter Methods
    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}