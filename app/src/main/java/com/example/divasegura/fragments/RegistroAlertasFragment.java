package com.example.divasegura.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro_alertas, container, false);
        registroAlertaController = new RegistroAlertaController(this.getContext());
        registroAlertaController.open();
        listaRegistros = registroAlertaController.obtenerTodosLosRegistros();

        return view;
    }
}