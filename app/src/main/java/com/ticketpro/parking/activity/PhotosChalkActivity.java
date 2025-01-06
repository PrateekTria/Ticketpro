package com.ticketpro.parking.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.LocationStatusCodes;
import com.ticketpro.exception.TPException;
import com.ticketpro.gpslibrary.ADLocation;
import com.ticketpro.gpslibrary.GetLocation;
import com.ticketpro.gpslibrary.MyTracker;
import com.ticketpro.lpr.lpr.ImenseLicenseServer;
import com.ticketpro.model.ALPRChalk;
import com.ticketpro.model.Address;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.DeviceGroup;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Feature;
import com.ticketpro.model.GPSLocation;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.parking.R;
import com.ticketpro.parking.api.ChalkVehicleNetworkCalls;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.parking.bl.WriteTicketBLProcessor;
import com.ticketpro.parking.service.AlarmReceiver;
import com.ticketpro.parking.service.GPSResultReceiver.Receiver;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.GPSTracker;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;
import com.ticketpro.util.UIHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Completable;

import static com.ticketpro.parking.activity.WriteTicketActivity.debug;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class PhotosChalkActivity extends BaseActivityImpl implements MyTracker.ADLocationListener {

    private final static int PT_INVALID_INVOCATION = 99;
    private final static int PT_LICENSE_MISSING_OR_INVALID = 100;
    private final static int PT_ANPR_NOTONWHITELIST = 101;
    private final static int PT_ANPR_PERMITEXPIRED = 102;
    private final static int PT_ANPR_SCANTIMEOUT = 103;
    public static String tag = "WriteTicketLPR";                 //tag for debugging
    public static String licenseKey = null;
    private static int REQUESTCODE = 55;
    final int ERROR_DATABASE = 1;
    final int ERROR_GENERIC = 2;
    final int ERROR_ADD_CHALK = 3;
    final int DATA_ERROR = 0;
    final int DATA_SUCCESSFULL = 1;
    boolean cStatus = false;
    boolean sStatus = false;
    ImageView cImage;
    ImageView sImage;
    boolean cDialogStatus = false;
    boolean sDialogStatus = false;
    ImageView cDialogImage;
    ImageView sDialogImage;
    int prevCX = -1;
    int prevCY = -1;
    int prevSX = -1;
    int prevSY = -1;
    private ImageView statusIndicatorImageView;
    private Spinner tireSpinner;
    private Spinner tireDialogSpinner;
    private Spinner durationSpinner;
    private final String[] tireDisplayNames = {"Select Tire", "Front Left", "Front Right", "Back Left", "Back Right"};
    private ArrayList<String> durations;
    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private Handler errorHandler;
    private EditText tmEditText;
    private EditText locationEditText;
    private boolean isGPSLocation = false;
    private boolean isPhotoChalk = false;
    private Address activeAddress;
    private Dialog dialog;
    private CheckBox photoChalkCheckbox;
    private Button startChalkingBtn;
    private Button addToListBtn;
    private Button gpsButton;
    private CheckBox stickyMarkersChk;
    private int cX = -1;
    private int cY = -1;
    private int sX = -1;
    private int sY = -1;
    private ChalkVehicle activeChalk;
    private ALPRChalk alprChalk;
    private GPSTracker gpsTracker;
    private Handler GPSHandler;
    private ProgressBar GPSProgressBar;
    private int dialerHeight;
    private int dialerWidth;
    private int currentAngle = 0;
    private Date chalkDate = null;
    private SharedPreferences mPreferences;
    private RelativeLayout wheelLayout;
    private Intent ptsIntent;
    private boolean lpr = false;
    private CountDownTimer countDownTimer1;
    private CountDownTimer countDownTimer;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.photo_chalk);
            setLogger(PhotosChalkActivity.class.getName());
            setActiveScreen(this);
            isNetworkInfoRequired = true;

            if (!Feature.isFeatureAllowed(Feature.CHALK)) {
                Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                finish();
            }

            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            tmEditText = (EditText) findViewById(R.id.photo_chalk_time_textview);
            locationEditText = (EditText) findViewById(R.id.photo_chalk_location_textview);
            locationEditText.setOnKeyListener(onKeyListener);

            photoChalkCheckbox = (CheckBox) findViewById(R.id.photo_chalk_photo_chk);
            startChalkingBtn = (Button) findViewById(R.id.photo_chalk_chalking_btn);
            addToListBtn = (Button) findViewById(R.id.photo_chalk_add_btn);
            statusIndicatorImageView = (ImageView) findViewById(R.id.status_indicator_image);
            GPSProgressBar = (ProgressBar) findViewById(R.id.GPSProcess);

            tireSpinner = (Spinner) findViewById(R.id.photo_chalk_tire_spinner);
            durationSpinner = (Spinner) findViewById(R.id.photo_chalk_duration_spinner);
            durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    TPUtility.hideSoftKeyboard(PhotosChalkActivity.this);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    TPUtility.hideSoftKeyboard(PhotosChalkActivity.this);
                }
            });

            wheelLayout = (RelativeLayout) findViewById(R.id.phot_chalk_circle_panel);

            activeChalk = TPApp.getActiveChalk();
            alprChalk = TPApp.getAlprChalk();
            photoChalkCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isPhotoChalk = isChecked;
                    if (isPhotoChalk) {
                        startChalkingBtn.setEnabled(true);
                        startChalkingBtn.setBackgroundResource(R.drawable.btn_blue);

                        addToListBtn.setEnabled(false);
                        addToListBtn.setBackgroundResource(R.drawable.btn_disabled);
                    } else {
                        startChalkingBtn.setEnabled(false);
                        startChalkingBtn.setBackgroundResource(R.drawable.btn_disabled);

                        addToListBtn.setEnabled(true);
                        addToListBtn.setBackgroundResource(R.drawable.btn_blue);
                    }
                }
            });

            if (!Feature.isFeatureAllowed(Feature.PHOTO_CHALK)) {
                photoChalkCheckbox.setEnabled(false);
                startChalkingBtn.setVisibility(View.GONE);
                photoChalkCheckbox.setVisibility(View.GONE);
            } else {
                try {
                    String devices = DeviceGroup.getDevicesIds(Feature.getFeatureValue(Feature.PHOTO_CHALK));
                    int deviceId = TPApplication.getInstance().getDeviceId();
                    if (devices != null && !TextUtils.isEmpty(devices)) {
                        if (devices.contains(",")) {
                            String[] split = devices.split(",");
                            for (String value : split) {
                                int s = Integer.parseInt(String.valueOf(value));
                                if (deviceId == s) {
                                    startChalkingBtn.setVisibility(View.VISIBLE);
                                    photoChalkCheckbox.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                        } else {
                            int s = Integer.parseInt(devices);
                            if (deviceId == s) {
                                startChalkingBtn.setVisibility(View.VISIBLE);
                                photoChalkCheckbox.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (Feature.isHiddenField("location")) {
                locationEditText.setVisibility(View.GONE);
            }

            if (Feature.isHiddenField("tm")) {
                tmEditText.setVisibility(View.GONE);
            }

            locationEditText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    locationAction(v);
                    return true;
                }
            });

            Button sliderMenu = (Button) findViewById(R.id.slider_menu_btn);
            sliderMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40,
                            getResources().getDisplayMetrics());
                    SlideoutActivity.prepare(PhotosChalkActivity.this, R.id.chalk_root_layout, width);
                    startActivity(new Intent(PhotosChalkActivity.this, PhotoChalkMenuActivity.class));
                    overridePendingTransition(0, 0);
                }
            });

            if (Feature.isFeatureAllowed(Feature.CHALK_TM_EDIT)) {
                tmEditText.setClickable(true);
                tmEditText.setEnabled(true);
                tmEditText.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showTMPicker();
                        return true;
                    }
                });
            }
            //startChalkingBtn.setVisibility(View.GONE);
            // startChalkingBtn.setEnabled(false);
            // startChalkingBtn.setBackgroundResource(R.drawable.btn_disabled);
            setBLProcessor(new ChalkBLProcessor((TPApplication) getApplicationContext()));
            setBLProcessor(new WriteTicketBLProcessor((TPApplication) getApplicationContext()));
            tmEditText.setText(DateUtil.getCurrentTime());

            activeAddress = TPUtility.createEmptyAddress();

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = (metrics.widthPixels / 2);
            //for high res device marker size issue
            String screenDensityName = TPApp.getDensityName(this);
            int wheelSize;
            int circleRadius = 0;
            int circleRadiusX = circleRadius;
            int circleRadiusY = circleRadius;
            int innerCircleRadius;
            if (screenDensityName.equalsIgnoreCase("xxhdpi") || screenDensityName.equalsIgnoreCase("xxxhdpi")) {
                circleRadius = (width / 2) + 5;
                wheelSize = circleRadius * 2;
                circleRadiusX = circleRadius;
                circleRadiusY = circleRadius;
                innerCircleRadius = circleRadius - 10;
            } else {
                circleRadius = (width / 2) - 20;
                wheelSize = circleRadius * 2;
                circleRadiusX = circleRadius;
                circleRadiusY = circleRadius;
                innerCircleRadius = circleRadius - 60;
            }
			/*
			final int circleRadius = (width / 2) - 15;
			final int circleRadiusX = circleRadius;
			final int circleRadiusY = circleRadius;
			final int innerCircleRadius = circleRadius - 60;*/

            RelativeLayout chalkPanel = (RelativeLayout) findViewById(R.id.phot_chalk_circle_panel);
            chalkPanel.getLayoutParams().width = circleRadius * 2;
            chalkPanel.getLayoutParams().height = circleRadius * 2;

            cImage = (ImageView) findViewById(R.id.chalk_location_c_img);
            cImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cStatus = true;
                    cImage.setImageResource(R.drawable.c_marker_selected);
                    sImage.setImageResource(R.drawable.s_marker);
                    sStatus = false;

                    showChalkWheelDialog(null);
                }
            });

            sImage = (ImageView) findViewById(R.id.chalk_location_s_img);
            sImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sStatus = true;
                    sImage.setImageResource(R.drawable.s_marker_selected);
                    cImage.setImageResource(R.drawable.c_marker);
                    cStatus = false;

                    showChalkWheelDialog(null);
                }
            });

            //int wheelSize = circleRadius * 2;

            // Calculate default X and Y values
            RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cImage.getLayoutParams();
            cLayout.setMargins(0, wheelSize - 100, 0, 0);
            cImage.setLayoutParams(cLayout);

            RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sImage.getLayoutParams();
            sLayout.setMargins(wheelSize - 100, wheelSize - 100, 0, 0);
            sImage.setLayoutParams(sLayout);

            if (TPApp.getUserSettings().isStickyMarker()) {
                sX = mPreferences.getInt(TPConstant.PREFS_KEY_STICKY_MARKER_S_X, -1);
                sY = mPreferences.getInt(TPConstant.PREFS_KEY_STICKY_MARKER_S_Y, -1);
                cX = mPreferences.getInt(TPConstant.PREFS_KEY_STICKY_MARKER_C_X, -1);
                cY = mPreferences.getInt(TPConstant.PREFS_KEY_STICKY_MARKER_C_Y, -1);
            }

            if (cX > -1 && cY > -1) {
                int x1 = (int) ((circleRadius - 20) * Math.cos(cX * Math.PI / 180) + circleRadiusX) - 20;
                int y1 = (int) ((circleRadius - 20) * Math.sin(cY * Math.PI / 180) + circleRadiusY) - 20;
                cLayout.setMargins(x1, y1, 0, 0);
                cImage.setLayoutParams(cLayout);
                cImage.setVisibility(View.VISIBLE);
            } else {
                cImage.setVisibility(View.GONE);
            }

            if (sX > -1 && sY > -1) {
                int x2 = (int) (innerCircleRadius * Math.cos(sX * Math.PI / 180) + circleRadiusX) - 20;
                int y2 = (int) (innerCircleRadius * Math.sin(sY * Math.PI / 180) + circleRadiusY) - 20;
                sLayout.setMargins(x2, y2, 0, 0);
                sImage.setLayoutParams(sLayout);
                sImage.setVisibility(View.VISIBLE);
            } else {
                sImage.setVisibility(View.GONE);
            }

            cImage.setVisibility(View.GONE);
            sImage.setVisibility(View.GONE);

            gpsButton = (Button) findViewById(R.id.photo_chalk_gps_button);
            GPSHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (activeAddress != null) {
                        locationEditText.setText(TPUtility.getFullAddress(activeAddress));
                    }

                    GPSProgressBar.setVisibility(View.GONE);
                }
            };

            dataLoadingHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (msg.what == DATA_SUCCESSFULL) {
                        durations.add(0, "ZONE");
                        String[] durationArray = (String[]) durations.toArray(new String[0]);

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PhotosChalkActivity.this,
                                R.layout.simple_spinner_item, tireDisplayNames);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        tireSpinner.setAdapter(dataAdapter);

                        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(PhotosChalkActivity.this,
                                R.layout.simple_spinner_item, durationArray);
                        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        durationSpinner.setAdapter(dataAdapter2);
                    }

                    // Disable GPS and LPR functionality on Emulators
                    try {
                        if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                            gpsButton.setEnabled(false);
                            gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                        } else {
                            gpsTracker = new GPSTracker(PhotosChalkActivity.this);
                            if (!gpsTracker.isGPSAvailable()) {
                                gpsButton.setEnabled(false);
                                gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                            } else {
                                gpsTracker.initService(new Receiver() {
                                    @Override
                                    public void onReceiveResult(GPSLocation location, Bundle resultData) {
                                    }

                                    @Override
                                    public void onReceiveResult(Location location, Bundle resultData) {
                                    }

                                    @Override
                                    public void onTimeout() {
                                    }
                                });
                            }
                            if (UIHelper.isGpsDeviceValue(TPApp.deviceId)) {
                                UIHelper.toggleButtonState(gpsButton, false);
                            } else {
                                if (!Feature.isFeatureAllowed(Feature.GPS)) {
                                    gpsButton.setEnabled(false);
                                    gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                                }
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                        isServiceAvailable = false;
                    }else {
                        isServiceAvailable = true;
                    }
                    if (isServiceAvailable) {
                        if (isFastConnection) {
                            statusIndicatorImageView.setImageResource(R.drawable.green_status_btn_bk);
                        } else {
                            statusIndicatorImageView.setImageResource(R.drawable.yellow_status_btn);
                        }
                    } else {
                        statusIndicatorImageView.setImageResource(R.drawable.gray_status_btn);
                    }

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            };

            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    switch (msg.what) {
                        case ERROR_GENERIC:
                            Toast.makeText(getBaseContext(), TPConstant.GENERIC_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR_ADD_CHALK:
                            Toast.makeText(getBaseContext(), "Failed to add chalk details.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void showTMPicker() {

        Calendar cal = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timePicker = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                chalkDate = new Date();
                chalkDate.setHours(hourOfDay);
                chalkDate.setMinutes(minute);

                if (chalkDate.getTime() > new Date().getTime()) {
                    TPUtility.showErrorToast(PhotosChalkActivity.this, "You cannot set a future time.");

                    showTMPicker();
                    return;
                }

                tmEditText.setText(DateUtil.getTimeStringFromDate(chalkDate));
            }
        };

        TimePickerDialog pickerDialog = new TimePickerDialog(this, timePicker, cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), true);
        pickerDialog.setButton(TimePickerDialog.BUTTON_NEUTRAL, "Current Time", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                tmEditText.setText(DateUtil.getCurrentTime());
            }
        });

        pickerDialog.show();
    }

    public void showChalkWheelDialog(View view) {

        dialog = new Dialog(this);
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = layoutInflater.inflate(R.layout.chalk_wheel_dialog, null, false);
        dialog.setTitle("Chalk Wheel Markers");
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.show();

        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        final int circleRadius = (width / 2) - (int) (26 * metrics.density);
        final int circleRadiusX = circleRadius;
        final int circleRadiusY = circleRadius;
        dialerWidth = circleRadius * 2;
        dialerHeight = circleRadius * 2;

        RelativeLayout chalkPanel = (RelativeLayout) dialogView.findViewById(R.id.chalk_vehicle_circle_panel);
        chalkPanel.getLayoutParams().width = dialerWidth;
        chalkPanel.getLayoutParams().height = dialerHeight;

        tireDialogSpinner = (Spinner) dialogView.findViewById(R.id.chalk_tire_spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, tireDisplayNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tireDialogSpinner.setAdapter(dataAdapter);
        tireDialogSpinner.setSelection(tireSpinner.getSelectedItemPosition());

        stickyMarkersChk = (CheckBox) dialogView.findViewById(R.id.chalk_wheel_sticky);
        stickyMarkersChk.setChecked(TPApp.getUserSettings().isStickyMarker());
        stickyMarkersChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TPApp.getUserSettings().setStickyMarker(isChecked);
                TPApp.updateUserSettings();
            }
        });

        if (TPApp.getUserSettings().isStickyMarker()) {
            if (sX == -1 && sY == -1) {
                sX = mPreferences.getInt(TPConstant.PREFS_KEY_STICKY_MARKER_S_X, -1);
                sY = mPreferences.getInt(TPConstant.PREFS_KEY_STICKY_MARKER_S_Y, -1);
            }

            if (cX == -1 && cY == -1) {
                cX = mPreferences.getInt(TPConstant.PREFS_KEY_STICKY_MARKER_C_X, -1);
                cY = mPreferences.getInt(TPConstant.PREFS_KEY_STICKY_MARKER_C_Y, -1);
            }
        }

        cDialogImage = (ImageView) dialogView.findViewById(R.id.chalk_vehicle_c_img);
        cDialogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cDialogStatus = true;
                cDialogImage.setImageResource(R.drawable.c_marker_selected);
                sDialogImage.setImageResource(R.drawable.s_marker);
                sDialogStatus = false;
            }
        });

        cDialogImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cDialogStatus = true;
                cDialogImage.setImageResource(R.drawable.c_marker_selected);
                sDialogImage.setImageResource(R.drawable.s_marker);
                sDialogStatus = false;
                return true;
            }
        });

        sDialogImage = (ImageView) dialogView.findViewById(R.id.chalk_vehicle_s_img);
        sDialogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialogStatus = true;
                sDialogImage.setImageResource(R.drawable.s_marker_selected);
                cDialogImage.setImageResource(R.drawable.c_marker);
                cDialogStatus = false;
            }
        });

        sDialogImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                sDialogStatus = true;
                sDialogImage.setImageResource(R.drawable.s_marker_selected);
                cDialogImage.setImageResource(R.drawable.c_marker);
                cDialogStatus = false;
                return true;
            }
        });

        if (sStatus) {
            sDialogStatus = true;
            cDialogStatus = false;
            sDialogImage.setImageResource(R.drawable.s_marker_selected);
        }

        if (cStatus) {
            cDialogStatus = true;
            sDialogStatus = false;
            cDialogImage.setImageResource(R.drawable.c_marker_selected);
        }

        prevCX = cX;
        prevCY = cY;
        prevSX = sX;
        prevSY = sY;

        final int innerCircleRadius = circleRadius - (int) (60 * metrics.density);

        // Calculate default X and Y values
        if (cX > -1 && cY > -1) {
            int x1 = (int) ((circleRadius - (int) (20 * metrics.density)) * Math.cos(cX * Math.PI / 180)
                    + circleRadiusX) - (int) (20 * metrics.density);
            int y1 = (int) ((circleRadius - (int) (20 * metrics.density)) * Math.sin(cY * Math.PI / 180)
                    + circleRadiusY) - (int) (20 * metrics.density);
            RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cDialogImage.getLayoutParams();
            cLayout.setMargins(x1, y1, 0, 0);
            cDialogImage.setLayoutParams(cLayout);
        } else {
            RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cDialogImage.getLayoutParams();
            cLayout.setMargins(0, dialerHeight - (int) (55 * metrics.density), 0, 0);
            cDialogImage.setLayoutParams(cLayout);
        }

        if (sX > -1 && sY > -1) {
            int x2 = (int) (innerCircleRadius * Math.cos(sX * Math.PI / 180) + circleRadiusX)
                    - (int) (20 * metrics.density);
            int y2 = (int) (innerCircleRadius * Math.sin(sY * Math.PI / 180) + circleRadiusY)
                    - (int) (20 * metrics.density);
            RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sDialogImage.getLayoutParams();
            sLayout.setMargins(x2, y2, 0, 0);
            sDialogImage.setLayoutParams(sLayout);
        } else {
            RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sDialogImage.getLayoutParams();
            sLayout.setMargins(dialerWidth - (int) (55 * metrics.density), dialerHeight - (int) (55 * metrics.density),
                    0, 0);
            sDialogImage.setLayoutParams(sLayout);
        }

        Button cancel = (Button) dialogView.findViewById(R.id.chalk_wheel_cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sImage.setImageResource(R.drawable.s_marker);
                cImage.setImageResource(R.drawable.c_marker);

                cX = prevCX;
                cY = prevCY;
                sX = prevSX;
                sY = prevSY;

                dialog.dismiss();
            }
        });

        Button reset = (Button) dialogView.findViewById(R.id.chalk_wheel_reset_btn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tireSpinner.setSelection(0);
                cX = -1;
                cY = -1;
                sX = -1;
                sY = -1;

                RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cDialogImage.getLayoutParams();
                cLayout.setMargins(0, dialerHeight - (int) (55 * metrics.density), 0, 0);
                cDialogImage.setLayoutParams(cLayout);

                RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sDialogImage.getLayoutParams();
                sLayout.setMargins(dialerWidth - (int) (55 * metrics.density),
                        dialerHeight - (int) (55 * metrics.density), 0, 0);
                sDialogImage.setLayoutParams(sLayout);
            }
        });

        Button update = (Button) dialogView.findViewById(R.id.chalk_wheel_update_btn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tireSpinner.setSelection(tireDialogSpinner.getSelectedItemPosition());

                // Update Preview Markers
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = (metrics.widthPixels / 2);
                int circleRadius = (width / 2) - 15;
                int circleRadiusX = circleRadius;
                int circleRadiusY = circleRadius;
                int innerCircleRadius = circleRadius - 60;
                int wheelSize = circleRadius * 2;

                if (cX > -1 && cY > -1) {
                    int x1 = (int) ((circleRadius - 20) * Math.cos(cX * Math.PI / 180) + circleRadiusX) - 20;
                    int y1 = (int) ((circleRadius - 20) * Math.sin(cY * Math.PI / 180) + circleRadiusY) - 20;
                    RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cImage.getLayoutParams();
                    cLayout.setMargins(x1, y1, 0, 0);
                    cImage.setLayoutParams(cLayout);
                    cImage.setVisibility(View.VISIBLE);
                } else {
                    RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cImage.getLayoutParams();
                    cLayout.setMargins(0, wheelSize - 100, 0, 0);
                    cImage.setLayoutParams(cLayout);
                    cImage.setVisibility(View.GONE);
                }

                if (sX > -1 && sY > -1) {
                    int x2 = (int) (innerCircleRadius * Math.cos(sX * Math.PI / 180) + circleRadiusX) - 20;
                    int y2 = (int) (innerCircleRadius * Math.sin(sY * Math.PI / 180) + circleRadiusY) - 20;
                    RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sImage.getLayoutParams();
                    sLayout.setMargins(x2, y2, 0, 0);
                    sImage.setLayoutParams(sLayout);
                    sImage.setVisibility(View.VISIBLE);
                } else {
                    RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sImage.getLayoutParams();
                    sLayout.setMargins(wheelSize - 100, wheelSize - 100, 0, 0);
                    sImage.setLayoutParams(sLayout);
                    sImage.setVisibility(View.GONE);
                }

                if ((sX > -1 && sY > -1) || (cX > -1 && cY > -1)) {
                    String tire = (String) tireSpinner.getSelectedItem();
                    if (tire.equalsIgnoreCase("Front Left")) {
                        wheelLayout.setBackgroundResource(R.drawable.tire_fl);

                    } else if (tire.equalsIgnoreCase("Front Right")) {
                        wheelLayout.setBackgroundResource(R.drawable.tire_fr);

                    } else if (tire.equalsIgnoreCase("Back Left")) {
                        wheelLayout.setBackgroundResource(R.drawable.tire_bl);

                    } else if (tire.equalsIgnoreCase("Back Right")) {
                        wheelLayout.setBackgroundResource(R.drawable.tire_br);
                    }
                } else {
                    wheelLayout.setBackgroundResource(R.drawable.tire2);
                }

                sImage.setImageResource(R.drawable.s_marker);
                cImage.setImageResource(R.drawable.c_marker);

                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putInt(TPConstant.PREFS_KEY_STICKY_MARKER_C_X, cX);
                editor.putInt(TPConstant.PREFS_KEY_STICKY_MARKER_C_Y, cY);
                editor.putInt(TPConstant.PREFS_KEY_STICKY_MARKER_S_X, sX);
                editor.putInt(TPConstant.PREFS_KEY_STICKY_MARKER_S_Y, sY);
                editor.commit();

                dialog.dismiss();
            }
        });

        chalkPanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        currentAngle = (int) TPUtility.getAngle(e.getX(), e.getY(), dialerWidth, dialerHeight);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        currentAngle = (int) TPUtility.getAngle(e.getX(), e.getY(), dialerWidth, dialerHeight);
                        break;

                    case MotionEvent.ACTION_UP:
                        currentAngle = (int) TPUtility.getAngle(e.getX(), e.getY(), dialerWidth, dialerHeight);
                        break;
                }

                currentAngle = 360 - currentAngle;
                if (cDialogStatus) {
                    cX = currentAngle;
                    cY = currentAngle;
                    int x1 = (int) ((circleRadius - (int) (20 * metrics.density))
                            * Math.cos(cX * Math.PI / 180)
                            + circleRadiusX) - (int) (20 * metrics.density);
                    int y1 = (int) ((circleRadius - (int) (20 * metrics.density))
                            * Math.sin(cY * Math.PI / 180)
                            + circleRadiusY) - (int) (20 * metrics.density);
                    RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cDialogImage.getLayoutParams();
                    cLayout.setMargins(x1, y1, 0, 0);
                    cDialogImage.setLayoutParams(cLayout);
                }

                if (sDialogStatus) {
                    sX = currentAngle;
                    sY = currentAngle;
                    int x2 = (int) (innerCircleRadius * Math.cos(sX * Math.PI / 180)
                            + circleRadiusX) - (int) (20 * metrics.density);
                    int y2 = (int) (innerCircleRadius * Math.sin(sY * Math.PI / 180)
                            + circleRadiusY) - (int) (20 * metrics.density);
                    RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sDialogImage.getLayoutParams();
                    sLayout.setMargins(x2, y2, 0, 0);
                    sDialogImage.setLayoutParams(sLayout);
                }

                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initTimeOut();
        tmEditText.setText(DateUtil.getCurrentTime());
        try {
            if (countDownTimer != null) {
                countDownTimer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindDataAtLoadingTime() {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread() {
            public void run() {
                ChalkBLProcessor blProcessor = new ChalkBLProcessor((TPApplication) getApplicationContext());
                try {
                    durations = blProcessor.getDurations();
                    dataLoadingHandler.sendEmptyMessage(DATA_SUCCESSFULL);
                } catch (TPException e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                    errorHandler.sendEmptyMessage(ERROR_GENERIC);
                }
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            int returnMessage = 0;
            if (lpr) {
                TPUtility.populateSQliteFromCSV(TPUtility.getALPRImagesFolder() + "chalkList.csv", Duration.getDurationIdByName((String) durationSpinner.getSelectedItem()), locationEditText.getText().toString(), (String) tireSpinner.getSelectedItem(), PhotosChalkActivity.this);
                lpr = false;
            }
            if (data != null) returnMessage = data.getExtras().getInt("message");
            if (debug > 0)
                Log.d(tag, "onActivityResult:  requestCode=" + requestCode + ", resultCode=" + resultCode + ", data=" + data + ", ptsIntent=" + ptsIntent + ", returnMessage=" + returnMessage);
            if (returnMessage == PT_ANPR_NOTONWHITELIST) {
                String sRegNumber = data.getExtras().getString("anpr_not_in_whitelist");
                int regConf = data.getExtras().getInt("anpr_not_in_whitelist_conf");
                //plateNumberEditText.setText(sRegNumber);
                Toast.makeText(this, "PTS returned with vehicle plate that is not in the whitelist: " + sRegNumber + " (conf=" + regConf + ")", Toast.LENGTH_LONG).show();
            } else if (returnMessage == PT_ANPR_PERMITEXPIRED) {
                String sRegNumber = data.getExtras().getString("anpr_permit_expired");
                int regConf = data.getExtras().getInt("anpr_permit_expired_conf");
                //plateNumberEditText.setText(sRegNumber);
                String sTimeExceeded = data.getExtras().getString("time_since_permit_expired");
                Toast.makeText(this, "PTS returned with whitelisted plate: " + sRegNumber + " (conf=" + regConf + ") having exceeded parking permit by " + sTimeExceeded, Toast.LENGTH_LONG).show();
            } else if (returnMessage == PT_ANPR_SCANTIMEOUT) {
                Toast.makeText(this, "PTS returned after scan timeout", Toast.LENGTH_LONG).show();
            } else if (returnMessage == PT_LICENSE_MISSING_OR_INVALID) {
                final String deviceID = data.getExtras().getString("duid"); //unique device ID
                final PhotosChalkActivity caller = this;
                //obtain new license key
                new AlertDialog.Builder(this)
                        .setTitle("License Verification Problem")
                        .setCancelable(false)
                        .setMessage("PTS reports: license key missing or invalid. Please ensure that your device's WiFi adapter is enabled and has Internet access, then " +
                                "click <" + this.getString(android.R.string.ok) + "> to (re)generate a valid license key from our server.")
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        // try to obtain new license key from Imense Server
                                        new ImenseLicenseServer(caller, deviceID).execute();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data == null) {
            return;
        }

        tmEditText.setText(DateUtil.getCurrentTime());
        if (resultCode == RESULT_OK) {
            if (data.hasExtra("Location")) {
                activeAddress.setLocation(data.getStringExtra("Location"));
                activeAddress.setStreetNumber(data.getStringExtra("StreetNumber"));
                activeAddress.setStreetPrefix(data.getStringExtra("StreetPrefix"));
                activeAddress.setStreetSuffix(data.getStringExtra("StreetSuffix"));
                activeAddress.setDirection(data.getStringExtra("Direction"));
                locationEditText.setText(TPUtility.getFullAddress(activeAddress));
                isGPSLocation = false;

                if (GPSProgressBar != null) {
                    GPSProgressBar.setVisibility(View.GONE);
                }

                // Redirect to Location Entry Form
                if (data.hasExtra("REDIRECT_LOCATION_FORM") && !TPApp.getUserSettings().isSkipLocationEntry()) {
                    locationAction(null);
                }
            }
        }

        TPUtility.hideSoftKeyboard(this);
    }

    private void showDialogForLastChalkedVehicle() {

        final ALPRChalk alprChalk = ALPRChalk.getLastChalkedVehicle();
        if (alprChalk != null) {
            int mins = Duration.getDurationMinsById(alprChalk.getChalkDurationId(), PhotosChalkActivity.this);
            long diff = (new Date().getTime() - alprChalk.getFirstDateTime().getTime());
            long expTime = (diff / 1000) / 60;
            if (expTime > mins) {
                String time = TPUtility.getHrsMinSecs(diff);
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(PhotosChalkActivity.this);
                confirmBuilder
                        .setTitle("Alert")
                        .setMessage(alprChalk.getPlate() + " has exceeded the " + alprChalk.getChalkDurationCode() + " zone. Do you want to write a ticket for the vehicle?").setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // ignorePlateValidation = true;

                            }
                        })
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Ticket ticket = TPApp.createTicketForChalk(alprChalk);

                                TPApp.setActiveTicket(ticket);
                                Intent i = new Intent();
                                i.setClass(PhotosChalkActivity.this, WriteTicketActivity.class);
                                i.putExtra("ChalkId", alprChalk.getChalkId());
                                startActivityForResult(i, 0);
                                dialog.dismiss();

                            }
                        });
                AlertDialog confirmAlert = confirmBuilder.create();
                confirmAlert.show();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void viewListAction(View view) {

        Intent i = new Intent();
        if (isPhotoChalk) {
            i.setClass(this, PhotosChalkListActivity.class);
        } else {
            i.setClass(this, LocationChalkListActivity.class);
        }

        startActivity(i);
    }

    public void startChalking(View view) {
        try {
            String location = locationEditText.getText().toString();
            String duration = (String) durationSpinner.getSelectedItem();

            tmEditText.setBackgroundResource(R.drawable.text_field_bkg);
            locationEditText.setBackgroundResource(R.drawable.text_field_bkg);

            if (location.equals("")) {
                locationEditText.setBackgroundResource(R.drawable.text_field_error);
                displayErrorMessage("Please enter location.");
                return;
            }

            if (duration.equals("ZONE")) {
                displayErrorMessage("Please select zone.");
                return;
            }

            //activeChalk = TPApp.createNewChalk();

            TPApp.setActiveChalk(activeChalk);

            if (durationSpinner != null) {
                int durationId = Duration.getDurationIdByName(duration);
                activeChalk.setDurationId(durationId);
            }

            if (tireSpinner != null) {
                String tire = (String) tireSpinner.getSelectedItem();
                activeChalk.setTire(tire);
            }

            activeChalk.setChalkDate(new Date());
            activeChalk.setChalkType(TPConstant.CHALK_TYPE_PHOTO);
            activeChalk.setChalkx(cX);
            activeChalk.setChalkx(cY);
            activeChalk.setStemx(sX);
            activeChalk.setStemy(sY);
            activeChalk.setLocation(activeAddress.getLocation());
            activeChalk.setStreetNumber(activeAddress.getStreetNumber());
            activeChalk.setStreetPrefix(activeAddress.getStreetPrefix());
            activeChalk.setStreetSuffix(activeAddress.getStreetSuffix());
            activeChalk.setDirection(activeAddress.getDirection());
            activeChalk.setIsGPSLocation(isGPSLocation ? "Y" : "N");

			/*Intent intent = new Intent();
			intent.setClass(this, PhotosChalkChalkingActivity.class);
			intent.putExtra("Location", activeChalk.getLocation());
			intent.putExtra("StreetNumber", activeChalk.getStreetNumber());
			intent.putExtra("StreetPrefix", activeChalk.getStreetPrefix());
			intent.putExtra("StreetSuffix", activeChalk.getStreetSuffix());
			intent.putExtra("Direction", activeChalk.getDirection());
			startActivity(intent);*/
            if (Feature.isFeatureAllowed(Feature.ALPR_ADMINLAUNCH)) {
                launchPTS(true);
            } else {
                launchPTS(false);
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            errorHandler.sendEmptyMessage(ERROR_GENERIC);
        }
    }


    void launchPTS(boolean admin) {

        //obtain an Intent to launch ANPR/ALPR Platform Server
        try {
            ptsIntent = new Intent();
            ptsIntent.setComponent(new ComponentName("com.imense.anprPlatformUS", "com.imense.anprPlatformUS.ImenseParkingEnforcer"));
            //authenticate the request with the correct invocation code
            if (admin) ptsIntent.putExtra("invocationcode", TPConstant.INVOCATION_ADMIN);
            else ptsIntent.putExtra("invocationcode", TPConstant.INVOCATION_USER);

            //set PT into portrait mode (not recommended since it reduces effective plate pixel resolution)
            if (Feature.isFeatureAllowed(Feature.ALPR_PORTRAIT_ORIENTATION)) {
                ptsIntent.putExtra("orientation", "portrait");
            }

            //optionally instruct PT to start scan (i.e. invoke shutter button) immediately; 0: off; 1: start scan using in-built device camera
            ptsIntent.putExtra("startscan", "1");
            ptsIntent.putExtra("preferences_alertsListRatherThanWhitelist", "true"); //Alerts list rather than Whitelist. Value can be "true" or "false" (default="true")
            ptsIntent.putExtra("returnOnScanTimeout", "1"); //return control to invoking application (with "PT_ANPR_SCANTIMEOUT") on a continuous scan timeout (see also "preferences_scanTimeout" and "startscan")
            ptsIntent.putExtra("preferences_saveimages_path", TPUtility.getALPRImagesFolder()); //Folder for data and images; has to exist and be writable
            ptsIntent.putExtra("preferences_vehiclesfilename", "chalkList.csv"); //Vehicles list file name. Default value="parkingList.csv"
            ptsIntent.putExtra("preferences_savecutouts", "true"); //Save plate cut-out image after every good read. Value can be "true" or "false" (default="true")
            ptsIntent.putExtra("preferences_savecontextimages", "true"); //Save context image to SD card after every good read. Value can be "true" or "false" (default="false")
            ptsIntent.putExtra("preferences_savecontextimagescolour", "true"); //Save context images in colour. Value can be "true" or "false" (default="false")
            //ptsIntent.putExtra("preferences_warnAfterNmins", "1");
			/*//////////////////////////////
			//Optionally explicitly specify values for settings such as folder for data and images, option to save context image, scan time threshold, minimum confidence threshold, region and read options.

			ptsIntent.putExtra("hideUI", "1"); //hide all UI elements, regardless of other settings

			/////////// PTS new settings
			ptsIntent.putExtra("preferences_alertsListRatherThanWhitelist", "true"); //Alerts list rather than Whitelist. Value can be "true" or "false" (default="true")
			ptsIntent.putExtra("preferences_showmagnifier", "true"); //Display magnifier button. Value can be "true" or "false" (default="true")
			ptsIntent.putExtra("preferences_viewfinder2", "false"); //Enable second adjustable zone-of-interest. Value can be "true" or "false" (default="false")
			ptsIntent.putExtra("preferences_minplatelength", "4"); //Minimum plate length (#characters). Value must be positive numeric, default="4".
			ptsIntent.putExtra("preferences_accuracyVsSpeed", "0"); //Accuracy vs Speed (0: most accurate; 4: fastest).
			ptsIntent.putExtra("preferences_IRmode", "false"); //Enable infrared ANPR. Value can be "true" or "false" (default="false")
			ptsIntent.putExtra("preferences_dataButtonPersistence", "5"); //Show data entry button for this many seconds after plate observation. Value must be positive numeric, default="5".


			///////////Process Files from Folder
			ptsIntent.putExtra("preferences_processFiles", "false"); //Process images from import folder rather than camera. Value can be "true" or "false" (default="false")
			ptsIntent.putExtra("preferences_processFiles_path", "/mnt/sdcard/ANPR"); //Import folder for image files to process; has to exist and be writable
			ptsIntent.putExtra("preferences_processFilesRecentOnly", "true"); //Only process images created after scan was started. Value can be "true" or "false" (default="true")
			ptsIntent.putExtra("preferences_processFilesDisplay", "true"); //Display image files after scanning. Value can be "true" or "false" (default="true")
			ptsIntent.putExtra("preferences_processFilesDelete", "true"); //Delete image files after scanning. Value can be "true" or "false" (default="true")
			ptsIntent.putExtra("preferences_processFilesPurgeAll", "true"); //Purge older images (i.e. those older than the time the current scan was started) in the import folder. Value can be "true" or "false" (default="true")

			///////////List settings
			ptsIntent.putExtra("preferences_saveimages_path", "/mnt/sdcard"); //Folder for data and images; has to exist and be writable
			ptsIntent.putExtra("preferences_vehiclesfilename", "parkingList.csv"); //Vehicles list file name. Default value="parkingList.csv"
			ptsIntent.putExtra("preferences_alertsfilename", "parkingAlerts.csv"); //Alerts list\Whitelist file name. Default value="parkingAlerts.csv"

			///////////General settings


			ptsIntent.putExtra("preferences_expungePlatesAfterNhours", "72"); //Expunge vehicle list entries after this many hours. Value must be positive numeric, default="72".
			ptsIntent.putExtra("preferences_warnAfterNmins", "0"); //Warn if parked vehicle time exceeds this many minutes. Value must be positive numeric, default="0".

			ptsIntent.putExtra("preferences_confGoodread", "80"); //"High confidence threshold (0-100). Value must be positive numeric, default="80".

			ptsIntent.putExtra("preferences_scanTimeout", "90"); //Continuous scan timeout (seconds). Value must be positive numeric, default="120".
			ptsIntent.putExtra("preferences_playsound", "true"); //Play beep after every high confidence scan. Value can be "true" or "false" (default="true")
			ptsIntent.putExtra("preferences_showsingleshot", "false"); //Display button to save single image to SD card. Value can be "true" or "false" (default="false")
			ptsIntent.putExtra("preferences_saveSingleshotInColour", "false"); //Store single PIC images in colour. Value can be "true" or "false" (default="false")

			ptsIntent.putExtra("preferences_showtorch", "false"); //Display torch button. Value can be "true" or "false" (default="false")

			ptsIntent.putExtra("preferences_viewfinder", "false"); //Enable adjustable zone-of-interest within viewfinder for faster processing. Value can be "true" or "false" (default="false")

			ptsIntent.putExtra("preferences_trylightondark", "false"); //Attempt to read light-on-dark plates (WARNING: reduces speed). Value can be "true" or "false" (default="false")


			///////////Optionally restrict ANPR to specific countries, regions or states

			//ONLY ONE of the following can be set. The default is "All regions: no restriction".
			ptsIntent.putExtra("preferences_region", "All regions: no restriction"); //No restrictions, i.e. all countries are considered.
			//ptsIntent.putExtra("preferences_region", "Quebec, Canada");
			//ptsIntent.putExtra("preferences_region", "Alberta, Canada");
			//ptsIntent.putExtra("preferences_region", "Ontario, Canada");
			//ptsIntent.putExtra("preferences_region", "Pennsylvania, USA");
			//ptsIntent.putExtra("preferences_region", "Maryland and DC, USA");
			//ptsIntent.putExtra("preferences_region", "New York, USA");
			//ptsIntent.putExtra("preferences_region", "Florida, USA");
			//ptsIntent.putExtra("preferences_region", "Idaho, USA");
			//ptsIntent.putExtra("preferences_region", "Mexico");


			///////////Parking Bay Numbers
			ptsIntent.putExtra("preferences_pbn_enable", "false"); //Automatically apply PBN (parking bay number). Value can be "true" or "false" (default="false")
			ptsIntent.putExtra("preferences_pbn_prefix", ""); //PBN prefix string. Text value of 0 to 5 characters, default is "" (empty string).
			ptsIntent.putExtra("preferences_pbn_start", "00"); //PBN start value that is applied to the next parking bay. Must be a string of digits of between 2 and 5 characters, default is "00".
			ptsIntent.putExtra("preferences_pbn_increment", "1"); //PBN increment value (can be positive or negative). Must be a string of digits (optionally starting with "-" to indicate a negative increment) of between 1 and 3 characters, default is "1".

			///////////Custom Data Fields
			ptsIntent.putExtra("preferences_data1prompt", "Custom Data 1"); //Prompt for custom data field 1. Must be a text string of 0 to 20 characters, default is "Custom Data 1".
			ptsIntent.putExtra("preferences_data2prompt", "Custom Data 2"); //Prompt for custom data field 2. Must be a text string of 0 to 20 characters, default is "Custom Data 2".
			ptsIntent.putExtra("preferences_data3prompt", "Custom Data 3"); //Prompt for custom data field 3. Must be a text string of 0 to 20 characters, default is "Custom Data 3".

			ptsIntent.putExtra("preferences_audiomax", "60"); //Maximum duration of voice note audio recordings in seconds. Value must be positive numeric, default="60".


			///////////Advanced Settings
			ptsIntent.putExtra("preferences_minConsecutiveReads", "1"); //Minimum number of consecutive ANPR reads of a particular plate before result can be accepted (default=1)

			ptsIntent.putExtra("preferences_videoResolutionWidth", "800"); //Preferred device video resolution (horizontal pixels). Value must be positive numeric.
			ptsIntent.putExtra("preferences_videoResolutionHeight", "600"); //Preferred device video resolution (vertical pixels). Value must be positive numeric.

			//**/


/*			//Setting to process files from import folder
			ptsIntent.putExtra("preferences_processFiles", "true"); //Process images from import folder rather than camera. Value can be "true" or "false" (default="false")
			ptsIntent.putExtra("preferences_processFiles_path", "/mnt/sdcard/ANPR"); //Import folder for image files to process; has to exist and be writable
			ptsIntent.putExtra("preferences_processFilesRecentOnly", "true"); //Only process images created after scan was started. Value can be "true" or "false" (default="true")
			ptsIntent.putExtra("preferences_processFilesDisplay", "true"); //Display image files after scanning. Value can be "true" or "false" (default="true")
			ptsIntent.putExtra("preferences_processFilesDelete", "true"); //Delete image files after scanning. Value can be "true" or "false" (default="true")
			ptsIntent.putExtra("preferences_processFilesPurgeAll", "true"); //Purge older images (i.e. those older than the time the current scan was started) in the import folder. Value can be "true" or "false" (default="true")

			//Be more lenient to ensure higher hit rate
			ptsIntent.putExtra("preferences_confGoodread", "75"); //"High confidence threshold (0-100). Value must be positive numeric, default="80".

			//Ensure scan does not terminate too quickly; 3600s = 1 hour
			ptsIntent.putExtra("preferences_scanTimeout", "3600"); //Continuous scan timeout (seconds). Value must be positive numeric, default="90".
	*/
            //if we already have a license key, we send it to Platform Server
            if (licenseKey != null) ptsIntent.putExtra("licensekey", licenseKey);

            if (debug > 0) Log.d(tag, "startActivityForResult ptsIntent=" + ptsIntent);

            startActivityForResult(ptsIntent, REQUESTCODE);
            lpr = true;
        } catch (Exception err) {
            /**/
            lpr = false;
            if (debug > 0) {
                Log.e(tag, "launchPTS Error: " + err);
                err.printStackTrace();
            }
            Toast.makeText(PhotosChalkActivity.this, "ALPR PlatformServerUS not found: please install it", Toast.LENGTH_LONG).show();
        }

    }

    public void addAction(View view) {
        try {
            String location = locationEditText.getText().toString();
            String duration = (String) durationSpinner.getSelectedItem();

            if (location.equals("")) {
                displayErrorMessage("Please enter location.");
                return;
            }

            if (duration.equals("ZONE")) {
                displayErrorMessage("Please select zone.");
                return;
            }

            int durationId = Duration.getDurationIdByName(duration);
            int durationMins = Duration.getDurationMinsById(durationId, this);

            String tireString = (String) tireSpinner.getSelectedItem();
            activeChalk.setTire(tireString.equalsIgnoreCase("Select Tire") ? "" : tireString);
            activeChalk.setChalkDate((chalkDate == null) ? new Date() : chalkDate);
            activeChalk.setChalkx(cX);
            activeChalk.setChalky(cY);
            activeChalk.setStemx(sX);
            activeChalk.setStemy(sY);
            activeChalk.setChalkType(TPConstant.CHALK_TYPE_LOCATION);
            activeChalk.setDurationId(durationId);
            activeChalk.setLocation(activeAddress.getLocation());
            activeChalk.setStreetNumber(activeAddress.getStreetNumber());
            activeChalk.setStreetPrefix(activeAddress.getStreetPrefix());
            activeChalk.setStreetSuffix(activeAddress.getStreetSuffix());
            activeChalk.setDirection(activeAddress.getDirection());
            activeChalk.setIsGPSLocation(isGPSLocation ? "Y" : "N");
            activeChalk.setSyncStatus("P");
            activeChalk.setCustId(TPApp.getCustId());

            activeChalk.setExpiration(TPUtility.addMinutesToDate(durationMins, activeChalk.getChalkDate()));

            // Update Wheel Size
            activeChalk.setWheelSize(dialerWidth);

            // Location Notification
            long currentTime = System.currentTimeMillis();
            long expirationTime = currentTime + (durationMins * 60 * 1000);

            Intent notificationIntent = new Intent(this, AlarmReceiver.class);
            notificationIntent.putExtra("Title", "Chalk Expiration");
            notificationIntent.putExtra("Message", "Chalk at " + activeAddress.getLocation() + " has expired");
            notificationIntent.putExtra("ChalkId", activeChalk.getChalkId());
            notificationIntent.putExtra("Type", "LocationChalk");

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, TPApplication.notificationId + 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, expirationTime, pendingIntent);

            // Update GPS Location
            if (gpsTracker != null && gpsTracker.getLastLocation() != null) {
                activeChalk.setLatitude(gpsTracker.getLastLocation().getLatitude() + "");
                activeChalk.setLongitude(gpsTracker.getLastLocation().getLongitude() + "");
            }

            //DatabaseHelper.getInstance().openWritableDatabase();
            //DatabaseHelper.getInstance().insertOrReplace(activeChalk.getContentValues(), "chalk_vehicles");
            Completable completable1 = ChalkVehicle.insertChalkVehicle(activeChalk);
            completable1.subscribe();
            tmEditText.setText(DateUtil.getCurrentTime());

            ArrayList<SyncData> syncList = new ArrayList<SyncData>();

            SyncData syncData = new SyncData();
            syncData.setActivity("INSERT");
            syncData.setPrimaryKey(activeChalk.getChalkId() + "");
            syncData.setActivityDate(new Date());
            syncData.setCustId(TPApp.custId);
            syncData.setTableName(TPConstant.TABLE_CHALKS);
            syncData.setStatus("Pending");
            Completable completable = SyncData.insertSyncData(syncData);
            completable.subscribe();
            //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
            syncList.add(syncData);
            if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                isServiceAvailable = false;
            }else {
                isServiceAvailable = true;
            }
            if (isServiceAvailable) {

                try {
                    ChalkVehicleNetworkCalls.saveChalk(syncList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*AsyncTask<ArrayList<SyncData>, Void, Boolean> saveTask = new AsyncTask<ArrayList<SyncData>, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(ArrayList<SyncData>... params) {
                        ArrayList<SyncData> syncData = params[0];
                        if (syncData != null) {
                            try {
                                ChalkBLProcessor blProcessor = new ChalkBLProcessor((TPApplication) getApplicationContext());
                                blProcessor.syncData(syncData, PhotosChalkActivity.this);
                                return true;
                            } catch (TPException e) {
                                log.error(TPUtility.getPrintStackTrace(e));
                            }
                        }

                        return false;
                    }
                };
                saveTask.execute(syncList);*/
            }

            // Create New Chalk
            activeChalk = TPApp.createNewChalk();
            if (!TPApp.getUserSettings().isStickyMarker()) {
                cX = -1;
                cY = -1;
                sX = -1;
                sY = -1;

                activeChalk.setChalkx(cX);
                activeChalk.setChalky(cY);
                activeChalk.setStemx(sX);
                activeChalk.setStemy(sY);
                activeChalk.setTire("");

                cImage.setVisibility(View.GONE);
                sImage.setVisibility(View.GONE);
                wheelLayout.setBackgroundResource(R.drawable.tire2);
            }
            TPApp.setActiveChalk(activeChalk);
            Toast toast = Toast.makeText(this, "Added Chalk by Location Successfully.", Toast.LENGTH_SHORT);
            toast.show();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            errorHandler.sendEmptyMessage(ERROR_GENERIC);
        }
    }

    public void locationAction(View view) {
        Intent intent = new Intent();
        intent.setClass(PhotosChalkActivity.this, LocationEntryActivity.class);
        intent.putExtra("Location", activeAddress.getLocation());
        intent.putExtra("StreetNumber", activeAddress.getStreetNumber());
        intent.putExtra("StreetPrefix", activeAddress.getStreetPrefix());

        intent.putExtra("Direction", activeAddress.getDirection());
        startActivityForResult(intent, 0);
        return;
    }

    public void gpsAction(View view) {
        try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext()) || gpsTracker == null
                    || !gpsTracker.isGPSAvailable()) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT < 21) {

            GPSProgressBar.setVisibility(View.VISIBLE);
            UIHelper.toggleButtonState(gpsButton, false);

		/*old implementation add new ticket screen implementation
		gpsTracker.updateLocation(gpsLocation, new Receiver() {
			@Override
			public void onReceiveResult(Location location, Bundle resultData) {
				UIHelper.toggleButtonState(gpsButton, true);
				
				if (GPSProgressBar != null) {
					GPSProgressBar.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onReceiveResult(GPSLocation location, Bundle resultData) {
				if (location != null && (!location.getLocation().equals("") || !location.getStreetNumber().equals(""))) {
					activeAddress.setLocation(location.getLocation());
					activeAddress.setStreetNumber(location.getStreetNumber());
					activeAddress.setStreetPrefix("");
					activeAddress.setStreetSuffix("");
					activeAddress.setDirection("");
					activeChalk.setLongitude(location.getLongitude());
					activeChalk.setLatitude(location.getLatitude());
					activeChalk.setGpstime(new Date());
					isGPSLocation = true;
					UIHelper.toggleButtonState(gpsButton, true);
				}

				GPSHandler.sendEmptyMessage(0);
			}

			@Override
			public void onTimeout() {
				if (GPSProgressBar != null) {
					GPSProgressBar.setVisibility(View.GONE);
				}
				
				if(TPApp.isServiceAvailable){
					UIHelper.toggleButtonState(gpsButton, true);
				}else{
					UIHelper.toggleButtonState(gpsButton, false);
				}
			}
		});*/


            Receiver receiver = new Receiver() {
                @Override
                public void onReceiveResult(Location location, Bundle resultData) {
                    if (location != null) {
                        UIHelper.toggleButtonState(gpsButton, true);

                        if (GPSProgressBar != null) {
                            GPSProgressBar.setVisibility(View.VISIBLE);
                        }
                    }
                    //Toast.makeText(getApplicationContext(),"Location Details\n"+"Lat"+location.getLatitude() +"\nLong"+location.getLongitude()+"\nStreet",Toast.LENGTH_SHORT).show();

                    UIHelper.toggleButtonState(gpsButton, true);

                }

                @Override
                public void onReceiveResult(GPSLocation location, Bundle resultData) {
                    if (location != null && (!location.getLocation().equals("") || !location.getStreetNumber().equals(""))) {

                        String location1 = location.getLocation();
                        String[] split = location1.split(",");
                        activeAddress.setLocation(split[0]);
                        activeAddress.setStreetNumber(location.getStreetNumber());
                        activeAddress.setStreetPrefix("");
                        activeAddress.setStreetSuffix("");
                        activeAddress.setDirection("");
                        activeChalk.setLongitude(location.getLongitude());
                        activeChalk.setLatitude(location.getLatitude());
                        activeChalk.setGpstime(new Date());
                        isGPSLocation = true;
                        UIHelper.toggleButtonState(gpsButton, true);

                    }
                    //Toast.makeText(getApplicationContext(),"Location Details\n"+"Lat"+location.getLatitude() +"\nLong"+location.getLongitude()+"\nStreet"+location.getStreetNumber(),Toast.LENGTH_SHORT).show();

                    GPSHandler.sendEmptyMessage(0);

                    GPSProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onTimeout() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (GPSProgressBar != null) {
                                GPSProgressBar.setVisibility(View.GONE);
                            }
                            if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                                TPApp.isServiceAvailable = false;
                            }else {
                                TPApp.isServiceAvailable = true;
                            }
                            if (TPApp.isServiceAvailable) {
                                UIHelper.toggleButtonState(gpsButton, true);
                            } else {
                                UIHelper.toggleButtonState(gpsButton, false);
                            }
                        }
                    });
                }
            };


            if (gpsLocation == null || !gpsLocation.hasAccuracy()) {
                getGPSTracker().initService(receiver);
            } else {

                LocationManager locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);


                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        gpsLocation = location;

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);
                getGPSTracker().updateLocation(gpsLocation, receiver);
            }
        } else {
            findLoc();
        }
    }

    private GPSTracker getGPSTracker() {
        //Create GPS Tracker is it's not initialized
        if (gpsTracker == null) {
            gpsTracker = new GPSTracker(this);
        }

        return gpsTracker;
    }

    @Override
    public void onBackPressed() {
        backAction(null);
    }

    public void backAction(View view) {
        TPApp.setActiveChalk(null);
        finish();
    }

    public void selectLocation(View view) {
        Intent i = new Intent();
        i.setClass(PhotosChalkActivity.this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.SELECT_LOCATION_LIST);
        startActivityForResult(i, 0);
        return;
    }

    public void searchAction(View view) {
        Toast.makeText(getApplicationContext(), "Currently this functionality is not available.", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            TPApp.setActiveChalk(null);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void handleVoiceInput(String text) {

        if (text == null)
            return;
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        if (text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")) {
            backAction(null);
        } else if (text.contains("SAVE") || text.contains("SAVE CHALK") || text.contains("ADD CHALK")) {
            if (!photoChalkCheckbox.isSelected())
                addAction(null);
            else
                startChalking(null);
        } else if (text.contains("GPS")) {
            gpsAction(null);
        } else if (text.contains("PHOTO CHALK")) {
            photoChalkCheckbox.setSelected(true);
        } else if (text.contains("LOCATION")) {
            selectLocation(null);
        } else if (text.contains("VIEW LIST") || text.contains("VIEW CHALKS")) {
            viewListAction(null);
        }
    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        if (connected) {
            if (UIHelper.isGpsDeviceValue(TPApp.deviceId)) {
                UIHelper.toggleButtonState(gpsButton, false);
            } else {
                if (Feature.isFeatureAllowed(Feature.GPS)) {
                    gpsButton.setEnabled(true);
                    gpsButton.setBackgroundResource(R.drawable.btn_yellow);
                } else {
                    gpsButton.setEnabled(false);
                    gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                }
            }


            statusIndicatorImageView.setImageResource(R.drawable.green_status_btn_bk);
            if (!isFastConnection) {
                statusIndicatorImageView.setImageResource(R.drawable.yellow_status_btn);
            }
        } else {
            gpsButton.setEnabled(false);
            gpsButton.setBackgroundResource(R.drawable.btn_disabled);
            statusIndicatorImageView.setImageResource(R.drawable.gray_status_btn);
        }

        try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                gpsButton.setEnabled(false);
                gpsButton.setBackgroundResource(R.drawable.btn_disabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ticketLogsAction(View view) {
        Intent intent = new Intent(this, TicketLogsActivity.class);
        startActivity(intent);
    }

    @Override
    public void whereIAM(ADLocation data) {

        //String[] split = data.address.split(",");
        activeAddress.setLocation(data.address);
        activeAddress.setStreetNumber(data.streetNumber);
        activeChalk.setLongitude(String.valueOf(data.longi));
        activeChalk.setLatitude(String.valueOf(data.lat));
        activeAddress.setStreetPrefix("");
        activeAddress.setStreetSuffix("");
        activeAddress.setDirection("");
        activeChalk.setGpstime(new Date());

        isGPSLocation = true;

        if (activeAddress != null) {
            locationEditText.setText(TPUtility.getFullAddress(activeAddress));
        }

    }

    private void findLoc() {
        //new MyTracker(PhotosChalkActivity.this, this).track();
        new GetLocation(PhotosChalkActivity.this, this).track();
    }

    @Override
    public void onUserInteraction() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void initTimeOut() {
        try {
           /* if (!TPUtility.isTrafficInstallled(getPackageManager())) {
                return;
            }*/

            int activityDuration = 0;
            if (Feature.isFeatureAllowed(Feature.INACTIVITY_DURATION)) {
                try {
                    activityDuration = Integer.parseInt(Feature.getFeatureValue(Feature.INACTIVITY_DURATION));
                } catch (NumberFormatException e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }


                final int duration = activityDuration;
                countDownTimer = new CountDownTimer(duration * 60 * 1000, 5000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        displayInactivityMsg("Your device has been idle for " + duration + " minutes.");
                        _autoLogOut();
                    }
                };
            }
        } catch (NumberFormatException e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    //

    void _autoLogOut() {

        // int activityDuration = 30;
        int activityDuration = 0;
        if (Feature.isFeatureAllowed(Feature.INACTIVITY_AUTOLOGOUT)) {
            try {
                activityDuration = Integer.parseInt(Feature.getFeatureValue(Feature.INACTIVITY_AUTOLOGOUT));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            final int duration = activityDuration;
            countDownTimer1 = new CountDownTimer((duration * 60) * LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    countDownTimer1.cancel();
                    countDownTimer.cancel();
                    TPUtility.createTxtFile();
                    finishActivity();
                    log.debug("Call countdown t WriteActivity");
                }
            }.start();
        }
    }

    void finishActivity() {
        try {
            // Close Active User Session and Update Duty Report
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                        isServiceAvailable = false;
                    }else {
                        isServiceAvailable = true;
                    }
                    ((WriteTicketBLProcessor) screenBLProcessor).closeActiveDuty(isServiceAvailable);
                }
            }).start();

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(TPConstant.EXTRA_END_SHIFT, true);
            intent.setClass(PhotosChalkActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void displayInactivityMsg(final String message) {
        countDownTimer.cancel();

        // Return if activity is finishing
        if (isFinishing()) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Return if activity is finishing
                if (isFinishing()) {
                    return;
                }

                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(PhotosChalkActivity.this);
                confirmBuilder.setCancelable(false);
                confirmBuilder.setTitle("Alert").setMessage(message).setCancelable(true)
                        .setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                endShift1();
                                dialog.dismiss();
                            }
                        }).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        countDownTimer.start();
                        countDownTimer1.cancel();
                        dialog.dismiss();
                    }
                });

                AlertDialog confirmAlert = confirmBuilder.create();
                confirmAlert.show();
            }
        });
    }

    private void endShift1() {

        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_lock_power_off).setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            // Close Active User Session and Update Duty Report
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                                        isServiceAvailable = false;
                                    }else {
                                        isServiceAvailable = true;
                                    }
                                    ((WriteTicketBLProcessor) screenBLProcessor).closeActiveDuty(isServiceAvailable);
                                }
                            }).start();

                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(TPConstant.EXTRA_END_SHIFT, true);
                            intent.setClass(PhotosChalkActivity.this, HomeActivity.class);
                            startActivity(intent);
                            TPUtility.createTxtFile();
                            finish();

                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    }
                }).setNegativeButton("No", null).show();
    }


}
