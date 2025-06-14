package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;

// Bu sınıf, backend'deki FilmSummaryDTO'ya karşılık gelir.
public class Film {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("imageUrl") // Backend'den gelen alan adı "imageUrl"
    private String imageUrl; // Değişken adını da imageUrl yapalım.

    public Film() {}

    public Film(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // posterUrl yerine imageUrl kullanacağız.
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}