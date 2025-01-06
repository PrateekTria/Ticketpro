package com.ticketpro.parking.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.ScanBarcodeActivity;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.api.ServiceGeneratorCubTrac;
import com.ticketpro.api.ServiceGeneratorParkmobile;
import com.ticketpro.gpslibrary.ADLocation;
import com.ticketpro.gpslibrary.GetLocation1;
import com.ticketpro.gpslibrary.MyTracker;
import com.ticketpro.lpr.lpr.ImenseLicenseServer;
import com.ticketpro.lpr.web.AutoLPRActivity;
import com.ticketpro.lpr.web.LPRActivityScreen;
import com.ticketpro.model.Address;
import com.ticketpro.model.Body;
import com.ticketpro.model.ChalkPicture;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Color;
import com.ticketpro.model.Contact;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.DeviceGroup;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.ErrorLog;
import com.ticketpro.model.Feature;
import com.ticketpro.model.GPSLocation;
//import com.ticketpro.model.GenetecPatrollerActivities;
import com.ticketpro.model.HotListHandler;
import com.ticketpro.model.Hotlist;
import com.ticketpro.model.LPRNotify;
import com.ticketpro.model.MakeAndModel;
import com.ticketpro.model.Permit;
import com.ticketpro.model.Placard;
import com.ticketpro.model.PrintMacro;
import com.ticketpro.model.PrintTemplate;
import com.ticketpro.model.RepeatOffender;
import com.ticketpro.model.RepeatOffenderParams;
import com.ticketpro.model.RepeatOffender_Rpc;
import com.ticketpro.model.State;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.SystemBackup;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketCommentTemp;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketPictureTemp;
import com.ticketpro.model.TicketTemp;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.TicketViolationTemp;
import com.ticketpro.model.User;
import com.ticketpro.model.ValidResult;
import com.ticketpro.model.ValidTicketParams;
import com.ticketpro.model.ValidTicketRequest_Rpc;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.model.Violation;
import com.ticketpro.model.VoidTicketReason;
import com.ticketpro.model.gps.GpsResponse;
import com.ticketpro.model.gps.GpsResult;
import com.ticketpro.model.gps.LocationPram;
import com.ticketpro.model.gps.Location_Json_rpc;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.TPApplication.TicketSource;
import com.ticketpro.parking.activity.handlers.LookupThread;
import com.ticketpro.parking.api.WriteTicketNetworkCalls;
import com.ticketpro.parking.bl.WriteTicketBLProcessor;
import com.ticketpro.parking.service.GPSResultReceiver.Receiver;
import com.ticketpro.parking.service.JobIntentServiceSaveChalk;
import com.ticketpro.parking.service.JobIntentServiceSaveTicket;
import com.ticketpro.print.TicketPrinter;
import com.ticketpro.util.CSVUtility;
import com.ticketpro.util.CallbackHandler;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.Preference;
import com.ticketpro.util.PrintTokenParser;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;
import com.ticketpro.util.UIHelper;
import com.ticketpro.vendors.CaleZoneActivity;
import com.ticketpro.vendors.CurbtrackZoneList;
import com.ticketpro.vendors.GenetecPatrollerActivity;
import com.ticketpro.vendors.IPSLotDetails;
import com.ticketpro.vendors.PP2ZoneListClass;
import com.ticketpro.vendors.ParkMobileSpace;
import com.ticketpro.vendors.ParkMobileSpaceData;
import com.ticketpro.vendors.ParkMobileZoneList;
import com.ticketpro.vendors.ParkMobileZonesActivity;
import com.ticketpro.vendors.ParkeonControlGroup;
import com.ticketpro.vendors.PassportParkingZonesActivity;
import com.ticketpro.vendors.PayByPhoneZonesActivity;
import com.ticketpro.vendors.SamtransZoneActivity;
import com.ticketpro.vendors.Zone;
import com.ticketpro.vendors.ZonePoleList;
import com.ticketpro.vendors.cubtrack.cbt_model.CubTracZone;
import com.ticketpro.vendors.curvesense.CurveSenseActivity;
import com.ticketpro.vendors.dpt.DigitalPaytechZonesActivity;
import com.twotechnologies.n5library.BarcodeScanner.BarcodeManualControl;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class WriteTicketActivity extends BaseActivityImpl implements MyTracker.ADLocationListener, HotListHandler {
    public final static int debug = 1;
    //invocation codes for ANPR/ALPR Platform Server
    //return messages from ANPR/ALPR Platform Server
    private final static int PT_INVALID_INVOCATION = 99;
    private final static int PT_LICENSE_MISSING_OR_INVALID = 100;
    private final static int PT_ANPR_NOTONWHITELIST = 101;
    private final static int PT_ANPR_PERMITEXPIRED = 102;
    private final static int PT_ANPR_SCANTIMEOUT = 103;
    public static String tag = "WriteTicketLPR"; //tag for debugging
    public static String licenseKey = null;
    public final int REQUEST_LOOKUP = 1;
    public final int REQUEST_TAKE_PICTURE = 2;
    public final int REQUEST_VIOLATIONS = 3;
    public final int REQUEST_PREVIEW = 4;
    public final int REQUEST_LPR = 5;
    public final int REQUEST_PLATE_LOOKUP = 6;
    public final int REQUEST_PERMIT_BARCODE = 7;
    public final int REQUEST_METER_BARCODE = 8;
    public final int REQUEST_VIN_BARCODE = 9;
    public final int REQUEST_VIN_OCRCODE = 10;
    public final int REQUEST_PLATE_OCRCODE = 11;
    public final int REQUEST_AUTO_LPR = 12;
    public final int REQUEST_TAKE_SELFI = 13;
    public final int REQUEST_IPS_MULTISPACE = 14;
    public EditText stateEditText;
    public EditText expEditText;
    public EditText makeEditText;
    public EditText bodyEditText;
    public EditText colorEditText;
    public EditText plateNumberEditText;
    public EditText vinNumberEditText;
    public EditText tmEditText;
    public EditText spaceEditText;
    public EditText locationEditText;
    public EditText permitEditText;
    public EditText meterNumberEditText;
    public int stateId;
    public int colorId;
    public int bodyId;
    public int makeId;
    public String colorCode;
    public String bodyCode;
    public String makeCode;
    public Ticket activeTicket;
    public Ticket ticketToSyncRepeatOffender;
    public Button violationBtn;
    public Button photosBtn;
    public Button warningBtn;
    public ImageView voiceMemoBtn;
    public TextView violationDescText;
    public Button alprBtn;
    public boolean alprMode = false;
    public String alprPlateNum = "";
    public boolean alprLookup = false;
    boolean isLoggingEnabled = false;
    Intent ptsIntent = null;
    User userInfo;
    private CheckBox driveAwayChk;
    private Button navButton;
    private Button lprButton;

    private Button regButton;
    private Button gpsButton;
    private Button locationGPSButton;
    private Button vnvButton;
    private Button meterBarcodeButton;
    private Button permitBarcodeButton;
    private Button vinBarcodeButton;
    private Button vinOCRButton;
    private Button hotListButton;
    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private Handler errorHandler;
    private Handler GPSHandler;
    private LookupThread lookupThread;
    private ImageView statusIndicatorImageView;
    private ImageView statusIndicatorImageView1;
    private boolean isGPSLocation = false;
    private boolean voidListFlag = false;
    private boolean isUpdatingResult = false;
    private boolean isProcessingLookup = false;
    private boolean isProcessingVinLookup = false;
    private GestureDetector gestureDetector;
    private SharedPreferences mPreferences;
    public static boolean plateLookup;
    private Date pickerDate;
    private ProgressBar GPSProgressBar;
    private EditText focusedEditText;
    private Dialog emailDialog;
    private EditText emailMessageText;
    private EditText emailMessageExtraText;
    private boolean cameraFlash = false;
    private Camera flashCamera = null;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private String audioFile;
    private Date startTime;
    private Button printBtn;
    private boolean ignorePlateValidation = false;
    private int brightness;
    private String prevPlate;
    private Dialog otherReasonDialog;
    private EditText voidReasonText;
    private ImageView userSettingBtn;
    private CountDownTimer countDownTimer;
    private View inputDlgView;
    private EditText beginDate, endDate;
    private final boolean changeExpHeadisng = false;
    private String prevVIN;
    private SharedPreferences logPreferences;
    private int chalkTimerFlag = 0;
    private boolean usingMultiSpaceIPS;
    private String ipsMultiSpaceLotDes = "";
    private boolean showTrainingAlert = false;
    private Timer timer;
    private TimerTask timerTask;
    private Handler timerHandler = new Handler();
    private Handler playerHandler = new Handler();
    private Dialog playerDialog;
    private MediaPlayer audioPlayer;
    private SeekBar seekbar;
    private Button playBtn;
    private Button mPbutton;
    private Violation violationexp = null;
    private Dialog dialog;
    private boolean isDialogShown = false;
    private Runnable moveSeekBarThread = new Runnable() {
        public void run() {
            if (audioPlayer == null) {
                return;
            }

            try {
                if (audioPlayer.isPlaying()) {
                    int mediaPosNew = audioPlayer.getCurrentPosition();
                    int mediaMaxNew = audioPlayer.getDuration();

                    seekbar.setMax(mediaMaxNew);
                    seekbar.setProgress(mediaPosNew);
                    playerHandler.postDelayed(this, 100);
                } else {
                    int mediaPosNew = audioPlayer.getCurrentPosition();
                    int mediaMaxNew = audioPlayer.getDuration();

                    seekbar.setMax(mediaMaxNew);
                    seekbar.setProgress(mediaPosNew);
                    playerHandler.postDelayed(this, 100);
                }
            } catch (IllegalStateException e) {
                Log.e("TicketPRO", "Error " + e.getMessage());
            }
        }
    };
    private CountDownTimer countDownTimer1;
    /*private MyLocationReceiver myLocationReceiver;
    private Intent intent;
    private LocationService mServer;*/
    private boolean mBounded;
    // LocalBroadcastManager
    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            processAction(action);
        }
    };
    private CameraManager mCameraManager;
    private String mCameraId;
    private boolean checkSaveTask = true;
    private ImageView btnPlacard;
    private String m_Text;
    private Preference preference;
    boolean status = false;
    TicketTemp ticketTemp;
    public static Button backBtn;
    TelephonyManager telephonyManager;
    PhoneCustomStateListener psListener;
    String result;
    private HandlerThread handlerThreadCellularSignal = null;

    private int ips = 0;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;


    @SuppressLint({"ClickableViewAccessibility", "HandlerLeak"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ticketTemp = new TicketTemp();
        ticketTemp.setCustId(TPApp.getCustId());
        System.gc();
        initTimeOut();
        setContentView(R.layout.write_ticket);
        setLogger(WriteTicketActivity.class.getName());
        setBLProcessor(new WriteTicketBLProcessor((TPApplication) getApplicationContext()));
        setActiveScreen(this);
        isNetworkInfoRequired = true;
        preference = Preference.getInstance(this);

        Intent iPatroller = getIntent();
        String result = iPatroller.getStringExtra("patrollerId");
        String result1 = iPatroller.getStringExtra("zone_id");

        if (result != null && !result.isEmpty()) {
            showAlertDialog(result + " is Selected");
        }else if(result1 != null && !result1.isEmpty()){
            showAlertDialog(result1);
        }


        mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        printBtn = findViewById(R.id.write_ticket_print_btn);
        mPbutton = findViewById(R.id.write_t_p);

        if (Feature.isFeatureAllowed(Feature.PARK_P_BUTTON)) {
            mPbutton.setVisibility(View.VISIBLE);
        } else {
            mPbutton.setVisibility(View.GONE);
        }
        regButton = findViewById(R.id.write_ticket_reg_btn);
        if (Feature.getFeatureValue(Feature.PARK_PLATE_REG).equalsIgnoreCase("ALL")) {
            regButton.setVisibility(View.VISIBLE);

        }
        btnPlacard = findViewById(R.id.btnPlacard);
        stateEditText = findViewById(R.id.write_ticket_st);
        stateEditText.setTag("State");

        expEditText = findViewById(R.id.write_ticket_exp);
        expEditText.setTag("Exp");

        makeEditText = findViewById(R.id.write_ticket_make);
        makeEditText.setTag("Make");

        bodyEditText = findViewById(R.id.write_ticket_body);
        bodyEditText.setTag("Body");

        alprBtn = findViewById(R.id.write_ticket_alpr_btn);
        backBtn = findViewById(R.id.write_ticket_back_btn);


        if (Feature.isFeatureAllowed(Feature.ALPR)) {
            try {
                String devices = DeviceGroup.getDevicesIds(Feature.getFeatureValue(Feature.ALPR));
                int deviceId = TPApplication.getInstance().getDeviceId();
                if (devices != null && !TextUtils.isEmpty(devices)) {
                    if (devices.contains(",")) {
                        String[] split = devices.split(",");
                        for (String value : split) {
                            int s = Integer.parseInt(String.valueOf(value));
                            if (deviceId == s) {
                                alprBtn.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                    } else {
                        int s = Integer.parseInt(devices);
                        if (deviceId == s) {
                            alprBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //alprBtn.setVisibility(View.VISIBLE);
        }

        alprBtn.setOnClickListener(view -> {
            if (Feature.isFeatureAllowed(Feature.ALPR_ADMINLAUNCH)) {
                launchPTS(true);
            } else {
                launchPTS(false);
            }
        });

        if (Feature.isFeatureAllowed(Feature.PLACARD)) {
            btnPlacard.setVisibility(View.VISIBLE);
        }
        btnPlacard.setOnClickListener(view -> {
            final ArrayList<String> strings = Placard.__getListOfPlacardNo();
            if (!strings.isEmpty()) {
                __openPlacardListDialog();
            } else {
                _openPlacardDialog();
            }
        });

        colorEditText = findViewById(R.id.write_ticket_color);
        colorEditText.setTag("Color");

        plateNumberEditText = findViewById(R.id.write_ticket_plate_number);
        plateNumberEditText.setTag("Plate");

        vinNumberEditText = findViewById(R.id.write_ticket_vin_number);
        vinNumberEditText.setTag("Vin");

        permitEditText = findViewById(R.id.write_ticket_permit_number);
        permitEditText.setTag("Permit");

        meterNumberEditText = findViewById(R.id.write_ticket_meter_number);
        meterNumberEditText.setTag("Meter");

        tmEditText = findViewById(R.id.write_ticket_tm_number);
        tmEditText.setTag("TM");

        locationEditText = findViewById(R.id.write_ticket_location_number);
        locationEditText.setTag("Location");

        spaceEditText = findViewById(R.id.write_ticket_space);
        spaceEditText.setTag("Space");

        // printWebview=(WebView)findViewById(R.id.print_webview);
        gpsButton = findViewById(R.id.write_ticket_gps_btn);
        locationGPSButton = findViewById(R.id.location_gps_btn);
        GPSProgressBar = findViewById(R.id.GPSProcess);
        navButton = findViewById(R.id.home_navigation_btn);
        driveAwayChk = findViewById(R.id.write_ticket_driveaway_chk);
        photosBtn = findViewById(R.id.write_ticket_photos_btn);
        violationBtn = findViewById(R.id.write_ticket_violation_btn);
        warningBtn = findViewById(R.id.write_ticket_warning_btn);
        voiceMemoBtn = findViewById(R.id.write_ticket_voicememo);
        violationDescText = findViewById(R.id.write_ticket_violation_desc);
        userSettingBtn = findViewById(R.id.main_user_settings);
        permitBarcodeButton = findViewById(R.id.permit_barcode_btn);
        vinBarcodeButton = findViewById(R.id.vin_barcode_btn);
        meterBarcodeButton = findViewById(R.id.meter_barcode_btn);
        vinOCRButton = findViewById(R.id.write_ticket_ocr_vin_btn);
        hotListButton = findViewById(R.id.write_ticket_hotlist_btn);
        statusIndicatorImageView = findViewById(R.id.status_indicator_image);
        statusIndicatorImageView1 = findViewById(R.id.status_indicator_image1);

        statusIndicatorImageView1.setOnClickListener(v -> {
            if (!isServiceAvailable) {
                return;
            }

            if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && !status) {

                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
                confirmBuilder.setTitle("Alert")
                        .setMessage("Disabling network service will suspend all activities related to plate lookup and other services such as LPR and GPS. Continue?").setCancelable(false)
                        .setNegativeButton("Continue", (dialog, which) -> {
                            status = true;
                            TPApplication.getInstance().setNetOnOff("Y");
                            //preference.putBoolean("NET_ON_OFF",true);
                            statusIndicatorImageView1.setImageResource(R.drawable.red_status_btn);
                            gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                            gpsButton.setClickable(false);
                            alprBtn.setBackgroundResource(R.drawable.btn_disabled);
                            alprBtn.setClickable(false);
                            btnPlacard.setBackgroundResource(R.drawable.btn_disabled);
                            btnPlacard.setClickable(false);
                            lprButton.setBackgroundResource(R.drawable.btn_disabled);
                            lprButton.setClickable(false);

                               /* hotListButton.setBackgroundResource(R.drawable.btn_hotlist_gry);
                                hotListButton.setClickable(false);
*/
                            dialog.dismiss();

                        }).setPositiveButton("Cancel", (dialog, which) -> dialog.dismiss());
                AlertDialog confirmAlert = confirmBuilder.create();
                confirmAlert.show();


              /*  if (!status) {
                    //RED
                    status = true;
                    TPApplication.getInstance().setNetOnOff("Y");
                    //preference.putBoolean("NET_ON_OFF",true);
                    statusIndicatorImageView1.setImageResource(R.drawable.red_status_btn);
                    gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                    gpsButton.setClickable(false);
                    alprBtn.setBackgroundResource(R.drawable.btn_disabled);
                    alprBtn.setClickable(false);
                    btnPlacard.setBackgroundResource(R.drawable.btn_disabled);
                    btnPlacard.setClickable(false);
                    lprButton.setBackgroundResource(R.drawable.btn_disabled);
                    lprButton.setClickable(false);
                    hotListButton.setBackgroundResource(R.drawable.btn_hotlist_gry);
                    hotListButton.setClickable(false);
                } else if (status) {
                    //GREEN
                    status = false;
                    TPApplication.getInstance().setNetOnOff("N");
                    //preference.putBoolean("NET_ON_OFF",true);
                    statusIndicatorImageView1.setImageResource(R.drawable.green_status_btn_bk);
                    gpsButton.setBackgroundResource(R.drawable.btn_yellow);
                    gpsButton.setClickable(true);
                    alprBtn.setBackgroundResource(R.drawable.btn_yellow);
                    alprBtn.setClickable(true);
                    btnPlacard.setBackgroundResource(R.drawable.btn_blue);
                    btnPlacard.setClickable(true);
                    lprButton.setBackgroundResource(R.drawable.btn_yellow);
                    lprButton.setClickable(true);
                    hotListButton.setBackgroundResource(R.drawable.btn_hotlist_yellow);
                    hotListButton.setClickable(true);
                }*/
            } else {
                status = false;
                TPApplication.getInstance().setNetOnOff("N");
                //preference.putBoolean("NET_ON_OFF",true);
                statusIndicatorImageView1.setImageResource(R.drawable.green_status_btn_bk);
                gpsButton.setBackgroundResource(R.drawable.btn_yellow);
                gpsButton.setClickable(true);
                alprBtn.setBackgroundResource(R.drawable.btn_yellow);
                alprBtn.setClickable(true);
                btnPlacard.setBackgroundResource(R.drawable.btn_blue);
                btnPlacard.setClickable(true);
                lprButton.setBackgroundResource(R.drawable.btn_yellow);
                lprButton.setClickable(true);

               /* hotListButton.setBackgroundResource(R.drawable.btn_hotlist_yellow);
                hotListButton.setClickable(true);*/
            }
        });
        userInfo = TPApp.getUserInfo();
        if (userInfo != null) {
            if (userInfo.getBadge().equals("8888")) {
                showTrainingAlert = true;
            }
        }

        hotListButton.setOnClickListener(v -> {
            try {
                if (isEmptyField(stateEditText) || isEmptyField(plateNumberEditText)) {
                    displayHotListValidationMsg();
                } else {
                    hotListAction(v);
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        });

        userSettingBtn.setOnClickListener(v -> {
            try {
                Intent intent = new Intent();
                intent.setClass(WriteTicketActivity.this, UserSettingsActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        });

        View.OnTouchListener touchListener = (v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (isBarcodeBtnPressed) {
                    BarcodeManualControl.stopScan();
                }
            }

            return false;
        };

        permitBarcodeButton.setOnLongClickListener(arg0 -> {
            if (TPUtility.isN5ServiceAvailable(WriteTicketActivity.this)) {
                isBarcodeBtnPressed = true;

                processBarcode(new CallbackHandler() {
                    @Override
                    public void success(String data) {
                        String permit = TPUtility.getValidPermit(data);

                        permitEditText.setText(permit);
                        if (!TPApp.isServiceAvailable()) {
                            displayErrorMessage("Unbale to verify permit data at this time. Possible connection issue.");
                            return;
                        }
                        lookupThread.getHandler().checkPermitHistory(permit);
                    }

                    @Override
                    public void failure(String message) {
                    }
                });
            }

            return false;
        });
        mPbutton.setOnClickListener(v -> checkZones());
        meterBarcodeButton.setOnLongClickListener(arg0 -> {
            if (TPUtility.isN5ServiceAvailable(WriteTicketActivity.this)) {
                isBarcodeBtnPressed = true;

                processBarcode(new CallbackHandler() {
                    @Override
                    public void success(String data) {
                        String meter = TPUtility.getValidMeter(data);

                        meterNumberEditText.setText(meter);
                        lookupThread.getHandler().checkMeterHistory(meter, false);
                    }

                    @Override
                    public void failure(String message) {
                    }
                });
            }

            return false;
        });

        vinBarcodeButton.setOnLongClickListener(arg0 -> {
            if (TPUtility.isN5ServiceAvailable(WriteTicketActivity.this)) {
                isBarcodeBtnPressed = true;

                processBarcode(new CallbackHandler() {
                    @Override
                    public void success(String data) {
                        String vin = TPUtility.getValidVIN(data);

                        vinNumberEditText.setText(vin);
                        lookupThread.getHandler().checkVinHistory(vin);
                    }

                    @Override
                    public void failure(String message) {
                    }
                });
            }

            return false;
        });

        permitBarcodeButton.setOnTouchListener(touchListener);
        meterBarcodeButton.setOnTouchListener(touchListener);
        vinBarcodeButton.setOnTouchListener(touchListener);

        navButton.setOnClickListener(v -> {
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
            SlideoutActivity.prepare(WriteTicketActivity.this, R.id.write_ticket_main_layout, width);
            startActivity(new Intent(WriteTicketActivity.this, MenuActivity.class));
            overridePendingTransition(0, 0);
        });

        plateNumberEditText.setOnFocusChangeListener((v, hasfocus) -> {
            if (!hasfocus && !isProcessingLookup) {
                String plate = plateNumberEditText.getText().toString();
                String state = stateEditText.getText().toString();
                if (plate.isEmpty()) {
                    return;
                }

                boolean b = __checkValidPlateFromFeature(plate);
                if (b) {
                    return;
                }

                plate = plate.toUpperCase(Locale.getDefault());
                plate = TPUtility.getValidPlate(plate);
                plateNumberEditText.setText(plate);
                ticketTemp.setPlate(plate);
                if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                    TicketTemp.insertTicket(ticketTemp);
                }
                if (isAutoLookupRequired() && !(plate.equals(prevPlate))) {
                    prevPlate = plate;
                    isProcessingLookup = true;
                    if (!TPUtility.isValidPlateNumber(plate, state) || !StringUtil.isAlphaNumeric(plate)) {
                        plateNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                        if (Feature.isFeatureAllowed(Feature.PLATE_CHECK_ALERT)) {
                            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
                            confirmBuilder.setTitle("Alert")
                                    .setMessage("Plate format is incorrect. Please validate it.").setCancelable(false)
                                    .setNegativeButton("Continue", (dialog, which) -> {
                                        dialog.dismiss();
                                        // ignorePlateValidation = true;
                                        if (isServiceAvailable) {
                                            lookupThread.getHandler().lookupPlateHistory(plateNumberEditText.getText().toString());
                                        }
                                    }).setPositiveButton("Edit", (dialog, which) -> {
                                        dialog.dismiss();
                                        plateNumberEditText.requestFocus();
                                    });
                            AlertDialog confirmAlert = confirmBuilder.create();
                            confirmAlert.show();
                        } else {
                            TPUtility.showErrorToast(WriteTicketActivity.this,
                                    "Plate format is incorrect. Please validate it.");
                            if (isServiceAvailable) {
                                lookupThread.getHandler().lookupPlateHistory(plate);
                            }
                        }
                    } else {
                        plateNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                        if (isServiceAvailable) {
                            lookupThread.getHandler().lookupPlateHistory(plate);
                        }
                    }
                }
                isProcessingLookup = false;
            } else {
                plateNumberEditText.setBackgroundResource(R.drawable.text_field_focus_bkg);
                focusedEditText = plateNumberEditText;
            }
            isProcessingLookup = false;
        });

        plateNumberEditText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // plateNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                String plate = plateNumberEditText.getText().toString();

                String state = stateEditText.getText().toString();
                if (plate.isEmpty()) {
                    return false;
                }

                boolean b = __checkValidPlateFromFeature(plate);
                if (b) {
                    return false;
                }
                isProcessingLookup = true;
                plate = plate.toUpperCase(Locale.getDefault());
                plate = TPUtility.getValidPlate(plate);
                plateNumberEditText.setText(plate);
                ticketTemp.setPlate(plate);
                if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                    TicketTemp.insertTicket(ticketTemp);
                }
                if (!TPUtility.isValidPlateNumber(plate, state) || !StringUtil.isAlphaNumeric(plate)) {
                    plateNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                    if (Feature.isFeatureAllowed(Feature.PLATE_CHECK_ALERT)) {
                        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
                        confirmBuilder.setTitle("Alert")
                                .setMessage("Plate format is incorrect. Please validate it.").setCancelable(false)
                                .setNegativeButton("Continue", (dialog, which) -> {
                                    dialog.dismiss();
                                    // ignorePlateValidation = true;
                                    lookupThread.getHandler()
                                            .lookupPlateHistory(plateNumberEditText.getText().toString());
                                }).setPositiveButton("Edit", (dialog, which) -> {
                                    dialog.dismiss();
                                    plateNumberEditText.requestFocus();
                                });
                        AlertDialog confirmAlert = confirmBuilder.create();
                        confirmAlert.show();
                    } else {
                        TPUtility.showErrorToast(WriteTicketActivity.this,
                                "Plate format is incorrect. Please validate it.");
                        if (isServiceAvailable) {
                            lookupThread.getHandler().lookupPlateHistory(plate);
                        }
                    }
                } else {
                    plateNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                    if (isServiceAvailable) {
                        lookupThread.getHandler().lookupPlateHistory(plate);
                    }
                }
            }
            return false;
        });

        vinNumberEditText.setOnFocusChangeListener((v, hasfocus) -> {
            if (!hasfocus && !isProcessingVinLookup) {
                vinNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                String vin = vinNumberEditText.getText().toString();
                if (vin.equals("")) {
                    return;
                }
                vin = vin.toUpperCase(Locale.getDefault());
                vin = TPUtility.getValidVIN(vin);
                vinNumberEditText.setText(vin);
                ticketTemp.setVin(vin);
                if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                    TicketTemp.insertTicket(ticketTemp);
                }
                String plate = plateNumberEditText.getText().toString();
                boolean plateFlag = !plate.equals("");
                if (!TPUtility.PlateVINValidate(vin, plateFlag) || !StringUtil.isAlphaNumeric(vin)) {
                    displayErrorMessage("VIN format is incorrect. Please validate it.");
                    vinNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                }
                if (isAutoLookupRequired() && !(vin.equals(prevVIN))) {
                    prevVIN = vin;
                    isProcessingLookup = true;
                    if (vin.length() == 11 || vin.length() == 17 || vin.length() == 9) {
                        if (vin.length() == 17) {
                            if (Feature.isFeatureAllowed(Feature.PASSPORT_PARKING)
                                    && TPApplication.getInstance().enableVinPassportParking) {
                                lookupThread.getHandler().checkVinHistory(vin, true);
                            } else {
                                lookupThread.getHandler().lookupEdmundsVin(vin);
                            }
                        } else {
                            lookupThread.getHandler().checkVinHistory(vin);
                        }
                    }
                }
            } else {
                vinNumberEditText.setBackgroundResource(R.drawable.text_field_focus_bkg);
                focusedEditText = vinNumberEditText;
            }
            isProcessingLookup = false;
        });

        vinNumberEditText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String vin = vinNumberEditText.getText().toString();
                if (vin.equals("")) {
                    vinNumberEditText.requestFocus();
                    return false;
                }

                vin = vin.toUpperCase(Locale.getDefault());
                vin = TPUtility.getValidVIN(vin);
                vinNumberEditText.setText(vin);
                ticketTemp.setVin(vin);
                if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                    TicketTemp.insertTicket(ticketTemp);
                }
                String message = TPUtility.VINValidationMsg(vin);
                if (!message.equals("")) {
                    //displayErrorMessage(message);
                    vinNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                } else if (!StringUtil.isAlphaNumeric(vin)) {
                    TPUtility.showErrorToast(WriteTicketActivity.this,
                            "VIN format is incorrect. Please validate it.");
                    vinNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                } else {
                    vinNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                }

                TPUtility.hideSoftKeyboard(WriteTicketActivity.this);
                //lookupThread.getHandler().lookupEdmundsVin(vin);
                // Check history if full vin is provided

                prevVIN = vin;
                if (vin.length() == 11 || vin.length() == 17 || vin.length() == 9) {
                    isProcessingVinLookup = true;
                    if (vin.length() == 17) {
                        if (Feature.isFeatureAllowed(Feature.PASSPORT_PARKING)
                                && TPApplication.getInstance().enableVinPassportParking) {
                            lookupThread.getHandler().checkVinHistory(vin, true);
                        } else {
                            lookupThread.getHandler().lookupEdmundsVin(vin);
                        }
                    } else {
                        lookupThread.getHandler().checkVinHistory(vin);
                    }
                }
            }

            return false;
        });

        meterNumberEditText.setOnFocusChangeListener((v, hasfocus) -> {
            if (!hasfocus && !isProcessingLookup) {

                meterNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                String meter = meterNumberEditText.getText().toString();
                meter = meter.toUpperCase(Locale.getDefault());
                meter = TPUtility.getValidMeter(meter);
                meterNumberEditText.setText(meter);

                if (isAutoLookupRequired() && !StringUtil.isEmpty(meter)) {
                    lookupThread.getHandler().checkMeterHistory(meter, false);
                }

            } else {
                meterNumberEditText.setBackgroundResource(R.drawable.text_field_focus_bkg);
                focusedEditText = meterNumberEditText;
            }
            isProcessingLookup = false;
        });

        meterNumberEditText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                isProcessingLookup = true;
                // meterNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                String meter = meterNumberEditText.getText().toString();
                meter = meter.toUpperCase(Locale.getDefault());
                meter = TPUtility.getValidMeter(meter);
                meterNumberEditText.setText(meter);
                if (!meter.isEmpty()) {
                    lookupThread.getHandler().checkMeterHistory(meter, false);
                }
                TPUtility.hideSoftKeyboard(WriteTicketActivity.this);
            }
            return false;
        });

        permitEditText.setOnFocusChangeListener((v, hasfocus) -> {
            if (!hasfocus) {
                permitEditText.setBackgroundResource(R.drawable.text_field_bkg);
                String permit = permitEditText.getText().toString();
                permit = permit.toUpperCase(Locale.getDefault());
                permit = TPUtility.getValidPermit(permit);
                permitEditText.setText(permit);

                if (isAutoLookupRequired() && !StringUtil.isEmpty(permit)) {
                    if (!TPApp.isServiceAvailable()) {
                        displayErrorMessage("Unable to verify permit data at this time. Possible connection issue.");
                        return;
                    }
                    lookupThread.getHandler().checkPermitHistory(permit);
                }
            } else {
                permitEditText.setBackgroundResource(R.drawable.text_field_focus_bkg);
                focusedEditText = permitEditText;
            }
        });

        permitEditText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                // permitEditText.setBackgroundResource(R.drawable.text_field_bkg);
                String permit = permitEditText.getText().toString();
                permit = permit.toUpperCase(Locale.getDefault());
                permit = TPUtility.getValidPermit(permit);
                permitEditText.setText(permit);

                if (!permit.isEmpty()) {
                    if (!TPApp.isServiceAvailable()) {
                        displayErrorMessage("Unable to verify permit data at this time. Possible connection issue.");
                        return false;
                    }
                    lookupThread.getHandler().checkPermitHistory(permit);
                }
                TPUtility.hideSoftKeyboard(WriteTicketActivity.this);
            }
            return false;
        });

        // Check for Numeric Keypads
        if (Feature.isNumericKeypadRequired("meter")) {
            meterNumberEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        }

        if (Feature.isNumericKeypadRequired("plate")) {
            plateNumberEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        }

        if (Feature.isNumericKeypadRequired("permit")) {
            permitEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        }

        if (Feature.isNumericKeypadRequired("vin")) {
            vinNumberEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        }

        if (Feature.isNumericKeypadRequired("space")) {
            spaceEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        }

        // Check visibility
        if (Feature.isHiddenField("permitBarcode")) {
            permitBarcodeButton.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("meterBarcode")) {
            meterBarcodeButton.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("vinBarcode")) {
            vinBarcodeButton.setVisibility(View.GONE);
        }

        if (!Feature.isFeatureAllowed(Feature.VOICE_MEMOS)) {
            voiceMemoBtn.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("meter")) {
            meterNumberEditText.setVisibility(View.GONE);
            meterBarcodeButton.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("plate")) {
            plateNumberEditText.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("permit")) {
            permitEditText.setVisibility(View.GONE);
            permitBarcodeButton.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("space")) {
            spaceEditText.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("location")) {
            locationEditText.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("exp")) {
            expEditText.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("make")) {
            makeEditText.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("body")) {
            bodyEditText.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("color")) {
            colorEditText.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("tm")) {
            tmEditText.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("vin")) {
            vinNumberEditText.setVisibility(View.GONE);
            vnvButton.setVisibility(View.GONE);
            vinBarcodeButton.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("state")) {
            stateEditText.setVisibility(View.GONE);
        }

        if (Feature.isHiddenField("warning")) {
            warningBtn.setVisibility(View.GONE);
        }

        // Show hide NotifyLPRLogs
        if (!Feature.isFeatureAllowed(Feature.SHOW_NOTIFY_LPR)) {
            ImageView lprNotify = findViewById(R.id.lprnotify_logs);
            lprNotify.setVisibility(View.GONE);
        }

        // Show/Hide VNV Button
        vnvButton = findViewById(R.id.write_ticket_vnv_btn);
        if (!Feature.isFeatureAllowed(Feature.SHOW_VNV)) {
            vnvButton.setVisibility(View.GONE);
        }

        if (Feature.isFeatureAllowed(Feature.PHOTO_VIN)) {
            vinOCRButton.setVisibility(View.VISIBLE);
        }

        if (Feature.isFeatureAllowed(Feature.ADD_HOTLIST)) {
            final String featureValue = Feature.getFeatureValue(Feature.ADD_HOTLIST);
            try {
                final String devices = DeviceGroup.getDevicesIds(featureValue);
                int deviceId = TPApplication.getInstance().getDeviceId();
                if (devices != null && !TextUtils.isEmpty(devices)) {
                    if (devices.contains(",")) {
                        String[] split = devices.split(",");
                        for (int i = 0; i < split.length; i++) {
                            int s = Integer.parseInt(String.valueOf(split[i]));
                            if (deviceId == s) {
                                hotListButton.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                    } else {
                        int s = Integer.parseInt(devices);
                        if (deviceId == s) {
                            hotListButton.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    hotListButton.setVisibility(View.VISIBLE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // Default State
        State defaultState = State.getDefaultState(this);
        if (defaultState != null) {
            stateEditText.setText(defaultState.getCode());
            stateId = defaultState.getId();
        }

        plateNumberEditText.clearFocus();
        vinNumberEditText.clearFocus();
        meterNumberEditText.clearFocus();
        tmEditText.clearFocus();
        spaceEditText.clearFocus();
        permitEditText.clearFocus();

        driveAwayChk.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
            builder.setTitle("DriveAway Confirmation")
                    .setMessage("Are you sure you want to mark this ticket as DriveAway?").setCancelable(false)
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        driveAwayChk.setChecked(false);
                    }).setPositiveButton("OK", (dialog, which) -> driveAwayChk.setChecked(true));

            AlertDialog alert = builder.create();
            alert.show();
        });

        /*GPSHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (activeTicket != null) {
                    locationEditText.setText(TPUtility.getFullAddress(activeTicket));
                }

                GPSProgressBar.setVisibility(View.GONE);
            }
        };*/

        // Initialize lookup thread
        lookupThread = new LookupThread(this);
        lookupThread.start();

        dataLoadingHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                closeProgressDialog();
                if (activeTicket == null || activeTicket.getCitationNumber() == 0) {
                    showApplicationError("There is an internal error, please contact your administrator immediately");
                    return;
                }
                lprButton = findViewById(R.id.write_ticket_lpr_btn);
                // Disable photos, GPS and LPR functionality on Emulators
                try {
                    if (false) {//TPUtility.isRunningOnEmulator(getApplicationContext())) {
                        UIHelper.toggleButtonState(gpsButton, false);
                        UIHelper.toggleButtonState(lprButton, false);
                        UIHelper.toggleButtonState(photosBtn, false);
                    } else {
                        if (!Feature.isFeatureAllowed(Feature.LPR)) {
                            UIHelper.toggleButtonState(lprButton, false);
                        }
                        if (UIHelper.isGpsDeviceValue(TPApp.deviceId)) {
                            UIHelper.toggleButtonState(gpsButton, false);
                        } else {
                            if (!Feature.isFeatureAllowed(Feature.GPS)) {
                                UIHelper.toggleButtonState(gpsButton, false);
                            }
                        }


                        if (TPApp.getUserSettings() != null
                                && TPApp.getUserSettings().getGps() != null
                                && TPApp.getUserSettings().getGps().equals("N")) {

                            UIHelper.toggleButtonState(gpsButton, false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                TextView citationNumber = findViewById(R.id.citation_number);
                citationNumber.setText("#" + TPUtility.prefixZeros(activeTicket.getCitationNumber(), 8));
                populateValues(activeTicket, true);

                // Update Network Indicator
                isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                if (isServiceAvailable) {
                    statusIndicatorImageView.setImageResource(
                            isFastConnection ? R.drawable.green_status_btn_bk : R.drawable.yellow_status_btn);
                } else {
                    statusIndicatorImageView.setImageResource(R.drawable.gray_status_btn);
                }
            }
        };

        errorHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                closeProgressDialog();
            }
        };

        gestureDetector = new GestureDetector(new SwipeGestureDetector());
        View.OnTouchListener gestureListener = (v, event) -> gestureDetector.onTouchEvent(event);

        View view = findViewById(R.id.write_ticket_main_layout);
        view.setOnTouchListener(gestureListener);

        locationEditText.setOnLongClickListener(v -> {
            LocationEntryAction(v);
            return true;
        });

        plateNumberEditText.setOnLongClickListener(v -> {
            clearField(v);
            return true;
        });

        permitEditText.setOnLongClickListener(v -> {
            clearField(v);
            return true;
        });

        meterNumberEditText.setOnLongClickListener(v -> {
            clearField(v);
            return true;
        });

        vinNumberEditText.setOnLongClickListener(v -> {
            clearField(v);
            return true;
        });

        tmEditText.setOnLongClickListener(v -> {
            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
            confirmBuilder.setTitle("WriteTicket").setMessage("Are you sure you want to discard this ticket?")
                    .setCancelable(false).setNegativeButton("No", (dialog, which) -> {
                    }).setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        discardTicket();

                        if (Feature.isFeatureAllowed(Feature.SHOW_CHALK_OPTIONS)) {
                            showChalkOptions();
                        } else {
                            ChalkVehicle chalk = TPApp.createNewChalk();
                            TPApp.setActiveChalk(chalk);

                            Intent i = new Intent();
                            i.setClass(WriteTicketActivity.this, ChalkVehicleActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });

            AlertDialog confirmAlert = confirmBuilder.create();
            confirmAlert.show();
            return true;
        });

        /*spaceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasfocus) {
                if (!hasfocus) {
                    spaceEditText.setBackgroundResource(R.drawable.text_field_bkg);
                } else {
                    spaceEditText.setBackgroundResource(R.drawable.text_field_focus_bkg);
                    focusedEditText = spaceEditText;
                }
            }
        });*/

        spaceEditText.setOnLongClickListener(v -> {
            if (spaceEditText.getText().toString().isEmpty()) {
                checkZones();
                return true;
            }

            clearField(v);
            return true;
        });

        spaceEditText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                TPUtility.hideSoftKeyboard(WriteTicketActivity.this);

                String spaceStr = spaceEditText.getText().toString();
                if (!spaceStr.isEmpty()) {
                    int ipsParkMobile = TPApplication.getInstance().getIpsParkMobile();
                    if (Feature.isFeatureAllowed(Feature.PARK_MOBILE) && Feature.getFeatureValue(Feature.PARK_MOBILE).equals("park_space") && ipsParkMobile == 0) {
                        if (TPApp.enableParkMobile) {
                            String park_zone = preference.getString("ZoneCode");
                            if (park_zone != null && !TextUtils.isEmpty(park_zone)) {
                                __getZoneSpace(park_zone, spaceStr);
                            } else {
                                __getParkmobileZoneList(spaceStr);
                            }
                        } else {
                            lookupThread.getHandler().lookupSpaceHistory(spaceStr);
                        }

                    } else {

                        lookupThread.getHandler().lookupSpaceHistory(spaceStr);
                    }
                }
            }

            return false;
        });

        colorEditText.setOnLongClickListener(v -> {
            clearField(v);
            colorCode = null;
            return true;
        });

        makeEditText.setOnLongClickListener(v -> {
            clearField(v);
            makeCode = null;
            return true;
        });

        bodyEditText.setOnLongClickListener(v -> {
            clearField(v);
            bodyCode = null;
            return true;
        });

        voiceMemoBtn.setOnLongClickListener(v -> {
            if (audioFile != null) {
                removeVoiceMemo();
            }

            return true;
        });

        photosBtn.setOnLongClickListener(v -> {
            Intent i = new Intent();
            i.setClass(WriteTicketActivity.this, PhotosActivity.class);
            startActivityForResult(i, REQUEST_TAKE_PICTURE);
            return true;
        });

        bindDataAtLoadingTime();

        // Register Location Broadcast Receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(TPConstant.LOCAL_BROADCAST_EVENT));

        enablePrint();

        try {
            logPreferences = getApplicationContext().getSharedPreferences(TPConstant.PREFS_KEY_ENABLE_LOG, Context.MODE_PRIVATE);
            isLoggingEnabled = logPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_LOG, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void __getParkmobileZoneList(String spaceStr) {
        //https://api.parkmobile.us/nforceapi/zones?format=json
        if (isServiceAvailable) {
            VendorServiceConfig config = VendorService.getServiceConfigCale(VendorService.PARK_MOBILE_ZONELIST, TPApp.deviceId, "/");
            if (config == null)
                return;
            Map<String, String> paramsMap = config.getParamsMap();
            String user = paramsMap.get("username");
            String password = paramsMap.get("password");
            String serviceURL = config.getServiceURL() + "/" + config.getParams() + "?format=json";

            if (user != null && password != null) {

                progressDialog = new ProgressDialog(WriteTicketActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                progressDialog.show();

                ApiRequest service = ServiceGeneratorParkmobile.createService(ApiRequest.class, user, password);
                service.getZoneParkMobile(serviceURL).enqueue(new Callback<ParkMobileZoneList>() {
                    @Override
                    public void onResponse(Call<ParkMobileZoneList> call, Response<ParkMobileZoneList> response) {
                        WriteTicketActivity.this.progressDialog.isShowing();
                        WriteTicketActivity.this.progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            List<Zone> zones = response.body().getZones();
                            if (zones != null && zones.size() > 0) {
                                __showList(zones, spaceStr);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ParkMobileZoneList> call, Throwable t) {
                        WriteTicketActivity.this.progressDialog.isShowing();
                        WriteTicketActivity.this.progressDialog.dismiss();

                        Log.i("WriteTicketActivity", t.getMessage());
                    }
                });

            } else {

                new iOSDialogBuilder(this)
                        .setSubtitle("Username and password not found!")
                        .setPositiveListener("OK", new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build().show();
            }
        } else {
            new iOSDialogBuilder(this)
                    .setSubtitle("Internet connection not available!")
                    .setPositiveListener("OK", new iOSDialogClickListener() {
                        @Override
                        public void onClick(iOSDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .build().show();
        }
    }

    private void __showList(List<Zone> zones, String spaceStr) {

        AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
        View headerView = LayoutInflater.from(WriteTicketActivity.this).inflate(R.layout.dialog_header, null);
        final TextView titleView = headerView.findViewById(R.id.header_title);

        titleView.setText("Select Zone");


        TextView groupName = new TextView(WriteTicketActivity.this);
        groupName.setText(TPApp.IPSSpaceGroup);

        LinearLayout actionLayout = headerView.findViewById(R.id.header_actions);
        actionLayout.addView(groupName);
        builder.setCustomTitle(headerView);

        ListView listView = new ListView(WriteTicketActivity.this);
        String[] from = new String[]{"search_title"};
        int[] to = new int[]{R.id.search_textview};

        List<HashMap<String, String>> fillMaps = new ArrayList<>();
        for (Zone item : zones) {
            HashMap<String, String> map = new HashMap<>();
            map.put("search_title", item.getSignageZoneCode());
            fillMaps.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(WriteTicketActivity.this, fillMaps, R.layout.search_list_item, from, to);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Zone zone = zones.get(position);
            preference.putString("ZoneCode", zone.getSignageZoneCode());
            __getZoneSpace(zone.getSignageZoneCode(), spaceStr);
            dialog.dismiss();
        });
        builder.setView(listView);
        dialog = builder.create();
        dialog = builder.show();
    }

    private void __getZoneSpace(String zoneNumber, String spaceNumber) {
        //https://api.parkmobile.us/nforceapi/parkingrights/zone/52503/122

        if (isServiceAvailable) {
            VendorServiceConfig config = VendorService.getServiceConfigCale(VendorService.PARK_MOBILE_ZONEINFO, TPApp.deviceId, "/");
            Map<String, String> paramsMap = config.getParamsMap();
            String user = paramsMap.get("username");
            String password = paramsMap.get("password");
            int zoneNum = Integer.parseInt(zoneNumber);
            int spcNum = Integer.parseInt(spaceNumber);
            String params = config.getParams();
            params = params.replaceAll("\\{ZONE_ID\\}", String.valueOf(zoneNum));
            String serviceURL = config.getServiceURL() + "/" + params + "/" + spcNum;

            if (config != null && user != null && password != null) {

                progressDialog = new ProgressDialog(WriteTicketActivity.this);
                progressDialog.setMessage("Searching space: " + spaceNumber);
                progressDialog.setCancelable(false);
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                progressDialog.show();

                ApiRequest service = ServiceGeneratorParkmobile.createService(ApiRequest.class, user, password);
                service.getParkMobileSingleSpace(serviceURL).enqueue(new Callback<ParkMobileSpace>() {
                    @Override
                    public void onResponse(Call<ParkMobileSpace> call, Response<ParkMobileSpace> response) {
                        WriteTicketActivity.this.progressDialog.isShowing();
                        WriteTicketActivity.this.progressDialog.dismiss();
                        if (response.isSuccessful() && response.body().getParkingRights().size() > 0) {
                            List<ParkMobileSpaceData> parkingRights = response.body().getParkingRights();
                            ParkMobileSpaceData parkMobileSpaceData = parkingRights.get(0);
                            displayParkingInfoMsg(parkMobileSpaceData);
                        }
                    }

                    @Override
                    public void onFailure(Call<ParkMobileSpace> call, Throwable t) {
                        WriteTicketActivity.this.progressDialog.isShowing();
                        WriteTicketActivity.this.progressDialog.dismiss();
                        Log.i("WriteTicketActivity", t.getMessage());
                    }
                });

            } else {

            }
        }
    }

    private boolean __checkValidPlateFromFeature(String plate) {
        boolean b = false;
        try {
            String park_plate_alert = Feature.getFeatureValue("park_plate_alert");
            if (park_plate_alert != null && !TextUtils.isEmpty(park_plate_alert)) {
                String[] strArray = park_plate_alert.split(",");
                for (int i = 0; i < strArray.length; i++) {
                    if (plate.equalsIgnoreCase(strArray[i])) {
                        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
                        confirmBuilder
                                .setTitle("Alert")
                                .setMessage("Plate format is incorrect. Please validate it.").setCancelable(false)
                                .setPositiveButton("Edit", (dialog, which) -> {
                                    dialog.dismiss();
                                    plateNumberEditText.requestFocus();
                                });
                        AlertDialog confirmAlert = confirmBuilder.create();
                        confirmAlert.show();
                        plateNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                        enablePrint();
                        b = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    private void __openPlacardListDialog() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(WriteTicketActivity.this);
        builderSingle.setIcon(R.drawable.ic_placard);
        builderSingle.setTitle("Placard List");

        final ArrayList<String> strings = Placard.__getListOfPlacardNo();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WriteTicketActivity.this, android.R.layout.select_dialog_singlechoice);

        arrayAdapter.addAll(strings);
        builderSingle.setPositiveButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
        builderSingle.setNegativeButton("NEW", (dialog, which) -> _openPlacardDialog());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            final String strName = arrayAdapter.getItem(which);
            final Placard placard1 = Placard.__getPlacardByNo(strName);
            if (placard1 != null && !TextUtils.isEmpty(placard1.getPlacardNo())) {
                new AlertDialog.Builder(WriteTicketActivity.this)
                        .setTitle("NOTIFICATION")
                        .setMessage(placard1.getPlacardDetails())
                        .setPositiveButton("Write ticket", (dialog12, which1) -> {
                            TPApplication.getInstance().getActiveTicket().setPlacard(strName);
                            dialog12.dismiss();
                        })
                        .setNegativeButton("Cancel", (dialog1, i) -> dialog1.dismiss())
                        .show();
            }
        });
        builderSingle.show();
    }

    private void _openPlacardDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.placard_dialog, null);
        final EditText mEventType = mView.findViewById(R.id.et_id_type);

        final Button mCancle = mView.findViewById(R.id.btn_cancel);
        final Button mOk = mView.findViewById(R.id.btn_ok);

        mBuilder.setView(mView);
        final android.app.AlertDialog dialog = mBuilder.create();
        dialog.show();
        TPUtility.showSoftKeyboard(WriteTicketActivity.this, mEventType);
        mOk.setOnClickListener(v -> {
            final String s = mEventType.getText().toString();
            if (TextUtils.isEmpty(s)) {
                Toast.makeText(getApplicationContext(), "Please enter placard", Toast.LENGTH_LONG).show();
                return;
            }
            TPUtility.hideSoftKeyboard(WriteTicketActivity.this, plateNumberEditText);
            //String placard1 = preference.getString("PLACARD");
            final Placard placard1 = Placard.__getPlacardByNo(s);
            if (placard1 != null && !TextUtils.isEmpty(placard1.getPlacardNo())) {
                dialog.dismiss();
                new AlertDialog.Builder(WriteTicketActivity.this)
                        .setTitle("NOTIFICATION")
                        .setMessage(placard1.getPlacardDetails())
                        .setPositiveButton("Write ticket", (dialog1, which) -> {
                            TPApplication.getInstance().getActiveTicket().setPlacard(s);
                            dialog1.dismiss();
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialog.dismiss())
                        .show();
            } else {
                if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")) {
                    return;
                }
                if (isServiceAvailable) {

                    try {
                        CustomerInfo customerInfo = CustomerInfo.getCustomerInfo(TPApplication.getInstance().custId);
                        User user = User.getUserInfo(TPApplication.getInstance().userId);
                        final WriteTicketBLProcessor bl = ((WriteTicketBLProcessor) screenBLProcessor);
                        final String agencyCode = customerInfo.getAgencyCode();
                        final String users = user.getFullName();
                        final String plate = plateNumberEditText.getText().toString();
                        final String placard = s;
                        final String source = "M";
                        new Thread(() -> {
                            try {
                                boolean placard11 = bl.getPlacard(agencyCode, users, plate, placard, source);
                            } catch (Exception e) {
                                e.printStackTrace();
                                TPUtility.hideSoftKeyboard(WriteTicketActivity.this, plateNumberEditText);
                            }
                        }).start();


                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    dialog.dismiss();
                }
            }
        });

        mCancle.setOnClickListener(v -> {
            new Handler().postDelayed(() -> TPUtility.hideSoftKeyboard(WriteTicketActivity.this, plateNumberEditText), 100);

            dialog.dismiss();
        });

    }

    public void ticketLogsAction(View view) {
        Intent intent = new Intent(this, TicketLogsActivity.class);
        startActivity(intent);
    }

    public void lprnotifyLogsAction(View view) {
        Intent intent = new Intent(this, LPRNotifyActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showApplicationError(String message) {
        closeProgressDialog();
        TPApplication TPApp = TPApplication.getInstance();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, 0);
                return;
            }

            String number = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
            ErrorLog errorlog = new ErrorLog();
            errorlog.setCustId(TPApp.custId);
            errorlog.setDeviceId(TPApp.deviceId);
            errorlog.setUserId(TPApp.userId);
            errorlog.setModule(TPConstant.MODULE_NAME);
            if (TPApplication.getInstance().getDeviceInfo() != null) {
                String appVersion = TPApplication.getInstance().getDeviceInfo().getAppVersion();
                errorlog.setApp_version(appVersion != null ? appVersion : "");
            }
            errorlog.setErrorDate(new Date());
            errorlog.setDevice(TPApp.getDeviceInfo().getDevice());
            errorlog.setErrorType("CitationIssue");
            errorlog.setErrorDesc("Mobile Number : " + number + " , Error: " + message);

            ArrayList<ErrorLog> list = new ArrayList<>();
            list.add(errorlog);

            log.error("Citation Issue: " + message);
            if (isServiceAvailable) {
                WriteTicketNetworkCalls.sendErrorLogs(list);
            }

            //screenBLProcessor.getProxy().sendErrorLog(list);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }


        if(!isDialogShown) {
            isDialogShown = true;

            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
            confirmBuilder.setTitle("Application error")
                    .setMessage("Please notify support. \nDevice cannot be used at this time.")
                    .setCancelable(false)
                    .setNegativeButton(HTTP.CONN_CLOSE, (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    })
                    .setPositiveButton("Call Support", (dialog, which) -> {
                        dialog.dismiss();
                        try {
                            String mobileNumber = Contact.getSupportNumber();
                            Intent callIntent = new Intent("android.intent.action.CALL");
                            callIntent.setData(Uri.parse("tel:" + mobileNumber));
                            startActivity(callIntent);
                        } catch (Exception e) {
                            Log.e("Call Support", TPUtility.getPrintStackTrace(e));
                        }

                        finish();
                    });


            confirmBuilder.create().show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showDuplicateEntryInDBError(String message) {
        TPApplication TPApp = TPApplication.getInstance();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, 0);
                return;
            }
            String number = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
            ErrorLog errorlog = new ErrorLog();
            errorlog.setCustId(TPApp.custId);
            errorlog.setDeviceId(TPApp.deviceId);
            errorlog.setUserId(TPApp.userId);
            errorlog.setModule(TPConstant.MODULE_NAME);
            errorlog.setApp_version(TPApplication.getInstance().getDeviceInfo().getAppVersion());
            errorlog.setErrorDate(new Date());
            errorlog.setDevice(TPApp.getDeviceInfo().getDevice());
            errorlog.setErrorType("DuplicateRecordIssue");
            errorlog.setErrorDesc("Mobile Number : " + number + " , Error: " + message);

            ArrayList<ErrorLog> list = new ArrayList<>();
            list.add(errorlog);

            log.error("Duplicate record Issue: " + message);
            if (isServiceAvailable) {
                WriteTicketNetworkCalls.sendErrorLogs(list);
            }
            //screenBLProcessor.getProxy().sendErrorLog(list);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
        confirmBuilder.setTitle("Application error")
                .setMessage("Please notify support. \nDevice cannot be used at this time.")
                .setCancelable(false)
                .setNegativeButton(HTTP.CONN_CLOSE, (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setPositiveButton("Call Support", (dialog, which) -> {
                    dialog.dismiss();
                    try {
                        String mobileNumber = Contact.getSupportNumber();
                        Intent callIntent = new Intent("android.intent.action.CALL");
                        callIntent.setData(Uri.parse("tel:" + mobileNumber));
                        startActivity(callIntent);
                    } catch (Exception e) {
                        Log.e("Call Support", TPUtility.getPrintStackTrace(e));
                    }

                    finish();
                });

        confirmBuilder.create().show();
    }

    private void showChalkOptions() {
        try {
            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
            confirmBuilder.setTitle("Chalk Type").setMessage("Please tap on the chalk type to create entry?")
                    .setCancelable(false).setNegativeButton("ChalkBy Vehicle", (dialog, which) -> {
                        dialog.dismiss();
                        ChalkVehicle chalk = TPApp.createNewChalk();
                        TPApp.setActiveChalk(chalk);

                        Intent i = new Intent();
                        i.setClass(WriteTicketActivity.this, ChalkVehicleActivity.class);
                        startActivity(i);
                        finish();
                    }).setPositiveButton("ChalkBy Location", (dialog, which) -> {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setClass(WriteTicketActivity.this, PhotosChalkActivity.class);
                        startActivity(i);
                        finish();
                    });

            AlertDialog confirmAlert = confirmBuilder.create();
            confirmAlert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void confirmAddViolation(final Violation violation) {
        StringBuffer msg = new StringBuffer();
        StringBuffer values = new StringBuffer();
        StringBuffer headerText = new StringBuffer();
        String title;

        title = "Meter Violation";
        headerText.append("Ticket violation is already there.\nDo you want to add meter violation?");

        msg.append("Code" + "\n");

        msg.append("Violation" + "\n");

        values.append(": " + StringUtil.getDisplayString(violation.getCode()) + "\n");

        values.append(": " + StringUtil.getDisplayString(violation.getTitle()) + "\n");

        /*WebView wv = new WebView(getBaseContext());
        wv.loadData(msg.toString(), "text/html", "utf-8");
        wv.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        wv.getSettings().setDefaultTextEncodingName("utf-8");*/

        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_view, null);
        TextView headerTV = view.findViewById(R.id.headerTV);
        TextView valueTV = view.findViewById(R.id.valueTV);
        TextView headTV = view.findViewById(R.id.headerTextTV);

        headTV.setVisibility(View.VISIBLE);

        headerTV.setText(msg.toString());
        valueTV.setText(values.toString());
        headTV.setText(headerText.toString());

        new AlertDialog.Builder(WriteTicketActivity.this).setView(view).setCancelable(false).setTitle(title)
                .setNegativeButton("No", (dialog, which) -> dialog.cancel()).setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();

                    TicketViolation ticketViolation = new TicketViolation();
                    ticketViolation.setFine(violation.getBaseFine());
                    ticketViolation.setViolationDesc(violation.getTitle());
                    ticketViolation.setViolationId(violation.getId());
                    ticketViolation.setViolationCode(violation.getCode());

                    // Added to ticket violations list
                    activeTicket.getTicketViolations().add(ticketViolation);
                    violationBtn.setText("V (" + activeTicket.getTicketViolations().size() + ")");
                    if (activeTicket.getTicketViolations().size() > 0) {
                        violationDescText.setText(activeTicket.getTicketViolations().get(0).getViolationDisplay());
                    } else {
                        violationDescText.setText("");
                    }
                }).show();
    }

    public void confirmAddViolation(final Violation violation, boolean isIPSFeatureViol) {
        if (Feature.isFeatureAllowed(Feature.IPS_MULTISPACE_VIOLATION_PROMPT)) {
            StringBuffer msg = new StringBuffer();
            StringBuffer values = new StringBuffer();
            StringBuffer headText = new StringBuffer();
            String title = "";

            title = "Add Violation?";
            headText.append("Default Ticket violation found.\nDo you want to add violation?");

            msg.append("Code" + "\n");

            msg.append("Violation" + "\n");

            values.append(": " + StringUtil.getDisplayString(violation.getCode()) + "\n");

            values.append(": " + StringUtil.getDisplayString(violation.getTitle()) + "\n");

            /*WebView wv = new WebView(getBaseContext());
            wv.loadData(msg.toString(), "text/html", "utf-8");
            wv.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            wv.getSettings().setDefaultTextEncodingName("utf-8");*/
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = view.findViewById(R.id.headerTV);
            TextView valueTV = view.findViewById(R.id.valueTV);
            TextView headTV = view.findViewById(R.id.headerTextTV);

            headTV.setVisibility(View.VISIBLE);

            headerTV.setText(msg.toString());
            valueTV.setText(values.toString());
            headTV.setText(headText.toString());
            new AlertDialog.Builder(WriteTicketActivity.this).setView(view).setCancelable(false).setTitle(title)
                    .setNegativeButton("No", (dialog, which) -> dialog.cancel()).setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();

                        TicketViolation ticketViolation = new TicketViolation();
                        ticketViolation.setFine(violation.getBaseFine());
                        ticketViolation.setViolationDesc(violation.getTitle());
                        ticketViolation.setViolationId(violation.getId());
                        ticketViolation.setViolationCode(violation.getCode());

                        // Added to ticket violations list
                        activeTicket.getTicketViolations().add(ticketViolation);
                        violationBtn.setText("V (" + activeTicket.getTicketViolations().size() + ")");
                        if (activeTicket.getTicketViolations().size() > 0) {
                            violationDescText.setText(activeTicket.getTicketViolations().get(0).getViolationDisplay());
                        } else {
                            violationDescText.setText("");
                        }
                    }).show();
        } else {
            try {
                TicketViolation ticketViolation = new TicketViolation();
                ticketViolation.setFine(violation.getBaseFine());
                ticketViolation.setViolationDesc(violation.getTitle());
                ticketViolation.setViolationId(violation.getId());
                ticketViolation.setViolationCode(violation.getCode());

                // Added to ticket violations list
                activeTicket.getTicketViolations().add(ticketViolation);
                violationBtn.setText("V (" + activeTicket.getTicketViolations().size() + ")");
                if (activeTicket.getTicketViolations().size() > 0) {
                    violationDescText.setText(activeTicket.getTicketViolations().get(0).getViolationDisplay());
                } else {
                    violationDescText.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (TPApplication.getInstance().getNetOnOff().equals("Y")) {
            status = true;
            statusIndicatorImageView1.setImageResource(R.drawable.red_status_btn);
        } else if (TPApplication.getInstance().getNetOnOff().equals("N")) {
            status = false;
            statusIndicatorImageView1.setImageResource(R.drawable.green_status_btn_bk);
        } else {
            if (isServiceAvailable) {
                statusIndicatorImageView1.setImageResource(R.drawable.green_status_btn_bk);
            }
        }

        if (TPConstant.parkeon) {
            lookupThread.getHandler().lookupPlateHistory(plateNumberEditText.getText().toString());
            TPConstant.parkeon = false;
        }

        if (getIntent().getBooleanExtra("SAMTRANS", false)) {
            try {
                String location = getIntent().getStringExtra("LOC");
                assert location != null;
                activeTicket.setLocation(location.toUpperCase());
                locationEditText.setText(location);
                String violationId = Feature.getFeatureValue(Feature.SAMTRANS); //bad somewhere violation id int
                Violation defaultViolation = Violation.getViolationById(Integer.parseInt(violationId));
                Violation violation = Violation.getViolationById(defaultViolation.getId());
                if (violation == null || isDuplicateViolation(violation.getId())) {
                    return;
                }
                if (checkViolationLimits()) {
                    Toast.makeText(getApplicationContext(), "Exceeded max violations limit.", Toast.LENGTH_SHORT).show();
                    return;
                }
                confirmAddViolation(defaultViolation, true);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e);
            }
        }

        if (getIntent().getBooleanExtra("PARKEON", false)) {
            try {
                String location = getIntent().getStringExtra("LOC");
                assert location != null;
                activeTicket.setLocation(location.toUpperCase());
                locationEditText.setText(location.toUpperCase());
                String violationId = Feature.getFeatureValue(Feature.PARKEON); //bad somewhere violation id int
                Violation defaultViolation = Violation.getViolationById(Integer.valueOf(violationId));
                Violation violation = Violation.getViolationById(defaultViolation.getId());
                if (violation == null || isDuplicateViolation(violation.getId())) {
                    return;
                }
                if (checkViolationLimits()) {
                    Toast.makeText(getApplicationContext(), "Exceeded max violations limit.", Toast.LENGTH_SHORT).show();
                    return;
                }
                confirmAddViolation(defaultViolation, true);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e);
            }
        }

        if (getIntent().getBooleanExtra("PARKMOBILE", false)) {
            try {

                String violationId = Feature.getFeatureValue(Feature.PARK_MOBILE); //bad somewhere violation id int
                Violation defaultViolation = Violation.getViolationById(Integer.valueOf(violationId));
                Violation violation = Violation.getViolationById(defaultViolation.getId());
                if (violation == null || isDuplicateViolation(violation.getId())) {
                    return;
                }
                if (checkViolationLimits()) {
                    Toast.makeText(getApplicationContext(), "Exceeded max violations limit.", Toast.LENGTH_SHORT).show();
                    return;
                }
                confirmAddViolation(defaultViolation, true);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e);
            }
        }

        if (violationBtn != null) {
            if (TPApp.stickyViolations) {
                violationBtn.setBackgroundResource(R.drawable.btn_red);

            } else {
                violationBtn.setBackgroundResource(R.drawable.btn_yellow);
            }
        }

        if (activeTicket != null && activeTicket.getCitationNumber() == 0) {
            activeTicket = TPApp.getActiveTicket();
            if (activeTicket == null) {
                activeTicket = TPApp.createNewTicket();
            }
            /*TPUtility.showErrorToast(WriteTicketActivity.this,
                    "There is an internal error, please contact your administrator immediately.");
            finish();*/
        }

        try {
            if (countDownTimer != null) {
                countDownTimer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().log(e.getMessage());

        }
        try {
            if (TPApp.activeDutyInfo != null && TPApp.activeDutyInfo.getAllowTicket().equalsIgnoreCase("N")) {
                discardTicket();
                setResult(RESULT_OK);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().log(e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.flashCamera != null) {
            this.flashCamera.stopPreview();
            this.flashCamera.release();
            this.flashCamera = null;
        }
        if (this.mCameraManager != null) {
            this.mCameraManager = null;
        }
       /* try {
            if (myLocationReceiver != null) {
                unregisterReceiver(myLocationReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onUserInteraction() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void populatePermitValues(Permit historyPermit) {
        String plate = historyPermit.getPlate();
        String vin = historyPermit.getVin();
        bodyCode = historyPermit.getBodyCode();
        colorCode = historyPermit.getColorCode();
        makeCode = historyPermit.getMakeCode();
        String stateCode = historyPermit.getStateCode();

        // Get Expiration Date
        String expDate = "";
        if (historyPermit.getExpiration() != null) {
            String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            String monthName = "";
            int month = historyPermit.getExpiration().getMonth();
            if (month > 0) {
                monthName = months[month];
            }

            expDate = monthName + "/" + (historyPermit.getExpiration().getYear() + 1900);
        }

        String bodyTitle = "";
        String colorTitle = "";
        String makeTitle = "";
        Body body = Body.getBodyByCode(bodyCode);
        if (body != null) {
            bodyTitle = body.getTitle();
            bodyCode = body.getCode();
            bodyId = body.getId();
        }

        Color color = Color.getColorByCode(colorCode);
        if (color != null) {
            colorTitle = color.getTitle();
            colorCode = color.getCode();
            colorId = color.getId();
        }

        MakeAndModel make = MakeAndModel.getMakeByCode(makeCode);
        if (make != null) {
            makeTitle = make.getMakeTitle();
            makeCode = make.getMakeCode();
            makeId = make.getMakeId();
        }

        if (stateCode != null) {
            if (!stateCode.equals("")) {
                stateId = State.getStateIdByCode(stateCode);
                stateEditText.setText(StringUtil.getDisplayString(stateCode));
            } else {
                State defaultState = State.getDefaultState(this);
                if (defaultState != null) {
                    stateEditText.setText(defaultState.getCode());
                    stateId = defaultState.getId();
                }
            }
        }
        colorEditText.setText(StringUtil.getDisplayString(colorTitle));
        bodyEditText.setText(StringUtil.getDisplayString(bodyTitle));
        makeEditText.setText(StringUtil.getDisplayString(makeTitle));
        plateNumberEditText.setText(StringUtil.getDisplayString(plate));
        vinNumberEditText.setText(StringUtil.getDisplayString(vin));
        expEditText.setText(getExpDateString(StringUtil.getDisplayString(expDate)));
        //expEditText.setText(expDate);

        if (permitEditText.getText().toString().isEmpty()) {
            permitEditText.setText(historyPermit.getPermitNumber());
        }

    }

    public void populateValues(Ticket ticket, boolean isLookupRequired) {
        if (ticket == null) {
            return;
        }

        // Mark for value updates to ignore auto lookups
        isUpdatingResult = true;

        vinNumberEditText.setText(StringUtil.getDisplayString(ticket.getVin()));
        plateNumberEditText.setText(StringUtil.getDisplayString(ticket.getPlate()));
        expEditText.setText(getExpDateString(StringUtil.getDisplayString(ticket.getExpiration())));

        // Apply IDs and Codes
        stateId = ticket.getStateId();
        if (!StringUtil.isEmpty(ticket.getStateCode())) {
            stateId = State.getStateIdByCode(ticket.getStateCode());
            stateEditText.setText(StringUtil.getDisplayString(ticket.getStateCode()));
        } else if (stateId != 0) {
            stateEditText.setText(State.getStateCodeById(stateId));
        } else {
            State defaultState = State.getDefaultState(this);
            if (defaultState != null) {
                stateEditText.setText(defaultState.getCode());
                stateId = defaultState.getId();
            }
        }

        bodyId = ticket.getBodyId();
        if (bodyId != 0) {
            Body body = Body.getBodyById(bodyId);
            if (body != null) {
                bodyCode = body.getCode();
                bodyEditText.setText(StringUtil.getDisplayString(body.getTitle()));
            }
        } else if (!StringUtil.isEmpty(ticket.getBodyCode())) {
            Body body = Body.getBodyByCode(ticket.getBodyCode());
            if (body != null) {
                bodyCode = body.getCode();
                bodyEditText.setText(StringUtil.getDisplayString(body.getTitle()));
            }
        }

        colorId = ticket.getColorId();
        if (colorId != 0) {
            Color color = Color.getColorById(colorId);
            if (color != null) {
                colorCode = color.getCode();
                colorEditText.setText(StringUtil.getDisplayString(color.getTitle()));
            }
        } else if (!StringUtil.isEmpty(ticket.getColorCode())) {
            Color color = Color.getColorByCode(ticket.getColorCode());
            if (color != null) {
                colorCode = color.getCode();
                colorEditText.setText(StringUtil.getDisplayString(color.getTitle()));
            }
        }

        makeId = ticket.getMakeId();
        if (makeId != 0) {
            MakeAndModel make = MakeAndModel.getMakeById(makeId);
            if (make != null) {
                makeCode = make.getMakeCode();
                makeEditText.setText(StringUtil.getDisplayString(make.getMakeTitle()));
            }
        } else if (!StringUtil.isEmpty(ticket.getMakeCode())) {
            MakeAndModel make = MakeAndModel.getMakeByCode(ticket.getMakeCode());
            if (make != null) {
                makeCode = make.getMakeCode();
                makeEditText.setText(StringUtil.getDisplayString(make.getMakeTitle()));
            }
        }

        if (TPApp.stickyViolations) {
            violationBtn.setBackgroundResource(R.drawable.btn_red);
        } else {
            violationBtn.setBackgroundResource(R.drawable.btn_yellow);
        }

        photosBtn.setText("(" + activeTicket.getTicketPictures().size() + ")");

        // Mark value updates completed
        isUpdatingResult = false;

        if (isLookupRequired) {
            // Populate Vehicle Info Only
            spaceEditText.setText(StringUtil.getDisplayString(ticket.getSpace()));

            locationEditText.setText(TPUtility.getFullAddress(ticket));
            permitEditText.setText(StringUtil.getDisplayString(ticket.getPermit()));
            violationBtn.setText("V (" + activeTicket.getTicketViolations().size() + ")");

            if (activeTicket.getTicketViolations().size() > 0) {
                violationDescText.setText(activeTicket.getTicketViolations().get(0).getViolationDisplay());
            } else {
                violationDescText.setText("");
            }


            if (TPApp.getStickyViolation() != null) {
                violationBtn.setText("V (" + 1 + ")");
                violationDescText.setText(TPApp.getStickyViolation().getViolationDesc());

                activeTicket.getTicketViolations().add(TPApp.getStickyViolation());
            }


           /* if (TPApp.getLastPhotos() != null && TPApp.getLastPhotos().size()>0) {

                activeTicket.setTicketPictures(TPApp.getLastPhotos());
                if (activeTicket.getTicketPictures().size() > 0) {
                    photosBtn.setText("(" + activeTicket.getTicketPictures().size() + ")");
                } else {
                    photosBtn.setText("(0)");
                }
            }*/

            if (activeTicket.isChalked() && TPApp.getStickyViolation() != null) {
                activeTicket.getTicketViolations().clear();
                activeTicket.getTicketViolations().add(TPApp.getStickyViolation());
            }

            // Disable TIME when chalked
            if (ticket.isChalked()) {
                tmEditText.setText(DateUtil.getStringFromDateShortYear(ticket.getTimeMarked()));
                meterNumberEditText.setText(StringUtil.getDisplayString(ticket.getMeter()));
                tmEditText.setFocusable(false);
                tmEditText.setFocusableInTouchMode(false);

                try {
                    ArrayList<ChalkPicture> chalkPictures = ChalkPicture.getChalkPictures(ticket.getChalkId());
                    for (int i = 0; i < chalkPictures.size(); i++) {

                        ChalkPicture chalkPicture = chalkPictures.get(i);
                        //ChalkPicture chalkPicture = ChalkPicture.getChalkPictureById(ticket.getChalkId());
                        if (chalkPicture != null && !StringUtil.isEmpty(chalkPicture.getImagePath())) {
                            TicketPicture picture = new TicketPicture();
                            picture.setTicketId(activeTicket.getTicketId());
                            picture.setCitationNumber(activeTicket.getCitationNumber());
                            picture.setCustId(activeTicket.getCustId());
                            picture.setImagePath(chalkPicture.getImagePath());
                            picture.setDownloadImageUrl(chalkPicture.getDownloadImage());
                            picture.setImageResolution(chalkPicture.getImageResolution());
                            picture.setImageSize(chalkPicture.getImageSize());
                            //picture.setPictureId(ChalkPicture.getNextPrimaryId());
                            picture.setPictureDate(chalkPicture.getChalkDate());
                            picture.setMarkPrint("N");

                            activeTicket.getTicketPictures().add(picture);
                            photosBtn.setText("(" + activeTicket.getTicketPictures().size() + ")");
                        }
                    }
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            }

            // Start Lookup after 300MS seconds
            if (ticket.getPlate() != null && ticket.getPlate().length() > 0) {
                final Handler handler = new Handler();
                final String plate = ticket.getPlate();
                handler.postDelayed(() -> lookupThread.getHandler().lookupPlateHistory(plate), 500);
            }
            //if (activeTicket.getLprNotificationId()!=null )
        }
    }

    public void populatePermitValues(Permit ticket, boolean isLookupRequired, Ticket ticketHistory) {
        if (ticket == null) {
            return;
        }

        // Mark for value updates to ignore auto lookups
        isUpdatingResult = true;

        vinNumberEditText.setText(StringUtil.getDisplayString(ticket.getVin()));
        plateNumberEditText.setText(StringUtil.getDisplayString(ticket.getPlate()));
        if (ticketHistory != null) {
            expEditText.setText(getExpDateString(StringUtil.getDisplayString(ticketHistory.getExpiration())));
        }
        // Apply IDs and Codes
        stateId = ticket.getStateId();
        if (!StringUtil.isEmpty(ticket.getStateCode())) {
            stateId = State.getStateIdByCode(ticket.getStateCode());
            stateEditText.setText(StringUtil.getDisplayString(ticket.getStateCode()));
        } else if (stateId != 0) {
            stateEditText.setText(State.getStateCodeById(stateId));
        } else {
            State defaultState = State.getDefaultState(this);
            if (defaultState != null) {
                stateEditText.setText(defaultState.getCode());
                stateId = defaultState.getId();
            }
        }

        bodyId = ticket.getBodyId();
        if (bodyId != 0) {
            Body body = Body.getBodyById(bodyId);
            if (body != null) {
                bodyCode = body.getCode();
                bodyEditText.setText(StringUtil.getDisplayString(body.getTitle()));
            }
        } else if (!StringUtil.isEmpty(ticket.getBodyCode())) {
            Body body = Body.getBodyByCode(ticket.getBodyCode());
            if (body != null) {
                bodyCode = body.getCode();
                bodyEditText.setText(StringUtil.getDisplayString(body.getTitle()));
            }
        }

        colorId = ticket.getColorId();
        if (colorId != 0) {
            Color color = Color.getColorById(colorId);
            if (color != null) {
                colorCode = color.getCode();
                colorEditText.setText(StringUtil.getDisplayString(color.getTitle()));
            }
        } else if (!StringUtil.isEmpty(ticket.getColorCode())) {
            Color color = Color.getColorByCode(ticket.getColorCode());
            if (color != null) {
                colorCode = color.getCode();
                colorEditText.setText(StringUtil.getDisplayString(color.getTitle()));
            }
        }

        makeId = ticket.getMakeId();
        if (makeId != 0) {
            MakeAndModel make = MakeAndModel.getMakeById(makeId);
            if (make != null) {
                makeCode = make.getMakeCode();
                makeEditText.setText(StringUtil.getDisplayString(make.getMakeTitle()));
            }
        } else if (!StringUtil.isEmpty(ticket.getMakeCode())) {
            MakeAndModel make = MakeAndModel.getMakeByCode(ticket.getMakeCode());
            if (make != null) {
                makeCode = make.getMakeCode();
                makeEditText.setText(StringUtil.getDisplayString(make.getMakeTitle()));
            }
        }

        if (TPApp.stickyViolations) {
            violationBtn.setBackgroundResource(R.drawable.btn_red);
        } else {
            violationBtn.setBackgroundResource(R.drawable.btn_yellow);
        }

        photosBtn.setText("(" + activeTicket.getTicketPictures().size() + ")");

        // Mark value updates completed
        isUpdatingResult = false;

        if (ticketHistory != null) {
            // Populate Vehicle Info Only
            spaceEditText.setText(StringUtil.getDisplayString(ticketHistory.getSpace()));
            locationEditText.setText(TPUtility.getFullAddress(ticketHistory));
            permitEditText.setText(StringUtil.getDisplayString(ticketHistory.getPermit()));
            violationBtn.setText("V (" + activeTicket.getTicketViolations().size() + ")");

            if (activeTicket.getTicketViolations().size() > 0) {
                violationDescText.setText(activeTicket.getTicketViolations().get(0).getViolationDisplay());
            } else {
                violationDescText.setText("");
            }

            // Disable TIME when chalked
            if (ticketHistory.isChalked()) {
                tmEditText.setText(DateUtil.getStringFromDateShortYear(ticketHistory.getTimeMarked()));
                meterNumberEditText.setText(StringUtil.getDisplayString(ticketHistory.getMeter()));
                tmEditText.setFocusable(false);
                tmEditText.setFocusableInTouchMode(false);

                try {
                    ChalkPicture chalkPicture = ChalkPicture.getChalkPictureById(ticketHistory.getChalkId());
                    if (chalkPicture != null && !StringUtil.isEmpty(chalkPicture.getImagePath())) {
                        TicketPicture picture = new TicketPicture();
                        picture.setTicketId(activeTicket.getTicketId());
                        picture.setCitationNumber(activeTicket.getCitationNumber());
                        picture.setCustId(activeTicket.getCustId());
                        picture.setImagePath(chalkPicture.getImagePath());
                        picture.setImageResolution(chalkPicture.getImageResolution());
                        picture.setImageSize(chalkPicture.getImageSize());
                        //picture.setPictureId(ChalkPicture.getNextPrimaryId());
                        picture.setPictureDate(chalkPicture.getChalkDate());
                        picture.setMarkPrint("N");

                        activeTicket.getTicketPictures().add(picture);

                        photosBtn.setText("(" + activeTicket.getTicketPictures().size() + ")");
                    }
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                    FirebaseCrashlytics.getInstance().log(e.getMessage());
                }
            }

            // Start Lookup after 300MS seconds
			/*if (ticket.getPlate() != null && ticket.getPlate().length() > 0) {
				final Handler handler = new Handler();
				final String plate = ticket.getPlate();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						lookupThread.getHandler().lookupPlateHistory(plate);
					}
				}, 300);
			}*/
        }
    }

    public void TMEditAction(View view) {
        if (!Feature.isFeatureAllowed(Feature.ALLOW_TM_EDIT) || activeTicket.isChalked()) {
            return;
        }

        showTMDatePicker(true);
    }

    private void showTMDatePicker(boolean dateRequired) {
        final Calendar cal = Calendar.getInstance();

        final TimePickerDialog.OnTimeSetListener timePicker = (view, hourOfDay, minute) -> {
            pickerDate.setHours(hourOfDay);
            pickerDate.setMinutes(minute);

            if (pickerDate.getTime() > new Date().getTime()) {
                TPUtility.showErrorToast(WriteTicketActivity.this, "Ticket date can not be a future date.");

                showTMDatePicker(false);
                return;
            }

            tmEditText.setText(DateUtil.getStringFromDate(pickerDate));
            activeTicket.setTimeMarked(pickerDate);
        };


        final DatePickerDialog.OnDateSetListener datePicker = (view, year, monthOfYear, dayOfMonth) -> {
            pickerDate.setYear(year - 1900);
            pickerDate.setMonth(monthOfYear);
            pickerDate.setDate(dayOfMonth);
            if (Build.VERSION.SDK_INT > 22) {
                new TimePickerDialog(WriteTicketActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar, timePicker, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
            } else {
                new TimePickerDialog(WriteTicketActivity.this, timePicker, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
            }
        };

        if (pickerDate == null)
            pickerDate = new Date();

        if (dateRequired) {
            DatePickerDialog datePickerDialog;
            if (android.os.Build.VERSION.SDK_INT > 22) {
                datePickerDialog = new DatePickerDialog(WriteTicketActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar, datePicker,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            } else {
                datePickerDialog = new DatePickerDialog(WriteTicketActivity.this, datePicker,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            }

            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

            datePickerDialog.show();

        } else {
            if (android.os.Build.VERSION.SDK_INT > 22) {
                new TimePickerDialog(WriteTicketActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar, timePicker, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
            } else {
                new TimePickerDialog(WriteTicketActivity.this, timePicker, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void bindDataAtLoadingTime() {
        bindDataAtLoadingTimeRetrofit();


      /*  {
            progressDialog = ProgressDialog.show(this, "", "Loading...");
            new Thread(() -> {
                Looper.prepare();
                try {
                    activeTicket = TPApp.getActiveTicket();
                    if (activeTicket == null) {
                        activeTicket = TPApp.createNewTicket();
                    }
                    activeTicket.setIsGPSLocation("N");
                    dataLoadingHandler.sendEmptyMessage(1);


                    if (Ticket.isDuplicateCitation(activeTicket.getCitationNumber())) {
                        runOnUiThread(() -> showApplicationError("Ticket already exits for citation " + activeTicket.getCitationNumber()));

                    } else {
                        SystemBackup backup = CSVUtility.getSystemBackupCSV();
                        if (backup != null && !TPUtility.prefixZeros(activeTicket.getCitationNumber(), 8)
                                .equalsIgnoreCase(backup.getCitationNumber())) {
                            runOnUiThread(() -> showApplicationError("Citation " + activeTicket.getCitationNumber() + " is not in sync"));

                        }
                    }

                    TPTask validateTask = new TPTask() {
                        @Override
                        public void execute() {
                            try {
                                WriteTicketBLProcessor bl = ((WriteTicketBLProcessor) screenBLProcessor);
                                final JSONObject response;
                                response = bl.getValidTicket(TPApp.custId, activeTicket.getCitationNumber());
                                if (response != null && response.has("serviceError")) {
                                    runOnUiThread(() -> {
                                        try {
                                            showApplicationError(response.getString("serviceError"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    validateTask.execute();
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            }).start();
        }*/
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void bindDataAtLoadingTimeRetrofit() {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        try {
            activeTicket = TPApp.getActiveTicket();
            if (activeTicket == null) {
                activeTicket = TPApp.createNewTicket();
            }
            activeTicket.setIsGPSLocation("N");
            dataLoadingHandler.sendEmptyMessage(1);


            if (Ticket.isDuplicateCitation(activeTicket.getCitationNumber())) {
                showApplicationError("Ticket already exits for citation " + activeTicket.getCitationNumber());

            } else {
                SystemBackup backup = CSVUtility.getSystemBackupCSV();
                if (TPApp.getDeviceInfo() != null) {
                    activeTicket.setCitationNumber(TPApp.getDeviceInfo().getCurrentCitationNumber());
                    if (backup != null && !TPUtility.prefixZeros(activeTicket.getCitationNumber(), 8).equalsIgnoreCase(backup.getCitationNumber())) {
                        showApplicationError("Citation " + activeTicket.getCitationNumber() + " is not in sync");

                    }
                }
            }
            ValidTicketRequest_Rpc requestPOJO = new ValidTicketRequest_Rpc();
            ValidTicketParams params = new ValidTicketParams();
            params.setCustId(TPApp.custId);
            params.setCitationNumber(String.valueOf(activeTicket.getCitationNumber()));
            requestPOJO.setParams(params);
            requestPOJO.setMethod("getValidTicket");
            requestPOJO.setId("82F85DB43CBF6");
            requestPOJO.setJsonrpc("2.0");

            ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
            api.getValidTicket(requestPOJO).enqueue(new Callback<ValidResult>() {
                @Override
                public void onResponse(Call<ValidResult> call, Response<ValidResult> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                       /* JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().log(e.getMessage());
                        }*/

                        if (response.body() != null && response.body().getResult().getResult() == false) {
                            try {
                                showApplicationError(response.body().getResult().getServiceError());
                            } catch (Exception e) {
                                e.printStackTrace();
                                FirebaseCrashlytics.getInstance().log(e.getMessage());

                            }

                        }

                    }

                }

                @Override
                public void onFailure(Call<ValidResult> call, Throwable t) {
                    progressDialog.dismiss();
                    log.error(TPUtility.getPrintStackTrace(t));
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            log.error(TPUtility.getPrintStackTrace(e));
            FirebaseCrashlytics.getInstance().log(e.getMessage());
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            int returnMessage = 0;
            try {
                if (data != null)
                    returnMessage = Objects.requireNonNull(data.getExtras()).getInt("message");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (debug > 0)
                Log.d(tag, "onActivityResult:  requestCode=" + requestCode + ", resultCode=" + resultCode + ", data=" + data + ", ptsIntent=" + ptsIntent + ", returnMessage=" + returnMessage);
            if (returnMessage == PT_ANPR_NOTONWHITELIST) {
                alprMode = true;
                String sRegNumber = data.getExtras().getString("anpr_not_in_whitelist");
                int regConf = data.getExtras().getInt("anpr_not_in_whitelist_conf");
                alprPlateNum = sRegNumber;
                if (Feature.getFeatureValue(Feature.ALPR_RETURN_PROCESS).equalsIgnoreCase("LPR")) {
                    LPRAction(null);
                } else if (Feature.getFeatureValue(Feature.ALPR_RETURN_PROCESS).equalsIgnoreCase("lookup")) {
                    plateNumberEditText.setText(sRegNumber);
                    alprLookup = true;
                    lookupThread.getHandler().lookupPlateHistory(plateNumberEditText.getText().toString());
                } else if (Feature.getFeatureValue(Feature.ALPR_RETURN_PROCESS).equalsIgnoreCase("getapermit")) {
                    plateNumberEditText.setText(sRegNumber);
                    lookupThread.getHandler().doPlatePermitLookup(sRegNumber);
                }
                //plateNumberEditText.setText(sRegNumber);
                Toast.makeText(this, "PTS returned with vehicle plate that is not in the whitelist: " + sRegNumber + " (conf=" + regConf + ")", Toast.LENGTH_LONG).show();
            } else if (returnMessage == PT_ANPR_PERMITEXPIRED) {
                alprMode = true;
                String sRegNumber = data.getExtras().getString("anpr_permit_expired");
                int regConf = data.getExtras().getInt("anpr_permit_expired_conf");
                alprPlateNum = sRegNumber;
                if (Feature.getFeatureValue(Feature.ALPR_RETURN_PROCESS).equalsIgnoreCase("LPR")) {
                    LPRAction(null);
                } else if (Feature.getFeatureValue(Feature.ALPR_RETURN_PROCESS).equalsIgnoreCase("lookup")) {
                    plateNumberEditText.setText(sRegNumber);
                    alprLookup = true;
                    lookupThread.getHandler().lookupPlateHistory(sRegNumber);
                } else if (Feature.getFeatureValue(Feature.ALPR_RETURN_PROCESS).equalsIgnoreCase("getapermit")) {
                    plateNumberEditText.setText(sRegNumber);
                    lookupThread.getHandler().doPlatePermitLookup(sRegNumber);
                }
                //plateNumberEditText.setText(sRegNumber);
                String sTimeExceeded = data.getExtras().getString("time_since_permit_expired");
                Toast.makeText(this, "PTS returned with whitelisted plate: " + sRegNumber + " (conf=" + regConf + ") having exceeded parking permit by " + sTimeExceeded, Toast.LENGTH_LONG).show();
            } else if (returnMessage == PT_ANPR_SCANTIMEOUT) {
                Toast.makeText(this, "PTS returned after scan timeout", Toast.LENGTH_LONG).show();
            } else if (returnMessage == PT_LICENSE_MISSING_OR_INVALID) {
                alprMode = true;
                final String deviceID = data.getExtras().getString("duid"); //unique device ID
                final WriteTicketActivity caller = this;
                //obtain new license key
                new AlertDialog.Builder(this)
                        .setTitle("License Verification Problem")
                        .setCancelable(false)
                        .setMessage("PTS reports: license key missing or invalid. Please ensure that your device's WiFi adapter is enabled and has Internet access, then " +
                                "click <" + this.getString(android.R.string.ok) + "> to (re)generate a valid license key from our server.")
                        .setPositiveButton(android.R.string.ok,
                                (dialog, which) -> {
                                    dialog.dismiss();
                                    // try to obtain new license key from Imense Server
                                    new ImenseLicenseServer(caller, deviceID).execute();
                                })
                        .setNegativeButton(android.R.string.cancel,
                                (dialog, which) -> dialog.dismiss()).show();
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            e.printStackTrace();
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        try {
            switch (requestCode) {
                case REQUEST_LOOKUP: {
                    if (data != null && data.hasExtra("STATE")) {
                        stateEditText.setText(data.getStringExtra("STATE"));
                        stateId = data.getIntExtra("StateId", stateId);
                    }

                    if (data != null && data.hasExtra("EXP")) {
                        expEditText.setText(getExpDateString(Objects.requireNonNull(data.getStringExtra("EXP"))));
                        activeTicket.setExpiration(getExpDateString(Objects.requireNonNull(data.getStringExtra("EXP"))));

                        ticketTemp.setExpiration(activeTicket.getExpiration());
                        if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                            TicketTemp.insertTicket(ticketTemp);
                        }

                        if (makeEditText.getText().toString().isEmpty() && isAutoPromptVehicle()) {
                            searchMake(makeEditText.getRootView());
                        }
                    }

                    if (data != null && data.hasExtra("MAKE")) {
                        makeEditText.setText(data.getStringExtra("MAKE"));
                        makeId = data.getIntExtra("MakeId", 0);
                        makeCode = data.getStringExtra("MakeCode");

                        ticketTemp.setMakeCode(makeCode);
                        ticketTemp.setMakeId(makeId);
                        if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                            TicketTemp.insertTicket(ticketTemp);
                        }

                        if (colorEditText.getText().toString().isEmpty() && isAutoPromptVehicle()) {
                            searchColor(colorEditText.getRootView());
                        }
                    }

                    if (data != null && data.hasExtra("BODY")) {
                        bodyEditText.setText(data.getStringExtra("BODY"));
                        bodyId = data.getIntExtra("BodyId", 0);
                        bodyCode = data.getStringExtra("BodyCode");

                        ticketTemp.setBodyCode(bodyCode);
                        ticketTemp.setBodyId(bodyId);
                        if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                            TicketTemp.insertTicket(ticketTemp);
                        }
                    }

                    if (data != null && data.hasExtra("COLOR")) {
                        colorEditText.setText(data.getStringExtra("COLOR"));
                        colorId = data.getIntExtra("ColorId", 0);
                        colorCode = data.getStringExtra("ColorCode");

                        ticketTemp.setColorCode(colorCode);
                        ticketTemp.setColorId(colorId);
                        if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                            TicketTemp.insertTicket(ticketTemp);
                        }

                        if (bodyEditText.getText().toString().isEmpty() && isAutoPromptVehicle()) {
                            searchBody(bodyEditText.getRootView());
                        }
                    }

                    if (data != null && data.hasExtra("PLATE")) {
                        plateNumberEditText.setText(data.getStringExtra("PLATE"));
                        ticketTemp.setPlate(data.getStringExtra("PLATE"));
                        if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                            TicketTemp.insertTicket(ticketTemp);
                        }
                    }

                    if (data != null && data.hasExtra("VIN")) {
                        vinNumberEditText.setText(data.getStringExtra("VIN"));

                        ticketTemp.setVin(data.getStringExtra("VIN"));
                        if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                            TicketTemp.insertTicket(ticketTemp);
                        }
                    }

                    if (data != null && data.hasExtra("TM")) {
                        tmEditText.setText(data.getStringExtra("TM"));

                    }

                    if (data != null && data.hasExtra("Location")) {
                        activeTicket.setLocation(data.getStringExtra("Location"));
                        activeTicket.setStreetNumber(data.getStringExtra("StreetNumber"));
                        activeTicket.setStreetPrefix(data.getStringExtra("StreetPrefix"));
                        activeTicket.setStreetSuffix(data.getStringExtra("StreetSuffix"));
                        activeTicket.setDirection(data.getStringExtra("Direction"));
                        locationEditText.setText(TPUtility.getFullAddress(activeTicket));
                        activeTicket.setIsGPSLocation("Y");

                        ticketTemp.setLocation(TPUtility.getFullAddress(activeTicket));
                        if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                            TicketTemp.insertTicket(ticketTemp);
                        }

                        isGPSLocation = false;

                        if (GPSProgressBar != null) {
                            GPSProgressBar.setVisibility(View.GONE);
                        }

                        // Redirect to Location Entry Form
                        if (data.hasExtra("REDIRECT_LOCATION_FORM") && !TPApp.getUserSettings().isSkipLocationEntry()) {
                            LocationEntryAction(null);
                        }
                    }

                    if (data != null && data.hasExtra("PERMIT")) {
                        permitEditText.setText(data.getStringExtra("PERMIT"));

                        ticketTemp.setPermit(data.getStringExtra("PERMIT"));
                        if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                            TicketTemp.insertTicket(ticketTemp);
                        }
                    }

                    if (data != null && data.hasExtra("METER")) {
                        meterNumberEditText.setText(data.getStringExtra("METER"));

                        ticketTemp.setMeter(data.getStringExtra("METER"));
                        if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                            TicketTemp.insertTicket(ticketTemp);
                        }
                    }

                    break;
                }

                case REQUEST_TAKE_PICTURE: {
                    photosBtn.setText("(" + activeTicket.getTicketPictures().size() + ")");
                    break;
                }

                case REQUEST_VIOLATIONS: {
                    updateWarnings();
                    if (TPApp.activeDutyInfo.getAllowTicket().equals("W") && activeTicket.getTicketViolations().size() > 0) {
                        if (Feature.isFeatureAllowed(Feature.PARK_WARNING_RESET)) {
                            warningBtn.setClickable(true);
                            warningBtn.setAlpha(1f);
                        } else {
                            warningBtn.setClickable(false);
                            warningBtn.setAlpha(0.5f);
                        }

                    } else {
                        warningBtn.setClickable(true);
                        warningBtn.setAlpha(1f);
                    }
                    violationBtn.setText("V (" + activeTicket.getTicketViolations().size() + ")");

                    if (activeTicket.getTicketViolations().size() > 0) {
                        if (Feature.getFeatureValue(Feature.PARK_PLATE_REG).equalsIgnoreCase("ALL")) {
                            regButton.setVisibility(View.VISIBLE);

                        } else {

                            regButton.setVisibility(View.GONE);
                        }
                        TicketViolation violation = activeTicket.getTicketViolations().get(0);
                        violationDescText.setText(violation.getViolationDisplay());
                        if (isServiceAvailable) {
                            if (Feature.isFeatureAllowed(Feature.PARK_PLATE_REG)) {
                                //  regButton.setVisibility(View.VISIBLE);
                                for (int i = 0; i < activeTicket.getTicketViolations().size(); i++) {

                                   /* String str="string,with,comma";
                                    ArrayList aList= new ArrayList(Arrays.asList(str.split(",")));
                                    for(int i=0;i<aList.size();i++)
                                    {
                                        System.out.println(" -->"+aList.get(i));
                                    }*/

                                    if (Feature.getFeatureValue(Feature.PARK_PLATE_REG) != null && Feature.getFeatureValue(Feature.PARK_PLATE_REG).contains(",")) {
                                        String featurestring = Feature.getFeatureValue(Feature.PARK_PLATE_REG);
                                        ArrayList<String> fValueList = new ArrayList<>(Arrays.asList(featurestring.split(",")));
                                        for (int j = 0; j < fValueList.size(); j++) {
                                            if (String.valueOf(activeTicket.getTicketViolations().get(i).getViolationId()).equals(fValueList.get(j))) {
                                                regButton.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        //regButton.setVisibility(View.VISIBLE);
                                    } else {
                                        if (Feature.isFeatureAllowed(Feature.PARK_PLATE_REG) && Feature.getFeatureValue(Feature.PARK_PLATE_REG).equalsIgnoreCase("ALL")) {
                                            regButton.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            } else {
                                if (Feature.getFeatureValue(Feature.PARK_PLATE_REG).equalsIgnoreCase("ALL")) {
                                    regButton.setVisibility(View.VISIBLE);

                                } else {

                                    regButton.setVisibility(View.GONE);
                                }
                            }

                        }
                    } else {
                        violationDescText.setText("");
                        if (Feature.getFeatureValue(Feature.PARK_PLATE_REG).equalsIgnoreCase("ALL")) {
                            regButton.setVisibility(View.VISIBLE);

                        } else {

                            regButton.setVisibility(View.GONE);
                        }
                    }

                    break;
                }

                case REQUEST_PREVIEW: {
                    printAction(null);
                    log.debug("-----------------2. saveTicket: " + DateUtil.getCurrentDateTime1() + "-------------");

                    break;
                }

                case REQUEST_LPR: {
                    if (data != null && data.hasExtra("State")) {
                        String state = data.getStringExtra("State");
                        State st = State.getStateByName(state);
                        if (st != null) {
                            stateId = st.getId();
                            stateEditText.setText(st.getCode());
                        }
                    }

                    if (data != null && data.hasExtra("PlateNumber")) {

                        String plate = data.getStringExtra("PlateNumber");
                        plateNumberEditText.setText(plate);

                        // Do Plate validation and lookup on successful LPR request
                        plate = TPUtility.getValidPlate(plate);
                        plateNumberEditText.setText(plate);
                        String state = stateEditText.getText().toString();
                        if (!TPUtility.isValidPlateNumber(plate, state) || !StringUtil.isAlphaNumeric(plate)) {
                            TPUtility.showErrorToast(WriteTicketActivity.this, "Plate format is incorrect. Please validate it.");
                            plateNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                        } else {
                            plateNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                        }
                        if (plate != null) {
                            if (isServiceAvailable) {
                                lookupThread.getHandler().lookupPlateHistory(plate);
                            }
                        }
                        prevPlate = plate;
                    }
                    if (data != null && data.hasExtra("Color")) {

                        colorEditText.setText(data.getStringExtra("Color"));
                    }

                    if (data != null && data.hasExtra("Body")) {
                        bodyEditText.setText(data.getStringExtra("Body"));
                    }

                    if (data != null && data.hasExtra("Make")) {
                        makeEditText.setText(data.getStringExtra("Make"));
                    }

                    if (data != null && data.hasExtra("PlateImageFile")) {
                        DeviceInfo deviceInfo = TPApp.getDeviceInfo();
                        long photoNumber = 0;//deviceInfo.getCurrentPhotoNumber()+1;
                        //long citeNumber = deviceInfo.getCurrentCitationNumber() + 1;
                        long citeNumber = 0;//deviceInfo.getCurrentCitationNumber();
                        String filename;
                        File dstFile = null;
                        boolean updateDevicePhotoCount = false;
                        if (activeTicket.isLPR()) {
                            try {
                                if (activeTicket.getTicketPictures().size() > 0) {
                                    for (int i = 0; i < activeTicket.getTicketPictures().size(); i++) {
                                        TicketPicture tp = activeTicket.getTicketPictures().get(i);
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
                            updateDevicePhotoCount = true;
                            photoNumber = deviceInfo.getCurrentPhotoNumber() + 1;
                            //long citeNumber = deviceInfo.getCurrentCitationNumber() + 1;
                            citeNumber = deviceInfo.getCurrentCitationNumber();
                            filename = TPUtility.prefixZeros(citeNumber, 8) + "-TICKET-LPR-" + photoNumber + ".JPG";
                            dstFile = new File(TPUtility.getTicketsFolder(), filename);
                        }
                        File srcFile = new File(Objects.requireNonNull(data.getStringExtra("PlateImageFile")));
                        String resolution = data.getStringExtra("Resolution");
                        String imageSize = data.getStringExtra("ImageSize");

                        TicketPicture picture;
                        try {
                            if (dstFile != null) {
                                dstFile.createNewFile();
                            }
                            TPUtility.copy(srcFile, dstFile, true);

                            picture = new TicketPicture();
                            picture.setPictureDate(new Date());
                            picture.setCitationNumber(activeTicket.getCitationNumber());
                            picture.setMarkPrint("Y");
                            assert dstFile != null;
                            picture.setImagePath(dstFile.getPath());
                            picture.setImageResolution(resolution);
                            picture.setImageSize(imageSize);
                            picture.setTicketId(activeTicket.getTicketId());
                            picture.setCustId(activeTicket.getCustId());

                            if (activeTicket.isLPR()) {
                                if (activeTicket.getTicketPictures().size() > 0) {
                                    for (int i = 0; i < activeTicket.getTicketPictures().size(); i++) {
                                        TicketPicture tp = activeTicket.getTicketPictures().get(i);
                                        if (tp.getImagePath() != null && tp.getImagePath().contains("LPR")) {
                                            activeTicket.getTicketPictures().set(i, picture);
                                            break;
                                        }
                                    }
                                } else {
                                    activeTicket.getTicketPictures().add(0, picture);
                                }
                            } else {
                                activeTicket.getTicketPictures().add(0, picture);
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
                            activeTicket.setIsLPR("Y");
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        } finally {
                            photosBtn.setText("(" + activeTicket.getTicketPictures().size() + ")");
                        }
                    }

                    break;
                }

                case REQUEST_PLATE_LOOKUP: {
                    if (TPApp.getActiveLookupResult() == null) {
                        break;
                    }

                    populateValues(TPApp.getActiveLookupResult().getHistoryTicket(), false);
                    TPApp.setActiveLookupResult(null);
                    break;
                }

                case REQUEST_PERMIT_BARCODE: {
                    try {
                        String text = null;
                        String type = null;
                        if (data != null) {
                            text = data.getStringExtra(Intents.Result.TEXT);
                            type = data.getStringExtra(Intents.Result.TYPE);
                        }


                        if (type != null && !(type.equals("TEXT") || type.equals("PRODUCT"))) {
                            Toast.makeText(this, "Invalid format. Please try again.", Toast.LENGTH_LONG).show();
                        } else if (text != null && !text.equals("")) {
                            permitEditText.setText(TPUtility.getValidPermit(text));
                            if (isServiceAvailable) {
                                lookupThread.getHandler().checkPermitHistory(TPUtility.getValidPermit(text));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error(e.getMessage());
                    }

                    break;
                }

                case REQUEST_METER_BARCODE: {
                    String text = null;
                    String type = null;
                    if (data != null) {
                        text = data.getStringExtra(Intents.Result.TEXT);
                        type = data.getStringExtra(Intents.Result.TYPE);
                    }
                    if (type != null && !(type.equals("TEXT") || type.equals("PRODUCT"))) {
                        Toast.makeText(this, "Invalid Data. Please try again.", Toast.LENGTH_LONG).show();

                    } else if (text != null && !text.equals("")) {
                        meterNumberEditText.setText(TPUtility.getValidMeter(text));
                        lookupThread.getHandler().checkMeterHistory(TPUtility.getValidMeter(text), false);
                    }

                    break;
                }

                case REQUEST_VIN_BARCODE: {
                    String text = null;
                    String type = null;
                    if (data != null) {
                        text = data.getStringExtra(Intents.Result.TEXT);
                        type = data.getStringExtra(Intents.Result.TYPE);
                    }
                    if (type != null && !(type.equals("TEXT") || type.equals("PRODUCT"))) {
                        Toast.makeText(this, "Invalid Data. Please try again.", Toast.LENGTH_LONG).show();

                    } else if (text != null && !text.equals("")) {
                        if (text.contains("MAKE") && text.contains("YEAR")) {

                            vinNumberEditText.setText(TPUtility.getDataFromQR(text, 1));
                            plateNumberEditText.setText(TPUtility.getDataFromQR(text, 10));
                            try {
                                String code = TPUtility.getDataFromQR(text, 5);
                                MakeAndModel make = MakeAndModel.getMakeByCode(code);
                                if (make != null) {
                                    makeEditText.setText(make.getMakeTitle());
                                } else {
                                    code = code.substring(0, 3);
                                    make = MakeAndModel.getMakeByCode(code);
                                    if (make != null) {
                                        makeEditText.setText(make.getMakeTitle());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                expEditText.setText(TPUtility.getDataFromQR(text, 12));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            lookupThread.getHandler().lookupPlateHistory(plateNumberEditText.getText().toString());
                        } else {
                            vinNumberEditText.setText(TPUtility.getValidVIN(text));
                            lookupThread.getHandler().checkVinHistory(TPUtility.getValidVIN(text));
                        }
                    }

                    break;
                }

                case REQUEST_VIN_OCRCODE: {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bundle results = extras.getBundle(ExtrasKeys.EXTRAS_RECOGNITION_RESULTS);
                        String vin = results.getString("Data");

                        if (!StringUtil.isEmpty(vin)) {
                            vinNumberEditText.setText(vin);
                            updateOCRImage("OCR_VIN_", extras.getString(ExtrasKeys.EXTRAS_IMAGE_PATH));
                        }
                    }

                    break;
                }

                case REQUEST_PLATE_OCRCODE: {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bundle results = extras.getBundle(ExtrasKeys.EXTRAS_RECOGNITION_RESULTS);
                        String plate = results.getString("Data");

                        if (!StringUtil.isEmpty(plate)) {
                            plateNumberEditText.setText(plate);
                            updateOCRImage("OCR_PLATE_", extras.getString(ExtrasKeys.EXTRAS_IMAGE_PATH));
                        }
                    }

                    break;
                }
                case REQUEST_IPS_MULTISPACE: {//concept behind this to fill auto data by minimum input
                    if (data != null) {
                        if (data.hasExtra(TPConstant.SPACE)) {
                            usingMultiSpaceIPS = true;
                            ipsMultiSpaceLotDes = data.getStringExtra(TPConstant.LOT_DESC);
                            if (data.getBooleanExtra(TPConstant.PLATE, false)) {
                                plateNumberEditText.setText(data.getStringExtra(TPConstant.SPACE));
                            } else {
                                spaceEditText.setText(data.getStringExtra(TPConstant.SPACE));
                            }
                            activeTicket.setLocation(data.getStringExtra(TPConstant.LOCATION));
                            locationEditText.setText(TPUtility.getFullAddress(activeTicket));
                            try {
                                String violationId = Feature.getFeatureValue(Feature.IPS_MULTISPACE);//bad somewhere violation id int
                                Violation defaultViolation = Violation.getViolationById(Integer.parseInt(violationId));
                                Violation violation = Violation.getViolationById(defaultViolation.getId());
                                if (violation == null || isDuplicateViolation(violation.getId())) {
                                    return;
                                }
                                if (checkViolationLimits()) {
                                    Toast.makeText(getApplicationContext(), "Exceeded max violations limit.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                confirmAddViolation(defaultViolation, true);
                            } catch (Exception e) {
                                e.printStackTrace();
                                log.error(e);
                            }
                        }
                    } else
                        Toast.makeText(getApplicationContext(), "Something went wrong. Please Re-try", Toast.LENGTH_SHORT).show();
                    break;
                }
                case REQUEST_AUTO_LPR: {

                }


            } // End of Switch Case Statement

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        try {
            TPUtility.hideSoftKeyboard(this);
        } catch (Exception ignored) {
        }
    }

    private void updateOCRImage(String ocrName, String ocrImage) {
        boolean imageFound = false;
        for (TicketPicture picture : activeTicket.getTicketPictures()) {
            if (picture.getImagePath().contains("OCR_PLATE_")) {
                TPUtility.removeFile(picture.getImagePath());
                picture.setImagePath(ocrImage);
                imageFound = true;
                break;
            }
        }

        if (!imageFound) {
            TicketPicture picture = new TicketPicture();
            picture.setImagePath(ocrImage);
            picture.setCitationNumber(activeTicket.getCitationNumber());
            picture.setTicketId(activeTicket.getTicketId());
            picture.setPictureDate(new Date());
            picture.setMarkPrint("N");
            picture.setSyncStatus("Pending");
            picture.setCustId(activeTicket.getCustId());

            activeTicket.getTicketPictures().add(picture);
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void updatePreviewSize(Intent intent) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public void permitBarcodeAction(View view) throws Exception {
        /*try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        if (!isBarcodeBtnPressed) {
            Intent intent = new Intent();
            intent.putExtra(Intents.Scan.SOURCE, Intents.Scan.EXTRA_SOURCE_INTEGRATED);
            updatePreviewSize(intent);

            intent.setClass(this, ScanBarcodeActivity.class);
            startActivityForResult(intent, REQUEST_PERMIT_BARCODE);
        }

        isBarcodeBtnPressed = false;
    }

    public void vinBarcodeAction(View view) {

        if (!isBarcodeBtnPressed) {
            Intent intent = new Intent();
            intent.putExtra(Intents.Scan.SOURCE, Intents.Scan.EXTRA_SOURCE_INTEGRATED);
            updatePreviewSize(intent);

            intent.setClass(this, ScanBarcodeActivity.class);
            startActivityForResult(intent, REQUEST_VIN_BARCODE);
        }

        isBarcodeBtnPressed = false;
    }

    public void meterBarcodeAction(View view) {

        if (!isBarcodeBtnPressed) {
            Intent intent = new Intent();
            intent.putExtra(Intents.Scan.SOURCE, Intents.Scan.EXTRA_SOURCE_INTEGRATED);
            updatePreviewSize(intent);

            intent.setClass(this, ScanBarcodeActivity.class);
            startActivityForResult(intent, REQUEST_METER_BARCODE);
        }

        isBarcodeBtnPressed = false;
    }

    public void VinNotVisibleAction(View view) {
        String vinText = vinNumberEditText.getText().toString();
        if (vinText.length() > 0) {
            return;
        }

        String vinStr = Feature.getFeatureValue(Feature.SHOW_VNV);
        if (vinStr == null) {
            vinStr = "";
        }

        vinNumberEditText.setText(vinStr);
    }

    public void viewViolationAction(View view) {
        updateActiveTicketInfo(true);

        Intent i = new Intent();
        i.setClass(WriteTicketActivity.this, ViolationsActivity.class);
        startActivityForResult(i, REQUEST_VIOLATIONS);
        return;
    }

    public void viewPhotosAction(View view) {

        if (!Feature.isFeatureAllowed(Feature.TAKE_PICTURES)) {
            Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent();
        i.setClass(WriteTicketActivity.this, PhotosActivity.class);
        startActivityForResult(i, REQUEST_TAKE_PICTURE);
        return;
    }

    public void takePictureAction(View view) {
        if (!Feature.isFeatureAllowed(Feature.TAKE_PICTURES)) {
            Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
            return;
        }

        int maxPhotos = 0;
        if (Feature.isFeatureAllowed(Feature.MAX_PHOTOS)) {
            String value = Feature.getFeatureValue(Feature.MAX_PHOTOS);
            try {
                maxPhotos = Integer.parseInt(value);

                if (activeTicket.getPhoto_count() > 0) {
                    maxPhotos = maxPhotos + activeTicket.getPhoto_count();
                }
                if (activeTicket.isLPR()) {
                    maxPhotos = maxPhotos + 1;
                }
            } catch (Exception ignored) {
            }
        }

        if (maxPhotos > 0 && activeTicket.getTicketPictures().size() >= maxPhotos) {
            displayErrorMessage("Exceeded max photos limit.");
            return;
        }

        if (this.flashCamera != null) {
            this.flashCamera.stopPreview();
            this.flashCamera.release();
            this.flashCamera = null;
        }

        Intent i = new Intent();
        i.setClass(WriteTicketActivity.this, TakePictureActivity.class);
        i.putExtra("CitationNumber", activeTicket.getCitationNumber());
        startActivityForResult(i, REQUEST_TAKE_PICTURE);
        return;
    }

    public void takeSelfiAction() {
       /* try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

		/*if (!Feature.isFeatureAllowed(Feature.TAKE_PICTURES)) {
			Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
			return;
		}

		if (this.flashCamera != null) {
			this.flashCamera.stopPreview();
			this.flashCamera.release();
			this.flashCamera = null;
		}*/

        Intent i = new Intent();
        i.setClass(WriteTicketActivity.this, TakePictureActivity.class);
        i.putExtra("CitationNumber", activeTicket.getCitationNumber());
        i.putExtra("isSelfi", true);
        startActivityForResult(i, REQUEST_TAKE_SELFI);
        return;
    }

    private void enablePrint() {
        try {
            Thread.sleep(700);
            printBtn.setEnabled(true);
            printBtn.setBackgroundResource(R.drawable.btn_blue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void disablePrint() {
        printBtn.setEnabled(false);
        printBtn.setBackgroundResource(R.drawable.btn_disabled);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showTrainingAlert() {
        User userInfo = TPApp.getUserInfo();
        if (userInfo.getBadge().equals("8888")) {
            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
            confirmBuilder.setTitle("Alert")
                    .setMessage("You are currently logged in as training.")
                    .setCancelable(false)
                    //.setNegativeButton("No", new CancelClickHandler())
                    .setPositiveButton("Continue", (dialog, which) -> {
                        showTrainingAlert = false;
                        printAction(null);
                        log.debug("1. printAction: " + DateUtil.getCurrentDateTime1());

                    });
            confirmBuilder.create().show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void printAction(View view) {


        log.info("<----------PrintButton pressed--->");
        checkSaveTask = true;
        try {
            if (TPApplication.getInstance().getActiveTicket().getPlacard() != null) {
                Placard.removeById(TPApplication.getInstance().getActiveTicket().getPlacard());
            }
            if (showTrainingAlert) {
                showTrainingAlert();
            } else {
                User userInfo = TPApp.getUserInfo();
                if (userInfo.getBadge().equals("8888")) {
                    showTrainingAlert = true;
                }
                disablePrint();

                String state = stateEditText.getText().toString();
                String plate = plateNumberEditText.getText().toString();
                String meter = meterNumberEditText.getText().toString();
                String location = locationEditText.getText().toString().trim().toUpperCase();

                String vin = vinNumberEditText.getText().toString();
                String body = bodyEditText.getText().toString();
                String color = colorEditText.getText().toString();
                String permit = permitEditText.getText().toString();

                String make = makeEditText.getText().toString();
                String expDate = expEditText.getText().toString();
                String space = spaceEditText.getText().toString();

                stateEditText.setBackgroundResource(R.drawable.text_field_bkg);
                plateNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                meterNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                locationEditText.setBackgroundResource(R.drawable.text_field_bkg);
                vinNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
                bodyEditText.setBackgroundResource(R.drawable.text_field_bkg);
                colorEditText.setBackgroundResource(R.drawable.text_field_bkg);
                tmEditText.setBackgroundResource(R.drawable.text_field_bkg);
                permitEditText.setBackgroundResource(R.drawable.text_field_bkg);
                makeEditText.setBackgroundResource(R.drawable.text_field_bkg);
                expEditText.setBackgroundResource(R.drawable.text_field_bkg);
                spaceEditText.setBackgroundResource(R.drawable.text_field_bkg);

                vin = TPUtility.getValidVIN(vin);
                vinNumberEditText.setText(vin);

                plate = TPUtility.getValidPlate(plate);
                plateNumberEditText.setText(plate);

                MakeAndModel makeRecord = MakeAndModel.getMakeByTitle(make);
                if (makeRecord != null) {
                    make = StringUtil.getDisplayString(makeRecord.getMakeTitle());
                    makeCode = StringUtil.getDisplayString(makeRecord.getMakeCode());
                    makeId = makeRecord.getMakeId();
                } else {
                    if (MakeAndModel.getMakeStandardName(make) == null) {
                        make = "";
                    }
                    makeCode = "";
                    makeId = 0;
                }

                Body bodyRecord = Body.getBodyByTitle(body);
                if (bodyRecord != null) {
                    body = StringUtil.getDisplayString(bodyRecord.getTitle());
                    bodyCode = StringUtil.getDisplayString(bodyRecord.getCode());
                    bodyId = bodyRecord.getId();
                } else {
                    if (Body.getBodyStandardName(body) == null) {
                        body = "";
                    }
                    bodyCode = "";
                    bodyId = 0;
                }

                Color colorRecord = Color.getColorByTitle(color);
                if (colorRecord != null) {
                    color = StringUtil.getDisplayString(colorRecord.getTitle());
                    colorCode = StringUtil.getDisplayString(colorRecord.getCode());
                    colorId = colorRecord.getId();
                } else {
                    color = "";
                    colorCode = "";
                    colorId = 0;
                }

                // Check for required Fields
                StringBuilder requiredFields = new StringBuilder();
                boolean requiredFlag = false;

			/*if (Feature.isRequiredField("plate") && plate.equals("")) {

				requiredFields.append("- Plate\n");
				plateNumberEditText.setBackgroundResource(R.drawable.text_field_error);
				requiredFlag = true;
			}*/

                if ((Feature.isFeatureAllowed(Feature.STATE_REQUIRED) || Feature.isRequiredField("state"))
                        && state.equals("")) {

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

                if ((Feature.isFeatureAllowed(Feature.MAKE_REQUIRED) || Feature.isRequiredField("make"))
                        && make.equals("")) {

                    requiredFields.append("- Make\n");
                    makeEditText.setBackgroundResource(R.drawable.text_field_error);
                    requiredFlag = true;
                }

                if ((Feature.isFeatureAllowed(Feature.COLOR_REQUIRED) || Feature.isRequiredField("color"))
                        && color.equals("")) {

                    requiredFields.append("- Color\n");
                    colorEditText.setBackgroundResource(R.drawable.text_field_error);
                    requiredFlag = true;
                }

                if ((Feature.isFeatureAllowed(Feature.BODY_REQUIRED) || Feature.isRequiredField("body"))
                        && body.equals("")) {

                    requiredFields.append("- Body\n");
                    bodyEditText.setBackgroundResource(R.drawable.text_field_error);
                    requiredFlag = true;
                }

                if ((Feature.isFeatureAllowed(Feature.LOCATION_REQUIRED) || Feature.isRequiredField("location"))
                        && location.equals("")) {

                    requiredFields.append("- Location\n");
                    locationEditText.setBackgroundResource(R.drawable.text_field_error);
                    requiredFlag = true;
                }

                if ((Feature.isFeatureAllowed(Feature.METER_REQUIRED) || Feature.isRequiredField("meter"))
                        && meter.equals("")) {

                    requiredFields.append("- Meter\n");
                    meterNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                    requiredFlag = true;
                }

                if ((Feature.isFeatureAllowed(Feature.PERMIT_REQUIRED) || Feature.isRequiredField("permit"))
                        && permit.equals("")) {
                    requiredFields.append("- Permit\n");
                    permitEditText.setBackgroundResource(R.drawable.text_field_error);
                    requiredFlag = true;
                }

                if (activeTicket.getTicketViolations().size() <= 0) {
                    requiredFields.append("- Violations\n");
                    requiredFlag = true;
                }

                if (requiredFlag) {
                    displayErrorMessage("Please complete required fields below -  \n\n" + requiredFields);
                    enablePrint();
                    return;
                }

                try {
                    String park_plate_alert = Feature.getFeatureValue("park_plate_alert");
                    if (park_plate_alert != null && !TextUtils.isEmpty(park_plate_alert)) {
                        String[] strArray = park_plate_alert.split(",");
                        for (String s : strArray) {
                            if (plate.equalsIgnoreCase(s)) {
                                TPUtility.showErrorToast(WriteTicketActivity.this, "Plate format is incorrect. Please validate it.");
                                plateNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                                enablePrint();
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
               /* if (plate.equalsIgnoreCase("NOPLATE") || plate.equalsIgnoreCase("VIN") ||
                        plate.equalsIgnoreCase("SAM123")||
                        plate.equalsIgnoreCase("UNK")||plate.equalsIgnoreCase("UNKNOWN")||
                        plate.equalsIgnoreCase("NONE")) {
                    TPUtility.showErrorToast(WriteTicketActivity.this, "Plate format is incorrect. Please validate it.");
                    plateNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                    enablePrint();
                    return;
                }*/

                if (plate.equals("") && !TPUtility.PlateVINValidate(vin, false)) {
                    displayErrorMessage("VIN should be either 11 or all 17 characters.");
                    vinNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                    enablePrint();
                    return;
                }

                if (!plate.equals("") && !TPUtility.PlateVINValidate(vin, true)) {
                    displayErrorMessage("VIN should be either the last 4 or all 17 characters.");
                    vinNumberEditText.setBackgroundResource(R.drawable.text_field_error);
                    enablePrint();
                    return;
                }

                boolean isCommentRequired = false;
                boolean isPhotoRequired = false;
                String requiredCommentMsg = "";
                String requiredPhotoMsg = "";

                for (TicketViolation violation : activeTicket.getTicketViolations()) {
                    if (violation.getRequiredComments() > violation.getTicketComments().size()) {
                        isCommentRequired = true;
                        requiredCommentMsg = "Violation " + violation.getViolationCode() + " requires at least "
                                + violation.getRequiredComments() + " comment(s)";
                    }

                    if (violation.getRequiredPhotos() > activeTicket.getTicketPictures().size()) {
                        isPhotoRequired = true;
                        requiredPhotoMsg = "Violation " + violation.getViolationCode() + " requires at least "
                                + violation.getRequiredPhotos() + " photo(s)";
                    }
                }

                if (isCommentRequired) {
                    displayErrorMessage(requiredCommentMsg);
                    enablePrint();
                    return;
                }

                if (isPhotoRequired) {
                    displayErrorMessage(requiredPhotoMsg);
                    enablePrint();
                    return;
                }
                try {
                    if (!expDate.isEmpty()) {
                        String[] dates = expDate.split("/");
                        String month;
                        if (TPUtility.isNumeric(dates[0])) {
                            month = dates[0];
                        } else {
                            month = TPUtility.getMonthfromText(dates[0]);
                        }
                        expDate = month + "/" + dates[1];
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                TicketTemp tt = new TicketTemp();
                activeTicket.setBodyId(bodyId);
                activeTicket.setColorId(colorId);
                activeTicket.setMakeId(makeId);
                activeTicket.setStateId(stateId);
                activeTicket.setVin(vin);
                activeTicket.setPlate(plate);
                activeTicket.setExpiration(expDate);
                //activeTicket.setLocation(location);
                activeTicket.setIsGPSLocation(isGPSLocation ? "Y" : "N");
                activeTicket.setIsDriveAway(driveAwayChk.isChecked() ? "Y" : "N");
                activeTicket.setIsChalked(activeTicket.isChalked() ? "Y" : "N");
                activeTicket.setIsVoid("N");
                activeTicket.setStateCode(state);
                activeTicket.setPermit(permit);
                activeTicket.setMeter(meter);
                activeTicket.setTicketDate(new Date());
                activeTicket.setTicketDateNew(DateUtil.getCurrentDateTime1());
                activeTicket.setColorCode(colorCode);
                activeTicket.setBodyCode(bodyCode);
                activeTicket.setMakeCode(makeCode);
                activeTicket.setSpace(space);
                activeTicket.setColorTitle(color);
                activeTicket.setBodyTitle(body);
                activeTicket.setMakeTitle(make);

                //Temp Store
                if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                    tt.setBodyId(bodyId);
                    tt.setColorId(colorId);
                    tt.setMakeId(makeId);
                    tt.setStateId(stateId);
                    tt.setVin(vin);
                    tt.setPlate(plate);
                    tt.setExpiration(expDate);
                    tt.setLocation(location);
                    tt.setIsGPSLocation(isGPSLocation ? "Y" : "N");
                    tt.setIsDriveAway(driveAwayChk.isChecked() ? "Y" : "N");
                    tt.setIsChalked(activeTicket.isChalked() ? "Y" : "N");
                    tt.setIsVoid("N");
                    tt.setStateCode(state);
                    tt.setPermit(permit);
                    tt.setMeter(meter);
                    tt.setTicketDate(new Date());
                    tt.setColorCode(colorCode);
                    tt.setBodyCode(bodyCode);
                    tt.setMakeCode(makeCode);
                    tt.setSpace(space);
                    //tt.setColorTitle(color);
                    //tt.setBodyTitle(body);
                    //tt.setMakeTitle(make);
                    //TicketTemp.insertTicket(tt);
                }
                if (!TextUtils.isEmpty(location) && !location.equalsIgnoreCase("null")) {
                    try {
                        saveTicket();
                        log.debug("-----------------1. saveTicket: " + DateUtil.getCurrentDateTime1() + "-------------");
                    } catch (Exception e) {
                        closeProgressDialog();
                        e.printStackTrace();
                    }
                } else {
                    new iOSDialogBuilder(WriteTicketActivity.this)
                            .setSubtitle("Location field is empty")
                            .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())

                            .build().show();
                }
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void warningAction(View view) {
        if (activeTicket.getTicketViolations().size() == 0) {
            Toast.makeText(this, "Please select violations.", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
        builder.setTitle("Select Violations for Warning");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialog, id) -> {
            dialog.dismiss();
            updateWarnings();
        });

        String[] violationChoices = new String[activeTicket.getTicketViolations().size()];
        boolean[] warnings = new boolean[activeTicket.getTicketViolations().size()];
        for (int i = 0; i < activeTicket.getTicketViolations().size(); i++) {
            TicketViolation violation = activeTicket.getTicketViolations().get(i);
            violationChoices[i] = violation.getViolationDesc();
            warnings[i] = violation.isWarning();
        }

        builder.setMultiChoiceItems(violationChoices, warnings, (dialog, whichButton, isChecked) -> {
            TicketViolation violation = activeTicket.getTicketViolations().get(whichButton);
            violation.setWarning(isChecked);
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button dialogButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (dialogButton != null) {
            dialogButton.setBackgroundResource(R.drawable.btn_yellow);
            dialogButton.setTextAppearance(this, R.style.ButtonText);
        }
    }

    private void updateWarnings() {
        int warnings = 0;

        for (TicketViolation violation : activeTicket.getTicketViolations()) {
            if (violation.isWarning()) {
                warnings++;
            }
        }

        warningBtn.setText("W (" + warnings + ")");

        if (warnings > 0) {
            warningBtn.setBackgroundResource(R.drawable.btn_green);
        } else {
            warningBtn.setBackgroundResource(R.drawable.btn_yellow);
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void processSaveTicket() {
        log.debug("processSaveTicket: " + DateUtil.getCurrentDate());
        int[] array = new int[0];

       /* log.info("Loader running in process save Ticket");
        log.info("Citation Number " + activeTicket.getCitationNumber());
        for (TicketViolation ticketViolation : activeTicket.getTicketViolations()) {
            log.info("Violations " + ticketViolation.getViolationDesc());
        }*/
        try {

            plateLookup = true;
            backBtn.setBackgroundResource(R.drawable.btn_yellow);
            backBtn.setEnabled(true);
        } catch (Exception e) {
            log.info("Loader stopped exception 3" + e.getMessage());
            closeProgressDialog();
            e.printStackTrace();
        }
        try {

            if (!Ticket.checkDuplicateRecords(activeTicket.getPlate(), activeTicket.getVin(), activeTicket.getLocation(), activeTicket.getTicketDate())) {
                try {
                    if (isEmptyData()) {
                        closeProgressDialog();
                        return;
                    }
                    try {
                        chalkTimerFlag = 0;
                        for (TicketViolation violation : activeTicket.getTicketViolations()) {
                            if (violation.isChalktimerequired()) {
                                tmEditText.setFocusable(false);
                                tmEditText.setFocusableInTouchMode(false);
                                if (tmEditText.getText().toString().trim().isEmpty()) {
                                    if (chalkTimerFlag == 0) {
                                        closeProgressDialog();
                                        displayErrorMessage("Chalk time is required.");
                                        chalkTimerFlag++;
                                        enablePrint();
                                    }
                                    log.info("Loader stopped 5");
                                    closeProgressDialog();
                                    return;
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.info("Loader stopped exception 6" + e.getMessage());
                        closeProgressDialog();
                        e.printStackTrace();
                    }

                    if (TPApp.getDeviceInfo() != null) {
                        log.info("Loader running deviceInfo nullcheck");
                        DeviceInfo deviceInfo = TPApp.getDeviceInfo();
                        if (activeTicket.getCitationNumber() > deviceInfo.getEndCitationNumber()) {
                            closeProgressDialog();
                            displayErrorMessage("You have exceeded max tickets allowed on this device. Please contact your administrator.");
                            return;
                        } else if (Ticket.isDuplicateCitation(activeTicket.getCitationNumber())) {
                            showApplicationError("Citation already exists.");
                            return;
                        }
                    }

                    try {
                        if (activeTicket.getIsGPSLocation().equalsIgnoreCase("N")) {
                            if (!isGPSRadioON()) {
                                activeTicket.setLatitude("");
                                activeTicket.setLongitude("");
                                activeTicket.setIsGPSLocation("N");
                            }
                        }
                    } catch (Exception e) {
                        log.info("Loader stopped exception 9" + e.getMessage());
                        closeProgressDialog();
                        e.printStackTrace();
                    }

                    TPApp.setLastViolations(new ArrayList<>());
                    TPApp.setLastPhotos(new ArrayList<>());
                    // Backup CSV File
                    CSVUtility.writeViolationCSV(activeTicket.getTicketViolations(), activeTicket.getCitationNumber());
                    CSVUtility.writePictureCSV(activeTicket.getTicketPictures());
                    for (TicketPicture chalkPicture : activeTicket.getTicketPictures()) {

                        if (chalkPicture.getImagePath().contains("CHALK") || chalkPicture.getImagePath().contains("CHALK-LPR-")) {
                            File previewImg = new File(chalkPicture.getImagePath());
                            File to = new File("/storage/emulated/0/TicketPro/PARKING/ChalkImages/", activeTicket.getCitationNumber() + "-" + previewImg.getName());
                            boolean b = previewImg.renameTo(to);
                            if (b) {
                                chalkPicture.setImagePath(to.toString());
                            }
                        }
                    }

                    for (TicketPicture ticketPicture : activeTicket.getTicketPictures()) {
                        if (ticketPicture.isPhotoSp()) {
                            TPApp.getLastPhotos().add(ticketPicture);
                        }
                    }

                    if (activeTicket.isLPR() && !activeTicket.getTicketPictures().isEmpty()) {
                        activeTicket.setPhotoCount(activeTicket.getTicketPictures().size() - 1);
                    } else {
                        activeTicket.setPhotoCount(activeTicket.getTicketPictures().size());
                    }
                    for (TicketViolation violation : activeTicket.getTicketViolations()) {
                        if (!Ticket.checkDuplicateRecordsPlates(activeTicket.getPlate(), violation.getViolationId(), activeTicket.getTicketDate(), activeTicket.getLocation())) {
                            //Checking previous ticket data with current ticket data.
                            log.debug("Current citation ===" + activeTicket.getCitationNumber());
                            final Ticket lastTicket = Ticket.getLastTicketForCheckDuplicateRecord(activeTicket.getUserId());
                            if (lastTicket != null) {
                                if (lastTicket.getTicketDate().equals(activeTicket.getTicketDate())
                                        && lastTicket.getPlate().equals(activeTicket.getPlate())
                                        && String.valueOf(lastTicket.getViolationId()).equals(String.valueOf(violation.getViolationId()))) {
                                    runOnUiThread(() -> {
                                        log.info("Loader stopped exception 10");
                                        showApplicationError("Ticket already exits for citation " + activeTicket.getCitationNumber());
                                    });

                                    break;
                                }
                            }
                            if (activeTicket.getPlate() == null && activeTicket.getVin() == null) {
                                continue;
                            }
                            TPApp.getLastViolations().add((TicketViolation) violation.clone());
                            if (violation.isWarning()) {
                                activeTicket.setIsWarn("Y");
                                activeTicket.setFine(0);
                                violation.setFine(0);
                            } else {
                                activeTicket.setIsWarn("N");
                                Violation violationN = Violation.getViolationById(violation.getViolationId());
                                if (violationN == null) {
                                    showAlertDialog();
                                    return;
                                } else {
                                    if (activeTicket.getFine() < 1) {
                                        double fine = getViolationFine(Violation.getViolationById(violation.getViolationId()));
                                        violation.setFine(fine);
                                        activeTicket.setFine(fine);
                                    } else {
                                        activeTicket.setFine(violation.getFine());
                                    }
                                }
                            }
                            // Add Current Violation Code and Description
                            activeTicket.setViolationCode(violation.getViolationCode());
                            activeTicket.setViolation(violation.getViolationDesc());
                            activeTicket.setViolationId(violation.getViolationId());
                            activeTicket.setDutyReportId(TPApplication.getInstance().getActiveDutyReport().getDuty_report_id());
                            activeTicket.setAppVersion(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                            activeTicket.setTicketDateNew(DateUtil.getCurrentDateTime1());
                            activeTicket.setTicketDate(new Date());
                            CSVUtility.writeTicketCSV(activeTicket);
                            Ticket.insertTicket(activeTicket, audioFile);
                            array = addElement(array, (int) activeTicket.getCitationNumber());

                            Thread.sleep(700);
                            ticketToSyncRepeatOffender = activeTicket;
                            activeTicket.setCitationNumber(activeTicket.getCitationNumber() + 1);
                        } else {
                            log.debug("Found duplicate value with this : " + activeTicket.getPlate());
                        }
                    }
                    TPApp.setMultipleCitation(array);
                    DeviceInfo deviceInfo = TPApp.getDeviceInfo();
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString(TPConstant.PREFS_KEY_LASTTICKETTIME, new SimpleDateFormat("MM/dd/yyyy HH:mm").format(activeTicket.getTicketDate()));
                    editor.apply();

                    deviceInfo.setCurrentCitationNumber(activeTicket.getCitationNumber());
                    DeviceInfo.insertDeviceInfo(deviceInfo);

                    // Default State
                    State defaultState = State.getDefaultState(WriteTicketActivity.this);
                    if (defaultState != null) {
                        stateEditText.setText(defaultState.getCode());
                        stateId = defaultState.getId();
                    }

                    TPApp.lastPlateNumber = plateNumberEditText.getText().toString();

                    //Use for last vin
                    TPApp.lastVinNumber = vinNumberEditText.getText().toString();

                    // Delete chalk entry for chalk type METER only
                    try {
                        if (activeTicket.isChalked()) {
                            if (ChalkVehicle.getChalkVehicleById(activeTicket.getChalkId()) != null) {
                                ChalkVehicle chalk = ChalkVehicle.getChalkVehicleById(activeTicket.getChalkId());
                                if (chalk != null && !chalk.getChalkType().equals(TPConstant.CHALK_TYPE_LOCATION)) {
                                    if (chalk.getSyncStatus().equals("S")) {
                                        ChalkVehicle.removeChalkById(activeTicket.getChalkId(), "");
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.info("loader stopped exception 11 " + e.getMessage());
                        closeProgressDialog();
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    CSVUtility.writeSystemBackupCSV(activeTicket);
                    if (!StringUtil.isEmpty(activeTicket.getLprNotificationId())) {
                        LPRNotify lprNotify = LPRNotify.getLPRNotificationById(Integer.parseInt(activeTicket.getLprNotificationId()));
                        if (lprNotify != null) {
                            lprNotify.setTicketIssued(true);
                            Completable completable1 = LPRNotify.insertLPRNotify(lprNotify);
                            completable1.subscribe();
                        }
                    }
                    closeProgressDialog();
                    log.info("Background process start");
                    //this  code is changed by mohit 12/01/2023. this  code is used for online/offline process
                    if ((TPApplication.getInstance().getNetOnOff().equals("") && status) || (TPApplication.getInstance().getNetOnOff().equals("N") && status)) {
                        isServiceAvailable = false;
                    } else {
                        isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                    }
                    //End
                    if (isServiceAvailable) {
                        try {

                            ArrayList<ChalkVehicle> pendingChalkedVehicle = ChalkVehicle.getPendingChalkedVehicle();
                            if (pendingChalkedVehicle != null && pendingChalkedVehicle.size() > 0) {
                                Intent serviceIntent = new Intent(WriteTicketActivity.this, JobIntentServiceSaveChalk.class);
                                JobIntentServiceSaveChalk.enqueueWork(WriteTicketActivity.this, serviceIntent);

                            } else {
                                Log.d("WriteTicketActivity", "No pending chalk available.");
                            }

                            Intent serviceIntent = new Intent(WriteTicketActivity.this, JobIntentServiceSaveTicket.class);
                            JobIntentServiceSaveTicket.enqueueWork(WriteTicketActivity.this, serviceIntent);

                            // this  code is added by mohit 06/01/2023. this  code is used for update the repeatoffender on local and database

                            if (Violation.getViolationById(ticketToSyncRepeatOffender.getViolationId()).isRepeatOffender()) {
                                TicketViolation ticketViolation = ticketToSyncRepeatOffender.getTicketViolations().get(0);
                                RepeatOffenderParams repeatOffenderobject = new RepeatOffenderParams();
                                repeatOffenderobject.setStateCode(ticketToSyncRepeatOffender.getStateCode());
                                repeatOffenderobject.setPlate(ticketToSyncRepeatOffender.getPlate());
                                if (ticketViolation.isRepeatOffenderVertical()) {
                                    repeatOffenderobject.setViolationId(ticketViolation.getVerticalViolationId());

                                } else {

                                    repeatOffenderobject.setViolationId(ticketToSyncRepeatOffender.getViolationId());
                                }
                                repeatOffenderobject.setCustId(TPApp.custId);
                                repeatOffenderobject.setCreatTime(DateUtil.getCurrentDateTime1());
                                updateRepeatOffendersCount(repeatOffenderobject);

                                  /*  boolean b = RepeatOffender.checkIsDataAlreadyInDBorNot(
                                            TPApp.custId,
                                            ticketToSyncRepeatOffender.getStateCode(),
                                            ticketToSyncRepeatOffender.getPlate(),
                                            ticketToSyncRepeatOffender.getViolationId()
                                    );
                                    if (b) {
                                        RepeatOffender.updateRepeatOffender(
                                                TPApp.custId,
                                                ticketToSyncRepeatOffender.getStateCode(),
                                                ticketToSyncRepeatOffender.getPlate(),
                                                ticketToSyncRepeatOffender.getViolationId(),
                                                ""
                                        );
                                    } else {
                                        RepeatOffender repeatOffender = new RepeatOffender();
                                        repeatOffender.setCustId(TPApp.custId);
                                        repeatOffender.setCount(1);
                                        repeatOffender.setStateCode(ticketToSyncRepeatOffender.getStateCode());
                                        repeatOffender.setPlate(ticketToSyncRepeatOffender.getPlate());
                                        repeatOffender.setViolationId(ticketToSyncRepeatOffender.getViolationId());
                                        repeatOffender.setViolation(ticketToSyncRepeatOffender.getViolation());
                                        repeatOffender.setCreatTime(DateUtil.getCurrentDateTime1());
                                        repeatOffender.setSyncStatus("S");
                                        Completable completable1 = RepeatOffender.insertRepeatOffender(repeatOffender);
                                        completable1.subscribe();
                                    }*/
                            }
                            //End

                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("loader stopped exception 12 " + e.getMessage());
                            closeProgressDialog();
                        }

                        new Thread(() -> {
                            try {
                                //WriteTicketNetworkCalls.saveTicketTask();
                                if (activeTicket.isChalked() && isServiceAvailable) {
                                    WriteTicketNetworkCalls.updateChalkStatus(Long.parseLong(activeTicket.getChalkId() + ""), "N", "");
                                }
                            } catch (Exception e) {
                                log.info("loader stopped exception 12 " + e.getMessage());
                                closeProgressDialog();
                                e.printStackTrace();
                            }

                        }).start();

                    } else {
                        if (Violation.getViolationById(ticketToSyncRepeatOffender.getViolationId()).isRepeatOffender()) {
                            // This code is added by mohit 12/01/2023.This is used to restrict the repeat offender in offline mode
                            if (!isServiceAvailable) {
                                new Handler().postDelayed(() -> {
                                    try {
                                        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
                                        confirmBuilder.setTitle("Alert")
                                                .setMessage("This device is currently off-line.Repeat Offender validation has been disabled.").setCancelable(false)
                                                .setPositiveButton("Ok", (dialog, which) -> {
                                                    enablePrint();
                                                    printTicket(true);
                                                    dialog.dismiss();
                                                });
                                        AlertDialog confirmAlert = confirmBuilder.create();
                                        confirmAlert.show();

                                    } catch (Exception ignored) {
                                    }
                                }, 500);

                                return;
                            }

                            boolean b = RepeatOffender.checkIsDataAlreadyInDBorNot(
                                    TPApp.custId,
                                    ticketToSyncRepeatOffender.getStateCode(),
                                    ticketToSyncRepeatOffender.getPlate(),
                                    ticketToSyncRepeatOffender.getViolationId()
                            );

                            if (b) {
                                RepeatOffender.updateRepeatOffender(
                                        TPApp.custId,

                                        ticketToSyncRepeatOffender.getStateCode(),
                                        ticketToSyncRepeatOffender.getPlate(),
                                        ticketToSyncRepeatOffender.getViolationId(),
                                        "P"

                                );
                            } else {
                                RepeatOffender repeatOffender = new RepeatOffender();
                                repeatOffender.setCustId(TPApp.custId);
                                repeatOffender.setCount(1);
                                repeatOffender.setStateCode(ticketToSyncRepeatOffender.getStateCode());
                                repeatOffender.setPlate(ticketToSyncRepeatOffender.getPlate());
                                repeatOffender.setViolationId(ticketToSyncRepeatOffender.getViolationId());
                                repeatOffender.setViolation(ticketToSyncRepeatOffender.getViolation());
                                repeatOffender.setCreatTime(DateUtil.getCurrentDateTime1());
                                repeatOffender.setSyncStatus("P");
                                Completable completable1 = RepeatOffender.insertRepeatOffender(repeatOffender);
                                completable1.subscribe();
                            }
                        }
                        //End

                        ticketToSyncRepeatOffender = null;
                    }
                    if (isServiceAvailable) {
                        try {
                            TPUtility.updateFB(WriteTicketActivity.this, "ticketIssued", activeTicket, log);
                        } catch (Exception e) {
                            log.info("loader stopped exception 13 " + e.getMessage());
                            closeProgressDialog();
                            e.printStackTrace();
                            log.error(TPUtility.getPrintStackTrace(e));
                        }

                    }
                    // Print Ticket and Create New Ticket
                    if (driveAwayChk.isChecked()) {

                        createNewTicket();


                    } else {

                        printTicket(true);

                    }
                    enablePrint();
                    try {
                        checkAndRequestForMultiSpaceIPS(ipsMultiSpaceLotDes);
                    } catch (Exception e) {
                        closeProgressDialog();
                        log.info("loader stopped exception 14 " + e.getMessage());
                        e.printStackTrace();
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                } catch (Exception e) {
                    enablePrint();
                    log.info("loader stopped exception 15 " + e.getMessage());
                    closeProgressDialog();
                    log.error(TPUtility.getPrintStackTrace(e));
                    errorHandler.sendEmptyMessage(0);
                }
            } else {
                closeProgressDialog();
            }
        } catch (Exception e) {
            log.info("loader stopped exception" + e.getMessage());
            closeProgressDialog();
            e.printStackTrace();
        }
        closeProgressDialog();
    }

    private static int[] addElement(int[] array, int newValue) {
        int[] newArray = Arrays.copyOf(array, array.length + 1);
        newArray[newArray.length - 1] = newValue;
        return newArray;
    }

    void closeProgressDialog() {
        log.info("Loader Stopped");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void saveTicket() throws Exception {
        // Show loader before GPS updates
        // throw new RuntimeException("This is a crash");

        log.debug("saveTicket: " + DateUtil.getCurrentDate());
        progressDialog = ProgressDialog.show(WriteTicketActivity.this, "", "Saving Ticket Info...");
        log.info("Loader start saveTicket");
        try {
            if (!isServiceAvailable) {
                //Force update if value is empty
                if (gpsLocation != null && gpsLocation.hasAccuracy()) {
                    activeTicket.setLatitude(gpsLocation.getLatitude() + "");
                    activeTicket.setLongitude(gpsLocation.getLongitude() + "");
                }
                log.info("Loader running network not connected");
                processSaveTicket();
                log.debug("1. processSaveTicket: " + DateUtil.getCurrentDate());
                closeProgressDialog();
                return;
            }
        } catch (Exception e) {
            log.info("Loader stopped exception 1" + e.getMessage());
            closeProgressDialog();
            e.printStackTrace();
        }

        final Handler delayhandler = new Handler();
        final Runnable saveTask = () -> runOnUiThread(() -> {
            log.info("Loader running in saveTask");
            if (checkSaveTask) {
                checkSaveTask = false;
                //Force update if value is empty
                try {
                    if (StringUtil.isEmpty(activeTicket.getLatitude())) {
                        if (gpsLocation != null && gpsLocation.hasAccuracy()) {
                            activeTicket.setLatitude(gpsLocation.getLatitude() + "");
                            activeTicket.setLongitude(gpsLocation.getLongitude() + "");
                        }
                    }
                } catch (Exception e) {
                    closeProgressDialog();
                    e.printStackTrace();
                }
                try {
                    processSaveTicket();
                    closeProgressDialog();
                } catch (Exception e) {
                    log.info("Loader stopped exception 2 " + e.getMessage());
                    closeProgressDialog();
                    e.printStackTrace();
                }
            } else {
                closeProgressDialog();
            }
        });

        //Update GPS Location if GPS action was not used
        try {
            if (!activeTicket.isGPSLocation()) {
                if (gpsLocation != null && gpsLocation.hasAccuracy()) {
                    activeTicket.setLatitude(gpsLocation.getLatitude() + "");
                    activeTicket.setLongitude(gpsLocation.getLongitude() + "");
                    delayhandler.postDelayed(saveTask, 10);
                } else {
                    LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                    boolean gps_enabled = false;
                    try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (Exception ignored) {
                        ignored.getMessage();
                    }
                    log.info("Loader running GPS enabled " + gps_enabled);
                    isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                    if (isServiceAvailable && gps_enabled) {

                        final String lat = preference.getString("LAT");
                        final String longi = preference.getString("LONGI");
                        if (lat != null && !TextUtils.isEmpty(lat)) {
                            activeTicket.setLatitude(String.valueOf(lat));
                            activeTicket.setLongitude(String.valueOf(longi));
                        } else {
                            activeTicket.setLatitude("");
                            activeTicket.setLongitude("");
                        }
                        delayhandler.postDelayed(saveTask, 50);
                       /* GetLatLongValue getLatLongValue = new GetLatLongValue(WriteTicketActivity.this, loc -> {
                            if (loc != null) {
                                log.info("Location fetched successfully");
                                activeTicket.setLatitude(String.valueOf(loc.lat));
                                activeTicket.setLongitude(String.valueOf(loc.longi));
                            } else {
                                log.info("Location not fetched ");
                                activeTicket.setLatitude("");
                                activeTicket.setLongitude("");
                            }
                            delayhandler.postDelayed(saveTask, 50);
                        });
                        log.info("Loader running isServiceAvailable && gps_enabled");
                        getLatLongValue.track();*/
                    } else {
                        activeTicket.setLatitude("");
                        activeTicket.setLongitude("");
                        delayhandler.postDelayed(saveTask, 50);
                    }
                }
            } else {
                delayhandler.postDelayed(saveTask, 100);
            }
        } catch (Exception e) {
            closeProgressDialog();
            e.printStackTrace();
        }
        TicketTemp.removeAll();
        TicketPictureTemp.removeAll();
        TicketCommentTemp.removeAll();
        TicketViolationTemp.removeAll();
        closeProgressDialog();
    }

    private void processAction(String action) {
        // Check if this activity is finishing
        if (isFinishing()) {
            return;
        }

        if ("createNewTicket".equalsIgnoreCase(action)) {
            new Handler().postDelayed(this::createNewTicket, 500);
            return;
        }

        if ("ClosePrintDialog".equalsIgnoreCase(action)) {
            new Handler().postDelayed(() -> {
                if (activeTicket != null) {
                    if (activeTicket.isChalked() && Feature.isFeatureAllowed(Feature.RETURN_TO_CHALK)) {
                        backToChalkScreen();
                    }
                }
            }, 500);

            return;
        }

        if (action.equals("Reprint Last Ticket")) {
            new Handler().postDelayed(this::reprintLastTicket, 500);

            return;
        }

        if (action.equals("Take Picture")) {

            Handler delayhandler = new Handler();
            final Runnable mUpdateTimeTask = () -> {
                Intent i = new Intent();
                i.setClass(WriteTicketActivity.this, TakePictureActivity.class);
                i.putExtra("CitationNumber", activeTicket.getCitationNumber());
                startActivityForResult(i, REQUEST_TAKE_PICTURE);
            };

            delayhandler.postDelayed(mUpdateTimeTask, 350);
            return;
        }

        if (action.equals("Use Same Plate/Vin")) {
            new Handler().postDelayed(this::useSamePlate, 500);

            return;
        }

        if (action.equals("Use Same Violation")) {
            new Handler().postDelayed(this::useSameViolations, 500);

            return;
        }

        if (action.equals("Void Last Ticket")) {
            voidLastTicket();
            return;
        }

        if (action.equals("Make Last Ticket A Driveaway")) {
            // this code is Changed by mohit 17 jan 2023
            if (!isServiceAvailable) {
                Ticket ticket1 = Ticket.getLastTicket(TPApp.userId);
                if (!checkNetworkAndAction(ticket1)) {
                    new Handler().postDelayed(() -> {
                        checkNetworkAndActionAlert("Driveaway Last Ticket");
                    }, 500);
                } else {
                    new Handler().postDelayed(() -> {
                        try {
                            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
                            confirmBuilder.setTitle("Driveaway Last Ticket")
                                    .setMessage("Are you sure you want to make last ticket a driveaway?").setCancelable(false)
                                    .setNegativeButton("No", (dialog, which) -> {

                                    }).setPositiveButton("Yes", (dialog, which) -> {
                                        dialog.dismiss();

                                        if (Feature.isFeatureAllowed(Feature.PROMPT_BEFORE_VOID)) {
                                            isTicketIssued("Warn");
                                        } else {
                                            driveAwayLastTicket();
                                        }


                                    });

                            AlertDialog confirmAlert = confirmBuilder.create();
                            confirmAlert.show();

                        } catch (Exception ignored) {
                        }
                    }, 500);

                    return;
                }

            } else {
                new Handler().postDelayed(() -> {
                    try {
                        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
                        confirmBuilder.setTitle("Driveaway Last Ticket")
                                .setMessage("Are you sure you want to make last ticket a driveaway?").setCancelable(false)
                                .setNegativeButton("No", (dialog, which) -> {

                                }).setPositiveButton("Yes", (dialog, which) -> {
                                    dialog.dismiss();

                                    if (Feature.isFeatureAllowed(Feature.PROMPT_BEFORE_VOID)) {
                                        isTicketIssued("Warn");
                                    } else {
                                        driveAwayLastTicket();
                                    }


                                });

                        AlertDialog confirmAlert = confirmBuilder.create();
                        confirmAlert.show();

                    } catch (Exception ignored) {
                    }
                }, 500);

                return;
            }
        }

        if (action.equals("Clear Fields")) {
            clearAction(null);
            return;
        }

        if (action.equals("Advance Paper")) {
            new Handler().postDelayed(() -> TPUtility.printAdvancePaper(WriteTicketActivity.this), 500);
            return;
        }

        if (action.equals("Chalk")) {
            try {
                ChalkVehicle chalk = TPApp.createNewChalk();
                TPApp.setActiveChalk(chalk);

                Intent i = new Intent();
                i.setClass(WriteTicketActivity.this, ChalkVehicleActivity.class);
                startActivity(i);

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            return;
        }

        if (action.equals("Make Last Ticket A Warning")) {
            // this code is added by mohit 17 jan 2023
            if (!isServiceAvailable) {
                Ticket ticket1 = Ticket.getLastTicket(TPApp.userId);
                if (!checkNetworkAndAction(ticket1)) {
                    new Handler().postDelayed(() -> {
                        checkNetworkAndActionAlert("Last Warn Ticket");
                    }, 500);
                } else {
                    new Handler().postDelayed(() -> {
                        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
                        confirmBuilder.setTitle("Warn Last Ticket")
                                .setMessage("Are you sure you want to make last ticket a warning?").setCancelable(false)
                                .setNegativeButton("No", (dialog, which) -> {

                                }).setPositiveButton("Yes", (dialog, which) -> {
                                    dialog.dismiss();
                                    if (Feature.isFeatureAllowed(Feature.PROMPT_BEFORE_VOID)) {
                                        promptForWarn();
                                    } else {
                                        warnLastTicket();
                                    }
                                });

                        AlertDialog confirmAlert = confirmBuilder.create();
                        confirmAlert.show();
                    }, 500);

                    return;
                }

            } else {
                new Handler().postDelayed(() -> {
                    AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
                    confirmBuilder.setTitle("Warn Last Ticket")
                            .setMessage("Are you sure you want to make last ticket a warning?").setCancelable(false)
                            .setNegativeButton("No", (dialog, which) -> {

                            }).setPositiveButton("Yes", (dialog, which) -> {
                                dialog.dismiss();
                                if (Feature.isFeatureAllowed(Feature.PROMPT_BEFORE_VOID)) {
                                    promptForWarn();
                                } else {
                                    warnLastTicket();
                                }
                            });

                    AlertDialog confirmAlert = confirmBuilder.create();
                    confirmAlert.show();
                }, 500);

                return;
            }
        }

        if (action.equals("Duty Logs")) {
            Intent i = new Intent();
            i.setClass(WriteTicketActivity.this, DutyLogsActivity.class);
            startActivity(i);
            return;
        }

        if (action.equals("Ticket Logs")) {
            Intent intent = new Intent(WriteTicketActivity.this, TicketLogsActivity.class);
            startActivity(intent);
            return;
        }

        if (action.equals("Messages")) {
            Intent i = new Intent();
            i.setClass(WriteTicketActivity.this, MessagesActivity.class);
            startActivity(i);
            return;
        }

        if (action.equals("Vehicles")) {
            Intent i = new Intent();
            i.setClass(WriteTicketActivity.this, VehiclesActivity.class);
            startActivity(i);
            return;
        }

        if (action.equals("Sticky Comment  \u2713") || action.equals("Sticky Comment")) {
            TPApp.stickyComments = !TPApp.stickyComments;
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(TPConstant.PREFS_KEY_STICKY_COMMENTS, TPApp.stickyComments);
            editor.commit();
            return;
        }

        if (action.equals("Sticky Violation  \u2713") || action.equals("Sticky Violation")) {
            TPApp.stickyViolations = !TPApp.stickyViolations;
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(TPConstant.PREFS_KEY_STICKY_VIOLATIONS, TPApp.stickyViolations);
            editor.commit();
            return;
        }

        if (action.equals("View Map")) {
            Intent i = new Intent();
            i.setClass(WriteTicketActivity.this, PhotoChalkedMapViewActivity.class);
            startActivity(i);
            return;
        }

        if (action.equals("Print Disclaimer")) {
            new Handler().postDelayed(() -> printDisclaimerDialog(), 500);
            return;
        }

        if (action.equals("Print Scofflaw")) {
            new Handler().postDelayed(() -> printScofflawDialog(), 500);
            return;
        }

        if (action.equals("Special Activity")) {
            if (!Feature.isFeatureAllowed(Feature.SPECIAL_ACTIVITY)) {
                Toast.makeText(getApplicationContext(), "This feature is disabled.", Toast.LENGTH_LONG).show();
                return;
            }

            Intent i = new Intent();
            i.setClass(WriteTicketActivity.this, SpecialActivityActivity.class);
            startActivity(i);
            return;
        }

        if (action.equals("Printer Settings")) {
            new Handler().postDelayed(() -> printerSettings(), 500);
            return;
        }

        if (action.equals("Send Tow Notify")) {
            new Handler().postDelayed(() -> checkPlateAction(null), 500);
            return;
        }

        if (action.equals("Turn Off Chalk Alerts")) {
            TPApp.enableChalkAlerts = false;

            SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_CHALKALERTS, false);
            editor.commit();
            return;
        }

        if (action.equals("Turn On Chalk Alerts")) {
            TPApp.enableChalkAlerts = true;

            SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_CHALKALERTS, true);
            editor.commit();
            return;
        }

        if (action.equals("Turn Off MobileNow")) {
            TPApp.enableMobileNow = false;

            SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_MOBILENOW, false);
            editor.commit();
            return;
        }

        if (action.equals("Turn On MobileNow")) {
            TPApp.enableMobileNow = true;

            SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_MOBILENOW, true);
            editor.commit();
            return;
        }

        if (action.equals("Turn Off PassportParking")) {
            TPApp.enablePassportParking = false;

            SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_PASSPORT_PARKING, false);
            editor.commit();
            return;
        }

        if (action.equals("Turn On PassportParking")) {
            TPApp.enablePassportParking = true;

            SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_PASSPORT_PARKING, true);
            editor.commit();
            return;
        }

        if (action.equals("Turn Off Vin PassportP")) {
            TPApp.enableVinPassportParking = false;

            SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_VIN_PASSPORT_PARKING, false);
            editor.commit();
            return;
        }

        if (action.equals("Turn On Vin PassportP")) {
            TPApp.enableVinPassportParking = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_VIN_PASSPORT_PARKING, true);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Turn Off Progressive")) {
            TPApp.enableProgressive = false;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_PROGRESSIVE, false);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Turn On Progressive")) {
            TPApp.enableProgressive = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_PROGRESSIVE, true);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Turn Off ParkMobile")) {
            TPApp.enableParkMobile = false;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_PARK_MOBILE, false);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Turn On ParkMobile")) {
            TPApp.enableParkMobile = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_PARK_MOBILE, true);
                editor.commit();
            }, 500);
            return;
        }

        // turn on paybyphone
        if (action.equals("Turn Off PayByPhone")) {
            TPApp.enablePayByPhone = false;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_PAY_BY_PHONE, false);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Turn On PayByPhone")) {
            TPApp.enablePayByPhone = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_PAY_BY_PHONE, true);
                editor.commit();
            }, 500);
            return;
        }


        // turn on CALE
        if (action.equals("Turn Off Cale")) {
            TPApp.enableCale = false;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_CALE, false);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Turn On Cale")) {
            TPApp.enableCale = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_CALE, true);
                editor.commit();
            }, 500);
            return;
        }

        // turn on CALE2
        if (action.equals("Turn Off Cale2")) {
            TPApp.enableCale2 = false;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_CALE2, false);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Turn On Cale2")) {
            TPApp.enableCale2 = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_CALE2, true);
                editor.commit();
            }, 500);
            return;
        }


        // turn on PARKEON
        if (action.equals("Turn Off Parkeon")) {
            TPApp.enableParkeon = false;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_PARKEON, false);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Turn On Parkeon")) {
            TPApp.enableParkeon = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_PARKEON, true);
                editor.commit();
            }, 500);
            return;
        }

        //SAMTRANS

        if (action.equals("Turn Off Samtrans")) {
            TPApp.enableSamtrans = false;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_SAMTRANS, false);
                editor.commit();
            }, 500);
            return;
        }
        if (action.equals("Turn On Samtrans")) {
            TPApp.enableSamtrans = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_SAMTRANS, true);
                editor.commit();
            }, 500);
            return;
        }
        if (action.equals("Turn On PP2")) {
            TPApp.enablePassportParking2 = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_PASSPORT_PARKING2, true);
                editor.commit();
            }, 500);
            return;


        }
        if (action.equals("Turn Off Curbtrac")) {
            TPApp.enableCubtrac = false;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_CUBTRAC, false);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Turn On Curbtrac")) {
            TPApp.enableCubtrac = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_CUBTRAC, true);
                editor.commit();
            }, 500);
            return;


        }

        if (action.equals("Turn Off OffStreet")) {
            TPApp.enableOffStreet = false;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_OFFSTEER, false);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Turn On OffStreet")) {
            TPApp.enableOffStreet = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_OFFSTEER, true);
                editor.commit();
            }, 500);
            return;


        }

        if (action.equals("Turn Off PP2")) {
            TPApp.enablePassportParking2 = false;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_PASSPORT_PARKING2, false);
                editor.commit();
            }, 500);
            return;
        }

        // turn on DPT
        if (action.equals("Turn Off T2")) {
            TPApp.enableDPT = false;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_DPT, false);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Turn On T2")) {
            TPApp.enableDPT = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_DPT, true);
                editor.commit();
            }, 500);
            return;
        }

        // turn on IPS
        if (action.equals("Turn Off IPS")) {
            TPApp.enableIPS = false;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_IPS, false);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Turn On IPS")) {
            TPApp.enableIPS = true;
            new Handler().postDelayed(() -> {
                SharedPreferences mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_IPS, true);
                editor.commit();
            }, 500);
            return;
        }

        if (action.equals("Send Support E-Mail")) {
            new Handler().postDelayed(() -> sendSupportEmail(), 500);

            return;
        }

        if (action.equals("LPRNotify Logs")) {
            new Handler().postDelayed(() -> {
                Intent i = new Intent();
                i.setClass(WriteTicketActivity.this, LPRNotifyActivity.class);
                startActivity(i);
            }, 500);
            return;
        }

        if (action.equals("Check Zone")) {
            new Handler().postDelayed(() -> checkZones(), 500);
        }

        if (action.equals("User Settings")) {
            Intent i = new Intent();
            i.setClass(WriteTicketActivity.this, UserSettingsActivity.class);
            startActivity(i);
            return;
        }

        if (action.equals("IPS SpaceGroup")) {
            lookupThread.getHandler().selectIPSSpaceGroup("", "spaceGroup");
        }

        if (action.equals("Change Duty/Route")) {
            launchDayActivity();
            return;
        }

        if (action.equals("Maintenance")) {
            Intent intent = new Intent();
            intent.setClass(WriteTicketActivity.this, MaintenanceLogsActivity.class);
            startActivity(intent);
            return;
        }

        if (action.equals("Brightness")) {
            // TPUtility.showBrightnessControl(this);
            adjustScreenBrightness();
            return;
        }

        if (action.equals("Switch To Traffic")) {
            try {
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.ticketpro.traffic");
                if (LaunchIntent == null) {
                    Toast.makeText(this, "TicketPRO Traffic is not installed", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(LaunchIntent);

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            return;
        }
        //Officer can take his selfi using this function
        if (action.equals("Selfie")) {
            takeSelfiAction();
            return;
        }
    }

    // this code is added by mohit 17 jan 2023
    private boolean checkNetworkAndAction(Ticket lastTicket) {
        boolean isVoid = true;
        if (lastTicket.getStatus().equals("P")) {
            isVoid = true;
        } else {
            isVoid = false;
        }
        return isVoid;
    }

    private void checkNetworkAndActionAlert(String type) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
        confirmBuilder.setTitle("Alert")
                .setMessage(type + " must be done while the device is connected. Please try again later.").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        return;
                    }
                });

        AlertDialog confirmAlert = confirmBuilder.create();
        confirmAlert.show();
    }

    //End

    private void promptForWarn() {
        try {
            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
            confirmBuilder.setTitle("Warn Ticket").setMessage("Has the ticket been placed/warned on the vehicle?")
                    .setCancelable(false).setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                        warnLastTicket();
                    }).setPositiveButton("Yes", (dialog, which) -> dialog.dismiss());

            AlertDialog confirmAlert = confirmBuilder.create();
            confirmAlert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void launchDayActivity() {
        String space = spaceEditText.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("fromClass", "WriteTicket");
        intent.putExtra("space", space);
        putExtra(intent);

        intent.setClass(this, DayActivitiesActivity.class);
        startActivity(intent);
    }

    private void printerSettings() {
        Intent i = new Intent();
        i.setClass(this, TicketPrinter.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.main_menu_1: {
                showActionsOptions();
                return true;
            }

            case R.id.main_menu_2: {
                Intent i = new Intent();
                i.setClass(WriteTicketActivity.this, PhotosActivity.class);
                startActivity(i);
                return true;
            }

            case R.id.main_menu_3:
                showSpecialOptions();
                return true;

            case R.id.main_menu_4:
                Toast.makeText(WriteTicketActivity.this, "Voice", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.main_menu_5:
                showAdminOptions();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showAdminOptions() {

        AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
        builder.setTitle("Select Option");

        final CharSequence[] choiceList = {"Duty Logs", // 0
                "Ticket Logs", // 1
                "Messages", // 2
                "Vehicles"}; // 3

        builder.setItems(choiceList, (dialog, which) -> {
            if (progressDialog.isShowing())
                dialog.dismiss();

            if (which == 0) {
                Intent i = new Intent();
                i.setClass(WriteTicketActivity.this, DutyLogsActivity.class);
                startActivity(i);
                return;
            }

            if (which == 1) {
                Intent i = new Intent();
                i.setClass(WriteTicketActivity.this, TicketLogsActivity.class);
                startActivity(i);
                return;
            }

            if (which == 2) {
                Intent i = new Intent();
                i.setClass(WriteTicketActivity.this, MessagesActivity.class);
                startActivity(i);
                return;
            }

            if (which == 3) {
                Intent i = new Intent();
                i.setClass(WriteTicketActivity.this, VehiclesActivity.class);
                startActivity(i);
                return;
            }
        }).setCancelable(false).setNegativeButton("Cancel", (dialog, which) -> {

        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showSpecialOptions() {

        String stickyComments = "Sticky Comment";
        String stickyViolations = "Sticky Violation";

        if (TPApp.stickyComments) {
            stickyComments = "Sticky Comment \u2713";
        }

        if (TPApp.stickyViolations) {
            stickyViolations = "Sticky Violation \u2713";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
        builder.setTitle("Select Option");

        final CharSequence[] choiceList = {"Aid Citizen", // 0
                "Maintenance", // 1
                "View Map", // 2
                "Contact List", // 3
                "LPR", // 4
                "Print Disclaimer", // 5
                stickyComments, // 6
                stickyViolations, // 7
                "Special Activity", // 8
                "Print Scofflaw"}; // 9

        builder.setItems(choiceList, (dialog, which) -> {
            if (progressDialog.isShowing()) {
                dialog.dismiss();
            }

            SharedPreferences.Editor editor = mPreferences.edit();
            Intent intent = new Intent();

            switch (which) {
                case 1:
                    intent.setClass(WriteTicketActivity.this, MaintenanceLogsActivity.class);
                    startActivity(intent);
                    break;

                case 2:
                    intent.setClass(WriteTicketActivity.this, PhotoChalkedMapViewActivity.class);
                    startActivity(intent);
                    break;

                case 5:
                    printDisclaimerDialog();
                    break;

                case 6:
                    TPApp.stickyComments = !TPApp.stickyComments;
                    editor.putBoolean(TPConstant.PREFS_KEY_STICKY_COMMENTS, TPApp.stickyComments);
                    editor.commit();
                    break;

                case 7:
                    TPApp.stickyViolations = !TPApp.stickyViolations;
                    editor.putBoolean(TPConstant.PREFS_KEY_STICKY_VIOLATIONS, TPApp.stickyViolations);
                    editor.commit();
                    break;

                case 8:
                    intent.setClass(WriteTicketActivity.this, SpecialActivityActivity.class);
                    startActivity(intent);
                    break;

                case 9:
                    printScofflawDialog();
                    break;
            }

        }).setCancelable(false).setNegativeButton("Cancel", (dialog, which) -> {

        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void updateActiveTicketInfo(boolean processGPS) {

        String state = stateEditText.getText().toString();
        String plate = plateNumberEditText.getText().toString();
        String meter = meterNumberEditText.getText().toString();
        String vin = vinNumberEditText.getText().toString();
        String permit = permitEditText.getText().toString();
        String expDate = expEditText.getText().toString();

        vin = TPUtility.getValidVIN(vin);
        plate = TPUtility.getValidPlate(plate);

        activeTicket.setBodyId(bodyId);
        activeTicket.setColorId(colorId);
        activeTicket.setMakeId(makeId);
        activeTicket.setStateId(stateId);
        activeTicket.setVin(vin);
        activeTicket.setPlate(plate);
        activeTicket.setExpiration(expDate);
        activeTicket.setIsGPSLocation(isGPSLocation ? "Y" : "N");
        activeTicket.setIsDriveAway(driveAwayChk.isChecked() ? "Y" : "N");
        activeTicket.setIsChalked(activeTicket.isChalked() ? "Y" : "N");
        activeTicket.setIsVoid("N");
        activeTicket.setStateCode(state);
        activeTicket.setPermit(permit);
        activeTicket.setMeter(meter);
        activeTicket.setTicketDate(new Date());
        activeTicket.setTicketDateNew(DateUtil.getCurrentDateTime1());
        activeTicket.setColorCode(colorCode);
        activeTicket.setBodyCode(bodyCode);
        activeTicket.setMakeCode(makeCode);

        // Don't process GPS location on emulators and explicit calls
        try {
            if (processGPS == false) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gpsLocation != null) {
            activeTicket.setLatitude(gpsLocation.getLatitude() + "");
            activeTicket.setLongitude(gpsLocation.getLongitude() + "");
        }
    }

    private void printDisclaimerDialog() {
        try {
            if (!Feature.isFeatureAllowed(Feature.PRINT_DISCLAIMER)) {
                Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_SHORT).show();
                return;
            }

            updateActiveTicketInfo(true);

            PrintTemplate template = PrintTemplate.getPrintTemplateByName("Disclaimer");
            if (template != null) {
                String printTemplate = template.getTemplateData();
                printTickets(activeTicket, printTemplate, true, false);
            } else {
                Toast.makeText(this, "Print template not available.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void printScofflawDialog() {
        try {
            if (!Feature.isFeatureAllowed(Feature.PRINT_SCOFFLAW)) {
                Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_SHORT).show();
                return;
            }

            updateActiveTicketInfo(false);

            PrintTemplate template = PrintTemplate.getPrintTemplateByName("Scofflaw");
            if (template != null) {
                String printTemplate = template.getTemplateData();
                printTickets(activeTicket, printTemplate, true, false);
            } else {
                Toast.makeText(this, "Print template not available.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void showActionsOptions() {
        final CharSequence[] choiceList = {"Reprint Last Ticket", // 0
                "Take Picture", // 1
                "Use Same Plate", // 2
                "Use Same Violation", // 3
                "Void Last Ticket", // 4
                "Make Last Ticket A Driveaway", // 5
                "Clear Fields", // 6
                "Advance Paper", // 7
                "Chalk", // 8
                "Make last Ticket a Warning",
                "Hotlist"};// 9

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
            builder.setTitle("Select Action");
            builder.setItems(choiceList, (dialog, which) -> {
                if (progressDialog.isShowing())
                    dialog.dismiss();

                if (which == 0) {
                    reprintLastTicket();
                    return;
                }

                if (which == 1) {
                    Intent i = new Intent();
                    i.setClass(WriteTicketActivity.this, TakePictureActivity.class);
                    i.putExtra("CitationNumber", activeTicket.getCitationNumber());
                    startActivityForResult(i, REQUEST_TAKE_PICTURE);
                    return;
                }

                if (which == 2) {
                    useSamePlate();
                    return;
                }

                if (which == 3) {
                    useSameViolations();
                    return;
                }

                if (which == 4) {
                    voidLastTicket();
                    return;
                }

                if (which == 5) {
                    driveAwayLastTicket();
                    return;
                }

                if (which == 6) {
                    clearAction(null);
                    return;
                }

                if (which == 7) {
                    TicketPrinter.print(WriteTicketActivity.this, "{LF}");
                    return;
                }

                if (which == 8) {
                    try {
                        ChalkVehicle chalk = TPApp.createNewChalk();
                        TPApp.setActiveChalk(chalk);

                        Intent i = new Intent();
                        i.setClass(WriteTicketActivity.this, ChalkVehicleActivity.class);
                        startActivity(i);
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }

                if (which == 9) {
                    if (Feature.isFeatureAllowed(Feature.PROMPT_BEFORE_VOID)) {
                        promptForWarn();
                    } else {
                        warnLastTicket();
                    }
                    //warnLastTicket();
                    return;
                }

                /* if (which == 10) {
                 *//* if (Feature.isFeatureAllowed(Feature.PROMPT_BEFORE_VOID)) {
                        promptForWarn();
                    } else {
                        warnLastTicket();
                    }*//*

                    return;
                }*/

            }).setCancelable(false).setNegativeButton("Cancel", (dialog, which) -> {

            });

            AlertDialog alert = builder.create();
            alert.show();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void reprintLastTicket() {
        final Ticket ticket = Ticket.getLastTicket(TPApp.userId);

        if (ticket == null) {
            new Handler().postDelayed(() -> {

                new iOSDialogBuilder(WriteTicketActivity.this)
                        .setTitle("Reprint Last Ticket")
                        .setSubtitle("Last ticket not available.")
                        .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())

                        .build().show();

              /*  AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
                builder.setTitle("Reprint Last Ticket");
                builder.setCancelable(false);
                builder.setMessage("Last ticket not available.");
                AlertDialog alert = builder.create();
                alert.show();*/
            }, 500);

            return;
        }

        // Check if ticket is warned or voided previously.
        if (ticket.isWarn() || ticket.isVoided()) {
            try {
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
                confirmBuilder.setTitle("Reprint Last Ticket")
                        .setMessage("Last ticket was voided/warned. Do you want to reprint it?").setCancelable(false)
                        .setNegativeButton("No", (dialog, which) -> {
                        }).setPositiveButton("Yes", (dialog, which) -> {
                            dialog.dismiss();

                            PrintTemplate template = TPUtility.getTicketPrintTemplate(getSharedPreferences(getPackageName(), MODE_PRIVATE));
                            if (template != null) {
                                printTickets(ticket, template.getTemplateData(), false, true);
                            } else {
                                Toast.makeText(WriteTicketActivity.this, "Print template not found", Toast.LENGTH_LONG).show();
                            }
                        });

                AlertDialog confirmAlert = confirmBuilder.create();
                confirmAlert.show();

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        } else {
            PrintTemplate template = TPUtility.getTicketPrintTemplate(getSharedPreferences(getPackageName(), MODE_PRIVATE));
            if (template != null) {
                printTickets(ticket, template.getTemplateData(), false, true);
            } else {
                Toast.makeText(this, "Print template not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void driveAwayLastTicket() {
        Ticket ticket = Ticket.getLastTicket(TPApp.userId);
        if (ticket == null) {

            new iOSDialogBuilder(WriteTicketActivity.this)
                    .setTitle("Make Last Ticket A Driveaway")
                    .setSubtitle("Last ticket not available.")
                    .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())

                    .build().show();

           /* AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
            builder.setTitle("Make Last Ticket A Driveaway");
            builder.setCancelable(true);
            builder.setMessage("Last ticket not available.");
            AlertDialog alert = builder.create();
            alert.show();*/

        } else {

            if (Feature.isSystemFeatureAllowed(Feature.UPDATE_CUTOFF_PERIOD)) {
                String cutOffTime = Feature.getFeatureValue(Feature.UPDATE_CUTOFF_PERIOD);
                if (cutOffTime != null && !cutOffTime.equals("") && ticket.getTicketDate() != null) {
                    try {
                        int cutOffMins = Integer.parseInt(cutOffTime);
                        long milliseconds = (new Date().getTime() - ticket.getTicketDate().getTime());
                        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                        if (minutes > cutOffMins) {
                            displayErrorMessage("No update allowed. Cutoff time exceeded.");
                            return;
                        }
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            }

            try {
                ticket.setIsDriveAway("Y");
                ticket.setIsWarn("N");
                //ticket.setStatus("P");
                Ticket.updateTicket(ticket);

                /*DatabaseHelper.getInstance().openWritableDatabase();
                DatabaseHelper.getInstance().insertOrReplace(ticket.getContentValues(), "tickets");*/

                boolean syncFlag = false;
                isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                if (isServiceAvailable) {
                    try {
                        WriteTicketBLProcessor blProcessor = (WriteTicketBLProcessor) screenBLProcessor;
                        ArrayList<Ticket> tickets = new ArrayList<>();
                        tickets.add(ticket);
                        syncFlag = blProcessor.getProxy().updateTickets(tickets);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // UPDATE SYNC DATA
                if (!syncFlag) {
                    SyncData syncData = new SyncData();
                    syncData.setActivity("UPDATE");
                    syncData.setPrimaryKey(ticket.getTicketId() + "");
                    syncData.setActivityDate(new Date());
                    syncData.setCustId(TPApp.custId);
                    syncData.setTableName(TPConstant.TABLE_TICKETS);
                    syncData.setStatus("Pending");
                    Completable completable6 = SyncData.insertSyncData(syncData);
                    completable6.subscribeWith(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
                    //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                }

                //DatabaseHelper.getInstance().closeWritableDb();
                Toast.makeText(WriteTicketActivity.this, "Updated ticket successfully.", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }
    }

    private void warnLastTicket() {
        Ticket ticket = Ticket.getLastTicket(TPApp.userId);
        if (ticket == null) {

            new iOSDialogBuilder(WriteTicketActivity.this)
                    .setTitle("Make last Ticket a Warning")
                    .setSubtitle("Last ticket not available.")
                    .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())

                    .build().show();


           /* AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
            builder.setTitle("Make last Ticket a Warning");
            builder.setCancelable(true);
            builder.setMessage("Last ticket not available.");
            AlertDialog alert = builder.create();
            alert.show();*/
        } else {

            if (Feature.isSystemFeatureAllowed(Feature.UPDATE_CUTOFF_PERIOD)) {
                String cutOffTime = Feature.getFeatureValue(Feature.UPDATE_CUTOFF_PERIOD);
                if (cutOffTime != null && !cutOffTime.equals("") && ticket.getTicketDate() != null) {
                    try {
                        int cutOffMins = Integer.parseInt(cutOffTime);
                        long milliseconds = (new Date().getTime() - ticket.getTicketDate().getTime());
                        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                        if (minutes > cutOffMins) {
                            displayErrorMessage("No update allowed. Cutoff time exceeded.");
                            return;
                        }
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            }

            if (ticket.isVoided()) {
                displayErrorMessage("No update allowed. Ticket is already voided.");
                return;
            }

            try {
                ticket.setIsWarn("Y");
                ticket.setIsDriveAway("N");
                ticket.setFine(0);
                Ticket.updateTicket(ticket);

                /*DatabaseHelper.getInstance().openWritableDatabase();
                DatabaseHelper.getInstance().insertOrReplace(ticket.getContentValues(), "tickets");*/

                boolean syncFlag = false;
                isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                if (isServiceAvailable) {
                    try {
                        WriteTicketBLProcessor blProcessor = (WriteTicketBLProcessor) screenBLProcessor;
                        ArrayList<Ticket> tickets = new ArrayList<>();
                        tickets.add(ticket);
                        syncFlag = blProcessor.getProxy().updateTickets(tickets);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // UPDATE SYNC DATA
                if (!syncFlag) {
                    SyncData syncData = new SyncData();
                    syncData.setActivity("UPDATE");
                    syncData.setPrimaryKey(ticket.getTicketId() + "");
                    syncData.setActivityDate(new Date());
                    syncData.setCustId(TPApp.custId);
                    syncData.setTableName(TPConstant.TABLE_TICKETS);
                    syncData.setStatus("Pending");
                    Completable completable7 = SyncData.insertSyncData(syncData);
                    completable7.subscribeWith(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
                    //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
                }

                //DatabaseHelper.getInstance().closeWritableDb();
                Toast.makeText(WriteTicketActivity.this, "Updated ticket successfully.", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
        }
    }

    private void voidLastTicket() {
        // this code is added by mohit 17 jan 2023
        if (!isServiceAvailable) {
            Ticket ticket1 = Ticket.getLastTicket(TPApp.userId);
            if (!checkNetworkAndAction(ticket1)) {
                new Handler().postDelayed(() -> {
                    checkNetworkAndActionAlert("Void Last Ticket");
                }, 500);
            } else {
                new Handler().postDelayed(() -> {
                    if (!Feature.isFeatureAllowed(Feature.VOID_LAST_TICKET) || !Feature.isFeatureAllowed(Feature.VOID_TICKET)) {
                        Toast.makeText(WriteTicketActivity.this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Ticket ticket = Ticket.getLastTicket(TPApp.userId);
                    if (ticket == null) {
                        new iOSDialogBuilder(WriteTicketActivity.this)
                                .setTitle("Void Last Ticket")
                                .setSubtitle("Last ticket not available.")
                                .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())

                                .build().show();

               /* AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
                builder.setTitle("Void Last Ticket");
                builder.setCancelable(true);
                builder.setMessage("Last ticket not available.");
                AlertDialog alert = builder.create();
                alert.show();*/

                    } else {
                        if (Feature.isSystemFeatureAllowed(Feature.UPDATE_CUTOFF_PERIOD)) {
                            String cutOffTime = Feature.getFeatureValue(Feature.UPDATE_CUTOFF_PERIOD);
                            if (cutOffTime != null && !cutOffTime.equals("") && ticket.getTicketDate() != null) {
                                try {
                                    int cutOffMins = Integer.parseInt(cutOffTime);
                                    long milliseconds = (new Date().getTime() - ticket.getTicketDate().getTime());
                                    int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                                    if (minutes > cutOffMins) {
                                        displayErrorMessage("No update allowed. Cutoff time exceeded.");
                                        return;
                                    }
                                } catch (Exception e) {
                                    log.error(TPUtility.getPrintStackTrace(e));
                                }
                            }
                        }

                        try {
                            if (ticket.isVoided()) {
                                displayErrorMessage("This ticket has already been voided.");
                                return;
                            }

                            if (ticket.isWarn()) {
                                displayErrorMessage(
                                        "This ticket was issued as a warning. Cannot be voided.");
                                return;
                            }


                            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
                            confirmBuilder.setTitle("Void Last Ticket").setMessage("Are you sure you want to void last ticket?")
                                    .setCancelable(false).setNegativeButton("No", (dialog, which) -> {
                                    }).setPositiveButton("Yes", (dialog, which) -> {
                                        dialog.dismiss();
                                        if (Feature.isFeatureAllowed(Feature.PROMPT_BEFORE_VOID)) {
                                            isTicketIssued("Void");
                                        } else {
                                            processVoidLastTicket();
                                        }
                                    });

                            AlertDialog confirmAlert = confirmBuilder.create();
                            confirmAlert.show();

                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    }
                }, 500);
            }

        } else {
            new Handler().postDelayed(() -> {
                if (!Feature.isFeatureAllowed(Feature.VOID_LAST_TICKET) || !Feature.isFeatureAllowed(Feature.VOID_TICKET)) {
                    Toast.makeText(WriteTicketActivity.this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                    return;
                }

                Ticket ticket = Ticket.getLastTicket(TPApp.userId);
                if (ticket == null) {
                    new iOSDialogBuilder(WriteTicketActivity.this)
                            .setTitle("Void Last Ticket")
                            .setSubtitle("Last ticket not available.")
                            .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())

                            .build().show();

               /* AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
                builder.setTitle("Void Last Ticket");
                builder.setCancelable(true);
                builder.setMessage("Last ticket not available.");
                AlertDialog alert = builder.create();
                alert.show();*/

                } else {
                    if (Feature.isSystemFeatureAllowed(Feature.UPDATE_CUTOFF_PERIOD)) {
                        String cutOffTime = Feature.getFeatureValue(Feature.UPDATE_CUTOFF_PERIOD);
                        if (cutOffTime != null && !cutOffTime.equals("") && ticket.getTicketDate() != null) {
                            try {
                                int cutOffMins = Integer.parseInt(cutOffTime);
                                long milliseconds = (new Date().getTime() - ticket.getTicketDate().getTime());
                                int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                                if (minutes > cutOffMins) {
                                    displayErrorMessage("No update allowed. Cutoff time exceeded.");
                                    return;
                                }
                            } catch (Exception e) {
                                log.error(TPUtility.getPrintStackTrace(e));
                            }
                        }
                    }

                    try {
                        if (ticket.isVoided()) {
                            displayErrorMessage("This ticket has already been voided.");
                            return;
                        }

                        if (ticket.isWarn()) {
                            displayErrorMessage(
                                    "This ticket was issued as a warning. Cannot be voided.");
                            return;
                        }


                        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
                        confirmBuilder.setTitle("Void Last Ticket").setMessage("Are you sure you want to void last ticket?")
                                .setCancelable(false).setNegativeButton("No", (dialog, which) -> {
                                }).setPositiveButton("Yes", (dialog, which) -> {
                                    dialog.dismiss();
                                    if (Feature.isFeatureAllowed(Feature.PROMPT_BEFORE_VOID)) {
                                        isTicketIssued("Void");
                                    } else {
                                        processVoidLastTicket();
                                    }
                                });

                        AlertDialog confirmAlert = confirmBuilder.create();
                        confirmAlert.show();

                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            }, 500);
        }
    }

    private void isTicketIssued(final String type) {
        try {
            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
            confirmBuilder.setTitle("Void Last Ticket").setMessage("Has the ticket been placed on the vehicle?")
                    .setCancelable(false).setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                        if (type.equalsIgnoreCase("Warn")) {
                            driveAwayLastTicket();
                        } else {
                            processVoidLastTicket();
                        }
                    }).setPositiveButton("Yes", (dialog, which) -> dialog.dismiss());

            AlertDialog confirmAlert = confirmBuilder.create();
            confirmAlert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processVoidLastTicket() {
        try {
            CharSequence[] choiceList;
            final ArrayList<VoidTicketReason> reasons = VoidTicketReason.getVoidReasons(TPApp.custId);
            if (reasons != null && reasons.size() > 0) {
                choiceList = new CharSequence[reasons.size()];
                int index = 0;
                for (VoidTicketReason reason : reasons) {
                    choiceList[index++] = reason.getTitle();
                }

                voidListFlag = true;
            } else {
                choiceList = new CharSequence[1];
                choiceList[0] = "NA";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
            builder.setTitle("Select Void Reason");
            builder.setItems(choiceList, (dialog, which) -> {
                try {
                    Ticket ticket = Ticket.getLastTicket(TPApp.userId);
                    if (ticket == null)
                        return;

                    assert reasons != null;
                    if (reasons.get(which).getTitle().equalsIgnoreCase("OTHER")) {
                        if (Feature.isFeatureAllowed(Feature.VOID_TICKET_OTHER_COMMENT)) {
                            try {
                                otherVoidReasonPopup(which, ticket, reasons);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            updateTicket(which, ticket, reasons);
                        }
                    } else {
                        updateTicket(which, ticket, reasons);
                    }

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            }).setCancelable(false).setNegativeButton("Cancel", (dialog, which) -> {

            });

            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void useSamePlate() {
      /*  String plate = TPApp.lastPlateNumber;
        plateNumberEditText.setText(plate);
        lookupThread.getHandler().lookupPlateHistory(plate);*/

        String plate = TPApp.lastPlateNumber;
        String vin = TPApp.lastVinNumber;
        if ((plate != null && !TextUtils.isEmpty(plate)) && (TextUtils.isEmpty(vin))) {
            plateNumberEditText.setText(plate);
            if (isServiceAvailable) {
                lookupThread.getHandler().lookupPlateHistory(plate);
            }
        } else if ((vin != null && !TextUtils.isEmpty(vin)) && (TextUtils.isEmpty(plate))) {
            vinNumberEditText.setText(vin);
            lookupThread.getHandler().checkVinHistory(vin);
        } else if ((plate != null && !TextUtils.isEmpty(plate)) && (vin != null && !TextUtils.isEmpty(vin))) {
            plateNumberEditText.setText(plate);
            vinNumberEditText.setText(vin);
            if (isServiceAvailable) {
                lookupThread.getHandler().lookupPlateHistory(plate);
            }
        }
    }

    private void useSameViolations() {
        if (TPApp.getLastViolations() != null) {
            activeTicket.setTicketViolations(TPApp.getLastViolations());
            violationBtn.setText("V (" + TPApp.getLastViolations().size() + ")");
            if (activeTicket.getTicketViolations().size() > 0) {
                violationDescText.setText(activeTicket.getTicketViolations().get(0).getViolationDisplay());
            } else {
                violationDescText.setText("");
            }
        }
    }

    public void searchStates(View view) {
        Intent i = new Intent();
        i.setClass(WriteTicketActivity.this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.STATES_SEARCH_LIST);
        putExtra(i);
        startActivityForResult(i, REQUEST_LOOKUP);
        return;
    }

    public void searchExp(View view) {
        Intent i = new Intent();
        i.setClass(WriteTicketActivity.this, ExpirationEntryActivity.class);
        putExtra(i);
        startActivityForResult(i, REQUEST_LOOKUP);
        return;
    }

    public void searchMake(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Intent i = new Intent();
        i.setClass(WriteTicketActivity.this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.MAKE_SEARCH_LIST);
        putExtra(i);
        startActivityForResult(i, REQUEST_LOOKUP);
        return;

    }

    public void searchBody(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Intent i = new Intent();

        i.setClass(WriteTicketActivity.this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.BODY_SEARCH_LIST);
        putExtra(i);
        startActivityForResult(i, REQUEST_LOOKUP);
        return;

    }

    public void searchColor(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Intent i = new Intent();
        i.setClass(WriteTicketActivity.this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.COLOR_SEARCH_LIST);
        putExtra(i);
        startActivityForResult(i, REQUEST_LOOKUP);
        return;
    }

    private void removeTempPhotos() {
        if (activeTicket == null || activeTicket.getTicketPictures() == null
                || activeTicket.getTicketPictures().size() == 0) {
            return;
        }

        // Remove files and update photo number
        try {
            DeviceInfo device = TPApp.getDeviceInfo();
            long currentPhotoNumber = device.getCurrentPhotoNumber();

            for (TicketPicture picture : activeTicket.getTicketPictures()) {
                if (picture.getImagePath() == null || !picture.getImagePath().contains("TICKET"))
                    continue;
                if (!picture.isPhotoSp()) {
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
            Log.e("TicketPRO", "Error " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        if (!plateLookup) {
            return;
        }
        backAction(null);

    }

    public void discardTicket() {
        if (isRecording) {
            this.stopRecording();
        }

        if (isPlaying) {
            this.stopPlaying();
        }

        if (audioFile != null) {
            TPUtility.removeFile(TPUtility.getVoiceMemosFolder() + audioFile);
        }

        // Stop VOICE COMMANDS
        stopListening();

        // Remove temp photos on back action`
        removeTempPhotos();

        LocalBroadcastManager.getInstance(WriteTicketActivity.this).unregisterReceiver(mMessageReceiver);

        TPApp.voiceMode = false;
        TPApp.setActiveTicket(null);
    }

    public void backAction(View view) {

        try {
            TicketTemp.removeAll();
            TicketPictureTemp.removeAll();
            TicketCommentTemp.removeAll();
            TicketViolationTemp.removeAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (alprMode) {
            launchPTS(Feature.isFeatureAllowed(Feature.ALPR_ADMINLAUNCH));

            return;
        }

        // Prompt is not required in the case of chalk ticket
        if (activeTicket.getTicketSource() != TicketSource.LPR_NOTIFICATION) {
            if (activeTicket != null && activeTicket.isChalked()) {
                discardTicket();
                setResult(RESULT_OK);
                finish();
                return;
            }
        }

        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
        confirmBuilder.setTitle("WriteTicket").setMessage("Are you sure you want to discard this ticket?")
                .setCancelable(false).setNegativeButton("No", (dialog, which) -> {

                }).setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    discardTicket();
                    //startActivity(new Intent(WriteTicketActivity.this, MainActivity.class));
                    finish();
                });
        AlertDialog confirmAlert = confirmBuilder.create();
        confirmAlert.show();
    }

    public void clearAction(View view) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
        confirmBuilder.setTitle("Alert").setMessage("Are you sure you want to clear all fields?").setCancelable(false)
                .setNegativeButton("No", (dialog, which) -> {
                }).setPositiveButton("Yes", (dialog, which) -> {
                    try {
                        plateLookup = true;
                        backBtn.setBackgroundResource(R.drawable.btn_yellow);
                        backBtn.setEnabled(true);
                        plateNumberEditText.setText("");
                        vinNumberEditText.setText("");
                        meterNumberEditText.setText("");
                        if (Feature.getFeatureValue(Feature.PARK_PLATE_REG).equalsIgnoreCase("ALL")) {
                            regButton.setVisibility(View.VISIBLE);

                        } else {

                            regButton.setVisibility(View.GONE);
                        }
                        if (!Feature.isFeatureAllowed(Feature.STICKY_LOCATION)) {
                            clearLocation();
                        }

                        if (!Feature.isFeatureAllowed(Feature.STICKY_MAKE)) {
                            makeEditText.setText("");
                        }

                        if (!Feature.isFeatureAllowed(Feature.STICKY_COLOR)) {
                            colorEditText.setText("");
                        }

                        if (!Feature.isFeatureAllowed(Feature.STICKY_BODY)) {
                            bodyEditText.setText("");
                        }

                        spaceEditText.setText("");
                        expEditText.setText("");
                        permitEditText.setText("");
                        driveAwayChk.setChecked(false);

                        // Default State
                        State defaultState = State.getDefaultState(WriteTicketActivity.this);
                        if (defaultState != null) {
                            stateEditText.setText(defaultState.getCode());
                            stateId = defaultState.getId();
                        }

                        // Clear Violations and Comments
                        activeTicket.getTicketViolations().clear();
                        violationBtn.setText("V (0)");
                        warningBtn.setText("W (0)");
                        warningBtn.setBackgroundResource(R.drawable.btn_yellow);
                        violationDescText.setText("");
                        activeTicket.setIsLPR("N");

                        // Remove all Pictures
                        for (TicketPicture picture : activeTicket.getTicketPictures()) {
                            File file = new File(picture.getImagePath());
                            if (file.exists()) {
                                file.delete();
                            }
                        }

                        activeTicket.getTicketPictures().clear();
                        photosBtn.setText("(0)");

                        if (!activeTicket.isChalked() && Feature.isFeatureAllowed(Feature.ALLOW_TM_EDIT)) {
                            tmEditText.setText("");
                            pickerDate = null;
                            activeTicket.setTimeMarked(null);
                        }
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                });

        AlertDialog confirmAlert = confirmBuilder.create();
        confirmAlert.show();
    }

    public void toggleFlashAction(View view) {
        this.runOnUiThread(() -> {
            try {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    Camera.Parameters cParameters = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (mCameraManager == null) {
                            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                            try {
                                assert mCameraManager != null;
                                mCameraId = mCameraManager.getCameraIdList()[0];
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (flashCamera == null) {
                            flashCamera = Camera.open();
                        }
                        cParameters = flashCamera.getParameters();
                    }
                    if (!cameraFlash) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            mCameraManager.setTorchMode(mCameraId, true);
                        } else {
                            try {
                                cParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                flashCamera.setParameters(cParameters);
                                flashCamera.startPreview();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        cameraFlash = true;

                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            mCameraManager.setTorchMode(mCameraId, false);
                        } else {
                            try {
                                cParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                                flashCamera.setParameters(cParameters);
                                flashCamera.stopPreview();
                                flashCamera.release();
                                flashCamera = null;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        cameraFlash = false;
                    }
                    //}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void clearLocation() {
        activeTicket.setStreetNumber("");
        activeTicket.setStreetPrefix("");
        activeTicket.setStreetSuffix("");
        activeTicket.setDirection("");
        activeTicket.setLocation("");
        locationEditText.setText("");

        if (GPSProgressBar != null) {
            GPSProgressBar.setVisibility(View.GONE);
        }

        isGPSLocation = false;
    }

    public void gpsAction(View view) {
        /*try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGPSEnabled) {
            isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
            if (isServiceAvailable) {
                if (Feature.isFeatureAllowed(Feature.PARK_GPS_API)) {
                    String featureValue = Feature.getFeatureValue(Feature.PARK_GPS_API);
                    if (featureValue.equalsIgnoreCase("G")) {
                        findLoc();
                    } else if (featureValue.equalsIgnoreCase("A")) {
                        getLocationFromGps();
                    } else if (featureValue.equalsIgnoreCase("E")) {

                    } else {
                        getLocationFromGps();
                    }
                } else {
                    findLoc();
                }
            }
        } else {
            new iOSDialogBuilder(WriteTicketActivity.this)
                    .setSubtitle("GPS is unavailable at the moment. Please try later or select location from list.")
                    .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())

                    .build().show();
        }

    }

    public void LocationEntryAction(View view) {
        Intent i = new Intent();
        i.setClass(WriteTicketActivity.this, LocationEntryActivity.class);
        i.putExtra("Location", activeTicket.getLocation());
        i.putExtra("StreetNumber", activeTicket.getStreetNumber());
        i.putExtra("StreetPrefix", activeTicket.getStreetPrefix());
        i.putExtra("StreetSuffix", activeTicket.getStreetSuffix());
        i.putExtra("Direction", activeTicket.getDirection());
        startActivityForResult(i, REQUEST_LOOKUP);
    }

    public void LPRAction(View view) {
        isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
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
    }


    public void REGAction(View view) {
        if (isServiceAvailable) {
            regButton.setBackgroundResource(R.drawable.btn_blue);

            if (plateNumberEditText.getText().toString().equals("") || plateNumberEditText.getText().toString().isEmpty()) {
                plateNumberEditText.setError("Please enter plate number");
            } else {
                try {
                    plcardsendrequestAlertDialog();
                    CustomerInfo customerInfo = CustomerInfo.getCustomerInfo(TPApplication.getInstance().custId);
                    User user = User.getUserInfo(TPApplication.getInstance().userId);
                    final WriteTicketBLProcessor bl = ((WriteTicketBLProcessor) screenBLProcessor);
                    final String agencyCode = customerInfo.getAgencyCode();
                    final String users = user.getFullName();
                    final String plate = plateNumberEditText.getText().toString();
                    final String placard = "";
                    final String source = "M";
                    new Thread(() -> {
                        try {
                            boolean placard11 = bl.getPlacard(agencyCode, users, plate, placard, source);
                        } catch (Exception e) {
                            e.printStackTrace();
                            TPUtility.hideSoftKeyboard(WriteTicketActivity.this, plateNumberEditText);
                        }
                    }).start();


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        } else {
            Toast.makeText(WriteTicketActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            //  regButton.setBackgroundResource(R.drawable.btn_disabled);

        }
    }

    public void selectLocation(View view) {
        Intent i = new Intent();
        i.setClass(WriteTicketActivity.this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.SELECT_LOCATION_LIST);
        putExtra(i);
        startActivityForResult(i, REQUEST_LOOKUP);
    }

    public void clearField(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setText("");
        }
    }

    private void putExtra(Intent intent) {
        try {
            intent.putExtra("STATE", stateEditText.getText().toString());
            intent.putExtra("EXP", expEditText.getText().toString());
            intent.putExtra("MAKE", makeEditText.getText().toString());
            intent.putExtra("BODY", bodyEditText.getText().toString());
            intent.putExtra("COLOR", colorEditText.getText().toString());
            intent.putExtra("PLATE", plateNumberEditText.getText().toString());
            intent.putExtra("VIN", vinNumberEditText.getText().toString());
            intent.putExtra("TM", tmEditText.getText().toString());
            intent.putExtra("Location", activeTicket.getLocation());
            intent.putExtra("StreetNumber", activeTicket.getStreetNumber());
            intent.putExtra("StreetPrefix", activeTicket.getStreetPrefix());
            intent.putExtra("StreetSuffix", activeTicket.getStreetSuffix());
            intent.putExtra("Direction", activeTicket.getDirection());
            intent.putExtra("PERMIT", permitEditText.getText().toString());
            intent.putExtra("METER", meterNumberEditText.getText().toString());
            intent.putExtra("StateId", stateId);
            intent.putExtra("BodyId", bodyId);
            intent.putExtra("ColorId", colorId);
            intent.putExtra("MakeId", makeId);
            intent.putExtra("BodyCode", bodyCode);
            intent.putExtra("ColorCode", colorCode);
            intent.putExtra("MakeCode", makeCode);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void updateTicket() {
        updateTicket(false);
    }

    private void updateTicket(boolean updateGpsAddress) {
        String state = stateEditText.getText().toString();
        String plate = plateNumberEditText.getText().toString();
        String meter = meterNumberEditText.getText().toString();
        String vin = vinNumberEditText.getText().toString();
        String permit = permitEditText.getText().toString();
        String expDate = expEditText.getText().toString();
        String space = spaceEditText.getText().toString();

        activeTicket.setBodyId(bodyId);
        activeTicket.setColorId(colorId);
        activeTicket.setMakeId(makeId);
        activeTicket.setStateId(stateId);
        activeTicket.setVin(vin);
        activeTicket.setPlate(plate);
        activeTicket.setExpiration(expDate);
        activeTicket.setIsGPSLocation(isGPSLocation ? "Y" : "N");
        activeTicket.setIsDriveAway(driveAwayChk.isChecked() ? "Y" : "N");
        activeTicket.setIsChalked(activeTicket.isChalked() ? "Y" : "N");
        activeTicket.setIsVoid("N");
        activeTicket.setStateCode(state);
        activeTicket.setPermit(permit);
        activeTicket.setMeter(meter);
        activeTicket.setBodyCode(bodyCode);
        activeTicket.setMakeCode(makeCode);
        activeTicket.setColorCode(colorCode);
        activeTicket.setSpace(space);

        if (gpsLocation != null && gpsLocation.hasAccuracy()) {
            activeTicket.setLatitude(gpsLocation.getLatitude() + "");
            activeTicket.setLongitude(gpsLocation.getLongitude() + "");
        }

        if (updateGpsAddress) {
            updateGPSAddress();
        }
    }

    private void updateGPSAddress() {
        Receiver receiver = new Receiver() {
            @Override
            public void onReceiveResult(Location location, Bundle resultData) {
                if (location != null) {
                    activeTicket.setLatitude(location.getLatitude() + "");
                    activeTicket.setLongitude(location.getLongitude() + "");
                }
            }

            @Override
            public void onReceiveResult(GPSLocation location, Bundle resultData) {
                if (location != null && (!location.getLocation().equals("") || !location.getStreetNumber().equals(""))) {
                    activeTicket.setStreetNumber(location.getStreetNumber().toUpperCase());
                    activeTicket.setLocation(location.getLocation().toUpperCase());
                    activeTicket.setLongitude(location.getLongitude());
                    activeTicket.setLatitude(location.getLatitude());
                    activeTicket.setStreetPrefix("");
                    activeTicket.setStreetSuffix("");
                    activeTicket.setDirection("");
                    activeTicket.setGpstime(new Date());
                    isGPSLocation = true;
                }
            }

            @Override
            public void onTimeout() {
            }
        };

    }

    public void onLeftSwipe() {
        if (!Feature.isFeatureAllowed(Feature.PRINT_PREVIEW)) {
            return;
        }

        updateTicket();

        Intent i = new Intent();
        i.setClass(WriteTicketActivity.this, PrintPreviewActivity.class);
        startActivityForResult(i, REQUEST_PREVIEW);
    }

    public void onRightSwipe() {
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40,
                getResources().getDisplayMetrics());
        SlideoutActivity.prepare(WriteTicketActivity.this, R.id.write_ticket_main_layout, width);
        startActivity(new Intent(WriteTicketActivity.this, MenuActivity.class));
        overridePendingTransition(0, 0);
    }

    private void backToChalkScreen() {
        closeProgressDialog();

        stopListening();

        LocalBroadcastManager.getInstance(WriteTicketActivity.this).unregisterReceiver(mMessageReceiver);
        TPApp.voiceMode = false;
        TPApp.setActiveTicket(null);

        // Intent intent = new Intent(TPConstant.LOCAL_BROADCAST_EVENT);
        // intent.putExtra("action",
        // TPConstant.LOCAL_BROADCAST_REFRESH_CHALK_LIST);
        // LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        setResult(RESULT_OK);
        finish();
    }

    private void createNewTicket() {
        if (activeTicket.getTicketSource() != TicketSource.LPR_NOTIFICATION) {
            if (activeTicket.getTicketSource() == TicketSource.CHALK_NOTIFICATION) {
                Intent intent = new Intent(TPConstant.LOCAL_BROADCAST_EVENT);
                intent.putExtra("action", "createNewTicket");

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mMessageReceiver);

                stopListening();

                finish();
                return;
            }

            if (activeTicket.isChalked() && Feature.isFeatureAllowed(Feature.RETURN_TO_CHALK)) {
                // Proceed with return to chalk screen if showPrintDialog is not
                // enabled or violations size is 1
                if (activeTicket.getTicketViolations().size() == 1 || TPApp.showPrintDialog == false) {
                    backToChalkScreen();
                    return;
                }
            }
        }

        Address stickyLocation = null;
        String lat = activeTicket.getLatitude();
        String lon = activeTicket.getLongitude();
        String isGPS = activeTicket.getIsGPSLocation();

        if (!(Feature.isFeatureAllowed(Feature.STICKY_LOCATION) || Feature.isStickyField("location"))) {
            locationEditText.setText("");
        } else {
            stickyLocation = new Address();
            stickyLocation.setLocation(activeTicket.getLocation());
            stickyLocation.setStreetNumber(activeTicket.getStreetNumber());
            stickyLocation.setStreetPrefix(activeTicket.getStreetPrefix());
            stickyLocation.setStreetSuffix(activeTicket.getStreetSuffix());
            stickyLocation.setDirection(activeTicket.getDirection());
        }

        if (!(Feature.isFeatureAllowed(Feature.STICKY_MAKE) || Feature.isStickyField("make"))) {
            makeId = 0;
            makeCode = "";
            makeEditText.setText("");
        }

        if (!(Feature.isFeatureAllowed(Feature.STICKY_COLOR) || Feature.isStickyField("color"))) {
            colorId = 0;
            colorCode = "";
            colorEditText.setText("");
        }

        if (!(Feature.isFeatureAllowed(Feature.STICKY_BODY) || Feature.isStickyField("body"))) {
            bodyId = 0;
            bodyCode = "";
            bodyEditText.setText("");
        }

        if (!(Feature.isFeatureAllowed(Feature.STICKY_SPACE) || Feature.isStickyField("space"))) {
            spaceEditText.setText("");
        }

        vinNumberEditText.setText("");
        permitEditText.setText("");
        plateNumberEditText.setText("");
        meterNumberEditText.setText("");
        expEditText.setText("");
        driveAwayChk.setChecked(false);
        audioFile = null;
        isRecording = false;
        isPlaying = false;
        ignorePlateValidation = false;
        isGPSLocation = false;

        voiceMemoBtn.setImageDrawable(getResources().getDrawable(R.drawable.mic));

        plateNumberEditText.requestFocus();
        plateNumberEditText.setBackgroundResource(R.drawable.text_field_focus_bkg);
        stateEditText.setBackgroundResource(R.drawable.text_field_bkg);
        meterNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
        locationEditText.setBackgroundResource(R.drawable.text_field_bkg);
        vinNumberEditText.setBackgroundResource(R.drawable.text_field_bkg);
        bodyEditText.setBackgroundResource(R.drawable.text_field_bkg);
        colorEditText.setBackgroundResource(R.drawable.text_field_bkg);
        tmEditText.setBackgroundResource(R.drawable.text_field_bkg);
        spaceEditText.setBackgroundResource(R.drawable.text_field_bkg);
        permitEditText.setBackgroundResource(R.drawable.text_field_bkg);
        makeEditText.setBackgroundResource(R.drawable.text_field_bkg);
        expEditText.setBackgroundResource(R.drawable.text_field_bkg);

        try {
            ArrayList<TicketViolation> violations = new ArrayList<TicketViolation>();
            if (TPApp.stickyViolations) {
                violations = TPApp.getLastViolations();
            }
            //ArrayList<TicketPicture> ticketPictures = new ArrayList<>();

            activeTicket = TPApp.createNewTicket();
            if (stickyLocation != null) {
                activeTicket.setLocation(stickyLocation.getLocation().toUpperCase());
                activeTicket.setStreetNumber(stickyLocation.getStreetNumber());
                activeTicket.setStreetPrefix(stickyLocation.getStreetPrefix());
                activeTicket.setStreetSuffix(stickyLocation.getStreetSuffix());
                activeTicket.setDirection(stickyLocation.getDirection());
                activeTicket.setLatitude(lat);
                activeTicket.setLongitude(lon);
                activeTicket.setIsGPSLocation(isGPS);
                isGPSLocation = "Y".equalsIgnoreCase(isGPS);
            }

            // Check and clear location as per user preferences
            try {
                boolean clearLocationField = mPreferences.getBoolean(TPConstant.PREFS_KEY_CLEAR_LOCATION, false);
                if (clearLocationField) {
                    clearLocation();
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            if (!(Feature.isFeatureAllowed(Feature.STICKY_TM) || Feature.isStickyField("tm"))) {
                tmEditText.setText("");
                pickerDate = null;
            } else {
                activeTicket.setTimeMarked(pickerDate);
            }

            TPApp.setActiveTicket(activeTicket);

            TextView citationNumberTextView = findViewById(R.id.citation_number);
            citationNumberTextView.setText("#" + TPUtility.prefixZeros(this.activeTicket.getCitationNumber(), 8));
            if (TPApp.stickyPhoto) {
                Iterator<TicketPicture> iterator = TPApp.getLastPhotos().iterator();
                while (iterator.hasNext()) {
                    TicketPicture next = iterator.next();
                    next.setCitationNumber(activeTicket.getCitationNumber());
                    if (!next.isPhotoSp()) {
                        iterator.remove();
                    }
                }
                activeTicket.setTicketPictures(TPApp.getLastPhotos());
            }
            if (activeTicket.getTicketPictures().size() > 0) {
                photosBtn.setText("(" + activeTicket.getTicketPictures().size() + ")");
            } else {
                photosBtn.setText("(0)");
            }
            /*warningBtn.setText("W (0)");
            warningBtn.setBackgroundResource(R.drawable.btn_yellow);*/

            if (violations.size() == 0) {
                activeTicket.getTicketViolations().clear();
            } else {
                activeTicket.getTicketViolations().add(violations.get(0));
                for (TicketViolation violation : activeTicket.getTicketViolations()) {
                    violation.setWarning(TPApp.activeDutyInfo.getAllowTicket().equals("W"));
                    if (!TPApp.stickyComments) {
                        violation.getTicketComments().clear();
                    }

                }
            }

            if (TPApp.activeDutyInfo.getAllowTicket().equals("W")) {

                updateWarnings();
                if (TPApp.activeDutyInfo.getAllowTicket().equals("W") && activeTicket.getTicketViolations().size() > 0) {
                    if (Feature.isFeatureAllowed(Feature.PARK_WARNING_RESET)) {
                        warningBtn.setClickable(true);
                        warningBtn.setAlpha(1f);
                    } else {
                        warningBtn.setClickable(false);
                        warningBtn.setAlpha(0.5f);
                    }

                } else {
                    warningBtn.setClickable(true);
                    warningBtn.setAlpha(1f);
                }
            } else {

                warningBtn.setText("W (0)");
                warningBtn.setBackgroundResource(R.drawable.btn_yellow);
                warningBtn.setClickable(true);
                warningBtn.setAlpha(1f);
            }

            violationBtn.setText("V (" + activeTicket.getTicketViolations().size() + ")");
            if (activeTicket.getTicketViolations().size() > 0) {
                violationDescText.setText(activeTicket.getTicketViolations().get(0).getViolationDisplay());
            } else {
                violationDescText.setText("");

            }

        } catch (Exception e) {
            enablePrint();
            log.error(TPUtility.getPrintStackTrace(e));
        }

        closeProgressDialog();

        enablePrint();

        if (alprMode) {
            launchPTS(Feature.isFeatureAllowed(Feature.ALPR_ADMINLAUNCH));
        }

        Toast toast = Toast.makeText(WriteTicketActivity.this, "Saved Ticket Successfully.", Toast.LENGTH_SHORT);
        toast.show();
    }

    /* Print Preview and Ticket Printing */
    private void printTicket(boolean isCurrentTicket) {
        log.info("Call printing....");
        PrintTemplate template = TPUtility.getTicketPrintTemplate(getSharedPreferences(getPackageName(), MODE_PRIVATE));
        if (template != null) {
            printTickets(activeTicket, template.getTemplateData(), false, false);
        } else {
            Toast.makeText(this, "Print template not found", Toast.LENGTH_LONG).show();
        }

        new Handler().postDelayed(this::createNewTicket, 500);

    }

    private void printTickets(Ticket printTicket, String printTemplateData, boolean printSpecialTemplate,
                              boolean isPreviousTicket) {
        // Create Empty Violation for Special Templates
        if (printSpecialTemplate && printTicket.getTicketViolations().size() == 0) {
            TicketViolation violation = new TicketViolation();
            violation.setCitationNumber(printTicket.getCitationNumber());
            violation.setFine(0);
            violation.setViolationDesc("");
            violation.setViolationCode("");
            violation.setViolationDisplay("");

            printTicket.getTicketViolations().add(violation);
        }

        if (!isPreviousTicket) {
            printTicket.setMakeTitle(makeEditText.getText().toString());
            printTicket.setBodyTitle(bodyEditText.getText().toString());
            printTicket.setColorTitle(colorEditText.getText().toString());
        }

        PrintTokenParser tokenParser = new PrintTokenParser(printTicket, printTemplateData);
        tokenParser.setSpecialTemplate(printSpecialTemplate);
        tokenParser.setPreviousTicket(isPreviousTicket);
        tokenParser.setMultiPrint(true);

        ArrayList<String> printTickets = tokenParser.parseTickets();

        Toast.makeText(this, "Processing ticket printing...", Toast.LENGTH_SHORT).show();
        TicketPrinter.print(this, printTickets);
        if (!TPApp.stickyViolations) {
            if (Feature.getFeatureValue(Feature.PARK_PLATE_REG).equalsIgnoreCase("ALL")) {
                regButton.setVisibility(View.VISIBLE);

            } else {

                regButton.setVisibility(View.GONE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void handleVoiceInput(String text) throws Exception {

        if (text == null)
            return;

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        if (text.contains("BACK") || text.contains("CLOSE")) {
            backAction(null);
        } else if (text.contains("PREVIEW") || text.contains("PRINT PREVIEW")) {
            printAction(null);
        } else if (text.contains("PRINT") || text.contains("SAVE TICKET")) {
            try {
                saveTicket();
                log.debug("2. saveTicket: " + DateUtil.getCurrentDate());

            } catch (Exception e) {
                closeProgressDialog();
                e.printStackTrace();
            }
        } else if (text.contains("GPS") || text.contains("GPS LOCATION")) {
            gpsAction(null);
        } else if (text.contains("LPR")) {
            LPRAction(null);
        } else if (text.contains("VOID TICKET") || text.contains("VOID LAST TICKET")) {
            voidLastTicket();
        } else if (text.contains("WARN TICKET") || text.contains("WARN LAST TICKET")) {
            warnLastTicket();
        } else if (text.contains("ACTION MENU") || text.contains("MENU") || text.contains("ACTIONS")) {
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40,
                    getResources().getDisplayMetrics());
            SlideoutActivity.prepare(WriteTicketActivity.this, R.id.write_ticket_main_layout, width);
            startActivity(new Intent(WriteTicketActivity.this, MenuActivity.class));
            overridePendingTransition(0, 0);

        } else if (text.contains("CHECK DRIVEAWAY")) {
            driveAwayChk.setSelected(true);

        } else if (text.contains("UNCHECK DRIVEAWAY")) {
            driveAwayChk.setSelected(false);

        } else if (text.contains("PHOTOS")) {
            viewPhotosAction(null);

        } else if (text.contains("TAKE PHOTO") || text.contains("TAKE PICTURE") || text.contains("NEW PICTURE")
                || text.contains("NEW PHOTO")) {
            takePictureAction(null);

        } else if (text.contains("VIOLATIONS") || text.contains("ADD VIOLATION")) {
            viewViolationAction(null);
        } else if (text.contains("CLEAR ALL")) {
            clearAction(null);
        } else if (text.contains("STATE")) {
            searchStates(null);
        } else if (text.contains("COLOR") || text.contains("COLOUR")) {
            searchColor(colorEditText);
        } else if (text.contains("BODY")) {
            searchBody(bodyEditText);
        } else if (text.contains("MAKE")) {
            searchMake(makeEditText);
        } else if (text.contains("EXPIRATION")) {
            searchExp(null);
        } else if (text.contains("METER")) {
            meterNumberEditText.requestFocus();
            focusedEditText = meterNumberEditText;
        } else if (text.contains("PLATE")) {
            plateNumberEditText.requestFocus();
            focusedEditText = plateNumberEditText;
        } else if (text.contains("VIN")) {
            vinNumberEditText.requestFocus();
            focusedEditText = vinNumberEditText;
        } else if (text.contains("PERMIT")) {
            permitEditText.requestFocus();
            focusedEditText = permitEditText;
        } else if (text.contains("LOCATION")) {
            selectLocation(null);
        } else if (text.contains("EDIT LOCATION")) {
            LocationEntryAction(null);
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

    private void processGoCommand(String tagName, String value) {
        if (value == null || value.equals(""))
            return;

        if (tagName.equals("Plate")) {
            String plate = TPUtility.getValidPlate(value);
            plateNumberEditText.setText(plate);
            lookupThread.getHandler().lookupPlateHistory(value);

        } else if (tagName.equals("Vin")) {
            String vin = TPUtility.getValidVIN(value);
            vinNumberEditText.setText(vin);
            lookupThread.getHandler().checkVinHistory(vin);

        } else if (tagName.equals("Permit")) {
            String permit = TPUtility.getValidPermit(value);
            permitEditText.setText(permit);
            if (!TPApp.isServiceAvailable()) {
                displayErrorMessage("Unbale to verify permit data at this time. Possible connection issue.");
                return;
            }
            lookupThread.getHandler().checkPermitHistory(permit);

        } else if (tagName.equals("Meter")) {
            String meter = TPUtility.getValidMeter(value);
            meterNumberEditText.setText(meter);
            lookupThread.getHandler().checkMeterHistory(meter, false);
        }
    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {
        // TODO Auto-generated method stub
    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {

        statusIndicatorImageView = findViewById(R.id.status_indicator_image);
        lprButton = findViewById(R.id.write_ticket_lpr_btn);

        // Notify handler about the network change
        lookupThread.getHandler().handleNetworkStatus(connected, isFastConnection);

        if (connected) {
            UIHelper.toggleButtonState(lprButton, Feature.isFeatureAllowed(Feature.LPR));
            //UIHelper.toggleButtonState(hotListButton, Feature.isFeatureAllowed(Feature.ADD_HOTLIST));
            UIHelper.toggleButtonState(alprBtn, Feature.isFeatureAllowed(Feature.ALPR));
            // This code is added by mohit 12/01/2023. This code is used for changing status in to online mode
            status = false;
            if (UIHelper.isGpsDeviceValue(TPApp.deviceId)) {
                UIHelper.toggleButtonState(gpsButton, false);
            } else {
                UIHelper.toggleButtonState(gpsButton, Feature.isFeatureAllowed(Feature.GPS));
            }

            statusIndicatorImageView.setImageResource(R.drawable.green_status_btn_bk);
            if (TPApplication.getInstance().getNetOnOff().equals("Y")) {
                //startListen();
                statusIndicatorImageView1.setImageResource(R.drawable.red_status_btn);
                gpsButton.setBackgroundResource(R.drawable.btn_disabled);
                gpsButton.setClickable(false);
                alprBtn.setBackgroundResource(R.drawable.btn_disabled);
                alprBtn.setClickable(false);
                btnPlacard.setBackgroundResource(R.drawable.btn_disabled);
                btnPlacard.setClickable(false);
                lprButton.setBackgroundResource(R.drawable.btn_disabled);
                lprButton.setClickable(false);
                hotListButton.setBackgroundResource(R.drawable.btn_hotlist_gry);
                hotListButton.setClickable(false);


            } else if (TPApplication.getInstance().getNetOnOff().equals("N")) {
                statusIndicatorImageView1.setImageResource(R.drawable.green_status_btn_bk);
                gpsButton.setBackgroundResource(R.drawable.btn_yellow);
                gpsButton.setClickable(true);
                alprBtn.setBackgroundResource(R.drawable.btn_yellow);
                alprBtn.setClickable(true);
                btnPlacard.setBackgroundResource(R.drawable.btn_blue);
                btnPlacard.setClickable(true);
                lprButton.setBackgroundResource(R.drawable.btn_yellow);
                lprButton.setClickable(true);
                hotListButton.setBackgroundResource(R.drawable.btn_hotlist_yellow);
                hotListButton.setClickable(true);

            } else {
                if (isServiceAvailable) {
                    statusIndicatorImageView1.setImageResource(R.drawable.green_status_btn_bk);
                }
            }
            //statusIndicatorImageView1.setImageResource(R.drawable.green_status_btn_bk);
            if (!isFastConnection) {
                UIHelper.toggleButtonState(lprButton, false);
                statusIndicatorImageView.setImageResource(R.drawable.yellow_status_btn);

            }
        } else {
            // startListen();
            UIHelper.toggleButtonState(gpsButton, false);
            UIHelper.toggleButtonState(lprButton, false);
            //UIHelper.toggleButtonState(hotListButton, false);
            UIHelper.toggleButtonState(alprBtn, false);

            statusIndicatorImageView.setImageResource(R.drawable.gray_status_btn);
            statusIndicatorImageView1.setImageResource(R.drawable.gray_status_btn);
            // This code is added by mohit 12/01/2023. This code is used for changing status in to offline mode
            status = true;

        }

      /*  try {
            if (TPUtility.isRunningOnEmulator(getApplicationContext())) {
                UIHelper.toggleButtonState(gpsButton, false);
                UIHelper.toggleButtonState(lprButton, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void checkPlateAction(View view) {
        String plate = plateNumberEditText.getText().toString();
        if (plate.length() == 0) {
            Toast.makeText(this, "Please enter vehicle details", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isServiceAvailable) {
            Toast.makeText(this, "Service not available. Please check network settings and try again.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        lookupThread.getHandler().checkPlate(plate);
    }

    private void sendEmail(final String from, final String[] to, final String subject, final String message) {
        try {
            TPUtility.hideSoftKeyboard(WriteTicketActivity.this);

            if (!isServiceAvailable) {
                Toast.makeText(this, "Network not available, please try again", Toast.LENGTH_LONG).show();
                return;
            }

            //progressDialog = ProgressDialog.show(this, "", "Sending Email...");

            if (isServiceAvailable) {
                WriteTicketNetworkCalls.sendEmail(WriteTicketActivity.this, from, to, subject, message);
            }

            /*AsyncTask<Void, Void, Boolean> emailTask = new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    try {
                        return ((WriteTicketBLProcessor) screenBLProcessor).sendEmail(from, to, subject, message);
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    return false;
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (result.booleanValue()) {
                        Toast.makeText(WriteTicketActivity.this, "Your email sent successfully", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Toast.makeText(WriteTicketActivity.this, "Failed sending email, please try again",
                                Toast.LENGTH_LONG).show();
                    }
                }
            };

            emailTask.execute();
*/
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void sendTowNotifyEmail(final String tickets, final String amounts) {
        try {
            updateTicket(true);

            emailDialog = new Dialog(WriteTicketActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDlgView = layoutInflater.inflate(R.layout.send_email_input_dialog, null, false);
            emailDialog.setTitle("Tow Vehicle");
            emailDialog.setContentView(inputDlgView);
            emailDialog.show();

            Button sendBtn = inputDlgView.findViewById(R.id.send_email_input_dialog_enter_btn);
            emailMessageText = inputDlgView.findViewById(R.id.send_email_input_dialog_text_field);
            emailMessageText.setOnLongClickListener(v -> true);

            emailMessageExtraText = inputDlgView.findViewById(R.id.send_email_extra_input_dialog_text_field);
            emailMessageExtraText.setOnLongClickListener(v -> {
                emailMessageExtraText.setText("");
                return true;
            });

            emailMessageExtraText.requestFocus();

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                emailMessageText.setText(Html.fromHtml(TPUtility.getTowNotifyEmail(WriteTicketActivity.this, tickets, amounts)));

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }, 100);

            sendBtn.setOnClickListener(view -> {
                TPUtility.hideSoftKeyboard(WriteTicketActivity.this);

                if (emailDialog.isShowing()) {
                    emailDialog.dismiss();
                }

                try {
                    ArrayList<Contact> contacts = Contact.getTowNotifyContacts();
                    if (contacts.size() == 0) {
                        Toast.makeText(WriteTicketActivity.this,
                                        "Contact information not available. Please sync the device", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }

                    String msg = Html.toHtml(emailMessageText.getText());
                    msg += "<br/>" + Html.toHtml(emailMessageExtraText.getText());

                    // Attached Images as HTML Image Data
                    if (activeTicket.getTicketPictures().size() > 0) {
                        for (TicketPicture picture : activeTicket.getTicketPictures()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(picture.getImagePath());
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(CompressFormat.PNG, 0, bos);
                            byte[] bitmapdata = bos.toByteArray();
                            String dataToUpload = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
                            msg = msg + "<br/><img src='" + dataToUpload + "'>";
                        }
                    }

                    String[] emails = new String[contacts.size()];
                    for (int i = 0; i < contacts.size(); i++) {
                        emails[i] = contacts.get(i).getEmailId();
                    }

                    sendEmail(TPApp.getUserInfo().getEmailAddress(), emails, "Tow Vehicle", msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Button cancelBtn = inputDlgView.findViewById(R.id.send_email_input_dialog_cancel_btn);
            cancelBtn.setOnClickListener(view -> {
                TPUtility.hideSoftKeyboard(WriteTicketActivity.this);

                if (emailDialog.isShowing()) {
                    emailDialog.dismiss();
                }
            });
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void sendSupportEmail() {
        try {
            updateTicket();

            emailDialog = new Dialog(WriteTicketActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDlgView = layoutInflater.inflate(R.layout.send_email_input_dialog, null, false);
            emailDialog.setTitle("Send Support Email");
            emailDialog.setContentView(inputDlgView);
            emailDialog.show();

            Button sendBtn = inputDlgView.findViewById(R.id.send_email_input_dialog_enter_btn);
            emailMessageText = inputDlgView.findViewById(R.id.send_email_input_dialog_text_field);
            emailMessageText.setOnLongClickListener(v -> true);

            emailMessageExtraText = inputDlgView.findViewById(R.id.send_email_extra_input_dialog_text_field);
            emailMessageExtraText.setOnLongClickListener(v -> {
                emailMessageExtraText.setText("");
                return true;
            });

            emailMessageExtraText.requestFocus();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }, 50);

            emailMessageText.setText(Html.fromHtml(TPUtility.getSupportEmail(this, activeTicket)));
            sendBtn.setOnClickListener(view -> {
                if (emailDialog.isShowing()) {
                    emailDialog.dismiss();
                }

                String msg = Html.toHtml(emailMessageText.getText());
                msg += "<br/>" + Html.toHtml(emailMessageExtraText.getText());

                try {
                    ArrayList<Contact> contacts = Contact.getSupportContacts();
                    if (contacts.size() == 0) {
                        String errorText = "Contact information not available. Please sync the device";
                        Toast.makeText(WriteTicketActivity.this, errorText, Toast.LENGTH_LONG).show();
                        return;
                    }

                    String[] emails = new String[contacts.size()];
                    for (int i = 0; i < contacts.size(); i++) {
                        emails[i] = contacts.get(i).getEmailId();
                    }

                    String emailAddress = TPApp.getUserInfo().getEmailAddress();
                    if (emailAddress == null || emailAddress.equals("")) {
                        emailAddress = "device@turbodata.com";
                    }

                    String citation = TPUtility.prefixZeros(activeTicket.getCitationNumber(), 8);
                    String subject = TPUtility.getSupportEmailSubject(citation,
                            TPUtility.getValidPlate(plateNumberEditText.getText().toString()));

                    sendEmail(emailAddress, emails, subject, msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Button cancelBtn = inputDlgView.findViewById(R.id.send_email_input_dialog_cancel_btn);
            cancelBtn.setOnClickListener(view -> {
                TPUtility.hideSoftKeyboard(WriteTicketActivity.this);

                if (emailDialog.isShowing()) {
                    emailDialog.dismiss();
                }
            });

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void checkZones() {
        final Intent intent = new Intent();
        ArrayList<String> _aListOfEnableService = new ArrayList<>();
        if (Feature.isFeatureAllowed(Feature.PARK_MOBILE) && TPApp.enableParkMobile) {
            _aListOfEnableService.add(Feature.PARK_MOBILE);

        }
        if (Feature.isFeatureAllowed(Feature.PASSPORT_PARKING) && TPApp.enablePassportParking) {
            _aListOfEnableService.add(Feature.PASSPORT_PARKING);

        }
        if (Feature.isFeatureAllowed(Feature.PAYBY_PHONE) && TPApp.enablePayByPhone) {
            _aListOfEnableService.add(Feature.PAYBY_PHONE);
        }
        if (Feature.isFeatureAllowed(Feature.DIGITAL_PAYTECH) && TPApp.enableDPT) {
            _aListOfEnableService.add(Feature.DIGITAL_PAYTECH);
        }
        if (Feature.isFeatureAllowed(Feature.IPS_GROUP) && TPApp.enableIPS) {
            _aListOfEnableService.add(Feature.IPS_GROUP);
        }
        if (Feature.isFeatureAllowed(Feature.CALE) && TPApp.enableCale) {
            _aListOfEnableService.add(Feature.CALE);
        }
        if (Feature.isFeatureAllowed(Feature.CALE2) && TPApp.enableCale2) {
            _aListOfEnableService.add(Feature.CALE2);
        }

        if (Feature.isFeatureAllowed(Feature.PARKEON) && TPApp.enableParkeon) {
            _aListOfEnableService.add(Feature.PARKEON);
        }
        if (Feature.isFeatureAllowed(Feature.SAMTRANS) && TPApp.enableSamtrans) {
            _aListOfEnableService.add(Feature.SAMTRANS);
        }
        if (Feature.isFeatureAllowed(Feature.PARK_GENETEC) && TPApp.enableGenetec) {
            _aListOfEnableService.add(Feature.PARK_GENETEC);
        }
        if (Feature.isFeatureAllowed(Feature.PASSPORT_PARKING2) && TPApp.enablePassportParking2) {
            _aListOfEnableService.add(Feature.PASSPORT_PARKING2);
        }
        if (Feature.isFeatureAllowed(Feature.PARK_CUBTRAC) && TPApp.enableCubtrac) {
            _aListOfEnableService.add(Feature.PARK_CUBTRAC);
        }
        if (Feature.isFeatureAllowed(Feature.PARK_CURBSENSE) && TPApp.enableCurbsense) {
            _aListOfEnableService.add(Feature.getFeatureValue(Feature.PARK_CURBSENSE));
        }
        if (Feature.isFeatureAllowed(Feature.PARK_ZONEPOLE) && TPApp.enableZonePole) {
            _aListOfEnableService.add(Feature.getFeatureValue(Feature.PARK_ZONEPOLE));
        }

        if (_aListOfEnableService.size() > 1) {

            AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
            builder.setTitle("Choose vendor list");

            final String[] stringArray = _aListOfEnableService.toArray(new String[_aListOfEnableService.size()]);
            builder.setItems(stringArray, (dialog, which) -> {
                String strName = stringArray[which];
                if (strName.equals(Feature.PARK_MOBILE)) {
                    TPApplication.getInstance().setIpsParkMobile(0);
                    intent.setClass(WriteTicketActivity.this, ParkMobileZonesActivity.class);
                    startActivity(intent);
                } else if (strName.equals(Feature.PASSPORT_PARKING) && TPApp.enablePassportParking) {
                    intent.setClass(WriteTicketActivity.this, PassportParkingZonesActivity.class);
                    startActivity(intent);
                } else if (strName.equals(Feature.PAYBY_PHONE) && TPApp.enablePayByPhone) {
                    intent.setClass(WriteTicketActivity.this, PayByPhoneZonesActivity.class);
                    startActivity(intent);
                } else if (strName.equals(Feature.DIGITAL_PAYTECH) && TPApp.enableDPT) {
                    intent.setClass(WriteTicketActivity.this, DigitalPaytechZonesActivity.class);
                    startActivity(intent);
                } else if (strName.equals(Feature.IPS_GROUP) && TPApp.enableIPS) {
                    TPApplication.getInstance().setIpsParkMobile(1);
                    lookupThread.getHandler().lookupSpaceIPS("");
                } else if (strName.equals(Feature.CALE) && TPApp.enableCale) {
                    intent.setClass(WriteTicketActivity.this, CaleZoneActivity.class);
                    startActivity(intent);
                } else if (strName.equals(Feature.PARKEON) && TPApp.enableParkeon) {
                    // intent.setClass(WriteTicketActivity.this, ParkeonZoneActivity.class);
                    intent.setClass(WriteTicketActivity.this, ParkeonControlGroup.class);
                    startActivity(intent);
                } else if (strName.equals(Feature.SAMTRANS) && TPApp.enableSamtrans) {
                    intent.setClass(WriteTicketActivity.this, SamtransZoneActivity.class);
                    startActivity(intent);
                } else if (strName.equals(Feature.PARK_GENETEC) && TPApp.enableGenetec) {
                    Intent i = new Intent();
                    i.setClass(WriteTicketActivity.this, GenetecPatrollerActivity.class);
                    startActivity(i);
                    finish();
                } else if (strName.equals(Feature.PASSPORT_PARKING2) && TPApp.enablePassportParking2) {
                    intent.setClass(WriteTicketActivity.this, PP2ZoneListClass.class);
                    startActivity(intent);
                } else if (strName.equals(Feature.PARK_CUBTRAC) && TPApp.enableCubtrac) {
                    //__openDialogForZoneList();
                    intent.setClass(WriteTicketActivity.this, CurbtrackZoneList.class);
                    startActivity(intent);

                } else if (strName.equals(Feature.getFeatureValue(Feature.PARK_CURBSENSE)) && TPApp.enableCurbsense) {
                    //__openDialogForZoneList();
                    intent.setClass(WriteTicketActivity.this, CurveSenseActivity.class);
                    startActivity(intent);

                }else if (strName.equals(Feature.getFeatureValue(Feature.PARK_ZONEPOLE)) && TPApp.enableZonePole) {
                    //__openDialogForZoneList();
                    intent.setClass(WriteTicketActivity.this, ZonePoleList.class);
                    startActivity(intent);

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            if (Feature.isFeatureAllowed(Feature.PARK_MOBILE) && TPApp.enableParkMobile) {
                TPApplication.getInstance().setIpsParkMobile(0);
                intent.setClass(this, ParkMobileZonesActivity.class);
                startActivity(intent);
            } else if (Feature.isFeatureAllowed(Feature.PASSPORT_PARKING) && TPApp.enablePassportParking) {
                intent.setClass(this, PassportParkingZonesActivity.class);
                startActivity(intent);
            } else if (Feature.isFeatureAllowed(Feature.PAYBY_PHONE) && TPApp.enablePayByPhone) {
                intent.setClass(this, PayByPhoneZonesActivity.class);
                startActivity(intent);
            } else if (Feature.isFeatureAllowed(Feature.DIGITAL_PAYTECH) && TPApp.enableDPT) {
                intent.setClass(WriteTicketActivity.this, DigitalPaytechZonesActivity.class);
                startActivity(intent);
            } else if (Feature.isFeatureAllowed(Feature.IPS_GROUP) && TPApp.enableIPS) {
                TPApplication.getInstance().setIpsParkMobile(1);
                lookupThread.getHandler().lookupSpaceIPS("");
            } else if (Feature.isFeatureAllowed(Feature.CALE) && TPApp.enableCale) {
                intent.setClass(WriteTicketActivity.this, CaleZoneActivity.class);
                startActivity(intent);
            } else if (Feature.isFeatureAllowed(Feature.PARKEON) && TPApp.enableParkeon) {
                //intent.setClass(WriteTicketActivity.this, ParkeonZoneActivity.class);
                intent.setClass(WriteTicketActivity.this, ParkeonControlGroup.class);
                startActivity(intent);
            } else if (Feature.isFeatureAllowed(Feature.SAMTRANS) && TPApp.enableSamtrans) {
                intent.setClass(WriteTicketActivity.this, SamtransZoneActivity.class);
                startActivity(intent);
            } else if (Feature.isFeatureAllowed(Feature.PASSPORT_PARKING2) && TPApp.enablePassportParking2) {
                intent.setClass(WriteTicketActivity.this, PP2ZoneListClass.class);
                startActivity(intent);
            } else if (Feature.isFeatureAllowed(Feature.PARK_CUBTRAC) && TPApp.enableCubtrac) {
                intent.setClass(WriteTicketActivity.this, CurbtrackZoneList.class);
                startActivity(intent);
            } else if (Feature.isFeatureAllowed(Feature.PARK_GENETEC) && TPApp.enableGenetec) {
                intent.setClass(WriteTicketActivity.this, GenetecPatrollerActivity.class);
                startActivity(intent);
            } else if (Feature.isFeatureAllowed(Feature.getFeatureValue(Feature.PARK_CURBSENSE)) && TPApp.enableCurbsense) {
                intent.setClass(WriteTicketActivity.this, CurveSenseActivity.class);
                startActivity(intent);
            }else if (Feature.isFeatureAllowed(Feature.getFeatureValue(Feature.PARK_ZONEPOLE)) && TPApp.enableZonePole) {
                intent.setClass(WriteTicketActivity.this, ZonePoleList.class);
                startActivity(intent);
            }

            else {
                intent.setClass(this, ZoneListActivity.class);
                startActivity(intent);
            }
        }

        /*Intent intent = new Intent();

        if (Feature.isFeatureAllowed(Feature.PARK_MOBILE) && TPApp.enableParkMobile) {
            intent.setClass(this, ParkMobileZonesActivity.class);
            startActivity(intent);
        } else if (Feature.isFeatureAllowed(Feature.PASSPORT_PARKING) && TPApp.enablePassportParking) {
            intent.setClass(this, PassportParkingZonesActivity.class);
            startActivity(intent);
        } else if (Feature.isFeatureAllowed(Feature.PAYBY_PHONE) && TPApp.enablePayByPhone) {
            intent.setClass(this, PayByPhoneZonesActivity.class);
            startActivity(intent);
        } else if (Feature.isFeatureAllowed(Feature.DIGITAL_PAYTECH) && TPApp.enableDPT) {
            intent.setClass(WriteTicketActivity.this, DigitalPaytechZonesActivity.class);
            startActivity(intent);
        } else if (Feature.isFeatureAllowed(Feature.IPS_GROUP) && TPApp.enableIPS) {
            lookupThread.getHandler().lookupSpaceIPS("");
        } else if (Feature.isFeatureAllowed(Feature.CALE) && TPApp.enableCale) {
            intent.setClass(WriteTicketActivity.this, CaleZoneActivity.class);
            startActivity(intent);
        } else {
            intent.setClass(this, ZoneListActivity.class);
            startActivity(intent);
        }*/
    }

    private void __openDialogForZoneList() {

        progressDialog = new ProgressDialog(WriteTicketActivity.this);
        progressDialog.setMessage("Looking Curbtrac zone");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();

        if (isServiceAvailable) {
            if (!(TPApp.enableCubtrac && Feature.isFeatureAllowed(Feature.PARK_CUBTRAC))) {
                return;
            }

            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.CUBTRAC_ZONE_SEARCH, TPApp.deviceId, "/");
            Map<String, String> paramsMap = config.getParamsMap();
            String user = paramsMap.get("User");
            String password = paramsMap.get("Password");
            String serviceURL = config.getServiceURL();
            String token = paramsMap.get("token");
            ApiRequest service = ServiceGeneratorCubTrac.createService(ApiRequest.class);
            service.cubtracGetZone(serviceURL, token).enqueue(new retrofit2.Callback<List<CubTracZone>>() {
                @Override
                public void onResponse(Call<List<CubTracZone>> call, Response<List<CubTracZone>> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful() && response.code() == 200) {
                        List<CubTracZone> zoneList = response.body();
                        if (zoneList != null && zoneList.size() > 0) {

                            ItemAdapter adapter = new ItemAdapter(WriteTicketActivity.this, zoneList);

                            AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
                            builder.setTitle("Select Zone");
                            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Handle item click
                                    CubTracZone selectedItem = zoneList.get(which);
                                    preference.putString("ZONE_ID", selectedItem.getAssignedId());
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builder.show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<CubTracZone>> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });

        }

    }

    private String getExpDateString(String expDate) {
        String month = "";
        String year = "";

        if (expDate.contains("/")) {
            String[] dates = expDate.split("/");
            month = dates[0];

            if (dates.length > 1) {
                year = dates[1];
            }
        } else {
            if (TPUtility.isNumeric(expDate)) {
                year = expDate;
            } else {
                month = expDate;
            }
        }

        if (year.length() == 4) {
            year = year.substring(2);
        }

        if (month.length() > 0 && year.length() > 0) {
            return month + "/" + year;
        } else if (month.length() > 0) {
            return month;
        } else if (year.length() > 0) {
            return "/" + year;
        } else {
            return "";
        }
    }

    public boolean isDuplicateViolation(int violation) {
        boolean result = false;
        for (TicketViolation ticketViolation : activeTicket.getTicketViolations()) {
            if (ticketViolation.getViolationId() == violation) {
                return true;
            }
        }

        return result;
    }

    public void voiceMemoAction(View view) {
        if (isRecording) {
            this.stopRecording();

        } else if (isPlaying) {
            this.stopPlaying();

        } else if (audioFile == null) {
            this.startRecording();

        } else if (!isPlaying && audioFile != null) {
            this.playAudio();
        }
    }

    private void removeVoiceMemo() {
        if (audioFile == null) {
            return;
        }

        if (isPlaying) {
            stopPlaying();
        }

        if (isRecording) {
            stopRecording();
        }

        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
        confirmBuilder.setTitle("VoiceMemo").setMessage("Are you sure you want to remove the voice memo?")
                .setCancelable(false).setNegativeButton("No", (dialog, which) -> {

                }).setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    TPUtility.removeFile(TPUtility.getVoiceMemosFolder() + audioFile);

                    audioFile = null;
                    audioPlayer = null;
                    voiceMemoBtn.setImageDrawable(getResources().getDrawable(R.drawable.mic));
                });

        AlertDialog confirmAlert = confirmBuilder.create();
        confirmAlert.show();
    }

    private void startRecording() {
        if (audioFile == null) {
            int voiceCommentId = (int) (Math.random() * 25 + TicketComment.getNextPrimaryId());
            audioFile = TPUtility.prefixZeros(activeTicket.getCitationNumber(), 8) + "-VOICE-"
                    + TPUtility.prefixZeros(voiceCommentId, 2) + ".mp3";
        }

        timerTask = new TimerTask() {
            public void run() {
                timerHandler.post(() -> {
                    stopRecording();

                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                });
            }
        };

        try {
            isRecording = true;
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(TPUtility.getVoiceMemosFolder() + audioFile);
            recorder.prepare();
            recorder.start();

            if (Feature.isFeatureAllowed(Feature.RECORDING_DURATION)) {
                try {
                    String duration = Feature.getFeatureValue(Feature.RECORDING_DURATION);
                    if (duration != null && !duration.isEmpty()) {
                        int secs = Integer.parseInt(duration);
                        if (secs > 0) {
                            timer = new Timer();
                            timer.schedule(timerTask, secs * 1000);
                        }
                    }
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            }

            voiceMemoBtn.setImageDrawable(getResources().getDrawable(R.drawable.player_stop));

        } catch (IOException e) {
            log.error("Error while recording audio. " + e.getMessage());
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            recorder.release();

            voiceMemoBtn.setImageDrawable(getResources().getDrawable(R.drawable.player_play));
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        isRecording = false;
    }

    public void playAudio() {
        if (audioFile == null || isPlaying || isRecording) {
            return;
        }

        playerDialog = new Dialog(this);
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View inputDlgView = layoutInflater.inflate(R.layout.audio_player_dialog, null, false);
        playerDialog.setCancelable(false);
        playerDialog.setTitle("Voice Memo");
        playerDialog.setContentView(inputDlgView);
        playerDialog.show();

        Button cancelBtn = inputDlgView.findViewById(R.id.audio_cancel_btn);
        Button deleteBtn = inputDlgView.findViewById(R.id.audio_stop_btn);
        deleteBtn.setText("Delete");

        playBtn = inputDlgView.findViewById(R.id.audio_play_btn);
        seekbar = inputDlgView.findViewById(R.id.audio_seekbar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }
        });

        cancelBtn.setOnClickListener(view -> {
            if (playerDialog.isShowing())
                playerDialog.dismiss();
        });

        deleteBtn.setOnClickListener(view -> {
            if (playerDialog.isShowing()) {
                playerDialog.dismiss();
            }

            removeVoiceMemo();
        });

        playBtn.setOnClickListener(view -> {
            if (isPlaying) {
                isPlaying = false;
                audioPlayer.stop();
                playBtn.setText("Play");
            } else {
                playRecording(audioFile);
                playBtn.setText("Stop");
            }
        });

    }

    private void playRecording(String audioFile) {
        playerHandler = new Handler();
        try {
            audioPlayer = new MediaPlayer();
            audioPlayer.setDataSource(TPUtility.getVoiceMemosFolder() + audioFile);
            audioPlayer.prepare();
            audioPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            log.error("Error playing recording.");
        }

        int mediaPos = audioPlayer.getCurrentPosition();
        int mediaMax = audioPlayer.getDuration();
        seekbar.setMax(mediaMax);
        seekbar.setProgress(mediaPos);
        playerHandler.removeCallbacks(moveSeekBarThread);
        playerHandler.postDelayed(moveSeekBarThread, 100);

        audioPlayer.setOnCompletionListener(mp -> {
            isPlaying = false;
            if (playerDialog.isShowing()) {
                playerDialog.dismiss();
            }

            audioPlayer.reset();
            audioPlayer.release();
        });
    }

    private void stopPlaying() {
        isPlaying = false;
        if (player != null) {
            player.stop();
            player.reset();
            player.release();

            voiceMemoBtn.setImageDrawable(getResources().getDrawable(R.drawable.player_play));
        }
    }

    private boolean isAutoLookupRequired() {
        return !isUpdatingResult && !isProcessingLookup && TPApp.getUserSettings() != null
                && TPApp.getUserSettings().isAutoLookup();
    }

    private void adjustScreenBrightness() {
        final ContentResolver Conresolver;
        final Dialog dialog;
        boolean isAutoBright;
        final SeekBar seekbar;
        final CheckBox autoBright;
        final Window window = getWindow();
        final int autoBrightness = 80;
        final int savedBrightness;

        try {
            dialog = new Dialog(this);
            Conresolver = getContentResolver();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.brightness);
            dialog.setCanceledOnTouchOutside(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialog.getWindow().setAttributes(lp);

            seekbar = dialog.findViewById(R.id.seekBar);
            autoBright = dialog.findViewById(R.id.autoBright);
            seekbar.setMax(255);

            isAutoBright = mPreferences.getBoolean(TPConstant.PREFS_KEY_SETTING_AUTO_BRIGHTNESS, false);
            savedBrightness = mPreferences.getInt(TPConstant.PREFS_KEY_SETTING_SAVED_BRIGHTNESS, 5);

            if (isAutoBright) {
                android.provider.Settings.System.putInt(Conresolver, Settings.System.SCREEN_BRIGHTNESS, autoBrightness);
                autoBright.setChecked(true);
                seekbar.setProgress(autoBrightness);
            } else {
                autoBright.setChecked(false);
                if (savedBrightness != 5) {
                    seekbar.setProgress(savedBrightness);
                    android.provider.Settings.System.putInt(Conresolver, Settings.System.SCREEN_BRIGHTNESS,
                            savedBrightness);
                } else {
                    brightness = android.provider.Settings.System.getInt(Conresolver,
                            Settings.System.SCREEN_BRIGHTNESS);
                    seekbar.setProgress(brightness);
                    android.provider.Settings.System.putInt(Conresolver, Settings.System.SCREEN_BRIGHTNESS,
                            savedBrightness);
                }
            }

            autoBright.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // update your model (or other business logic) based on
                // isChecked
                if (isChecked) {
                    seekbar.setProgress(autoBrightness);
                    Settings.System.putInt(Conresolver, Settings.System.SCREEN_BRIGHTNESS,
                            autoBrightness);
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean(TPConstant.PREFS_KEY_SETTING_AUTO_BRIGHTNESS, true);
                    editor.commit();

                    WindowManager.LayoutParams layoutpars = window.getAttributes();
                    layoutpars.screenBrightness = autoBrightness / (float) 255;
                    window.setAttributes(layoutpars);
                } else {
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean(TPConstant.PREFS_KEY_SETTING_AUTO_BRIGHTNESS, false);
                    editor.commit();
                }
            });

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    brightness = Math.max(progress, 5);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    android.provider.Settings.System.putInt(Conresolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                    android.view.WindowManager.LayoutParams layoutpars = window.getAttributes();
                    layoutpars.screenBrightness = brightness / (float) 255;
                    window.setAttributes(layoutpars);

                    if (autoBright.isChecked()) {
                        autoBright.setChecked(false);
                    }

                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putInt(TPConstant.PREFS_KEY_SETTING_SAVED_BRIGHTNESS, brightness);
                    editor.commit();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }
            });

            dialog.show();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        if(mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent mIntent = new Intent(this, LocationService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(WriteTicketActivity.this, "Service is disconnected", 1000).show();
            mBounded = false;
            mServer = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(WriteTicketActivity.this, "Service is connected", 1000).show();
            mBounded = true;
            LocationService.LocalBinder mLocalBinder = (LocationService.LocalBinder)service;
            mServer = mLocalBinder.getServerInstance();
        }
    };*/
    // update ticket for void reason
    private void updateTicket(int which, Ticket ticket, ArrayList<VoidTicketReason> reasons) {
        try {
            ticket.setIsVoid("Y");
            ticket.setFine(0);
            ticket.setVoidId(voidListFlag ? reasons.get(which).getId() : 0);
            ticket.setVoidReasonCode(voidListFlag ? reasons.get(which).getCode() : "");

            Ticket.updateTicket(ticket);

            boolean syncFlag = false;
            isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
            if (isServiceAvailable) {
                try {
                    WriteTicketBLProcessor blProcessor = (WriteTicketBLProcessor) screenBLProcessor;
                    ArrayList<Ticket> tickets = new ArrayList<>();
                    tickets.add(ticket);
                    syncFlag = blProcessor.getProxy().updateTickets(tickets);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // UPDATE SYNC DATA
            if (!syncFlag) {
                SyncData syncData = new SyncData();
                syncData.setActivity("UPDATE");
                syncData.setPrimaryKey(ticket.getTicketId() + "");
                syncData.setActivityDate(new Date());
                syncData.setCustId(TPApp.custId);
                syncData.setTableName(TPConstant.TABLE_TICKETS);
                syncData.setStatus("Pending");
                Completable completable8 = SyncData.insertSyncData(syncData);
                completable8.subscribeWith(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
                //DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
            }

            //DatabaseHelper.getInstance().closeWritableDb();
            Toast.makeText(WriteTicketActivity.this, "Updated ticket successfully.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Void ticket and update custom reason
    private void addOtherCommentReson(String voidReason, int which, Ticket ticket, ArrayList<VoidTicketReason> reasons) {

        //DatabaseHelper.getInstance().openWritableDatabase();
        TicketComment tc = new TicketComment();
        int ticketCommentId = 0;

        ArrayList<TicketComment> ticketComments = new ArrayList<TicketComment>();
        try {

            tc.setCommentId(0); // As said
            tc.setTicketId(ticket.getTicketId());
            tc.setCitationNumber(ticket.getCitationNumber());
            tc.setCustId(TPApp.custId);
            tc.setComment(voidReason);
            tc.setVoiceComment(false);
            TicketComment.insertTicketComment(tc);

           /* //DatabaseHelper.getInstance().insertOrReplace(tc.getContentValues(), "ticket_comments");

            SyncData syncCommentData = new SyncData();
            syncCommentData.setActivity("INSERT");
            syncCommentData.setActivityDate(new Date());
            syncCommentData.setCustId(TPApp.custId);
            syncCommentData.setTableName(TPConstant.TABLE_TICKET_COMMENTS);
            syncCommentData.setStatus("Pending");*/

            boolean result = false;
            isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
            if (isServiceAvailable) {
                ticketComments.add(tc);
                try {
                    WriteTicketBLProcessor blProcessor = (WriteTicketBLProcessor) screenBLProcessor;
                    result = blProcessor.getProxy().updateTicketsComments(ticket.getViolationId(), ticketComments);

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }

            }

            /*if (!result) {
                try {
                    Completable completable = SyncData.insertSyncData(syncCommentData);
                    completable.subscribeWith(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }
                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
                    //DatabaseHelper.getInstance().insertOrReplace(syncCommentData.getContentValues(), "sync_data");
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
                // syncDataList.add(syncCommentData);
            }*/
            // DatabaseHelper.getInstance().closeWritableDb();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            updateTicket(which, ticket, reasons);
        }
    }

    // Show popup to get void reason from users and sync it to server after
    // issuing the ticket
    private void otherVoidReasonPopup(final int which, final Ticket ticket, final ArrayList<VoidTicketReason> reasons) {
        try {
            otherReasonDialog = new Dialog(this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View inputDialogView = layoutInflater.inflate(R.layout.void_reason_input_dialog, null, false);
            otherReasonDialog.setTitle("Add Void Reason");
            otherReasonDialog.setContentView(inputDialogView);
            otherReasonDialog.show();
            Button saveBtn = inputDialogView.findViewById(R.id.void_reason_input_dialog_enter_btn);
            voidReasonText = inputDialogView.findViewById(R.id.void_reason_input_dialog_text_field);
            TPUtility.showSoftKeyboard(WriteTicketActivity.this, voidReasonText);
            saveBtn.setOnClickListener(view -> {
                TPUtility.hideSoftKeyboard(WriteTicketActivity.this);
                if (otherReasonDialog.isShowing()) {
                    otherReasonDialog.dismiss();
                }

                addOtherCommentReson(voidReasonText.getText().toString(), which, ticket, reasons);
            });

            Button cancelBtn = inputDialogView.findViewById(R.id.void_reason_input_dialog_cancel_btn);
            cancelBtn.setOnClickListener(view -> {
                TPUtility.hideSoftKeyboard(WriteTicketActivity.this);

                if (otherReasonDialog.isShowing()) {
                    otherReasonDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isAutoPromptVehicle() {
        boolean isAutoPrmpt;
        isAutoPrmpt = TPApp.getUserSettings() != null && TPApp.getUserSettings().isAutoPromptVehicle();
        return isAutoPrmpt;
    }

    public void vinOCRAction(View view) {
        // Intent for SegmentScanActivity Activity
        Intent intent = new Intent(this, ScanImageActivity.class);
        intent.putExtra(ExtrasKeys.EXTRAS_LICENSE_KEY,
                "GJQBNWD2-EIZ7XZGS-U352YY4S-DUSK7VFZ-ANZ3EOZE-V7KLSA3T-WI5SID5Y-QPZWZNIA");
        intent.putExtra(ExtrasKeys.EXTRAS_IMAGE_PATH,
                TPUtility.getTicketsFolder() + "OCR_VIN_" + System.currentTimeMillis() + ".JPG");

        // prepare settings for raw OCR
        /*
         * BlinkOCRRecognizerSettings ocrSett = new BlinkOCRRecognizerSettings();
         * VinParserSettings vinSett = new VinParserSettings();
         * ocrSett.addParser("Data", vinSett);
         *
         * // prepare recognition settings RecognitionSettings mRecognitionSettings =
         * new RecognitionSettings();
         *
         * // set recognizer settings array that is used to configure recognition //
         * BlinkOCRRecognizer will be used in the recognition process
         * mRecognitionSettings.setRecognizerSettingsArray(new
         * RecognizerSettings[]{ocrSett});
         *
         * intent.putExtra(ExtrasKeys.EXTRAS_RECOGNITION_SETTINGS,
         * mRecognitionSettings);
         */

        // Starting Activity
        startActivityForResult(intent, REQUEST_VIN_OCRCODE);
    }

    public void plateOCRAction(View view) {
        // Intent for SegmentScanActivity Activity
        Intent intent = new Intent(this, ScanImageActivity.class);
        intent.putExtra(ExtrasKeys.EXTRAS_LICENSE_KEY,
                "GJQBNWD2-EIZ7XZGS-U352YY4S-DUSK7VFZ-ANZ3EOZE-V7KLSA3T-WI5SID5Y-QPZWZNIA");
        intent.putExtra(ExtrasKeys.EXTRAS_IMAGE_PATH,
                TPUtility.getTicketsFolder() + "OCR_PLATE_" + System.currentTimeMillis() + ".JPG");

        // prepare settings for raw OCR
        /*
         * BlinkOCRRecognizerSettings ocrSett = new BlinkOCRRecognizerSettings();
         * LicensePlatesParserSettings plateSett = new LicensePlatesParserSettings();
         * ocrSett.addParser("Data", plateSett);
         *
         * // prepare recognition settings RecognitionSettings mRecognitionSettings =
         * new RecognitionSettings();
         *
         * // set recognizer settings array that is used to configure recognition //
         * BlinkOCRRecognizer will be used in the recognition process
         * mRecognitionSettings.setRecognizerSettingsArray(new
         * RecognizerSettings[]{ocrSett});
         *
         * intent.putExtra(ExtrasKeys.EXTRAS_RECOGNITION_SETTINGS,
         * mRecognitionSettings);
         */

        // Starting Activity
        startActivityForResult(intent, REQUEST_PLATE_OCRCODE);
    }

    public void hotListAction(View view) {
        try {
            if (!isServiceAvailable) {
                displayErrorMessage("Service not available. Please check network settings and try again.");
                return;
            }
            emailDialog = new Dialog(WriteTicketActivity.this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            inputDlgView = layoutInflater.inflate(R.layout.dialog_hotlist, null, false);
            emailDialog.setTitle("Add HotList");
            emailDialog.setContentView(inputDlgView);
            emailDialog.setCanceledOnTouchOutside(false);
            emailDialog.show();

            final EditText commentEditText = inputDlgView.findViewById(R.id.hotlist_Comment);
            Button btnCancel = inputDlgView.findViewById(R.id.btnCancel);
            Button btnAccept = inputDlgView.findViewById(R.id.btnAccept);
            Button btnClear = inputDlgView.findViewById(R.id.btnClear);
            beginDate = inputDlgView.findViewById(R.id.hotlist_BeginDate);
            endDate = inputDlgView.findViewById(R.id.hotlist_EndDate);

            commentEditText.requestFocus();
            /*
             * String badge =""; if (TPApp.getUserInfo() != null) badge =
             * TPApp.getUserInfo().getBadge()+"- "; commentEditText.setText(badge+"- ");
             */

            beginDate.setText(DateUtil.getBeginEndDate(new Date()));

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                // commentEditText.setSelection(commentEditText.getText().length());
            }, 50);
            btnCancel.setOnClickListener(v -> {
                emailDialog.dismiss();
                TPUtility.hideSoftKeyboard(WriteTicketActivity.this);
            });
            btnClear.setOnClickListener(v -> {
                commentEditText.setText("");
                // beginDate.setText("");
                endDate.setText("");
            });
            /*
             * beginDate.setOnClickListener(new View.OnClickListener() {
             *
             * @Override public void onClick(View v) { showDatePicker(true); } });
             */
            endDate.setOnClickListener(v -> showDatePicker(false));

            btnAccept.setOnClickListener(v -> {
                // Ask permission to send data to server
                if (commentEditText.getText().toString().trim().isEmpty()) {
                    displayErrorMessage("Please provide your comment");
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(WriteTicketActivity.this);
                builder.setTitle("Confirmation").setMessage("Are you sure you want to add this plate as HotList?")
                        .setCancelable(false).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).setPositiveButton("OK", (dialog, which) -> {
                            // process ticket to server or offline
                            if (!commentEditText.getText().toString().trim().isEmpty()) {
                                addNewHotList(plateNumberEditText.getText().toString().trim(),
                                        stateEditText.getText().toString().trim(),
                                        commentEditText.getText().toString().trim(),
                                        beginDate.getText().toString().trim(),
                                        endDate.getText().toString().trim());
                                emailDialog.dismiss();
                                TPUtility.hideSoftKeyboard(WriteTicketActivity.this);
                            } else
                                TPUtility.showErrorToast(WriteTicketActivity.this,
                                        "Please provide your comment");
                        });
                AlertDialog alert = builder.create();
                alert.show();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNewHotList(String plate, String stateCode, String comment, String beginDate, String endDate) {
        //DatabaseHelper.getInstance().openWritableDatabase();
        Hotlist hotList = new Hotlist();
        int hotListId = 0;
        try {
            hotListId = Hotlist.getNextPrimaryId();
            String badge = "";
            if (TPApp.getUserInfo() != null)
                badge = TPApp.getUserInfo().getBadge() + "- ";

            if (hotList != null) {
                hotList.setHostlistId(hotListId);
                hotList.setCustId(TPApp.custId);
                hotList.setPlate(plate.toUpperCase(Locale.getDefault()));
                hotList.setStateCode(stateCode.toUpperCase(Locale.getDefault()));
                hotList.setBeginDate(DateUtil.getDateObjectFromString(beginDate));
                hotList.setEndDate(DateUtil.getDateObjectFromString(endDate));
                hotList.setPlateType("ALERT");
                hotList.setComments(badge + comment.toUpperCase(Locale.getDefault()));
                Completable completable = Hotlist.insertHotlist(hotList);
                completable.subscribe();
                //DatabaseHelper.getInstance().insertOrReplace(hotList.getContentValues(), "hotlist");
            }

            boolean result = false;

            if (isServiceAvailable) {
                try {
                    WriteTicketBLProcessor blProcessor = (WriteTicketBLProcessor) screenBLProcessor;
                    // result = blProcessor.getProxy().newHotlist(hotList);
                    result = blProcessor.getProxy().newHotlist(hotList, this);

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
            } else {
                SyncData syncHotListData = new SyncData();
                syncHotListData.setActivity("INSERT");
                syncHotListData.setActivityDate(new Date());
                syncHotListData.setCustId(TPApp.custId);
                syncHotListData.setPrimaryKey(hotList.getHostlistId() + "");
                syncHotListData.setTableName(TPConstant.TABLE_HOTLIST);
                syncHotListData.setStatus("Pending");
                Completable completable = SyncData.insertSyncData(syncHotListData);
                completable.subscribe();
                Toast.makeText(this, "HotList saved successfully.", Toast.LENGTH_LONG).show();
                //DatabaseHelper.getInstance().insertOrReplace(syncHotListData.getContentValues(), "sync_data");
            }

            // This code is changed by mohit 01/03/2023
           /* if (!result) {
                try {
                    Toast.makeText(this, "HotList saved successfully.", Toast.LENGTH_LONG).show();
                } catch (SQLiteException e) {
                    log.error(TPUtility.getPrintStackTrace(e));

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
                // syncDataList.add(syncCommentData);
            } else {
                Toast.makeText(this, "HotList added successfully.", Toast.LENGTH_LONG).show();
            }*/
            // End
            //DatabaseHelper.getInstance().closeWritableDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayHotListValidationMsg() {
        // Check for required Fields
        StringBuffer requiredFields = new StringBuffer();
        if (stateEditText.getText().toString().trim().isEmpty()) {
            requiredFields.append("- State\n");
            stateEditText.setBackgroundResource(R.drawable.text_field_error);
        }
        if (plateNumberEditText.getText().toString().trim().isEmpty()) {
            requiredFields.append("- Plate Number\n");
            plateNumberEditText.setBackgroundResource(R.drawable.text_field_error);
        }
        displayErrorMessage("Please complete required fields below -  \n\n" + requiredFields);
        return;
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
            countDownTimer1 = new CountDownTimer((duration * 60L) * LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, 1000) {
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
            isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
            ((WriteTicketBLProcessor) screenBLProcessor).closeActiveDuty(isServiceAvailable);

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(TPConstant.EXTRA_END_SHIFT, true);
            intent.setClass(WriteTicketActivity.this, HomeActivity.class);
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

        runOnUiThread(() -> {
            // Return if activity is finishing
            if (isFinishing()) {
                return;
            }

            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(WriteTicketActivity.this);
            confirmBuilder.setTitle("Alert").setMessage(message).setCancelable(false)
                    .setNegativeButton("Logout", (dialog, which) -> {
                        endShift();
                        dialog.dismiss();
                    }).setPositiveButton("Continue", (dialog, which) -> {
                        countDownTimer.start();
                        countDownTimer1.cancel();
                        dialog.dismiss();
                    });

            AlertDialog confirmAlert = confirmBuilder.create();
            confirmAlert.show();
        });
    }

    private void endShift() {

        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_lock_power_off).setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    try {
                        // Close Active User Session and Update Duty Report
                        ((WriteTicketBLProcessor) screenBLProcessor).closeActiveDuty(isServiceAvailable);

                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(TPConstant.EXTRA_END_SHIFT, true);
                        intent.setClass(WriteTicketActivity.this, HomeActivity.class);
                        startActivity(intent);
                        TPUtility.createTxtFile();
                        finish();

                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }).setNegativeButton("No", null).show();
    }

    private void showDatePicker(final boolean beginDateRequired) {
        final Calendar cal = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePicker = (view, year, monthOfYear, dayOfMonth) -> {
            pickerDate.setYear(year - 1900);
            pickerDate.setMonth(monthOfYear);
            pickerDate.setDate(dayOfMonth);

            if (beginDateRequired) {
                // beginDate.setText(DateUtil.getBeginEndDate(pickerDate));
            } else {
                endDate.setText(DateUtil.getBeginEndDate(pickerDate));
            }
        };

        if (pickerDate == null) {
            pickerDate = new Date();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(WriteTicketActivity.this, datePicker, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private boolean isEmptyField(EditText text) {
        return text == null || text.getText().toString().trim().isEmpty();
    }

    private double getViolationFine(Violation violation) {

        if (!violation.isRepeatOffender()) {

            return violation.getBaseFine();
        }
        return violation.getBaseFine();
    }

    /*
     * @Return - Returns boolean value - Weather GPS Service of device is ON Or OFF
     * @EditedBy - Bankesh
     * */
    private boolean isGPSRadioON() {
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isEmptyData() {
        boolean isEmptyData = false;

        try {
            if ((Feature.isFeatureAllowed(Feature.STATE_REQUIRED) || Feature.isRequiredField("state"))
                    && activeTicket.getStateCode().equals("")) {
                isEmptyData = true;
            } else if (activeTicket.getPlate().equals("") && activeTicket.getVin().equals("")) {
                isEmptyData = true;
            } else if ((Feature.isFeatureAllowed(Feature.MAKE_REQUIRED) || Feature.isRequiredField("make"))
                    && activeTicket.getMakeTitle().equals("")) {
                isEmptyData = true;
            } else if ((Feature.isFeatureAllowed(Feature.COLOR_REQUIRED) || Feature.isRequiredField("color"))
                    && activeTicket.getColorTitle().equals("")) {

                isEmptyData = true;
            } else if ((Feature.isFeatureAllowed(Feature.BODY_REQUIRED) || Feature.isRequiredField("body"))
                    && activeTicket.getBodyTitle().equals("")) {

                isEmptyData = true;
            } else if ((Feature.isFeatureAllowed(Feature.LOCATION_REQUIRED) || Feature.isRequiredField("location"))
                    && activeTicket.getLocation().equals("")) {

                isEmptyData = true;
            } else if ((Feature.isFeatureAllowed(Feature.METER_REQUIRED) || Feature.isRequiredField("meter"))
                    && activeTicket.getMeter().equals("")) {
                isEmptyData = true;
            } else if ((Feature.isFeatureAllowed(Feature.PERMIT_REQUIRED) || Feature.isRequiredField("permit"))
                    && activeTicket.getPermit().equals("")) {
                isEmptyData = true;
            } else if (activeTicket.getTicketViolations().size() <= 0) {
                isEmptyData = true;
            } else if (activeTicket.getPlate().equalsIgnoreCase("NOPLATE") || activeTicket.getPlate().equalsIgnoreCase("VIN")) {
                isEmptyData = true;
            } else if (activeTicket.getPlate().equals("") && !TPUtility.PlateVINValidate(activeTicket.getVin(), false)) {
                isEmptyData = true;
            } else if (!activeTicket.getPlate().equals("") && !TPUtility.PlateVINValidate(activeTicket.getVin(), true)) {
                isEmptyData = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isEmptyData;
    }

    private void checkAndRequestForMultiSpaceIPS(final String lotDesc) {
        try {
            new Handler().postDelayed(() -> {
                if (usingMultiSpaceIPS) {
                    Intent ipsLotInfoIntent = new Intent();
                    ipsLotInfoIntent.setClass(WriteTicketActivity.this, IPSLotDetails.class);
                    ipsLotInfoIntent.putExtra("spaceGroup", TPApp.IPSSpaceGroup);
                    ipsLotInfoIntent.putExtra(TPConstant.LOT_DESC, lotDesc);
                    startActivityForResult(ipsLotInfoIntent, REQUEST_IPS_MULTISPACE);
                }
                usingMultiSpaceIPS = false;
            }, 700);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkViolationLimits() {
        int maxViolations = 0;
        if (Feature.isFeatureAllowed(Feature.MAX_VIOLATIONS)) {
            String value = Feature.getFeatureValue(Feature.MAX_VIOLATIONS);
            try {
                maxViolations = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                log.error(e.getMessage());
            }
        }
        return maxViolations > 0 && activeTicket.getTicketViolations().size() >= maxViolations;
    }

    void launchPTS(boolean admin) {
        //obtain an Intent to launch ANPR/ALPR Platform Server
        try {
            ptsIntent = new Intent();
            ptsIntent.setComponent(new ComponentName("com.imense.anprPlatformUS", "com.imense.anprPlatformUS.ImenseParkingEnforcer"));
            log.info("com.imense.anprPlatformUS     com.imense.anprPlatformUS.ImenseParkingEnforcer");
            //authenticate the request with the correct invocation code
            if (admin) ptsIntent.putExtra("invocationcode", TPConstant.INVOCATION_ADMIN);
            else ptsIntent.putExtra("invocationcode", TPConstant.INVOCATION_USER);

            //set PT into portrait mode (not recommended since it reduces effective plate pixel resolution)
            if (Feature.isFeatureAllowed(Feature.ALPR_PORTRAIT_ORIENTATION)) {
                ptsIntent.putExtra("orientation", "portrait");
            }
            log.info("Orientation set");
            //optionally instruct PT to start scan (i.e. invoke shutter button) immediately; 0: off; 1: start scan using in-built device camera
            ptsIntent.putExtra("startscan", "1");
            ptsIntent.putExtra("preferences_alertsListRatherThanWhitelist", "false"); //Alerts list rather than Whitelist. Value can be "true" or "false" (default="true")
            ptsIntent.putExtra("returnOnScanTimeout", "1"); //return control to invoking application (with "PT_ANPR_SCANTIMEOUT") on a continuous scan timeout (see also "preferences_scanTimeout" and "startscan")
            ptsIntent.putExtra("preferences_saveimages_path", TPUtility.getALPRImagesFolder()); //Folder for data and images; has to exist and be writable

            ptsIntent.putExtra("preferences_vehiclesfilename", "parkingList.csv"); //Vehicles list file name. Default value="parkingList.csv"
            ptsIntent.putExtra("preferences_savecutouts", "true"); //Save plate cut-out image after every good read. Value can be "true" or "false" (default="true")
            ptsIntent.putExtra("preferences_savecontextimages", "true"); //Save context image to SD card after every good read. Value can be "true" or "false" (default="false")
            ptsIntent.putExtra("preferences_savecontextimagescolour", "true"); //Save context images in colour. Value can be "true" or "false" (default="false")

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
			ptsIntent.putExtra("preferences_savecutouts", "true"); //Save plate cut-out image after every good read. Value can be "true" or "false" (default="true")
			ptsIntent.putExtra("preferences_savecontextimages", "false"); //Save context image to SD card after every good read. Value can be "true" or "false" (default="false")
			ptsIntent.putExtra("preferences_savecontextimagescolour", "false"); //Save context images in colour. Value can be "true" or "false" (default="false")

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
            log.info(licenseKey);
            if (debug > 0) Log.d(tag, "startActivityForResult ptsIntent=" + ptsIntent);
            alprMode = false;
            int REQUESTCODE = 55;
            startActivityForResult(ptsIntent, REQUESTCODE);
            log.info("ALPR launched");
        } catch (Exception err) {
            /**/
            log.error(TPUtility.getPrintStackTrace(err));
            if (debug > 0) {
                Log.e(tag, "launchPTS Error: " + err);
                err.printStackTrace();
            }
            Toast.makeText(WriteTicketActivity.this, "ALPR PlatformServerUS not found: please install it", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void whereIAM(ADLocation data) {

        // String[] split = data.address.split(",");
        //if (false) {
        //
        if (data != null && data.address != null) {
            activeTicket.setLocation(data.address.toUpperCase());
            activeTicket.setLongitude(String.valueOf(data.longi));
            activeTicket.setLatitude(String.valueOf(data.lat));
            activeTicket.setStreetPrefix("");
            activeTicket.setStreetSuffix("");
            activeTicket.setDirection("");
            activeTicket.setGpstime(new Date());
            activeTicket.setIsGPSLocation("Y");
            activeTicket.setStreetNumber(data.streetNumber);

            if (activeTicket != null) {
                locationEditText.setText(TPUtility.getFullAddress(activeTicket));
                ticketTemp.setLocation(TPUtility.getFullAddress(activeTicket));
                if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                    TicketTemp.insertTicket(ticketTemp);
                }
            }

            isGPSLocation = true;
        } else {
            new iOSDialogBuilder(WriteTicketActivity.this)
                    .setSubtitle("GPS is unavailable at the moment. Please try later or select location from list.")
                    .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())

                    .build().show();

            //Toast.makeText(this, "GPS is unavailable at the moment. Please try later or select location from list.", Toast.LENGTH_LONG).show();
        }
    }

    private void findLoc() {
        //new MyTracker(WriteTicketActivity.this, this).track();
        new GetLocation1(WriteTicketActivity.this, this).track();
    }

    @Override
    public void hotListHandler(boolean result) {
        Toast.makeText(this, "HotList added successfully.", Toast.LENGTH_LONG).show();
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
                    WriteTicketActivity.this.onLeftSwipe();
                }
                // Right swipe
                else if (-diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    WriteTicketActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    public class JavaScriptInterfaceWriteTicket {
        Context mContext;
        JSONObject ticketJSON;

        JavaScriptInterfaceWriteTicket(Context c, Ticket ticketInfo) {
            mContext = c;
            ticketJSON = ticketInfo.getJSONObject();
        }

        public String getTicketInfo(String attr) {
            try {
                if (attr.equals("make_code")) {
                    String makeCode = ticketJSON.getString(attr);
                    MakeAndModel make = MakeAndModel.getMakeByCode(makeCode);
                    if (make != null) {
                        return make.getMakeTitle();
                    }

                    return "";
                }

                if (attr.equals("body_code")) {
                    String bodyCode = ticketJSON.getString(attr);
                    Body body = Body.getBodyByCode(bodyCode);
                    if (body != null) {
                        return body.getTitle();
                    }

                    return "";
                }

                if (attr.equals("color_code")) {
                    String colorCode = ticketJSON.getString(attr);
                    Color color = Color.getColorByCode(colorCode);
                    if (color != null) {
                        return color.getTitle();
                    }

                    return "";
                }

                return ticketJSON.getString(attr);

            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            return "";
        }

        public int getTicketsCount() {
            return activeTicket.getTicketViolations().size();
        }

        public String getAgencyCode() {
            if (TPApp.getCustomerInfo() != null) {
                return TPApp.getCustomerInfo().getAgencyCode();
            }

            return "";
        }

        public String getTicketViolation(int index) {
            if (activeTicket.getTicketViolations().size() <= 0) {
                return "";
            }

            return activeTicket.getTicketViolations().get(index).getViolationDesc();
        }

        public String getTicketViolationCode(int index) {
            if (activeTicket.getTicketViolations().size() <= 0)
                return "";

            return activeTicket.getTicketViolations().get(index).getViolationCode();
        }

        public String getTicketComments(int index) {
            if (activeTicket.getTicketViolations().size() <= 0
                    || activeTicket.getTicketViolations().get(index).getTicketComments().size() <= 0)
                return "";

            ArrayList<TicketComment> ticketComments = TPUtility
                    .getPrintableComments(activeTicket.getTicketViolations().get(index).getTicketComments());
            if (ticketComments.size() > 0) {
                return ticketComments.get(0).getComment();
            }

            return "";
        }

        public String getTicketViolationFine(int index) {
            DecimalFormat dec = new DecimalFormat("0.00");
            dec.setRoundingMode(RoundingMode.DOWN);

            if (activeTicket.getTicketViolations().size() <= 0 || index > activeTicket.getTicketViolations().size()
                    || activeTicket.isVoided() || activeTicket.isWarn())
                return "0";

            return dec.format(activeTicket.getTicketViolations().get(index).getFine());
        }

        public String getPrintMacros() {
            StringBuffer str = new StringBuffer();
            if (activeTicket.isVoided())
                str.append(PrintMacro.getPrintMacroMessageByName("VOIDMSG") + "<br>");

            if (activeTicket.isWarn())
                str.append(PrintMacro.getPrintMacroMessageByName("WARNMSG") + "<br>");

            return str.toString();
        }

        public String getFullLocation() {
            return TPUtility.getFullAddress(activeTicket);
        }

        public String getBadge() {
            if (TPApp.getUserInfo() != null)
                return TPApp.getUserInfo().getBadge();

            return "";
        }

        public String getPhotosString() {
            if (activeTicket.getTicketPictures().size() > 0)
                return "PHOTOS(S) TAKEN";
            else
                return "";
        }

        public String getPhotoImgTag() {
            for (TicketPicture pic : activeTicket.getTicketPictures()) {
                if (pic.getMarkPrint().equals("Y")) {
                    return "<img src='file://" + pic.getImagePath() + "'/>";
                }
            }
            return "";
        }

    }

    private void updateRepeatOffendersCount(RepeatOffenderParams repeatOffender) throws JSONException {
        ApiRequest apiRequest = ServiceGenerator.createRxService(ApiRequest.class);
        //  RepeatOffender_Rpc requestPOJO = new RepeatOffender_Rpc();
        RepeatOffender_Rpc requestPOJO = new RepeatOffender_Rpc();
        requestPOJO.setMethod("updateRepeatOffendersCount");
        // params.setRepeatOffendersobject(repeatOffender);
        requestPOJO.setRepeatOffenderParams(repeatOffender);
        System.out.println("RequestObj**" + new Gson().toJson(requestPOJO));
        apiRequest.updateRepeatOffendersCount(requestPOJO).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    if (response.isSuccessful()) {
                       /* Intent serviceIntent = new Intent(WriteTicketActivity.this, JobIntentServiceSaveTicket.class);
                        JobIntentServiceSaveTicket.enqueueWork(WriteTicketActivity.this, serviceIntent);*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("", "Function name is updateRepeatOffendersCount() " + t.getMessage());
                log.error(TPUtility.getPrintStackTrace(t));
            }
        });
    }

    private void showAlertDialog() {
        new iOSDialogBuilder(WriteTicketActivity.this)
                .setSubtitle("Invalid violation. Please select another from list.")
                .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())

                .build().show();
        enablePrint();
    }

    private void plcardsendrequestAlertDialog() {
        new iOSDialogBuilder(WriteTicketActivity.this)
                .setTitle("Alert")
                .setSubtitle("Your request has been submitted, display result shortly")
                .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())

                .build().show();

    }


    public class PhoneCustomStateListener extends PhoneStateListener {

        public int signalSupport = 0;

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            signalSupport = signalStrength.getGsmSignalStrength();
            log.info("------ gsm signal --> " + signalSupport);

            if (signalSupport > 30) {
                log.info("Signal GSM : Good");


            } else if (signalSupport > 20 && signalSupport < 30) {
                log.info("Signal GSM : Avarage");


            } else if (signalSupport < 20 && signalSupport > 3) {
                log.info("Signal GSM : Weak");


            } else if (signalSupport < 3) {
                log.info("Signal GSM : Very weak");


            }
            //stopListen();
        }
    }

    public void startListen() {
        handlerThreadCellularSignal = new HandlerThread("CELLULAR_INFO_THREAD");
        handlerThreadCellularSignal.start();
        Looper looper = handlerThreadCellularSignal.getLooper();
        Handler handler = new Handler(looper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                psListener = new PhoneCustomStateListener();
                TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            }
        });
    }

    private void stopListen() {
        handlerThreadCellularSignal.quit();
        psListener = null;
    }

    private void displayParkingInfoMsg(final ParkMobileSpaceData parking) {
        parking.getExpireInfo();
        try {
            StringBuffer message = new StringBuffer();
            StringBuffer values = new StringBuffer();


            message.append("Name" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getProductDescription()) + "\n");
            message.append("Space Number" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getSpaceNumber()) + "\n");


            message.append("Zone" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getCustomField1()) + "-" + StringUtil.getDisplayString(parking.getSignageZoneCode()) + "\n");


            message.append("Plate" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getLpn()) + "\n");


            message.append("Permit" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getPermit()) + "\n");


            message.append("State" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getLpnState()) + "\n");


            message.append("Payed Mins" + "\n");
            values.append(": " + StringUtil.getDisplayString("" + parking.getPayedMinutes()) + "\n");


            message.append("Expire" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getExpireInfo().getExpireMsg()) + "\n");

            message.append("Start Date" + "\n");
            values.append(": " + StringUtil.getDisplayString(DateUtil.getStringFromDate2(parking.getStartDateLocal1())) + "\n");

            message.append("End Date" + "\n");
            values.append(": " + StringUtil.getDisplayString(DateUtil.getStringFromDate2(parking.getEndDateLocal1())) + "\n");


			/*WebView wv = new WebView (getBaseContext());
			wv.loadData(message.toString(), "text/html", "utf-8");
			wv.setBackgroundColor(android.graphics.Color.BLACK);
			wv.getSettings().setDefaultTextEncodingName("utf-8");*/

            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = (TextView) view.findViewById(R.id.headerTV);
            TextView valueTV = (TextView) view.findViewById(R.id.valueTV);

            headerTV.setText(message.toString());
            valueTV.setText(values.toString());

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setView(view);
            dialog.setTitle("Parking Space Info: " + parking.getSpaceNumber());
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            if (parking.getExpireInfo().isExpired()) {
                dialog.setNegativeButton("Write Ticket", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Ticket ticket = TPApp.createNewTicket();
                        ticket.setPlate(parking.getLpn());
                        ticket.setPermit(parking.getPermit());
                        ticket.setStateCode(parking.getLpnState());
                        ticket.setTimeMarked(parking.getEndDateLocal1());

                        TPApp.setActiveTicket(ticket);

                        Intent intent = new Intent();
                        intent.setClass(WriteTicketActivity.this, WriteTicketActivity.class);
                        intent.putExtra("PARKMOBILE", true);
                        startActivity(intent);
                    }
                });
            }

            dialog.show();

        } catch (Exception e) {
            Log.e("TicketPRO", "Passport Zone Info " + e.getMessage());
        }
    }

    void getLocationFromGps() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            // Permission already granted, get location
            getLastLocation();
        }
    }

    private void getLastLocation() {
        // Check if permissions are granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permissions granted, get last known location
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                // Get last known location
                                Location lastLocation = task.getResult();
                                double latitude = lastLocation.getLatitude();
                                double longitude = lastLocation.getLongitude();

                                __getGpsLocationFromApi(latitude, longitude);

                            } else {
                                // Unable to get location
                                Toast.makeText(WriteTicketActivity.this, "Unable to get location",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Permissions not granted
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void __getGpsLocationFromApi(double latitude, double longitude) {

        progressDialog = ProgressDialog.show(WriteTicketActivity.this, "", "Loading...");
        Location_Json_rpc locationJsonRpc = new Location_Json_rpc();
        LocationPram locationPram = new LocationPram();

        locationPram.setLat(String.valueOf(latitude));
        locationPram.setLon(String.valueOf(longitude));

        locationJsonRpc.setId("82F85DB43CBF6");
        locationJsonRpc.setJsonrpc("2.0");
        locationJsonRpc.setParams(locationPram);
        locationJsonRpc.setMethod("getGPSLocation");

        ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
        api.getGpsLocation(locationJsonRpc).enqueue(new Callback<GpsResponse>() {
            @Override
            public void onResponse(Call<GpsResponse> call, Response<GpsResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.code() == 200) {
                    GpsResult result = response.body().getResult();
                    if (result.getLocation() != null) {
                       /* new iOSDialogBuilder(WriteTicketActivity.this)
                                .setTitle("Location")
                                .setSubtitle(response.body().getResult().getLocation())
                                .setPositiveListener(getString(R.string._ok), dialog -> dialog.dismiss())
                                .build().show();*/

                        String location = result.getLocation();
                        try {
                            if (location != null && !location.isEmpty()) {
                                String s = "";
                                String[] split = location.split(",");
                                if (startsWithNumeric(split[0])) {
                                    s = removeBeforeFirstSpace(split[0]);
                                    activeTicket.setLocation(s.toUpperCase());
                                } else {
                                    activeTicket.setLocation(split[0].toUpperCase());
                                }
                                activeTicket.setLongitude(String.valueOf(longitude));
                                activeTicket.setLatitude(String.valueOf(latitude));
                                activeTicket.setStreetPrefix("");
                                activeTicket.setStreetSuffix("");
                                activeTicket.setDirection("");
                                activeTicket.setGpstime(new Date());
                                activeTicket.setIsGPSLocation("Y");
                                activeTicket.setStreetNumber(result.getStreetNumber());

                                if (activeTicket != null) {
                                    locationEditText.setText(TPUtility.getFullAddress(activeTicket));
                                    ticketTemp.setLocation(TPUtility.getFullAddress(activeTicket));
                                    if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
                                        TicketTemp.insertTicket(ticketTemp);
                                    }
                                }

                                isGPSLocation = true;
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GpsResponse> call, Throwable t) {
                progressDialog.dismiss();
                log.error(t.getMessage());
            }
        });
    }

    public static boolean startsWithNumeric(String input) {
        // Check if the first character is a digit
        return Character.isDigit(input.charAt(0));
    }

    public static String removeBeforeFirstSpace(String input) {
        int index = input.indexOf(' '); // Find the index of the first space
        if (index != -1) {
            return input.substring(index + 1); // Return the substring starting from the first space (inclusive)
        } else {
            return input; // If no space is found, return the original string
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class ItemAdapter extends ArrayAdapter<CubTracZone> {
        private Context context;
        private List<CubTracZone> items;

        public ItemAdapter(Context context, List<CubTracZone> items) {
            super(context, 0, items);
            this.context = context;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            CubTracZone item = items.get(position);

            TextView nameTextView = convertView.findViewById(android.R.id.text1);
            TextView descriptionTextView = convertView.findViewById(android.R.id.text2);

            nameTextView.setText(item.getName());
            nameTextView.setTextColor(R.color.black);
            descriptionTextView.setText(item.getAssignedId());
            descriptionTextView.setTextColor(R.color.black);


            return convertView;
        }
    }
}

