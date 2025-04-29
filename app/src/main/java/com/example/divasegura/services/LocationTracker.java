package com.example.divasegura.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationTracker extends Service implements LocationListener {
    private LocationManager locationManager;
    private Location lastLocation;
    private static final long MIN_TIME_MS = 5000;
    private static final float MIN_DISTANCE_M = 10;

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        startGpsUpdates();
    }

    private void startGpsUpdates() {
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_MS,
                    MIN_DISTANCE_M,
                    this
            );
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            Log.e("Location", "Permission denied");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            lastLocation = location;
            Log.d("LocationTracker", "New location: " + location.getLatitude() + ", " + location.getLongitude());
        }
    }

    public Location getLastLocation() {
        if(lastLocation != null) {
            Log.d("LocationTracker", "New location: " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude());
            return lastLocation;
        } else {
            Log.e("LocationTracker", "No location available");
            return null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override public void onProviderEnabled(String provider) {}
    @Override public void onProviderDisabled(String provider) {}
}