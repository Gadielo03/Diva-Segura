package com.example.divasegura.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.divasegura.R;
import com.google.android.material.button.MaterialButton;

public class NumerosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numeros);

        // Find the back button from your layout
        MaterialButton btnBack = findViewById(R.id.btnBack);

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
}