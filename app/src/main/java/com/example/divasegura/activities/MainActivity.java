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

import com.example.divasegura.controladores.ContactoController;
import com.example.divasegura.controladores.UsuariosController;
import com.example.divasegura.modelos.Contacto;
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
import java.util.HashMap;
import java.util.Map;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.divasegura.R;
import com.example.divasegura.adapters.ViewPagerAdapter;
import com.example.divasegura.fragments.MainScreenFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Intent locationServiceIntent;
    private Usuario usuario;
    private Contacto contacto1,contacto2;
    private ContactoController contactoController;
    private UsuariosController usuariosController;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
     private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

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

        // Set up ViewPager
        viewPager = findViewById(R.id.viewPager);
        setupViewPager();

        usuariosController = new UsuariosController(this);
        contactoController = new ContactoController(this);
        usuariosController.open();
        contactoController.open();

        // Get user data
         usuario = usuariosController.obtenerUsuarioUnico();
         contacto1 = contactoController.obtenerContactoUnico(1);
         contacto2 = contactoController.obtenerContactoUnico(2);

        Alert alert911 = new Alert(MainActivity.this);
        //alert911.call911();
         startLocationTracking();
    }

    private void startLocationTracking() {
         try {
             locationServiceIntent = new Intent(this, LocationTracker.class);
             startService(locationServiceIntent);

             new android.os.Handler().postDelayed(() -> {
                 LocationTracker tracker = new LocationTracker();
                 Location lastLocation = tracker.getLastLocation();

                 if (lastLocation != null) {
                     String locationText = lastLocation.getLatitude() + "," + lastLocation.getLongitude();
                     System.out.println("DEBUG LOCATION: " + locationText);
                     Toast.makeText(this, locationText, Toast.LENGTH_SHORT).show();
                 } else {
                     System.out.println("DEBUG LOCATION: No location available");
                     Toast.makeText(this, "No location available", Toast.LENGTH_SHORT).show();
                 }
             }, 5000);
         }catch (Exception e){
                System.out.println("DEBUG LOCATION: " + e.getMessage());
                Toast.makeText(this, "Error starting location tracking", Toast.LENGTH_SHORT).show();
         }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationServiceIntent != null) {
            stopService(locationServiceIntent);
        }
    }
    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(MainScreenFragment.newInstance());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setUserInputEnabled(false); // Disable swiping if needed
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                // Crear un diccionario de mensajes
                Map<Integer, String> mensajes = new HashMap<>();
                mensajes.put(R.id.nav_info, "Información");
                mensajes.put(R.id.nav_config, "Configuración");
                mensajes.put(R.id.nav_alerts, "Alertas");
                mensajes.put(R.id.nav_photo, "Tomar foto");
                mensajes.put(R.id.nav_terms, "Términos y condiciones");
                mensajes.put(R.id.nav_privacy, "Aviso de privacidad");

                // Obtener el mensaje correspondiente al id
                String mensaje = mensajes.get(id);

                if (mensaje != null) {
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
}