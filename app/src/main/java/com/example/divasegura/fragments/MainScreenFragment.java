package com.example.divasegura.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;

import com.example.divasegura.R;
import com.example.divasegura.activities.Alert;
import com.example.divasegura.activities.MainActivity;

public class MainScreenFragment extends Fragment {

    private AppCompatButton btnAlert;
    private ImageButton btnCall;
    private ImageButton btnSeguridadPublica;

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

        btnAlert = view.findViewById(R.id.btnAlert);
        btnCall = view.findViewById(R.id.btnCall);
        btnSeguridadPublica = view.findViewById(R.id.btnSeguridadPublica);

        btnAlert.setOnTouchListener(new View.OnTouchListener() {
            private Handler handler = new Handler();
            private int holdTimeRequired = 3000; // 3 seconds
            private boolean isCountingDown = false;
            private CountDownTimer countDownTimer;
            private String originalText;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isCountingDown = true;
                        originalText = btnAlert.getText().toString();
                        countDownTimer = new CountDownTimer(holdTimeRequired, 1000) {
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
                                    handler.postDelayed(() -> btnAlert.setText(originalText), 2000);
                                }
                            }
                        }.start();
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isCountingDown = false;
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            btnAlert.setText(originalText);
                        }
                        return true;
                }
                return false;
            }
        });

        btnCall.setOnClickListener(v -> {
            Alert alert = new Alert(getContext());
            alert.callNumber("tel:8713345900");
        });

        btnSeguridadPublica.setOnClickListener(v -> {
            Alert alert = new Alert(getContext());
            alert.callNumber("tel:7290099");
        });
    }

    private void sendEmergencyAlert() {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.triggerEmergencyAlert();
        }
    }
}