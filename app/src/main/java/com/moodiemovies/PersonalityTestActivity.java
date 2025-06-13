package com.moodiemovies;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PersonalityTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Direkt varolan TestActivity'i başlat
        startActivity(new Intent(this, TestActivity.class));
        finish();
    }
}
