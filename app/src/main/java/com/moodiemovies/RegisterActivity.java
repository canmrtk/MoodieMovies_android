package com.moodiemovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, nameEditText;
    private Button registerButton;
    private final String REGISTER_URL = "http://10.0.2.2:8080/api/v1/auth/register"; // backend adresi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);
            json.put("name", name);

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"), json.toString());

            Request request = new Request.Builder()
                    .url(REGISTER_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Sunucu hatası", Toast.LENGTH_SHORT).show());
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Kayıt başarısız: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "İstek hazırlanırken hata", Toast.LENGTH_SHORT).show();
        }
    }
}
