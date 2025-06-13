package com.moodiemovies.model;

public class RatingItem {
    private String filmId;
    private String title;
    private String posterUrl;
    private double rating;

    // No-arg constructor
    public RatingItem() {}

    // Parametreli constructor
    public RatingItem(String filmId, String title, String posterUrl, double rating) {
        this.filmId    = filmId;
        this.title     = title;
        this.posterUrl = posterUrl;
        this.rating    = rating;
    }

    // Getter / Setter
    public String getFilmId() {
        return filmId;
    }
    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
}
