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
import com.google.gson.Gson;
import com.moodiemovies.adapter.FilmAdapter;
import com.moodiemovies.adapter.ListAdapter;
import com.moodiemovies.adapter.RatingAdapter;
import com.moodiemovies.model.Film;
import com.moodiemovies.model.FilmListSummary;
import com.moodiemovies.model.ProfileData; // Yeni modelimizi import ediyoruz
import com.moodiemovies.model.RatingItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    private TextView favCountText, listCountText, ratingCountText;
    private RecyclerView favoritesRecycler, listsRecycler, ratingsRecycler;

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private static final String TAG = "ProfileActivity";
    // Backend endpoint'leri
    private static final String PROFILE_API = "http://10.0.2.2:8080/api/v1/users/me";
    // Güncelleme için user ID'yi URL'e ekleyeceğiz.
    private static final String UPDATE_API_BASE = "http://10.0.2.2:8080/api/v1/users/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // View'leri bağla
        avatarImage = findViewById(R.id.avatarImage);
        usernameEdit = findViewById(R.id.usernameEdit);
        editButton = findViewById(R.id.editButton);
        favCountText = findViewById(R.id.favCountText);
        listCountText = findViewById(R.id.listCountText);
        ratingCountText = findViewById(R.id.ratingCountText);
        favoritesRecycler = findViewById(R.id.favoritesRecycler);
        listsRecycler = findViewById(R.id.listsRecycler);
        ratingsRecycler = findViewById(R.id.ratingsRecycler);

        // RecyclerView'ları ayarla
        setupRecyclerViews();

        editButton.setOnClickListener(v -> updateUsername());
        loadProfileData();
    }

    private void setupRecyclerViews() {
        favoritesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ratingsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void loadProfileData() {
        Request request = new Request.Builder()
                .url(PROFILE_API)
                .addHeader("Authorization", "Bearer " + getToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Profil verisi alınamadı", e);
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Profil verisi alınamadı.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseString = response.body().string();
                        Log.d(TAG, "Profil Yanıtı: " + responseString);
                        // Yeni model sınıfımızı kullanarak JSON'ı parse ediyoruz.
                        ProfileData profileData = gson.fromJson(responseString, ProfileData.class);
                        runOnUiThread(() -> bindProfile(profileData));
                    } catch (Exception e) {
                        Log.e(TAG, "Profil JSON parse hatası", e);
                    }
                } else {
                    Log.e(TAG, "Profil isteği başarısız: " + response.code());
                }
            }
        });
    }

    private void bindProfile(ProfileData data) {
        if (data == null) return;

        // Kullanıcı bilgileri
        if (data.getUser() != null) {
            usernameEdit.setText(data.getUser().getName()); // 'username' yerine 'name'
            favCountText.setText(String.valueOf(data.getUser().getFavoriteCount()));
            listCountText.setText(String.valueOf(data.getUser().getListCount()));
            ratingCountText.setText(String.valueOf(data.getUser().getRatingCount()));

            // Avatarı yükle
            Glide.with(this)
                    .load(data.getUser().getAvatarImageUrl()) // 'avatarImageUrl' kullanılıyor.
                    .placeholder(R.drawable.placeholder_avatar)
                    .error(R.drawable.placeholder_error)
                    .into(avatarImage);
        }

        // Favori Filmler
        if (data.getFavorites() != null) {
            favoritesRecycler.setAdapter(new FilmAdapter(data.getFavorites(), this::openFilmDetail));
        }

        // Listeler
        if (data.getLists() != null) {
            listsRecycler.setAdapter(new ListAdapter(data.getLists(), this::openListDetail));
        }

        // Puanlamalar
        if (data.getRatings() != null) {
            ratingsRecycler.setAdapter(new RatingAdapter(data.getRatings(), item -> openFilmDetail(new Film(item.getFilmId(), item.getTitle(), item.getPosterUrl()))));
        }
    }

    private void updateUsername() {
        String newName = usernameEdit.getText().toString().trim();
        String userId = getUserId();

        if (newName.isEmpty() || userId == null) {
            Toast.makeText(this, "Kullanıcı adı boş olamaz veya kullanıcı ID bulunamadı.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = UPDATE_API_BASE + userId;

        JSONObject bodyJson = new JSONObject();
        try {
            // Backend'deki UserUpdateRequestDTO 'name' alanı bekliyor.
            bodyJson.put("name", newName);
        } catch (JSONException e) {
            Log.e(TAG, "Güncelleme JSON oluşturma hatası", e);
            return;
        }

        RequestBody body = RequestBody.create(bodyJson.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .put(body) // PUT isteği
                .addHeader("Authorization", "Bearer " + getToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(ProfileActivity.this, "Güncelleme başarısız.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Kullanıcı adı güncellendi.", Toast.LENGTH_SHORT).show();
                        // SharedPreferences'teki adı da güncelle
                        getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE).edit().putString("user_name", newName).apply();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Güncelleme başarısız: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                });
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