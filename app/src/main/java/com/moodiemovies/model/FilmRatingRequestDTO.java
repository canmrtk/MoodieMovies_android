package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

public class FilmRatingRequestDTO {
    @SerializedName("rating")
    private Integer rating;

    @SerializedName("comment")
    private String comment;

    public FilmRatingRequestDTO(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
}