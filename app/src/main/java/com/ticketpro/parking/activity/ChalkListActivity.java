package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Duration;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.User;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.ChalkBLProcessor;
import com.ticketpro.util.DataUtility;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;
import com.triazine.pulltorefresh.library.PullToRefreshBase;
import com.triazine.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.triazine.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */
public class ChalkListActivity extends BaseActivityImpl {

    final int DATA_ERROR = 0;
    final int DATA_SUCCESSFULL = 1;
    final int ERROR_LOAD = 1;
    final int ERROR_SERVICE = 2;
    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    private TableLayout tableLayout;
    private ArrayList<ChalkVehicle> chalks;
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
    private ArrayList<Long> filteredIds = new ArrayList<Long>();
    private int sortBy = 0;
    private int sortOrder = 0;
    private View tableRow;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ScrollView mScrollView;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if (action.equalsIgnoreCase(TPConstant.LOCAL_BROADCAST_REFRESH_CHALK_LIST)) {
                try {
                    chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPlate(ChalkListActivity.this);
                    initDatagrid();
                } catch (Exception e) {
                    Log.e(TPConstant.TAG, "Error reloading chalk list");
                }
            }
        }
    };

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.chalk_list);
            setLogger(LocationChalkListActivity.class.getName());
            setBLProcessor(new ChalkBLProcessor((TPApplication) getApplicationContext()));
            setActiveScreen(this);

            tableLayout = (TableLayout) findViewById(R.id.location_chalk_4_table_view);
            expiredTextView = (TextView) findViewById(R.id.chalk_list_expired_textview);
            expiredFilterChk = (CheckBox) findViewById(R.id.chalk_expired_filter_chk);

            zoneFilter = (Spinner) findViewById(R.id.chalk_zone_filter_spinner);
            locationFilter = (Spinner) findViewById(R.id.chalk_location_filter_spinner);
            userFilter = (Spinner) findViewById(R.id.chalk_user_filter_spinner);

            mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
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

                    for (ChalkVehicle chalk : chalks) {
                        if (chalk.getDurationCode() != null && !zones.contains(chalk.getDurationCode())) {
                            zones.add(chalk.getDurationCode());
                        }

                        String fullLocation = TPUtility.getFullAddress(chalk);
                        if (!fullLocation.equals("") && !locations.contains(fullLocation)) {
                            locations.add(fullLocation);
                        }

                        User userInfo = User.getUserInfo(chalk.getUserId() == -1 ? chalk.getDeviceId() : chalk.getUserId());
                        if (userInfo != null && !users.contains(userInfo.getUsername())) {
                            users.add(userInfo.getUsername());
                        }
                    }

                    if (zones != null) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChalkListActivity.this, android.R.layout.simple_spinner_item, zones);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        zoneFilter.setAdapter(dataAdapter);
                        zoneFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                selectedZone = zones.get(position);
                                applyFilter();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {

                            }
                        });
                    }

                    if (locations != null) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChalkListActivity.this, android.R.layout.simple_spinner_item, locations);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        locationFilter.setAdapter(dataAdapter);
                        locationFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                selectedLocation = locations.get(position);
                                applyFilter();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {

                            }
                        });
                    }

                    if (users != null) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChalkListActivity.this, android.R.layout.simple_spinner_item, users);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userFilter.setAdapter(dataAdapter);
                        userFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                selectedUser = users.get(position);
                                applyFilter();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {

                            }
                        });
                    }

                    initDatagrid();

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                }
            };

            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (msg.what == 0) {
                        bindDataAtLoadingTime(1);
                    }
                }
            };

            refreshHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    removeChalk();
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

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    //OnCreate End Here
    private void applyFilter() {
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

    @Override
    protected void onResume() {
        super.onResume();
        removeChalk();
        initDatagrid();
    }

    void removeChalk() {
        try {
            if (chalks != null && chalks.size() > 0) {
                for (int i = 0; i < chalks.size(); i++) {
                    ChalkVehicle chalkVehicle = chalks.get(i);
                    try {
                        boolean b = Ticket.chalkSyncOrNot(chalkVehicle.getChalkId());
                        if (b) {
                            if (!chalkVehicle.getChalkType().equals(TPConstant.CHALK_TYPE_LOCATION)) {
                                ChalkVehicle.removeChalkById(chalkVehicle.getChalkId(), "");
                                initDatagrid();
                            }
                        } else {
                            Log.d("ChalkListActivity", "Ticket not issue yet.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initDatagrid() {
        try {
            if (chalks == null || tableLayout == null)
                return;

            tableLayout.removeAllViews();
            filteredIds.clear();
            LayoutInflater inflater = LayoutInflater.from(this);

            //adding Header
            View headerRow = inflater.inflate(R.layout.table_row_4_column_view, null);

            TextView plateColumn = ((TextView) headerRow.findViewById(R.id.tr_header1));
            TextView timeColumn = ((TextView) headerRow.findViewById(R.id.tr_header2));
            TextView zoneColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
            TextView elapsedColumn = ((TextView) headerRow.findViewById(R.id.tr_header4));

            plateColumn.setText("Plate/VIN");
            plateColumn.setClickable(true);
            plateColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(chalks, new ChalkVehicle.PlateComparator());

                    //Update Sorting Order
                    if (sortBy != 1) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(chalks);
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
                    Collections.sort(chalks, new ChalkVehicle.DateComparator());

                    //Update Sorting Order
                    if (sortBy != 2) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(chalks);
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
                    Collections.sort(chalks, new ChalkVehicle.ZoneComparator());

                    //Update Sorting Order
                    if (sortBy != 3) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(chalks);
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
                    Collections.sort(chalks, new ChalkVehicle.DateComparator());

                    //Update Sorting Order
                    if (sortBy != 4) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(chalks);
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
                        plateColumn.setText("Plate/VIN \u25BC");
                    else
                        plateColumn.setText("Plate/VIN \u25B2");
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
            for (ChalkVehicle chalk : chalks) {
                if (expiredFilterChk.isChecked() && !chalk.isExpired())
                    continue;

                String fullLocation = TPUtility.getFullAddress(chalk);
                if (!selectedLocation.equals("All Locations")) {
                    if (!selectedLocation.equals(fullLocation)) {
                        continue;
                    }
                }

                if (!selectedZone.equals("All Zones")) {
                    if (chalk.getDurationCode() != null && !selectedZone.equals(chalk.getDurationCode())) {
                        continue;
                    }
                }

                if (!selectedUser.equals("All Users")) {
                    User userInfo = User.getUserInfo(chalk.getUserId() == -1 ? chalk.getDeviceId() : chalk.getUserId());
                    if (userInfo != null && !userInfo.getUsername().equalsIgnoreCase(selectedUser)) {
                        continue;
                    }
                }

                tableRow = inflater.inflate(R.layout.table_row_4_column_view, null);

                String plate = "NA";
                if (chalk.getPlate() != null && !chalk.getPlate().equals("")) {
                    plate = chalk.getPlate();
                } else if (chalk.getVin() != null && !chalk.getVin().equals("")) {
                    plate = chalk.getVin();
                }

                ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(plate);
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(DateUtil.getStringFromDateShortYear2(chalk.getChalkDate()));

                String duration = Duration.getDurationTitleById(chalk.getDurationId());
                ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(duration == null ? "NA" : duration);

                long diff = (new Date().getTime() - chalk.getChalkDate().getTime());
                long expTime = (diff / 1000) / 60;
                if (expTime > 0) {
                    if (!chalk.isExpired()) {
                        chalk.setIsExpired("Y");
                        ChalkVehicle.insertChalkVehicle(chalk).subscribe();
                        /*DatabaseHelper.getInstance().openWritableDatabase();
                        DatabaseHelper.getInstance().insertOrReplace(chalk.getContentValues(), "chalk_vehicles");
                        DatabaseHelper.getInstance().closeWritableDb();*/
                    }

                    ImageView actionIcon = ((ImageView) tableRow.findViewById(R.id.tr_action));
                    actionIcon.setBackgroundResource(R.drawable.write_ticket);
                    actionIcon.setVisibility(View.VISIBLE);
                    actionIcon.setTag(R.id.ChalkId, chalk.getChalkId());
                    actionIcon.setOnClickListener(v -> {
                        long chalkId = (Long) v.getTag(R.id.ChalkId);
                        try {
                            ChalkVehicle activeChalk = ChalkVehicle.getChalkVehicleById(chalkId);
                            if (activeChalk != null) {

                                if (!Ticket.chalkTicketIssue(activeChalk.getChalkId())) {
                                    Ticket ticket = TPApp.createTicketForChalk(activeChalk);
                                    TPApp.setActiveTicket(ticket);

                                    Intent i1 = new Intent();
                                    i1.setClass(ChalkListActivity.this, WriteTicketActivity.class);
                                    i1.putExtra("ChalkId", chalkId);
                                    startActivityForResult(i1, 0);
                                } else {
                                    new iOSDialogBuilder(this)
                                            .setSubtitle("Chalk already issued.")
                                            .setPositiveListener("OK", new iOSDialogClickListener() {
                                                @Override
                                                public void onClick(iOSDialog dialog) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .build().show();
                                }
                            }
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    });
                }

                ((TextView) tableRow.findViewById(R.id.tr_header4)).setText(TPUtility.getHrsMinSecs(diff));
                ((TextView) tableRow.findViewById(R.id.tr_header4)).setGravity(Gravity.CENTER);

                if ((i % 2) == 0) {
                    tableRow.setBackgroundResource(R.drawable.tablerow_even);
                } else {
                    tableRow.setBackgroundResource(R.drawable.tablerow_odd);
                }

                try {
                    int mins = Duration.getDurationMinsById(chalk.getDurationId(), this);
                    long expTimeinMilis = diff - mins * 60000;
                    if (expTimeinMilis > 0) {
                        tableRow.setBackgroundResource(R.drawable.tablerow_expired);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                tableRow.setTag(R.id.ChalkId, chalk.getChalkId());
                tableRow.setTag(R.id.ListIndex, i);

                tableRow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long chalkId = ((Long) v.getTag(R.id.ChalkId)).longValue();
                        int listIndex = ((Integer) v.getTag(R.id.ListIndex)).intValue();

                        Intent i = new Intent();
                        i.setClass(ChalkListActivity.this, ChalkedVehicleDetailsActivity.class);
                        i.putExtra("ChalkId", chalkId);
                        i.putExtra("ListIndex", listIndex);
                        startActivityForResult(i, 0);
                        return;
                    }
                });

                if (chalk.isExpired())
                    expiredCount++;

                TPApp.chalkList.add(chalk);
                filteredIds.add(Long.valueOf(chalk.getChalkId()));
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
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(TPConstant.LOCAL_BROADCAST_EVENT));

        bindDataAtLoadingTime(0);

        new GetDataTask().execute();
    }

    public void bindDataAtLoadingTime(final int reloadCount) {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread() {
            public void run() {
                try {
                    chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPlate(ChalkListActivity.this);
                    locations = new ArrayList<String>();
                    zones = new ArrayList<String>();
                    users = new ArrayList<String>();

                    locations.add(0, "All Locations");
                    zones.add(0, "All Zones");
                    users.add(0, "All Users");

                    dataLoadingHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                    errorHandler.sendEmptyMessage(reloadCount);
                }
            }
        }.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPlate(ChalkListActivity.this);
                initDatagrid();
            } catch (Exception e) {
                Log.e(TPConstant.TAG, "Error reloading chalk list");
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backAction(null);
    }

    public void backAction(View view) {
        refreshTimer.cancel();
        finish();
    }

    public void mapViewAction(View view) {
        try {
            Intent intent = new Intent();
            if (selectedLocation != null && !selectedLocation.equals("All Locations"))
                intent.putExtra("SelectedLocation", selectedLocation);

            if (selectedZone != null && !selectedZone.equals("All Zones"))
                intent.putExtra("SelectedZone", selectedZone);

            if (selectedUser != null && !selectedUser.equals("All Users"))
                intent.putExtra("SelectedUser", selectedUser);

            intent.setClass(this, ChalkVehicleMapViewActivity.class);
            startActivityForResult(intent, 0);

        } catch (ActivityNotFoundException e) {
            (Toast.makeText(this, "Google Map Services not available on your device.", Toast.LENGTH_LONG)).show();

        } catch (NoClassDefFoundError e) {
            (Toast.makeText(this, "Google Map Services not available on your device.", Toast.LENGTH_LONG)).show();
        }
    }

    public void removeAction(View view) {
        return;
    }

    public void clearAction(View view) {
        if (filteredIds.size() == 0)
            return;
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

                            if (filteredIds.size() == 0)
                                return;

                            if (Feature.isFeatureAllowed(Feature.CHALK_REMOVE_REASION)) {
                                _selectOption();

                            } else {

                                for (Long chalkId : filteredIds) {
                                    ChalkVehicle.removeChalkById(chalkId, "");
                                }
                            }

                            chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPlate(ChalkListActivity.this);

                        } catch (Exception ae) {
                            log.error(ae);
                        }

                        initDatagrid();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void _selectOption() {
        String featureValue = Feature.getFeatureValue(Feature.CHALK_REMOVE_REASION);
        final String[] split = featureValue.split(";");
        final AlertDialog.Builder myDialog =
                new AlertDialog.Builder(ChalkListActivity.this);
        myDialog.setTitle("Select Reason");
        myDialog.setItems(split, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = split[which];
                try {
                    for (Long chalkId : filteredIds) {
                        ChalkVehicle.removeChalkById(chalkId, item);
                    }
                    chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPlate(ChalkListActivity.this);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        myDialog.setPositiveButton("Add New Comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                _openEditDialog();

            }
        });

        myDialog.show();
    }

    private void _openEditDialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ChalkListActivity.this);
        View mView = this.getLayoutInflater().inflate(R.layout.chalk_remove_dialog, null);
        final EditText inputText = (EditText) mView.findViewById(R.id.add_comment_input_dialog_text_field);

        final Button mCancle = (Button) mView.findViewById(R.id.add_comment_input_dialog_cancel_btn);
        final Button mOk = (Button) mView.findViewById(R.id.add_comment_input_dialog_enter_btn);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(inputText.getText().toString())) {
                    try {
                        TPUtility.hideKeyboard(ChalkListActivity.this);
                        for (Long chalkId : filteredIds) {
                            ChalkVehicle.removeChalkById(chalkId, inputText.getText().toString());
                        }
                        chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPlate(ChalkListActivity.this);

                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    TPUtility.hideKeyboard(ChalkListActivity.this);
                    dialog.dismiss();
                }

            }
        });

        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TPUtility.hideKeyboard(ChalkListActivity.this);
                dialog.dismiss();
            }
        });
        TPUtility.showSoftKeyboard(ChalkListActivity.this, inputText);
    }


    @Override
    public void handleVoiceInput(String text) {
        if (text == null) return;

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        if (text.contains("BACK") || text.contains("GO BACK") || text.contains("CLOSE")) {
            backAction(null);
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

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                if (chalks != null) {
                    chalks.clear();
                    Thread.sleep(100);
                }

                DataUtility.checkExpiredChalks(ChalkListActivity.this);

                chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByPlate(ChalkListActivity.this);

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
