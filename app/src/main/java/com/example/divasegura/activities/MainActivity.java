package com.example.divasegura.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.divasegura.controladores.ContactoController;
import com.example.divasegura.controladores.RegistroAlertaController;
import com.example.divasegura.controladores.UsuariosController;
import com.example.divasegura.fragments.ConfigurationEmergencyContactsFragment;
import com.example.divasegura.fragments.ConfigurationFragment;
import com.example.divasegura.fragments.InformationFragment;
import com.example.divasegura.fragments.TermsFragment;
import com.example.divasegura.fragments.RegistroAlertasFragment;
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
import java.util.ArrayList;
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
import com.example.divasegura.utils.LocationPermissionHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationPermissionHelper.PermissionCallback {
    private static final int SMS_PERMISSION_REQUEST_CODE = 100;
    private Intent locationServiceIntent;
    private Usuario usuario;
    private Contacto contacto1,contacto2;
    private ContactoController contactoController;
    private UsuariosController usuariosController;
    private RegistroAlertaController registroAlertaController;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    public ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private LocationPermissionHelper locationPermissionHelper;
    public Double latitud;
    public Double longitud;

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
        navigationView.setCheckedItem(R.id.nav_home);

        // Set up ViewPager
        viewPager = findViewById(R.id.viewPager);
        setupViewPager();
         viewPager.setCurrentItem(0);

        usuariosController = new UsuariosController(this);
        contactoController = new ContactoController(this);
        registroAlertaController = new RegistroAlertaController(this);
        usuariosController.open();
        contactoController.open();
        registroAlertaController.open();

        // Get user data
         usuario = usuariosController.obtenerUsuarioUnico();
         contacto1 = contactoController.obtenerContactoUnico(1);
         contacto2 = contactoController.obtenerContactoUnico(2);

         locationPermissionHelper = new LocationPermissionHelper(this, this);
         requestLocationPermissionOnStart();
    }

    private void requestLocationPermissionOnStart() {
         if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                 Manifest.permission.ACCESS_FINE_LOCATION)) {
             showLocationRationaleDialog();
         } else {
             locationPermissionHelper.checkLocationPermission();
         }
    }

    private void showLocationRationaleDialog() {
         new AlertDialog.Builder(this)
                 .setTitle("Permiso de ubicación requerida")
                 .setMessage("Diva-Segura necesita acceso a su ubicación para enviar alertas de emergencia.")
                 .setPositiveButton("OK", (dialog, which) -> {
                     locationPermissionHelper.checkLocationPermission();
                 })
                 .setNegativeButton("Later", (dialog, which) -> {
                     proceedWithoutLocation();
                 })
                 .setCancelable(false)
                 .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Handle Location Permission Result
        if (requestCode == LocationPermissionHelper.REQUEST_LOCATION_PERMISSION) {
            locationPermissionHelper.onRequestPermissionsResult(requestCode, grantResults);
        }
        // Handle SMS Permission Result
        else if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                triggerEmergencyAlert();
            } else {
                Toast.makeText(this,
                        "Se requiere permiso para enviar mensajes de emergencia",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onPermissionGranted() {
        startLocationService();
        proceedWithApp();
    }

    @Override
    public void onPermissionDenied() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            showLocationRationaleDialog();
        } else {
            proceedWithoutLocation();
        }
    }

    private void proceedWithApp() {
         Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show();
         startLocationService();
    }

    private void proceedWithoutLocation() {
         Toast.makeText(this, "Ubicación no disponible, es obligatorio para usar Diva-Segura", Toast.LENGTH_SHORT).show();
         requestLocationPermissionOnStart();
    }

    private void startLocationService() {
        Intent serviceIntent = new Intent(this, LocationTracker.class);
        ContextCompat.startForegroundService(this, serviceIntent);
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
                    Toast.makeText(this, locationText, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No location available", Toast.LENGTH_SHORT).show();
                }
            }, 5000);
        }catch (Exception e){
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
        viewPagerAdapter.addFragment(new InformationFragment()); // Añadir el fragmento de información
        viewPagerAdapter.addFragment(new ConfigurationEmergencyContactsFragment());
        viewPagerAdapter.addFragment(new ConfigurationFragment()); // Añadir el fragmento de configuración
        viewPagerAdapter.addFragment(new RegistroAlertasFragment()); // Añadir el fragmento de configuración
        viewPagerAdapter.addFragment(new TermsFragment());
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

      // Crear un diccionario de mensajes con índices
        Map<Integer, Pair<Integer, String>> mensajes = new HashMap<>();
        mensajes.put(R.id.nav_home, new Pair<>(0, "Inicio"));
        mensajes.put(R.id.nav_info, new Pair<>(1, "Información"));
        mensajes.put(R.id.nav_config_emergency_contacts, new Pair<>(2, "Configuración"));
        mensajes.put(R.id.nav_config_user, new Pair<>(3, "Configuración"));
        mensajes.put(R.id.nav_alerts, new Pair<>(4, "Alertas"));
        mensajes.put(R.id.nav_photo, new Pair<>(-1, "Tomar foto"));
        mensajes.put(R.id.nav_terms, new Pair<>(5, "Términos y condiciones"));
        mensajes.put(R.id.nav_privacy, new Pair<>(-1, "Aviso de privacidad"));

        // Manejar navegación entre fragmentos
        Pair<Integer, String> datos = mensajes.get(id);
        if (datos != null) {
            int indice = datos.first;
            String mensaje = datos.second;

            if (indice >= 0) {
                viewPager.setCurrentItem(indice); // Cambiar al fragmento correspondiente
            } else {
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show(); // Mostrar mensaje
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
     }


    // app/src/main/java/com/example/divasegura/activities/MainActivity.java

    public void triggerEmergencyAlert() {
        if (!checkSmsPermission()) {
            return; // Will request permission and return
        }
        // Get current location
        LocationTracker locationTracker = new LocationTracker(this);
        locationTracker.onCreate();
        Location currentLocation =  locationTracker.getLastLocation();

        // Prepare emergency message with location
        String locationText = "No hay ubicación disponible";
        if (currentLocation != null) {
            locationText = "https://maps.google.com/?q=" +
                       currentLocation.getLatitude() + "," +
                       currentLocation.getLongitude();
            latitud = currentLocation.getLatitude();
            longitud = currentLocation.getLongitude();
        }

        String emergencyMessage = "ALERTA DE EMERGENCIA: " + usuario.getNombre() +
                             " está en peligro. Ubicación: " + locationText;

        // Send message to registered contacts
        if (contacto1 != null && contacto1.getNumero() != null) {
            sendSMS(contacto1.getNumero(), emergencyMessage);
        }

        if (contacto2 != null && contacto2.getNumero() != null) {
            sendSMS(contacto2.getNumero(), emergencyMessage);
        }

        // Extension: Start continuous location sharing for 30 minutes
        startLocationSharing(30);

        // Display confirmation
        Toast.makeText(this, "Alerta de emergencia enviada", Toast.LENGTH_LONG).show();
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
        } catch (Exception e) {
            Toast.makeText(this, "Error al enviar mensaje: " + e.getMessage(),
                          Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void startLocationSharing(int minutes) {
        // Create intent for extended location sharing
        Intent extendedLocationIntent = new Intent(this, LocationTracker.class);
        extendedLocationIntent.putExtra("SHARING_DURATION", minutes * 60 * 1000); // Convert to milliseconds
        startService(extendedLocationIntent);

        // Schedule to stop location sharing after specified time
        new Handler().postDelayed(() -> {
            stopService(extendedLocationIntent);
            Toast.makeText(this, "Compartir ubicación finalizada",
                          Toast.LENGTH_SHORT).show();
        }, minutes * 60 * 1000);
    }

    private boolean checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }


}