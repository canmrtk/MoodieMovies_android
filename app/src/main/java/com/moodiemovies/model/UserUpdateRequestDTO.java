package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

public class UserUpdateRequestDTO {
    @SerializedName("name")
    private String name;

    public UserUpdateRequestDTO(String name) {
        this.name = name;
    }
}