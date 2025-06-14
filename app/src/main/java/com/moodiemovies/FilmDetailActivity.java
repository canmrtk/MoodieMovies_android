package com.moodiemovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.moodiemovies.model.FilmDetail;
import com.moodiemovies.model.FilmRatingRequestDTO;
import com.moodiemovies.network.ApiClient;
import com.moodiemovies.network.ApiService;
import com.moodiemovies.viewmodel.FilmDetailViewModel;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmDetailActivity extends AppCompatActivity {

    private FilmDetailViewModel viewModel;
    private ImageView filmPoster;
    private TextView filmTitle, filmYear, filmGenre, filmCountry, filmDuration, filmRating, filmPlot;
    private RatingBar ratingBar;
    private EditText commentInput;
    private Button submitReviewBtn, favoriteButton;

    private String filmId;
    private ApiService apiService; // Etkileşimler için
    private static final String TAG = "FilmDetailActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        // View'leri ve API servisini başlat
        initViews();
        apiService = ApiClient.getClient().create(ApiService.class);

        // Intent'ten film ID'sini al
        filmId = getIntent().getStringExtra("film_id");
        if (filmId == null || filmId.isEmpty()) {
            Toast.makeText(this, "Film ID bulunamadı.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ViewModel kurulumu
        viewModel = new ViewModelProvider(this).get(FilmDetailViewModel.class);
        observeViewModel();

        // Buton olayları
        submitReviewBtn.setOnClickListener(v -> submitReview());
        favoriteButton.setOnClickListener(v -> toggleFavorite());

        // Veriyi çek
        viewModel.fetchFilmDetails(filmId);
    }

    private void initViews() {
        filmPoster = findViewById(R.id.filmPoster);
        filmTitle = findViewById(R.id.filmTitle);
        filmYear = findViewById(R.id.filmYear);
        filmGenre = findViewById(R.id.filmGenre);
        filmCountry = findViewById(R.id.filmCountry);
        filmDuration = findViewById(R.id.filmDuration);
        filmRating = findViewById(R.id.filmRating);
        filmPlot = findViewById(R.id.filmPlot);
        ratingBar = findViewById(R.id.ratingBar);
        commentInput = findViewById(R.id.commentInput);
        submitReviewBtn = findViewById(R.id.submitReviewBtn);
        favoriteButton = findViewById(R.id.favoriteButton);
    }

    private void observeViewModel() {
        viewModel.getFilmDetail().observe(this, this::bindFilmData);
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        });
    }

    private void bindFilmData(FilmDetail film) {
        if (film == null) return;

        filmTitle.setText(film.getTitle() != null ? film.getTitle() : "Başlık Yok");
        filmYear.setText(film.getReleaseYear() != null ? "(" + film.getReleaseYear() + ")" : "");

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            filmGenre.setText(String.join(", ", film.getGenres()));
        } else {
            filmGenre.setText("");
        }

        filmCountry.setText(film.getCountry() != null ? film.getCountry() : "");
        filmDuration.setText(film.getFormattedDuration() != null ? film.getFormattedDuration() : "");

        if (film.getAverageRating() != null) {
            filmRating.setText(String.format("%.1f/10", film.getAverageRating().doubleValue()));
        } else {
            filmRating.setText("N/A");
        }

        filmPlot.setText(film.getPlot() != null ? film.getPlot() : "Açıklama bulunmuyor.");

        Glide.with(this)
                .load(film.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error)
                .into(filmPoster);
    }

    private void submitReview() {
        float ratingValue = ratingBar.getRating();
        String commentText = commentInput.getText().toString();

        if (ratingValue == 0) {
            Toast.makeText(this, "Lütfen puan vermek için yıldızları kullanın.", Toast.LENGTH_SHORT).show();
            return;
        }

        FilmRatingRequestDTO requestDTO = new FilmRatingRequestDTO((int) (ratingValue * 2), commentText);

        apiService.rateFilm("Bearer " + getToken(), filmId, requestDTO).enqueue(
                getGenericCallback("Değerlendirme kaydedildi", "Değerlendirme gönderilemedi")
        );
    }

    private void toggleFavorite() {
        apiService.toggleFavorite("Bearer " + getToken(), filmId).enqueue(
                getGenericCallback("Favori durumu güncellendi", "Favori durumu güncellenemedi")
        );
    }

    private Callback<ResponseBody> getGenericCallback(String successMessage, String failureMessage) {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(FilmDetailActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, failureMessage + " - Kod: " + response.code());
                        Toast.makeText(FilmDetailActivity.this, failureMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, failureMessage, t);
                runOnUiThread(() -> Toast.makeText(FilmDetailActivity.this, failureMessage, Toast.LENGTH_SHORT).show());
            }
        };
    }

    private String getToken() {
        SharedPreferences prefs = getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE);
        return prefs.getString("user_token", null);
    }
}