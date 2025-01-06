package com.ticketpro.parking.service;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

public class LocationUpdateWorker extends Worker {
    public LocationUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Context context = getApplicationContext();

            // Check if the app has location permission
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return Result.failure(); // Permission not granted
            }

            // Get the FusedLocationProviderClient instance
            FusedLocationProviderClient locationClient =
                    LocationServices.getFusedLocationProviderClient(context);

            // Create a location request
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10000); // Update every 10 seconds

            // Request location updates using a CancellationToken
            CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
            Task<android.location.Location> locationTask =
                    locationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken());

            // Block until the task completes
            android.location.Location location = Tasks.await(locationTask);

            // Use the fetched location for further processing
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Return the fetched location as output data
            Data outputData = new Data.Builder()
                    .putDouble("latitude", latitude)
                    .putDouble("longitude", longitude)
                    .build();

            return Result.success(outputData);
        } catch (Exception e) {
            return Result.failure();
        }
    }
}
