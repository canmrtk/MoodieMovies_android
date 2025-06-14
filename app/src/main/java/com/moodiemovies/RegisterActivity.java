package com.moodiemovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.model.UserDTO;
import com.moodiemovies.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, nameEditText;
    private Button registerButton;
    private ProgressBar progressBar;
    private RegisterViewModel registerViewModel;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        registerButton = findViewById(R.id.registerButton);
        // activity_register.xml'e bir ProgressBar eklediğinizi varsayıyorum.
        // progressBar = findViewById(R.id.progressBarRegister);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        registerViewModel.getRegisterResult().observe(this, authResponse -> {
            // progressBar.setVisibility(View.GONE);
            registerButton.setEnabled(true);

            if (authResponse != null && authResponse.getAccessToken() != null && authResponse.getUser() != null) {
                Toast.makeText(RegisterActivity.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                saveAuthInfo(authResponse);
                navigateToHome();
            } else {
                String errorMessage = authResponse != null && authResponse.getMessage() != null ? authResponse.getMessage() : "Kayıt başarısız oldu.";
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            registerButton.setEnabled(false);
            // progressBar.setVisibility(View.VISIBLE);
            registerUser();
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            registerButton.setEnabled(true);
            // progressBar.setVisibility(View.GONE);
            return;
        }

        registerViewModel.register(name, email, password);
    }

    private void saveAuthInfo(AuthResponse authResponse) {
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
        Log.d(TAG, "Token ve kullanıcı bilgileri kaydedildi. Token: " + authResponse.getAccessToken());
    }

    private void navigateToHome() {
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}