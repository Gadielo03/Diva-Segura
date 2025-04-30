package com.example.divasegura.activities;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;


public class Alert {
    private Context context;
    private static final int REQUEST_CALL_PERMISSION = 1;

    public Alert(Context context) {
        this.context = context;
    }

    public void callNumber(String phoneNumber) {
        // Check if we have the permission to make phone calls
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            try {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(phoneNumber));//ig: @alejandroorgg "tel:8713345900"
                context.startActivity(callIntent);
            } catch (Exception e) {
                Toast.makeText(context, "Error al realizar la llamada", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Permiso para llamadas no concedido", Toast.LENGTH_SHORT).show();
            // If this method is called from an Activity, you would typically request permission here
             ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }
    }

}
