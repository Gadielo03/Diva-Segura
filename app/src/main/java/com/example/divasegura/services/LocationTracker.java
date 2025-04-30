package com.example.divasegura.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LocationTracker extends Service implements LocationListener {
    private LocationManager locationManager;
    private static Location lastLocation;
    private static final long MIN_TIME_MS = 5000;
    private static final float MIN_DISTANCE_M = 10;
    private static final String CHANNEL_ID = "location_channel";

    private Context locationContext;

    // Static instance to access service methods
    private static LocationTracker instance;

    public LocationTracker(){}

    public LocationTracker(Context locationContext){
       instance = this;
       this.locationContext = locationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) this.locationContext.getSystemService(Context.LOCATION_SERVICE);
        startGpsUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
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
            Log.d("LocationTracker", "New location: " +
                      location.getLatitude() + ", " + location.getLongitude());
        }
    }

    public Location getLastLocation() {
        if(lastLocation != null) {
            Log.d("LocationTracker", "Last location: " +
                  lastLocation.getLatitude() + ", " + lastLocation.getLongitude());
            return lastLocation;
        } else {
            Log.e("LocationTracker", "No location available");
            return null;
        }
    }

    // Non-static method for immediate location
    public Location getLocationRightNow() {
        try {
            if (ActivityCompat.checkSelfPermission(this.locationContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("LocationTracker", "Permission not granted");
                return null;
            }
            final CountDownLatch latch = new CountDownLatch(1);
            final Location[] result = new Location[1];

            LocationListener oneShotListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    result[0] = location;
                    latch.countDown();
                }
                @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override public void onProviderEnabled(String provider) {}
                @Override public void onProviderDisabled(String provider) {}
            };

            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, oneShotListener, null);
            locationManager.removeUpdates(oneShotListener);

            if (result[0] == null) {
                Log.e("LocationTracker", "No immediate location available");
            }
            return result[0];
        } catch (Exception e) {
            Log.e("LocationTracker", "Error getting immediate location: " + e.getMessage());
            return null;
        }
    }

    // New static method to be used to get immediate location
    public static Location getLocationRightNowStatic() {
        if (instance != null) {
            return instance.getLocationRightNow();
        } else {
            Log.e("LocationTracker", "Service is not running");
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