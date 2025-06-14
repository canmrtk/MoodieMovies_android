// app/src/main/java/com/moodiemovies/model/RatedFilmDTO.java
package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

public class RatedFilmDTO {
    @SerializedName("film")
    private Film film; // FilmSummaryDTO'ya karşılık gelen Film modelimiz

    @SerializedName("userRating")
    private Integer userRating;

    @SerializedName("userComment")
    private String userComment;

    @SerializedName("ratedDate")
    private String ratedDate;

    // Getters
    public Film getFilm() { return film; }
    public Integer getUserRating() { return userRating; }
    public String getUserComment() { return userComment; }
    public String getRatedDate() { return ratedDate; }
}