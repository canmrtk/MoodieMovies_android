package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Bu sınıf, /api/v1/users/me endpoint'inden gelen tüm veriyi temsil eder.
public class ProfileData {

    @SerializedName("user")
    private UserDTO user;

    @SerializedName("favorites")
    private List<Film> favorites;

    @SerializedName("lists")
    private List<FilmListSummary> lists;

    @SerializedName("ratings")
    private List<RatingItem> ratings;

    // Getters
    public UserDTO getUser() { return user; }
    public List<Film> getFavorites() { return favorites; }
    public List<FilmListSummary> getLists() { return lists; }
    public List<RatingItem> getRatings() { return ratings; }
}