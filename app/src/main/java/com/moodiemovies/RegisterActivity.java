package com.moodiemovies;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.moodiemovies.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText;
    private Button registerButton;
    private ProgressBar progressBar;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);

        registerButton.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Tüm alanlar doldurulmalıdır.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Geçerli bir e-posta adresi girin.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);

        registerViewModel.register(username, email, password).observe(this, authResponse -> {
            progressBar.setVisibility(View.GONE);
            registerButton.setEnabled(true);

            if (authResponse != null && authResponse.getToken() != null) {
                Toast.makeText(RegisterActivity.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                finish(); // Login ekranına dön
            } else {
                String errorMessage = (authResponse != null && authResponse.getMessage() != null)
                        ? authResponse.getMessage()
                        : "Bilinmeyen bir hata oluştu.";
                Toast.makeText(RegisterActivity.this, "Hata: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}