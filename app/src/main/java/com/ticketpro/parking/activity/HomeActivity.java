package com.ticketpro.parking.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.exception.TPException;
import com.ticketpro.logger.LoggerConfigurator;
import com.ticketpro.model.CustomerInfo;
import com.ticketpro.model.DeviceFeatures;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.Feature;
import com.ticketpro.model.ParamsDeviceFeatures;
import com.ticketpro.model.RequestPOJODeviceFeatures;
import com.ticketpro.model.ServiceResult;
import com.ticketpro.model.SystemBackup;
import com.ticketpro.model.User;
import com.ticketpro.model.devicefeature.ResponseResult;
import com.ticketpro.model.devicefeature.Result;
import com.ticketpro.parking.R;
import com.ticketpro.parking.api.WriteTicketNetworkCalls;
import com.ticketpro.parking.bl.HomeBLProcessor;
import com.ticketpro.parking.dao.DatabaseHelper;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.parking.service.CleanupReceiver;
import com.ticketpro.parking.service.DeviceDataWorker;
import com.ticketpro.parking.service.RepeatOffenderReceiver;
import com.ticketpro.parking.service.ServicePhotoPurge;
import com.ticketpro.parking.service.TPSyncAdapter;
import com.ticketpro.parking.service.VersionUpdater;
import com.ticketpro.print.TicketPrinter;
import com.ticketpro.print.model.PrintServiceInfo;
import com.ticketpro.syncbackup.syncactivity.NewSyncBackup;
import com.ticketpro.util.CallbackHandler;
import com.ticketpro.util.DataUtility;
import com.ticketpro.util.DownloadFilesTask;
import com.ticketpro.util.GCMUtilities;
import com.ticketpro.util.LocationService;
import com.ticketpro.util.PowerConnectionReceiver;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPROw
 * @version : 1.0
 * @since : 1.0
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class HomeActivity extends BaseActivityImpl {
    final int ERROR_DATABASE = 1;
    final int ERROR_NETWORK = 2;
    final int ERROR_NOT_REGISTERED = 3;
    final int ERROR_SERVICE = 4;

    final int DATABASE_ERROR = 1;
    final int DATABASE_SUCCESSFUL = 2;
    final int DATABASE_SYNC_ERROR = 3;
    final int DATABASE_DEVICE_REGISTERED = 4;
    final int DATABASE_DEVICE_REGISTRATION_FAILED = 5;
    final int DATABASE_DEVICE_NOT_ACTIVE = 6;

    final int SYNC_ERROR = 1;
    final int SYNC_SUCCESSFUL = 2;
    boolean isLoggingEnabled = false;
    Object handleSyncObserver;
    private WeakReference<HomeActivity> activityReference;

    SyncStatusObserver syncObserver = new SyncStatusObserver() {
        @Override
        public void onStatusChanged(final int which) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    };

    String versionString;
    private Button loginBtn;
    private TextView lastSynchTimeLabel;
    private TextView deviceIdText;
    private TextView uniqueNoLabel;
    private TextView appVersionTextView;
    private ProgressDialog progressDialog;
    private Handler synchHandler;
    private Handler errorHandler;
    private Handler dbConfigureHandler;
    private SharedPreferences mPreferences;
    private ImageView logoImageView;
    private AsyncTask<Void, Void, Void> mRegisterTask;
    private MenuItem loggingMenu;
    private SharedPreferences logPreferences;
    private Intent intent;

    private boolean isCharging;
    private PowerConnectionReceiver powerReceiver;
    private long time;
    private Preference preference;
    private boolean syncResult;

    public static String formatDateToString(Date date, String format, String timeZone) {
        // null check
        if (date == null) return null;
        // create SimpleDateFormat object with input format
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // default system timezone if passed null or empty
        if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
            timeZone = Calendar.getInstance().getTimeZone().getID();
        }
        // set timezone to SimpleDateFormat
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        // return Date in required format with timezone as String
        return sdf.format(date);
    }

    public static String convertToDate() {
        String newTime = "";
        try {
            String myTime = "2018-10-10 02:45:03";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            Date d = df.parse(myTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, 480);
            newTime = df.format(cal.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newTime;
    }

    /**
     * Entry point of the Activity
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.home);//getDate();
            preference = Preference.getInstance(this);
            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            setLogger(HomeActivity.class.getName());
            setBLProcessor(new HomeBLProcessor());
            isNetworkInfoRequired = true;
            versionString = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            /*TicketCleanupReceiver ticketCleanupReceiver = new TicketCleanupReceiver();
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(ticketCleanupReceiver, filter);*/

            loginBtn = findViewById(R.id.home_login_btn);
            loginBtn.setEnabled(false);
            loginBtn.setBackgroundResource(R.drawable.btn_disabled);

            lastSynchTimeLabel = findViewById(R.id.home_synchronize_time);
            deviceIdText = findViewById(R.id.home_device_id);
            uniqueNoLabel = findViewById(R.id.home_unique_no);
            appVersionTextView = findViewById(R.id.home_version);
            logoImageView = findViewById(R.id.home_customer_logo_imageview);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        new LoggerConfigurator().configLogger(versionString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);

            dbConfigureHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    try {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    SharedPreferences.Editor editor = mPreferences.edit();
                    switch (msg.what) {
                        case DATABASE_SUCCESSFUL:
                            loginBtn.setEnabled(true);
                            loginBtn.setBackgroundResource(R.drawable.btn_yellow);
                            editor.putInt(TPConstant.PREFS_KEY_RESTORE_CUSTID, TPApp.custId);
                            editor.putInt(TPConstant.PREFS_KEY_RESTORE_DEVICEID, TPApp.deviceId);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Initialized Database Successfully", Toast.LENGTH_SHORT).show();
                            // Date lastSync = TPApplication.getInstance().getDeviceInfo().getLastSync();
                            // System.out.println("LAST SYNC: " +lastSync);
                            // lastSynchTimeLabel.setText(mPreferences.getString(TPConstant.PREFS_KEY_DBSYNCTIME, new SimpleDateFormat("MMM dd, yyyy HH:mm:ss").format(new Date())));
                            log.info("Initialized Database Successfully");
                            if (!preference.getBoolean("DEVICE_FEATURE")) {
                                saveDeviceFeaturesDetails();
                            }
                            try {
                                registerDeleteBroadcast();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // Update Sync Date
                            try {
                                DeviceInfo device = DeviceInfo.getDeviceInfo(getDeviceId());

                                if (!StringUtil.isEmpty(versionString)) {
                                    device.setAppVersion(versionString);
                                }
                                Log.i("TAG", "updateDeviceInfo: " + versionString);
                                device.setPlatform(Build.MANUFACTURER + " " + Build.MODEL);
                                device.setOsVersion(Build.VERSION.RELEASE);
                                DeviceInfo.insertDeviceInfo(device);
                                TPApp.setDeviceInfo(device);
                                // Sync Device Information
                                ArrayList<DeviceInfo> devices = DeviceInfo.getDevices();
                                if (isNetworkConnected()) {
                                    WriteTicketNetworkCalls.syncDevices(devices, "HomeActivity");
                                }

                                // ((HomeBLProcessor) screenBLProcessor).updateDeviceInfo(HomeActivity.this);
                                Date lastSync = TPApplication.getInstance().getDeviceInfo().getLastSync();
                                System.out.println("LAST SYNC:" + lastSync);
                                lastSynchTimeLabel.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm:ss").format(lastSync));
                            } catch (Exception e) {
                                log.error(TPUtility.getPrintStackTrace(e));
                            }

                            // Configure External Storage
                            try {
                                if (Feature.isSystemFeatureAllowed(Feature.USE_EXTERNAL_STORAGE)) {
                                    TPApp.useExtStorage = true;
                                    TPApp.externalStoragePath = TPUtility.getExternalStorage();
                                } else {
                                    TPApp.useExtStorage = false;
                                    TPApp.externalStoragePath = "";
                                }
                            } catch (Exception e) {
                                log.error(TPUtility.getPrintStackTrace(e));
                            }

                            // Check Network Signal
                            try {
                                TPApp.checkNetworkSignal = Feature.isSystemFeatureAllowed(Feature.CHECK_NETWORK_SIGNAL);
                            } catch (Exception e) {
                                log.error(TPUtility.getPrintStackTrace(e));
                            }

                            // Apply default printer settings
                            try {
                                updateDefaultPrinter();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            break;

                        case DATABASE_ERROR:
                            log.info("Failed Database Initialization.");
                            try {
                                registerDeleteBroadcast();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            editor.putBoolean(TPConstant.PREFS_KEY_DBCONFIGURATION, false);
                            Toast.makeText(getApplicationContext(), "Synchronization Failed. Please retry and If the problem persists please contact system administrator.", Toast.LENGTH_LONG).show();
                            break;

                        case DATABASE_SYNC_ERROR:
                            log.info("Failed Sync Database.");
                            try {
                                registerDeleteBroadcast();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "Data synchronization failed.", Toast.LENGTH_LONG).show();
                            break;

                        case DATABASE_DEVICE_REGISTERED:
                            log.info("Device registered successfully but not activated.");
                            try {
                                registerDeleteBroadcast();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "Device registered successfully but not activated. Please contact your administrator.", Toast.LENGTH_LONG).show();
                            break;

                        case DATABASE_DEVICE_REGISTRATION_FAILED:
                            log.info("Device registration failed. Please contact your administrator");
                            try {
                                registerDeleteBroadcast();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "Device registration failed. Please contact your administrator.", Toast.LENGTH_LONG).show();
                            break;

                        case DATABASE_DEVICE_NOT_ACTIVE:
                            log.info("Device is registered but not activated.");
                            try {
                                registerDeleteBroadcast();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "Device is registered but not activated. Please contact your administrator.", Toast.LENGTH_LONG).show();
                            break;
                    }

                    editor.apply();
                }
            };

            synchHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (progressDialog != null && progressDialog.isShowing()) {
                        try {
                            progressDialog.dismiss();
                        } catch (IllegalArgumentException e) {
                            log.error("Dialog was not attached to the window manager: " + e.getMessage());
                        }
                    }

                    loginBtn.setEnabled(true);
                    loginBtn.setBackgroundResource(R.drawable.btn_yellow);

                    switch (msg.what) {
                        case SYNC_SUCCESSFUL:
                            TPApp.dbConfigured = true;
                            Toast.makeText(HomeActivity.this, "Synchronized Database Successfully", Toast.LENGTH_SHORT).show();

                            saveDeviceFeaturesDetails();
                            if (mPreferences != null) {
                                SharedPreferences.Editor editor = mPreferences.edit();
                                editor.putString(TPConstant.PREFS_KEY_DBSYNCTIME, new SimpleDateFormat("MMM dd, yyyy HH:mm:ss").format(new Date()));
                                editor.commit();
                                Date lastSync = TPApplication.getInstance().getDeviceInfo().getLastSync();
                                System.out.println("LAST SYNC: " + lastSync);

                                lastSynchTimeLabel.setText(mPreferences.getString(TPConstant.PREFS_KEY_DBSYNCTIME, "Not Synchronized"));
                            }

                            try {
                                DeviceInfo device = DeviceInfo.getDeviceInfo(getDeviceId());
                                if (!StringUtil.isEmpty(versionString)) {
                                    device.setAppVersion(versionString);
                                }

                                device.setPlatform(Build.MANUFACTURER + " " + Build.MODEL);
                                device.setOsVersion(Build.VERSION.RELEASE);
                                device.setLastSync(new Date());
                                DeviceInfo.insertDeviceInfo(device);

                                TPApp.setDeviceInfo(device);
                            } catch (Exception e) {
                                log.error(TPUtility.getPrintStackTrace(e));
                            }

                            updateDefaultPrinter();
                            downloadCustomerLogo();
                            break;

                        case SYNC_ERROR:
                            Toast.makeText(HomeActivity.this, "Synchronization Failed. Please retry and if the problem persists please contact system administrator.", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            };


            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    switch (msg.what) {
                        case ERROR_NOT_REGISTERED:
                            displayErrorMessage("This device is not registered with TicketPRO System. Please contact your administrator.");
                            break;

                        case ERROR_NETWORK:
                        case ERROR_SERVICE:
                            Toast.makeText(getApplicationContext(), "Network Service Error. Please try again.", Toast.LENGTH_LONG).show();
                            break;

                        case ERROR_DATABASE:
                            SharedPreferences.Editor editor = mPreferences.edit();
                            editor.putBoolean(TPConstant.PREFS_KEY_DBCONFIGURATION, false);
                            editor.apply();

                            Toast.makeText(getApplicationContext(), "Failed Database Initialization. Please try again.", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            };

            TPApp.deviceName = getDeviceId();

            // Update version number
            if (TPConstant.IS_DEVELOPMENT_BUILD) {
                appVersionTextView.setText("v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "(D)");
            } else if (TPConstant.IS_STAGING_BUILD) {
                appVersionTextView.setText("v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "(S)");
            } else {
                appVersionTextView.setText("v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

            }

            // Configure database and GCM registration if not end shift request
            Intent data = getIntent();

            if (data.hasExtra(TPConstant.EXTRA_END_SHIFT)) {
                loginBtn.setEnabled(true);
                loginBtn.setBackgroundResource(R.drawable.btn_yellow);
            }

            addShortcut(getApplicationContext());

            initLogDebugging();

            if (!data.hasExtra(TPConstant.EXTRA_END_SHIFT) && !data.hasExtra(TPConstant.EXTRA_RESTORE_SESSION)) {
                checkVersionUpdates();
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        //updateRequiredTimeZone1("16/03/2018 19:12:40");

        /*intent = new Intent(HomeActivity.this, LocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }*/
    }

    public String updateRequiredTimeZone1(String utcString) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = format.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertedDateTime(date);

    }

    public String convertedDateTime(Date date) {
        return formatDateToString(date, "MM/dd/yyyy hh:mm:ss", null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkVersionUpdates() {
        if (isServiceAvailable) {
            //TPApp.updateCheckInProgress = true;

            VersionUpdater updater = VersionUpdater.getInstance();
            updater.setActivity(this);
            updater.setServiceProxy(screenBLProcessor.getProxy());
            updater.checkForUpdate(new CallbackHandler() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void success(String data) {
                    processDbInitialization();
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void failure(String message) {
                    processDbInitialization();
                }
            });
        } else {
            processDbInitialization();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void processDbInitialization() {
        TPApp.updateCheckInProgress = false;
        try {
            runOnUiThread(() -> {
                try {
                    configureDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bindDataAtLoadingTime();
                registerGCMBackground();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        if (handleSyncObserver != null) {
            ContentResolver.removeStatusChangeListener(handleSyncObserver);
        }

        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();
        try {
            DeviceInfo device = DeviceInfo.getDeviceInfo(getDeviceId());
            if (device!=null) {
                if (versionString != null && !StringUtil.isEmpty(versionString)) {
                    device.setAppVersion(versionString);
                }
                device.setPlatform(Build.MANUFACTURER + " " + Build.MODEL);
                device.setOsVersion(Build.VERSION.RELEASE);
                DeviceInfo.insertDeviceInfo(device);
                ArrayList<DeviceInfo> devices = DeviceInfo.getDevices();
                if (isNetworkConnected()) {
                    WriteTicketNetworkCalls.syncDevices(devices, "HomeActivity");
                }
            }
           /* if (isNetworkConnected()) {
                Intent serviceIntent = new Intent(HomeActivity.this, JobIntentServiceSaveTicket.class);
                JobIntentServiceSaveTicket.enqueueWork(HomeActivity.this, serviceIntent);
            }*/

            //((HomeBLProcessor) screenBLProcessor).updateDeviceInfo(HomeActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        handleSyncObserver = ContentResolver.addStatusChangeListener(ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE | ContentResolver.SYNC_OBSERVER_TYPE_PENDING, syncObserver);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(TPConstant.PREFS_KEY_IS_LOGGED_IN, false);
        editor.apply();

        TPApp.enableChalkAlerts = mPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_CHALKALERTS, true);
        TPApp.enableMobileNow = mPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_MOBILENOW, true);
        TPApp.enablePassportParking = mPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_PASSPORT_PARKING, true);
        TPApp.enableVinPassportParking = mPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_VIN_PASSPORT_PARKING, false);
        TPApp.enableParkMobile = mPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_PARK_MOBILE, true);
        TPApp.enablePayByPhone = mPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_PAY_BY_PHONE, true);
        TPApp.enableProgressive = mPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_PROGRESSIVE, true);
        TPApp.enableDPT = mPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_DPT, true);
        TPApp.enableCale = mPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_CALE, true);
        //TPApp.enableCale = mPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_OFFSTEER, true);
        TPApp.enableParkeon = mPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_PARKEON, true);
        try {
            TPApp.enableIPS = mPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_IPS, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TPApp.voiceMode = false;

        stopListening();

        Intent data = getIntent();
        if (data.hasExtra(TPConstant.EXTRA_RESTORE_SESSION)) {
            DatabaseHelper.init(this);

            TPApp.dbConfigured = true;
            loginBtn.setEnabled(true);
            loginBtn.setBackgroundResource(R.drawable.btn_yellow);
            log.info("Session Restored Successfully.");

            TPApp.setUserInfo(null);

            try {
                // Update device info
                DeviceInfo deviceInfo = DeviceInfo.getDeviceInfo(getDeviceId());
                TPApp.setDeviceInfo(deviceInfo);
                // Update Customer Info
                CustomerInfo customerInfo = CustomerInfo.getCustomerInfo(deviceInfo.getCustId());
                TPApp.setCustomerInfo(customerInfo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            powerReceiver = new PowerConnectionReceiver();
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            registerReceiver(powerReceiver, ifilter);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(LocationService.LOCATION_ACTION);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (data.hasExtra(TPConstant.EXTRA_END_SHIFT) || data.hasExtra(TPConstant.EXTRA_RESTORE_SESSION)) {
            //lastSynchTimeLabel.setText(mPreferences.getString(TPConstant.PREFS_KEY_DBSYNCTIME, "Not Synchronized"));

           /* try {
                DeviceInfo deviceInfo = DeviceInfo.getDeviceInfo(getDeviceId());
                Date lastSync = deviceInfo.getLastSync();
                lastSynchTimeLabel.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm:ss").format(lastSync));

            } catch (TPException e) {
                e.printStackTrace();
            }*/
            try {
                Date lastSync = TPApplication.getInstance().getDeviceInfo().getLastSync();
                System.out.println("LAST SYNC:" + lastSync);
                lastSynchTimeLabel.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm:ss").format(lastSync));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (TPApp.dbConfigured) {
            // Update Customer Logo
            CustomerInfo customerInfo = TPApp.getCustomerInfo();
            if (customerInfo != null) {
                File file = new File(TPUtility.getDataFolder() + customerInfo.getLogoImage());

                if (file.exists()) {
                    logoImageView.setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
                }
            }
            try {
                updateDefaultPrinter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        photoPurge();
        uniqueNoLabel.setText(getDeviceId());
        updateLogDebuggingMenu();
        TPApp.resetUserSession();
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDestroy();
        unregisterReceiver(powerReceiver);
        //stopService(intent);
    }

    private void updateDefaultPrinter() {
        try {
            Log.i("TAG N5 Printer", "N5==" + TPUtility.isN5ServiceAvailable(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(() -> {
            DeviceInfo deviceInfo = TPApp.getDeviceInfo();
            String printerType = mPreferences.getString(TicketPrinter.APPLICATION_COMM_METHOD_NAME_KEY, null);
            SharedPreferences.Editor editor = mPreferences.edit();

            if (printerType != null && deviceInfo != null && deviceInfo.getDefaultPrinterName() != null
                    && printerType.equalsIgnoreCase(TicketPrinter.COMMUNICATION_METHOD_TSC_BLUETOOTH)) {
                String configuredPrinter = mPreferences.getString(TicketPrinter.PRINTER_BLUETOOTH_DEVICE_NAME_KEY, null);
                if (configuredPrinter == null || configuredPrinter.equals("")) {
                    editor.putString(TicketPrinter.PRINTER_BLUETOOTH_DEVICE_NAME_KEY, deviceInfo.getDefaultPrinterName());
                    editor.putString(TicketPrinter.APPLICATION_COMM_METHOD_NAME_KEY, TicketPrinter.COMMUNICATION_METHOD_TSC_BLUETOOTH);
                    editor.apply();
                }
            } else if (printerType == null && TPUtility.isN5ServiceAvailable(HomeActivity.this)) {
                editor.putString(TicketPrinter.PRINTER_SERVICE_NAME_KEY, new PrintServiceInfo("N5").getDeviceName());
                editor.putString(TicketPrinter.APPLICATION_COMM_METHOD_NAME_KEY, TicketPrinter.COMMUNICATION_METHOD_PRINTSERVICE);
                editor.apply();
            } else if (!TPUtility.isN5ServiceAvailable(HomeActivity.this)) {
                String configuredPrinter = mPreferences.getString(TicketPrinter.PRINTER_BLUETOOTH_DEVICE_NAME_KEY, null);
                if (configuredPrinter == null || configuredPrinter.equals("")) {
                    if (deviceInfo != null) {
                        editor.putString(TicketPrinter.PRINTER_BLUETOOTH_DEVICE_NAME_KEY, deviceInfo.getDefaultPrinterName());
                    }
                    editor.putString(TicketPrinter.APPLICATION_COMM_METHOD_NAME_KEY, TicketPrinter.COMMUNICATION_METHOD_BLUETOOTH);
                    editor.apply();
                }
            }

            if (Feature.isSystemFeatureAllowed(Feature.SHOW_PRINT_DIALOG) && !mPreferences.contains(TPConstant.PREFS_KEY_SHOW_PRINTDIALOG)) {
                TPApp.showPrintDialog = true;
                editor.putBoolean(TPConstant.PREFS_KEY_SHOW_PRINTDIALOG, TPApp.showPrintDialog);
                editor.apply();
            }

            // Update device id
            try {
                if (deviceInfo != null && deviceInfo.getDevice() != null && deviceIdText != null) {
                    deviceIdText.setText("DeviceId: " + deviceInfo.getDeviceId() + " - " + deviceInfo.getDevice());
                } else {
                    deviceIdText.setText("DeviceId: " + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 500);
    }
    /*
	private void updateDefaultPrinter() {
		Log.i("TAG N5 Printer","N5=="+TPUtility.isN5ServiceAvailable(this));
		//Toast.makeText(getApplicationContext(),""+TPUtility.isN5ServiceAvailable(this),Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {


				DeviceInfo deviceInfo = TPApp.getDeviceInfo();
		String printerType = mPreferences.getString(TicketPrinter.APPLICATION_COMM_METHOD_NAME_KEY, null);
		SharedPreferences.Editor editor = mPreferences.edit();

		 if (deviceInfo != null && deviceInfo.getDefaultPrinterName() != null) {
			   if (printerType != null && printerType.equalsIgnoreCase(TicketPrinter.COMMUNICATION_METHOD_BLUETOOTH)) {
				String configuredPrinter = mPreferences.getString(TicketPrinter.PRINTER_BLUETOOTH_DEVICE_NAME_KEY, null);
				if (configuredPrinter == null || configuredPrinter.equals("")) {
					editor.putString(TicketPrinter.PRINTER_BLUETOOTH_DEVICE_NAME_KEY, deviceInfo.getDefaultPrinterName());
					editor.putString(TicketPrinter.APPLICATION_COMM_METHOD_NAME_KEY, TicketPrinter.COMMUNICATION_METHOD_BLUETOOTH);
					editor.commit();
				}
			}
		}
		if (printerType == null && TPUtility.isN5ServiceAvailable(HomeActivity.this)) {
				editor.putString(TicketPrinter.PRINTER_SERVICE_NAME_KEY, new PrintServiceInfo("N5").getDeviceName());
				editor.putString(TicketPrinter.APPLICATION_COMM_METHOD_NAME_KEY, TicketPrinter.COMMUNICATION_METHOD_PRINTSERVICE);
				editor.commit();
			}
		else if (deviceInfo != null && (printerType ==null  && !TPUtility.isN5ServiceAvailable(HomeActivity.this))) {
				String configuredPrinter = mPreferences.getString(TicketPrinter.PRINTER_BLUETOOTH_DEVICE_NAME_KEY, null);
				if (configuredPrinter == null || configuredPrinter.equals("")) {
					editor.putString(TicketPrinter.PRINTER_BLUETOOTH_DEVICE_NAME_KEY, deviceInfo.getDefaultPrinterName());
					editor.putString(TicketPrinter.APPLICATION_COMM_METHOD_NAME_KEY, TicketPrinter.COMMUNICATION_METHOD_BLUETOOTH);
					editor.commit();
				}
			}

		if (Feature.isSystemFeatureAllowed(Feature.SHOW_PRINT_DIALOG) && mPreferences.contains(TPConstant.PREFS_KEY_SHOW_PRINTDIALOG) == false) {
			TPApp.showPrintDialog = true;
			editor.putBoolean(TPConstant.PREFS_KEY_SHOW_PRINTDIALOG, TPApp.showPrintDialog);
			editor.commit();
		}

		// Update device id
		if (deviceInfo != null && deviceIdText != null) {
			deviceIdText.setText("DeviceId - " + deviceInfo.getDeviceId());
		}}
		},500);
	}*/

    private void addShortcut(Context context) {
        boolean wasInstalled = mPreferences.getBoolean(TPConstant.PREFS_KEY_APP_SHORTCUT, false);
        if (wasInstalled) {
            return;
        }

        // Remove any previous shortcuts
        removeShortcut(context);

        Intent shortcutIntent = new Intent(context, SplashActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "TicketPRO");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.icon));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(TPConstant.PREFS_KEY_APP_SHORTCUT, true);
        editor.apply();
    }

    private void removeShortcut(Context context) {
        Intent shortcutIntent = new Intent(context, SplashActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "TicketPRO");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.icon));
        addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void registerGCMBackground() {
        try {
            if (isNetworkConnected()) {
                FirebaseApp.initializeApp(this);
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    String newToken = task.getResult();
                    Log.e("newToken", newToken);
                    try {
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putString(TPConstant.PUSH_TOKEN, newToken);
                        editor.apply();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    GCMUtilities.register(HomeActivity.this, newToken);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void configureDB() throws IOException {

        if (!isFinishing() && !isDestroyed()) {
            progressDialog = ProgressDialog.show(HomeActivity.this, "", "Initializing Database...");

        }

         // Get Device ID
        final String deviceName = getDeviceId();

       /* String deviceNmae = TPUtility.readExistingDeviceName();
       /* if (!deviceNmae.isEmpty() && !deviceNmae.equals(deviceName)) {
            if (isNetworkConnected()) {
                if (NetworkCalls.checkForExistingRecord(deviceNmae, deviceName)) {
                    new File(TPUtility.getDataFolder() + "deviceName.txt").delete();
                }
            }
        }*/

        // Update device ID
        uniqueNoLabel.setText(deviceName);

        // Initialize Database Handler
        DatabaseHelper.init(this);

        log.info("Database configuration status=" + TPApp.dbConfigured);

        // Configure Database
        final boolean dbConfigured = DatabaseHelper.getInstance().dbExists();
        if (!dbConfigured) {
            try {
                DatabaseHelper.getInstance().createDatabase();
            } catch (Exception e) {
                errorHandler.sendEmptyMessage(ERROR_DATABASE);
                return;
            }
        }

        TPApp.dbConfigured = dbConfigured;

        if (!dbConfigured && !isNetworkConnected()) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            displayErrorMessage("Data Network is not available on your device or Network Service Error. Please try again.");
            return;
        }

        photoPurge();

        Handler initHandler = new Handler();
        initHandler.postDelayed(() -> {
            try {
                // Check registered device
                ServiceResult regResult = ((HomeBLProcessor) screenBLProcessor).isRegisteredDevice(deviceName);
                if (!regResult.isResult() && regResult.getResultCode().equals(ServiceResult.DATA_NOT_AVAILABLE)) {
                    try {
                        DeviceInfo device = new DeviceInfo();
                        if (versionString != null && !versionString.equals("")) {
                            device.setAppVersion(versionString);
                        }

                        device.setPlatform(Build.MANUFACTURER + " " + Build.MODEL);
                        device.setOsVersion(Build.VERSION.RELEASE);
                        device.setLastSync(new Date());
                        device.setDeviceName(deviceName);
                        device = ((HomeBLProcessor) screenBLProcessor).registeredDevice(device);

                        if (device != null) {
                            dbConfigureHandler.sendEmptyMessage(DATABASE_DEVICE_REGISTERED);
                        } else {
                            dbConfigureHandler.sendEmptyMessage(DATABASE_DEVICE_REGISTRATION_FAILED);
                        }
                        return;
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                    errorHandler.sendEmptyMessage(ERROR_NOT_REGISTERED);
                    return;
                }

                // If device is not active
                if (regResult.getResultCode().equals(ServiceResult.NOT_ACTIVE)) {
                    dbConfigureHandler.sendEmptyMessage(DATABASE_DEVICE_NOT_ACTIVE);
                    return;
                }

                // Configure Database
                if (!TPApp.dbConfigured) {
                    if (!isServiceAvailable) {
                        errorHandler.sendEmptyMessage(ERROR_NETWORK);
                        return;
                    }
                    try {
                        // Download Logo Image
                        downloadCustomerLogo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean(TPConstant.PREFS_KEY_DBCONFIGURATION, true);
                    editor.apply();

                    new Thread(() -> {
                        try {
                            ((HomeBLProcessor) screenBLProcessor).configureDevice(message -> {
                                SharedPreferences.Editor editor1 = mPreferences.edit();
                                editor1.putString(TPConstant.PREFS_KEY_DBSYNCTIME, new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(new Date()));
                                editor1.apply();
                                // Update Device Sync Date
                                try {
                                    DeviceInfo device = DeviceInfo.getDeviceInfo(getDeviceId());
                                    if (device != null) {
                                        if (versionString != null && !versionString.equals("")) {
                                            device.setAppVersion(versionString);
                                        }
                                        device.setLastSync(new Date());
                                        DeviceInfo.insertDeviceInfo(device);
                                        /*DatabaseHelper.getInstance().openWritableDatabase();
                                        DatabaseHelper.getInstance().insertOrReplace(device.getContentValues(), "devices");
                                        DatabaseHelper.getInstance().closeWritableDb();*/
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // Display Loading Data
                                dbConfigureHandler.sendEmptyMessage(message.what);
                                return false;
                            });
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                            dbConfigureHandler.sendEmptyMessage(DATABASE_SYNC_ERROR);
                        }
                    }).start();

                } else {
                    downloadCustomerLogo();
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean(TPConstant.PREFS_KEY_RESTORE_SESSION, false);
                    editor.apply();
                    dbConfigureHandler.sendEmptyMessage(DATABASE_SUCCESSFUL);
                }
            } catch (TPException ae) {
                log.error(ae.getMessage());

                if (ae.getErrorCode() == TPException.DB_SYNC_ERROR) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            syncDatabase();
                        }
                    }).start();

                } else if (!isServiceAvailable) {
                    errorHandler.sendEmptyMessage(ERROR_NETWORK);

                } else {
                    dbConfigureHandler.sendEmptyMessage(DATABASE_ERROR);
                }
            }
        }, 2000);
    }

    private void photoPurge() {
        Intent serviceIntent = new Intent(HomeActivity.this, ServicePhotoPurge.class);
        ServicePhotoPurge.enqueueWork(HomeActivity.this, serviceIntent);

    }

   /* private void photoPurge() {
        if (TPApp.dbConfigured && Feature.isSystemFeatureAllowed(Feature.PHOTO_PURGE)) {
            Handler cleanupHandler = new Handler();
            cleanupHandler.postDelayed(() -> {
                String daysString = Feature.getFeatureValue(Feature.PHOTO_PURGE);
                if (daysString != null) {
                    if (daysString.isEmpty()) {
                        daysString = Feature.getFeatureValue(Feature.PHOTO_PURGE);
                    }
                    try {
                        int days = Integer.parseInt(daysString);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.add(Calendar.DATE, -days);

                        File dir = new File(TPUtility.getDataFolder() + "TicketImages");
                        File[] files = dir.listFiles();

                        if (files != null) {
                            for (File file : files) {
                                long lastmodified = file.lastModified();
                                if (lastmodified < cal.getTimeInMillis()) {
                                    log.info("Deleting ticket image " + file.getName());
                                    file.delete();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //log.error(e.getMessage());
                    }

                    //Deleting LPR Images on photoPurge Day basis
                    try {
                        int days = Integer.parseInt(daysString);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.add(Calendar.DATE, -days);

                        File dir = new File(TPUtility.getDataFolder() + "LPRImages");
                        File[] files = dir.listFiles();
                        assert files != null;
                        for (File file : files) {
                            long lastmodified = file.lastModified();
                            if (lastmodified < cal.getTimeInMillis()) {
                                file.delete();
                                log.info("Deleting LPR ticket image " + file.getName());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //log.error(e.getMessage());
                    }

                    //Deleting Chalk Images on photoPurge Day basis
                    try {
                        int days = Integer.parseInt(daysString);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.add(Calendar.DATE, -days);

                        File dir = new File(TPUtility.getDataFolder() + "ChalkImages");
                        File[] files = dir.listFiles();
                        for (File file : files) {
                            long lastmodified = file.lastModified();
                            if (lastmodified < cal.getTimeInMillis()) {
                                file.delete();
                                log.info("Deleting Chalk image " + file.getName());
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }

                    //Deleting CSV Files on photoPurge Day basis
                    try {
                        int days = Integer.parseInt(daysString);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.add(Calendar.DATE, -days);

                        File dir = new File(TPUtility.getDataFolder() + "CSVBackups");
                        File[] files = dir.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                long lastmodified = file.lastModified();
                                if (lastmodified < cal.getTimeInMillis()) {
                                    file.delete();
                                    log.info("Deleting CSVBackup files" + file.getName());
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }, 1000);
        }

    }
*/
    private void downloadCustomerLogo() {
        CustomerInfo customerInfo = TPApp.getCustomerInfo();
        File file = new File(TPUtility.getDataFolder() + customerInfo.getLogoImage());

        if (!file.exists() && !customerInfo.getLogoImage().equals("null")) {
            // Check Customer's Content Folder for Logos
            String contentFolder = customerInfo.getContentFolder();
            if (contentFolder == null || contentFolder.equals("")) {
                contentFolder = customerInfo.getCustId() + "";
            }

            String imageURL = TPConstant.ASSETS_URL + "/" + contentFolder + "/" + customerInfo.getLogoImage();
            new DownloadFilesTask().execute(imageURL, TPUtility.getDataFolder() + customerInfo.getLogoImage());

            lazyLoadImage(imageURL, customerInfo.getLogoImage(), logoImageView);
        }
    }

    @Override
    public void bindDataAtLoadingTime() {
        TPApp.stickyComments = mPreferences.getBoolean(TPConstant.PREFS_KEY_STICKY_COMMENTS, false);
        TPApp.stickyViolations = mPreferences.getBoolean(TPConstant.PREFS_KEY_STICKY_VIOLATIONS, false);
        TPApp.printDebugMode = mPreferences.getBoolean(TPConstant.PREFS_KEY_PRINT_DEBUG, false);
        TPApp.showPrintDialog = mPreferences.getBoolean(TPConstant.PREFS_KEY_SHOW_PRINTDIALOG, false);

        /* Configure SyncAdapter Account */
        try {
            Account syncAccount = new Account(TPSyncAdapter.ACCOUNT, TPSyncAdapter.ACCOUNT_TYPE);
            AccountManager accountManager = (AccountManager) getApplicationContext().getSystemService(ACCOUNT_SERVICE);

            if (accountManager.addAccountExplicitly(syncAccount, null, null)) {
                ContentResolver.setIsSyncable(syncAccount, TPSyncAdapter.AUTHORITY, 1);
                ContentResolver.addPeriodicSync(syncAccount, TPSyncAdapter.AUTHORITY, new Bundle(), 30 * 60 * 1000);
                ContentResolver.setSyncAutomatically(syncAccount, TPSyncAdapter.AUTHORITY, true);
            }
        } catch (Exception e) {
            Log.d("SyncAdapter", TPUtility.getPrintStackTrace(e));
        }

        TPApp.disableSync = true;
    }

    public void loginAction(View view) {
        if (Feature.isSystemFeatureAllowed("PARK_RMSID_LOGIN")) {
            Intent i = new Intent();
            //i.setClass(this, LoginSelectUserActivity.class);
            i.setClass(this, IDVerification.class);
            startActivity(i);
        } else {
            Intent i = new Intent();
            i.setClass(this, LoginSelectUserActivity.class);
            startActivity(i);
        }
    }

    public void syncAction(View view) {
        progressDialog = ProgressDialog.show(this, "", "Processing Database Synchronization...");

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        doSynchronization(false);
                        log.debug("1.Normal Sync execute");
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void settingsAction(View view) {
        openOptionsMenu();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);

        MenuItem item = menu.getItem(5);
        SpannableString s = new SpannableString("Backup Database");
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        item.setTitle(s);

        item = menu.getItem(6);
        s = new SpannableString("Advance Paper");
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        item.setTitle(s);

       if (Feature.isSystemFeatureAllowed(Feature.PARK_DIAGNOSTICS)) {
           item = menu.getItem(7);
           s = new SpannableString("Diagnostics");
           s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
           item.setTitle(s);


       }else {
           item = menu.getItem(7);
           s = new SpannableString("Diagnostics");
           s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
           item.setTitle(s);
           item.setVisible(false);
       }


        loggingMenu = menu.findItem(R.id.debug_off_menu);
        updateLogDebuggingMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.test_printer_menu:
                Intent i = new Intent();
                i.setClass(this, TicketPrinter.class);
                startActivity(i);
                return true;

            case R.id.activity_printer_menu:
                __openPasswordInputDialog();
                return true;

            case R.id.system_backup_menu:
                Intent inte= new Intent();
                inte.setClass(this, NewSyncBackup.class);
                startActivity(inte);
                return true;

            case R.id.test_network_menu:
                if (isServiceAvailable) {
                    Toast.makeText(HomeActivity.this, "Network Test Successfull.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Data network is not available on this device or Network Service Error.", Toast.LENGTH_SHORT).show();
                }

                return true;

            case R.id.full_sync_menu:
                progressDialog = ProgressDialog.show(this, "", "Processing Database Synchronization...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doSynchronization(true);
                            log.debug("2.Full Sync execute");
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    }
                }).start();
                return true;

            case R.id.backup_menu:
                progressDialog = ProgressDialog.show(this, "", "Processing Database Backup...");
                backupDatabase();
                return true;

            case R.id.advance_paper_menu:
                TPUtility.printAdvancePaper(HomeActivity.this);
                return true;

            case R.id.debug_off_menu:
                changeLogMenuPref();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void __openPasswordInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mPass = input.getText().toString();

                if (!TextUtils.isEmpty(mPass)) {
                    int userId = User.getUserId(mPass);
                    if (userId > 0) {
                        Intent i2 = new Intent();
                        i2.putExtra("USER_ID", userId);
                        i2.setClass(HomeActivity.this, PrintPreviewSpecialActivity.class);
                        startActivity(i2);
                    }

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void changeLogMenuPref() {
        try {
            logPreferences = getApplicationContext().getSharedPreferences(TPConstant.PREFS_KEY_ENABLE_LOG, Context.MODE_PRIVATE);
            isLoggingEnabled = logPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_LOG, true);
            SharedPreferences.Editor editor = logPreferences.edit();

            if (loggingMenu != null) {
                if (!isLoggingEnabled) {
                    loggingMenu.setTitle("Debug ON");
                    loggingMenu.setIcon(R.drawable.log_enabled);
                } else {
                    loggingMenu.setTitle("Debug OFF");
                    loggingMenu.setIcon(R.drawable.log_disabled);
                }
            }

            editor.putBoolean(TPConstant.PREFS_KEY_ENABLE_LOG, !isLoggingEnabled);
            editor.commit();

            LoggerConfigurator.toggleDebugLog(this, !isLoggingEnabled);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void initLogDebugging() {
        try {
            logPreferences = getApplicationContext().getSharedPreferences(TPConstant.PREFS_KEY_ENABLE_LOG, Context.MODE_PRIVATE);
            isLoggingEnabled = logPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_LOG, true);
            LoggerConfigurator.toggleDebugLog(this, isLoggingEnabled);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void updateLogDebuggingMenu() {
        try {
            logPreferences = getApplicationContext().getSharedPreferences(TPConstant.PREFS_KEY_ENABLE_LOG, Context.MODE_PRIVATE);
            isLoggingEnabled = logPreferences.getBoolean(TPConstant.PREFS_KEY_ENABLE_LOG, true);

            if (loggingMenu != null) {
                if (!isLoggingEnabled) {
                    loggingMenu.setTitle("Debug OFF");
                    loggingMenu.setIcon(R.drawable.log_disabled);
                } else {
                    loggingMenu.setTitle("Debug ON");
                    loggingMenu.setIcon(R.drawable.log_enabled);
                }
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

//    private String getDeviceId() {
//        String deviceName = "";
//        deviceName = mPreferences.getString("DeviceID", null);
//
//        if (deviceName == null) {
//            // Check for telephony features
//            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//
//            if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
//                deviceName = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
//            } else {
//                deviceName = telephonyManager.getDeviceId();
//            }
//            if (deviceName == null || deviceName.equals("")){
//                deviceName = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
//            }
//
//            if (deviceName == null || deviceName.equals("")) {
//                deviceName = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
//            }
//
//            if (TPUtility.isRunningOnEmulator(getApplicationContext()) || "000000000000000".equals(deviceName)) {
//                deviceName = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
//            }
//
//            if (deviceName == null || deviceName.equals("")) {
//                try {
//                    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//                    WifiInfo wInfo = wifiManager.getConnectionInfo();
//                    deviceName = wInfo.getMacAddress();
//                } catch (Exception e) {
//                    deviceName = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
//                }
//            }
//
//            if (deviceName == null || deviceName.equals("")) {
//                deviceName = Long.toString(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
//            }
//
//            // Append ModuleName
//            deviceName = deviceName + "-" + TPConstant.MODULE_NAME;
//
//            SharedPreferences.Editor editor = mPreferences.edit();
//            editor.putString("DeviceID", deviceName);
//            editor.commit();
//        }
//
//        return deviceName;
//    }

    private void backupDatabase() {
        //DatabaseHelper.getInstance().backupDatabase();
        ParkingDatabase.backupDatabase(HomeActivity.this);
        progressDialog.dismiss();
    }

    private void doSynchronization(boolean fullSync) {
        try {
            if (!isNetworkConnected()) {
                errorHandler.sendEmptyMessage(ERROR_NETWORK);
                return;
            }

            // Check registered device
            ServiceResult regResult = ((HomeBLProcessor) screenBLProcessor).isRegisteredDevice(getDeviceId());
            try {
                if (!regResult.isResult() && regResult.getResultCode().equals(ServiceResult.DATA_NOT_AVAILABLE)) {
                    try {
                        DeviceInfo device = new DeviceInfo();
                        if (!StringUtil.isEmpty(versionString)) {
                            device.setAppVersion(versionString);
                        }

                        device.setPlatform(Build.MANUFACTURER + " " + Build.MODEL);
                        device.setOsVersion(Build.VERSION.RELEASE);
                        device.setLastSync(new Date());
                        device.setDeviceName(getDeviceId());
                        device = ((HomeBLProcessor) screenBLProcessor).registeredDevice(device);

                        if (device != null) {
                            dbConfigureHandler.sendEmptyMessage(DATABASE_DEVICE_REGISTERED);
                            return;

                        } else {
                            dbConfigureHandler.sendEmptyMessage(DATABASE_DEVICE_REGISTRATION_FAILED);
                            return;
                        }

                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }

                    errorHandler.sendEmptyMessage(ERROR_NOT_REGISTERED);
                    return;
                }

                if (regResult.getResultCode().equals(ServiceResult.NOT_ACTIVE)) {
                    dbConfigureHandler.sendEmptyMessage(DATABASE_DEVICE_NOT_ACTIVE);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            boolean dbConfigured = mPreferences.getBoolean(TPConstant.PREFS_KEY_DBCONFIGURATION, false);
            if (!dbConfigured) {
                fullSync = true;
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_DBCONFIGURATION, true);
                editor.apply();
            } else {
                if (Feature.isSystemFeatureAllowed(Feature.BACKUP_ON_SYNC)) {
                    ParkingDatabase.backupDatabase(HomeActivity.this);
                }
            }

            ((HomeBLProcessor) screenBLProcessor).doSynchronize(fullSync, this, msg -> {
                syncResult = msg.what == 1;
                return false;
            });

            if (!syncResult) {
                try {
                    // Update Location Database Sync Date
                    DeviceInfo device = DeviceInfo.getDeviceInfo(getDeviceId());
                    if (device == null) {
                        errorHandler.sendEmptyMessage(ERROR_DATABASE);
                        return;
                    }
                    device.setLastSync(new Date());
                    if (versionString != null && !versionString.equals("")) {
                        device.setAppVersion(versionString);
                    }
                    DeviceInfo.insertDeviceInfo(device);

                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                }
                synchHandler.sendEmptyMessage(SYNC_SUCCESSFUL);
            } else {
                synchHandler.sendEmptyMessage(SYNC_ERROR);
            }
        } catch (TPException ae) {
            log.error(ae.getMessage());
            errorHandler.sendEmptyMessage(SYNC_ERROR);
            Looper.prepareMainLooper();
            TPUtility.showErrorDialog(HomeActivity.this, getString(R.string.server_error));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HardwareIds")
    private String getDeviceId() {
        String deviceName;
        deviceName = mPreferences.getString("DeviceID", null);

        if (deviceName == null) {
            // Check for telephony features
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            assert telephonyManager != null;
            try {
                if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
                    deviceName = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return "";
                    }
                    deviceName = telephonyManager.getDeviceId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (TPUtility.isRunningOnEmulator(getApplicationContext()) || "000000000000000".equals(deviceName)) {
                    deviceName = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (deviceName == null || deviceName.equals("")) {
                    deviceName = Secure.getString(getContentResolver(),
                            Secure.ANDROID_ID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (deviceName == null || deviceName.equals("")) {
                try {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wInfo = wifiManager.getConnectionInfo();
                    deviceName = wInfo.getMacAddress();
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                    deviceName = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
                }
            }

            if (deviceName == null || deviceName.equals("")) {
                deviceName = Long.toString(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
            }

            // Append ModuleName
            deviceName = deviceName + "-" + TPConstant.MODULE_NAME;

            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("DeviceID", deviceName);
            editor.apply();
        }
        TPApplication.getInstance().setDeviceIdName(deviceName);
        return deviceName;
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
        Button syncButton = (Button) findViewById(R.id.home_synchronize_btn);
        if (connected) {
            syncButton.setEnabled(true);
            syncButton.setBackgroundResource(R.drawable.btn_black);
            if (TPApp.dbConfigured) {
                loginBtn.setEnabled(true);
                loginBtn.setBackgroundResource(R.drawable.btn_yellow);
            }

            if (!isFastConnection) {
                syncButton.setEnabled(false);
                syncButton.setBackgroundResource(R.drawable.btn_disabled);
            }
        } else {
            syncButton.setEnabled(false);
            syncButton.setBackgroundResource(R.drawable.btn_disabled);
        }
    }

    private void syncDatabase() {
        if (!isServiceAvailable) {
            errorHandler.sendEmptyMessage(ERROR_NETWORK);
            return;
        }

        ((HomeBLProcessor) screenBLProcessor).doSynchronize(true, this, msg -> {
            syncResult = msg.what == 2;
            return false;
        });

        if (syncResult) {
            try {
                DeviceInfo device = DeviceInfo.getDeviceInfo(getDeviceId());
                if (device == null) {
                    errorHandler.sendEmptyMessage(ERROR_DATABASE);
                    return;
                }

                device.setLastSync(new Date());
                if (versionString != null && !versionString.equals("")) {
                    device.setAppVersion(versionString);
                }
                DeviceInfo.insertDeviceInfo(device);
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            synchHandler.sendEmptyMessage(SYNC_SUCCESSFUL);

        } else {
            synchHandler.sendEmptyMessage(SYNC_ERROR);
        }
    }

    public void getDate() {
        String utcString1 = "11/28/2017 23:46:11";
        fromISO8601UTC(utcString1);
    }

    public void fromISO8601UTC(String utcString) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = format.parse(utcString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainDate(date);

    }

    public String mainDate(Date date) {
        return formatDateToString(date, "MM/dd/yyyy hh:mm:ss", null);
        //System.out.println(formatDateToString(date, "dd MMM yyyy hh:mm:ss a", null));
        //System.out.println(formatDateToString(date, "MM/dd/yyyy hh:mm:ss", null));
		/*System.out.println("System Date in IST: "+formatDateToString(date, "dd MMM yyyy hh:mm:ss a", "IST"));
		System.out.println("System Date in GMT: "+formatDateToString(date, "dd MMM yyyy hh:mm:ss a", "GMT"));*/
    }

    private void registerDeleteBroadcast() {
        log.info("delete broadcast registered");
        try {
            try {
                if (Feature.isAutoDeleteFeatureAllowed(Feature.PARKING_CLEAR_TICKET_BY_HOUR, this)) {
                    // Initiate periodic task for data cleanup
                    Intent intent = new Intent(this, CleanupReceiver.class);
                    intent.putExtra("Type", "Chalk");
                    PendingIntent pIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                    String featureParkingClearTicket = Feature.getFeatureParkingClearTicket(Feature.PARKING_CLEAR_TICKET_BY_HOUR);

                    int hours = Integer.parseInt(featureParkingClearTicket);
                    alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), hours * AlarmManager.INTERVAL_HOUR, pIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            DataUtility.setDeleteScheduler(this);

            /*// Initiate periodic task for data cleanup
            Intent intent = new Intent(this, TicketCleanupReceiver.class);
            intent.putExtra("TypeClearTickets", "Tickets");
            PendingIntent pIntent;
            AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            String featureParkingClearTicket;
            if (Feature.isAutoDeleteFeatureAllowed(Feature.PARKING_CLEAR_TICKET_BY_HOUR, this)) {
                featureParkingClearTicket = Feature.getFeatureParkingClearTicket(Feature.PARKING_CLEAR_TICKET_BY_HOUR);
                log.info("delete feature is active and value is set to :"+ featureParkingClearTicket);
                int minutes = Integer.parseInt(featureParkingClearTicket);
                long currntTime = System.currentTimeMillis();
                if (mPreferences.getLong(TPConstant.PREFS_SCHEDULE_TIME, currntTime) < currntTime) {
                    time = mPreferences.getLong(TPConstant.PREFS_SCHEDULE_TIME, currntTime);
                } else {
                    time = currntTime;
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putLong(TPConstant.PREFS_SCHEDULE_TIME, time);
                    editor.apply();
                }
                intent.putExtra("fromFeature",true);
                pIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                time = time + (minutes * 60 * 1000);
                assert alarm != null;
                alarm.setRepeating(AlarmManager.RTC, time, minutes * 60 * 1000, pIntent);
            } else {
                Calendar calendar = Calendar.getInstance(Locale.US);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                log.info("delete tickets at midnight");
                intent.putExtra("fromFeature",false);
                pIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarm.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);
            }*/

            Intent rointent = new Intent(this, RepeatOffenderReceiver.class);
            rointent.putExtra("MSG", "SyncRepeatOffender");
            PendingIntent ropIntent = PendingIntent.getBroadcast(this, 1, rointent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager roalarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            roalarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, ropIntent);

            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build();
            PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(DeviceDataWorker.class, 15, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .addTag("DELETE_PENDING_FBDATA")
                    .build();
            WorkManager.getInstance(HomeActivity.this).cancelAllWorkByTag("DELETE_PENDING_FBDATA");
            WorkManager.getInstance(HomeActivity.this).enqueue(workRequest);
//            JobUtil.scheduleJob(HomeActivity.this);
           /* AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent fbIntent = new Intent(this, FirebaseReceiver.class);
            PendingIntent fbpendingIntent = PendingIntent.getBroadcast(this, 1, fbIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            assert alarmManager != null;
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, fbpendingIntent);*/

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //
    private void saveDeviceFeaturesDetails() {
        try {
            if (isServiceAvailable) {
                RequestPOJODeviceFeatures requestPOJO = new RequestPOJODeviceFeatures();
                ArrayList<DeviceFeatures> arrayList = new ArrayList<>();
                requestPOJO.setMethod("saveDeviceFeatures");
                ArrayList<Feature> features = Feature.getFeaturesList(TPApplication.getInstance().custId);
                if (features.size() > 0) {
                    for (int i = 0; i < features.size(); i++) {
                        Feature feature = features.get(i);

                        DeviceFeatures p = new DeviceFeatures();
                        p.setCustId(TPApplication.getInstance().custId);
                        p.setUserId(TPApplication.getInstance().userId);
                        p.setDeviceId(TPApplication.getInstance().deviceId);
                        p.setDevice(TPApplication.getInstance().deviceName);
                        p.setFeatureName(feature.getFeature());
                        p.setAdmin(feature.getAllowedAdmin());
                        p.setIsActive(String.valueOf(feature.isAllowed()));
                        p.setModuleName("Parking");
                        p.setOfficer(feature.getAllowedOfficer());
                        p.setValue(feature.getValue());
                        arrayList.add(p);


                    }

              /*  for (Feature feature : features) {
                    DeviceFeatures p = new DeviceFeatures();
                    p.setCustId(TPApplication.getInstance().custId);
                    p.setUserId(TPApplication.getInstance().userId);
                    p.setDeviceId(TPApplication.getInstance().deviceId);
                    p.setDevice(TPApplication.getInstance().deviceName);
                    p.setFeatureName(feature.getFeature());
                    p.setAdmin(feature.getAllowedAdmin());
                    p.setIsActive(String.valueOf(feature.isAllowed()));
                    p.setModuleName("Parking");
                    p.setOfficer(feature.getAllowedOfficer());
                    p.setValue(feature.getValue());
                    arrayList.add(p);
                }*/
                    ParamsDeviceFeatures pp = new ParamsDeviceFeatures();
                    pp.setDeviceFeaturesData(arrayList);
                    requestPOJO.setParams(pp);
                    ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
                    api.saveDeviceFeatures1(requestPOJO).enqueue(new Callback<ResponseResult>() {
                        @Override
                        public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {

                            if (response.isSuccessful()) {
                                System.out.println("Response is: " + response.body().getResult());
                                Result result = response.body().getResult();
                                if (result!=null && result.getResult()) {
                                    preference.putBoolean("DEVICE_FEATURE", result.getResult());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseResult> call, Throwable t) {

                            System.out.println(t.getMessage());
                            api.saveDeviceFeatures1(requestPOJO).cancel();
                        }
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }


    }

}
