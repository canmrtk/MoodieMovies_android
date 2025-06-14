package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.List;

// Bu sınıf, backend'deki FilmDetailDTO'ya karşılık gelir.
public class FilmDetail {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("rating")
    private BigDecimal rating; // GSON'un doğru işlemesi için Double yerine BigDecimal kullanalım.

    @SerializedName("releaseYear")
    private String releaseYear;

    @SerializedName("country")
    private String country;

    @SerializedName("formattedDuration")
    private String formattedDuration;

    @SerializedName("plot")
    private String plot;

    @SerializedName("genres")
    private List<String> genres;

    @SerializedName("averageRating")
    private BigDecimal averageRating;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public String getReleaseYear() { return releaseYear; }
    public void setReleaseYear(String releaseYear) { this.releaseYear = releaseYear; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getFormattedDuration() { return formattedDuration; }
    public void setFormattedDuration(String formattedDuration) { this.formattedDuration = formattedDuration; }
    public String getPlot() { return plot; }
    public void setPlot(String plot) { this.plot = plot; }
    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }
    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }
}