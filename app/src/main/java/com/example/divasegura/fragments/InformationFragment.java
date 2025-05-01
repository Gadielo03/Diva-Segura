package com.example.divasegura.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.divasegura.R;
import com.google.android.material.navigation.NavigationView;

public class InformationFragment extends Fragment {

    private ImageButton btnViolentometro, btnNumerosDeAtencion, btnCentroDeInformacion;
    private ImageButton btnModalidadesDeViolencia, btnTiposDeViolencia, btnAlerta;

    public InformationFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar botones
        btnViolentometro = view.findViewById(R.id.btnViolentometro);
        btnNumerosDeAtencion = view.findViewById(R.id.btnNumerosDeAtencion);
        btnCentroDeInformacion = view.findViewById(R.id.btnCentroDeInformacion);
        btnModalidadesDeViolencia = view.findViewById(R.id.btnModalidadesDeViolencia);
        btnTiposDeViolencia = view.findViewById(R.id.btnTiposDeViolencia);
        btnAlerta = view.findViewById(R.id.btnAlerta);


        btnViolentometro.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.divasegura.activities.ViolentometroActivity.class);
            startActivity(intent);
            // Agregar animación de entrada y salida
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        btnNumerosDeAtencion.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.divasegura.activities.NumerosActivity.class);
            startActivity(intent);
            // Agregar animación de entrada y salida
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        btnCentroDeInformacion.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.divasegura.activities.CentroActivity.class);
            startActivity(intent);
            // Agregar animación de entrada y salida
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        btnModalidadesDeViolencia.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.divasegura.activities.ModalidadesActivity.class);
            startActivity(intent);
            // Agregar animación de entrada y salida
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        btnTiposDeViolencia.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.divasegura.activities.TiposActivity.class);
            startActivity(intent);

            // Agregar animación de entrada y salida
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        btnAlerta.setOnClickListener(v -> {
            ViewPager2 viewPager = requireActivity().findViewById(R.id.viewPager);
            if (viewPager != null) {
                viewPager.setCurrentItem(0, true); // Ir a Home
            }
            // Actualizar el menú lateral
            NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
            if (navigationView != null) {
                navigationView.setCheckedItem(R.id.nav_home);
            }
        });
        // Configura los demás listeners de manera similar
    }
}