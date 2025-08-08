package com.example.nasa_image_project;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ImageDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        ImageView detailImage = findViewById(R.id.detailImage);
        TextView detailDate = findViewById(R.id.detailDate);
        TextView detailHdUrl = findViewById(R.id.detailHdUrl);

        String date = getIntent().getStringExtra("date");
        String url = getIntent().getStringExtra("url");
        String hdurl = getIntent().getStringExtra("hdurl");

        detailDate.setText("Date: " + date);
        detailHdUrl.setText("HD URL: " + hdurl);

        Picasso.get().load(url).into(detailImage);
    }
}
