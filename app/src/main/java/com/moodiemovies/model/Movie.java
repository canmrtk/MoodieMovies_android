package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("MovieID")
    private int movieId;

    @SerializedName("Title")
    private String title;

    @SerializedName("LetterboxdID")
    private String letterboxdId;

    @SerializedName("ReleaseYear")
    private int releaseYear;

    @SerializedName("Director")
    private String director;

    @SerializedName("Synopsis")
    private String synopsis;

    @SerializedName("PosterURL")
    private String posterUrl;

    @SerializedName("AverageRating")
    private Double averageRating;

    // --- Getter Methods ---
    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getLetterboxdId() {
        return letterboxdId;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    // --- Setter Methods ---
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLetterboxdId(String letterboxdId) {
        this.letterboxdId = letterboxdId;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}