package com.imraniman.projet_android;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class TrackingService extends Service {

    private static final String TAG = "TrackingService";
    private static final String CHANNEL_ID = "TrackingChannel";
    private static final int NOTIFICATION_ID = 1001;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private GpsDatabaseHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        dbHelper = new GpsDatabaseHelper(this);

        createNotificationChannel();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    Log.d(TAG, "Location received: " + location.getLatitude() + ", " + location.getLongitude());

                    dbHelper.addPosition(location.getLatitude(), location.getLongitude(), System.currentTimeMillis());
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Suivi GPS actif")
                .setContentText("Enregistrement des positions en arrière-plan")
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(NOTIFICATION_ID, notification);

        requestLocationUpdates();

        return START_STICKY; 
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000) 
                .setMinUpdateIntervalMillis(5000)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location permission not granted. Stopping service.");
            stopSelf();
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Service de Tracking GPS",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Utilisé pour le suivi GPS en arrière-plan");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; 
    }
}

