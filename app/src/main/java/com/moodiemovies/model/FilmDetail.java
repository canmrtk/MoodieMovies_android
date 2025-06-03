// com.moodiemovies.model.FilmDetail.java
package com.moodiemovies.model;

import java.util.List;
// Backend BigDecimal kullandığı için Double veya String olarak alabiliriz.
// Gson genellikle BigDecimal'ı Double'a mapler.

public class FilmDetail {
    private String id;
    private String title;
    private String imageUrl; // Backend'deki FilmDetailDTO.imageUrl'a karşılık gelecek (tam URL)
    private Double rating;   // Backend: BigDecimal
    private String releaseYear;
    private String country;
    private String formattedDuration;
    private String plot;
    private List<String> genres;

    // Getters and Setters for all fields
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
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
}