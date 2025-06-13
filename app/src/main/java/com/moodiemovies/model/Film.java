package com.moodiemovies.model;

public class Film {
    private String id;
    private String title;
    private String posterUrl;

    // 1a. Boş yapıcı (no-arg) ekleyin, böylece new Film() çalışır.
    public Film() {}

    // 1b. Parametreli yapıcı hâlâ kalsın:
    public Film(String id, String title, String posterUrl) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
    }

    // 1c. Alias olarak getFilmId() ekleyin:
    public String getFilmId() {
        return id;
    }

    // Var olan getter/setter’lar:
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}
