// com.moodiemovies.ui.SignUpActivity.java
package com.moodiemovies.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences; // SharedPreferences için import
import android.os.Bundle;
import android.text.Html;
import android.util.Log; // Loglama için
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moodiemovies.R;
import com.moodiemovies.model.User; // User modelini import et
import com.moodiemovies.network.ApiService;
import com.moodiemovies.network.RegisterRequest;
import com.moodiemovies.network.RegisterResponse; // RegisterResponse'u import et
import com.moodiemovies.network.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity"; // Loglama için TAG

    EditText emailEditText, passwordEditText, nameEditText;
    Button registerButton;
    TextView legalPopupTrigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        registerButton = findViewById(R.id.registerButton);
        legalPopupTrigger = findViewById(R.id.legalPopupTrigger);

        legalPopupTrigger.setOnClickListener(v -> showLegalPopup());

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String name = nameEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Şifre ve e-posta için frontend validasyonu eklenebilir (uzunluk, format vb.)

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            RegisterRequest request = new RegisterRequest(email, password, name);

            // Callback tipini RegisterResponse olarak değiştir
            apiService.registerUser(request).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        RegisterResponse registerResponse = response.body();
                        Toast.makeText(SignUpActivity.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Kayıt başarılı. Yanıt: " + response.body().toString());


                        // Başarılı kayıt sonrası token ve kullanıcı bilgilerini kaydet
                        // ve HomeActivity'ye yönlendir (Login'deki gibi)
                        if (registerResponse.getAccessToken() != null && registerResponse.getUser() != null) {
                            saveAuthToken(registerResponse.getAccessToken());
                            saveUserToPreferences(registerResponse.getUser()); // Opsiyonel: Kullanıcı adını vs. saklamak için

                            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // Eğer backend token ve user dönmüyorsa, sadece Login'e yönlendir.
                            // Bu durum backend API tasarımına bağlı.
                            Log.w(TAG, "Kayıt yanıtında token veya kullanıcı bilgisi eksik. Login ekranına yönlendiriliyor.");
                            Toast.makeText(SignUpActivity.this, "Kayıt başarılı, lütfen giriş yapın.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        String errorMessage = "Kayıt başarısız!";
                        if (response.errorBody() != null) {
                            try {
                                // Backend'den gelen spesifik hata mesajını göstermeye çalış
                                // Örneğin: {"message": "Bu e-posta zaten kullanımda"}
                                String errorBodyStr = response.errorBody().string();
                                // Eğer hata JSON formatındaysa parse edilebilir, şimdilik direkt gösterelim
                                errorMessage += "\n" + errorBodyStr.substring(0, Math.min(errorBodyStr.length(), 100)); // Çok uzun olmasın
                                Log.e(TAG, "Kayıt hatası body: " + errorBodyStr);
                            } catch (IOException e) {
                                Log.e(TAG, "Error body parse hatası", e);
                            }
                        } else {
                            errorMessage += " Hata kodu: " + response.code();
                        }
                        Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Kayıt başarısız. Kod: " + response.code() + ", Mesaj: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Log.e(TAG, "Kayıt API çağrı hatası: " + t.getMessage(), t);
                    Toast.makeText(SignUpActivity.this, "Bağlantı hatası: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void saveAuthToken(String token) {
        SharedPreferences prefs = getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("authToken", token);
        editor.apply();
        Log.d(TAG, "Auth token kaydedildi.");
    }

    private void saveUserToPreferences(User user) {
        if (user == null) return;
        SharedPreferences prefs = getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (user.getId() != null) editor.putString("userId", user.getId());
        if (user.getName() != null) editor.putString("userName", user.getName());
        if (user.getEmail() != null) editor.putString("userEmail", user.getEmail());
        editor.apply();
        Log.d(TAG, "Kullanıcı bilgileri SharedPreferences'a kaydedildi.");
    }

    private void showLegalPopup() {
        String popupText =
                "<h4>Kullanım Koşulları</h4>" +
                        "<p>MoodieMovies platformunu kullanarak aşağıdaki şartları kabul etmiş sayılırsınız.<br>" +
                        "Bu platform yalnızca bireysel, kişisel ve eğlence amaçlı kullanım içindir.<br>" +
                        "Hesap bilgilerinizin güvenliğinden siz sorumlusunuz.<br>" +
                        "MoodieMovies, hizmetlerinde değişiklik yapma hakkını saklı tutar.</p>" +
                        "<h4>Gizlilik Politikası</h4>" +
                        "<p>E-posta adresiniz ve kişisel bilgileriniz gizli tutulur, üçüncü taraflarla paylaşılmaz.<br>" +
                        "Giriş yaptığınızda çerezler aracılığıyla oturum bilginiz geçici olarak saklanabilir.<br>" +
                        "Kişisel veriler yalnızca deneyimi iyileştirmek amacıyla kullanılır.</p>";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kullanım Koşulları ve Gizlilik");

        final TextView message = new TextView(this);
        message.setText(Html.fromHtml(popupText, Html.FROM_HTML_MODE_LEGACY)); // FROM_HTML_MODE_LEGACY eklendi
        message.setPadding(40, 30, 40, 30);
        message.setTextSize(15);

        builder.setView(message);
        builder.setPositiveButton("Kapat", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}