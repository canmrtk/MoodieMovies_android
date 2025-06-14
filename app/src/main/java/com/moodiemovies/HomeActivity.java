package com.moodiemovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moodiemovies.adapter.FilmAdapter;
import com.moodiemovies.model.Film;
import com.moodiemovies.viewmodel.HomeViewModel; // Yeni ViewModel'i import et

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private HomeViewModel homeViewModel;
    private FilmAdapter filmAdapter;
    private List<Film> filmList = new ArrayList<>();
    private RecyclerView recyclerPopularMovies;
    private Button btnRecommend;
    private ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // UI Elemanlarını Bağlama
        btnRecommend = findViewById(R.id.btnRecommend);
        menuIcon = findViewById(R.id.menuIcon);
        recyclerPopularMovies = findViewById(R.id.recyclerPopularMovies);

        // RecyclerView ve Adapter Kurulumu
        recyclerPopularMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        filmAdapter = new FilmAdapter(filmList, this::openFilmDetail);
        recyclerPopularMovies.setAdapter(filmAdapter);

        // ViewModel Kurulumu
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // LiveData Gözlemcileri
        observeViewModel();

        // Buton ve Menü Tıklama Olayları
        btnRecommend.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PersonalityTestActivity.class);
            startActivity(intent);
        });
        menuIcon.setOnClickListener(this::showPopupMenu);

        // Veriyi Çekme
        homeViewModel.fetchPopularFilms(10); // Ana sayfada 10 popüler film gösterelim
    }

    private void observeViewModel() {
        homeViewModel.getPopularFilms().observe(this, films -> {
            if (films != null) {
                filmList.clear();
                filmList.addAll(films);
                filmAdapter.notifyDataSetChanged();
            }
        });

        homeViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openFilmDetail(Film film) {
        Intent intent = new Intent(HomeActivity.this, FilmDetailActivity.class);
        intent.putExtra("film_id", film.getId());
        startActivity(intent);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_navbar, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
        popupMenu.show();
    }

    private boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (itemId == R.id.menu_films) {
            startActivity(new Intent(this, AllFilmsActivity.class));
            return true;
        } else if (itemId == R.id.menu_logout) {
            logoutUser();
            return true;
        }
        return false;
    }

    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Çıkış yapıldı", Toast.LENGTH_SHORT).show();
    }
}