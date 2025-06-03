package com.moodiemovies.model;

public class FilmListSummary {
    private String listId;
    private String name;
    private String tag;
    private int filmCount;
    private int visibility; // 0: Private, 1: Public
    // Figma tasarımındaki kullanıcı bilgileri için ek alanlar gerekebilir,
    // API /api/v1/lists (kullanıcının kendi listeleri) bunu dönmüyor gibi.
    // Eğer popüler listeler için farklı bir DTO varsa o kullanılmalı.
    // Şimdilik Figma'daki "Arjean", "J" gibi kullanıcı bilgileri için
    // bu DTO'ya owner bilgisi ekleyebiliriz veya adapter içinde mock veri kullanabiliriz.
    private UserSummary owner; // Basit kullanıcı bilgisi
    private String coverImageUrl; // Liste kapak resmi için (API'de yok, frontend oluşturacak)

    // Getters and Setters
    public String getListId() { return listId; }
    public void setListId(String listId) { this.listId = listId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public int getFilmCount() { return filmCount; }
    public void setFilmCount(int filmCount) { this.filmCount = filmCount; }
    public int getVisibility() { return visibility; }
    public void setVisibility(int visibility) { this.visibility = visibility; }
    public UserSummary getOwner() { return owner; }
    public void setOwner(UserSummary owner) { this.owner = owner; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
}