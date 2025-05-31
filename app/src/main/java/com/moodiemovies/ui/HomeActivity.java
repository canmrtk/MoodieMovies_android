package com.moodiemovies.ui;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.moodiemovies.R;
import com.moodiemovies.network.ApiService;
import com.moodiemovies.network.Film;
import com.moodiemovies.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView filmsRecyclerView;
    private FilmAdapter filmAdapter;
    private List<Film> filmList = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private void onFilmClicked(Film film) {
        // Şimdilik sadece Toast
        Toast.makeText(this, "Film: " + film.getTitle(), Toast.LENGTH_SHORT).show();


        // Intent intent = new Intent(this, FilmDetailActivity.class);
        // intent.putExtra("film_id", film.getId());
        // startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Navbar ve Drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Hamburger menü seçenekleri
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_profile) {
                Toast.makeText(this, "Profil", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.menu_films) {
                Toast.makeText(this, "Filmler", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.menu_list) {
                Toast.makeText(this, "Listem", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.menu_forum) {
                Toast.makeText(this, "Forum", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.menu_logout) {
                Toast.makeText(this, "Çıkış yapılıyor...", Toast.LENGTH_SHORT).show();
                // logout işlemi burada yapılır
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        ImageView posterImageView = findViewById(R.id.posterImageView);
        // posterImageView.setImageResource(R.drawable.godfather);

        // Sana Film Öneriyim mi? butonu
        Button aiSuggestionButton = findViewById(R.id.aiSuggestionButton);
        aiSuggestionButton.setOnClickListener(v -> {

            Toast.makeText(this, "AI servis ile öneri yakında!", Toast.LENGTH_SHORT).show();
        });

        // RecyclerView
        filmsRecyclerView = findViewById(R.id.filmsRecyclerView);
        filmsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        filmAdapter = new FilmAdapter(filmList, this::onFilmClicked);
        filmsRecyclerView.setAdapter(filmAdapter);

        // Filmleri çek
        fetchFilms();
    }

    // Hamburger menü için override
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Backend'den film listesini çek
    private void fetchFilms() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Film>> call = apiService.getLastWatchedFilms();
        call.enqueue(new Callback<List<Film>>() {
            @Override
            public void onResponse(Call<List<Film>> call, Response<List<Film>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    filmList.clear();
                    filmList.addAll(response.body());
                    filmAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HomeActivity.this, "Film verisi alınamadı!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Film>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Bağlantı hatası!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Film kartına tıklanınca detay ekranına yönlendir
    /*private void onFilmClicked(Film film) {
        Intent intent = new Intent(this, FilmDetailActivity.class);
        intent.putExtra("film_id", film.getId());
        startActivity(intent);
    }*/
}
