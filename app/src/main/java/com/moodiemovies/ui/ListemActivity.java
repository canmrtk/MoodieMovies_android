package com.moodiemovies.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem; // Hamburger menü için
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle; // Drawer için
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat; // Drawer için
import androidx.drawerlayout.widget.DrawerLayout; // Drawer için
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView; // Drawer için
import com.moodiemovies.R;
import com.moodiemovies.model.FilmListSummary;
import com.moodiemovies.network.ApiService;
import com.moodiemovies.network.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListemActivity extends AppCompatActivity implements PopularListAdapter.OnListClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ListemActivity";

    private Button btnCreateNewList;
    private TextView tvMorePopularLists;
    private RecyclerView recyclerViewPopularLists;
    private PopularListAdapter popularListAdapter;
    private List<FilmListSummary> popularFilmLists = new ArrayList<>();
    private TextView tvNoPopularLists;
    private ProgressBar progressBarListem;
    private String authToken;

    private DrawerLayout drawerLayoutListem; // DrawerLayout için
    private ActionBarDrawerToggle drawerToggleListem; // DrawerToggle için

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listem);

        Toolbar toolbar = findViewById(R.id.toolbarListem);
        setSupportActionBar(toolbar);

        drawerLayoutListem = findViewById(R.id.drawerLayoutListem); // XML'de DrawerLayout ID'si böyle olmalı
        NavigationView navigationViewListem = findViewById(R.id.navigationViewListem); // XML'de NavigationView ID'si

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Özel başlığımız var
        }

        // Drawer Toggle Kurulumu
        drawerToggleListem = new ActionBarDrawerToggle(this, drawerLayoutListem, toolbar, R.string.open, R.string.close);
        drawerLayoutListem.addDrawerListener(drawerToggleListem);
        drawerToggleListem.syncState();
        if (navigationViewListem != null) {
            navigationViewListem.setNavigationItemSelectedListener(this);
        }


        SharedPreferences prefs = getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE);
        authToken = prefs.getString("authToken", null);

        btnCreateNewList = findViewById(R.id.btnCreateNewList);
        tvMorePopularLists = findViewById(R.id.tvMorePopularLists);
        recyclerViewPopularLists = findViewById(R.id.recyclerViewPopularLists);
        tvNoPopularLists = findViewById(R.id.tvNoPopularLists);
        progressBarListem = findViewById(R.id.progressBarListem);

        btnCreateNewList.setOnClickListener(v -> {
            if (authToken == null) {
                Toast.makeText(this, "Liste oluşturmak için giriş yapmalısınız.", Toast.LENGTH_SHORT).show();
                navigateToLogin();
                return;
            }
            // TODO: Yeni liste oluşturma ekranına yönlendir (CreateListActivity)
            // Intent intent = new Intent(ListemActivity.this, CreateListActivity.class);
            // startActivity(intent);
            Toast.makeText(ListemActivity.this, "Yeni Liste Oluşturma Ekranı Yakında!", Toast.LENGTH_SHORT).show();
        });

        tvMorePopularLists.setOnClickListener(v -> {
            // TODO: Tüm popüler listeler ekranına yönlendir
            Toast.makeText(ListemActivity.this, "Tüm Popüler Listeler Ekranı Yakında!", Toast.LENGTH_SHORT).show();
        });

        setupRecyclerView();

        if (authToken != null) {
            fetchMyLists(); // "Popüler Listeler" yerine kullanıcının kendi listelerini çekiyoruz şimdilik
        } else {
            showEmptyView("Listeleri görmek için giriş yapmalısınız.");
            btnCreateNewList.setEnabled(false);
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ListemActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupRecyclerView() {
        recyclerViewPopularLists.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        popularListAdapter = new PopularListAdapter(this, popularFilmLists, this);
        recyclerViewPopularLists.setAdapter(popularListAdapter);
    }

    private void fetchMyLists() {
        if (authToken == null) return;
        showLoading(true);

        Log.d(TAG, "Kullanıcının listeleri çekiliyor...");
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<FilmListSummary>> call = apiService.getMyLists("Bearer " + authToken);

        call.enqueue(new Callback<List<FilmListSummary>>() {
            @Override
            public void onResponse(@NonNull Call<List<FilmListSummary>> call, @NonNull Response<List<FilmListSummary>> response) {
                showLoading(false);
                Log.d(TAG, "API Yanıt Kodu (getMyLists): " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isEmpty()) {
                        popularListAdapter.updateData(response.body());
                        showEmptyView(null); // Veri varsa boş görünümü gizle
                    } else {
                        showEmptyView("Henüz oluşturulmuş bir listeniz bulunmuyor.");
                    }
                } else {
                    handleApiError(response, "Listeler alınamadı.");
                    showEmptyView("Listeler yüklenirken bir hata oluştu.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FilmListSummary>> call, @NonNull Throwable t) {
                showLoading(false);
                Log.e(TAG, "API Çağrı Hatası (getMyLists): " + t.getMessage(), t);
                Toast.makeText(ListemActivity.this, "Bağlantı hatası: " + t.getMessage(), Toast.LENGTH_LONG).show();
                showEmptyView("Listeler yüklenirken bir hata oluştu.");
            }
        });
    }

    @Override
    public void onListClick(FilmListSummary filmList) {
        Toast.makeText(ListemActivity.this, "Liste tıklandı: " + filmList.getName(), Toast.LENGTH_SHORT).show();
        // TODO: Liste detay ekranına yönlendir (ListDetailActivity)
        // Intent intent = new Intent(ListemActivity.this, ListDetailActivity.class);
        // intent.putExtra("LIST_ID_EXTRA", filmList.getListId());
        // startActivity(intent);
    }

    private void showLoading(boolean isLoading) {
        if (progressBarListem != null) {
            progressBarListem.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        if (recyclerViewPopularLists != null) {
            recyclerViewPopularLists.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            if(isLoading) tvNoPopularLists.setVisibility(View.GONE); // Yüklenirken boş mesajı da gizle
        }
    }

    private void showEmptyView(String message) {
        if (tvNoPopularLists != null) {
            if (message != null && popularFilmLists.isEmpty()) { // Sadece liste gerçekten boşsa ve mesaj varsa
                tvNoPopularLists.setText(message);
                tvNoPopularLists.setVisibility(View.VISIBLE);
                if (recyclerViewPopularLists != null) recyclerViewPopularLists.setVisibility(View.GONE);
            } else {
                tvNoPopularLists.setVisibility(View.GONE);
                // Liste boş değilse RecyclerView'ın görünür olduğundan emin ol
                if (recyclerViewPopularLists != null && !popularFilmLists.isEmpty()) {
                    recyclerViewPopularLists.setVisibility(View.VISIBLE);
                } else if (recyclerViewPopularLists != null && popularFilmLists.isEmpty() && message == null){
                    // Veri geldi ama liste boş (örneğin hiç listesi yok), yine de no popular lists mesajı kalabilir.
                    // Veya özel bir "Hiç listeniz yok, bir tane oluşturun!" mesajı gösterilebilir.
                    tvNoPopularLists.setText("Oluşturulmuş bir listeniz bulunmuyor.");
                    tvNoPopularLists.setVisibility(View.VISIBLE);
                    recyclerViewPopularLists.setVisibility(View.GONE);
                }
            }
        }
    }

    private void handleApiError(Response<?> response, String defaultMessage) {
        String errorMsg = defaultMessage;
        if (response.errorBody() != null) {
            try {
                errorMsg += " Hata: " + response.code() + " - " + response.errorBody().string();
            } catch (IOException e) {
                Log.e(TAG, "Error body parse error", e);
            }
        } else if (response.message() != null && !response.message().isEmpty()){
            errorMsg += " Hata: " + response.code() + " - " + response.message();
        } else {
            errorMsg += " Hata: " + response.code();
        }
        Toast.makeText(ListemActivity.this, errorMsg, Toast.LENGTH_LONG).show();
        Log.e(TAG, "API Hatası: " + errorMsg);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Hamburger menü tıklamasını işle
        if (drawerToggleListem.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Drawer menüdeki tıklamaları işle
        int id = item.getItemId();
        if (id == R.id.menu_home) { // Home için ID'niz neyse o olmalı
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish(); // ListemActivity'yi kapatıp Home'a dön
        } else if (id == R.id.menu_profile) {
            Toast.makeText(this, "Profil Tıklandı", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_films) {
            Toast.makeText(this, "Filmler Tıklandı", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_list) {
            // Zaten bu ekrandayız
            Toast.makeText(this, "Zaten Listelerim Ekranındasınız", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_forum) {
            Toast.makeText(this, "Forum Tıklandı", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_logout) {
            performLogout();
        }
        drawerLayoutListem.closeDrawer(GravityCompat.START);
        return true;
    }

    private void performLogout() {
        Toast.makeText(this, "Çıkış yapılıyor...", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("authToken");
        editor.apply();
        navigateToLogin();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutListem.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutListem.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}