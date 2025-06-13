package com.moodiemovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moodiemovies.adapter.FilmAdapter;
import com.moodiemovies.model.Film;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllFilmsActivity extends AppCompatActivity {

    private RecyclerView allFilmsRecycler;
    private final OkHttpClient client = new OkHttpClient();
    private static final String FILMS_API = "http://10.0.2.2:8080/api/v1/films?page=0&size=100";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_films);

        Toolbar toolbar = findViewById(R.id.toolbarAllFilms);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        allFilmsRecycler = findViewById(R.id.allFilmsRecycler);
        allFilmsRecycler.setLayoutManager(new GridLayoutManager(this, 3));

        fetchAllFilms();
    }

    private void fetchAllFilms() {
        Request request = new Request.Builder()
                .url(FILMS_API)
                .addHeader("Authorization", "Bearer " + getToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(AllFilmsActivity.this, "Filmler yüklenemedi", Toast.LENGTH_SHORT).show()
                );
                Log.e("AllFilms", "HTTP Error", e);
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONArray arr = json.getJSONArray("content");
                        List<Film> films = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject f = arr.getJSONObject(i);
                            films.add(new Film(
                                    f.getString("id"),
                                    f.getString("filmName"),   // backend’deki field name
                                    f.getString("imageUrl")     // summary DTO’daki resim URL’i
                            ));
                        }
                        runOnUiThread(() ->
                                allFilmsRecycler.setAdapter(
                                        new FilmAdapter(films, AllFilmsActivity.this::openFilmDetail)
                                )
                        );
                    } catch (Exception ex) {
                        Log.e("AllFilms", "ParseError", ex);
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(AllFilmsActivity.this, "Filmler bulunamadı", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void openFilmDetail(Film film) {
        Intent i = new Intent(this, FilmDetailActivity.class);
        i.putExtra("film_id", film.getId());
        startActivity(i);
    }

    private String getToken() {
        SharedPreferences prefs = getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE);
        return prefs.getString("user_token", null);
    }
}
