package com.moodiemovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.moodiemovies.adapter.MovieAdapter;
import com.moodiemovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView hamburgerMenu;
    private FloatingActionButton fabAi;
    private RecyclerView recommendationsRecyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        hamburgerMenu = findViewById(R.id.hamburger_menu);
        fabAi = findViewById(R.id.fab_ai);
        recommendationsRecyclerView = findViewById(R.id.recommendationsRecyclerView);

        setupDrawer();
        setupRecyclerView();

        fabAi.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Kişilik testi başlıyor...", Toast.LENGTH_SHORT).show();
            // TODO: TestActivity'e yönlendirme yapılacak.
            // Intent intent = new Intent(HomeActivity.this, TestActivity.class);
            // startActivity(intent);
        });
    }

    private void setupDrawer() {
        hamburgerMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(item -> {
            // Menü tıklama olayları burada yönetilir.
            int id = item.getItemId();
            if (id == R.id.drawer_logout) {
                // TODO: Çıkış yapma işlemleri
                Toast.makeText(this, "Çıkış yapıldı.", Toast.LENGTH_SHORT).show();
                // LoginActivity'e geri dön
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            // Diğer menü elemanları için de benzer şekilde...

            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private void setupRecyclerView() {
        movieList = new ArrayList<>();
        // TODO: Bu liste ViewModel aracılığıyla backend'den gelen verilerle doldurulacak.
        // Şimdilik test için manuel veri ekleyelim.
        // Movie testMovie = new Movie();
        // testMovie.setTitle("Test Filmi");
        // testMovie.setPosterUrl("https://...poster.jpg");
        // movieList.add(testMovie);

        movieAdapter = new MovieAdapter(this, movieList);
        recommendationsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recommendationsRecyclerView.setAdapter(movieAdapter);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}