package com.ticketpro.parking.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.ticketpro.model.Address;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.Duration;
import com.ticketpro.model.GPSLocation;
import com.ticketpro.model.SyncData;
import com.ticketpro.parking.R;
import com.ticketpro.parking.api.ChalkVehicleNetworkCalls;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.parking.service.AlarmReceiver;
import com.ticketpro.parking.service.GPSResultReceiver.Receiver;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.GPSTracker;
import com.ticketpro.util.Preview;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class PhotosChalkChalkingActivity extends BaseActivityImpl {

    final int DATA_ERROR = 0;
    final int DATA_SUCCESSFULL = 1;
    final int ERROR_LOAD = 1;
    final int ERROR_SERVICE = 2;
    private Uri picUri;
    private ChalkVehicle activeChalk;
    private TextView totalChalked;
    private TextView locationTextView;
    private TextView tmTextView;
    private Address address;
    private int durationId;
    private String tire;
    private int cX;
    private int cY;
    private int sX;
    private int sY;
    private ProgressDialog progressDialog;
    private Handler handler;
    private int totalChalkCount = 0;
    private GPSTracker gpsTracker;
    private Preview cView;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        cView = new Preview(this);
        setContentView(cView);
        setActiveScreen(this);
        isNetworkInfoRequired = true;

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.photo_chalk_chalking, null, false);
        addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        setLogger(PhotosChalkChalkingActivity.class.getName());
        setBLProcessor(new ChalkBLProcessor((TPApplication) getApplicationContext()));

        locationTextView = findViewById(R.id.photo_chalk_location_textview);
        tmTextView = findViewById(R.id.photo_chalk_time_textview);
        tmTextView.setText(DateUtil.getCurrentTime());
        totalChalked = findViewById(R.id.photo_chalk_total_textview);

        address = TPUtility.createEmptyAddress();
        activeChalk = TPApp.getActiveChalk();

        // Check Address
        Intent data = getIntent();
        if (data.hasExtra("Location")) {
            address.setLocation(data.getStringExtra("Location"));
            address.setStreetNumber(data.getStringExtra("StreetNumber"));
            address.setStreetPrefix(data.getStringExtra("StreetPrefix"));
            address.setStreetSuffix(data.getStringExtra("StreetSuffix"));
            address.setDirection(data.getStringExtra("Direction"));
        } else {
            address.setLocation(activeChalk.getLocation());
            address.setStreetNumber(activeChalk.getStreetNumber());
            address.setStreetPrefix(activeChalk.getStreetPrefix());
            address.setStreetSuffix(activeChalk.getStreetSuffix());
            address.setDirection(activeChalk.getDirection());
        }

        locationTextView.setText(TPUtility.getFullAddress(address));
        durationId = activeChalk.getDurationId();
        tire = activeChalk.getTire();
        cX = activeChalk.getChalkx();
        cY = activeChalk.getChalky();
        sX = activeChalk.getStemx();
        sY = activeChalk.getStemy();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {
                    //cView.camera.stopPreview();

                    // Update GPS Location
                    if (gpsTracker != null) {
                        gpsTracker.initService(new Receiver() {
                            @Override
                            public void onReceiveResult(GPSLocation location, Bundle resultData) {
                                if (location != null && (!location.getLocation().equals("") || !location.getStreetNumber().equals(""))) {
                                    address.setStreetNumber(location.getStreetNumber().toUpperCase());
                                    address.setLocation(location.getLocation().toUpperCase());
                                    address.setStreetPrefix("");
                                    address.setStreetSuffix("");
                                    address.setDirection("");
                                }
                            }

                            @Override
                            public void onReceiveResult(Location location, Bundle resultData) {
                                activeChalk.setLatitude(location.getLatitude() + "");
                                activeChalk.setLongitude(location.getLongitude() + "");
                            }

                            @Override
                            public void onTimeout() {
                                // TODO Auto-generated method stub

                            }
                        });
                    }

                    try {
                        // Location Notification
                        int durationMins = Duration.getDurationMinsById(durationId, PhotosChalkChalkingActivity.this);
                        long currentTime = System.currentTimeMillis();
                        long expirationTime = currentTime + (durationMins * 60 * 1000);

                        activeChalk.setExpiration(TPUtility.addMinutesToDate(durationMins, activeChalk.getChalkDate()));
                        activeChalk.setChalkDate(new Date());
                        activeChalk.setCustId(TPApp.custId);
                        activeChalk.setSyncStatus("P");

                        ChalkVehicle.insertChalkVehicle(activeChalk).subscribe();

                       // int chalkPicId = ChalkPicture.getNextPrimaryId();
                        ChalkPicture chalkPicture = new ChalkPicture();
                        //chalkPicture.setPictureId(chalkPicId);
                        chalkPicture.setChalkDate(new Date());
                        chalkPicture.setImagePath(picUri.getPath());
                        chalkPicture.setImageResolution("640x480");
                        chalkPicture.setChalkId(activeChalk.getChalkId());
                        chalkPicture.setSyncStatus("Pending");
                        chalkPicture.setLocation(TPUtility.getFullAddress(address));
                        chalkPicture.setCustId(TPApp.custId);

                        // Update Current Location
                        try {
                            if (gpsTracker != null) {
                                GPSLocation gpsLocation = gpsTracker.getLastGPSLocation();
                                if (gpsLocation != null) {
                                    if (isEmptyString(gpsLocation.getLocation()) && isEmptyString(gpsLocation.getStreetNumber())) {
                                        address.setLocation(gpsLocation.getLocation());
                                        address.setStreetNumber(gpsLocation.getStreetNumber());
                                    }
                                    chalkPicture.setLocation(TPUtility.getFullAddress(address));
                                    chalkPicture.setLatitude(gpsLocation.getLatitude());
                                    chalkPicture.setLongitude(gpsLocation.getLongitude());
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TPConstant.TAG, TPUtility.getPrintStackTrace(e));
                        }
                        ChalkPicture.insertChalkPicture(chalkPicture).subscribe();
                        //DatabaseHelper.getInstance().insertOrReplace(chalkPicture.getContentValues(), "chalk_pictures");
                        ArrayList<SyncData> syncList = new ArrayList<>();

                        // Update Sync Status.....
                        SyncData syncData = new SyncData();
                        syncData.setActivity("INSERT");
                        syncData.setPrimaryKey(activeChalk.getChalkId() + "");
                        syncData.setActivityDate(new Date());
                        syncData.setCustId(TPApp.custId);
                        syncData.setTableName(TPConstant.TABLE_CHALKS);
                        syncData.setStatus("Pending");
                        SyncData.insertSyncData(syncData).subscribe();
                        //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                        syncList.add(syncData);

                        SyncData syncPicture = new SyncData();
                        syncPicture.setActivity("INSERT");
                        syncPicture.setPrimaryKey(chalkPicture.getPictureId() + "");
                        syncPicture.setActivityDate(new Date());
                        syncPicture.setCustId(TPApp.custId);
                        syncPicture.setTableName(TPConstant.TABLE_CHALK_PICTURES);
                        syncPicture.setStatus("Pending");
                        SyncData.insertSyncData(syncPicture).subscribe();
                        syncList.add(syncData);

                        Intent notificationIntent = new Intent(PhotosChalkChalkingActivity.this, AlarmReceiver.class);
                        notificationIntent.putExtra("Title", "Chalk Expiration");
                        notificationIntent.putExtra("Message", "Photo chalk has expired");
                        notificationIntent.putExtra("ChalkId", activeChalk.getChalkId());

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(PhotosChalkChalkingActivity.this, TPApplication.notificationId + 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, expirationTime, pendingIntent);

                        if (isServiceAvailable) {
                            ChalkVehicleNetworkCalls.saveChalk(syncList);
                            /*ChalkBLProcessor blProcessor = new ChalkBLProcessor((TPApplication) getApplicationContext());
                            blProcessor.syncData(syncList, PhotosChalkChalkingActivity.this);*/
                        }

                        activeChalk = TPApp.createNewChalk();
                        TPApp.setActiveChalk(activeChalk);
                        activeChalk.setDurationId(durationId);
                        activeChalk.setTire(tire);
                        activeChalk.setChalkx(cX);
                        activeChalk.setChalkx(cY);
                        activeChalk.setStemx(sX);
                        activeChalk.setStemy(sY);
                        activeChalk.setChalkType(TPConstant.CHALK_TYPE_PHOTO);

                        if (gpsTracker != null && gpsTracker.getLastLocation() != null) {
                            activeChalk.setLongitude(gpsTracker.getLastLocation().getLongitude() + "");
                            activeChalk.setLatitude(gpsTracker.getLastLocation().getLatitude() + "");
                            activeChalk.setLocation(address.getLocation());
                            activeChalk.setStreetNumber(address.getStreetNumber());
                            activeChalk.setIsGPSLocation("Y");
                        } else {
                            activeChalk.setLocation(address.getLocation());
                            activeChalk.setStreetNumber(address.getStreetNumber());
                            activeChalk.setStreetPrefix(address.getStreetPrefix());
                            activeChalk.setStreetSuffix(address.getStreetSuffix());
                            activeChalk.setDirection(address.getDirection());
                            activeChalk.setIsGPSLocation("N");
                            activeChalk.setLongitude("");
                            activeChalk.setLatitude("");
                        }
                        DeviceInfo device = TPApp.getDeviceInfo();
                        long photoNumber = device.getCurrentPhotoNumber() + 1;
                        device.setCurrentPhotoNumber(photoNumber);
                        DeviceInfo.insertDeviceInfo(device);
                        //DatabaseHelper.getInstance().insertOrReplace(device.getContentValues(), "devices");

                        totalChalkCount++;
                        totalChalked.setText("Chalked(" + totalChalkCount + ")");
                        cView.camera.startPreview();
                        cView.registerPreviewCallback();
                    } catch (Exception e) {
                        Log.e(TPConstant.TAG, TPUtility.getPrintStackTrace(e));
                    }
                }

                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }

        };
        bindDataAtLoadingTime();
    }

    @Override
    protected void onResume() {
        if (totalChalked != null) {
            totalChalked.setText("Chalked(" + totalChalkCount + ")");
        }
        super.onResume();
    }

    public void bindDataAtLoadingTime() {
        gpsTracker = new GPSTracker(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        backAction(null);
    }

    public void backAction(View view) {
        finish();
    }

    public void chalkAction(View view) {
        long photoNumber = TPApp.getDeviceInfo().getCurrentPhotoNumber() + 1;
        File file = new File(TPUtility.getChalksFolder(), "CHALK-" + photoNumber + ".JPG");
        try {
            file.createNewFile();
            picUri = Uri.fromFile(file);

            cView.playSoundEffect(SoundEffectConstants.CLICK);
            if (cView.previewBitmapData == null)
                return;

            final byte[] data = cView.previewBitmapData.clone();
            cView.camera.stopPreview();

            progressDialog = ProgressDialog.show(this, "", "Processing Image...");
            runOnUiThread(() -> {
                Camera.Parameters parameters = cView.camera.getParameters();
                Size size = parameters.getPreviewSize();

                YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                ByteArrayOutputStream outstr = new ByteArrayOutputStream();
                Rect rect = new Rect(0, 0, size.width, size.height);
                yuvimage.compressToJpeg(rect, 100, outstr);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                options.inPurgeable = true;
                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(outstr.toByteArray(), 0, outstr.size(), options);
                    if (size.width > size.height) {
                        Matrix mat = new Matrix();
                        mat.postRotate(90);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
                    }
                    FileOutputStream fos = new FileOutputStream(picUri.getPath());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
                handler.sendEmptyMessage(1);
            });

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void viewListAction(View view) {
        Intent i = new Intent();
        i.setClass(this, PhotosChalkListActivity.class);
        startActivity(i);
    }


    @Override
    public void handleVoiceInput(String text) {
        // TODO Auto-generated method stub
    }


    @Override
    public void handleVoiceMode(boolean voiceMode) {
        // TODO Auto-generated method stub
    }


    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        // TODO Auto-generated method stub
    }

}
