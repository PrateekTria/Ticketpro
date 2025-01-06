package com.ticketpro.parking.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.User;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class ChalkVehicleMapViewActivity extends FragmentActivity implements
        OnTouchListener, OnMarkerClickListener, LocationListener,
        OnInfoWindowClickListener, OnMapReadyCallback {

    final int DATA_ERROR = 0;
    final int DATA_SUCCESSFULL = 1;
    final int ERROR_LOAD = 1;
    final int ERROR_SERVICE = 2;
    private List<ChalkVehicle> chalks;
    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private Handler errorHandler;
    private ChalkBLProcessor chalkMapViewBLProcessor;
    private TextView totalTextview;
    private boolean expiredOnly = false;
    private String selectedZone;
    private String selectedLocation;
    private String selectedUser;
    private LocationManager locationManager;
    private GoogleMap googleMap;
    private HashMap<Marker, ChalkVehicle> chalkMarkers;
    private ChalkPicture chalkPicture;
    private TextView dateTextView;
    private TextView locationTextView;
    private TextView statusTextView;
    private TextView durationTextView;
    private ImageView previewImg;
    private Bitmap bitmap;
    private Dialog dialog;

    private GestureDetector gestureDetector;
    private int currentPosition;
    private Location gpsLocation;
    private SupportMapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.chalk_map_view);

            //Initialize Business Logic Handler
            chalkMapViewBLProcessor = new ChalkBLProcessor((TPApplication) getApplicationContext());
            totalTextview = (TextView) findViewById(R.id.chalked_photos_total_textview);

            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);
            mapFragment.getMapAsync(this);

            Intent data = getIntent();
            selectedLocation = data.getStringExtra("SelectedLocation");
            selectedZone = data.getStringExtra("SelectedZone");
            selectedUser = data.getStringExtra("SelectedUser");

            dataLoadingHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (chalks == null) {
                        return;
                    }

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

            gestureDetector = new GestureDetector(new SwipeGestureDetector());
            View.OnTouchListener gestureListener = new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            };

            bindDataAtLoadingTime();
            setUpMapIfNeeded();
            chalkMarkers = new HashMap<>();
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
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
            Location location = locationManager.getLastKnownLocation(provider);
            Location myLocation = getMyLocation();

            if (myLocation != null) {
                onLocationChanged(location);
            } else if (location != null) {
                onLocationChanged(location);
            }

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
            locationManager.requestLocationUpdates(provider, 10000, 0, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Location getMyLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            myLocation = lm.getLastKnownLocation(provider);
        }
        return myLocation;
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

        if (googleMap != null) {
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.setOnMarkerClickListener(ChalkVehicleMapViewActivity.this);
            googleMap.setOnInfoWindowClickListener(ChalkVehicleMapViewActivity.this);
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

        int count = 0;
        for (ChalkVehicle chalk : chalks) {
            if (chalk.getLatitude() == null || chalk.getLatitude().equals("") || chalk.getLongitude() == null
                    || chalk.getLongitude().equals("")) {
                continue;
            }

            Duration duration = null;
            try {
                duration = Duration.getDurationById(chalk.getDurationId());
                if (duration == null) {
                    continue;
                }
            } catch (Exception e) {
                continue;
            }

            if (selectedLocation != null && selectedLocation.length() > 0 && !chalk.getLocation().equalsIgnoreCase(selectedLocation)) {
                continue;
            }

            if (selectedZone != null && selectedZone.length() > 0 && !duration.getTitle().equalsIgnoreCase(selectedZone)) {
                continue;
            }

            if (selectedUser != null) {
                User userInfo = User.getUserInfo(chalk.getUserId() == -1 ? chalk.getDeviceId() : chalk.getUserId());
                if (userInfo != null && !userInfo.getUsername().equalsIgnoreCase(selectedUser)) {
                    continue;
                }
            }

            int iconResourceId;
            int mins = duration.getDurationMinutes();
            long diff = (new Date().getTime() - chalk.getChalkDate().getTime());
            long expTime = (diff / 1000) / 60;
            if (expTime > mins) {
                iconResourceId = R.drawable.car_red;
            } else {
                iconResourceId = R.drawable.car_green;

                if (expiredOnly) {
                    continue;
                }
            }
            if (chalk.getLatitude()!=null && chalk.getLongitude()!=null) {
                double lat = Double.parseDouble(chalk.getLatitude());
                double lng = Double.parseDouble(chalk.getLongitude());
                position = new LatLng(lat, lng);

                MarkerOptions options = new MarkerOptions();
                options.position(position);
                options.icon(BitmapDescriptorFactory.fromResource(iconResourceId));
                options.title(chalk.getPlate());

                Marker marker = googleMap.addMarker(options);
                chalkMarkers.put(marker, chalk);
            }

            count++;
        }

        if (position != null) {
            locationManager.removeUpdates(this);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,15));
            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

        totalTextview.setText("Total (" + count + ")");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        ChalkVehicle chalk = chalkMarkers.get(marker);
        if (chalk == null) {
            return false;
        }

        currentPosition = getChalkPosition(chalk);
        showInfoWindow(chalk);
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        ChalkVehicle chalk = chalkMarkers.get(marker);
        if (chalk == null)
            return;

        currentPosition = getChalkPosition(chalk);
        showInfoWindow(chalk);
    }

    private int getChalkPosition(ChalkVehicle chalk) {
        int position = 0;
        Iterator<Marker> itr = chalkMarkers.keySet().iterator();
        while (itr.hasNext()) {
            position++;
            Marker marker = (Marker) itr.next();
            ChalkVehicle tempChalk = chalkMarkers.get(marker);
            if (tempChalk.getChalkId() == chalk.getChalkId()) {
                return position;
            }
        }

        return position;
    }


    private ChalkVehicle getChalkByIndex(int index) {
        if (index < 0 || index > (chalkMarkers.size() - 1)) {
            return null;
        }

        Object[] keys = chalkMarkers.keySet().toArray();
        Marker key = (Marker) keys[index];
        return chalkMarkers.get(key);
    }

    public void onLeftSwipe() {
        if (currentPosition < chalks.size()) {
            currentPosition++;
        }

        ChalkVehicle chalk = getChalkByIndex(currentPosition - 1);
        showInfoWindow(chalk);
    }

    public void onRightSwipe() {
        if (currentPosition > 1) {
            currentPosition--;
        }

        ChalkVehicle chalk = getChalkByIndex(currentPosition - 1);
        showInfoWindow(chalk);
    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap = googleMap1;

        if (googleMap != null) {
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.setOnMarkerClickListener(ChalkVehicleMapViewActivity.this);
            googleMap.setOnInfoWindowClickListener(ChalkVehicleMapViewActivity.this);
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
        }
    }

    private void showInfoWindow(final ChalkVehicle chalk) {
        if (chalk == null) {
            Toast.makeText(this, "Failed to load chalk details.", Toast.LENGTH_LONG).show();
            return;
        }

        if (chalk.getChalkPictures().size() > 0) {
            chalkPicture = chalk.getChalkPictures().get(0);
        } else {
            chalkPicture = null;
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new Dialog(this) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        dialog.setContentView(R.layout.map_overlay);
        dialog.setTitle("Chalk Vehicle Details (" + currentPosition + "/" + chalkMarkers.size() + ")");
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

        Button writeBtn = (Button) dialog.findViewById(R.id.overlay_write_ticket_btn);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    int mins = Duration.getDurationMinsById(chalk.getDurationId(), ChalkVehicleMapViewActivity.this);
                    long diff = (new Date().getTime() - chalk.getChalkDate().getTime());
                    long expTime = (diff / 1000) / 60;
                    if (expTime < mins) {
                        Toast.makeText(ChalkVehicleMapViewActivity.this, "Chalk is not exipired. You can write ticket for expired chalks only.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Ticket ticket = TPApplication.getInstance().createTicketForChalk(chalk);
                    TPApplication.getInstance().setActiveTicket(ticket);

                    Intent i = new Intent();
                    i.setClass(ChalkVehicleMapViewActivity.this, WriteTicketActivity.class);
                    i.putExtra("ChalkPicture", true);
                    i.putExtra("ChalkId", chalk.getChalkId());
                    ChalkVehicleMapViewActivity.this.startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
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
                e.printStackTrace();
            }
        }
        dialog.show();
    }

    public void bindDataAtLoadingTime() {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    chalks = chalkMapViewBLProcessor.getChalkByPlate(ChalkVehicleMapViewActivity.this);
                    dataLoadingHandler.sendEmptyMessage(DATA_SUCCESSFULL);
                } catch (Exception e) {
                    errorHandler.sendEmptyMessage(ERROR_LOAD);
                }
            }
        }).start();
    }

    public void backAction(View view) {
        finish();
    }

    public void mapviewAction(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Action");

        final CharSequence[] choiceList = {"Remove All",                //0
                "View All",                //1
                "View Expired Only",        //2
        };

        builder.setItems(choiceList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (progressDialog.isShowing()) {
                    dialog.dismiss();
                }

                if (which == 0) {
                    dialog.dismiss();
                    removeAllChalks();
                } else if (which == 1) {
                    expiredOnly = false;
                    initOverlayMarkers();
                } else if (which == 2) {
                    expiredOnly = true;
                    initOverlayMarkers();
                }

            }
        })
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void removeAllChalks() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete all chalks?")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            for (ChalkVehicle chalk : chalks) {
                                ChalkVehicle.removeChalkById(chalk.getChalkId(), "");
                            }
                        } catch (Exception ae) {
                            ae.printStackTrace();
                        }

                        setResult(RESULT_OK);
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
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

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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

    private class SwipeGestureDetector extends SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 50;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH) {
                    return false;
                }

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onLeftSwipe();
                }
                // Right swipe
                else if (-diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onRightSwipe();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
