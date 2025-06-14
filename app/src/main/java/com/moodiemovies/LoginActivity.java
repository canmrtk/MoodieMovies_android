package com.moodiemovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.model.UserDTO;

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
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private static final String LOGIN_URL = "http://10.0.2.2:8080/api/v1/auth/login";
    private static final String TAG = "LoginActivity_Debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }

        // JSON payload oluşturma
        JSONObject jsonPayload = new JSONObject();
        try {
            jsonPayload.put("email", email);
            jsonPayload.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, "JSON oluşturma hatası", e);
            return;
        }

        RequestBody body = RequestBody.create(jsonPayload.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();

        Log.d(TAG, "İstek gönderiliyor: " + LOGIN_URL);
        Log.d(TAG, "Payload: " + jsonPayload.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Ağ hatası
                Log.e(TAG, "Ağ hatası: ", e);
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Bağlantı hatası: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Başarılı yanıt
                    try (ResponseBody responseBody = response.body()) {
                        if (responseBody == null) {
                            Log.e(TAG, "Başarılı yanıt (200) ama body boş.");
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Sunucudan boş yanıt alındı.", Toast.LENGTH_SHORT).show());
                            return;
                        }

                        String responseString = responseBody.string();
                        Log.d(TAG, "Başarılı yanıt alındı. Body: " + responseString);

                        // GSON ile AuthResponse nesnesine çevirme
                        AuthResponse authResponse = gson.fromJson(responseString, AuthResponse.class);

                        if (authResponse != null && authResponse.getAccessToken() != null) {
                            Log.d(TAG, "Token başarıyla alındı: " + authResponse.getAccessToken());
                            saveAuthInfo(authResponse);
                            runOnUiThread(() -> {
                                Toast.makeText(LoginActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
                                navigateToHome();
                            });
                        } else {
                            Log.e(TAG, "JSON parse edildi ama token veya user nesnesi null.");
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Yanıt işlenemedi.", Toast.LENGTH_SHORT).show());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Yanıt işlenirken hata oluştu: ", e);
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Yanıt parse hatası.", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    // Başarısız HTTP yanıtı (4xx, 5xx)
                    String errorBodyString = response.body() != null ? response.body().string() : "Boş hata mesajı";
                    Log.e(TAG, "Giriş başarısız. Kod: " + response.code() + ", Hata Mesajı: " + errorBodyString);
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Giriş başarısız: " + response.code(), Toast.LENGTH_LONG).show());
                }
            }
        });
    }

    private void saveAuthInfo(AuthResponse authResponse) {
        // ... (Bu metod önceki gibi kalabilir)
        SharedPreferences sharedPreferences = getSharedPreferences("MoodieMoviesPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_token", authResponse.getAccessToken());
        UserDTO user = authResponse.getUser();
        if (user != null) {
            editor.putString("user_id", user.getId());
            editor.putString("user_name", user.getName());
            editor.putString("user_email", user.getEmail());
        }
        editor.apply();
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}