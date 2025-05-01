package com.example.divasegura.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.example.divasegura.R;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CentroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centro); // Asume que el XML se llama activity_information_center.xml

        // Configurar botón de volver
        MaterialButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Configurar preguntas frecuentes (acordeón)
        setupFAQ();


        // Set click listener for the back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity to go back
                finish();

                // Optional: Add animation
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    // Optional: Handle device back button the same way
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Add the same animation for consistency
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }




    private void setupFAQ() {
        // Pregunta 1
        LinearLayout question1Layout = findViewById(R.id.question1_layout);
        ImageView expandIcon1 = findViewById(R.id.expand_icon1);
        TextView answer1 = findViewById(R.id.answer1);

        question1Layout.setOnClickListener(v -> {
            if (answer1.getVisibility() == View.VISIBLE) {
                answer1.setVisibility(View.GONE);
                expandIcon1.setImageResource(R.drawable.ic_expand_more);
            } else {
                answer1.setVisibility(View.VISIBLE);
                expandIcon1.setImageResource(R.drawable.ic_expand_less);
            }
        });

        // Pregunta 2
        LinearLayout question2Layout = findViewById(R.id.question2_layout);
        ImageView expandIcon2 = findViewById(R.id.expand_icon2);
        TextView answer2 = findViewById(R.id.answer2);

        question2Layout.setOnClickListener(v -> {
            if (answer2.getVisibility() == View.VISIBLE) {
                answer2.setVisibility(View.GONE);
                expandIcon2.setImageResource(R.drawable.ic_expand_more);
            } else {
                answer2.setVisibility(View.VISIBLE);
                expandIcon2.setImageResource(R.drawable.ic_expand_less);
            }
        });
    }

}