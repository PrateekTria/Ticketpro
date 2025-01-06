package com.ticketpro.gpslibrary;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

public class MyTracker implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Activity ctx;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ADLocationListener locListener;
    Location currentLocation = null;

    public static final String TAG = "WhereAmIActivity";
    private static final String ERROR_MSG = "Google Play services are unavailable.";
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            if (location != null) {
                fetchLocation(location);
            }
        }
    };

    public MyTracker(Activity ctx, ADLocationListener locListener) {
        this.ctx = ctx;
        this.locListener = locListener;
    }

    public void track() {
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


    }
  /*  private void getLastLocation() {
        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx);
        try {
            if (ActivityCompat.checkSelfPermission(ctx, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(ctx, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(ctx, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        fetchLocation(location);
                    }
                });
                fusedLocationClient.getLastLocation().addOnFailureListener(ctx, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // requestLocationUpdates();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(ctx, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(ctx, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED) {
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    currentLocation = (Location) locationResult.getLastLocation();

                    String result = "Current Location Latitude is " +
                            currentLocation.getLatitude() + "\n" +
                            "Current location Longitude is " + currentLocation.getLongitude();

                    fetchLocation(currentLocation);
                }
            }, Looper.myLooper());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("My Tracker", "Location services connected!.");

      /*  if (ActivityCompat.checkSelfPermission(ctx, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        fetchLocation(location);

        if (location == null) {
            if (ActivityCompat.checkSelfPermission(ctx, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }*/

        // Check if we have permission to access high accuracy fine location.
        int permission = ActivityCompat.checkSelfPermission(ctx, ACCESS_FINE_LOCATION);

        // If permission is granted, fetch the last location.
        if (permission == PERMISSION_GRANTED) {
            //getLastLocation();
            requestLocationUpdates();
        }
        // Check of the location settings are compatible with our Location Request.
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(ctx);

      /*  final Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(builder.build());
        locationSettingsResponseTask.addOnSuccessListener(ctx, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //requestLocationUpdates();

            }
        });

       locationSettingsResponseTask.addOnFailureListener(ctx, new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

           }
       });*/
    }

    private void fetchLocation(final Location location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Address> addresses;
                Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
                try {
                    ADLocation adLocation = new ADLocation();
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    String streetNumber = addresses.get(0).getSubThoroughfare();
                    String thoroughfare = addresses.get(0).getThoroughfare();
                    if (thoroughfare==null && TextUtils.isEmpty(thoroughfare)){
                        final String addressLine = addresses.get(0).getAddressLine(0);
                        final String[] split = addressLine.split(",");
                        //String str = split[0].replaceAll("[\\d.]", "");
                        thoroughfare = split[0];
                        streetNumber= "";

                    }
                    adLocation.address = thoroughfare;//addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    adLocation.city = addresses.get(0).getLocality();
                    adLocation.state = addresses.get(0).getAdminArea();
                    adLocation.country = addresses.get(0).getCountryName();
                    adLocation.pincode = addresses.get(0).getPostalCode();
                    adLocation.lat = location.getLatitude();
                    adLocation.longi = location.getLongitude();
                    adLocation.streetNumber = streetNumber;
                    locListener.whereIAM(adLocation);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("My Tracker", "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("My Tracker", "Location service connection failed.");
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(ctx,"Lat = "+location.getLatitude()+"  Long= "+location.getLongitude(),Toast.LENGTH_LONG).show();
        Log.i("My Tracker", "Location change");
        fetchLocation(location);
    }

    public interface ADLocationListener {
        void whereIAM(ADLocation loc);
    }
}



