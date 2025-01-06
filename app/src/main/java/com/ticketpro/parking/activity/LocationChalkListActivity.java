package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ticketpro.model.ChalkVehicle;
import com.ticketpro.model.Duration;
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

public class LocationChalkListActivity extends BaseActivityImpl {

    final int REQUEST_VIEW_CHALK = 1;
    final int REQUEST_VIEW_MAP = 2;
    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    //pull to refresh
    View tableRow;
    PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;
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
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if (action.equalsIgnoreCase(TPConstant.LOCAL_BROADCAST_REFRESH_CHALK_LIST)) {
                try {
                    chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByLocation(LocationChalkListActivity.this);
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
            setContentView(R.layout.location_chalk_list);
            setLogger(LocationChalkListActivity.class.getName());
            setBLProcessor(new ChalkBLProcessor((TPApplication) getApplicationContext()));
            setActiveScreen(this);

            tableLayout = (TableLayout) findViewById(R.id.location_chalk_4_table_view);
            expiredTextView = (TextView) findViewById(R.id.chalk_expired_textview);
            expiredFilterChk = (CheckBox) findViewById(R.id.chalk_expired_filter_chk);

            zoneFilter = (Spinner) findViewById(R.id.chalk_zone_filter_spinner);
            locationFilter = (Spinner) findViewById(R.id.chalk_location_filter_spinner);
            userFilter = (Spinner) findViewById(R.id.chalk_user_filter_spinner);

            mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview_loc);
            mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

                @Override
                public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                    new GetDataTask().execute();
                }
            });


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
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(LocationChalkListActivity.this, android.R.layout.simple_spinner_item, zones);
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
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(LocationChalkListActivity.this, android.R.layout.simple_spinner_item, locations);
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
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(LocationChalkListActivity.this, android.R.layout.simple_spinner_item, users);
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
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    //try to reload the datagrid
                    if (msg.what == 0)
                        bindDataAtLoadingTime(1);
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

            bindDataAtLoadingTime(0);
            // refreshTimer.schedule(task, 5000, 30*1000);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

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
        initDatagrid();
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

            TextView locationColumn = ((TextView) headerRow.findViewById(R.id.tr_header1));
            TextView timeColumn = ((TextView) headerRow.findViewById(R.id.tr_header2));
            TextView zoneColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
            TextView elapsedColumn = ((TextView) headerRow.findViewById(R.id.tr_header4));

            locationColumn.setText("Location");
            locationColumn.setClickable(true);
            locationColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(chalks, new ChalkVehicle.LocationComparator());

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
                        locationColumn.setText("Location \u25BC");
                    else
                        locationColumn.setText("Location \u25B2");
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

                ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(fullLocation);
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(DateUtil.getStringFromDateShortYear2(chalk.getChalkDate()));
                ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(chalk.getDurationCode() == null ? "NA" : chalk.getDurationCode());

                int mins = Duration.getDurationMinsById(chalk.getDurationId(), this);
                long diff = (new Date().getTime() - chalk.getChalkDate().getTime());
                long expTime = (diff / 1000) / 60;
                if (expTime > mins) {
                    chalk.setIsExpired("Y");

                    ImageView actionIcon = ((ImageView) tableRow.findViewById(R.id.tr_action));
                    actionIcon.setBackgroundResource(R.drawable.write_ticket);
                    actionIcon.setVisibility(View.VISIBLE);
                    actionIcon.setTag(R.id.ChalkId, chalk.getChalkId());
                    actionIcon.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long chalkId = ((Long) v.getTag(R.id.ChalkId)).longValue();
                            try {
                                ChalkVehicle activeChalk = ChalkVehicle.getChalkVehicleById(chalkId);
                                if (activeChalk != null) {
                                    Ticket ticket = TPApp.createTicketForChalk(activeChalk);
                                    TPApp.setActiveTicket(ticket);

                                    Intent i = new Intent();
                                    i.setClass(LocationChalkListActivity.this, WriteTicketActivity.class);
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

	        	/*if(expTime > mins)
	        		tableRow.setBackgroundResource(R.drawable.tablerow_expired);*/
                long expTimeinMilis = diff - mins * 60000;
                if (expTimeinMilis > 0)
                    tableRow.setBackgroundResource(R.drawable.tablerow_expired);


                tableRow.setTag(R.id.ChalkId, chalk.getChalkId());
                tableRow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long chalkId = ((Long) v.getTag(R.id.ChalkId)).longValue();

                        Intent i = new Intent();
                        i.setClass(LocationChalkListActivity.this, LocationChalkDetailsActivity.class);
                        i.putExtra("ChalkId", chalkId);
                        startActivityForResult(i, REQUEST_VIEW_CHALK);
                        return;
                    }
                });

                if (chalk.isExpired())
                    expiredCount++;

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
    }

    public void bindDataAtLoadingTime(final int reloadCount) {

        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread() {
            public void run() {
                try {
                    chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByLocation(LocationChalkListActivity.this);
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
            switch (requestCode) {
                case REQUEST_VIEW_CHALK: {
                    try {
                        chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByLocation(LocationChalkListActivity.this);
                        initDatagrid();
                    } catch (Exception e) {
                        Log.e(TPConstant.TAG, "Error reloading chalk list");
                    }

                    break;
                }

                case REQUEST_VIEW_MAP: {
                    break;
                }
            }
        }
    }

    public void clearAction(View view) {

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
                            for (Long chalkId : filteredIds) {
                                ChalkVehicle.removeChalkById(chalkId, "");
                            }

                            chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByLocation(LocationChalkListActivity.this);
                        } catch (Exception ae) {
                            log.error(ae);
                        }

                        initDatagrid();

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onClick(View v) {

    }

    public void backAction(View view) {
        refreshTimer.cancel();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        refreshTimer.cancel();
        finish();
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

                chalks = ((ChalkBLProcessor) screenBLProcessor).getChalkByLocation(LocationChalkListActivity.this);

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
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }
    }
}
