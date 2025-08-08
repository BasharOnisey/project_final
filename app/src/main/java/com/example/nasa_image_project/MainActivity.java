package com.example.nasa_image_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ImageView imageView;
    private TextView imageTitle, imageDate;
    private EditText feedbackInput;
    private ProgressBar progressBar;
    private String currentTitle, currentUrl, currentDate;
    private DatabaseHelper dbHelper;

    private final String NASA_API_KEY = "phbya82HzQz0XICeyxH8hkDq6egfiYcS3nS6cg0X"; // Replace with your real key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_saved_images) {
                startActivity(new Intent(this, SavedImagesActivity.class));
            } else if (id == R.id.menu_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            } else if (id == R.id.menu_about) {
                startActivity(new Intent(this, AboutActivity.class));
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // UI References
        imageView = findViewById(R.id.imageView);
        imageTitle = findViewById(R.id.imageTitle);
        imageDate = findViewById(R.id.imageDate);
        feedbackInput = findViewById(R.id.feedbackInput);
        progressBar = findViewById(R.id.progressBar);

        Button datePickerButton = findViewById(R.id.datePickerButton);
        datePickerButton.setOnClickListener(view -> showDatePicker());

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> saveImage());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    String date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    fetchNasaImage(date);
                }, year, month, day);
        datePicker.show();
    }

    private void fetchNasaImage(String date) {
        String apiUrl = "https://api.nasa.gov/planetary/apod?api_key=" + NASA_API_KEY + "&date=" + date;
        new FetchImageTask().execute(apiUrl);
    }

    private class FetchImageTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    result.append(inputLine);
                }

                in.close();
                return result.toString();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            progressBar.setVisibility(View.GONE);

            if (response != null) {
                try {
                    JSONObject json = new JSONObject(response);
                    currentTitle = json.getString("title");
                    currentUrl = json.getString("url");
                    currentDate = json.getString("date");

                    imageTitle.setText(currentTitle);
                    imageDate.setText(currentDate);

                    Picasso.get().load(currentUrl).into(imageView);
                    Toast.makeText(MainActivity.this, getString(R.string.toast_image_loaded), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, getString(R.string.toast_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.toast_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImage() {
        String feedback = feedbackInput.getText().toString();
        if (currentTitle != null && currentUrl != null && currentDate != null) {
            SavedImageModel image = new SavedImageModel(0, currentTitle, currentUrl, currentDate, feedback);
            dbHelper.insertImage(image);
            Toast.makeText(this, getString(R.string.toast_image_saved), Toast.LENGTH_SHORT).show();
            Snackbar.make(imageView, R.string.toast_image_saved, Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.toast_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_help) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.help_title)
                    .setMessage(R.string.help_message_main)
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
