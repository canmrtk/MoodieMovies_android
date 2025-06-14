package com.moodiemovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.moodiemovies.model.ProfileData;
import com.moodiemovies.model.RatedFilmDTO;
import com.moodiemovies.model.UserUpdateRequestDTO;
import com.moodiemovies.network.ApiClient;
import com.moodiemovies.network.ApiService;

import java.io.IOException;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView avatarImage;
    private TextInputEditText usernameEdit;
    private ImageView editButton;
    private TextView favCountText, listCountText, ratingCountText;
    private RecyclerView favoritesRecycler, listsRecycler, ratingsRecycler;

    private ApiService apiService;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        apiService = ApiClient.getClient().create(ApiService.class);
        initViews();
        setupRecyclerViews();

        editButton.setOnClickListener(v -> updateUsername());
        loadProfileData();
    }

    private void initViews() {
        avatarImage = findViewById(R.id.avatarImage);
        usernameEdit = findViewById(R.id.usernameEdit);
        editButton = findViewById(R.id.editButton);
        favCountText = findViewById(R.id.favCountText);
        listCountText = findViewById(R.id.listCountText);
        ratingCountText = findViewById(R.id.ratingCountText);
        favoritesRecycler = findViewById(R.id.favoritesRecycler);
        listsRecycler = findViewById(R.id.listsRecycler);
        ratingsRecycler = findViewById(R.id.ratingsRecycler);
    }

    private void setupRecyclerViews() {
        favoritesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ratingsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void loadProfileData() {
        apiService.getProfileData("Bearer " + getToken()).enqueue(new Callback<ProfileData>() {
            @Override
            public void onResponse(@NonNull Call<ProfileData> call, @NonNull Response<ProfileData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileData profileData = response.body();
                    runOnUiThread(() -> bindProfile(profileData));
                } else {
                    Log.e(TAG, "Profil isteği başarısız: " + response.code());
                    runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Profil verisi alınamadı.", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileData> call, @NonNull Throwable t) {
                Log.e(TAG, "Profil verisi alınamadı", t);
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Ağ hatası: " + t.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void bindProfile(ProfileData data) {
        if (data == null) return;

        if (data.getUser() != null) {
            usernameEdit.setText(data.getUser().getName());
            favCountText.setText(String.valueOf(data.getUser().getFavoriteCount()));
            listCountText.setText(String.valueOf(data.getUser().getListCount()));
            ratingCountText.setText(String.valueOf(data.getUser().getRatingCount()));

            Glide.with(this)
                    .load(data.getUser().getAvatarImageUrl())
                    .placeholder(R.drawable.placeholder_avatar)
                    .error(R.drawable.placeholder_error)
                    .into(avatarImage);
        }

        if (data.getFavorites() != null) {
            favoritesRecycler.setAdapter(new FilmAdapter(data.getFavorites(), this::openFilmDetail));
        }

        if (data.getLists() != null) {
            listsRecycler.setAdapter(new ListAdapter(data.getLists(), this::openListDetail));
        }

        if (data.getRatings() != null) {
            // RatedFilmDTO'yu eski RatingItem modeline dönüştürmemiz gerekiyor.
            // Çünkü RatingAdapter hala onu bekliyor.
            ratingsRecycler.setAdapter(new RatingAdapter(
                    data.getRatings().stream().map(ratedFilm -> new com.moodiemovies.model.RatingItem(
                            ratedFilm.getFilm().getId(),
                            ratedFilm.getFilm().getTitle(),
                            ratedFilm.getFilm().getImageUrl(),
                            ratedFilm.getUserRating() != null ? ratedFilm.getUserRating() : 0.0
                    )).collect(Collectors.toList()),
                    item -> openFilmDetail(new Film(item.getFilmId(), item.getTitle(), item.getPosterUrl()))
            ));
        }
    }

    private void updateUsername() {
        String newName = usernameEdit.getText().toString().trim();
        String userId = getUserId();

        if (newName.isEmpty() || userId == null) {
            Toast.makeText(this, "Kullanıcı adı boş olamaz.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserUpdateRequestDTO updateRequest = new UserUpdateRequestDTO(newName);
        apiService.updateUserProfile("Bearer " + getToken(), userId, updateRequest).enqueue(new Callback<com.moodiemovies.model.UserDTO>() {
            @Override
            public void onResponse(@NonNull Call<com.moodiemovies.model.UserDTO> call, @NonNull Response<com.moodiemovies.model.UserDTO> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Kullanıcı adı güncellendi.", Toast.LENGTH_SHORT).show();
                        getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE).edit().putString("user_name", newName).apply();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Güncelleme başarısız: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<com.moodiemovies.model.UserDTO> call, @NonNull Throwable t) {
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Güncelleme başarısız.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void openFilmDetail(Film film) {
        Intent i = new Intent(this, FilmDetailActivity.class);
        i.putExtra("film_id", film.getId());
        startActivity(i);
    }

    private void openListDetail(FilmListSummary list) {
        Intent i = new Intent(this, ListDetailActivity.class);
        i.putExtra("list_id", list.getListId());
        startActivity(i);
    }

    private String getToken() {
        return getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE).getString("user_token", null);
    }

    private String getUserId() {
        return getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE).getString("user_id", null);
    }
}