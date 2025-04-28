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

public class EmergencyContactFragment extends Fragment {
    private EditText etNombre, etTelefono;
    private Button btnNext;
    private int contactNumber;

    public EmergencyContactFragment(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_emergency_contact, container, false);

        etNombre = view.findViewById(R.id.etNombreContacto);
        etTelefono = view.findViewById(R.id.etTelefonoContacto);
        btnNext = view.findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {
            // Save contact info
            // Navigate to next page
            ((WelcomeActivity)getActivity()).navigateToPage(contactNumber + 1);
        });

        return view;
    }
}