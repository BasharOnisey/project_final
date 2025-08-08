package com.example.nasa_image_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SavedImageAdapter extends BaseAdapter {

    private final Context context;
    private final List<SavedImageModel> imageList;
    private final OnItemDeleteListener deleteListener;

    public SavedImageAdapter(Context context, List<SavedImageModel> imageList, OnItemDeleteListener deleteListener) {
        this.context = context;
        this.imageList = imageList;
        this.deleteListener = deleteListener;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return imageList.get(position).getId(); // Assumes ID is a unique long or int
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.item_saved_image, parent, false);
        }

        TextView titleText = rowView.findViewById(R.id.titleText);
        TextView dateText = rowView.findViewById(R.id.dateText);
        TextView feedbackText = rowView.findViewById(R.id.feedbackText);
        android.widget.ImageView imageView = rowView.findViewById(R.id.savedImageView);
        ImageButton deleteButton = rowView.findViewById(R.id.deleteButton);

        SavedImageModel image = imageList.get(position);

        titleText.setText(image.getTitle());
        dateText.setText(image.getDate());
        feedbackText.setText(image.getFeedback());

        Picasso.get().load(image.getUrl()).into(imageView);

        deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(image);
            }
        });

        return rowView;
    }

    public void remove(SavedImageModel model) {
        imageList.remove(model);
        notifyDataSetChanged();
    }

    public interface OnItemDeleteListener {
        void onDelete(SavedImageModel model);
    }
}
