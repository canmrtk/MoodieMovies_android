package com.moodiemovies.model;

public class FilmListSummary {
    private String listId;
    private String name;
    private String tag;
    private int filmCount;
    private int visibility; // 0: private, 1: public
    private UserSummary owner;
    private String coverImageUrl;

    // No-arg constructor
    public FilmListSummary() { }

    // All-args constructor (optional)
    public FilmListSummary(String listId, String name, String tag, int filmCount, int visibility, UserSummary owner, String coverImageUrl) {
        this.listId       = listId;
        this.name         = name;
        this.tag          = tag;
        this.filmCount    = filmCount;
        this.visibility   = visibility;
        this.owner        = owner;
        this.coverImageUrl = coverImageUrl;
    }

    public String getListId() {
        return listId;
    }
    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getFilmCount() {
        return filmCount;
    }
    public void setFilmCount(int filmCount) {
        this.filmCount = filmCount;
    }

    public int getVisibility() {
        return visibility;
    }
    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public UserSummary getOwner() {
        return owner;
    }
    public void setOwner(UserSummary owner) {
        this.owner = owner;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }
    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
}
