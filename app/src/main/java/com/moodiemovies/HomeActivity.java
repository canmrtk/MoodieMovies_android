package com.moodiemovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class HomeActivity extends AppCompatActivity {

    private Button btnRecommend;
    private ImageView menuIcon;
    private RecyclerView recyclerLastWatched;
    private FilmAdapter filmAdapter;
    private final List<Film> filmList = new ArrayList<>();
    private final String FILMS_API = "http://10.0.2.2:8080/api/v1/films/last-watched";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnRecommend = findViewById(R.id.btnRecommend);
        menuIcon = findViewById(R.id.menuIcon);
        recyclerLastWatched = findViewById(R.id.recyclerLastWatched);

        recyclerLastWatched.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        filmAdapter = new FilmAdapter(filmList, film -> {
            Intent intent = new Intent(HomeActivity.this, FilmDetailActivity.class);
            intent.putExtra("film_id", film.getId());
            startActivity(intent);
        });
        recyclerLastWatched.setAdapter(filmAdapter);

        btnRecommend.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PersonalityTestActivity.class);
            startActivity(intent);
        });

        menuIcon.setOnClickListener(this::showPopupMenu);

        fetchFilmsFromApi();
    }

    private void fetchFilmsFromApi() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(FILMS_API).build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(HomeActivity.this, "Film listesi alınamadı", Toast.LENGTH_SHORT).show());
            }

            @Override public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        filmList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            filmList.add(new Film(
                                    obj.getString("id"),
                                    obj.getString("title"),
                                    obj.getString("posterUrl")
                            ));
                        }
                        runOnUiThread(() -> filmAdapter.notifyDataSetChanged());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_navbar, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> onMenuItemClick(item));
        popupMenu.show();
    }

    private boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.menu_films) {
            startActivity(new Intent(this, AllFilmsActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            Toast.makeText(this, "Çıkış yapıldı", Toast.LENGTH_SHORT).show();
            // TODO: token temizleme vb. logout işlemleri
            return true;
        }
        return false;
    }
}
