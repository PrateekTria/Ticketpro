package com.ticketpro.parking.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.Feature;
import com.ticketpro.model.SpecialActivityPicture;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketPictureTemp;
import com.ticketpro.model.TicketTemp;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.parking.bl.PhotosBLProcessor;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.CSVUtility;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.Preview;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;
import com.ticketpro.util.TouchImageView;
import com.ticketpro.util.VerticalSeekBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.schedulers.Schedulers;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

enum FlashLightMode {
    ON, OFF, AUTO
}

public class TakePictureActivity extends BaseActivityImpl {

    private final int DONE_BTN = 0;
    private final int RETAKE_BTN = 1;
    private final int TAKE_BTN = 2;
    long photoNumber = 0;
    long photoNum = 0;
    private Button doneBtn;
    private Button retakeBtn;
    private Button takeBtn;
    private Uri picUri;
    private long citationNumber;
    private int pictureIndex;
    private boolean isChalkPicture = false;
    private boolean editTicketPictures = false;
    private boolean isNewChalkPicture = false;
    private boolean isGenericPicture = false;
    private String imageResolution;
    private String imageName;
    private long chalkId;
    private Preview cView;
    private ProgressDialog progressDialog;
    private Handler handler;
    private Bitmap bitmap;
    private ImageView flashlightLEDImage;
    private ImageView flashlightModeImage;
    private boolean flashlightLED = false;
    private FlashLightMode flashlightMode = FlashLightMode.AUTO;
    private VerticalSeekBar verticalSeekBar;
    private CheckBox stickyZoom;
    private int maxZoomLevel = 0;
    private int savedzoomLevel = 0;
    private SharedPreferences mPreferences;
    private TouchImageView imageView;
    private View inputDlgView;
    private Dialog imageDialog;
    private boolean isSelfi;
    private boolean recaptureImage = false;
    private long ticketID;
    private String imageChalkName;
    private String chalPictureName;
    private String SPAPictureName;
    private boolean isEditChalkPicture = false;
    private boolean isPhotoChalkEditScreen;
    private boolean isSepecialActivity;
    private boolean isEditSepecialActivity;
    private String imageSPAName;
    private int ReportId;
    private int pictureId;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent data = getIntent();
        isSelfi = data.getBooleanExtra("isSelfi", false);
        // Check if take picture is allowed on not.
        if (isSelfi) {
            try {
                if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                    Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                if ((!Feature.isFeatureAllowed(Feature.TAKE_PICTURES) || TPUtility.isRunningOnEmulator(getApplicationContext()))) {
                    Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isSelfi) {
            cView = new Preview(TakePictureActivity.this, true);
        } else {
            cView = new Preview(this);
        }
        setContentView(cView);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.take_picture, null, false);
        addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        setBLProcessor(new PhotosBLProcessor());
        setLogger(TakePictureActivity.class.getName());
        setActiveScreen(this);
        isNetworkInfoRequired = true;

        initUi();
        flashlightLEDImage.setOnClickListener(v -> {
            try {
                cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);
                if (!flashlightLED) {
                    flashlightLEDImage.setImageResource(R.drawable.flashlight_disable);
                    flashlightLED = true;
                    cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_TORCH);
                } else {
                    flashlightLEDImage.setImageResource(R.drawable.flashlight);
                    flashlightLED = false;
                    cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);
                }

                TPApplication.getInstance().pictureFlashLED = flashlightLED;

            } catch (Exception e) {
                Log.e("TakePicture", "Error :" + TPUtility.getPrintStackTrace(e));
            }
        });


        flashlightModeImage.setOnClickListener(v -> {
            try {
                if (flashlightMode == FlashLightMode.AUTO) {
                    flashlightMode = FlashLightMode.OFF;
                    flashlightModeImage.setImageResource(R.drawable.flashlight_off);
                    cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);

                } else if (flashlightMode == FlashLightMode.OFF) {
                    flashlightMode = FlashLightMode.ON;
                    flashlightModeImage.setImageResource(R.drawable.flashlight_on);
                    cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_ON);

                } else {
                    flashlightMode = FlashLightMode.AUTO;
                    flashlightModeImage.setImageResource(R.drawable.flashlight_auto);
                    cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_AUTO);
                }

            } catch (Exception e) {
                Log.e("TakePicture", "Error :" + TPUtility.getPrintStackTrace(e));
            }
        });

        pictureIndex = data.getIntExtra("PictureIndex", -1);
        chalPictureName = data.getStringExtra("PictureName");
        chalkId = data.getLongExtra("ChalkId", 0);
        citationNumber = data.getLongExtra("CitationNumber", 0);
        isChalkPicture = data.getBooleanExtra("ChalkPicture", false);
        isEditChalkPicture = data.getBooleanExtra("EditChalkPictures", false);
        isPhotoChalkEditScreen = data.getBooleanExtra("isPhotoChalkEditScreen", false);
        isNewChalkPicture = data.getBooleanExtra("NewChalkPicture", false);
        editTicketPictures = data.getBooleanExtra("EditTicketPictures", false);
        try {
            ticketID = data.getLongExtra("ticketID", TPApp.getActiveTicket().getTicketId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        isSepecialActivity = data.getBooleanExtra("NewSpecialPicture", false);
        isEditSepecialActivity = data.getBooleanExtra("EditPictureSPA", false);
        SPAPictureName = data.getStringExtra("PictureName");

        isGenericPicture = data.getBooleanExtra("GenericPicture", false);
        imageName = data.getStringExtra("ImageName");
        ReportId = data.getIntExtra("ReportId", 0);
        pictureId = data.getIntExtra("PictureId", 0);

        recaptureImage = data.getBooleanExtra(TPConstant.INTENT_RECAPTURE_CONSTANT, false);
        if (recaptureImage) {
            String photoNumber = data.getStringExtra("photoNumber");
            try {
                photoNum = Long.parseLong(photoNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!isGenericPicture) {
            TextView tv = (TextView) findViewById(R.id.take_picture_citation_number_textview);
            if (citationNumber > 0) {
                tv.setText("#" + TPUtility.prefixZeros(citationNumber, 8));
            } else {
                tv.setText("#" + TPUtility.prefixZeros(chalkId, 8));
            }
        }

        stickyZoom.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isSelfi)
                return;
            int progress = verticalSeekBar.getProgress();
            if (isChecked) {
                updateProgress(progress);
            } else {
                updateProgress(0);
            }
        });

        verticalSeekBar.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    if (isSelfi)
                        return;
                    Camera camera = cView.camera;
                    if (camera.getParameters().isZoomSupported()) {
                        maxZoomLevel = camera.getParameters().getMaxZoom();
                        verticalSeekBar.setMax(maxZoomLevel);
                        if (progress <= maxZoomLevel) {
                            Camera.Parameters p = camera.getParameters();
                            p.setZoom(progress);
                            camera.setParameters(p);
                            if (stickyZoom.isChecked()) {
                                updateProgress(progress);
                            } else {
                                updateProgress(0);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (msg.what == 1) {
                    if (cView.camera != null) {
                        cView.camera.stopPreview();
                    }

                    retakeBtn.setEnabled(true);
                    doneBtn.setEnabled(true);
                    takeBtn.setEnabled(true);

                    retakeBtn.setVisibility(View.VISIBLE);
                    doneBtn.setVisibility(View.VISIBLE);
                    takeBtn.setVisibility(View.INVISIBLE);

                    try {
                        imageView.setVisibility(View.VISIBLE);
                        Bitmap imgBitMap = BitmapFactory.decodeFile(picUri.getPath());
                        imageView.setImageBitmap(imgBitMap);
                        verticalSeekBar.setVisibility(View.GONE);
                        stickyZoom.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }

    private void initUi() {
        try {
            doneBtn = (Button) findViewById(R.id.take_picture_done_btn);
            doneBtn.setOnClickListener(this);
            doneBtn.setTag(DONE_BTN);

            retakeBtn = (Button) findViewById(R.id.take_picture_retake_btn);
            retakeBtn.setOnClickListener(this);
            retakeBtn.setTag(RETAKE_BTN);

            takeBtn = (Button) findViewById(R.id.take_picture_take_btn);
            takeBtn.setOnClickListener(this);
            takeBtn.setTag(TAKE_BTN);

            verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
            stickyZoom = (CheckBox) findViewById(R.id.stickyZoom);

            flashlightLEDImage = (ImageView) findViewById(R.id.flashlight_led_imgview);
            flashlightModeImage = (ImageView) findViewById(R.id.flashlight_mode_imgview);

            imageView = findViewById(R.id.touchImageView);
            imageView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProgress(int progress) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(TPConstant.PREFS_KEY_SETTING_STICKY_ZOOM, progress);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cView == null) {
            return;
        }

        // Turn on FlashLED if it was turned on earlier
        flashlightLED = TPApp.pictureFlashLED;
        if (flashlightLED) {
            flashlightLEDImage.setImageResource(R.drawable.flashlight_disable);
            cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_TORCH);
        } else {
            flashlightLEDImage.setImageResource(R.drawable.flashlight);
            cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);
        }

        if (flashlightMode == FlashLightMode.AUTO) {
            flashlightMode = FlashLightMode.OFF;
            flashlightModeImage.setImageResource(R.drawable.flashlight_off);
            cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);
        } else if (flashlightMode == FlashLightMode.OFF) {
            flashlightMode = FlashLightMode.ON;
            flashlightModeImage.setImageResource(R.drawable.flashlight_on);
            cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_ON);
        } else {
            flashlightMode = FlashLightMode.AUTO;
            flashlightModeImage.setImageResource(R.drawable.flashlight_auto);
            cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_AUTO);
        }

        try {
            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            savedzoomLevel = mPreferences.getInt(TPConstant.PREFS_KEY_SETTING_STICKY_ZOOM, 0);
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (isSelfi)
                            return;
                        Camera camera = cView.camera;
                        if (camera.getParameters().isZoomSupported()) {
                            maxZoomLevel = camera.getParameters().getMaxZoom();
                            verticalSeekBar.setMax(maxZoomLevel);

                            if (savedzoomLevel != 0) {
                                if (savedzoomLevel <= maxZoomLevel) {
                                    Camera.Parameters p = camera.getParameters();
                                    p.setZoom(savedzoomLevel);
                                    camera.setParameters(p);
                                }

                                verticalSeekBar.setProgress(savedzoomLevel);
                                verticalSeekBar.updateThumb();
                                stickyZoom.setChecked(true);
                            }
                        }
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            }, 100);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {

    }

    @Override
    public void onBackPressed() {
        backAction(null);
    }

    public void backAction(View view) {
        try {
            if (cView != null) {
                if (cView.camera != null) {
                    cView.camera.stopPreview();
                }

                cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Clear Bitmap
        if (bitmap != null) {
            bitmap.recycle();
            System.gc();
        }

        Intent data = new Intent();
        if (pictureIndex >= 0 && picUri != null) {
            data.putExtra("PicturePath", picUri.getPath());
            data.putExtra("PictureIndex", pictureIndex);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    private void acceptPicture() {
        Intent data = new Intent();

        if (isGenericPicture) {
            if (picUri != null) {
                data.putExtra("PicturePath", picUri.getPath());
            }

            if (pictureIndex >= 0) {
                data.putExtra("PictureIndex", pictureIndex);
            }

            if (imageResolution != null) {
                data.putExtra("ImageResolution", imageResolution);
            }

        } else if (isSepecialActivity) {
            SpecialActivityPicture picture = new SpecialActivityPicture();
            picture.setPictureId(SpecialActivityPicture.getNextPrimaryId());
            picture.setCustid(TPApp.custId);
           // picture.setPictureId(TPApp.pic);
            picture.setImagePath(picUri.getPath());
            picture.setImageResulation(imageResolution);
            picture.setPictureDate(DateUtil.getCurrentDate());
            picture.setImageSize(TPUtility.getImageSize(picUri.getPath()));
            picture.setImageName(imageSPAName);
            picture.setReportId(ReportId);

            try {
                ParkingDatabase.getInstance(TPApplication.getInstance()).specialActivityPictureDao().insertSpecialActivityPicture(picture).subscribeOn(Schedulers.io()).subscribe();
                //DatabaseHelper.getInstance().openWritableDatabase();
                //DatabaseHelper.getInstance().insertOrReplace(picture.getContentValues(), "Special_Actvities_Pictures");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (picUri != null) {
                data.putExtra("PicturePath", picUri.getPath());
            }
        } else if (isEditSepecialActivity) {
            SpecialActivityPicture picture = new SpecialActivityPicture();
            picture.setCustid(TPApp.custId);
            picture.setImagePath(picUri.getPath());
            picture.setImageResulation(imageResolution);
            picture.setPictureDate(DateUtil.getCurrentDate());
            picture.setImageSize(TPUtility.getImageSize(picUri.getPath()));
            picture.setImageName(imageSPAName);
            picture.setReportId(ReportId);
            try {
                SpecialActivityPicture.updatePicture(pictureId, picture);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (picUri != null) {
                data.putExtra("PicturePath", picUri.getPath());
            }
        } else {
            if (chalkId > 0) {
                ChalkPicture chalkPicture = new ChalkPicture();
                //chalkPicture.setPictureId(ChalkPicture.getNextPrimaryId());
                chalkPicture.setChalkDate(new Date());
                chalkPicture.setChalkId(chalkId);
                //String[] imagePath = Objects.requireNonNull(picUri.getPath()).split("/");
                chalkPicture.setImagePath(picUri.getPath());
                chalkPicture.setImgName(imageChalkName);
                chalkPicture.setImageResolution(imageResolution);
                chalkPicture.setSyncStatus("Pending");
                chalkPicture.setLocation("NA");
                chalkPicture.setCustId(TPApp.custId);
                chalkPicture.setImageSize(TPUtility.getImageSize(picUri.getPath()));

                if (isChalkPicture) {
                    try {
                        /*DatabaseHelper.getInstance().openWritableDatabase();
                        DatabaseHelper.getInstance().insertOrReplace(chalkPicture.getContentValues(), "chalk_pictures");*/
                        ChalkPicture.insertChalkPicture(chalkPicture).subscribe();
                        ArrayList<SyncData> syncList = new ArrayList<SyncData>();
                        SyncData syncPicture = new SyncData();
                        syncPicture.setActivity("INSERT");
                        syncPicture.setPrimaryKey(chalkPicture.getPictureId() + "");
                        syncPicture.setActivityDate(new Date());
                        syncPicture.setCustId(TPApp.custId);
                        syncPicture.setTableName(TPConstant.TABLE_CHALK_PICTURES);
                        syncPicture.setStatus("Pending");
                        SyncData.insertSyncData(syncPicture).subscribe();
                        //DatabaseHelper.getInstance().insertOrReplace(syncPicture.getContentValues(), "sync_data");
                        syncList.add(syncPicture);
                        //DatabaseHelper.getInstance().closeWritableDb();

                        if (isFastConnection) {
                            ChalkBLProcessor blProcessor = new ChalkBLProcessor((TPApplication) getApplicationContext());
                            blProcessor.syncChalkPicture(syncList, TakePictureActivity.this);
                        }
                    } catch (Exception e) {
                        Log.e("TakePicture", TPUtility.getPrintStackTrace(e));
                    }
                } else if (isNewChalkPicture) {
                    if (isEditChalkPicture) {
                        TPApp.getActiveChalk().getChalkPictures().set(pictureIndex, chalkPicture);
                        if (picUri != null) {
                            data.putExtra("PicturePath", picUri.getPath());
                        }

                        if (pictureIndex >= 0) {
                            data.putExtra("PictureIndex", pictureIndex);
                        }
                    } else {
                        if (isPhotoChalkEditScreen) {

                            TPApp.getActiveChalk().getChalkPictures().add(chalkPicture);
                            if (picUri != null) {
                                data.putExtra("PicturePath", picUri.getPath());
                            }

                            if (pictureIndex >= 0) {
                                data.putExtra("PictureIndex", pictureIndex);
                            }

                        } else {
                            TPApp.getActiveChalk().getChalkPictures().add(chalkPicture);

                        }
                    }
                }

            } else {
                Ticket activeTicket = TPApp.getActiveTicket();
                if (activeTicket != null) {
                    //upload to customer folder if selfi
                    if (isSelfi) {
                        //saveAndSyncPicture(activeTicket, isSelfi);

                        TicketPicture picture = new TicketPicture();
                        picture.setPictureDate(new Date());
                        picture.setCitationNumber(citationNumber);
                        picture.setMarkPrint("N");
                        //String[] imagePath = Objects.requireNonNull(picUri.getPath()).split("/");
                        picture.setImagePath(picUri.getPath());
                        picture.setImageResolution(imageResolution);
                        picture.setTicketId(activeTicket.getTicketId());
                        picture.setImageSize(TPUtility.getImageSize(picUri.getPath()));
                        picture.setCustId(TPApp.custId);
                       activeTicket.getTicketPictures().add(picture);
                        if (isSelfi) {
                            picture.setIsSelfi("Y");
                        } else {
                            picture.setIsSelfi("N");
                        }

                        //if (editTicketPictures) {
                        try {
            /*DatabaseHelper.getInstance().openWritableDatabase();
            DatabaseHelper.getInstance().insertOrReplace(picture.getContentValues(), "ticket_pictures");*/
                       //    TicketPicture.insertTicketPicture(picture);

                            boolean result = false;
                            if (isFastConnection) {
                                result = ((PhotosBLProcessor) screenBLProcessor).updateSelfiPicture(citationNumber, picture, TakePictureActivity.this);
                            } else {
                                SyncData syncData = new SyncData();
                                syncData.setActivity("INSERT");
                                syncData.setPrimaryKey(picture.getPictureId() + "");
                                syncData.setActivityDate(new Date());
                                syncData.setCustId(TPApp.custId);
                                syncData.setTableName(TPConstant.TABLE_TICKET_PICTURES);
                                syncData.setStatus("Pending");
                                SyncData.insertSyncData(syncData).subscribe();
                                //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                            }

                            //DatabaseHelper.getInstance().closeWritableDb();
                        } catch (Exception e) {
                            Log.e("TakePicture", "Error update ticket picture records " + e.getMessage());
                        }
                        // Clear Bitmap
                        if (bitmap != null) {
                            bitmap.recycle();
                            System.gc();
                        }
                        setResult(RESULT_OK);
                        finish();
                        return;

                    }
                    if (pictureIndex < 0) {
                        TicketPicture picture = new TicketPicture();
                        TicketPictureTemp temp = new TicketPictureTemp();
                        picture.setPictureDate(new Date());
                        temp.setPictureDate(new Date());
                        picture.setCitationNumber(citationNumber);
                        temp.setCitationNumber(citationNumber);
                        picture.setMarkPrint("N");
                        temp.setMarkPrint("N");
                        //String[] imagePath = Objects.requireNonNull(picUri.getPath()).split("/");
                        picture.setImagePath(picUri.getPath());
                        temp.setImagePath(picUri.getPath());

                        picture.setImageResolution(imageResolution);
                        temp.setImageResolution(imageResolution);


                        try {
                            if (editTicketPictures) {
                                picture.setTicketId(ticketID);
                                temp.setTicketId(ticketID);
                            } else {
                                picture.setTicketId(activeTicket.getTicketId());
                                temp.setTicketId(activeTicket.getTicketId());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        picture.setImageSize(TPUtility.getImageSize(picUri.getPath()));
                        temp.setImageSize(TPUtility.getImageSize(picUri.getPath()));
                        picture.setCustId(TPApp.custId);
                        temp.setCustId(TPApp.custId);

                        if (editTicketPictures) {
                            try {
                                ArrayList<TicketPicture> ticketPictures = new ArrayList<>();
                                ticketPictures.add(picture);
                                CSVUtility.writePictureCSV(ticketPictures);

                               /* DatabaseHelper.getInstance().openWritableDatabase();
                                DatabaseHelper.getInstance().insertOrReplace(picture.getContentValues(), "ticket_pictures");*/

                                if (Ticket.isTicketPending(citationNumber)){
                                    picture.setSyncStatus("null");

                                }else {
                                    picture.setSyncStatus("P");
                                }
                                TicketPicture.insertTicketPicture(picture);
                                Thread.sleep(200);
                                boolean result = false;
                               /* if (isFastConnection) {
                                    result = ((PhotosBLProcessor) screenBLProcessor).updateTicketPicture(citationNumber, picture);
                                }

                                if (!result) {
                                    SyncData syncData = new SyncData();
                                    syncData.setActivity("INSERT");
                                    syncData.setPrimaryKey(picture.getPictureId() + "");
                                    syncData.setActivityDate(new Date());
                                    syncData.setCustId(TPApp.custId);
                                    syncData.setTableName(TPConstant.TABLE_TICKET_PICTURES);
                                    syncData.setStatus("Pending");
                                    SyncData.insertSyncData(syncData).subscribe();
                                    //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                                }
*/
                                // This code is chnged by mohit 10/4/2023

                                if (isFastConnection) {

                                    TicketPicture pictureP = TicketPicture.getTicketPictureByPStatus(citationNumber, "P");
                                    result = ((PhotosBLProcessor) screenBLProcessor).updateTicketPicture(citationNumber, pictureP);
                                }

                            } catch (Exception e) {

                                Log.e("TakePicture", "Error update ticket picture records " + e.getMessage());
                            }

                        } else {
                            activeTicket.getTicketPictures().add(picture);
                            if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                                TicketPictureTemp.insertTicketPictureTemp(temp);
                            }

                        }
                    }
                }

                if (picUri != null) {
                    data.putExtra("PicturePath", picUri.getPath());
                }

                if (pictureIndex >= 0) {
                    data.putExtra("PictureIndex", pictureIndex);
                }
            }

            try {
                if (!recaptureImage) {
                    DeviceInfo device = TPApp.getDeviceInfo();
                    //photoNumber = device.getCurrentPhotoNumber() + 1;
                    device.setCurrentPhotoNumber(photoNumber);

                    DeviceInfo.insertDeviceInfo(device);
                    /*DatabaseHelper.getInstance().openWritableDatabase();
                    DatabaseHelper.getInstance().insertOrReplace(device.getContentValues(), "devices");
                    DatabaseHelper.getInstance().closeWritableDb();*/
                }
            } catch (Exception e) {
                log.error("Critical error in saving photo" + "\n" + TPUtility.getPrintStackTrace(e));
            }
        }

        // Clear Bitmap
        if (bitmap != null) {
            bitmap.recycle();
            System.gc();
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onClick(View v) {
        try {
            int tagId = Integer.valueOf(v.getTag().toString());

            switch (tagId) {
                case DONE_BTN:
                    doneBtn.setEnabled(false);
                    acceptPicture();
                    return;

                case RETAKE_BTN:
                    onRetake();
                    break;

                case TAKE_BTN:
                    takeBtn.setEnabled(false);
                    takePicture();
                    break;

            }

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void onRetake() {
        try {
            if (isChalkPicture || isNewChalkPicture) {
                recaptureImage = true;
                photoNum = photoNumber;
            }
            retakeBtn.setEnabled(false);
            retakeBtn.setVisibility(View.INVISIBLE);
            doneBtn.setVisibility(View.INVISIBLE);
            takeBtn.setVisibility(View.VISIBLE);
            verticalSeekBar.setVisibility(View.VISIBLE);
            stickyZoom.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);

            // Start Camera Preview and Register PreviewCallback
            cView.camera.startPreview();
            cView.registerPreviewCallback();

            // Turn on Flash Torch if already enabled
            if (TPApp.pictureFlashLED) {
                try {
                    if (cView != null) {
                        cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_TORCH);
                    }
                } catch (Exception e) {
                    log.error("Error " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        DeviceInfo deviceInfo = TPApp.getDeviceInfo();
        if (!recaptureImage) {
            photoNumber = deviceInfo.getCurrentPhotoNumber() + 1;
            deviceInfo.setCurrentPhotoNumber(photoNumber);
        } else {
            photoNumber = photoNum;
        }
        //deviceInfo.setCurrentPhotoNumber(deviceInfo.getCurrentPhotoNumber() + 1);
        takeBtn.setEnabled(false);

        String filename = "";
        File file = null;
        imageResolution = "";

        if (isGenericPicture) {
            filename = imageName;
            file = new File(TPUtility.getDataFolder(), filename);

        } else if (isSepecialActivity) {

            if (isEditSepecialActivity) {
                filename = SPAPictureName;
                imageSPAName = filename;
                file = new File(TPUtility.getActivityFolder(), filename);
            } else {
                filename = "SPA-" + photoNumber + ".JPG";
                imageSPAName = filename;
                file = new File(TPUtility.getActivityFolder(), filename);
            }
        } else if (isEditSepecialActivity) {
            filename = SPAPictureName;
            imageSPAName = filename;
            file = new File(TPUtility.getActivityFolder(), filename);
        } else {
            if (isChalkPicture || isNewChalkPicture) {

                if (isEditChalkPicture) {
                    filename = chalPictureName;
                    imageChalkName = filename;
                    file = new File(TPUtility.getChalksFolder(), filename);
                } else {
                    filename = "CHALK-" + photoNumber + ".JPG";
                    imageChalkName = filename;
                    file = new File(TPUtility.getChalksFolder(), filename);
                }
            } else {
                if (citationNumber > 0) {
                    filename = TPUtility.prefixZeros(citationNumber, 8);
                } else if (chalkId > 0) {
                    filename = TPUtility.prefixZeros(chalkId, 8);
                } else {
                    filename = TPUtility.prefixZeros(photoNumber, 8);
                }

                filename = filename + "-TICKET-" + photoNumber + ".JPG";
                if (isSelfi) {
                    filename = "U" + TPApp.userId + "_" + getBadge() + ".JPG";
                }
                file = new File(TPUtility.getTicketsFolder(), filename);
            }
        }

        try {
            if (cView.camera == null) {
                takeBtn.setEnabled(true);
                return;
            }

            file.createNewFile();
            picUri = Uri.fromFile(file);
            cView.playSoundEffect(SoundEffectConstants.CLICK);
            if (cView.previewBitmapData == null) {
                return;
            }

            // Turn on torch mode
            try {
                if (flashlightMode == FlashLightMode.AUTO || flashlightMode == FlashLightMode.ON) {
                    cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_TORCH);
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            final byte[] data = cView.previewBitmapData.clone();

            // Turn off torch mode....
            try {
                cView.setFlashlightMode(Camera.Parameters.FLASH_MODE_OFF);
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            cView.camera.stopPreview();

            progressDialog = ProgressDialog.show(this, "", "Processing Image...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Camera.Parameters parameters = cView.camera.getParameters();
                        Size size = parameters.getPreviewSize();

                        YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                        ByteArrayOutputStream outstr = new ByteArrayOutputStream();
                        Rect rect = new Rect(0, 0, size.width, size.height);
                        yuvimage.compressToJpeg(rect, 90, outstr);

                        // Set MAX Image resolutions
                        int imageWidth = size.width;
                        int imageHeight = size.height;
                        try {
                            String imageSize = Feature.getFeatureValue("MaxImageResolution");
                            if (!StringUtil.isEmpty(imageSize)) {
                                String[] sizes = imageSize.split("x");
                                if (sizes.length == 2) {
                                    imageWidth = Integer.parseInt(sizes[0]);
                                    imageHeight = Integer.parseInt(sizes[1]);
                                }
                            }
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                        int degrees = 0;
                        boolean isLandscape = false;
                        if (isSelfi) {
                            degrees = cView.getRotationAngleFront();
                        } else {
                            degrees = cView.getRotationAngle();
                        }
                        if (degrees == 0 || degrees == 180) {
                            isLandscape = true;
                            imageResolution = imageHeight + "x" + imageWidth;
                        } else {
                            imageResolution = imageWidth + "x" + imageHeight;
                        }

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = TPUtility.getBitmapScale(size.width, size.height, imageWidth, imageHeight);
                        options.inJustDecodeBounds = false;
                        options.outWidth = imageWidth;
                        options.outHeight = imageHeight;

                        bitmap = BitmapFactory.decodeByteArray(outstr.toByteArray(), 0, outstr.size(), options);
                        bitmap = TPUtility.resizeBitmap(bitmap, imageWidth, imageHeight);

                        // Print date and time on photo
                        try {
                            Matrix matrix = new Matrix();
                          /*  ExifInterface exifInterface = new ExifInterface(picUri.getPath());

                            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                            int angle = 0;
                            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                                angle = 90;
                            }
                            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                                angle = 180;
                            }
                            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                                angle = 180;
                            }*/
                            matrix.postRotate(degrees);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                            if (Feature.isFeatureAllowed(Feature.PRINT_TIME_ON_PHOTO)) {
                                int y = bitmap.getHeight() - 30;
                                int x = 20;

                                if (isLandscape) {
                                    bitmap = bitmap.copy(Config.ARGB_8888, true);
                                }

                                Paint paint = new Paint();
                                paint.setColor(Color.RED);
                                paint.setTextSize(20);
                                paint.setFlags(Paint.ANTI_ALIAS_FLAG);
                                Canvas canvas = new Canvas(bitmap);
                                canvas.drawText(DateUtil.getCurrentDateTime(), x, y, paint);
                            }
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }

                        FileOutputStream fos = new FileOutputStream(picUri.getPath());
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                        fos.flush();
                        fos.close();

                    } catch (IOException e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    handler.sendEmptyMessage(1);
                }
            }).start();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public void handleVoiceInput(String text) {
        if (text == null) {
            return;
        }

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

        if (text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")) {
            backAction(null);
        } else if (text.contains("DONE") || text.contains("ACCEPT")) {
            acceptPicture();

        } else if (text.contains("TAKE PICTURE")) {
            takePicture();

        } else if (text.contains("RETAKE") || text.contains("TAKE ANOTHER")) {
            retakeBtn.setVisibility(View.INVISIBLE);
            doneBtn.setVisibility(View.INVISIBLE);
            takeBtn.setVisibility(View.VISIBLE);
            cView.camera.startPreview();
        }
    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        // TODO Auto-generated method stub
    }
	
	/*//Show Pinch Zoom
	public void showZoomImage(Bitmap imgBitmap) {
		try {
			final LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			inputDlgView = layoutInflater.inflate(R.layout.take_picture_dialog, null, false);

			imageDialog = new Dialog(TakePictureActivity.this);
			imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			imageDialog.setContentView(inputDlgView);
			imageDialog.getWindow().getAttributes().width = ViewGroup.LayoutParams.match_parent;
			imageDialog.show();
 
			TouchImageView imageView = (TouchImageView) inputDlgView.findViewById(R.id.touchImage); 
			imageView.setImageBitmap(imgBitmap);
			Button cancelBtn = (Button) inputDlgView.findViewById(R.id.take_picture_dialog_cancel_btn);
			cancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (imageDialog.isShowing()) {
						imageDialog.dismiss();
					}
				}
			});

			Button retakeBtn = (Button) inputDlgView.findViewById(R.id.take_picture_dialog_retake_btn);
			retakeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (imageDialog.isShowing()) {
						imageDialog.dismiss();
					}
					onRetake();
				}
			});
 

		} catch (Exception e) {
			log.error(TPUtility.getPrintStackTrace(e));
		}
	}
	*/

    private void saveAndSyncPicture(Ticket activeTicket, boolean isSelfi) {
        TicketPicture picture = new TicketPicture();
        picture.setPictureDate(new Date());
        picture.setCitationNumber(activeTicket.getCitationNumber());
        picture.setMarkPrint("N");
        //String[] imagePath = Objects.requireNonNull(picUri.getPath()).split("/");
        picture.setImagePath(picUri.getPath());
        picture.setImageResolution(imageResolution);
        picture.setTicketId(activeTicket.getTicketId());
        picture.setImageSize(TPUtility.getImageSize(picUri.getPath()));
        picture.setCustId(TPApp.custId);
        activeTicket.getTicketPictures().add(picture);
        activeTicket.setPhoto_count(activeTicket.getTicketPictures().size()+1);
        if (isSelfi) {
            picture.setIsSelfi("Y");
        } else {
            picture.setIsSelfi("N");
        }

        //if (editTicketPictures) {
        try {
            /*DatabaseHelper.getInstance().openWritableDatabase();
            DatabaseHelper.getInstance().insertOrReplace(picture.getContentValues(), "ticket_pictures");*/
            TicketPicture.insertTicketPicture(picture);

            boolean result = false;
            if (isFastConnection) {
                result = ((PhotosBLProcessor) screenBLProcessor).updateSelfiPicture(citationNumber, picture, TakePictureActivity.this);
            } else {
                SyncData syncData = new SyncData();
                syncData.setActivity("INSERT");
                syncData.setPrimaryKey(picture.getPictureId() + "");
                syncData.setActivityDate(new Date());
                syncData.setCustId(TPApp.custId);
                syncData.setTableName(TPConstant.TABLE_TICKET_PICTURES);
                syncData.setStatus("Pending");
                SyncData.insertSyncData(syncData).subscribe();
                //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
            }

            //DatabaseHelper.getInstance().closeWritableDb();
        } catch (Exception e) {
            Log.e("TakePicture", "Error update ticket picture records " + e.getMessage());
        }
        // Clear Bitmap
        if (bitmap != null) {
            bitmap.recycle();
            System.gc();
        }
        setResult(RESULT_OK);
        finish();

		/*} else {
			activeTicket.getTicketPictures().add(picture);
		}*/
    }

    public String getBadge() {
        if (TPApp.getUserInfo() != null)
            return TPApp.getUserInfo().getBadge();

        return "";
    }
}
