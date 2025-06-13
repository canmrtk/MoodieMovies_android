package com.moodiemovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import java.io.IOException;

import okhttp3.*;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private final String LOGIN_URL = "http://10.0.2.2:8080/api/v1/auth/login"; // Spring Boot login endpoint

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

        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), json.toString());

            Request request = new Request.Builder()
                    .url(LOGIN_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Bağlantı hatası", Toast.LENGTH_SHORT).show());
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Giriş başarısız: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "İstek hatası", Toast.LENGTH_SHORT).show();
        }
    }
}
