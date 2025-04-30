package com.example.divasegura.activities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.divasegura.R;

public class InfoActivity extends AppCompatActivity {
    ImageButton btnViolentometro ,btnNumerosDeAtencion, btnCentroDeInformacion,btnModalidadesDeViolencia,btnTiposDeViolencia,btnAlerta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnViolentometro = findViewById(R.id.btnViolentometro);
        btnNumerosDeAtencion = findViewById(R.id.btnNumerosDeAtencion);
        btnCentroDeInformacion = findViewById(R.id.btnCentroDeInformacion);
        btnModalidadesDeViolencia = findViewById(R.id.btnModalidadesDeViolencia);
        btnTiposDeViolencia = findViewById(R.id.btnTiposDeViolencia);
        btnAlerta = findViewById(R.id.btnAlerta);
    }


}