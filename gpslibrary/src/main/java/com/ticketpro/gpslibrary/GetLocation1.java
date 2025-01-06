package com.ticketpro.gpslibrary;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * This file is created by Triazine 27/11/20
 */
public class GetLocation1 {

    Activity ctx;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private final MyTracker.ADLocationListener locListener;
    Location mLastLocation = null;
    Location lastLocation = null;
    Location currentLocation = null;
    public static final String TAG = "WhereAmIActivity";
    private static final String ERROR_MSG = "Google Play services are unavailable.";
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private FusedLocationProviderClient mFusedLocationClient;

    public GetLocation1(Activity ctx, MyTracker.ADLocationListener locListener) {
        this.ctx = ctx;
        this.locListener = locListener;
    }

    public void track() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000) ;     // 10 seconds, in milliseconds

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx);

        checkForLocationRequest();
        //checkForLocationSettings();

        // Check if we have permission to access high accuracy fine location.
        int permission = ActivityCompat.checkSelfPermission(ctx, ACCESS_FINE_LOCATION);

        // If permission is granted, fetch the last location.
        if (permission == PERMISSION_GRANTED) {
            getLastLocation();
            //callCurrentLocation();
        }
    }
    public void checkForLocationRequest(){
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10*1000);
        locationRequest.setFastestInterval(1 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


    }
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(ctx, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                            String result = "Last known Location Latitude is " +
                                    mLastLocation.getLatitude() + "\n" +
                                    "Last known longitude Longitude is " + mLastLocation.getLongitude();

                            //resultTextView.setText(result);
                            fetchLocation(mLastLocation);
                        } else {
                            //showSnackbar("No Last known location found. Try current location..!");
                        }
                    }});
    }
    public void callCurrentLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(ctx, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {

                return;
            }

            mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    currentLocation = (Location) locationResult.getLastLocation();

                    String result = "Current Location Latitude is " +
                            currentLocation.getLatitude() + "\n" +
                            "Current location Longitude is " + currentLocation.getLongitude();

                    fetchLocation(currentLocation);
                    //resultTextView.setText(result);
                }
            }, Looper.myLooper());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fetchLocation(final Location location) {

        Observable.fromCallable(new Callable<ADLocation>() {
                    @Override
                    public ADLocation call() throws Exception {
                        ADLocation adLocation = null;
                        try {
                            List<Address> addresses;
                            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
                            adLocation = new ADLocation();
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            String streetNumber = addresses.get(0).getSubThoroughfare();
                            String thoroughfare = addresses.get(0).getThoroughfare();
                            if (thoroughfare == null && TextUtils.isEmpty(thoroughfare)) {
                                final String addressLine = addresses.get(0).getAddressLine(0);
                                final String[] split = addressLine.split(",");
                                //String str = split[0].replaceAll("[\\d.]", "");
                                thoroughfare = split[0];
                                streetNumber = "";

                            }
                            adLocation.address = thoroughfare;//addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            adLocation.city = addresses.get(0).getLocality();
                            adLocation.state = addresses.get(0).getAdminArea();
                            adLocation.country = addresses.get(0).getCountryName();
                            adLocation.pincode = addresses.get(0).getPostalCode();
                            adLocation.lat = location.getLatitude();
                            adLocation.longi = location.getLongitude();
                            adLocation.streetNumber = streetNumber;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return adLocation;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ADLocation>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ADLocation adLocation) {
                        locListener.whereIAM(adLocation);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

}
