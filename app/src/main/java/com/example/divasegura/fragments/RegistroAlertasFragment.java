package com.example.divasegura.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.divasegura.R;
import com.example.divasegura.controladores.RegistroAlertaController;
import com.example.divasegura.modelos.RegistroAlerta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        listaRegistros = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.rvRegistros);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView tvEmptyState = view.findViewById(R.id.tvEmptyState);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressBar.setVisibility(View.VISIBLE);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        progressBar.setVisibility(View.VISIBLE);
        executor.execute(() -> {
            ArrayList<RegistroAlerta> result = registroAlertaController.obtenerTodosLosRegistros();
            handler.post(() -> {
                listaRegistros = result;
                if (listaRegistros.isEmpty()) {
                    tvEmptyState.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tvEmptyState.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                recyclerView.setAdapter(new RegistroAlertaAdapter(listaRegistros, getContext()));
                progressBar.setVisibility(View.GONE);
            });
        });

        return view;
    }


}