package com.moodiemovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.moodiemovies.R;
import com.moodiemovies.network.ApiService;
import com.moodiemovies.network.RegisterRequest;
import com.moodiemovies.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText, usernameEditText;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String name = usernameEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            RegisterRequest request = new RegisterRequest(email, password, name);

            apiService.registerUser(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                        // Kaydı bitirince login ekranına yönlendir
                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        // Veya doğrudan HomeActivity'ye atmak
                        // Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                        // startActivity(intent);
                        // finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Kayıt başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Hata: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
