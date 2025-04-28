package com.example.divasegura.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.divasegura.R;
import com.example.divasegura.activities.WelcomeActivity;

public class UserInfoFragment extends Fragment {
    private EditText etTelefono, etNombre, etDomicilio;
    private Button btnNext, btnTakePhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        etTelefono = view.findViewById(R.id.etTelefonoUsuario);
        etNombre = view.findViewById(R.id.etNombreUsuario);
        etDomicilio = view.findViewById(R.id.etDomicilioUsuario);
        btnNext = view.findViewById(R.id.btnNext1);
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto);

        btnNext.setOnClickListener(v -> {
            // Navigate to next page
            ((WelcomeActivity)getActivity()).navigateToPage(1);
        });

        btnTakePhoto.setOnClickListener(v -> {
            // Handle photo capture logic
        });

        return view;
    }
}