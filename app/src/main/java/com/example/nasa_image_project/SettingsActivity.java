package com.example.nasa_image_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREF_BACKGROUND = "pref_background";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.settings);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button btnRed = findViewById(R.id.btnRed);
        Button btnGreen = findViewById(R.id.btnGreen);
        Button btnBlue = findViewById(R.id.btnBlue);
        Button btnWhite = findViewById(R.id.btnWhite);
        Button btnHelp = findViewById(R.id.btnHelp);

        btnRed.setOnClickListener(v -> setBackgroundColor(Color.RED));
        btnGreen.setOnClickListener(v -> setBackgroundColor(Color.GREEN));
        btnBlue.setOnClickListener(v -> setBackgroundColor(Color.BLUE));
        btnWhite.setOnClickListener(v -> setBackgroundColor(Color.WHITE));
        btnHelp.setOnClickListener(v -> showHelpDialog());

        // Apply background color
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int bgColor = prefs.getInt(PREF_BACKGROUND, Color.WHITE);
        findViewById(R.id.settingsLayout).setBackgroundColor(bgColor);
    }

    private void setBackgroundColor(int color) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putInt(PREF_BACKGROUND, color).apply();

        findViewById(R.id.settingsLayout).setBackgroundColor(color);
        Toast.makeText(this, R.string.toast_background_saved, Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.settingsLayout), R.string.snackbar_color_applied, Snackbar.LENGTH_SHORT).show();
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.help_title)
                .setMessage(R.string.help_message_settings)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
