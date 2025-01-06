package com.ticketpro.parking.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.location.LocationStatusCodes;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.ScanBarcodeActivity;
import com.ticketpro.exception.TPException;
import com.ticketpro.gpslibrary.ADLocation;
import com.ticketpro.gpslibrary.GetLatLongValue;
import com.ticketpro.gpslibrary.GetLocation;
import com.ticketpro.gpslibrary.MyTracker;
import com.ticketpro.lpr.web.AutoLPRActivity;
import com.ticketpro.lpr.web.LPRActivityScreen;
import com.ticketpro.model.Address;
import com.ticketpro.model.ChalkComment;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Color;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Feature;
import com.ticketpro.model.GPSLocation;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.Meter;
import com.ticketpro.model.MeterHandler;
import com.ticketpro.model.State;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.parking.R;
import com.ticketpro.parking.api.ChalkVehicleNetworkCalls;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.parking.bl.WriteTicketBLProcessor;
import com.ticketpro.parking.service.AlarmReceiver;
import com.ticketpro.parking.service.GPSResultReceiver.Receiver;
import com.ticketpro.parking.service.JobIntentServiceSaveChalk;
import com.ticketpro.parking.service.ParkingWorker;
import com.ticketpro.util.CallbackHandler;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.GPSTracker;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;
import com.ticketpro.util.UIHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class ChalkVehicleActivity extends BaseActivityImpl implements MyTracker.ADLocationListener, MeterHandler {

    final int ERROR_DATABASE = 1;
    final int ERROR_GENERIC = 2;
    final int ERROR_ADD_CHALK = 3;
    final int DATA_ERROR = 0;
    final int DATA_SUCCESSFULL = 1;
    final int REQUEST_LPR = 1;
    final int REQUEST_VIN_BARCODE = 2;
    final int REQUEST_METER_BARCODE = 3;
    final int REQUEST_TAKE_PICTURE = 4;
    final int REQUEST_LOOKUP = 5;
    boolean cStatus = false;
    boolean sStatus = false;
    ImageView cImage;
    ImageView sImage;
    boolean cDialogStatus = false;
    boolean sDialogStatus = false;
    ImageView cDialogImage;
    ImageView sDialogImage;
    Button pictureButton;
    int prevCX = -1;
    int prevCY = -1;
    int prevSX = -1;
    int prevSY = -1;
    private ImageView statusIndicatorImageView;
    private Spinner tireSpinner;
    private Spinner tireDialogSpinner;
    private Spinner durationSpinner;
    private String tireDisplayNames[] = {"Select Tire", "Front Left", "Front Right", "Back Left", "Back Right"};
    private ArrayList<String> durations;
    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private Handler errorHandler;
    private Dialog dialog;
    private EditText stateEditText;
    private EditText spaceEditText;
    private EditText plateNumberEditText;
    private EditText vinNumberEditText;
    private EditText tmEditText;
    private EditText makeEditText;
    private EditText colorEditText;
    private EditText locationEditText;
    private EditText meterNumberEditText;
    private boolean isGPSLocation = false;
    private Address activeAddress;
    private Button vnvButton;
    private Button gpsButton;
    private Button lprButton;
    private CheckBox stickyMarkersChk;
    private int cX = -1;
    private int cY = -1;
    private int sX = -1;
    private int sY = -1;
    private ChalkVehicle activeChalk;
    private GPSTracker gpsTracker;
    private Handler GPSHandler;
    private ProgressBar GPSProgressBar;
    private int dialerHeight;
    private int dialerWidth;
    private int currentAngle = 0;
    private Date chalkDate = null;
    private EditText focusedEditText;
    private SharedPreferences mPreferences;
    private RelativeLayout wheelLayout;
    private String imageChalkName;
    private EditText dialogInputText;
    private CountDownTimer countDownTimer1;
    private CountDownTimer countDownTimer;
    private  Meter historyMeter;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // Check if chalk feature is allowed or not
            if (!Feature.isFeatureAllowed(Feature.CHALK)) {
                Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                finish();
            }

            setContentView(R.layout.chalk_vehicle);
            setLogger(ChalkVehicleActivity.class.getName());
            setActiveScreen(this);
            isNetworkInfoRequired = true;

            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            stateEditText = (EditText) findViewById(R.id.chalk_state_textview);
            stateEditText.setTag("State");

            plateNumberEditText = (EditText) findViewById(R.id.chalk_plate_textview);
            plateNumberEditText.setTag("Plate");

            vinNumberEditText = (EditText) findViewById(R.id.chalk_vin_textview);
            vinNumberEditText.setTag("Vin");

            tmEditText = (EditText) findViewById(R.id.chalk_time_textview);
            tmEditText.setTag("Time");

            locationEditText = (EditText) findViewById(R.id.chalk_location_textview);
            locationEditText.setTag("Location");

            meterNumberEditText = (EditText) findViewById(R.id.chalk_meter_textview);
            meterNumberEditText.setTag("Meter");

            spaceEditText = (EditText) findViewById(R.id.chalk_space_textview);
            spaceEditText.setTag("Space");

            makeEditText = (EditText) findViewById(R.id.chalk_make_textview);
            makeEditText.setTag("Make");

            colorEditText = (EditText) findViewById(R.id.chalk_color_textview);
            colorEditText.setTag("Color");

            GPSProgressBar = (ProgressBar) findViewById(R.id.GPSProcess);
            lprButton = (Button) findViewById(R.id.chalk_lpr_btn);

            tireSpinner = (Spinner) findViewById(R.id.chalk_tire_spinner);
            pictureButton = (Button) findViewById(R.id.chalk_picture_btn);

            durationSpinner = (Spinner) findViewById(R.id.chalk_duration_spinner);
            durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    TPUtility.hideSoftKeyboard(ChalkVehicleActivity.this);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    TPUtility.hideSoftKeyboard(ChalkVehicleActivity.this);
                }
            });

            wheelLayout = (RelativeLayout) findViewById(R.id.chalk_vehicle_circle_panel);

            Button sliderMenu = (Button) findViewById(R.id.slider_menu_btn);
            sliderMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                    SlideoutActivity.prepare(ChalkVehicleActivity.this, R.id.chalk_root_layout, width);
                    startActivity(new Intent(ChalkVehicleActivity.this, ChalkMenuActivity.class));
                    overridePendingTransition(0, 0);
                }
            });

            plateNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasfocus) {
                    if (!hasfocus) {
                        plateNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);

                        String plate = plateNumberEditText.getText().toString();
                        String state = stateEditText.getText().toString();
                        if (plate.equals(""))
                            return;

                        plate = plate.toUpperCase();
                        plate = TPUtility.getValidPlate(plate);
                        plateNumberEditText.setText(plate);

                        if (!TPUtility.isValidPlateNumber(plate, state) || !StringUtil.isAlphaNumeric(plate)) {
                            TPUtility.showErrorToast(ChalkVehicleActivity.this, "Plate format is incorrect. Please validate it.");
                            plateNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                        }
                    } else {
                        plateNumberEditText.setBackgroundResource(R.drawable.text_field_focus_bkg);
                        focusedEditText = plateNumberEditText;
                    }
                }
            });

            plateNumberEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        // plateNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                        String plate = plateNumberEditText.getText().toString();
                        String state = stateEditText.getText().toString();
                        if (plate.isEmpty()) {
                            return false;
                        }

                        plate = plate.toUpperCase();
                        plate = TPUtility.getValidPlate(plate);
                        plateNumberEditText.setText(plate);

                        if (!TPUtility.isValidPlateNumber(plate, state) || !StringUtil.isAlphaNumeric(plate)) {
                            TPUtility.showErrorToast(ChalkVehicleActivity.this, "Plate format is incorrect. Please validate it.");
                            plateNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                        } else {
                            plateNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                        }

                        checkPlateHistory(plate);

                        TPUtility.hideSoftKeyboard(ChalkVehicleActivity.this);
                    }

                    return false;
                }
            });

            vinNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasfocus) {
                    if (!hasfocus) {
                        vinNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                        String vin = vinNumberEditText.getText().toString();
                        if (vin.equals("")) {
                            return;
                        }

                        vin = vin.toUpperCase();
                        vin = TPUtility.getValidVIN(vin);
                        vinNumberEditText.setText(vin);

                        String message = TPUtility.VINValidationMsg(vin);
                        if (!message.isEmpty()) {
                            TPUtility.showErrorToast(ChalkVehicleActivity.this, message);
                            vinNumberEditText.setBackgroundResource(R.drawable.text_field_error);

                        } else if (!StringUtil.isAlphaNumeric(vin)) {
                            TPUtility.showErrorToast(ChalkVehicleActivity.this, "VIN format is incorrect. Please validate it.");
                            vinNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                        }
                    } else {
                        vinNumberEditText.setBackgroundResource(R.drawable.text_field_focus_bkg);
                        focusedEditText = vinNumberEditText;
                    }
                }
            });

            vinNumberEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        // vinNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                        String vin = vinNumberEditText.getText().toString();
                        if (vin.isEmpty()) {
                            return false;
                        }

                        vin = vin.toUpperCase();
                        vin = TPUtility.getValidVIN(vin);
                        vinNumberEditText.setText(vin);

                        String message = TPUtility.VINValidationMsg(vin);
                        if (!message.equals("")) {
                            TPUtility.showErrorToast(ChalkVehicleActivity.this, message);
                            vinNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                        } else if (!StringUtil.isAlphaNumeric(vin)) {
                            TPUtility.showErrorToast(ChalkVehicleActivity.this, "VIN format is incorrect. Please validate it.");
                            vinNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                        } else {
                            vinNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                        }

                        TPUtility.hideSoftKeyboard(ChalkVehicleActivity.this);
                    }

                    return false;
                }
            });

            makeEditText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clearField(v);

                    activeChalk.setMake("");
                    activeChalk.setMakeCode("");
                    return true;
                }
            });

            colorEditText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clearField(v);

                    activeChalk.setColor("");
                    activeChalk.setColorCode("");
                    return true;
                }
            });

            // Reduce Font size to fit the 17 chars
            final float fontSize = vinNumberEditText.getTextSize();
            final int screenWidth = getResources().getDisplayMetrics().widthPixels;
            vinNumberEditText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    // Abstract Method of TextWatcher Interface.
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Abstract Method of TextWatcher Interface.
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int len = vinNumberEditText.getText().length();
                    if (len > 12 && screenWidth <= 480) {
                        vinNumberEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize - 5);
                    } else {
                        vinNumberEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
                    }
                }
            });

            if (Feature.isFeatureAllowed(Feature.METER_NUMERIC_ENTRY)) {
                meterNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }

            meterNumberEditText.setOnFocusChangeListener((v, hasfocus) -> {
                if (!hasfocus) {
                    meterNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);

                    String meter = meterNumberEditText.getText().toString();
                    meter = meter.toUpperCase();
                    meter = TPUtility.getValidMeter(meter);
                    meterNumberEditText.setText(meter);

                    if (TPApp.getUserSettings() != null && TPApp.getUserSettings().isAutoLookup()) {
                        if (!StringUtil.isEmpty(meter)) {
                            checkMeterHistory(meter);
                        }
                    }
                } else {
                    meterNumberEditText.setBackgroundResource(R.drawable.text_field_focus_bkg);
                    focusedEditText = meterNumberEditText;
                }
            });

            meterNumberEditText.setOnKeyListener((v, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    /// meterNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                    String meter = meterNumberEditText.getText().toString();
                    meter = meter.toUpperCase();
                    meter = TPUtility.getValidMeter(meter);
                    meterNumberEditText.setText(meter);
                    if (!meter.equals("")) {
                        checkMeterHistory(meter);
                    }
                }

                return false;
            });

            spaceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasfocus) {
                    if (!hasfocus) {
                        spaceEditText.setBackgroundResource(R.drawable.text_field_bkg);
                        String space = spaceEditText.getText().toString();
                        space = space.toUpperCase();
                        spaceEditText.setText(space);
                    } else {
                        spaceEditText.setBackgroundResource(R.drawable.text_field_focus_bkg);
                        focusedEditText = spaceEditText;
                    }
                }
            });

            spaceEditText.setOnKeyListener((v, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    // spaceEditText.setBackgroundResource(R.drawable.text_field_bkg);
                    String space = spaceEditText.getText().toString();
                    space = space.toUpperCase();
                    spaceEditText.setText(space);
                }

                return false;
            });

            pictureButton.setOnLongClickListener(v -> {
                Intent i = new Intent();
                i.setClass(ChalkVehicleActivity.this, PhotoChalkEditActivity.class);
                startActivityForResult(i, REQUEST_TAKE_PICTURE);
                return true;
            });
            // Check for Numeric Keypads
            if (Feature.isNumericKeypadRequired("meter")) {
                meterNumberEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            }

            if (Feature.isNumericKeypadRequired("plate")) {
                plateNumberEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            }

            if (Feature.isNumericKeypadRequired("vin")) {
                vinNumberEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            }

            if (Feature.isNumericKeypadRequired("space")) {
                spaceEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            }

            // Check for hidden fields
            if (Feature.isHiddenField("meter")) {
                meterNumberEditText.setVisibility(View.GONE);
                Button barcode = (Button) findViewById(R.id.chalk_meter_barcode_btn);
                barcode.setVisibility(View.GONE);
            }

            if (Feature.isHiddenField("plate")) {
                plateNumberEditText.setVisibility(View.GONE);
            }

            if (Feature.isHiddenField("space")) {
                spaceEditText.setVisibility(View.GONE);
            }

            if (Feature.isHiddenField("location")) {
                locationEditText.setVisibility(View.GONE);
            }

            if (Feature.isHiddenField("tm")) {
                tmEditText.setVisibility(View.GONE);
            }

            if (Feature.isHiddenField("vin")) {
                vinNumberEditText.setVisibility(View.GONE);
                Button barcode = (Button) findViewById(R.id.chalk_vin_barcode_btn);
                barcode.setVisibility(View.GONE);
            }

            if (Feature.isHiddenField("state")) {
                stateEditText.setVisibility(View.GONE);
            }

            // plateNumberEditText.setOnKeyListener(onKeyListener);
            // vinNumberEditText.setOnKeyListener(onKeyListener);
            // spaceEditText.setOnKeyListener(onKeyListener);
            // meterNumberEditText.setOnKeyListener(onKeyListener);
            locationEditText.setOnKeyListener(onKeyListener);

            // Show/Hide VNV Button
            vnvButton = (Button) findViewById(R.id.chalk_vnv_btn);
            if (!Feature.isFeatureAllowed(Feature.SHOW_VNV)) {
                vnvButton.setVisibility(View.GONE);
            }

            // Default State
            State defaultState = State.getDefaultState(this);
            if (defaultState != null) {
                stateEditText.setText(defaultState.getCode());
            }

            locationEditText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    locationEntryAction(v);
                    return true;
                }
            });

            plateNumberEditText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((EditText) v).setText("");
                    return true;
                }
            });

            meterNumberEditText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((EditText) v).setText("");
                    return true;
                }
            });

            vinNumberEditText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((EditText) v).setText("");
                    return true;
                }
            });

            spaceEditText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((EditText) v).setText("");
                    return true;
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

            tmEditText.setText(DateUtil.getCurrentTime());
            plateNumberEditText.clearFocus();
            vinNumberEditText.clearFocus();
            meterNumberEditText.clearFocus();
            tmEditText.clearFocus();

            activeAddress = TPUtility.createEmptyAddress();

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = (metrics.widthPixels / 2);

            // for high res device marker size issue
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
             * final int circleRadius=(width/2)-20; final int
             * circleRadiusX=circleRadius; final int circleRadiusY=circleRadius;
             * final int innerCircleRadius=circleRadius-60;
             */

            RelativeLayout chalkPanel = (RelativeLayout) findViewById(R.id.chalk_vehicle_circle_panel);
            chalkPanel.getLayoutParams().width = circleRadius * 2;
            chalkPanel.getLayoutParams().height = circleRadius * 2;

            cImage = (ImageView) findViewById(R.id.chalk_vehicle_c_img);
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

            sImage = (ImageView) findViewById(R.id.chalk_vehicle_s_img);
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

            // int wheelSize=circleRadius * 2;

            // Calculate default X and Y values
            RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cImage.getLayoutParams();
            cLayout.setMargins(0, wheelSize - 100, 0, 0);
            cImage.setLayoutParams(cLayout);

            RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sImage.getLayoutParams();
            sLayout.setMargins(wheelSize - 100, wheelSize - 100, 0, 0);
            sImage.setLayoutParams(sLayout);

            if (TPApp.getUserSettings() != null && TPApp.getUserSettings().isStickyMarker()) {
                sX = mPreferences.getInt(TPConstant.PREFS_KEY_STICKY_MARKER_S_X, -1);
                sY = mPreferences.getInt(TPConstant.PREFS_KEY_STICKY_MARKER_S_Y, -1);
                cX = mPreferences.getInt(TPConstant.PREFS_KEY_STICKY_MARKER_C_X, -1);
                cY = mPreferences.getInt(TPConstant.PREFS_KEY_STICKY_MARKER_C_Y, -1);
            }

            if (cX > -1 && cY > -1) {
                int x1 = (int) ((circleRadius - 20) * Math.cos(cX * Math.PI / 180) + circleRadiusX) - 20;
                int y1 = (int) ((circleRadius - 20) * Math.sin(cY * Math.PI / 180) + circleRadiusY) - 20;
                cLayout.setMargins(x1, y1, 0, 0);
                cLayout.setMarginStart(x1);
                cImage.setLayoutParams(cLayout);
                cImage.setVisibility(View.VISIBLE);
            } else {
                cImage.setVisibility(View.GONE);
            }

            if (sX > -1 && sY > -1) {
                int x2 = (int) (innerCircleRadius * Math.cos(sX * Math.PI / 180) + circleRadiusX) - 20;
                int y2 = (int) (innerCircleRadius * Math.sin(sY * Math.PI / 180) + circleRadiusY) - 20;
                sLayout.setMargins(x2, y2, 0, 0);
                if (x2 > circleRadius) {
                    sLayout.setMarginStart(x2 - 40);
                } else
                    sLayout.setMarginStart(x2);
                sImage.setLayoutParams(sLayout);
                sImage.setVisibility(View.VISIBLE);
            } else {
                sImage.setVisibility(View.GONE);
            }

            gpsButton = (Button) findViewById(R.id.chalk_gps_btn);
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

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChalkVehicleActivity.this, R.layout.simple_spinner_item, tireDisplayNames);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        tireSpinner.setAdapter(dataAdapter);

                        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(ChalkVehicleActivity.this, R.layout.simple_spinner_item, durationArray);
                        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        durationSpinner.setAdapter(dataAdapter2);
                    }

                    // Disable photos, GPS and LPR functionality on Emulators
                    try {

                        if (TPApp.getUserSettings() != null && TPApp.getUserSettings().getGps() != null && TPApp.getUserSettings().getGps().equals("N")) {

                            gpsButton.setEnabled(false);
                            gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                        }

                        gpsTracker = new GPSTracker(ChalkVehicleActivity.this);
                        if (!gpsTracker.isGPSAvailable()) {
                            gpsButton.setEnabled(false);
                            gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                        }
                        if (UIHelper.isGpsDeviceValue(TPApp.deviceId)) {
                            UIHelper.toggleButtonState(gpsButton, false);
                        } else {
                            if (!Feature.isFeatureAllowed(Feature.GPS)) {
                                gpsButton.setEnabled(false);
                                gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    statusIndicatorImageView = (ImageView) findViewById(R.id.status_indicator_image);
                    if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")) {
                        isServiceAvailable = false;
                    } else {
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
                        case 0:
                            bindDataAtLoadingTime(ERROR_GENERIC);
                            break;

                        case ERROR_GENERIC:
                            Toast.makeText(getBaseContext(), TPConstant.GENERIC_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                            break;

                        case ERROR_ADD_CHALK:
                            Toast.makeText(getBaseContext(), "Failed to add chalk details. Please try again.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            bindDataAtLoadingTime();

            activeChalk = TPApp.getActiveChalk();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    // onCreate Ends Here
    private void showTMPicker() {
        Calendar cal = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timePicker = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                chalkDate = new Date();
                chalkDate.setHours(hourOfDay);
                chalkDate.setMinutes(minute);

                if (chalkDate.getTime() > new Date().getTime()) {
                    TPUtility.showErrorToast(ChalkVehicleActivity.this, "You cannot set a future time.");

                    showTMPicker();
                    return;
                }

                tmEditText.setText(DateUtil.getTimeStringFromDate(chalkDate));
            }
        };

        TimePickerDialog pickerDialog = new TimePickerDialog(this, timePicker, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
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
            int x1 = (int) ((circleRadius - (int) (20 * metrics.density)) * Math.cos(cX * Math.PI / 180) + circleRadiusX) - (int) (20 * metrics.density);
            int y1 = (int) ((circleRadius - (int) (20 * metrics.density)) * Math.sin(cY * Math.PI / 180) + circleRadiusY) - (int) (20 * metrics.density);
            RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cDialogImage.getLayoutParams();
            cLayout.setMargins(x1, y1, 0, 0);
            cLayout.setMarginStart(x1);
            cDialogImage.setLayoutParams(cLayout);
        } else {
            RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cDialogImage.getLayoutParams();
            cLayout.setMargins(0, dialerHeight - (int) (55 * metrics.density), 0, 0);
            cDialogImage.setLayoutParams(cLayout);
        }

        if (sX > -1 && sY > -1) {
            int x2 = (int) (innerCircleRadius * Math.cos(sX * Math.PI / 180) + circleRadiusX) - (int) (20 * metrics.density);
            int y2 = (int) (innerCircleRadius * Math.sin(sY * Math.PI / 180) + circleRadiusY) - (int) (20 * metrics.density);
            RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sDialogImage.getLayoutParams();
            sLayout.setMargins(x2, y2, 0, 0);
            if (x2 > circleRadius) {
                sLayout.setMarginStart(x2 - 40);
            } else
                sLayout.setMarginStart(x2);
            sDialogImage.setLayoutParams(sLayout);
        } else {
            RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sDialogImage.getLayoutParams();
            sLayout.setMargins(dialerWidth - (int) (55 * metrics.density), dialerHeight - (int) (55 * metrics.density), 0, 0);
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
                cLayout.setMargins(0, dialerHeight - (int) (60 * metrics.density), 0, 0);
                cDialogImage.setLayoutParams(cLayout);

                RelativeLayout.LayoutParams sLayout = (RelativeLayout.LayoutParams) sDialogImage.getLayoutParams();
                sLayout.setMargins(dialerWidth - (int) (60 * metrics.density), dialerHeight - (int) (60 * metrics.density), 0, 0);
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
                int circleRadius = (width / 2) - 20;
                int circleRadiusX = circleRadius;
                int circleRadiusY = circleRadius;
                int innerCircleRadius = circleRadius - 60;
                int wheelSize = circleRadius * 2;

                if (cX > -1 && cY > -1) {
                    int x1 = (int) ((circleRadius - 20) * Math.cos(cX * Math.PI / 180) + circleRadiusX) - 20;
                    int y1 = (int) ((circleRadius - 20) * Math.sin(cY * Math.PI / 180) + circleRadiusY) - 20;
                    RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cImage.getLayoutParams();
                    cLayout.setMargins(x1, y1, 0, 0);
                    cLayout.setMarginStart(x1);
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
                    if (x2 > circleRadius) {
                        sLayout.setMarginStart(x2 - 40);
                    } else
                        sLayout.setMarginStart(x2);
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
                            * Math.cos(cX * Math.PI / 180) + circleRadiusX)
                            - (int) (20 * metrics.density);
                    int y1 = (int) ((circleRadius - (int) (20 * metrics.density))
                            * Math.sin(cY * Math.PI / 180) + circleRadiusY)
                            - (int) (20 * metrics.density);
                    RelativeLayout.LayoutParams cLayout = (RelativeLayout.LayoutParams) cDialogImage.getLayoutParams();
                    cLayout.setMargins(x1, y1, 0, 0);
                    cLayout.setMarginStart(x1);
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
                    if (x2 > circleRadius) {
                        sLayout.setMarginStart(x2 - 40);
                    } else if (x2 > circleRadius) {
                        sLayout.setMarginStart(x2 - 40);
                    } else
                        sLayout.setMarginStart(x2);
                    sDialogImage.setLayoutParams(sLayout);
                }

                return true;
            }
        });
    }

    private void checkPlateHistory(String plate) {
        if (plate == null || plate.equals(""))
            return;

        progressDialog = ProgressDialog.show(ChalkVehicleActivity.this, "", "Processing Plate Lookup...");
        try {
            String state = stateEditText.getText().toString();
            ChalkVehicle historyChalk = ChalkVehicle.searchPreviousChalkByPlate(plate, state);
            System.out.println("=========> process one ");
            if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")) {
                isServiceAvailable = false;
            } else {
                isServiceAvailable = true;
            }
            if (historyChalk == null && isServiceAvailable) {
                WriteTicketBLProcessor blp = new WriteTicketBLProcessor((TPApplication) getApplicationContext());
                historyChalk = blp.searchChalkHistory(plate, state);
            }

            System.out.println("=========> process 2 ");
            if (historyChalk != null) {
                StringBuffer msg = new StringBuffer();
                StringBuffer values = new StringBuffer();
                StringBuffer headText = new StringBuffer();
               /* msg.append("<style>body{color:#fff;} table{color:#fff;} td{vertical-align:top;}</style>");
                msg.append("<p>This vehicle was previously chalked on " + DateUtil.getStringFromDate2(historyChalk.getChalkDate()));

                msg.append("</p>");
                msg.append("<table>");*/

                headText.append("This vehicle was previously chalked on " + DateUtil.getStringFromDate2(historyChalk.getChalkDate()) + " \n \n");


                final ChalkVehicle chalk = historyChalk;
                Duration duration = Duration.getDurationById(historyChalk.getDurationId());

                if (duration == null) {
                    duration = new Duration();
                    duration.setDurationMinutes(0);
                    duration.setTitle("NA");
                }

                int durationMins = duration.getDurationMinutes();
                long milliseconds = (new Date().getTime() - historyChalk.getChalkDate().getTime());
                int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                int hours = (int) ((milliseconds / (1000 * 60 * 60)));
                String hrs = (hours < 10) ? ("0" + hours) : hours + "";
                String mins = (minutes < 10) ? ("0" + minutes) : minutes + "";

              /*  msg.append("<tr><td>Plate</td><td>:</td><td>" + StringUtil.getDisplayString(historyChalk.getPlate()));
                msg.append("</td></tr>");
                msg.append("<tr><td>VIN</td><td>:</td><td>" + StringUtil.getDisplayString(historyChalk.getVin()));
                msg.append("</td></tr>");
                msg.append("<tr><td>State</td><td>:</td><td>" + StringUtil.getDisplayString(historyChalk.getStateCode()));
                msg.append("</td></tr>");
                msg.append("<tr><td>Meter</td><td>:</td><td>" + StringUtil.getDisplayString(historyChalk.getMeter()));
                msg.append("</td></tr>");
                msg.append("<tr><td>Duration</td><td>:</td><td>" + StringUtil.getDisplayString(duration.getTitle()));
                msg.append("</td></tr>");
                msg.append("<tr><td>Elapsed</td><td>:</td><td>" + hrs + ":" + mins + " hrs/min");
                msg.append("</td></tr>");
                msg.append("<tr><td>Location</td><td>:</td><td>" + TPUtility.getFullAddress(historyChalk));
                msg.append("</td></tr>");
                msg.append("</table>");*/

                msg.append("Plate" + "\n");

                msg.append("VIN" + "\n");

                msg.append("State" + "\n");

                msg.append("Meter" + "\n");

                msg.append("Duration" + "\n");

                msg.append("Elapsed" + "\n");

                msg.append("Location" + "\n");

                values.append(":  " + StringUtil.getDisplayString(historyChalk.getPlate()) + "\n");

                values.append(":  " + StringUtil.getDisplayString(historyChalk.getVin()) + "\n");

                values.append(":  " + StringUtil.getDisplayString(historyChalk.getStateCode()) + "\n");

                values.append(":  " + StringUtil.getDisplayString(historyChalk.getMeter()) + "\n");

                values.append(":  " + StringUtil.getDisplayString(duration.getTitle()) + "\n");

                values.append(":   " + hrs + ":" + mins + " hrs/min" + "\n");

                values.append(":  " + TPUtility.getFullAddress(historyChalk) + "\n");

                /*WebView wv = new WebView(getBaseContext());
                wv.loadDataWithBaseURL(null,msg.toString(), "text/html", "utf-8",null);
                wv.setBackgroundColor(android.graphics.Color.TRANSPARENT);
                wv.getSettings().setJavaScriptEnabled(true);
                wv.getSettings().setDefaultTextEncodingName("utf-8");*/

                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_view, null);
                TextView headerTV = (TextView) view.findViewById(R.id.headerTV);
                TextView valueTV = (TextView) view.findViewById(R.id.valueTV);
                TextView headTV = (TextView) view.findViewById(R.id.headerTextTV);

                headTV.setVisibility(View.VISIBLE);

                headerTV.setText(msg.toString());
                valueTV.setText(values.toString());
                headTV.setText(headText.toString());

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setCancelable(false);
                //dialog.setView(wv);
                dialog.setTitle("Lookup Result");
                dialog.setView(view);
                dialog.setPositiveButton("OK", (dialog1, which) -> dialog1.dismiss());

                if ((milliseconds / (1000 * 60)) >= durationMins) {
                    dialog.setNegativeButton("Write Ticket", (dialog12, which) -> {
                        dialog12.dismiss();

                        Ticket ticket = TPApp.createTicketForChalk(chalk);
                        ticket.setPhoto_count(activeChalk.getChalkPictures().size());
                        TPApp.setActiveTicket(ticket);

                        Intent i = new Intent();
                        i.setClass(ChalkVehicleActivity.this, WriteTicketActivity.class);
                        i.putExtra("ChalkId", chalk.getChalkId());
                        startActivity(i);
                        plateNumberEditText.setText("");
                    });
                }
                System.out.println("=========> process end ");
                dialog.show();
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void checkMeterHistory(String meter) {
        if (meter == null || meter.equals(""))
            return;

        if (!Feature.isFeatureAllowed(Feature.LOOKUP_METER))
            return;

        progressDialog = ProgressDialog.show(this, "", "Processing Meter Lookup...");
        try {
            historyMeter = Meter.searchMeterHistory(meter);
            if (historyMeter == null) {
                isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                if (isServiceAvailable) {
                    WriteTicketBLProcessor blp = new WriteTicketBLProcessor((TPApplication) getApplicationContext());
                    historyMeter = blp.searchMeterHistory(meter);
                    //blp.searchMeterHistory1(meter,this);
                }
            }

            if (historyMeter == null) {
                displayErrorMessage("Meter record not found.");
                progressDialog.dismiss();
                return;
            }
                activeAddress.setDirection(historyMeter.getDirection());
                activeAddress.setStreetNumber(historyMeter.getStreetNumber());
                activeAddress.setStreetPrefix(historyMeter.getStreetPrefix());
                activeAddress.setStreetSuffix(historyMeter.getStreetSuffix());
                activeAddress.setLocation(historyMeter.getLocation());
                locationEditText.setText(TPUtility.getFullAddress(activeAddress));


        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void bindDataAtLoadingTime() {
        bindDataAtLoadingTime(0);
    }

    public void bindDataAtLoadingTime(final int reloadCount) {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread() {
            public void run() {
                ChalkBLProcessor blProcessor = new ChalkBLProcessor((TPApplication) getApplicationContext());
                try {
                    durations = blProcessor.getDurations();
                    dataLoadingHandler.sendEmptyMessage(DATA_SUCCESSFULL);
                } catch (TPException e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                    errorHandler.sendEmptyMessage(reloadCount);
                }
            }
        }.start();
    }

    /*
     * Handling all the click events
     */
    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();

       // __startChalkWork();
        initTimeOut();
        Button chkButton = (Button) findViewById(R.id.chalk_comments_btn);
        if (activeChalk != null && chkButton != null) {
            chkButton.setText("Comments(" + activeChalk.getComments().size() + ")");
        }
        pictureButton.setText("Pictures (" + activeChalk.getChalkPictures().size() + ")");

        tmEditText.setText(DateUtil.getCurrentTime());
        TPUtility.hideSoftKeyboard(this);

        try {
            if (countDownTimer != null) {
                countDownTimer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void __startChalkWork() {
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(ParkingWorker.class,15, TimeUnit.MINUTES).setConstraints(constraints).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("chalk-log-service", ExistingPeriodicWorkPolicy.REPLACE, work);
        // Utility.appendLog("1. Schedule Job");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        tmEditText.setText(DateUtil.getCurrentTime());

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_LPR:
                    if (data.hasExtra("PlateNumber")) {
                        String plate = data.getStringExtra("PlateNumber");
                        plateNumberEditText.setText(plate);

                        checkPlateHistory(plate);
                    }

                    if (data.hasExtra("State")) {
                        String state = data.getStringExtra("State");
                        State st = State.getStateByName(state);
                        if (st != null) {
                            stateEditText.setText(st.getCode());
                        }
                    }
                    try {
                        if (data.hasExtra("Make")) {
                            makeEditText.setText(data.getStringExtra("Make"));
                            activeChalk.setMake(data.getStringExtra("Make"));
                            MakeAndModel make = MakeAndModel.getMakeByTitle(data.getStringExtra("Make"));
                            if (make != null) {
                                activeChalk.setMakeCode(make.getMakeCode());
                            }
                        }

                        if (data.hasExtra("Color")) {
                            colorEditText.setText(data.getStringExtra("Color"));
                            activeChalk.setColor(data.getStringExtra("Color"));
                            Color color = Color.getColorByTitle(data.getStringExtra("Color"));
                            if (color != null) {
                                activeChalk.setColorCode(color.getCode());
                            }
                            //activeChalk.setColorCode(data.getStringExtra("ColorCode"));
                        }
					/*if (data.hasExtra("Body")) {
						body.setText(data.getStringExtra("Body"));
						Body body = Body.getBodyByTitle(data.getStringExtra("Body"));
						if (body != null) {
							activeChalk.set
							bodyTitle = body.getTitle();
							bodyCode = body.getCode();
							bodyId = body.getId();
						}

					}*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (data.hasExtra("Color")) {
                        colorEditText.setText(data.getStringExtra("Color"));
                    }

				/*if (data.hasExtra("Body")) {
					Body.setText(data.getStringExtra("Body"));
				}*/

                    if (data.hasExtra("Make")) {
                        makeEditText.setText(data.getStringExtra("Make"));
                    }
                    if (data.hasExtra("PlateImageFile")) {

                        DeviceInfo deviceInfo = TPApp.getDeviceInfo();
                        long photoNumber = 0;//deviceInfo.getCurrentPhotoNumber()+1;
                        //long citeNumber = deviceInfo.getCurrentCitationNumber() + 1;
                        long citeNumber = 0;//deviceInfo.getCurrentCitationNumber();
                        String filename = "";
                        File dstFile = null;
                        boolean updateDevicePhotoCount = false;
                        if (activeChalk.isLPRCaptured()) {
                            try {
                                if (activeChalk.getChalkPictures().size() > 0) {
                                    for (int i = 0; i < activeChalk.getChalkPictures().size(); i++) {
                                        ChalkPicture tp = activeChalk.getChalkPictures().get(i);
                                        if (tp.getImagePath() != null && tp.getImagePath().contains("LPR")) {
                                            dstFile = new File(tp.getImagePath());
                                            if (dstFile.exists()) {
                                                dstFile.delete();
                                                updateDevicePhotoCount = false;
                                            }
                                            break;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            photoNumber = deviceInfo.getCurrentPhotoNumber() + 1;
                            filename = "CHALK-LPR-" + photoNumber + ".JPG";
                            imageChalkName = filename;
                            dstFile = new File(TPUtility.getChalksFolder(), filename);
                            updateDevicePhotoCount = true;

                        }
                        File srcFile = new File(Objects.requireNonNull(data.getStringExtra("PlateImageFile")));
                        String resolution = data.getStringExtra("Resolution");
                        String imageSize = data.getStringExtra("ImageSize");

                        try {
                            dstFile.createNewFile();
                            TPUtility.copy(srcFile, dstFile, true);

                            ChalkPicture chalkPicture = new ChalkPicture();
                            chalkPicture.setChalkDate(new Date());
                            chalkPicture.setChalkId(activeChalk.getChalkId());
                            chalkPicture.setImagePath(dstFile.getAbsolutePath());
                            chalkPicture.setImageResolution(resolution);
                            chalkPicture.setImgName(imageChalkName);
                            chalkPicture.setSyncStatus("Pending");
                            chalkPicture.setLocation(locationEditText.getText().toString());
                            chalkPicture.setCustId(TPApp.custId);
                            chalkPicture.setImageSize(imageSize);


                            // Update or Add LPR image
                            if (activeChalk.isLPRCaptured()) {
                                for (int i = 0; i < activeChalk.getChalkPictures().size(); i++) {
                                    ChalkPicture picture = activeChalk.getChalkPictures().get(i);
                                    if (picture.getImagePath() != null && picture.getImagePath().contains("LPR")) {
                                        activeChalk.getChalkPictures().set(i, chalkPicture);
                                        break;
                                    }
                                }
                            } else {
                                activeChalk.getChalkPictures().add(chalkPicture);
                            }

                            try {
                                if (updateDevicePhotoCount) {
                                    deviceInfo.setCurrentPhotoNumber(photoNumber);
                                    DeviceInfo.insertDeviceInfo(deviceInfo);
                                    /*DatabaseHelper.getInstance().openWritableDatabase();
                                    DatabaseHelper.getInstance().insertOrReplace(deviceInfo.getContentValues(), "devices");
                                    DatabaseHelper.getInstance().closeWritableDb();*/
                                }

                            } catch (Exception e) {
                                log.error(TPUtility.getPrintStackTrace(e));
                            }
                            // Set LPR true
                            activeChalk.setLPRCaptured(true);

                            try {
                                //Button pictureButton = (Button) findViewById(R.id.chalk_picture_btn);
                                pictureButton.setText("Pictures (" + activeChalk.getChalkPictures().size() + ")");
                            } catch (Exception e) {
                                Log.d("Exception", TPUtility.getPrintStackTrace(e));
                            }

                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    }


                    break;

                case REQUEST_METER_BARCODE: {
                    String text = data.getStringExtra(Intents.Result.TEXT);
                    String type = data.getStringExtra(Intents.Result.TYPE);
                    // String format=data.getStringExtra(Intents.Result.FORMAT);
                    // String
                    // displayText=data.getStringExtra(Intents.Result.DISPLAY_TEXT);

                    if (type != null && !(type.equals("TEXT") || type.equals("PRODUCT"))) {
                        Toast.makeText(this, "Invalid Data. Please try again.", Toast.LENGTH_LONG).show();

                    } else if (text != null && !text.equals("")) {
                        meterNumberEditText.setText(text);
                        checkMeterHistory(text);
                    }

                    break;
                }

                case REQUEST_VIN_BARCODE: {
                    String text = data.getStringExtra(Intents.Result.TEXT);
                    String type = data.getStringExtra(Intents.Result.TYPE);
                    // String format=data.getStringExtra(Intents.Result.FORMAT);
                    // String
                    // displayText=data.getStringExtra(Intents.Result.DISPLAY_TEXT);

                    if (type != null && !(type.equals("TEXT") || type.equals("PRODUCT"))) {
                        Toast.makeText(this, "Invalid Data. Please try again.", Toast.LENGTH_LONG).show();

                    } else if (text != null && !text.equals("")) {
                        vinNumberEditText.setText(text);
                    }

                    break;
                }

                case REQUEST_TAKE_PICTURE: {
                    try {
                        //Button pictureButton = (Button) findViewById(R.id.chalk_picture_btn);
                        pictureButton.setText("Pictures (" + activeChalk.getChalkPictures().size() + ")");
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    break;
                }

                default:
                    if (data.hasExtra("STATE")) {
                        stateEditText.setText(data.getStringExtra("STATE"));
                    }

                    if (data.hasExtra("MAKE")) {
                        makeEditText.setText(data.getStringExtra("MAKE"));
                        activeChalk.setMake(data.getStringExtra("MAKE"));
                        activeChalk.setMakeCode(data.getStringExtra("MakeCode"));
                        if (colorEditText.getText().toString().isEmpty() && isAutoPromptVehicle()) {
                            selectColor(colorEditText.getRootView());
                        }
                    }

                    if (data.hasExtra("COLOR")) {
                        colorEditText.setText(data.getStringExtra("COLOR"));
                        activeChalk.setColor(data.getStringExtra("COLOR"));
                        activeChalk.setColorCode(data.getStringExtra("ColorCode"));
                        if (makeEditText.getText().toString().isEmpty() && isAutoPromptVehicle()) {
                            selectMake(makeEditText.getRootView());
                        }
                    }

                    if (data.hasExtra("Location")) {
                        activeAddress.setLocation(data.getStringExtra("Location"));
                        activeAddress.setStreetNumber(data.getStringExtra("StreetNumber"));
                        activeAddress.setStreetPrefix(data.getStringExtra("StreetPrefix"));
                        activeAddress.setStreetSuffix(data.getStringExtra("StreetSuffix"));
                        activeAddress.setDirection(data.getStringExtra("Direction"));
                        locationEditText.setText(TPUtility.getFullAddress(activeAddress));
                        activeChalk.setIsGPSLocation("Y");
                        isGPSLocation = false;

                        if (GPSProgressBar != null) {
                            GPSProgressBar.setVisibility(View.GONE);
                        }

                        // Redirect to Location Entry Form
                        if (data.hasExtra("REDIRECT_LOCATION_FORM") && !TPApp.getUserSettings().isSkipLocationEntry()) {
                            locationEntryAction(null);
                        }
                    }

                    if (data.hasExtra("Comment")) {
                        if (activeChalk.getComments().size() >= 6) {
                            displayErrorMessage("Exceeded max comments limit.");
                            return;
                        }

                        boolean isPrivate = data.getBooleanExtra("PrivateComment", false);
                        int commentId = data.getIntExtra("CommentId", 0);
                        String comment = data.getStringExtra("Comment");

                        if (isPrivate) {
                            if (isDuplicatePrivateComment(commentId)) {
                                displayErrorMessage("Private comment for this violation is already exists.");
                                return;
                            }
                        }

                        if (isDuplicateComment(activeChalk.getComments(), commentId)) {
                            displayErrorMessage("Selected comment already exists. Please select another.");
                            return;
                        }

                        ChalkComment chalkcomment = new ChalkComment();
                        chalkcomment.setChalkId(activeChalk.getChalkId());
                        chalkcomment.setComment(comment);
                        chalkcomment.setCommentId(commentId);
                        chalkcomment.setIsPrivate(isPrivate ? "Y" : "N");
                        chalkcomment.setCustId(TPApp.getCustId());

                        activeChalk.getComments().add(chalkcomment);
                    }
            }
        }

        TPUtility.hideSoftKeyboard(this);
    }

    private boolean isDuplicateComment(List<ChalkComment> commentList, int commentId) {
        boolean result = false;
        for (ChalkComment chalkComment : commentList) {
            if (chalkComment.getCommentId() == commentId) {
                return true;
            }
        }

        return result;
    }

    private boolean isDuplicatePrivateComment(int commentId) {
        boolean result = false;

        List<ChalkComment> commentList = activeChalk.getComments();
        for (ChalkComment chalkComment : commentList) {
            if (chalkComment.isPrivateComment()) {
                return true;
            }
        }

        return result;
    }

    public void viewListAction(View view) {
        Intent i = new Intent();
        i.setClass(this, ChalkListActivity.class);
        startActivity(i);
        return;
    }

    private void updatePreviewSize(Intent intent) {
        int minWidth = 480;
        int scrWidth = getWindowManager().getDefaultDisplay().getWidth();
        int scrHeight = getWindowManager().getDefaultDisplay().getHeight();
        if (scrWidth < scrHeight) {
            int temp = scrHeight;
            scrHeight = scrWidth;
            scrWidth = temp;
        }

        // Calculate Preview Width
        int width = scrWidth;
        if (width > minWidth) {
            width = scrWidth - ((width * 20) / 100);
            if (width > minWidth) {
                scrWidth = width;
            }
        }

        intent.putExtra(Intents.Scan.WIDTH, scrWidth);
        intent.putExtra(Intents.Scan.HEIGHT, scrHeight / 2);
    }

    public void vinBarcodeAction(View view) {
        try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TPUtility.isN5ServiceAvailable(this)) {
            processBarcode(new CallbackHandler() {
                @Override
                public void success(String data) {
                    vinNumberEditText.setText(data);
                }

                @Override
                public void failure(String message) {
                }
            });
        } else {
            Intent i = new Intent();
            i.putExtra(Intents.Scan.SOURCE, Intents.Scan.EXTRA_SOURCE_INTEGRATED);
            updatePreviewSize(i);

            i.setClass(this, ScanBarcodeActivity.class);
            startActivityForResult(i, REQUEST_VIN_BARCODE);
        }
    }

    public void VinNotVisibleAction(View view) {
        String vinText = vinNumberEditText.getText().toString();
        if (vinText.length() > 0)
            return;

        String vinStr = Feature.getFeatureValue(Feature.SHOW_VNV);
        if (vinStr == null)
            vinStr = "";

        vinNumberEditText.setText(vinStr);
    }

    public void meterBarcodeAction(View view) {
        try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TPUtility.isN5ServiceAvailable(this)) {
            processBarcode(new CallbackHandler() {
                @Override
                public void success(String data) {
                    meterNumberEditText.setText(data);
                    checkMeterHistory(data);
                }

                @Override
                public void failure(String message) {
                }
            });
        } else {
            Intent i = new Intent();
            i.putExtra(Intents.Scan.SOURCE, Intents.Scan.EXTRA_SOURCE_INTEGRATED);
            updatePreviewSize(i);
            i.setClass(this, ScanBarcodeActivity.class);
            startActivityForResult(i, REQUEST_METER_BARCODE);
        }
    }

    @SuppressWarnings("unchecked")
    public void addChalkAction(View view) {
        try {
            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ignored) {
            }
            if (gps_enabled) {
                GetLatLongValue getLatLongValue = new GetLatLongValue(ChalkVehicleActivity.this, new MyTracker.ADLocationListener() {
                    @Override
                    public void whereIAM(ADLocation loc) {
                        if (loc != null) {
                            activeChalk.setLatitude(String.valueOf(loc.lat));
                            activeChalk.setLongitude(String.valueOf(loc.longi));
                        } else {
                            activeChalk.setLatitude("");
                            activeChalk.setLongitude("");
                        }
                    }
                });
                getLatLongValue.track();
            }

            activeChalk.setLPRCaptured(false);
            String state = stateEditText.getText().toString();
            String plate = plateNumberEditText.getText().toString();
            String space = spaceEditText.getText().toString();
            String meter = meterNumberEditText.getText().toString();
            String location = locationEditText.getText().toString();
            String vin = vinNumberEditText.getText().toString();

            stateEditText.setBackgroundResource(R.drawable.text_field_bkg);
            plateNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
            String duration = (String) durationSpinner.getSelectedItem();

            vin = TPUtility.getValidVIN(vin);
            vinNumberEditText.setText(vin);

            plate = TPUtility.getValidPlate(plate);
            plateNumberEditText.setText(plate);

            meter = TPUtility.getValidMeter(meter);
            meterNumberEditText.setText(meter);

            // Check for required Fields
            StringBuffer requiredFields = new StringBuffer();
            boolean requiredFlag = false;

            if ((Feature.isFeatureAllowed(Feature.STATE_REQUIRED) || Feature.isRequiredField("state")) && state.equals("")) {
                requiredFields.append("- State\n");
                stateEditText.setBackgroundResource(R.drawable.text_field_error);
                requiredFlag = true;
            }

            if (plate.equals("") && vin.equals("")) {
                requiredFields.append("- VIN Number\n");
                requiredFields.append("- Plate Number\n");
                plateNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                vinNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                requiredFlag = true;
            }

            if ((Feature.isFeatureAllowed(Feature.SPACE_REQUIRED) || Feature.isRequiredField("space")) && space.equals("")) {
                requiredFields.append("- Space\n");
                spaceEditText.setBackgroundResource(R.drawable.text_field_error);
                requiredFlag = true;
            }

            if ((Feature.isFeatureAllowed(Feature.LOCATION_REQUIRED) || Feature.isRequiredField("location")) && location.equals("")) {
                requiredFields.append("- Location\n");
                locationEditText.setBackgroundResource(R.drawable.text_field_error);
                requiredFlag = true;
            }

            if ((Feature.isFeatureAllowed(Feature.METER_REQUIRED) || Feature.isRequiredField("meter")) && meter.equals("")) {
                requiredFields.append("- Meter\n");
                meterNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                requiredFlag = true;
            }

            if (duration.equals("ZONE")) {
                requiredFields.append("- Zone\n");
                requiredFlag = true;
            }

            if (requiredFlag) {
                displayErrorMessage("Please complete required fields below - \n\n" + requiredFields.toString());
                return;
            }

            if (plate.equalsIgnoreCase("NOPLATE") || plate.equalsIgnoreCase("VIN")) {
                TPUtility.showErrorToast(ChalkVehicleActivity.this, "Plate format is incorrect. Please validate it.");
                plateNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                return;
            }

            if (plate.equals("") && !TPUtility.ChalkVINValidate(vin, false)) {
                displayErrorMessage("VIN should be either 11 or all 17 characters.");
                vinNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                return;
            }

            if (!plate.equals("") && !TPUtility.ChalkVINValidate(vin, true)) {
                displayErrorMessage("VIN should be either the last 4 or all 17 characters.");
                vinNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                return;
            }

            int durationId = Duration.getDurationIdByName(duration);
            int durationMins = Duration.getDurationMinsById(durationId, this);

            String tireString = (String) tireSpinner.getSelectedItem();
            activeChalk.setTire(tireString.equalsIgnoreCase("Select Tire") ? "" : tireString);
            activeChalk.setChalkx(cX);
            activeChalk.setChalky(cY);
            activeChalk.setStemx(sX);
            activeChalk.setStemy(sY);
            activeChalk.setMeter(meter);
            activeChalk.setPlate(plate);
            activeChalk.setStateId(State.getStateIdByName(state));
            activeChalk.setSpace(space);
            activeChalk.setChalkType(TPConstant.CHALK_TYPE_PLATE);
            activeChalk.setDurationId(durationId);
            activeChalk.setVin(vin);
            activeChalk.setChalkDate((chalkDate == null) ? new Date() : chalkDate);
            activeChalk.setCustId(TPApp.getCustId());

            activeChalk.setExpiration(TPUtility.addMinutesToDate(durationMins, activeChalk.getChalkDate()));

            // Update Wheel Size
            activeChalk.setWheelSize(dialerWidth);

            // Location Notification
            long currentTime = System.currentTimeMillis();
            long expirationTime = currentTime + (durationMins * 60 * 1000);

            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("Title", "Chalk Expiration");
            intent.putExtra("Message", "Chalk for plate " + plate + " has expired");
            intent.putExtra("ChalkId", activeChalk.getChalkId());
            intent.putExtra("Type", "Chalk");

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) currentTime, intent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, expirationTime, pendingIntent);

            // Update GPS Location
            if (gpsTracker != null) {
                if (GPSProgressBar != null) {
                    GPSProgressBar.setVisibility(View.GONE);
                }

                if (gpsLocation != null) {
                    activeChalk.setLatitude(gpsLocation.getLatitude() + "");
                    activeChalk.setLongitude(gpsLocation.getLongitude() + "");
                }

				/*gpsTracker.initService(new Receiver() {
					@Override
					public void onReceiveResult(Location location, Bundle resultData) {
						if (location != null) {
							activeChalk.setLatitude(location.getLatitude() + "");
							activeChalk.setLongitude(location.getLongitude() + "");
						}

						if (GPSProgressBar != null) {
							GPSProgressBar.setVisibility(View.GONE);
						}
					}

					@Override
					public void onReceiveResult(GPSLocation location, Bundle resultData) {
					}
				});*/
            }

            activeChalk.setLocation(activeAddress.getLocation());
            activeChalk.setStreetNumber(activeAddress.getStreetNumber());
            activeChalk.setStreetPrefix(activeAddress.getStreetPrefix());
            activeChalk.setStreetSuffix(activeAddress.getStreetSuffix());
            activeChalk.setDirection(activeAddress.getDirection());
            activeChalk.setIsGPSLocation(isGPSLocation ? "Y" : "N");
            activeChalk.setDurationCode(Duration.getDurationTitleUsingId(activeChalk.getDurationId()));
            activeChalk.setSyncStatus("P");

            Completable completable = ChalkVehicle.insertChalkVehicle(activeChalk);
            completable.subscribe();
            /*DatabaseHelper.getInstance().openWritableDatabase();
            DatabaseHelper.getInstance().insertOrReplace(activeChalk.getContentValues(), "chalk_vehicles");*/

            ArrayList<SyncData> syncList = new ArrayList<SyncData>();

            // Insert Chalk Comments
            for (ChalkComment comment : activeChalk.getComments()) {
                ChalkComment.insertChalkComment(comment).subscribe();
                //DatabaseHelper.getInstance().insertOrReplace(comment.getContentValues(), "chalk_comments");
                SyncData syncData = new SyncData();
                syncData.setActivity("INSERT");
                syncData.setPrimaryKey(comment.getChalkCommentId() + "");
                syncData.setActivityDate(new Date());
                syncData.setCustId(TPApp.getCustId());
                syncData.setTableName(TPConstant.TABLE_CHALK_COMMENTS);
                syncData.setStatus("Pending");
                //SyncData.insertSyncData(syncData).subscribe();
                //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                //syncList.add(syncData);
            }

            SyncData syncData = new SyncData();
            syncData.setActivity("INSERT");
            syncData.setPrimaryKey(activeChalk.getChalkId() + "");
            syncData.setActivityDate(new Date());
            syncData.setCustId(TPApp.getCustId());
            syncData.setTableName(TPConstant.TABLE_CHALKS);
            syncData.setStatus("Pending");
            //SyncData.insertSyncData(syncData).subscribe();
            //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
            //syncList.add(syncData);

            ArrayList<ChalkPicture> chalkPictures = activeChalk.getChalkPictures();
            //if (!Feature.isSystemFeatureAllowed(Feature.PHOTO_PURGE)) {
            for (ChalkPicture picture : chalkPictures) {
                try {
                    //picture.setPictureId(ChalkPicture.getNextPrimaryId());
                    picture.setLocation(activeChalk.getLocation());
                    if (gpsTracker != null && gpsTracker.getLastLocation() != null) {
                        picture.setLatitude(gpsTracker.getLastLocation().getLatitude() + "");
                        picture.setLongitude(gpsTracker.getLastLocation().getLongitude() + "");
                    }

                    ChalkPicture.insertChalkPicture(picture).subscribe();
                    //DatabaseHelper.getInstance().insertOrReplace(picture.getContentValues(), "chalk_pictures");
                    if (!Feature.isSystemFeatureAllowed(Feature.PHOTO_PURGE)) {
                        SyncData syncPicture = new SyncData();
                        syncPicture.setActivity("INSERT");
                        syncPicture.setPrimaryKey(picture.getPictureId() + "");
                        syncPicture.setActivityDate(new Date());
                        syncPicture.setCustId(TPApp.getCustId());
                        syncPicture.setTableName(TPConstant.TABLE_CHALK_PICTURES);
                        syncPicture.setStatus("Pending");

                        //SyncData.insertSyncData(syncPicture).subscribe();
                        //DatabaseHelper.getInstance().insertOrReplace(syncPicture.getContentValues(), "sync_data");
                        syncList.add(syncPicture);
                    }

                } catch (Exception e) {
                    Log.e(TPConstant.TAG, TPUtility.getPrintStackTrace(e));
                }
            }
            isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");

            if (isServiceAvailable) {
                try {
                    //ChalkVehicleNetworkCalls.saveChalk(syncList);
                    Intent serviceIntent = new Intent(ChalkVehicleActivity.this, JobIntentServiceSaveChalk.class);
                    JobIntentServiceSaveChalk.enqueueWork(ChalkVehicleActivity.this, serviceIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                /* AsyncTask<ArrayList<SyncData>, Void, Boolean> saveTask = new AsyncTask<ArrayList<SyncData>, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(ArrayList<SyncData>... params) {
                        ArrayList<SyncData> syncData = params[0];
                        if (syncData != null) {
                            try {
                                ChalkBLProcessor blProcessor = new ChalkBLProcessor((TPApplication) getApplicationContext());
                                blProcessor.syncData(syncData, ChalkVehicleActivity.this);
                                return true;
                            } catch (TPException e) {
                                log.error(TPUtility.getPrintStackTrace(e));
                            }
                        }
                        return false;
                    }
                };
                saveTask.execute(syncList); */
            }

            Toast.makeText(this, "Added Chalk Successfully.", Toast.LENGTH_SHORT).show();

            // Default State
            State defaultState = State.getDefaultState(this);
            if (defaultState != null) {
                stateEditText.setText(defaultState.getCode());
            }

            plateNumberEditText.setText("");
            meterNumberEditText.setText("");
            vinNumberEditText.setText("");
            tmEditText.setText(DateUtil.getCurrentTime());
            makeEditText.setText("");
            colorEditText.setText("");

            if (!(Feature.isFeatureAllowed(Feature.STICKY_SPACE) || Feature.isStickyField("space")))
                spaceEditText.setText("");

            if (!(Feature.isFeatureAllowed(Feature.STICKY_LOCATION) || Feature.isStickyField("location"))) {
                locationEditText.setText("");
                activeAddress.setLocation("");
                activeAddress.setStreetNumber("");
                activeAddress.setStreetPrefix("");
                activeAddress.setStreetSuffix("");
                activeAddress.setDirection("");
            }

            pictureButton = (Button) findViewById(R.id.chalk_picture_btn);
            pictureButton.setText("Pictures(0)");

            Button chkButton = (Button) findViewById(R.id.chalk_comments_btn);
            chkButton.setText("Comments(0)");

            plateNumberEditText.requestFocus();
            plateNumberEditText.setBackgroundResource(R.drawable.text_field_focus_bkg);
            meterNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
            locationEditText.setBackgroundResource(R.drawable.text_field_bkg);
            vinNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);

            // Create New Chalk
            activeChalk = TPApp.createNewChalk();
            activeChalk.setLocation(activeAddress.getLocation());
            activeChalk.setStreetNumber(activeAddress.getStreetNumber());
            activeChalk.setStreetPrefix(activeAddress.getStreetPrefix());
            activeChalk.setStreetSuffix(activeAddress.getStreetSuffix());
            activeChalk.setDirection(activeAddress.getDirection());

            if (!(TPApp.getUserSettings() != null && TPApp.getUserSettings().isStickyMarker())) {
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
            return;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            errorHandler.sendEmptyMessage(ERROR_GENERIC);
        }
    }

    public void locationEntryAction(View view) {
        Intent intent = new Intent();
        intent.setClass(ChalkVehicleActivity.this, LocationEntryActivity.class);
        intent.putExtra("Location", activeAddress.getLocation());
        intent.putExtra("StreetNumber", activeAddress.getStreetNumber());
        intent.putExtra("StreetPrefix", activeAddress.getStreetPrefix());
        intent.putExtra("StreetSuffix", activeAddress.getStreetSuffix());
        intent.putExtra("Direction", activeAddress.getDirection());
        startActivityForResult(intent, 0);
        return;

    }

    public void commentsAction(View view) {
        TPApp.setActiveChalk(activeChalk);

        Intent i = new Intent();
        i.setClass(ChalkVehicleActivity.this, ChalkCommentsActivity.class);
        startActivityForResult(i, 0);
        return;
    }

    public void takePictureAction(View view) {
        if (activeChalk == null)
            return;
        try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                return;
            }

            if (!Feature.isFeatureAllowed(Feature.TAKE_PICTURES)) {
                Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                return;
            }

            int maxPhotos = 0;
            if (Feature.isFeatureAllowed(Feature.MAX_PHOTOS)) {
                String value = Feature.getFeatureValue(Feature.MAX_PHOTOS);
                try {
                    maxPhotos = Integer.parseInt(value);
                    for (int i = 0; i < activeChalk.getChalkPictures().size(); i++) {
                        if (activeChalk.getChalkPictures().get(i).getImagePath().contains("LPR")) {
                            maxPhotos = maxPhotos + 1;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (maxPhotos > 0 && activeChalk.getChalkPictures().size() >= maxPhotos) {
                displayErrorMessage("Exceeded max photos limit.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TPApp.setActiveChalk(activeChalk);
        Intent i = new Intent();
        i.setClass(this, TakePictureActivity.class);
        i.putExtra("NewChalkPicture", true);
        i.putExtra("ChalkId", activeChalk.getChalkId());
        startActivityForResult(i, REQUEST_TAKE_PICTURE);

        return;
    }

    public void gpsActionOLD(View view) {
        try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext()) || gpsTracker == null || !gpsTracker.isGPSAvailable()) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        GPSProgressBar.setVisibility(View.VISIBLE);
        UIHelper.toggleButtonState(gpsButton, false);

        gpsTracker.updateLocation(gpsLocation, new Receiver() {
            @Override
            public void onReceiveResult(Location location, Bundle resultData) {
                if (location != null) {
                    activeChalk.setLongitude(location.getLongitude() + "");
                    activeChalk.setLatitude(location.getLatitude() + "");
                    activeChalk.setGpstime(new Date());
                    isGPSLocation = true;
                }

                UIHelper.toggleButtonState(gpsButton, true);

                if (GPSProgressBar != null) {
                    GPSProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceiveResult(GPSLocation location, Bundle resultData) {
                if (location != null) {
                    activeAddress.setLocation(location.getLocation());
                    activeAddress.setStreetNumber(location.getStreetNumber());
                    activeAddress.setStreetPrefix("");
                    activeAddress.setStreetSuffix("");
                    activeAddress.setDirection("");
                    activeChalk.setLongitude(location.getLongitude());
                    activeChalk.setLatitude(location.getLatitude());
                    activeChalk.setGpstime(new Date());
                    isGPSLocation = true;
                }

                GPSHandler.sendEmptyMessage(0);
            }

            @Override
            public void onTimeout() {
                if (GPSProgressBar != null) {
                    GPSProgressBar.setVisibility(View.GONE);
                }
                if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")) {
                    TPApp.isServiceAvailable = false;
                } else {
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

    public void gpsAction(View view) {
        try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT <= 21) {

            GPSProgressBar.setVisibility(View.VISIBLE);

            UIHelper.toggleButtonState(gpsButton, false);

            Receiver receiver = new Receiver() {
                @Override
                public void onReceiveResult(Location location, Bundle resultData) {
                    if (location != null) {
                        activeChalk.setLongitude(location.getLongitude() + "");
                        activeChalk.setLatitude(location.getLatitude() + "");
                        activeChalk.setGpstime(new Date());
                        isGPSLocation = true;
                    }

                    UIHelper.toggleButtonState(gpsButton, true);

                }

                @Override
                public void onReceiveResult(GPSLocation location, Bundle resultData) {
                    if (location != null && (!location.getLocation().equals("") || !location.getStreetNumber().equals(""))) {

                        String strL = location.getLocation();
                        String[] split = strL.split(",");
                        activeAddress.setLocation(split[0]);
                        activeAddress.setStreetNumber(location.getStreetNumber());
                        activeAddress.setStreetPrefix("");
                        activeAddress.setStreetSuffix("");
                        activeAddress.setDirection("");
                        activeChalk.setLongitude(location.getLongitude());
                        activeChalk.setLatitude(location.getLatitude());
                        activeChalk.setGpstime(new Date());
                        isGPSLocation = true;
                    }

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

                            isGPSLocation = false;
                            activeChalk.setIsGPSLocation("N");
                            if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")) {
                                TPApp.isServiceAvailable = false;
                            } else {
                                TPApp.isServiceAvailable = true;
                            }
                            UIHelper.toggleButtonState(gpsButton, TPApp.isServiceAvailable);
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
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    }
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);

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

    public void searchStatesAction(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Intent intent = new Intent();
        intent.setClass(ChalkVehicleActivity.this, SearchLookupActivity.class);
        intent.putExtra("LIST_TYPE", TPConstant.STATES_SEARCH_LIST);
        intent.putExtra("Location", activeAddress.getLocation());
        intent.putExtra("StreetNumber", activeAddress.getStreetNumber());
        intent.putExtra("StreetPrefix", activeAddress.getStreetPrefix());
        intent.putExtra("StreetSuffix", activeAddress.getStreetSuffix());
        intent.putExtra("Direction", activeAddress.getDirection());
        intent.putExtra("STATE", stateEditText.getText().toString());
        startActivityForResult(intent, 0);
    }

    public void selectLocation(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Intent intent = new Intent();
        intent.setClass(ChalkVehicleActivity.this, SearchLookupActivity.class);
        intent.putExtra("LIST_TYPE", TPConstant.SELECT_LOCATION_LIST);
        intent.putExtra("Location", activeAddress.getLocation());
        intent.putExtra("StreetNumber", activeAddress.getStreetNumber());
        intent.putExtra("StreetPrefix", activeAddress.getStreetPrefix());
        intent.putExtra("StreetSuffix", activeAddress.getStreetSuffix());
        intent.putExtra("Direction", activeAddress.getDirection());
        intent.putExtra("STATE", stateEditText.getText().toString());
        startActivityForResult(intent, 0);
    }

    public void notesAction(View view) {
        try {
            dialog = new Dialog(this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDlgView = layoutInflater.inflate(R.layout.textinput_dialog, null, false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(inputDlgView);
            dialog.show();

            TextView titleTextview = (TextView) inputDlgView.findViewById(R.id.dialog_title);
            titleTextview.setText("Chalk Notes/References");

            Button saveBtn = (Button) inputDlgView.findViewById(R.id.dialog_save_btn);
            dialogInputText = (EditText) inputDlgView.findViewById(R.id.dialog_text_field);
            dialogInputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
            dialogInputText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dialogInputText.setText("");
                    return true;
                }
            });

            dialogInputText.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        TPUtility.hideSoftKeyboard(ChalkVehicleActivity.this);
                        return true;
                    }

                    return false;
                }
            });

            dialogInputText.setText(StringUtil.getDisplayString(activeChalk.getNotes()));
            dialogInputText.requestFocus();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }, 50);

            saveBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(ChalkVehicleActivity.this);

                    activeChalk.setNotes(dialogInputText.getText().toString());
                    dialog.dismiss();
                }
            });

            Button cancelBtn = (Button) inputDlgView.findViewById(R.id.dialog_cancel_btn);
            cancelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TPUtility.hideSoftKeyboard(ChalkVehicleActivity.this);
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (dialog!=null && dialog.isShowing())
            dialog.dismiss();
        backAction(null);
    }

    public void discardChalk() {
        try {
            // Remove Files
            try {
                DeviceInfo device = TPApp.getDeviceInfo();
                long currentPhotoNumber = device.getCurrentPhotoNumber();

                ArrayList<ChalkPicture> chalkPictures = activeChalk.getChalkPictures();
                if (chalkPictures != null) {
                    for (ChalkPicture picture : chalkPictures) {
                        File file = new File(picture.getImagePath());
                        if (file.exists()) {
                            file.delete();
                            currentPhotoNumber--;
                        }
                    }
                }

                device.setCurrentPhotoNumber(currentPhotoNumber);
                DeviceInfo.insertDeviceInfo(device);
                /*DatabaseHelper.getInstance().openWritableDatabase();
                DatabaseHelper.getInstance().insertOrReplace(device.getContentValues(), "devices");
                DatabaseHelper.getInstance().closeWritableDb();*/

            } catch (Exception e) {
                Log.e(TPConstant.TAG, "Error " + e.getMessage());
            }

            TPApp.setActiveChalk(null);
        } catch (Exception e) {
            Log.e(TPConstant.TAG, "Error " + e.getMessage());
        }

        finish();
    }

    public void backAction(View view) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setTitle("Chalk").setMessage("Are you sure you want to discard chalk entries?").setCancelable(true).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                discardChalk();
            }
        });

        AlertDialog confirmAlert = confirmBuilder.create();
        confirmAlert.show();
    }

    public void LPRAction(View view) {
        /*try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")) {
            isServiceAvailable = false;
        } else {
            isServiceAvailable = true;
        }
        if (isServiceAvailable) {
            Intent intent = new Intent();
            if (Feature.isFeatureAllowed(Feature.AUTO_LPR) && Feature.isAutoLPRActive(TPApp.userId)) {
                intent.setClass(this, AutoLPRActivity.class);
                startActivityForResult(intent, REQUEST_LPR);
            } else {
                intent.setClass(this, LPRActivityScreen.class);
                startActivityForResult(intent, REQUEST_LPR);
            }
        } else {
            Toast.makeText(this, "Data Network not available. Please try again.", Toast.LENGTH_SHORT).show();
        }

        return;
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

        if (text.contains("BACK") || text.contains("CLOSE")) {
            backAction(null);
        } else if (text.contains("SAVE") || text.contains("SAVE CHALK") || text.contains("ADD CHALK")) {
            addChalkAction(null);
        } else if (text.contains("GPS")) {
            gpsAction(null);
        } else if (text.contains("METER")) {
            meterNumberEditText.requestFocus();
            focusedEditText = meterNumberEditText;
        } else if (text.contains("PLATE")) {
            plateNumberEditText.requestFocus();
            focusedEditText = plateNumberEditText;
        } else if (text.contains("VIN")) {
            vinNumberEditText.requestFocus();
            focusedEditText = vinNumberEditText;
        } else if (text.contains("SPACE")) {
            spaceEditText.requestFocus();
            focusedEditText = spaceEditText;
        } else if (text.contains("LOCATION")) {
            selectLocation(null);
        } else if (text.contains("VIEW LIST") || text.contains("VIEW CHALKS")) {
            viewListAction(null);
        } else if (text.contains("COMMENTS")) {
            commentsAction(null);
        } else if (text.contains("STATE")) {
            searchStatesAction(null);
        } else if (text.contains("GO")) {
            if (focusedEditText != null && focusedEditText.getTag() != null) {
                processGoCommand(String.valueOf(focusedEditText.getTag()), focusedEditText.getText().toString());
            }
        } else if (text.contains("DONE")) {
            focusedEditText = null;
        } else if (focusedEditText != null) {
            focusedEditText.setText(text);
        }
    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {
        // TODO Auto-generated method stub
    }

    private void processGoCommand(String tagName, String value) {
        if (value == null || value.equals(""))
            return;

        if (tagName.equals("Plate")) {
            String plate = TPUtility.getValidPlate(value);
            plateNumberEditText.setText(plate);
            checkPlateHistory(value);

        } else if (tagName.equals("Vin")) {
            String vin = TPUtility.getValidVIN(value);
            vinNumberEditText.setText(vin);

        } else if (tagName.equals("Meter")) {
            String meter = TPUtility.getValidMeter(value);
            meterNumberEditText.setText(meter);
            checkMeterHistory(meter);
        }

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        statusIndicatorImageView = (ImageView) findViewById(R.id.status_indicator_image);

        if (connected) {
            if (Feature.isFeatureAllowed(Feature.LPR)) {
                lprButton.setEnabled(true);
                lprButton.setBackgroundResource(R.drawable.btn_yellow);
            } else {
                lprButton.setEnabled(false);
                lprButton.setBackgroundResource(R.drawable.btn_disabled);
            }
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
                lprButton.setEnabled(false);
                lprButton.setBackgroundResource(R.drawable.btn_disabled);
                statusIndicatorImageView.setImageResource(R.drawable.yellow_status_btn);
            }

        } else {
            lprButton.setEnabled(false);
            lprButton.setBackgroundResource(R.drawable.btn_disabled);
            gpsButton.setEnabled(false);
            gpsButton.setBackgroundResource(R.drawable.btn_disabled);

            statusIndicatorImageView.setImageResource(R.drawable.gray_status_btn);
        }

      /*  try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                lprButton.setEnabled(false);
                lprButton.setBackgroundResource(R.drawable.btn_disabled);
                gpsButton.setEnabled(false);
                gpsButton.setBackgroundResource(R.drawable.btn_disabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void clearField(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setText("");
        }

        return;
    }

    public void selectColor(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Intent i = new Intent();
        i.setClass(this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.COLOR_SEARCH_LIST);
        startActivityForResult(i, REQUEST_LOOKUP);
        return;
    }

    public void selectMake(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Intent i = new Intent();
        i.setClass(this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.MAKE_SEARCH_LIST);
        startActivityForResult(i, REQUEST_LOOKUP);
        return;
    }

    private boolean isAutoPromptVehicle() {
        boolean isAutoPrmpt = false;
        if (TPApp.getUserSettings() != null && TPApp.getUserSettings().isAutoPromptVehicle()) {
            isAutoPrmpt = true;
        } else {
            isAutoPrmpt = false;
        }
        return isAutoPrmpt;
    }

    public void ticketLogsAction(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Intent intent = new Intent(this, TicketLogsActivity.class);
        startActivity(intent);
    }

    @Override
    public void whereIAM(ADLocation data) {

        // String[] split = data.address.split(",");
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
        //new MyTracker(ChalkVehicleActivity.this, this).track();
        new GetLocation(ChalkVehicleActivity.this, this).track();
    }

    @Override
    public void onUserInteraction() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void initTimeOut() {
        try {
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
                    if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")) {
                        isServiceAvailable = false;
                    } else {
                        isServiceAvailable = true;
                    }
                    ((WriteTicketBLProcessor) screenBLProcessor).closeActiveDuty(isServiceAvailable);
                }
            }).start();

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(TPConstant.EXTRA_END_SHIFT, true);
            intent.setClass(ChalkVehicleActivity.this, HomeActivity.class);
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

                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(ChalkVehicleActivity.this);
                confirmBuilder.setCancelable(false);
                confirmBuilder.setTitle("Alert").setMessage(message).setCancelable(true)
                        .setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // endShift1();
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
                                    if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")) {
                                        isServiceAvailable = false;
                                    } else {
                                        isServiceAvailable = true;
                                    }
                                    ((WriteTicketBLProcessor) screenBLProcessor).closeActiveDuty(isServiceAvailable);
                                }
                            }).start();

                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(TPConstant.EXTRA_END_SHIFT, true);
                            intent.setClass(ChalkVehicleActivity.this, HomeActivity.class);
                            startActivity(intent);
                            TPUtility.createTxtFile();
                            finish();

                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    }
                }).setNegativeButton("No", null).show();
    }

    @Override
    public void meterResponse(Meter meter, String meterString) {
            historyMeter = meter;
        if (historyMeter == null) {
            displayErrorMessage("Meter record not found.");
            progressDialog.dismiss();
            return;
        }
        else {
            activeAddress.setDirection(historyMeter.getDirection());
            activeAddress.setStreetNumber(historyMeter.getStreetNumber());
            activeAddress.setStreetPrefix(historyMeter.getStreetPrefix());
            activeAddress.setStreetSuffix(historyMeter.getStreetSuffix());
            activeAddress.setLocation(historyMeter.getLocation());
            locationEditText.setText(TPUtility.getFullAddress(activeAddress));
        }

    }
}
