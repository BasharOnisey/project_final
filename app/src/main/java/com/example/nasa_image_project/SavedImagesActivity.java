package com.example.nasa_image_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class SavedImagesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SavedImageAdapter adapter;
    private RelativeLayout savedImagesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.saved_images);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Load and display saved images
        dbHelper = new DatabaseHelper(this);
        List<SavedImageModel> savedImages = dbHelper.getAllImages();

        ListView listView = findViewById(R.id.listView);
        adapter = new SavedImageAdapter(this, savedImages, model -> {
            dbHelper.deleteImage(model.getId());
            adapter.remove(model);
            Toast.makeText(this, R.string.toast_deleted, Toast.LENGTH_SHORT).show();
        });

        listView.setAdapter(adapter);
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
