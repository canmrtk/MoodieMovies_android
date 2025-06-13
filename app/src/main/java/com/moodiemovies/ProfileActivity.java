package com.moodiemovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.moodiemovies.adapter.FilmAdapter;
import com.moodiemovies.adapter.ListAdapter;
import com.moodiemovies.adapter.RatingAdapter;
import com.moodiemovies.model.Film;
import com.moodiemovies.model.FilmListSummary;
import com.moodiemovies.model.RatingItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView avatarImage;
    private TextInputEditText usernameEdit;
    private ImageView editButton;
    private RecyclerView favoritesRecycler, listsRecycler, ratingsRecycler;

    private final OkHttpClient client = new OkHttpClient();
    private static final String PROFILE_API = "http://10.0.2.2:8080/api/v1/users/me";
    private static final String UPDATE_API  = "http://10.0.2.2:8080/api/v1/users/me";
    private static final String AVATAR_BASE_URL = "http://10.0.2.2:8080/api/v1/avatars/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        avatarImage      = findViewById(R.id.avatarImage);
        usernameEdit     = findViewById(R.id.usernameEdit);
        editButton       = findViewById(R.id.editButton);
        favoritesRecycler= findViewById(R.id.favoritesRecycler);
        listsRecycler    = findViewById(R.id.listsRecycler);
        ratingsRecycler  = findViewById(R.id.ratingsRecycler);

        favoritesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ratingsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        editButton.setOnClickListener(v -> updateUsername());
        loadProfileData();
    }

    private void loadProfileData() {
        Request request = new Request.Builder()
                .url(PROFILE_API)
                .addHeader("Authorization", "Bearer " + getToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e("Profile", "HTTP fail", e);
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        runOnUiThread(() -> bindProfile(json));
                    } catch (Exception e) {
                        Log.e("Profile", "JSON parse", e);
                    }
                }
            }
        });
    }

    private void bindProfile(JSONObject json) {
        try {
            // Avatar
            String avatarId = json.getString("avatarId");
            Glide.with(this)
                    .load(AVATAR_BASE_URL + avatarId)
                    .into(avatarImage);

            // Kullanıcı adı
            usernameEdit.setText(json.getString("username"));

            // Favori Filmler
            JSONArray favs = json.getJSONArray("favorites");
            List<Film> favFilms = new ArrayList<>();
            for (int i = 0; i < favs.length(); i++) {
                JSONObject f = favs.getJSONObject(i);
                Film film = new Film();
                film.setId(f.getString("id"));
                film.setTitle(f.getString("title"));
                film.setPosterUrl(f.getString("posterUrl"));
                favFilms.add(film);
            }
            favoritesRecycler.setAdapter(new FilmAdapter(favFilms, this::openFilmDetail));

            // Listeler
            JSONArray listsArr = json.getJSONArray("lists");
            List<FilmListSummary> listSummaries = new ArrayList<>();
            for (int i = 0; i < listsArr.length(); i++) {
                JSONObject l = listsArr.getJSONObject(i);
                FilmListSummary list = new FilmListSummary();
                list.setListId(l.getString("id"));
                list.setName(l.getString("name"));
                list.setCoverImageUrl(l.getString("coverImageUrl"));
                listSummaries.add(list);
            }
            listsRecycler.setAdapter(new ListAdapter(listSummaries, this::openListDetail));

            // Puanlamalar
            JSONArray ratingsArr = json.getJSONArray("ratings");
            List<RatingItem> ratingItems = new ArrayList<>();
            for (int i = 0; i < ratingsArr.length(); i++) {
                JSONObject r = ratingsArr.getJSONObject(i);
                ratingItems.add(new RatingItem(
                        r.getString("filmId"),
                        r.getString("title"),
                        r.getString("posterUrl"),
                        r.getDouble("rating")
                ));
            }
            ratingsRecycler.setAdapter(new RatingAdapter(ratingItems, item -> {
                Film film = new Film(item.getFilmId(), item.getTitle(), item.getPosterUrl());
                openFilmDetail(film);
            }));

        } catch (Exception e) {
            Log.e("Profile", "bind error", e);
        }
    }

    private void updateUsername() {
        try {
            JSONObject body = new JSONObject();
            body.put("username", usernameEdit.getText().toString());

            RequestBody rb = RequestBody.create(
                    MediaType.parse("application/json"), body.toString()
            );
            Request req = new Request.Builder()
                    .url(UPDATE_API)
                    .put(rb)
                    .addHeader("Authorization", "Bearer " + getToken())
                    .build();

            client.newCall(req).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) { }
                @Override public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(() -> {
                        Toast.makeText(ProfileActivity.this,
                                response.isSuccessful() ? "Güncellendi" : "Hata",
                                Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void openFilmDetail(Film film) {
        Intent i = new Intent(this, FilmDetailActivity.class);
        i.putExtra("film_id", film.getFilmId());
        startActivity(i);
    }

    private void openListDetail(FilmListSummary list) {
        Intent i = new Intent(this, ListDetailActivity.class);
        i.putExtra("list_id", list.getListId());
        startActivity(i);
    }

    private String getToken() {
        return getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE)
                .getString("user_token", null);
    }
}
