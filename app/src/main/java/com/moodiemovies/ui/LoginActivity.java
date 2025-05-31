package com.moodiemovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.moodiemovies.R;
import com.moodiemovies.network.ApiService;
import com.moodiemovies.network.LoginRequest;
import com.moodiemovies.network.LoginResponse;
import com.moodiemovies.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "E-posta ve şifre girin!", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            LoginRequest request = new LoginRequest(email, password);

            apiService.loginUser(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body().token;
                        Toast.makeText(LoginActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
                        // BAŞARILI GİRİŞTE ANA SAYFAYA YÖNLENDİR
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        // intent.putExtra("token", token);
                        startActivity(intent);
                        finish(); // LoginActivity'yi kapat
                    } else {
                        Toast.makeText(LoginActivity.this, "Giriş başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Hata: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
