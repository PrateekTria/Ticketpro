package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.gms.location.LocationStatusCodes;
import com.ticketpro.gpslibrary.ADLocation;
import com.ticketpro.gpslibrary.GetLocation1;
import com.ticketpro.gpslibrary.MyTracker;
import com.ticketpro.model.Address;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.DeviceInfo;
import com.ticketpro.model.Duty;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Placard;
import com.ticketpro.model.PrintTemplate;
import com.ticketpro.model.SpecialActivity;
import com.ticketpro.model.SpecialActivityReport;
import com.ticketpro.model.SyncData;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.TicketComment;
import com.ticketpro.model.TicketCommentTemp;
import com.ticketpro.model.TicketPicture;
import com.ticketpro.model.TicketPictureTemp;
import com.ticketpro.model.TicketTemp;
import com.ticketpro.model.TicketViolation;
import com.ticketpro.model.TicketViolationTemp;
import com.ticketpro.model.User;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.MainBLProcessor;
import com.ticketpro.parking.bl.SpecialActivityBLProcessor;
import com.ticketpro.parking.service.JobIntentServiceSaveTicket;
import com.ticketpro.parking.service.LocationUpdatesService;
import com.ticketpro.print.TicketPrinter;
import com.ticketpro.util.DataUtility;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.GPSTracker;
import com.ticketpro.util.MyLocationReceiver;
import com.ticketpro.util.Preference;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class MainActivity extends BaseActivityImpl implements MyTracker.ADLocationListener {

    final int PENDING_TICKETS_TAG = 11;
    final int USER_SETTINGS_TAG = 0;
    final int WRITE_TICKET_TAG = 1;
    final int SEARCH_TAG = 2;
    final int CHALK_TAG = 3;
    final int SPECIAL_TAG = 4;
    final int CHALK_LOCATION_TAG = 5;
    final int CHALK_PHOTOS_TAG = 6;
    final int CHANGE_DUTY_TAG = 7;
    final int END_SHIFT_TAG = 8;
    final int LOGOUT = 9;
    final int DATA_ERROR = 0;
    final int DATA_SUCCESSFULL = 1;
    final int ERROR_LOAD = 1;
    final int ERROR_SERVICE = 2;
    private TextView idLabel;
    private TextView versionLabel;
    private TextView locationLabel;
    private ImageView statusIndicatorImageView;
    private ImageView userSettingsImageView;
    private ImageView switchToTrafficImageView;
    private RelativeLayout writeTicketTR;
    private RelativeLayout searchTR;
    private RelativeLayout chalkTR;
    private RelativeLayout chalkPhotosTR;
    private RelativeLayout specialTR;
    private RelativeLayout changeDutyTR;
    private RelativeLayout endShiftTR;
    private RelativeLayout logoutTR;
    private RelativeLayout pendingTickets;
    private ProgressDialog progressDialog;
    private Handler errorHandler;
    private Handler dataLoadingHandler;
    private Handler GPSHandler;
    private String versionNo;
    private SharedPreferences mPreferences;
    private GPSTracker gpsTracker;
    private Address gpsAddress;
    /*private MyLocationReceiver myLocationReceiver;
    private Intent intent;*/
    private Preference preference;
    private SpecialActivityReport specialActivity;
    private String actionPerform = null;

    private String latitude;
    private String longitude;
    private ProgressDialog pd;
    private Handler activityHandler;
    private CountDownTimer countDownTimer1;
    private CountDownTimer countDownTimer;
    private MyLocationReceiver myReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            System.gc();
            myReceiver = new MyLocationReceiver();
            TPUtility.removeTxtFile();
            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            preference = Preference.getInstance(this);
            preference.putString(TPConstant.CURRENT_SAMTRANS_SPACE, null);
            specialActivity = TPApp.getReport();
            /* Start of Session Persistence */
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(TPConstant.PREFS_KEY_RESTORE_USERID, TPApp.userId);
            editor.putInt(TPConstant.PREFS_KEY_RESTORE_DEVICEID, TPApp.deviceId);
            editor.putInt(TPConstant.PREFS_KEY_RESTORE_CUSTID, TPApp.custId);
            preference.putString("ZoneCode","");
            TPApplication.getInstance().setIpsParkMobile(0);

            if (TPApp.getActiveDutyInfo() != null) {
                editor.putInt(TPConstant.PREFS_KEY_RESTORE_DUTYID, TPApp.getActiveDutyInfo().getId());
            }

            editor.apply();

            /* End Session Persistence */
            try {
                if (Feature.isFeatureAllowed(Feature.AUTODELETE_CHALKLOG)) {
                    DataUtility.checkExpiredChalks(this);
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            setLogger(MainActivity.class.getName());
            setBLProcessor(new MainBLProcessor((TPApplication) getApplicationContext()));
            setActiveScreen(this);

            /*
             * if (alarmManager == null) { alarmManager = (AlarmManager)
             * getSystemService(Context.ALARM_SERVICE); Intent intent = new
             * Intent(this, GpsAlarmReceiver.class); pendingIntent =
             * PendingIntent.getBroadcast(this, 0, intent, 0);
             * alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
             * System.currentTimeMillis(), 30000, pendingIntent); }
             */

            isNetworkInfoRequired = true;

            /*try {
                intent = new Intent(MainActivity.this, LocationService.class);
                startService(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            versionLabel = findViewById(R.id.main_version_label);
            locationLabel = findViewById(R.id.main_location_label);
            statusIndicatorImageView = findViewById(R.id.main_status_indicator);
            idLabel = findViewById(R.id.id_label);
            idLabel.setText(getDeviceId());

            userSettingsImageView = findViewById(R.id.main_user_settings);
            userSettingsImageView.setOnClickListener(this);
            userSettingsImageView.setTag(USER_SETTINGS_TAG);

            switchToTrafficImageView = findViewById(R.id.btnTraffic);

            try {
                if (TPUtility.isTrafficInstallled(getPackageManager())) {
                    switchToTrafficImageView.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            writeTicketTR = findViewById(R.id.main_write_ticket_option);
            writeTicketTR.setOnClickListener(this);
            writeTicketTR.setTag(WRITE_TICKET_TAG);

            searchTR = findViewById(R.id.main_search_option);
            searchTR.setOnClickListener(this);
            searchTR.setTag(SEARCH_TAG);

            chalkTR = findViewById(R.id.main_chalk_option);
            chalkTR.setOnClickListener(this);
            chalkTR.setTag(CHALK_TAG);

            chalkPhotosTR = findViewById(R.id.main_chalk_photos_option);
            chalkPhotosTR.setOnClickListener(this);
            chalkPhotosTR.setTag(CHALK_PHOTOS_TAG);

            specialTR = findViewById(R.id.main_special_option);
            specialTR.setOnClickListener(this);
            specialTR.setTag(SPECIAL_TAG);

            changeDutyTR = findViewById(R.id.main_change_duty_option);
            changeDutyTR.setOnClickListener(this);
            changeDutyTR.setTag(CHANGE_DUTY_TAG);

            endShiftTR = findViewById(R.id.main_end_shift_option);
            endShiftTR.setOnClickListener(this);
            endShiftTR.setTag(END_SHIFT_TAG);

            pendingTickets = findViewById(R.id.main_pendingTickets);
            pendingTickets.setOnClickListener(this);
            pendingTickets.setTag(PENDING_TICKETS_TAG);


            logoutTR = findViewById(R.id.main_logout);
            logoutTR.setOnClickListener(this);
            logoutTR.setTag(LOGOUT);


            if (Feature.isFeatureAllowed("SpecialActivity")) {
                endShiftTR.setVisibility(View.VISIBLE);

            } else {
                endShiftTR.setVisibility(View.GONE);
            }
            logoutTR.setVisibility(View.VISIBLE);

            checkTrainingUser();
			/*User userInfo = TPApp.getUserInfo();
			if (userInfo.getBadge().equals("8888")){
				AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
				confirmBuilder.setTitle("Alert")
						.setMessage("You are currently logged in as training.")
						.setCancelable(true)
						//.setNegativeButton("No", new CancelClickHandler())
						.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						});

				confirmBuilder.create().show();

			}*/

            activityHandler = new Handler() {
                /**
                 * Subclasses must implement this to receive messages.
                 * @param msg
                 */
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);

                    if (pd.isShowing())
                        pd.dismiss();
                }
            };

            dataLoadingHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    versionLabel.setText(versionNo);
                    enableDisableMenus();
                    isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                    if (isServiceAvailable) {
                        //new Thread(() -> ((MainBLProcessor) screenBLProcessor).syncServices()).start();
                    }
                    if (progressDialog!=null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            };

            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    enableDisableMenus();

                    if (progressDialog!=null &&progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    switch (msg.what) {
                        case ERROR_SERVICE:
                            Toast.makeText(getBaseContext(), TPConstant.GENERIC_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            GPSHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                    if (isServiceAvailable) {
                        if (isFastConnection) {
                            statusIndicatorImageView.setImageResource(R.drawable.green_status_btn_bk);
                        } else {
                            statusIndicatorImageView.setImageResource(R.drawable.yellow_status_btn);
                        }
                    } else {
                        statusIndicatorImageView.setImageResource(R.drawable.gray_status_btn);
                    }

                    if (gpsAddress != null) {
                        locationLabel.setText(TPUtility.getFullAddress(gpsAddress));
                    }
                }
            };

            bindDataAtLoadingTime();
            TPApp.checkNetworkSignal = Feature.isSystemFeatureAllowed(Feature.CHECK_NETWORK_SIGNAL);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void clearOlderTickets() {
        try {
            progressDialog = ProgressDialog.show(this, "", "Clearing previous day's Tickets. Please wait...");
            progressDialog.setCancelable(false);

            Ticket.removeAllOlderTicketsAtMidnight(log, true);
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    private void checkTrainingUser() {
        try {
            User userInfo = TPApp.getUserInfo();
            if (userInfo != null) {
                if (userInfo.getBadge().equals("8888")) {
                    AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
                    confirmBuilder.setTitle("Alert")
                            .setMessage("You are currently logged in as training.")
                            .setCancelable(true)
                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    confirmBuilder.create().show();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableDisableMenus() {
        try {
            Duty activeDuty = TPApp.getActiveDutyInfo();
            if (activeDuty != null && activeDuty.getAllowTicket().equalsIgnoreCase("N")) {
                writeTicketTR.setEnabled(false);
                writeTicketTR.setBackgroundResource(R.drawable.btn_disabled);
                chalkPhotosTR.setEnabled(false);
                chalkPhotosTR.setBackgroundResource(R.drawable.btn_disabled);
                chalkTR.setEnabled(false);
                chalkTR.setBackgroundResource(R.drawable.btn_disabled);
            }

            if (!Feature.isFeatureAllowed(Feature.CHALK)) {
                chalkPhotosTR.setEnabled(false);
                chalkPhotosTR.setBackgroundResource(R.drawable.btn_disabled);
                chalkTR.setEnabled(false);
                chalkTR.setBackgroundResource(R.drawable.btn_disabled);
            }

            if (!Feature.isFeatureAllowed(Feature.SPECIAL_MENU)) {
                specialTR.setEnabled(false);
                specialTR.setBackgroundResource(R.drawable.btn_disabled);
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
       // bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        try {
            TPApplication.getInstance().setDeviceInfo(DeviceInfo.getDeviceInfo(TPApplication.getInstance().getDeviceIdName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();


        /*if (isServiceAvailable) {
            ArrayList<Ticket> pendingTickets = Ticket.getPendingTickets();
            if (pendingTickets.size()>0) {
                Intent serviceIntent = new Intent(MainActivity.this, JobIntentServiceSaveTicket.class);
                JobIntentServiceSaveTicket.enqueueWork(MainActivity.this, serviceIntent);
            }
            //serviceProxy.verifyAndUploadTickets(getApplicationContext(), false);
        }*/

        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
        System.out.println("MAINACTIVITY RESUME METHOD");
        initTimeOut();
        if (Feature.isFeatureAllowed("SpecialActivity")) {

            if (actionPerform != null && actionPerform.equals("ACTION")) {
                try {
                    _endActivity();
                    actionPerform = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        findLoc();

        try {
            if (TPApp.activeDutyInfo != null && TPApp.activeDutyInfo.getAllowTicket().equalsIgnoreCase("N")) {
                enableDisableMenus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (countDownTimer != null) {
                countDownTimer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserInteraction() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    public void bindDataAtLoadingTime() {
        TPApp.disableSync = false;


        try {
            versionNo = "TicketPro v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            if (TPConstant.IS_DEVELOPMENT_BUILD) {
                versionNo = versionNo + "(D)";
            }
            if (TPConstant.IS_STAGING_BUILD) {
                versionNo = versionNo + "(S)";
            }
        } catch (Exception e) {
            versionNo = ((MainBLProcessor) screenBLProcessor).getVersionNo();
        }
        clearOlderTickets();
        dataLoadingHandler.sendEmptyMessage(DATA_SUCCESSFULL);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        endShift();
    }

    private void endShift() {

        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_lock_power_off).setTitle("Closing Activity").setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TPApplication.getInstance().setNetOnOff("");
                        preference.putString("PLACARD", "");
                        try {
                            Placard.removeAll();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        TPUtility.createTxtFile();
                        finishActivity();
                        /* try {
                            // Close Active User Session and Update Duty Report
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                                    ((MainBLProcessor) screenBLProcessor).closeActiveDuty(isServiceAvailable);
                                }
                            }).start();

                            try {

                                SharedPreferences.Editor editor = mPreferences.edit();
                                editor.putBoolean(TPConstant.PREFS_KEY_STICKY_VIOLATIONS, false);
                                TPApp.setStickyComment(null);
                                TPApp.setStickyViolations(false);
                                TPApp.setStickyViolation(null);
                                editor.apply();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(TPConstant.EXTRA_END_SHIFT, true);
                            intent.setClass(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            TPUtility.createTxtFile();
                            TPUtility.updateFB(MainActivity.this, "logOut");
                            //Toast.makeText(MainActivity.this,"LogOut called.",Toast.LENGTH_LONG).show();
                            if (mService != null) {
                                mService.removeLocationUpdates();
                            }
                            finish();
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }*/
                    }
                }).setNegativeButton("No", null).show();
    }

    @Override
    public void onClick(View v) {
        try {
            int tagId = Integer.parseInt(v.getTag().toString());
            switch (tagId) {
                case USER_SETTINGS_TAG: {
                    userSettings();
                    return;
                }

                case WRITE_TICKET_TAG: {
                    if (Feature.isFeatureAllowed("SpecialActivity")) {
                        actionPerform = "ACTION";
                        _startActivity("ENFORCEMENT");
                    }
                    writeTicket();
                    return;
                }

                case SEARCH_TAG: {
                    search();
                    return;
                }

                case SPECIAL_TAG: {
                    showSpecialOptions();
                    return;
                }

                case PENDING_TICKETS_TAG: {
                    __showPendingTickets();
                    return;
                }

                case CHALK_TAG: {
                    if (Feature.isFeatureAllowed("SpecialActivity")) {
                        actionPerform = "ACTION";
                        _startActivity("CHALK VEHICLE");
                    }
                    chalk();
                    return;
                }

                case CHALK_PHOTOS_TAG: {
                    if (Feature.isFeatureAllowed("SpecialActivity")) {
                        actionPerform = "ACTION";
                        _startActivity("CHALK LOCATION");
                    }
                    photoChalk();
                    return;
                }

                case CHANGE_DUTY_TAG: {
                    changeDuty();
                    return;
                }

                case END_SHIFT_TAG: {
                    Intent intent = new Intent(MainActivity.this, PrintPreviewSpecialActivity.class);
                    intent.putExtra("USER_ID", TPApplication.getInstance().userId);
                    startActivity(intent);
                    return;
                }

                case LOGOUT: {
                    //TPApplication.getInstance().setNetOnOff("");
                    endShift();
                    return;
                }

            }// switch

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            errorHandler.sendEmptyMessage(ERROR_SERVICE);
        }
    }

    private void __showPendingTickets() {
        boolean b = Ticket.checkOlderTickets(TPApplication.getInstance().userId);
        if (b) {
            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(MainActivity.this);
            confirmBuilder.setCancelable(false);
            confirmBuilder.setTitle("Alert").setMessage("Tickets from previous day that been successfully process will be cleared. Tickets from today will remain on the list. Continue?").setCancelable(true)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Ticket.removeOlderTickets(TPApplication.getInstance().userId);
                        }
                    });

            AlertDialog confirmAlert = confirmBuilder.create();
            confirmAlert.show();
        } else {
            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(MainActivity.this);
            confirmBuilder.setCancelable(false);
            confirmBuilder.setTitle("Alert").setMessage("Tickets not found.").setCancelable(true)
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });


            AlertDialog confirmAlert = confirmBuilder.create();
            confirmAlert.show();
        }
    }

    private void _startActivity(String str) {
        try {
            specialActivity.setStartDate(DateUtil.getCurrentDateTime1());
            specialActivity.setStartTime(DateUtil.getCurrentDateTimeActivity());
            specialActivity.setUserId(TPApp.userId);
            specialActivity.setCustId(TPApp.custId);
            specialActivity.setAutoSelect(SpecialActivity.getAutoSelect(str));
            specialActivity.setActivityName(str);
            specialActivity.setNotes(null);
            specialActivity.setStreetAddress(null);
            specialActivity.setActivityId(SpecialActivity.getActivityIdByName(str));
            specialActivity.setCaseNumber(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _endActivity() throws Exception {
        specialActivity.setEndDate(DateUtil.getCurrentDateTime1());
        specialActivity.setEndTime(DateUtil.getCurrentDateTimeActivity());
        specialActivity.setCreatedDate(DateUtil.getCurrentDate());
        specialActivity.setDuration(String.valueOf(specialActivity.getExpireInfo().getExpireMsg()));
        specialActivity.setLatitude(latitude);
        specialActivity.setLongitude(longitude);
        specialActivity.setDeviceId(TPApp.deviceId);
        String count = Ticket.getTicketCountFromTicketTable(specialActivity.getUserId(), DateUtil.getDateFromString(specialActivity.getStartDate()), DateUtil.getDateFromString(specialActivity.getEndDate()));
        System.out.println("total count: " + count);
        specialActivity.setTicketCount(count);

        /*DatabaseHelper.getInstance().openWritableDatabase();
        DatabaseHelper.getInstance().insertOrReplace(specialActivity.getContentValues(), "special_activity_reports");
*/
        SpecialActivityReport.insertSpecialActivityReport(specialActivity);
        if (isServiceAvailable) {
            pd = ProgressDialog.show(MainActivity.this, "", "Wait...", false, true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SpecialActivityBLProcessor blProcessor = new SpecialActivityBLProcessor();
                        JSONArray activityReports = new JSONArray();
                        activityReports.put(specialActivity.getJSONObject());
                        boolean b = blProcessor.updateActivity(activityReports);
                        activityHandler.sendEmptyMessage(1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();


        } else {
            SyncData syncData = new SyncData();
            syncData.setActivity("INSERT");
            syncData.setPrimaryKey(specialActivity.getReportId() + "");
            syncData.setActivityDate(new Date());
            syncData.setCustId(TPApp.custId);
            syncData.setTableName(TPConstant.TABLE_SPECIAL_ACTIVITY_REPORTS);
            syncData.setStatus("Pending");
            SyncData.insertSyncData(syncData);
            /*DatabaseHelper.getInstance().insertOrReplace(syncData.getContentValues(), "sync_data");
            DatabaseHelper.getInstance().closeWritableDb();*/
        }

    }

    private void search() {
        Intent i = new Intent();
        i.setClass(MainActivity.this, SearchActivity.class);
        startActivity(i);
    }

    private void chalk() {
        if (!chalkTR.isEnabled())
            return;

        ChalkVehicle chalk = TPApp.createNewChalk();
        TPApp.setActiveChalk(chalk);

        Intent i = new Intent();
        i.setClass(MainActivity.this, ChalkVehicleActivity.class);
        startActivity(i);
    }

    private void changeDuty() {
        /*
         * On change duty, show list of activities to change, once selected
         * update activity report otherwise back to main screen with same
         * activity
         */

        Intent intent = new Intent();
        intent.putExtra("fromClass", "MainActivity");
        intent.setClass(this, DayActivitiesActivity.class);
        startActivity(intent);
    }

    private void photoChalk() {
        if (!chalkPhotosTR.isEnabled())
            return;

        ChalkVehicle chalk = TPApp.createNewChalk();
        TPApp.setActiveChalk(chalk);

        Intent i = new Intent();
        i.setClass(MainActivity.this, PhotosChalkActivity.class);
        startActivity(i);
    }

    private void userSettings() {
        Intent i = new Intent();
        i.setClass(this, UserSettingsActivity.class);
        startActivity(i);
    }

    private void writeTicket() {
        //checkTraingingUser();
        if (!writeTicketTR.isEnabled())
            return;
        if (Feature.isFeatureAllowed(Feature.PARK_RECOVERY_DATA)) {
            try {
                if (TicketPictureTemp.getCount() > 0 || TicketCommentTemp.getCount() > 0 || TicketViolationTemp.getCount() > 0 || TicketTemp.getCount() > 0) {
                    new iOSDialogBuilder(this)
                            .setTitle("We detected an interruption while creating the previous ticket. Would you like to recover and resume?")
                            .setPositiveListener("Yes", new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    try {
                                        _isTemData();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeListener("No", new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    Ticket ticket = TPApp.createNewTicket();
                                    TPApp.setActiveTicket(ticket);
                                    Intent i = new Intent();
                                    i.setClass(MainActivity.this, WriteTicketActivity.class);
                                    startActivity(i);
                                    dialog.dismiss();
                                }
                            }).build().show();

                } else {
                    Ticket ticket = TPApp.createNewTicket();
                    TPApp.setActiveTicket(ticket);
                    Intent i = new Intent();
                    i.setClass(MainActivity.this, WriteTicketActivity.class);
                    startActivity(i);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            Ticket ticket = TPApp.createNewTicket();
            TPApp.setActiveTicket(ticket);
            Intent i = new Intent();
            i.setClass(MainActivity.this, WriteTicketActivity.class);
            startActivity(i);
        }

    }

    private void _isTemData() throws Exception {

        Ticket ticket = TPApp.createNewTicket();
        final ArrayList<TicketCommentTemp> ticketCommentTemps = TicketCommentTemp.getTicketCommentTemps();
        final ArrayList<TicketViolationTemp> ticketViolations = TicketViolationTemp.getTicketViolations();
        final ArrayList<TicketPictureTemp> ticketPictureTemps = TicketPictureTemp.getTicketPictureTemps();
        final TicketTemp lastTicket = TicketTemp.getLastTicket();
        ArrayList<TicketComment> atc = new ArrayList<>();
        ArrayList<TicketViolation> arrayListtv = new ArrayList<>();
        ArrayList<TicketPicture> aTp = new ArrayList<>();

        if (ticketPictureTemps != null && ticketPictureTemps.size() > 0) {
            for (int i = 0; i < ticketPictureTemps.size(); i++) {
                final TicketPictureTemp tPt = ticketPictureTemps.get(i);
                TicketPicture tp = new TicketPicture();
                tp.setCustId(tPt.getCustId());
                tp.setS_no(tPt.getS_no());
                tp.setMarkPrint(tPt.getMarkPrint());
                tp.setCitationNumber(ticket.getCitationNumber());
                tp.setDownloadImageUrl(tPt.getImageName());
                tp.setImageName(tPt.getImageName());
                tp.setImageResolution(tPt.getImageResolution());
                tp.setImageSize(tPt.getImageSize());
                tp.setImagePath(tPt.getImagePath());
                tp.setLprImageName(tPt.getLprImageName());
                aTp.add(tp);

            }
            ticket.setTicketPictures(aTp);
        }

        if (ticketViolations != null && ticketViolations.size() > 0) {

            for (int i = 0; i < ticketViolations.size(); i++) {
                final TicketViolationTemp tvt = ticketViolations.get(i);
                TicketViolation tv = new TicketViolation();
                tv.setCustId(tvt.getCustId());
                tv.setFine(tvt.getFine());
                tv.setCitationNumber(ticket.getCitationNumber());
                tv.setViolationCode(tvt.getViolationCode());
                tv.setViolationDesc(tvt.getViolationDesc());
                tv.setViolationDisplay(tvt.getViolationDisplay());
                tv.setTicketViolationId(tvt.getTicketViolationId());
                tv.setViolationId(tvt.getViolationId());
                arrayListtv.add(tv);
            }
            ticket.setTicketViolations(arrayListtv);
        }

        try {
            if (ticketCommentTemps != null && ticketCommentTemps.size() > 0) {
                for (int i = 0; i < ticketCommentTemps.size(); i++) {
                    final TicketCommentTemp tct = ticketCommentTemps.get(i);
                    TicketComment tc = new TicketComment();
                    tc.setCustId(tct.getCustId());
                    tc.setCitationNumber(ticket.getCitationNumber());
                    tc.setAudioFile(tct.getAudioFile());
                    tc.setComment(tct.getComment());
                    tc.setCommentId(tct.getCommentId());
                    tc.setIsPrivate(tct.getIsPrivate());
                    tc.setTicketCommentId(tct.getTicketCommentId());
                    atc.add(tc);
                }
                ticket.setTicketComments(atc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        if (lastTicket != null) {
            ticket.setStateCode(lastTicket.getStateCode());
            ticket.setPlate(lastTicket.getPlate());
            ticket.setVin(lastTicket.getVin());
            ticket.setExpiration(lastTicket.getExpiration());
            ticket.setSpace(lastTicket.getSpace());
            ticket.setMakeCode(lastTicket.getMakeCode());
            ticket.setColorCode(lastTicket.getColorCode());
            ticket.setBodyCode(lastTicket.getBodyCode());
            ticket.setMakeCode(lastTicket.getMakeCode());
            ticket.setPermit(lastTicket.getPermit());
            ticket.setLocation(lastTicket.getLocation());

        }

        TPApp.setActiveTicket(ticket);
        Intent i = new Intent();
        i.setClass(MainActivity.this, WriteTicketActivity.class);
        startActivity(i);
    }

    public void locationChalk(View view) {

        Intent i = new Intent();
        i.setClass(this, LocationChalkActivity.class);
        startActivity(i);
        return;

    }

    public void photoChalk(View view) {
        Intent i = new Intent();
        i.setClass(this, PhotosChalkActivity.class);
        startActivity(i);
        return;
    }

    public void userSettings(View view) {
        Intent i = new Intent();
        i.setClass(this, UserSettingsActivity.class);
        startActivity(i);
        return;
    }

    private void printDisclaimerDialog() {
        try {
            PrintTemplate template = PrintTemplate.getPrintTemplateByName("Disclaimer");
            if (template != null) {
                TicketPrinter.print(this, template.getTemplateData());
            } else {
                Toast.makeText(this, "Not Available.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }

    private void printScofflawDialog() {
        try {
            PrintTemplate template = PrintTemplate.getPrintTemplateByName("Scrofflaw");
            if (template != null) {
                TicketPrinter.print(this, template.getTemplateData());
            } else {
                Toast.makeText(this, "Not Available.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }

    public void showAdminOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Option");
        final CharSequence[] choiceList = {"Duty Logs", // 0
                "Messages", // 1
                "Ticket Logs", // 2
                "Vehicles"}; // 3

        builder.setItems(choiceList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (progressDialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }).setCancelable(true).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showSpecialOptions() {
        if (!Feature.isFeatureAllowed(Feature.SPECIAL_MENU)) {
            Toast.makeText(getApplicationContext(), "This feature is disabled.", Toast.LENGTH_LONG).show();
            return;
        }

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Option");


            List<String> aList = new ArrayList<>();
            aList.add("Special Activity");
            aList.add("View Map");
            if (Feature.isFeatureAllowed("PARK_SPECIAL_MAINTENANCE")) {

                aList.add("Maintenance");
            }
            if (Feature.isFeatureAllowed("PARK_SPECIAL_PRINT_DISCLAIMER")) {

                aList.add("Print Disclaimer");
            }
            if (Feature.isFeatureAllowed("PARK_SPECIAL_AID_CITIZEN")) {

                aList.add("Aid Citizen");
            }
            if (Feature.isFeatureAllowed("PARK_SPECIAL_SCOFFLAW")) {

                aList.add("Print Scofflaw");
            }

            String[] choiceList = aList.toArray(new String[0]);
            builder.setItems(choiceList, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (progressDialog.isShowing()) {
                        dialog.dismiss();
                    }

                    Intent intent = new Intent();
                    //SharedPreferences.Editor editor = mPreferences.edit();

                    if (choiceList[which].equals("Special Activity")) {
                        if (!Feature.isFeatureAllowed(Feature.SPECIAL_ACTIVITY)) {
                            Toast.makeText(getApplicationContext(), "This feature is disabled.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        intent.setClass(MainActivity.this, SpecialActivityActivity.class);
                        startActivity(intent);
                    }
                    if (choiceList[which].equals("View Map")) {
                        intent.setClass(MainActivity.this, SpecilaMapActivity.class);
                        startActivity(intent);
                    }
                    if (choiceList[which].equals("Maintenance")) {
                        intent.setClass(MainActivity.this, MaintenanceLogsActivity.class);
                        startActivity(intent);
                    }
                    if (choiceList[which].equals("Print Disclaimer")) {
                        printDisclaimerDialog();
                    }
                    if (choiceList[which].equals("Aid Citizen")) {

                    }
                    if (choiceList[which].equals("Print Scofflaw")) {
                        printScofflawDialog();
                    }

                }
            }).setCancelable(true).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void showActionsOptions() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Action");

            final CharSequence[] choiceList = {"Reprint Last Ticket", // 0
                    "Take Picture", // 1
                    "Use Same Plate", // 2
                    "Use Same Violation", // 3
                    "Voice Last Ticket", // 4
                    "Make Last Ticket A Driveaway", // 5
                    "Clear Fields", // 6
                    "Advance Paper", // 7
                    "Chalk"}; // 7

            builder.setItems(choiceList, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (progressDialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }).setCancelable(true).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alert = builder.create();
            alert.show();

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
        if (text.contains("BACK") || text.contains("END SHIFT") || text.contains("CLOSE SHIFT") || text.contains("GO BACK")) {
            endShift();
        } else if (text.contains("TICKET") || text.contains("WRITE")) {
            writeTicket();
        } else if (text.contains("PHOTO") || text.contains("PHOTO CHALK")) {
            photoChalk();
        } else if (text.contains("CHALK") || text.contains("NEW CHALK")) {
            chalk();
        } else if (text.contains("CHANGE") || text.contains("CHANGE DUTY")) {
            changeDuty();
        } else if (text.contains("SPECIAL") || text.contains("SHOW SPECIAL MENU")) {
            showSpecialOptions();
        }

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        if (connected) {
            if (isFastConnection) {
                statusIndicatorImageView.setImageResource(R.drawable.green_status_btn_bk);
            } else {
                statusIndicatorImageView.setImageResource(R.drawable.yellow_status_btn);
            }
        } else {
            statusIndicatorImageView.setImageResource(R.drawable.gray_status_btn);
        }
    }

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
                mService = binder.getService();
                mBound = true;
                if (mService != null) {
                    mService.requestLocationUpdates();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    public void switchToTraffic(View view) {
        try {
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.ticketpro.traffic");
            if (LaunchIntent == null) {
                Toast.makeText(this, "TicketPRO Traffic is not installed", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(LaunchIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDeviceId() {
        String deviceId = "";
        try {
            deviceId = "-" + TPApp.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceId;
    }

    @Override
    public void whereIAM(ADLocation loc) {
        //isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
        if (isServiceAvailable) {
            if (isFastConnection) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update your UI component
                        statusIndicatorImageView.setBackgroundResource(R.drawable.green_status_btn_bk);

                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update your UI component
                        statusIndicatorImageView.setBackgroundResource(R.drawable.yellow_status_btn);

                    }
                });

            }
        } else {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Update your UI component
                    statusIndicatorImageView.setBackgroundResource(R.drawable.gray_status_btn);

                }
            });

        }

        if (loc!=null) {
            latitude = String.valueOf(loc.lat);
            longitude = String.valueOf(loc.longi);
            //System.out.println("==========>"+latitude+"  "+ longitude);
            preference.putString("LAT", latitude);
            preference.putString("LONGI", longitude);
            //String[] split = loc.address.split(",");
            gpsAddress = new com.ticketpro.model.Address();
            gpsAddress.setLocation(loc.address);
            gpsAddress.setStreetNumber(loc.streetNumber);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    locationLabel.setText(TPUtility.getFullAddress(gpsAddress));
                }
            });
        }

    }

    private void findLoc() {
        new MyTracker(MainActivity.this, this).track();
        // new GetLocation1(MainActivity.this, this).track();
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

                @RequiresApi(api = Build.VERSION_CODES.M)
                public void onFinish() {
                    countDownTimer1.cancel();
                    countDownTimer.cancel();
                    TPUtility.createTxtFile();
                    finishActivity();

                    log.debug("Call countdown t MainActivity");
                }
            }.start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void finishActivity() {
        try {
            // Close Active User Session and Update Duty Report
            new Thread(() -> {
                isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                ((MainBLProcessor) screenBLProcessor).closeActiveDuty(isServiceAvailable);

            }).start();


            try {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(TPConstant.PREFS_KEY_STICKY_VIOLATIONS, false);
                TPApp.setStickyComment(null);
                TPApp.setStickyViolations(false);
                TPApp.setStickyViolation(null);
                editor.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
            preference.putString("ZoneCode","");
            TPApplication.getInstance().setIpsParkMobile(0);
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(TPConstant.EXTRA_END_SHIFT, true);
            intent.setClass(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            TPUtility.updateFB(this, "logOut", null, log);
            if (mService != null) {
                mService.removeLocationUpdates();
            }
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

                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(MainActivity.this);
                confirmBuilder.setCancelable(false);
                confirmBuilder.setTitle("Alert").setMessage(message).setCancelable(true)
                        .setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                endShift1();
                                dialog.dismiss();
                            }
                        }).setPositiveButton("Continue", (dialog, which) -> {
                            countDownTimer.start();
                            countDownTimer1.cancel();
                            dialog.dismiss();
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
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void run() {
                                    //Looper.prepare();
                                    isServiceAvailable = !Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) || !TPApplication.getInstance().getNetOnOff().equals("Y");
                                    ((MainBLProcessor) screenBLProcessor).closeActiveDuty(isServiceAvailable);
                                }
                            }).start();

                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(TPConstant.EXTRA_END_SHIFT, true);
                            intent.setClass(MainActivity.this, HomeActivity.class);
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
