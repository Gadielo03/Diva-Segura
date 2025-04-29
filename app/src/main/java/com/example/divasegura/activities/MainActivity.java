package com.example.divasegura.activities;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.divasegura.controladores.CRUDHelper;
import com.example.divasegura.R;
import com.example.divasegura.modelos.Usuario;
import com.example.divasegura.services.LocationTracker;

import com.example.divasegura.activities.Alert;

import java.io.File;
import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {
    private CRUDHelper crudHelper;
    private Intent locationServiceIntent;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        imgFotoPerfil = findViewById(R.id.ivFotoUsuario);

        crudHelper = new CRUDHelper(this);

        usuario = new Usuario();
        Alert alert911 = new Alert(MainActivity.this);
        alert911.call911();


        //cargarUsuario();
        //startLocationTracking();
    }

    private void startLocationTracking() {
        locationServiceIntent = new Intent(this, LocationTracker.class);
        startService(locationServiceIntent);

        new android.os.Handler().postDelayed(() -> {
            LocationTracker tracker = new LocationTracker();
            Location lastLocation = tracker.getLastLocation();

            if(lastLocation != null) {
                String locationText = lastLocation.getLatitude() + "," + lastLocation.getLongitude();
                System.out.println("DEBUG LOCATION: " + locationText);
                Toast.makeText(this, locationText, Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("DEBUG LOCATION: No location available");
                Toast.makeText(this, "No location available", Toast.LENGTH_SHORT).show();
            }
        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationServiceIntent != null) {
            stopService(locationServiceIntent);
        }
    }

    private void cargarUsuario() {
        crudHelper.open();
        usuario = crudHelper.obtenerUsuario();

        if (usuario != null) {
            String imagePath = usuario.getRutaFoto();
        }
    }
}