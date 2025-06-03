package com.moodiemovies.model;

public class Film {
    private String id;
    private String title;
    private String posterUrl; // Backend'deki FilmSummaryDTO.imageUrl'a karşılık gelecek (tam URL)

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getPosterUrl() { return posterUrl; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}