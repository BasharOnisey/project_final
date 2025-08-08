package com.example.nasa_image_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ImageDetailsFragment extends Fragment {

    private TextView titleView, dateView, urlView;

    public static ImageDetailsFragment newInstance(String title, String date, String url) {
        ImageDetailsFragment fragment = new ImageDetailsFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("date", date);
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        titleView = view.findViewById(R.id.fragmentTitle);
        dateView = view.findViewById(R.id.fragmentDate);
        urlView = view.findViewById(R.id.fragmentUrl);

        Bundle args = getArguments();
        if (args != null) {
            titleView.setText(args.getString("title", ""));
            dateView.setText(args.getString("date", ""));
            urlView.setText(args.getString("url", ""));
        }
    }
}
