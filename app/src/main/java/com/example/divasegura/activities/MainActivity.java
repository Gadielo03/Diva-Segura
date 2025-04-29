package com.example.divasegura.activities;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import android.view.MenuItem;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CRUDHelper crudHelper;
    private Intent locationServiceIntent;
    Usuario usuario;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set up drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        crudHelper = new CRUDHelper(this);
        usuario = new Usuario();
        Alert alert911 = new Alert(MainActivity.this);
        alert911.call911();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_info) {
            // Handle information action
            Toast.makeText(this, "Información", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_config) {
            // Handle configuration action
            Toast.makeText(this, "Configuración", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_alerts) {
            // Handle alerts action
            Toast.makeText(this, "Alertas", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_photo) {
            // Handle photo action
            Toast.makeText(this, "Tomar foto", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_terms) {
            // Handle terms action
            Toast.makeText(this, "Términos y condiciones", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_privacy) {
            // Handle privacy action
            Toast.makeText(this, "Aviso de privacidad", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}