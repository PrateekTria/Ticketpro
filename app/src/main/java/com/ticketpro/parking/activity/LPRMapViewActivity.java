package com.ticketpro.parking.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdate;
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
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.LPRNotify;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.Violation;
import com.ticketpro.parking.R;
import com.ticketpro.util.BitmapDownloaderTask;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

public class LPRMapViewActivity extends FragmentActivity implements
        OnTouchListener, OnMarkerClickListener, LocationListener, OnInfoWindowClickListener, OnMapReadyCallback {

    final int DATA_ERROR = 0;
    final int DATA_SUCCESSFULL = 1;
    final int ERROR_LOAD = 1;
    final int ERROR_SERVICE = 2;
    private List<LPRNotify> lprNotifications;
    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private Handler errorHandler;
    private LocationManager locationManager;
    private GoogleMap googleMap;
    private HashMap<Marker, LPRNotify> lprMarkers;
    private TextView totalTextview;
    private ImageView previewImg;
    private Dialog dialog;

    private GestureDetector gestureDetector;
    private int currentPosition;
    private SupportMapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.lpr_map_view);

            totalTextview = (TextView) findViewById(R.id.lpr_total_textview);
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);
            mapFragment.getMapAsync(this);
            dataLoadingHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (lprNotifications == null)
                        return;

                    initOverlayMarkers();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            };

            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    setUpMapIfNeeded();

                    switch (msg.what) {
                        case ERROR_LOAD:
                            Toast.makeText(getBaseContext(), "Failed to load LPR Notifications. Please try again.", Toast.LENGTH_SHORT).show();
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

            lprMarkers = new HashMap<Marker, LPRNotify>();
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
            Location myLocation = googleMap.getMyLocation();
            if (myLocation != null) {
                onLocationChanged(location);
            } else if (location != null) {
                onLocationChanged(location);
            }

            locationManager.requestLocationUpdates(provider, 20000, 0, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpMapIfNeeded() {
	   /* if (googleMap == null) {
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
            googleMap.setOnMarkerClickListener(LPRMapViewActivity.this);
            googleMap.setOnInfoWindowClickListener(LPRMapViewActivity.this);
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
        lprMarkers.clear();
        LatLng position = null;

        int count = 0;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LPRNotify lprNotify : lprNotifications) {
            if (lprNotify.getPhoto1().isEmpty() || lprNotify.getLatitude().isEmpty() || lprNotify.getLongitude().isEmpty()) {
                continue;
            }

            double lat = Double.parseDouble(lprNotify.getLatitude());
            double lng = Double.parseDouble(lprNotify.getLongitude());
            position = new LatLng(lat, lng);

            MarkerOptions options = new MarkerOptions();
            options.position(position);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_green));
            options.title(lprNotify.getPlate());

            Marker marker = googleMap.addMarker(options);
            lprMarkers.put(marker, lprNotify);
            builder.include(marker.getPosition());
            count++;
        }
        
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 30);
        googleMap.animateCamera(cu);
        if (position != null) {
            locationManager.removeUpdates(this);
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(15));
        }

        totalTextview.setText("Total (" + count + ")");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LPRNotify lprNotify = lprMarkers.get(marker);
        if (lprNotify == null)
            return false;

        currentPosition = getLPRNotifyPosition(lprNotify);
        showInfoWindow(lprNotify);
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        LPRNotify lprNotify = lprMarkers.get(marker);
        if (lprNotify == null)
            return;

        currentPosition = getLPRNotifyPosition(lprNotify);
        showInfoWindow(lprNotify);
    }

    private int getLPRNotifyPosition(LPRNotify lprNotify) {
        int position = 0;
        Iterator<Marker> itr = lprMarkers.keySet().iterator();
        while (itr.hasNext()) {
            position++;
            Marker marker = (Marker) itr.next();
            LPRNotify tempMarker = lprMarkers.get(marker);
            if (tempMarker.getNotificationId() == lprNotify.getNotificationId()) {
                return position;
            }
        }

        return position;
    }


    private LPRNotify getLPRNotifyByIndex(int index) {
        if (index < 0 || index > (lprMarkers.size() - 1))
            return null;

        Object[] keys = lprMarkers.keySet().toArray();
        Marker key = (Marker) keys[index];
        return lprMarkers.get(key);
    }

    public void onLeftSwipe() {
        if (currentPosition < lprNotifications.size()) {
            currentPosition++;
        }

        LPRNotify lprNotify = getLPRNotifyByIndex(currentPosition - 1);
        showInfoWindow(lprNotify);
    }

    public void onRightSwipe() {
        if (currentPosition > 1) {
            currentPosition--;
        }

        LPRNotify lprNotify = getLPRNotifyByIndex(currentPosition - 1);
        showInfoWindow(lprNotify);
    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap = googleMap1;

        if (googleMap != null) {
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.setOnMarkerClickListener(LPRMapViewActivity.this);
            googleMap.setOnInfoWindowClickListener(LPRMapViewActivity.this);
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void showInfoWindow(final LPRNotify lprNotify) {
        if (lprNotify == null) {
            Toast.makeText(this, "Failed to load LPR details.", Toast.LENGTH_LONG).show();
            return;
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new Dialog(this) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };

        dialog.setContentView(R.layout.lpr_map_overlay);
        dialog.setTitle("LPR Notification");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        previewImg = (ImageView) dialog.findViewById(R.id.overlay_lpr_image);

        TextView notifyDate = (TextView) dialog.findViewById(R.id.overlay_lpr_date_textview);
        TextView permit = (TextView) dialog.findViewById(R.id.overlay_lpr_permit_textview);
        TextView status = (TextView) dialog.findViewById(R.id.overlay_lpr_status_textview);
        TextView plate = (TextView) dialog.findViewById(R.id.overlay_lpr_plate_textview);

        notifyDate.setText(StringUtil.getDisplayString(DateUtil.getDateStringFromDate(lprNotify.getNotifyDate())));
        permit.setText(StringUtil.getDisplayString(lprNotify.getPermit()));
        status.setText(StringUtil.getDisplayString(lprNotify.getPermitStatus()));
        plate.setText(StringUtil.getDisplayString(lprNotify.getPlate()));

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

                Ticket ticket = TPApplication.getInstance().createNewTicket();
                ticket.setStateCode(lprNotify.getState());
                ticket.setBodyCode(lprNotify.getBody());
                ticket.setPlate(lprNotify.getPlate());
                ticket.setMakeCode(lprNotify.getMake());
                ticket.setColorCode(lprNotify.getColor());
                ticket.setPermit(lprNotify.getPermit());
                ticket.setLocation(lprNotify.getLocation());
                ticket.setLatitude(lprNotify.getLatitude());
                ticket.setLongitude(lprNotify.getLongitude());

                if (lprNotify.getViolationCode() != null) {
                    try {
                        TicketViolation ticketViolation = new TicketViolation();
                        ticketViolation.setTicketId(ticket.getTicketId());
                        ticketViolation.setCitationNumber(ticket.getCitationNumber());
                        ticketViolation.setViolationCode(lprNotify.getViolationCode());
                        ticketViolation.setViolationDesc(lprNotify.getViolationDesc());
                        ticketViolation.setViolationId(Integer.parseInt(lprNotify.getViolationId()));

                        Violation violation = Violation.getViolationById(Integer.valueOf(lprNotify.getViolationId()));
                        if (violation != null) {
                            ticketViolation.setFine(violation.getBaseFine());
                        }

                        ticket.getTicketViolations().add(ticketViolation);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (lprNotify.getPhoto1().length() > 0) {
                    String image1Path = TPUtility.getTicketsFolder() + lprNotify.getPhoto1();
                    Bitmap bitmap = previewImg.getDrawingCache();
                    try {
                        bitmap.compress(CompressFormat.JPEG, 90, new FileOutputStream(image1Path));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    TicketPicture picture = new TicketPicture();
                    picture.setCitationNumber(ticket.getCitationNumber());
                    picture.setTicketId(ticket.getTicketId());
                    picture.setImagePath(image1Path);
                    picture.setCustId(ticket.getCustId());
                    //picture.setMarkPrint("Y");
                    picture.setMarkPrint("N");

                    ticket.getTicketPictures().add(picture);
                }

                if (lprNotify.getPhoto2().length() > 0) {
                    String image2Path = TPUtility.getTicketsFolder() + lprNotify.getPhoto2();
                    Bitmap bitmap = previewImg.getDrawingCache();
                    try {
                        bitmap.compress(CompressFormat.JPEG, 90, new FileOutputStream(image2Path));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    TicketPicture picture = new TicketPicture();
                    picture.setCitationNumber(ticket.getCitationNumber());
                    picture.setTicketId(ticket.getTicketId());
                    picture.setImagePath(image2Path);
                    picture.setCustId(ticket.getCustId());
                    //picture.setMarkPrint("Y");
                    picture.setMarkPrint("N");

                    ticket.getTicketPictures().add(picture);
                }

                TPApplication.getInstance().setActiveTicket(ticket);
                Intent i = new Intent();
                i.setClass(getApplicationContext(), WriteTicketActivity.class);
                startActivity(i);
            }
        });

        if (!lprNotify.getPhoto1().isEmpty()) {
            Glide.with(this)
                    .load(getCustomerImagesURL(lprNotify.getPhoto1()))
                    .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                    .into(previewImg);
            //lazyLoadImage(this.getCustomerImagesURL(lprNotify.getPhoto1()), lprNotify.getPhoto1(), previewImg);

        } else if (!lprNotify.getPhoto2().isEmpty()) {
            Glide.with(this)
                    .load(getCustomerImagesURL(lprNotify.getPhoto1()))
                    .into(previewImg);
            //lazyLoadImage(this.getCustomerImagesURL(lprNotify.getPhoto2()), lprNotify.getPhoto2(), previewImg);
        }

        dialog.show();
    }

    public String getCustomerImagesURL(String filename) {
        CustomerInfo customerInfo = TPApplication.getInstance().getCustomerInfo();
        String contentFolder = customerInfo.getContentFolder();
        if (contentFolder == null || contentFolder.equals(""))
            contentFolder = customerInfo.getCustId() + "";

        return TPConstant.IMAGES_URL + "/" + contentFolder + "/" + filename;
    }

    public void lazyLoadImage(String url, String photoName, ImageView imageView) {
        File imgFile = new File(TPUtility.getLPRImagesFolder() + photoName);
        if (imgFile.exists()) {
            imageView.setImageURI(Uri.fromFile(imgFile));
            return;
        }

        BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
        task.execute(url, photoName);
    }

    public void bindDataAtLoadingTime() {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lprNotifications = LPRNotify.getLPRNotifications();
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

        final CharSequence[] choiceList = {"Remove All"};

        builder.setItems(choiceList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (progressDialog.isShowing())
                    dialog.dismiss();

                if (which == 0) {
                    dialog.dismiss();
                    removeAllNotifications();
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

    private void removeAllNotifications() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete all LPR Notifications?")
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
                            LPRNotify.removeAllNotifications();
                        } catch (Exception ae) {
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

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

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
