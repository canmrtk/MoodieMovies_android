package com.moodiemovies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull; // Hata 3 için import eklendi
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.moodiemovies.model.User;
import com.moodiemovies.network.ApiService;
import com.moodiemovies.network.LoginResponse; // Güncellenmiş LoginResponse'u import et
import com.moodiemovies.network.RetrofitClient;
import com.moodiemovies.ui.HomeActivity;
import com.moodiemovies.ui.LoginActivity;
import com.moodiemovies.ui.SignUpActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // --- Google Sign-In Kurulumu ---
        // Hata 1 Düzeltmesi: R.string.your_server_client_id -> R.string.server_client_id
        String serverClientId = getString(R.string.server_client_id);

        // Hata 2 Düzeltmesi: DEFAULT_SIN_IN -> DEFAULT_SIGN_IN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(serverClientId)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleSignInResult(task);
                    } else {
                        Log.w(TAG, "Google Sign-In iptal edildi veya başarısız oldu. Sonuç Kodu: " + result.getResultCode());
                        Toast.makeText(WelcomeActivity.this, "Google ile giriş iptal edildi.", Toast.LENGTH_SHORT).show();
                    }
                });

        Button signUpButton = findViewById(R.id.signUpButton);
        Button googleSignInButton = findViewById(R.id.googleSignInButton);
        Button facebookSignInButton = findViewById(R.id.facebookSignInButton);
        TextView signInTextView = findViewById(R.id.signInTextView);

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        googleSignInButton.setOnClickListener(v -> signInWithGoogle());

        facebookSignInButton.setOnClickListener(v -> {
            Toast.makeText(WelcomeActivity.this, "Facebook ile giriş yakında!", Toast.LENGTH_SHORT).show();
        });

        signInTextView.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            Log.d(TAG, "Google Sign-In başarılı! ID Token alındı.");
            sendGoogleTokenToBackend(idToken);
        } catch (ApiException e) {
            Log.w(TAG, "Google ile giriş başarısız oldu. Hata Kodu: " + e.getStatusCode());
            Toast.makeText(this, "Google ile giriş başarısız oldu. Hata: " + e.getStatusCode(), Toast.LENGTH_LONG).show();
        }
    }

    private void sendGoogleTokenToBackend(String idToken) {
        if (idToken == null) {
            Toast.makeText(this, "Google'dan geçerli bir token alınamadı.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.signInWithGoogle(idToken);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) { // Hata 3 Düzeltmesi: @NonNull import edildi
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Backend Google token'ı doğruladı ve giriş başarılı.");
                    Toast.makeText(WelcomeActivity.this, "Google ile giriş başarılı!", Toast.LENGTH_SHORT).show();
                    saveAuthData(response.body());
                    navigateToHome();
                } else {
                    handleApiError(response, "Sunucu doğrulaması başarısız.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) { // Hata 3 Düzeltmesi: @NonNull import edildi
                Log.e(TAG, "Backend'e Google token gönderilirken bağlantı hatası: " + t.getMessage(), t);
                Toast.makeText(WelcomeActivity.this, "Bağlantı hatası: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAuthData(LoginResponse loginResponse) {
        // Hata 4, 5, 7, 8, 9, 10 Düzeltmesi: Artık LoginResponse bu metodları içeriyor.
        if (loginResponse == null || loginResponse.getAccessToken() == null || loginResponse.getUser() == null) {
            Log.e(TAG, "Kaydedilecek geçerli token veya kullanıcı bilgisi bulunamadı.");
            return;
        }

        SharedPreferences prefs = getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("authToken", loginResponse.getAccessToken());

        User user = loginResponse.getUser();
        if (user != null) {
            if (user.getId() != null) editor.putString("userId", user.getId());
            if (user.getName() != null) editor.putString("userName", user.getName());
            if (user.getEmail() != null) editor.putString("userEmail", user.getEmail());
        }

        editor.apply();
        Log.d(TAG, "Oturum verileri (token ve kullanıcı) kaydedildi.");
    }

    private void navigateToHome() {
        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void handleApiError(Response<?> response, String defaultMessage) {
        String errorMsg = defaultMessage;
        if (response.errorBody() != null) {
            try {
                errorMsg += " Hata: " + response.code() + " " + response.errorBody().string();
            } catch (IOException e) {
                Log.e(TAG, "Error body parse error", e);
            }
        } else {
            errorMsg += " Hata: " + response.code();
        }
        Toast.makeText(WelcomeActivity.this, errorMsg, Toast.LENGTH_LONG).show();
        Log.e(TAG, "API Hatası: " + errorMsg);
    }
}