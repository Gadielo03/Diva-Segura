package com.example.divasegura.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.divasegura.R;
import com.example.divasegura.activities.Alert;
import com.example.divasegura.activities.MainActivity;
import com.example.divasegura.controladores.RegistroAlertaController;
import com.example.divasegura.controladores.UsuariosController;
import com.example.divasegura.modelos.Usuario;
import com.example.divasegura.utils.AppPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainScreenFragment extends Fragment {

    private AppCompatButton btnAlert;
    private ImageButton btnCall;
    private ImageButton btnSeguridadPublica;

    // Class level variables for better management
    private Alert alert;
    private CountDownTimer countDownTimer;
    private Handler handler;
    private String originalAlertButtonText;
    private boolean isCountingDown = false;
    private static final int HOLD_TIME_REQUIRED = 2800; // 3 seconds
    private String lastPhotoPath;
    private RegistroAlertaController registroAlertaController;
    private UsuariosController usuariosController;
    private Usuario usuario;
    public double latitud;
    public double longitud;
    public MainScreenFragment() {
        // Required empty public constructor
    }

    public static MainScreenFragment newInstance() {
        return new MainScreenFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        btnAlert = view.findViewById(R.id.btnAlert);
        btnCall = view.findViewById(R.id.btnCall);
        btnSeguridadPublica = view.findViewById(R.id.btnSeguridadPublica);

        // Initialize helper objects
        handler = new Handler(Looper.getMainLooper());
        alert = new Alert(getContext());

        registroAlertaController = new RegistroAlertaController(this.getContext());
        usuariosController = new UsuariosController(this.getContext());
        usuariosController.open();
        registroAlertaController.open();
        usuario = usuariosController.obtenerUsuarioUnico();

        // Set up button listeners
        setupAlertButton();
        setupCallButtons();
    }

    private void setupAlertButton() {
        btnAlert.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handleButtonPress();
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    handleButtonRelease();
                    return true;
            }
            return false;
        });
    }

    private void handleButtonPress() {
        isCountingDown = true;
        originalAlertButtonText = btnAlert.getText().toString();

        // Start the countdown
        countDownTimer = new CountDownTimer(HOLD_TIME_REQUIRED, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000) + 1;
                btnAlert.setText("ENVIANDO ALERTA EN\n" + secondsLeft + "...");
            }

            @Override
            public void onFinish() {
                if (isCountingDown) {
                    sendEmergencyAlert();
                    btnAlert.setText("¡ALERTA ENVIADA!");
                    // Reset after 2 seconds
                    handler.postDelayed(() -> btnAlert.setText(originalAlertButtonText), 2000);
                }
            }
        }.start();

        // Start taking photos
        if(AppPreferences.isAutomaticTakePictureEnabled(this.getContext())){
            try {
                 alert.startTakingPhotos();
                 lastPhotoPath = alert.PhotoPath;
            } catch (Exception e) {
                 Toast.makeText(getContext(), "Error al iniciar la cámara", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleButtonRelease() {
        isCountingDown = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            btnAlert.setText(originalAlertButtonText);
        }

        try {
            alert.stopTakingPhotos();
            lastPhotoPath = alert.PhotoPath;
        } catch (Exception e) {
            // Silent handling - already stopping
        }
        guardarRegistroAlerta();
    }

    private void setupCallButtons() {
        btnCall.setOnClickListener(v -> {
            alert.callNumber("tel:8713345900");
        });

        btnSeguridadPublica.setOnClickListener(v -> {
            alert.callNumber("tel:7290099");
        });
    }

    private void sendEmergencyAlert() {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.triggerEmergencyAlert();
            latitud = mainActivity.latitud;
            longitud = mainActivity.longitud;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        // Clean up resources when fragment is paused
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        try {
            alert.stopTakingPhotos();
        } catch (Exception e) {
            // Silent cleanup
        }
    }

    public boolean guardarRegistroAlerta(){
        // Crear un formato para la fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long id;

        // Obtener la fecha actual
        Date date = new Date();

        // Convertir la fecha a String
        String fechaActual = dateFormat.format(date);

        if(AppPreferences.isAutomaticTakePictureEnabled(this.getContext())) {
            id = registroAlertaController.insertarRegistroAlertaConFoto(
                    usuario.getId(),
                    fechaActual,
                    latitud,
                    longitud,
                    lastPhotoPath
            );
        }
        else {
            id = registroAlertaController.insertarRegistroAlertaSinFoto(
                    usuario.getId(),
                    fechaActual,
                    latitud,
                    longitud
            );
        }
        if (id != -1) {
            Toast.makeText(getActivity(), "Información guardada correctamente", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(getActivity(), "Error al guardar la información", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}