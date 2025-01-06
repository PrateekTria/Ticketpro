package com.ticketpro.parking.activity;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Ticket;
import com.ticketpro.parking.R;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class ChalkVehicleOnMapViewActivity extends FragmentActivity implements
        OnTouchListener, OnMarkerClickListener, LocationListener,
        OnInfoWindowClickListener, OnMapReadyCallback {

    final int DATA_ERROR = 0;
    final int DATA_SUCCESSFULL = 1;
    final int ERROR_LOAD = 1;
    final int ERROR_SERVICE = 2;
    private ChalkVehicle activeChalk;
    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private Handler errorHandler;
    private LocationManager locationManager;
    private GoogleMap googleMap;
    private ChalkPicture chalkPicture;
    private TextView dateTextView;
    private TextView locationTextView;
    private TextView statusTextView;
    private TextView durationTextView;
    private ImageView previewImg;
    private Bitmap bitmap;
    private Dialog dialog;

    private Location gpsLocation;
    private HashMap<Marker, ChalkVehicle> chalkMarkers;
    private LatLngBounds.Builder builder;
    private LatLngBounds bounds;
    private SupportMapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.chalk_on_map_view);

            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);
            mapFragment.getMapAsync(this);

            //Initialize Business Logic Handler
            dataLoadingHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    initOverlayMarkers();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            };


            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    setUpMapIfNeeded();

                    switch (msg.what) {
                        case ERROR_LOAD:
                            Toast.makeText(getBaseContext(), "Failed to load chalks. Please try again.", Toast.LENGTH_SHORT).show();
                            break;

                        case ERROR_SERVICE:
                            Toast.makeText(getBaseContext(), TPConstant.GENERIC_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            bindDataAtLoadingTime();
            setUpMapIfNeeded();
            chalkMarkers = new HashMap<>();
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            Location myLocation = getMyLocation();

            if (myLocation != null) {
                onLocationChanged(location);
            } else if (location != null) {
                onLocationChanged(location);
            }

            locationManager.requestLocationUpdates(provider, 10000, 0, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Location getMyLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }
        return myLocation;
    }

    public void bindDataAtLoadingTime() {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent data = getIntent();
                long chalkId = data.getLongExtra("ChalkId", 0);
                try {
                    activeChalk = ChalkVehicle.getChalkVehicleById(chalkId);
                    dataLoadingHandler.sendEmptyMessage(DATA_SUCCESSFULL);
                } catch (Exception e) {
                    Log.e(TPConstant.TAG, "Failed loading chalk details");
                    errorHandler.sendEmptyMessage(ERROR_LOAD);
                }
            }
        }).start();
    }


    private void setUpMapIfNeeded() {
	    /*if (googleMap == null) {
	    	googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview)).getMap();
	        if (googleMap != null) {
	        	googleMap.getUiSettings().setZoomControlsEnabled(true);
	    	    googleMap.getUiSettings().setAllGesturesEnabled(true);
	    	    googleMap.setOnMarkerClickListener(this);
	    	    googleMap.setOnInfoWindowClickListener(this);
	    	    googleMap.setMyLocationEnabled(true);
	    	}
	    }*/

        if (googleMap!=null) {
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.setOnMarkerClickListener(ChalkVehicleOnMapViewActivity.this);
            googleMap.setOnInfoWindowClickListener(ChalkVehicleOnMapViewActivity.this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);
        }else {

        }
    }

    private void initOverlayMarkers() {
        setUpMapIfNeeded();
        if (googleMap == null) {
            Toast.makeText(getBaseContext(), "Failed to initialize Goolge Maps.", Toast.LENGTH_SHORT).show();
            return;
        }

        googleMap.clear();
        chalkMarkers.clear();
        LatLng position = null;

        if (activeChalk.getLatitude() == null
                || activeChalk.getLatitude().equals("")
                || activeChalk.getLongitude() == null
                || activeChalk.getLongitude().equals("")) {
            return;
        }

        Duration duration = Duration.getDurationById(activeChalk.getDurationId());
        if (duration == null) {
            return;
        }

        int iconResourceId;
        int mins = duration.getDurationMinutes();
        long diff = (new Date().getTime() - activeChalk.getChalkDate().getTime());
        long expTime = (diff / 1000) / 60;
        if (expTime > mins) {
            iconResourceId = R.drawable.car_red;
        } else {
            iconResourceId = R.drawable.car_green;
        }

        double lat = Double.parseDouble(activeChalk.getLatitude());
        double lng = Double.parseDouble(activeChalk.getLongitude());
        position = new LatLng(lat, lng);

        MarkerOptions options = new MarkerOptions();
        options.position(position);
        options.icon(BitmapDescriptorFactory.fromResource(iconResourceId));
        options.title(activeChalk.getPlate());

        Marker marker = googleMap.addMarker(options);
        chalkMarkers.put(marker, activeChalk);
        try {
            builder.include(marker.getPosition());
            bounds = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (position != null) {
            locationManager.removeUpdates(this);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        showInfoWindow(activeChalk);
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        showInfoWindow(activeChalk);
    }


    private void showInfoWindow(final ChalkVehicle chalk) {
        if (chalk == null) {
            Toast.makeText(this, "Failed to load chalk details.", Toast.LENGTH_LONG).show();
            return;
        }

        if (chalk.getChalkPictures().size() > 0) {
            chalkPicture = chalk.getChalkPictures().get(0);
        }

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.map_overlay);
        dialog.setTitle("Chalk Vehicle Details");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dateTextView = (TextView) dialog.findViewById(R.id.overlay_chalk_date_textview);
        locationTextView = (TextView) dialog.findViewById(R.id.overlay_chalk_location_textview);
        durationTextView = (TextView) dialog.findViewById(R.id.overlay_chalk_duration_textview);
        statusTextView = (TextView) dialog.findViewById(R.id.overlay_chalk_status_textview);
        previewImg = (ImageView) dialog.findViewById(R.id.overlay_chalk_image);

        Button closeBtn = (Button) dialog.findViewById(R.id.overlay_close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button writeBtn = (Button) dialog.findViewById(R.id.overlay_write_ticket_btn);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    int mins = Duration.getDurationMinsById(chalk.getDurationId(), ChalkVehicleOnMapViewActivity.this);
                    long diff = (new Date().getTime() - chalk.getChalkDate().getTime());
                    long expTime = (diff / 1000) / 60;
                    if (expTime < mins) {
                        Toast.makeText(ChalkVehicleOnMapViewActivity.this, "Chalk is not exipired. You can write ticket for expired chalks only.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Ticket ticket = TPApplication.getInstance().createTicketForChalk(chalk);
                    TPApplication.getInstance().setActiveTicket(ticket);

                    Intent i = new Intent();
                    i.setClass(ChalkVehicleOnMapViewActivity.this, WriteTicketActivity.class);
                    i.putExtra("ChalkPicture", true);
                    i.putExtra("ChalkId", chalk.getChalkId());
                    ChalkVehicleOnMapViewActivity.this.startActivity(i);

                } catch (Exception e) {
                }

            }
        });


        ImageView navigateBtn = (ImageView) dialog.findViewById(R.id.overlay_navigate_btn);
        navigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsLocation != null) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=" + gpsLocation.getLatitude() + "," + gpsLocation.getLongitude()
                                    + "&daddr=" + chalk.getLatitude() + "," + chalk.getLongitude()));

                    startActivity(intent);
                }
            }
        });

        dateTextView.setText(DateUtil.getStringFromDate(chalk.getChalkDate()));
        locationTextView.setText(chalk.getLocation());

        long diff = (new Date().getTime() - chalk.getChalkDate().getTime());
        int minutes = (int) ((diff / (1000 * 60)) % 60);
        int hours = (int) ((diff / (1000 * 60 * 60)));
        String hrs = (hours < 10) ? ("0" + hours) : hours + "";
        String min = (minutes < 10) ? ("0" + minutes) : minutes + "";
        statusTextView.setText("Elapsed " + hrs + ":" + min + " hrs/min");

        try {
            int mins = Duration.getDurationMinsById(chalk.getDurationId(), this);
            durationTextView.setText(mins + " Mins");
            long expTime = (diff / 1000) / 60;
            if (expTime < mins) {
                writeBtn.setClickable(false);
                writeBtn.setBackgroundResource(R.drawable.btn_disabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (chalkPicture != null) {
            try {
                if (bitmap != null) {
                    bitmap.recycle();
                    System.gc();
                }

                File imgFile = new File(chalkPicture.getImagePath());
                if (imgFile.exists()) {
                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    previewImg.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
            }
        }

        dialog.show();
    }

    public void backAction(View view) {
        locationManager.removeUpdates((android.location.LocationListener) this);
        finish();
    }

    protected boolean isRouteDisplayed() {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        setUpMapIfNeeded();

        gpsLocation = location;

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        if (googleMap != null) {

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap = googleMap1;

        if (googleMap != null) {
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.setOnMarkerClickListener(ChalkVehicleOnMapViewActivity.this);
            googleMap.setOnInfoWindowClickListener(ChalkVehicleOnMapViewActivity.this);
            googleMap.setMyLocationEnabled(true);
        }
    }
}
