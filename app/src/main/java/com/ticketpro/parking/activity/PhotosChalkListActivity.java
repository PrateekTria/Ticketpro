package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.lpr.lpr.ImenseLicenseServer;
import com.ticketpro.model.ALPRChalk;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.User;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;
import com.triazine.pulltorefresh.library.PullToRefreshBase;
import com.triazine.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.triazine.pulltorefresh.library.PullToRefreshScrollView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.ticketpro.parking.activity.WriteTicketActivity.debug;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class PhotosChalkListActivity extends BaseActivityImpl {

    private final static int PT_INVALID_INVOCATION = 99;
    private final static int PT_LICENSE_MISSING_OR_INVALID = 100;
    private final static int PT_ANPR_NOTONWHITELIST = 101;
    private final static int PT_ANPR_PERMITEXPIRED = 102;
    private final static int PT_ANPR_SCANTIMEOUT = 103;
    private static final int REQUESTCODE = 55;
    public static String tag = "PhotoChalkList"; //tag for debugging
    public static String licenseKey = null;

    final int DATA_ERROR = 0;
    final int DATA_SUCCESSFULL = 1;
    final int ERROR_LOAD = 1;
    final int ERROR_SERVICE = 2;
    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    // pull to refresh
    View tableRow;
    PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;
    private TableLayout tableLayout;
    // private ArrayList<ChalkVehicle> chalks;
    private ArrayList<ALPRChalk> alprChalks;
    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private Handler errorHandler;
    private Handler refreshHandler;
    private TextView expiredTextView;
    private Spinner zoneFilter;
    private Spinner locationFilter;
    private Spinner userFilter;
    private List<String> zones;
    private List<String> locations;
    private List<String> users;
    private CheckBox expiredFilterChk;
    private String selectedLocation = "All Locations";
    private String selectedZone = "All Zones";
    private String selectedUser = "All Users";
    private Timer refreshTimer = new Timer();
    private int sortBy = 0;
    private int sortOrder = 0;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if (action.equalsIgnoreCase(TPConstant.LOCAL_BROADCAST_REFRESH_CHALK_LIST)) {
                try {
                    //chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPhoto(PhotosChalkListActivity.this);
                    alprChalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPhotoALPR(PhotosChalkListActivity.this);
                    initDatagrid();
                } catch (Exception e) {
                    Log.e(TPConstant.TAG, "Error reloading chalk list");
                }
            }
        }
    };
    private boolean lpr;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.photo_chalk_list);
            setLogger(PhotosChalkListActivity.class.getName());
            setBLProcessor(new ChalkBLProcessor((TPApplication) getApplicationContext()));
            setActiveScreen(this);

            tableLayout = (TableLayout) findViewById(R.id.photo_chalk_3_table_view);
            expiredTextView = (TextView) findViewById(R.id.chalk_expired_textview);
            expiredFilterChk = (CheckBox) findViewById(R.id.chalk_expired_filter_chk);

            zoneFilter = (Spinner) findViewById(R.id.chalk_zone_filter_spinner);
            locationFilter = (Spinner) findViewById(R.id.chalk_location_filter_spinner);
            userFilter = (Spinner) findViewById(R.id.chalk_user_filter_spinner);

            mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview_photo);
            mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

                @Override
                public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                    new GetDataTask().execute();
                }
            });

            mScrollView = mPullRefreshScrollView.getRefreshableView();

            expiredFilterChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    applyFilter();
                }
            });

            dataLoadingHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (msg.what == DATA_SUCCESSFULL) {
                        for (ALPRChalk chalk : alprChalks) {
                            if (chalk.getChalkDurationCode() != null && !zones.contains(chalk.getChalkDurationCode())) {
                                zones.add(chalk.getChalkDurationCode());
                            }

                            String fullLocation = chalk.getChalkLocation();
                            if (!fullLocation.equals("") && !locations.contains(fullLocation)) {
                                locations.add(fullLocation);
                            }

                            User userInfo = User
                                    .getUserInfo(chalk.getUserid() == -1 ? chalk.getDeviceId() : chalk.getUserid());
                            if (userInfo != null && !users.contains(userInfo.getUsername())) {
                                users.add(userInfo.getUsername());
                            }
                        }

                        setZoneFilter(zones,false);

                        setLocationFilter(locations,false);

                        setuserFilter(users);

                        initDatagrid();
                    }

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

                    switch (msg.what) {
                        case 0:
                            bindDataAtLoadingTime(ERROR_LOAD);
                            break;

                        case ERROR_LOAD:
                            Toast.makeText(getBaseContext(), "Failed to load chalks. Please try again.", Toast.LENGTH_SHORT)
                                    .show();
                            break;

                        case ERROR_SERVICE:
                            Toast.makeText(getBaseContext(), TPConstant.GENERIC_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            refreshHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    initDatagrid();
                }
            };

            TimerTask task = new TimerTask() {
                public void run() {
                    refreshHandler.sendEmptyMessage(1);
                }
            };

            selectedUser = TPApp.getUserInfo().getUsername();

            bindDataAtLoadingTime();
            // refreshTimer.schedule(task, 5000, 30*1000);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void setuserFilter(final List<String> users) {
        if (users != null) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PhotosChalkListActivity.this,
                    android.R.layout.simple_spinner_item, users);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            userFilter.setAdapter(dataAdapter);
            userFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                           int position, long id) {
                    selectedUser = users.get(position);
                    applyFilter();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }
            });
        }
    }

    private void setLocationFilter(final List<String> locations, final boolean applyFilter) {
        if (locations != null && locationFilter != null) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PhotosChalkListActivity.this,
                    android.R.layout.simple_spinner_item, locations);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationFilter.setAdapter(dataAdapter);
            locationFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                           int position, long id) {
                    selectedLocation = locations.get(position);
                    if (!applyFilter) {
                        if (!selectedLocation.equalsIgnoreCase("All Locations")) {
                            zones = new ArrayList<>();
                            zones.add(0,"All Zones");
                            for (ALPRChalk alprChalk : alprChalks) {
                                if (alprChalk.getChalkLocation().equalsIgnoreCase(selectedLocation)) {
                                    if (!zones.contains(alprChalk.getChalkDurationCode())) {
                                        zones.add(alprChalk.getChalkDurationCode());
                                    }
                                }
                            }
                            setZoneFilter(zones,true);
                            return;
                        }
                    }
                    applyFilter();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }
            });
        }
    }

    private void applyFilter() {
        initDatagrid();
    }

    private void setZoneFilter(final List<String> zones, final boolean applyFilter) {
        if (zones != null && zoneFilter != null) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(PhotosChalkListActivity.this,
                    android.R.layout.simple_spinner_item, zones);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            zoneFilter.setAdapter(dataAdapter);
            zoneFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                           int position, long id) {
                    selectedZone = zones.get(position);
                    if (!applyFilter) {
                        if (!selectedZone.equalsIgnoreCase("All Zones")) {
                            locations = new ArrayList<>();
                            locations.add(0,"All Locations");
                            for (ALPRChalk alprChalk : alprChalks) {
                                if (alprChalk.getChalkDurationCode().equalsIgnoreCase(selectedZone)) {
                                    if (!locations.contains(alprChalk.getChalkLocation())) {
                                        locations.add(alprChalk.getChalkLocation());
                                    }
                                }
                            }
                            setLocationFilter(locations,true);
                            return;
                        }
                    }
                    applyFilter();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatagrid();
    }

    public void resetFilters(View view) {
        selectedLocation = "All Locations";
        selectedZone = "All Zones";
        selectedUser = "All Users";
        locationFilter.setSelection(0);
        zoneFilter.setSelection(0);
        userFilter.setSelection(0);

        applyFilter();
    }

    private void initDatagrid() {
        try {
            if (alprChalks == null || tableLayout == null)
                return;

            tableLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

            // adding Header
            View headerRow = inflater.inflate(R.layout.table_row_4_column_view, null);

            TextView locationColumn = ((TextView) headerRow.findViewById(R.id.tr_header1));
            TextView timeColumn = ((TextView) headerRow.findViewById(R.id.tr_header2));
            TextView zoneColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
            TextView elapsedColumn = ((TextView) headerRow.findViewById(R.id.tr_header4));

            locationColumn.setText("Plate");
            locationColumn.setClickable(true);
            locationColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(alprChalks, new ALPRChalk.PlateComparator());

                    // Update Sorting Order
                    if (sortBy != 1) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        //	Collections.reverse(chalks);
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 1;
                    initDatagrid();
                }
            });

            timeColumn.setText("Time");
            timeColumn.setClickable(true);
            timeColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(alprChalks, new ALPRChalk.DateComparator());

                    // Update Sorting Order
                    if (sortBy != 2) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(alprChalks);
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 2;
                    initDatagrid();
                }
            });

            zoneColumn.setText("Zone");
            zoneColumn.setClickable(true);
            zoneColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(alprChalks, new ALPRChalk.ZoneComparator());

                    // Update Sorting Order
                    if (sortBy != 3) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(alprChalks);
                    } else {
                        sortOrder = ASC_ORDER;
                    }
                    sortBy = 3;
                    initDatagrid();
                }
            });

            elapsedColumn.setText("Elapsed");
            elapsedColumn.setGravity(Gravity.CENTER);
            elapsedColumn.setClickable(true);
            elapsedColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(alprChalks, new ALPRChalk.DateComparator());
                    // Update Sorting Order
                    if (sortBy != 4) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(alprChalks);
                    } else {
                        sortOrder = ASC_ORDER;
                    }
                    sortBy = 4;
                    initDatagrid();
                }
            });

            switch (sortBy) {
                case 1:
                    if (sortOrder == ASC_ORDER)
                        locationColumn.setText("Plate \u25BC");
                    else
                        locationColumn.setText("Plate \u25B2");
                    break;
                case 2:
                    if (sortOrder == ASC_ORDER)
                        timeColumn.setText("Time \u25BC");
                    else
                        timeColumn.setText("Time \u25B2");
                    break;
                case 3:
                    if (sortOrder == ASC_ORDER)
                        zoneColumn.setText("Zone \u25BC");
                    else
                        zoneColumn.setText("Zone \u25B2");
                    break;
                case 4:
                    if (sortOrder == ASC_ORDER)
                        elapsedColumn.setText("Elasped \u25BC");
                    else
                        elapsedColumn.setText("Elasped \u25B2");
                    break;
            }

            tableLayout.addView(headerRow);
            TPApp.chalkList.clear();

            int i = 0, expiredCount = 0;
            for (ALPRChalk alprChalk : alprChalks) {
                //for (ChalkVehicle chalk : chalks) {
                if (expiredFilterChk.isChecked() && !alprChalk.isExpired())
                    continue;

                String fullLocation = alprChalk.getChalkLocation();
                //String fullLocation = TPUtility.getFullAddress(chalk);
                if (!selectedLocation.equals("All Locations")) {
                    if (!selectedLocation.equals(fullLocation)) {
                        continue;
                    }
                }

                if (!selectedZone.equals("All Zones")) {
                    if (alprChalk.getChalkDurationCode() != null && !selectedZone.equals(alprChalk.getChalkDurationCode())) {
                        continue;
                    }
                }

                if (!selectedUser.equals("All Users")) {
                    User userInfo = User.getUserInfo(alprChalk.getUserid() == -1 ? alprChalk.getDeviceId() : alprChalk.getUserid());
                    if (userInfo != null && !userInfo.getUsername().equalsIgnoreCase(selectedUser)) {
                        continue;
                    }
                }

                tableRow = inflater.inflate(R.layout.table_row_4_column_view, null);
                ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(alprChalk.getPlate());
				/*((TextView) tableRow.findViewById(R.id.tr_header2))
						.setText(DateUtil.getStringFromDateShortYear2(chalk.getChalkDate()));*/
                ((TextView) tableRow.findViewById(R.id.tr_header2))
                        .setText(DateUtil.getStringFromDateShortYear2(alprChalk.getFirstDateTime()));
                ((TextView) tableRow.findViewById(R.id.tr_header3))
                        .setText(alprChalk.getChalkDurationCode() == null ? "NA" : alprChalk.getChalkDurationCode());

                int mins = Duration.getDurationMinsById(alprChalk.getChalkDurationId(), this);
                long diff = (new Date().getTime() - alprChalk.getFirstDateTime().getTime());
                long expTime = (diff / 1000) / 60;

                if (expTime > mins) {
                    if (!alprChalk.isExpired()) {
                        alprChalk.setIsExpired("Y");
                        ContentValues values = new ContentValues();
                        values.put("is_expired", "Y");
                        ALPRChalk.updateChalkExpired(alprChalk.getPlate(), "Y");
                    } else {
                        alprChalk.setIsExpired("Y");
                    }

                    ImageView actionIcon = ((ImageView) tableRow.findViewById(R.id.tr_action));
                    actionIcon.setBackgroundResource(R.drawable.write_ticket);
                    actionIcon.setVisibility(View.VISIBLE);
                    actionIcon.setTag(R.id.ChalkId, alprChalk.getChalkId());
                    actionIcon.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long chalkId = (Long) v.getTag(R.id.ChalkId);
                            try {
                                ALPRChalk activeChalk = ALPRChalk.getChalkVehicleById(chalkId);
                                if (activeChalk != null) {
                                    Ticket ticket = TPApp.createTicketForChalk(activeChalk);
                                    TPApp.setActiveTicket(ticket);
                                    Intent i = new Intent();
                                    i.setClass(PhotosChalkListActivity.this, WriteTicketActivity.class);
                                    i.putExtra("ChalkId", chalkId);
                                    startActivityForResult(i, 0);
                                }
                            } catch (Exception e) {
                                log.error(TPUtility.getPrintStackTrace(e));
                            }
                        }
                    });
                }

                ((TextView) tableRow.findViewById(R.id.tr_header4)).setText(TPUtility.getHrsMinSecs(diff));
                ((TextView) tableRow.findViewById(R.id.tr_header4)).setGravity(Gravity.CENTER);

                if ((i % 2) == 0)
                    tableRow.setBackgroundResource(R.drawable.tablerow_even);
                else
                    tableRow.setBackgroundResource(R.drawable.tablerow_odd);

                if (expTime > mins)
                    tableRow.setBackgroundResource(R.drawable.tablerow_expired);

                long expTimeinMilis = diff - mins * 60000;
                if (expTimeinMilis > 0)
                    tableRow.setBackgroundResource(R.drawable.tablerow_expired);

                tableRow.setTag(R.id.ChalkId, alprChalk.getChalkId());
                tableRow.setTag(R.id.ListIndex, i);

                tableRow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long chalkId = (Long) v.getTag(R.id.ChalkId);
                        int listIndex = (Integer) v.getTag(R.id.ListIndex);

                        Intent i = new Intent();
                        i.setClass(PhotosChalkListActivity.this, PhotoChalkDetailsActivity.class);
                        i.putExtra("ChalkId", chalkId);
                        i.putExtra("ListIndex", listIndex);
                        startActivityForResult(i, 0);
                        return;
                    }
                });

                /*if (chalk.isExpired())
                    expiredCount++;
*/
                //              TPApp.chalkList.add(chalk);
                tableLayout.addView(tableRow);
                i++;
            }

            expiredTextView.setText("Expired(" + expiredCount + ")");

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(TPConstant.LOCAL_BROADCAST_EVENT));
        bindDataAtLoadingTime(0);
    }

    public void bindDataAtLoadingTime(final int reloadCount) {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread() {
            public void run() {
                try {
                    // chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPhoto(PhotosChalkListActivity.this);
                    alprChalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPhotoALPR(PhotosChalkListActivity.this);
                    locations = new ArrayList<String>();
                    zones = new ArrayList<String>();
                    users = new ArrayList<String>();

                    locations.add(0, "All Locations");
                    zones.add(0, "All Zones");
                    users.add(0, "All Users");

                    dataLoadingHandler.sendEmptyMessage(DATA_SUCCESSFULL);
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                    errorHandler.sendEmptyMessage(reloadCount);
                }
            }
        }.start();
    }

    private void showDialogForLastChalkedVehicle() {

        final ALPRChalk alprChalk = ALPRChalk.getLastChalkedVehicle();
        if (alprChalk != null) {
            int mins = Duration.getDurationMinsById(alprChalk.getChalkDurationId(), PhotosChalkListActivity.this);
            long diff = (new Date().getTime() - alprChalk.getFirstDateTime().getTime());
            long expTime = (diff / 1000) / 60;
            if (expTime > mins) {
                String time = TPUtility.getHrsMinSecs(diff);
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(PhotosChalkListActivity.this);
                confirmBuilder
                        .setTitle("Alert")
                        .setMessage(alprChalk.getPlate() + " has exceeded the " + alprChalk.getChalkDurationCode() + " zone. Do you want to write a ticket for the vehicle?").setCancelable(true)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // ignorePlateValidation = true;

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Ticket ticket = TPApp.createTicketForChalk(alprChalk);

                                TPApp.setActiveTicket(ticket);
                                Intent i = new Intent();
                                i.setClass(PhotosChalkListActivity.this, WriteTicketActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (lpr) {
            showDialogForLastChalkedVehicle();
            lpr = false;
        }
        try {
            int returnMessage = 0;
            if (data != null) returnMessage = data.getExtras().getInt("message");

            //if (debug > 0)
            //Log.d(tag, "onActivityResult:  requestCode=" + requestCode + ", resultCode=" + resultCode + ", data=" + data + ", ptsIntent=" + ptsIntent + ", returnMessage=" + returnMessage);
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
                final PhotosChalkListActivity caller = this;
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
        if (resultCode == RESULT_OK) {
            progressDialog = ProgressDialog.show(this, "", "Loading...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPhoto(PhotosChalkListActivity.this);
                        alprChalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPhotoALPR(PhotosChalkListActivity.this);
                        dataLoadingHandler.sendEmptyMessage(DATA_SUCCESSFULL);
                    } catch (Exception e) {
                        errorHandler.sendEmptyMessage(ERROR_LOAD);
                        Log.e(TPConstant.TAG, "Error reloading chalk list");
                    }
                }
            }).start();
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void showMap(View view) {
        try {
            Intent i = new Intent();
            if (selectedLocation != null && !selectedLocation.equals("All Locations"))
                i.putExtra("SelectedLocation", selectedLocation);

            if (selectedZone != null && !selectedZone.equals("All Zones"))
                i.putExtra("SelectedZone", selectedZone);

            if (selectedUser != null && !selectedUser.equals("All Users"))
                i.putExtra("SelectedUser", selectedUser);

            i.setClass(this, PhotoChalkedMapViewActivity.class);
            startActivityForResult(i, 0);

        } catch (ActivityNotFoundException e) {
            (Toast.makeText(this, "Google Map Services not available on your device.", Toast.LENGTH_LONG)).show();

        } catch (NoClassDefFoundError e) {
            (Toast.makeText(this, "Google Map Services not available on your device.", Toast.LENGTH_LONG)).show();
        }
    }

    public void backAction(View view) {
        refreshTimer.cancel();
        finish();
    }

    @Override
    public void onBackPressed() {
        refreshTimer.cancel();
        finish();
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

    public void rescanAction(View view) {

        if ("All Locations".equalsIgnoreCase((String) locationFilter.getSelectedItem())) {

            displayErrorMessage("Please enter location.");
            return;
        }
        if ("All Zones".equalsIgnoreCase((String) zoneFilter.getSelectedItem())) {
            displayErrorMessage("Please select zone.");
            return;
        }


        try {
            File file = new File(TPUtility.getALPRImagesFolder(), "chalkList.csv");
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Feature.isFeatureAllowed(Feature.ALPR_ADMINLAUNCH)) {
            launchPTS(true);
        } else {
            launchPTS(false);
        }
    }

    void launchPTS(boolean admin) {
        TPUtility.exportCSV((String) zoneFilter.getSelectedItem(), (String) locationFilter.getSelectedItem());

        //obtain an Intent to launch ANPR/ALPR Platform Server
        try {
            Intent ptsIntent = new Intent();
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
            int mins = Duration.getDurationMinsByName((String) zoneFilter.getSelectedItem(), PhotosChalkListActivity.this);
            ptsIntent.putExtra("preferences_warnAfterNmins", String.valueOf(mins));
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

            if (debug > 0) {
                Log.e(tag, "launchPTS Error: " + err);
                err.printStackTrace();
            }
            Toast.makeText(PhotosChalkListActivity.this, "ALPR PlatformServerUS not found: please install it", Toast.LENGTH_LONG).show();
        }

    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                if (alprChalks != null) {
                    alprChalks.clear();
                    Thread.sleep(100);
                }
                // chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPhoto(PhotosChalkListActivity.this);
                alprChalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPhotoALPR(PhotosChalkListActivity.this);
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshScrollView.onRefreshComplete();
            try {
                initDatagrid();
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }

            super.onPostExecute(result);
        }
    }

}
