// app/src/main/java/com/moodiemovies/model/ProfileData.java
package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProfileData {

    @SerializedName("user")
    private UserDTO user;

    @SerializedName("favorites")
    private List<Film> favorites;

    @SerializedName("lists")
    private List<FilmListSummary> lists;

    @SerializedName("ratings")
    private List<RatedFilmDTO> ratings; // TİPİ GÜNCELLENDİ

    // Getters
    public UserDTO getUser() { return user; }
    public List<Film> getFavorites() { return favorites; }
    public List<FilmListSummary> getLists() { return lists; }
    public List<RatedFilmDTO> getRatings() { return ratings; } // TİPİ GÜNCELLENDİ
}