// FilmDetailActivity.java
package com.moodiemovies;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FilmDetailActivity extends AppCompatActivity {

    private ImageView filmPoster;
    private TextView filmTitle, filmYear, filmGenre, filmCountry, filmDuration, filmRating, filmPlot, ratingCountText;
    private RatingBar ratingBar;
    private EditText commentInput;
    private Button submitCommentBtn, favoriteButton;

    private final OkHttpClient client = new OkHttpClient();
    private final String FILM_DETAIL_API = "http://10.0.2.2:8080/api/v1/films/"; // + filmId
    private final String COMMENT_API = "http://10.0.2.2:8080/api/v1/comments";
    private final String FAVORITE_API = "http://10.0.2.2:8080/api/v1/favorites";

    private String filmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        filmPoster = findViewById(R.id.filmPoster);
        filmTitle = findViewById(R.id.filmTitle);
        filmYear = findViewById(R.id.filmYear);
        filmGenre = findViewById(R.id.filmGenre);
        filmCountry = findViewById(R.id.filmCountry);
        filmDuration = findViewById(R.id.filmDuration);
        filmRating = findViewById(R.id.filmRating);
        ratingCountText = findViewById(R.id.ratingCount);
        filmPlot = findViewById(R.id.filmPlot);
        ratingBar = findViewById(R.id.ratingBar);
        commentInput = findViewById(R.id.commentInput);
        submitCommentBtn = findViewById(R.id.submitCommentBtn);
        favoriteButton = findViewById(R.id.favoriteButton);

        filmId = getIntent().getStringExtra("film_id");

        loadFilmDetail(filmId);

        submitCommentBtn.setOnClickListener(v -> sendComment());
        favoriteButton.setOnClickListener(v -> addToFavorites());
    }

    private void loadFilmDetail(String filmId) {
        Request request = new Request.Builder()
                .url(FILM_DETAIL_API + filmId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(FilmDetailActivity.this, "Hata oluştu", Toast.LENGTH_SHORT).show());
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());

                        runOnUiThread(() -> {
                            try {
                                filmTitle.setText(json.getString("title"));
                                filmYear.setText("(" + json.getInt("year") + ")");
                                filmGenre.setText(json.getString("genres"));
                                filmCountry.setText(json.getString("country"));
                                filmDuration.setText(json.getInt("duration") + " dk");
                                filmRating.setText(json.getDouble("rating") + "/10");
                                ratingCountText.setText("(" + json.getInt("ratingCount") + " değerlendirme)");
                                filmPlot.setText(json.getString("plot"));

                                Glide.with(FilmDetailActivity.this)
                                        .load(json.getString("posterUrl"))
                                        .into(filmPoster);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendComment() {
        try {
            JSONObject json = new JSONObject();
            json.put("filmId", filmId);
            json.put("comment", commentInput.getText().toString());
            json.put("rating", ratingBar.getRating());

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), json.toString());

            Request request = new Request.Builder()
                    .url(COMMENT_API)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(FilmDetailActivity.this, "Yorum gönderilemedi", Toast.LENGTH_SHORT).show());
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(FilmDetailActivity.this, "Yorum kaydedildi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FilmDetailActivity.this, "Sunucu hatası", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToFavorites() {
        try {
            JSONObject json = new JSONObject();
            json.put("filmId", filmId);

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), json.toString());

            Request request = new Request.Builder()
                    .url(FAVORITE_API)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(FilmDetailActivity.this, "Favori eklenemedi", Toast.LENGTH_SHORT).show());
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(FilmDetailActivity.this, "Favorilere eklendi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FilmDetailActivity.this, "Sunucu hatası", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
