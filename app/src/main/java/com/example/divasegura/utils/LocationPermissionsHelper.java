package com.example.divasegura.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationPermissionsHelper {
    public static final int REQUEST_LOCATION_PERMISSION = 1012;
    private final Context context;
    private final PermissionCallback callback;

    public interface PermissionCallback {
        void onPermissionGranted();
        void onPermissionDenied();
    }


    public LocationPermissionsHelper(Context context, PermissionCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void checkLocationPermission() {
        if (hasLocationPermission()) {
            callback.onPermissionGranted();
        } else {
            requestLocationPermission();
        }
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions((Activity) context,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERMISSION);
    }

    public void onRequestPermissionResult(int requestCode, int [] grantResults) {
        if(requestCode == REQUEST_LOCATION_PERMISSION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callback.onPermissionGranted();
            } else {
                callback.onPermissionDenied();
            }
        }
    }
}
