package com.moodiemovies.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.moodiemovies.R;
import com.moodiemovies.model.Film; // FilmSummaryDTO için modelimiz (doğru paketten import)
import com.moodiemovies.model.FilmPage; // Sayfalanmış yanıt için modelimiz
import com.moodiemovies.network.ApiService;
import com.moodiemovies.network.RetrofitClient;
import com.squareup.picasso.Picasso; // Picasso importu

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements FilmAdapter.OnFilmClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    private static final int PAGE_SIZE = 10;
    private static final String DEFAULT_SORT = "id,asc";

    private RecyclerView filmsRecyclerView;
    private FilmAdapter filmAdapter;
    private List<Film> filmList = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Button aiSuggestionButton;
    private ImageView posterImageView;
    private ProgressBar progressBarHome;
    private TextView tvEmptyViewHome;

    private String authToken;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar_home); // XML'deki Toolbar ID'niz
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout); // XML'deki DrawerLayout ID'niz
        navigationView = findViewById(R.id.navigationView); // XML'deki NavigationView ID'niz
        aiSuggestionButton = findViewById(R.id.aiSuggestionButton);
        posterImageView = findViewById(R.id.posterImageView);
        filmsRecyclerView = findViewById(R.id.filmsRecyclerView);
        progressBarHome = findViewById(R.id.progressBarHome);
        tvEmptyViewHome = findViewById(R.id.tvEmptyViewHome);

        SharedPreferences prefs = getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE);
        authToken = prefs.getString("authToken", null);

        if (authToken == null) {
            Toast.makeText(this, "Lütfen giriş yapın.", Toast.LENGTH_LONG).show();
            navigateToLogin();
            return;
        }

        setupDrawer(toolbar);
        setupRecyclerView();
        setupListeners();

        loadFilmsFirstPage();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupDrawer(Toolbar toolbar) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Ana Sayfa");
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupRecyclerView() {
        filmsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        filmAdapter = new FilmAdapter(filmList, this); // 'this' OnFilmClickListener olarak HomeActivity'yi verir
        filmsRecyclerView.setAdapter(filmAdapter);

        filmsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && !isLastPage && linearLayoutManager != null) {
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    if (totalItemCount <= (lastVisibleItemPosition + 3)) { // Son 3 item kala yükle
                        Log.d(TAG, "Listenin sonuna yaklaşıldı, bir sonraki sayfa yükleniyor.");
                        currentPage++;
                        fetchFilms();
                    }
                }
            }
        });
    }

    private void setupListeners() {
        aiSuggestionButton.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "AI servis ile öneri yakında!", Toast.LENGTH_SHORT).show();
            // TODO: TestIntroActivity veya RecommendationActivity'ye yönlendir
        });
    }

    private void loadFilmsFirstPage() {
        isLoading = true;
        currentPage = 0;
        isLastPage = false;
        filmList.clear();
        filmAdapter.notifyDataSetChanged(); // Önce listeyi temizle ve adapter'ı uyar
        showLoading(true);
        if (tvEmptyViewHome != null) tvEmptyViewHome.setVisibility(View.GONE);
        fetchFilms();
    }

    private void fetchFilms() {
        if (authToken == null) {
            Log.e(TAG, "Token yok, film çekilemiyor.");
            showLoading(false);
            showEmptyView("Giriş yapmanız gerekiyor.");
            return;
        }

        isLoading = true;
        if (currentPage == 0) showLoading(true); // Sadece ilk sayfa yüklenirken ana progress bar

        Log.d(TAG, "Filmler çekiliyor: Sayfa " + currentPage + ", Boyut: " + PAGE_SIZE + ", Sıralama: " + DEFAULT_SORT);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<FilmPage> call = apiService.getAllFilms("Bearer " + authToken, currentPage, PAGE_SIZE, DEFAULT_SORT);

        call.enqueue(new Callback<FilmPage>() {
            @Override
            public void onResponse(@NonNull Call<FilmPage> call, @NonNull Response<FilmPage> response) {
                isLoading = false;
                if (currentPage == 0) showLoading(false);

                Log.d(TAG, "API Yanıt Kodu (getAllFilms): " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    FilmPage filmPage = response.body();
                    if (filmPage.getContent() != null && !filmPage.getContent().isEmpty()) {
                        int oldSize = filmList.size();
                        filmList.addAll(filmPage.getContent());
                        filmAdapter.notifyItemRangeInserted(oldSize, filmPage.getContent().size());
                        Log.d(TAG, "Alınan Film Sayısı: " + filmPage.getContent().size() + ", Toplam: " + filmList.size());
                        showEmptyView(null);

                        if (currentPage == 0 && !filmList.isEmpty() && filmList.get(0).getPosterUrl() != null) {
                            Picasso.get()
                                    .load(filmList.get(0).getPosterUrl())
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder_error) // Bu drawable'ı eklediğinden emin ol
                                    .into(posterImageView);
                        }
                    } else if (currentPage == 0) {
                        Log.d(TAG, "Film listesi boş geldi (ilk sayfa).");
                        showEmptyView("Gösterilecek film bulunamadı.");
                    }
                    isLastPage = filmPage.isLast();
                    if (isLastPage) Log.d(TAG, "Son sayfaya ulaşıldı.");
                } else {
                    handleApiError(response, "Film verisi alınamadı!");
                    if (currentPage == 0) showEmptyView("Film verisi alınamadı.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<FilmPage> call, @NonNull Throwable t) {
                isLoading = false;
                if (currentPage == 0) showLoading(false);
                Log.e(TAG, "API Çağrı Hatası (getAllFilms): " + t.getMessage(), t);
                Toast.makeText(HomeActivity.this, "Bağlantı hatası: Filmler alınamadı!", Toast.LENGTH_LONG).show();
                if (currentPage == 0) showEmptyView("Filmler yüklenirken bir hata oluştu.");
            }
        });
    }

    private void handleApiError(Response<?> response, String defaultMessage) {
        String errorMsg = defaultMessage;
        if (response.errorBody() != null) {
            try {
                errorMsg += " Hata: " + response.code() + " " + response.errorBody().string();
            } catch (IOException e) {
                Log.e(TAG, "Error body parse error", e);
            }
        } else if (response.message() != null && !response.message().isEmpty()) {
            errorMsg += " Hata: " + response.code() + " - " + response.message();
        } else {
            errorMsg += " Hata: " + response.code();
        }
        Toast.makeText(HomeActivity.this, errorMsg, Toast.LENGTH_LONG).show();
        Log.e(TAG, "API Hatası: " + errorMsg);
    }

    @Override
    public void onFilmClick(Film film) { // FilmAdapter.OnFilmClickListener implementasyonu
        Toast.makeText(this, "Film Tıklandı: " + film.getTitle(), Toast.LENGTH_SHORT).show();
        // TODO: Film detay ekranına yönlendir (FilmDetailActivity)
        // Intent intent = new Intent(this, FilmDetailActivity.class);
        // intent.putExtra("FILM_ID_EXTRA", film.getId());
        // startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        // drawer_menu.xml dosyasındaki ID'lere göre case'leri düzenle
        if (id == R.id.menu_home) { // Örnek ID, kendi XML'indeki ID ile değiştir
            // Zaten bu ekrandayız
            Toast.makeText(this, "Zaten Ana Sayfadasınız", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_profile) { // Örnek ID
            Toast.makeText(this, "Profil Tıklandı", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, ProfileActivity.class);
            // startActivity(intent);
        } else if (id == R.id.menu_films) { // Örnek ID
            Toast.makeText(this, "Filmler Tıklandı", Toast.LENGTH_SHORT).show();
            // Bu ekran zaten bir tür film listeleme, belki farklı bir film arama/filtreleme ekranına gidilebilir
        } else if (id == R.id.menu_list) { // Örnek ID
            Intent intent = new Intent(this, ListemActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_forum) { // Örnek ID
            Toast.makeText(this, "Forum Tıklandı", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_logout) { // Örnek ID
            performLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
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

    private void showLoading(boolean show) {
        if (progressBarHome != null) {
            progressBarHome.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (filmsRecyclerView != null) {
            if (show && currentPage == 0) {
                filmsRecyclerView.setVisibility(View.GONE);
                if (tvEmptyViewHome != null) tvEmptyViewHome.setVisibility(View.GONE);
            } else if (!show && !filmList.isEmpty()) {
                filmsRecyclerView.setVisibility(View.VISIBLE);
                if (tvEmptyViewHome != null) tvEmptyViewHome.setVisibility(View.GONE);
            }
        }
    }

    private void showEmptyView(String message) {
        if (tvEmptyViewHome != null) {
            if (message != null && filmList.isEmpty()) {
                tvEmptyViewHome.setText(message);
                tvEmptyViewHome.setVisibility(View.VISIBLE);
                if (filmsRecyclerView != null) filmsRecyclerView.setVisibility(View.GONE);
            } else if (!filmList.isEmpty()) {
                tvEmptyViewHome.setVisibility(View.GONE);
                if (filmsRecyclerView != null) filmsRecyclerView.setVisibility(View.VISIBLE);
            } else if (message == null && filmList.isEmpty()) { // Mesaj yok ama liste boşsa (ilk yükleme sonrası)
                tvEmptyViewHome.setVisibility(View.GONE); // Boş görünümü gizle, progress bar görünebilir
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}