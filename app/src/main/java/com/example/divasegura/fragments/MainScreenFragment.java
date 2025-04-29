package com.example.divasegura.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.divasegura.R;

public class MainScreenFragment extends Fragment {

    private AppCompatButton btnAlert;
    private ImageButton btnCall;
    private ImageButton btnLocation;

    public MainScreenFragment() {
        // Required empty public constructor
    }

    public static MainScreenFragment newInstance() {
        return new MainScreenFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize buttons
        btnAlert = view.findViewById(R.id.btnAlert);
        btnCall = view.findViewById(R.id.btnCall);
        btnLocation = view.findViewById(R.id.btnLocation);

        // Set click listeners
        btnAlert.setOnClickListener(v -> {
            // Handle alert button press
        });

        btnCall.setOnClickListener(v -> {
            // Handle call button press
        });

        btnLocation.setOnClickListener(v -> {
            // Handle location button press
        });
    }
}