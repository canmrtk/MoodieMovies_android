// app/src/main/java/com/moodiemovies/model/FilmListSummary.java
package com.moodiemovies.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FilmListSummary {
    @SerializedName("listId")
    private String listId;
    @SerializedName("name")
    private String name;
    @SerializedName("tag")
    private String tag;
    @SerializedName("filmCount")
    private int filmCount;
    @SerializedName("visibility")
    private int visibility;
    @SerializedName("owner")
    private UserSummary owner;
    @SerializedName("films")
    private List<Film> films; // İlk birkaç filmin öngörünümü için

    // Getters and Setters
    public String getListId() { return listId; }
    public String getName() { return name; }
    public String getTag() { return tag; }
    public int getFilmCount() { return filmCount; }
    public int getVisibility() { return visibility; }
    public UserSummary getOwner() { return owner; }
    public List<Film> getFilms() { return films; }

    // Kapak resmini listedeki ilk filmden al
    public String getCoverImageUrl() {
        if (films != null && !films.isEmpty() && films.get(0) != null) {
            return films.get(0).getImageUrl();
        }
        return null;
    }
}