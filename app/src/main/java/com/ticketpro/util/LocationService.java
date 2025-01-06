package com.ticketpro.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.ticketpro.model.Feature;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rohit Kumar on 27-09-2019.
 */

public class LocationService extends Service {
    public static final String LOCATION_ACTION = "ACTION_LOCATION";
    private static int TIME_IN_MINUTES = 1000 * 5 * 60;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    Intent intent;
    IBinder mBinder = new LocalBinder();
    private long time;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(LOCATION_ACTION);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public LocationService getServerInstance() {
            return LocationService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            initLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }



    public String getTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return mDateFormat.format(new Date());
    }

    public void initLocation() {
        int DISTANCE_IN_METER = 100;
        try {
            TIME_IN_MINUTES = Integer.parseInt(Feature.getFeatureValue(Feature.FT_LOCATION_UPDATE_TIME_MINUTES)) * 1000 * 60;
            DISTANCE_IN_METER = Integer.parseInt(Feature.getFeatureValue(Feature.FT_LOCATION_UPDATE_DISTANCE_METER));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            TIME_IN_MINUTES = 1000 * 5 * 60;
            DISTANCE_IN_METER = 100;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_IN_MINUTES,
                        DISTANCE_IN_METER, listener);

                // currentLocation =
                // lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                // according to gps update location
            }
            // through network Provider
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_IN_MINUTES,
                        DISTANCE_IN_METER, listener);

                // currentLocation =
                // lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                // according to gps update location
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Location getLocation() {
        if (previousBestLocation == null) {
            initLocation();
        }
        return previousBestLocation;
    }

    /*@Override
    public IBinder onBind(Intent intent) {
        return null;
    }*/

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }
        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TIME_IN_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TIME_IN_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }


    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(listener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }


    public  class MyLocationListener implements LocationListener {
        synchronized public void onLocationChanged(final Location loc) {
            //System.out.println("MyLocationReceiver locationlistener");

            if (isBetterLocation(loc, previousBestLocation)) {
                //if (loc.getAccuracy() < 30) {
                    loc.getLatitude();
                    loc.getLongitude();
                    Bundle mBundle = new Bundle();
                    mBundle.putDouble("Latitude", loc.getLatitude());
                    mBundle.putDouble("Longitude", loc.getLongitude());
                    intent.putExtra("BUNDLE", mBundle);
                    intent.setAction(LOCATION_ACTION);
                    if (System.currentTimeMillis() - time > 3000)
                        sendBroadcast(intent);
                    time = System.currentTimeMillis();
                }
           // }
            System.gc();
        }

        public void onProviderDisabled(String provider) {
            //Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            //Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }
}