package com.moodiemovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.moodiemovies.model.AuthResponse;
import com.moodiemovies.model.RetrofitClient;
import com.moodiemovies.network.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button googleButton = findViewById(R.id.googleSignInButton);
        Button emailSignUpButton = findViewById(R.id.emailSignUpButton);
        TextView loginText = findViewById(R.id.loginText);

        // 🔹 Google yapılandırması
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // 🔹 Google ile Giriş
        googleButton.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        // ✅ KABUL ET VE KATIL (register sayfasına yönlendir)
        emailSignUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // ✅ OTURUM AÇ (login sayfasına yönlendir)
        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acct = task.getResult(ApiException.class);
                String idToken = acct.getIdToken();  // ← BURADA alıyoruz
                sendGoogleTokenToBackend(idToken);
            } catch (ApiException e) {

                Log.e("GoogleSignIn", "Kod: " + e.getStatusCode(), e);
                Log.e("GoogleSignIn", "fail code=" + e.getStatusCode());
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void sendGoogleTokenToBackend(String idToken) {
        Map<String,String> payload = new HashMap<>();
        payload.put("idToken", idToken);

        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.loginWithGoogle(payload).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    String jwt = resp.body().getAccessToken();
                    // 1) SharedPreferences’a kaydet
                    getSharedPreferences("MoodieMoviesPrefs", MODE_PRIVATE)
                            .edit()
                            .putString("user_token", jwt)
                            .apply();
                    // 2) İstersen user bilgilerini de kaydedebilirsin
                    // 3) Ana ekrana geç
                    startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(WelcomeActivity.this, "Giriş başarısız", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(WelcomeActivity.this, "Sunucu hatası", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
