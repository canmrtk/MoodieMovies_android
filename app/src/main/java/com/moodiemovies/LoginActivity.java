package com.moodiemovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private ImageView backButton;
    private TextView registerLink;
    private TextView forgotPasswordLink;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.backButton);
        registerLink = findViewById(R.id.registerLink);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);

        loginButton.setOnClickListener(v -> handleLogin());
        backButton.setOnClickListener(v -> onBackPressed());
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        forgotPasswordLink.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Şifremi Unuttum özelliği yakında eklenecek.", Toast.LENGTH_SHORT).show();
        });
    }

    private void handleLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Kullanıcı adı ve şifre boş olamaz.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        loginViewModel.login(username, password).observe(this, new Observer<AuthResponse>() {
            @Override
            public void onChanged(AuthResponse authResponse) {
                progressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);

                if (authResponse != null && authResponse.getToken() != null && authResponse.getUser() != null) {
                    // Giriş başarılı
                    Toast.makeText(LoginActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();

                    // Kullanıcı bilgilerini ve token'ı SharedPreferences'e kaydet
                    SharedPreferences sharedPreferences = getSharedPreferences("MoodieMoviesPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_token", authResponse.getToken());
                    // User ID'yi String olarak kaydediyoruz, çünkü backend Integer beklese de SharedPreferences'te String tutmak daha esnektir.
                    editor.putString("user_id", String.valueOf(authResponse.getUser().getUserId()));
                    editor.apply();

                    // Ana ekrana yönlendir
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    // Geri tuşuyla giriş ekranlarına dönülmesini engellemek için tüm geçmişi temizle
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    // Giriş başarısız veya hata var
                    String errorMessage = (authResponse != null && authResponse.getMessage() != null)
                            ? authResponse.getMessage()
                            : "Kullanıcı adı veya şifre hatalı.";
                    Toast.makeText(LoginActivity.this, "Hata: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}