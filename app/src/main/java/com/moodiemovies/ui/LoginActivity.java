package com.moodiemovies.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    CheckBox rememberMeCheckBox;
    TextView resetPasswordTextView;
    // Eklenenler:
    TextView advantageTitle, loginTitle;
    LinearLayout advantageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        resetPasswordTextView = findViewById(R.id.resetPasswordTextView);

        // Eklenenler:
        advantageTitle = findViewById(R.id.advantageTitle);
        loginTitle = findViewById(R.id.loginTitle);
        advantageList = findViewById(R.id.advantageList);

        // Inputlara focus olduğunda başlıklar ve avantajlar GONE olsun
        View.OnFocusChangeListener focusListener = (v, hasFocus) -> {
            if (hasFocus) {
                advantageTitle.setVisibility(View.GONE);
                loginTitle.setVisibility(View.GONE);
                advantageList.setVisibility(View.GONE);
            } else {
                // Eğer başka input hala focus'ta ise saklı kalsın:
                if (!emailEditText.hasFocus() && !passwordEditText.hasFocus()) {
                    advantageTitle.setVisibility(View.VISIBLE);
                    loginTitle.setVisibility(View.VISIBLE);
                    advantageList.setVisibility(View.VISIBLE);
                }
            }
        };
        emailEditText.setOnFocusChangeListener(focusListener);
        passwordEditText.setOnFocusChangeListener(focusListener);

        // Parola sıfırlama
        resetPasswordTextView.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Lütfen önce e-posta adresinizi girin.", Toast.LENGTH_SHORT).show();
            } else if (email.contains("@")) {
                Toast.makeText(this, "Eğer bu e-posta adresi kayıtlıysa, parola sıfırlama bağlantısı gönderilecektir.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Geçerli bir e-posta adresi girin.", Toast.LENGTH_SHORT).show();
            }
        });

        // Giriş yap butonu
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            boolean rememberMe = rememberMeCheckBox.isChecked();

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
                            SharedPreferences prefs = getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("authToken", token); // "authToken" key'i ile kaydet
                            editor.apply();
                        Toast.makeText(LoginActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        // intent.putExtra("token", token); // Token gerekiyorsa ekle
                        startActivity(intent);
                        finish();
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
