package com.moodiemovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.moodiemovies.ui.LoginActivity;
import com.moodiemovies.ui.SignUpActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        Button signUpButton = findViewById(R.id.signUpButton);
        Button googleSignInButton = findViewById(R.id.googleSignInButton);
        Button facebookSignInButton = findViewById(R.id.facebookSignInButton);
        TextView signInTextView = findViewById(R.id.signInTextView);

        // Kayıt ekranına geçiş
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Google ile giriş tıklanınca (şimdilik Toast)
        googleSignInButton.setOnClickListener(v -> {
            Toast.makeText(WelcomeActivity.this, "Google ile giriş yakında!", Toast.LENGTH_SHORT).show();
        });

        // Facebook ile giriş tıklanınca (şimdilik Toast)
        facebookSignInButton.setOnClickListener(v -> {
            Toast.makeText(WelcomeActivity.this, "Facebook ile giriş yakında!", Toast.LENGTH_SHORT).show();
        });

        // Giriş ekranına geçiş
        signInTextView.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });


    }
}
