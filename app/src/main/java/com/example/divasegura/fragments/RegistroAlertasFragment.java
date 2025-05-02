package com.example.divasegura.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.divasegura.R;
import com.example.divasegura.controladores.RegistroAlertaController;
import com.example.divasegura.modelos.RegistroAlerta;

import java.util.ArrayList;

public class RegistroAlertasFragment extends Fragment {

   private RegistroAlertaController registroAlertaController;
   public ArrayList<RegistroAlerta> listaRegistros;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_alertas, container, false);

        registroAlertaController = new RegistroAlertaController(this.getContext());
        registroAlertaController.open();
        listaRegistros = registroAlertaController.obtenerTodosLosRegistros();

        RecyclerView rvRegistros = view.findViewById(R.id.rvRegistros);
        TextView tvEmptyState = view.findViewById(R.id.tvEmptyState);

        // Mostrar mensaje si no hay registros
        if(listaRegistros.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvRegistros.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvRegistros.setVisibility(View.VISIBLE);

            rvRegistros.setLayoutManager(new LinearLayoutManager(getContext()));
            rvRegistros.setAdapter(new RegistroAlertaAdapter(listaRegistros, getContext()));
        }

        return view;
    }
}