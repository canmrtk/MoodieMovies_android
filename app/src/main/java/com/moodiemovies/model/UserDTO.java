package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

public class UserDTO {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    // (If your backend also returns profilePictureUrl, role, etc., add them here
    //  with @SerializedName and proper types.)

    // --- Getters & setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
