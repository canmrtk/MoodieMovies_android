package com.moodiemovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
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

public class ListDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView listCoverImage;
    private TextView listNameText, listDescText;
    private RecyclerView listFilmsRecycler;

    private final OkHttpClient client = new OkHttpClient();
    private static final String LIST_DETAIL_API = "http://10.0.2.2:8080/api/v1/lists/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        // View’leri bağla
        toolbar          = findViewById(R.id.toolbar);
        listCoverImage   = findViewById(R.id.listCoverImage);
        listNameText     = findViewById(R.id.listNameText);
        listDescText     = findViewById(R.id.listDescText);
        listFilmsRecycler= findViewById(R.id.listFilmsRecycler);

        // Toolbar back tuşu
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // RecyclerView grid yapısı
        listFilmsRecycler.setLayoutManager(new GridLayoutManager(this, 3));

        // list_id intent’ten al
        String listId = getIntent().getStringExtra("list_id");
        fetchListDetail(listId);
    }

    private void fetchListDetail(String id) {
        String token = getToken();
        Request request = new Request.Builder()
                .url(LIST_DETAIL_API + id)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(ListDetailActivity.this, "Liste yüklenirken hata", Toast.LENGTH_SHORT).show()
                );
                Log.e("ListDetail", "HTTP failed", e);
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        runOnUiThread(() -> bindListDetail(json));
                    } catch (Exception ex) {
                        Log.e("ListDetail", "JSON parse error", ex);
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(ListDetailActivity.this, "Liste bulunamadı", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void bindListDetail(JSONObject json) {
        try {
            // Kapak görseli
            String coverUrl = json.optString("coverImageUrl");
            Glide.with(this)
                    .load(coverUrl)
                    .into(listCoverImage);

            // Başlık & açıklama
            listNameText.setText(json.getString("name"));
            listDescText.setText(json.optString("description", ""));

            // Film dizisi
            JSONArray filmsArr = json.getJSONArray("films");
            List<Film> films = new ArrayList<>();
            for (int i = 0; i < filmsArr.length(); i++) {
                JSONObject f = filmsArr.getJSONObject(i);
                films.add(new Film(
                        f.getString("id"),
                        f.getString("title"),
                        f.getString("posterUrl")
                ));
            }

            // Adapter & tıklama
            FilmAdapter adapter = new FilmAdapter(films, this::openFilmDetail);
            listFilmsRecycler.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("ListDetail", "bind error", e);
        }
    }

    private void openFilmDetail(Film film) {
        Intent intent = new Intent(this, FilmDetailActivity.class);
        intent.putExtra("film_id", film.getId());
        startActivity(intent);
    }

    private String getToken() {
        SharedPreferences prefs = getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE);
        return prefs.getString("user_token", null);
    }
}
