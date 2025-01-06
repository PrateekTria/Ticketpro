package com.ticketpro.vendors;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.Ticket;
import com.ticketpro.model.UserSetting;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.parking.service.TPAsyncTask;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class PassportParkingZoneInfoActivity extends BaseActivityImpl {

    private TableLayout tableLayout;
    private PassportParkingZoneInfo passportParkingZoneInfo;
    private PassportParkingZoneInfo filteredParkingZoneInfo = new PassportParkingZoneInfo();
    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    private int sortBy = 0;
    private int sortOrder = 0;

    private Handler lookupHandler;
    private String zoneName;
    private String zoneId;
    private EditText searchEditText;

    private CheckBox expiredCheckbox;
    private Button pageSizeButton;
    private int pageSize = -1;
    private String plateNumber = "";
    private String spaceNumber = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.zone_logs);
            setActiveScreen(this);

            setLogger(PassportParkingZoneInfoActivity.class.getName());
            setBLProcessor(new CommonBLProcessor((TPApplication) getApplicationContext()));
            tableLayout = (TableLayout) findViewById(R.id.logs_4_table_view);

            UserSetting userSettings = TPApp.getUserSettings();
            if (userSettings != null) {
                CachedMap.cachedDuration = userSettings.getCacheExpiry();
            }

            pageSizeButton = (Button) findViewById(R.id.pageSizeBtn);
            pageSizeButton.setVisibility(View.VISIBLE);

            expiredCheckbox = (CheckBox) findViewById(R.id.expiredChk);
            expiredCheckbox.setVisibility(View.VISIBLE);

            expiredCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    loadData(true);
                }
            });

            searchEditText = (EditText) findViewById(R.id.searchText);
            searchEditText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String searchText = searchEditText.getText().toString();

                    filteredParkingZoneInfo.getSpaces().clear();
                    filteredParkingZoneInfo.getVehicles().clear();

                    if (searchText.length() == 0) {
                        filteredParkingZoneInfo = passportParkingZoneInfo;
                    } else {
                        searchText = searchText.toLowerCase();
                        for (PassportParkingSpace space : passportParkingZoneInfo.getSpaces()) {
                            String plateNumber = space.getStatus();

                            if (!StringUtil.isEmpty(plateNumber)) {
                                if (plateNumber.toLowerCase().startsWith(searchText)) {
                                    filteredParkingZoneInfo.getSpaces().add(space);
                                }
                            }
                        }

                        for (PassportParkingVehicle vehicle : passportParkingZoneInfo.getVehicles()) {
                            String plateNumber = vehicle.getLicensePlateNumber();

                            if (!StringUtil.isEmpty(plateNumber)) {
                                if (plateNumber.toLowerCase().startsWith(searchText)) {
                                    filteredParkingZoneInfo.getVehicles().add(vehicle);
                                }
                            }
                        }
                    }

                    initDatagrid(filteredParkingZoneInfo);
                }
            });

            lookupHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    String responseJSON = (String) msg.obj;
                    try {

                        String key1 = "PassportParkingSpaces-" + zoneId;
                        String key2 = "PassportParkingVehicles-" + zoneId;

                        passportParkingZoneInfo = PassportParkingParser.getZoneInfo(responseJSON);
						
						/*if(passportParkingZoneInfo!=null){
							//Merge expired results
							CachedResult cachedResult = (CachedResult) TPApp.cachedMap.getResults(key1);
							if(cachedResult!=null && cachedResult.hasExpired()==false){
								ArrayList<PassportParkingSpace> expiredSpaces = (ArrayList<PassportParkingSpace>) cachedResult.getResults();
								expiredSpaces = getExpiredSpaces(expiredSpaces);
								if(expiredSpaces.size() > 0){
									passportParkingZoneInfo.getSpaces().addAll(expiredSpaces);
								}
							}
							
							cachedResult = (CachedResult) TPApp.cachedMap.getResults(key2);
							if(cachedResult!=null && cachedResult.hasExpired()==false){
								ArrayList<PassportParkingVehicle> expiredVehicles = (ArrayList<PassportParkingVehicle>) cachedResult.getResults();
								expiredVehicles = getExpiredVehicles(expiredVehicles);
								if(expiredVehicles.size() > 0){
									passportParkingZoneInfo.getVehicles().addAll(expiredVehicles);
								}
							}
							
							TPApp.cachedMap.setResults(key1, new CachedResult(passportParkingZoneInfo.getSpaces()));
							TPApp.cachedMap.setResults(key2, new CachedResult(passportParkingZoneInfo.getVehicles()));
						}*/

                        initDatagrid(passportParkingZoneInfo);

                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            };

            zoneName = getIntent().getStringExtra("ZoneName");
            zoneId = getIntent().getStringExtra("ZoneId");

            if (zoneName != null) {
                ((TextView) findViewById(R.id.zone_info_text)).setText(zoneName);
            }

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

    }

    private void initDatagrid(final PassportParkingZoneInfo passportParkingZoneInfo) {
        try {
            if (passportParkingZoneInfo == null) {
                return;
            }

            tableLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

            if (pageSize > 0) {
                pageSizeButton.setText("Show " + pageSize);
            } else {
                pageSizeButton.setText("Show All");
            }

            //adding Header
            View headerRow = inflater.inflate(R.layout.table_row_3_coulm_status_view, null);

            TextView plateColumn = ((TextView) headerRow.findViewById(R.id.tr_header1));
            plateColumn.setVisibility(View.GONE);
            plateColumn.setText("Zone");
            plateColumn.setClickable(true);
            plateColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (passportParkingZoneInfo.getSpaces().size() > 0) {
                        Collections.sort(passportParkingZoneInfo.getSpaces(), new PassportParkingSpace.ZoneComparator());
                    } else {
                        Collections.sort(passportParkingZoneInfo.getVehicles(), new PassportParkingVehicle.ZoneComparator());
                    }

                    //Update Sorting Order
                    if (sortBy != 1) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        if (passportParkingZoneInfo.getSpaces().size() > 0) {
                            Collections.reverse(passportParkingZoneInfo.getSpaces());
                        } else {
                            Collections.reverse(passportParkingZoneInfo.getVehicles());
                        }
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 1;
                    initDatagrid(passportParkingZoneInfo);
                }
            });

            TextView infoColumn = ((TextView) headerRow.findViewById(R.id.tr_header2));
            infoColumn.setText("Space/LPN");
            infoColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (passportParkingZoneInfo.getSpaces().size() > 0) {
                        Collections.sort(passportParkingZoneInfo.getSpaces(), new PassportParkingSpace.StatusComparator());
                    } else {
                        Collections.sort(passportParkingZoneInfo.getVehicles(), new PassportParkingVehicle.PlateComparator());
                    }

                    //Update Sorting Order
                    if (sortBy != 2) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        if (passportParkingZoneInfo.getSpaces().size() > 0) {
                            Collections.reverse(passportParkingZoneInfo.getSpaces());
                        } else {
                            Collections.reverse(passportParkingZoneInfo.getVehicles());
                        }
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 2;
                    initDatagrid(passportParkingZoneInfo);
                }
            });

            TextView statusColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
            statusColumn.setText("Expire");
            statusColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (passportParkingZoneInfo.getSpaces().size() > 0) {
                        Collections.sort(passportParkingZoneInfo.getSpaces(), new PassportParkingSpace.ExpireComparator());
                    } else {
                        Collections.sort(passportParkingZoneInfo.getVehicles(), new PassportParkingVehicle.ExpireComparator());
                    }

                    //Update Sorting Order
                    if (sortBy != 3) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;

                        if (passportParkingZoneInfo.getSpaces().size() > 0) {
                            Collections.reverse(passportParkingZoneInfo.getSpaces());
                        } else {
                            Collections.reverse(passportParkingZoneInfo.getVehicles());
                        }

                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 3;
                    initDatagrid(passportParkingZoneInfo);
                }
            });

            switch (sortBy) {
                case 1:
                    if (sortOrder == ASC_ORDER)
                        plateColumn.setText("Zone \u25BC");
                    else
                        plateColumn.setText("Zone \u25B2");
                    break;

                case 2:
                    if (sortOrder == ASC_ORDER)
                        infoColumn.setText("Space/LPN \u25BC");
                    else
                        infoColumn.setText("Space/LPN \u25B2");
                    break;

                case 3:
                    if (sortOrder == ASC_ORDER)
                        statusColumn.setText("Expire \u25BC");
                    else
                        statusColumn.setText("Expire \u25B2");
                    break;
            }

            tableLayout.addView(headerRow);

            int index = 0;
            for (PassportParkingSpace item : passportParkingZoneInfo.getSpaces()) {
                View tableRow = inflater.inflate(R.layout.table_row_3_coulm_status_view, null);

                ImageView statusIcon = ((ImageView) tableRow.findViewById(R.id.tr_status_img));
                statusIcon.setBackgroundResource(R.drawable.color_green);

                ImageView actionIcon = ((ImageView) tableRow.findViewById(R.id.tr_action));
                actionIcon.setBackgroundResource(R.drawable.info);
                actionIcon.setVisibility(View.GONE);
                actionIcon.setTag(R.id.ListIndex, index);
                actionIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = ((Integer) v.getTag(R.id.ListIndex)).intValue();
                        PassportParkingSpace spaceInfo = passportParkingZoneInfo.getSpaces().get(index);
                        //displaySpaceInfoMsg(spaceInfo);
                        refreshAndDisplaySpaceInfoMsg(index, spaceInfo);
                    }
                });

                tableRow.setTag(R.id.ListIndex, index);
                tableRow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = ((Integer) v.getTag(R.id.ListIndex)).intValue();
                        PassportParkingSpace spaceInfo = passportParkingZoneInfo.getSpaces().get(index);
                        //displaySpaceInfoMsg(spaceInfo);
                        refreshAndDisplaySpaceInfoMsg(index, spaceInfo);
                    }
                });

                String expireStr = "";
                long expireMinutes, expireHours, expireDays;
                long expireInMs = Integer.valueOf(item.getExpirationTimeInSecs()) * 1000;
                if (expireInMs > 0) {
                    expireMinutes = expireInMs / (60 * 1000) % 60;
                    expireHours = expireInMs / (60 * 60 * 1000) % 24;
                    expireDays = expireInMs / (24 * 60 * 60 * 1000);

                    if (expireDays > 0) {
                        expireStr =/*"in "+*/expireDays + " d " + expireHours + " h";
                    } else if (expireHours > 0) {
                        expireStr =/*"in "+*/expireHours + " h " + expireMinutes + " m";
                    } else if (expireMinutes > 0) {
                        expireStr =/*"in "+*/expireMinutes + " m";
                    } else {
                        expireStr =/*"in "+*/item.getExpirationTimeInSecs() + " s";
                    }

                    if (expireMinutes <= 3) {
                        statusIcon.setBackgroundResource(R.drawable.color_yellow);
                    }
                } else {
                    expireStr = "Expired";
                    statusIcon.setBackgroundResource(R.drawable.color_red);

                    ((TextView) tableRow.findViewById(R.id.tr_header1)).setTextColor(Color.RED);
                    ((TextView) tableRow.findViewById(R.id.tr_header2)).setTextColor(Color.RED);
                    ((TextView) tableRow.findViewById(R.id.tr_header3)).setTextColor(Color.RED);
                }

                //((TextView)tableRow.findViewById(R.id.tr_header1)).setText(item.getZoneNumber());
                ((TextView) tableRow.findViewById(R.id.tr_header1)).setVisibility(View.GONE);
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(item.getStatus());
                ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(expireStr);

                if ((index % 2) == 0) {
                    tableRow.setBackgroundResource(R.drawable.tablerow_even);
                } else {
                    tableRow.setBackgroundResource(R.drawable.tablerow_odd);
                }

                if (!expiredCheckbox.isChecked() || item.hasExpired()) {
                    tableLayout.addView(tableRow);
                    index++;
                }

                if (pageSize > 0 && index >= pageSize) {
                    break;
                }
            }

            index = 0;
            for (PassportParkingVehicle item : passportParkingZoneInfo.getVehicles()) {
                View tableRow = inflater.inflate(R.layout.table_row_3_coulm_status_view, null);

                ImageView statusIcon = ((ImageView) tableRow.findViewById(R.id.tr_status_img));
                statusIcon.setBackgroundResource(R.drawable.color_green);

                ImageView actionIcon = ((ImageView) tableRow.findViewById(R.id.tr_action));
                actionIcon.setBackgroundResource(R.drawable.info);
                actionIcon.setVisibility(View.GONE);
                actionIcon.setTag(R.id.ListIndex, index);
                actionIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = ((Integer) v.getTag(R.id.ListIndex)).intValue();
                        PassportParkingVehicle vehicleInfo = passportParkingZoneInfo.getVehicles().get(index);
                        //displayVehicleInfoMsg(vehicleInfo);
                        refreshAndDisplayVehicleInfoMsg(index, vehicleInfo);
                    }
                });

                tableRow.setTag(R.id.ListIndex, index);
                tableRow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = ((Integer) v.getTag(R.id.ListIndex)).intValue();
                        PassportParkingVehicle vehicleInfo = passportParkingZoneInfo.getVehicles().get(index);
                        //displayVehicleInfoMsg(vehicleInfo);
                        refreshAndDisplayVehicleInfoMsg(index, vehicleInfo);
                    }
                });

                String expireStr = "";
                long expireMinutes, expireHours, expireDays;
                long expireInMs = Integer.valueOf(item.getExpirationTimeInSecs()) * 1000;
                if (expireInMs > 0) {
                    expireMinutes = expireInMs / (60 * 1000) % 60;
                    expireHours = expireInMs / (60 * 60 * 1000) % 24;
                    expireDays = expireInMs / (24 * 60 * 60 * 1000);

                    if (expireDays > 0) {
                        expireStr =/*"in "+*/expireDays + " d " + expireHours + " h";
                    } else if (expireHours > 0) {
                        expireStr =/*"in "+*/expireHours + " h " + expireMinutes + " m";
                    } else if (expireMinutes > 0) {
                        expireStr =/*"in "+*/expireMinutes + " m";
                    } else {
                        expireStr =/*"in "+*/item.getExpirationTimeInSecs() + " s";
                    }

                    if (expireMinutes <= 3 && expireHours == 0 && expireDays == 0) {
                        statusIcon.setBackgroundResource(R.drawable.color_yellow);
                    }
                } else {
                    statusIcon.setBackgroundResource(R.drawable.color_red);
                    //((TextView)tableRow.findViewById(R.id.tr_header1)).setTextColor(Color.RED);
                    //((TextView)tableRow.findViewById(R.id.tr_header2)).setTextColor(Color.RED);
                    //((TextView)tableRow.findViewById(R.id.tr_header3)).setTextColor(Color.RED);
                }

                //((TextView)tableRow.findViewById(R.id.tr_header1)).setText(item.getZoneNumber());
                ((TextView) tableRow.findViewById(R.id.tr_header1)).setVisibility(View.GONE);
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(item.getLicensePlateNumber());
                ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(expireStr);

                if ((index % 2) == 0) {
                    tableRow.setBackgroundResource(R.drawable.tablerow_even);
                } else {
                    tableRow.setBackgroundResource(R.drawable.tablerow_odd);
                }

                if (!expiredCheckbox.isChecked() || item.hasExpired()) {
                    tableLayout.addView(tableRow);
                    index++;
                }

                if (pageSize > 0 && index >= pageSize) {
                    break;
                }
            }

        } catch (Exception e) {
            //log.error(TPUtility.getPrintStackTrace(e));
            e.printStackTrace();
        }
        try {
            if (!plateNumber.equalsIgnoreCase("")) {
                int selectedItem = 0;
                for (PassportParkingVehicle item : passportParkingZoneInfo.getVehicles()) {
                    selectedItem++;
                    if (plateNumber.equalsIgnoreCase(item.getLicensePlateNumber())) {
                        plateNumber = "";
                        displayVehicleInfoMsg(passportParkingZoneInfo.getVehicles().get(selectedItem - 1));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!spaceNumber.equalsIgnoreCase("")) {
                int selectedItem = 0;
                for (PassportParkingSpace item : passportParkingZoneInfo.getSpaces()) {
                    selectedItem++;
                    if (spaceNumber.equalsIgnoreCase(item.getNumber())) {
                        spaceNumber = "";
                        displaySpaceInfoMsg(passportParkingZoneInfo.getSpaces().get(selectedItem - 1));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<PassportParkingVehicle> getExpiredVehicles(ArrayList<PassportParkingVehicle> vehicles) {
        ArrayList<PassportParkingVehicle> expiredVehicles = new ArrayList<PassportParkingVehicle>();
        for (PassportParkingVehicle vehicle : vehicles) {
            if (vehicle.hasExpired()) {
                expiredVehicles.add(vehicle);
            }
        }

        return expiredVehicles;
    }

    private ArrayList<PassportParkingSpace> getExpiredSpaces(ArrayList<PassportParkingSpace> spaces) {
        ArrayList<PassportParkingSpace> expiredSpaces = new ArrayList<PassportParkingSpace>();
        for (PassportParkingSpace space : spaces) {
            if (space.hasExpired()) {
                expiredSpaces.add(space);
            }
        }

        return expiredSpaces;
    }

    public void bindDataAtLoadingTime() {
        loadData(false);
    }

    public void loadData(final boolean processCached) {
        TPAsyncTask task = new TPAsyncTask(this, "Checking Zone Info...");
        task.execute(new TPTask() {
            @Override
            public void execute() {
                final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PASSPORT_PARKING_ZONEINFO, TPApp.userId);
                if (config != null) {
                    if (processCached) {
						/*String key1 = "PassportParkingSpaces-" + zoneId;
						String key2 = "PassportParkingVehicles-" + zoneId;
						
						CachedResult cachedResult1=(CachedResult) TPApp.cachedMap.getResults(key1);
						CachedResult cachedResult2=(CachedResult) TPApp.cachedMap.getResults(key2);
						
						if(cachedResult1!=null || cachedResult2!=null){
							passportParkingZoneInfo = new PassportParkingZoneInfo();
							if(cachedResult1!=null && cachedResult1.hasExpired()==false){
								passportParkingZoneInfo.setSpaces((ArrayList<PassportParkingSpace>)cachedResult1.getResults());
							}
							
							if(cachedResult2!=null && cachedResult2.hasExpired()==false){
								passportParkingZoneInfo.setVehicles((ArrayList<PassportParkingVehicle>)cachedResult2.getResults());
							}
							 
							initDatagrid(passportParkingZoneInfo);
							return;
						}*/

                        String params = config.getParams();
                        params = params.replaceAll("\\{ZONE_ID\\}", zoneId);
                        try {
                            String response = TPUtility.getURLResponse(config.getServiceURL() + "?" + params);
                            Message msg = lookupHandler.obtainMessage();
                            msg.what = 1;
                            msg.obj = response;

                            lookupHandler.sendMessage(msg);
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    } else {
                        String params = config.getParams();
                        params = params.replaceAll("\\{ZONE_ID\\}", zoneId);
                        try {
                            String response = TPUtility.getURLResponse(config.getServiceURL() + "?" + params);
                            Message msg = lookupHandler.obtainMessage();
                            msg.what = 1;
                            msg.obj = response;

                            lookupHandler.sendMessage(msg);
                        } catch (Exception e) {
                            log.error(TPUtility.getPrintStackTrace(e));
                        }
                    }
                }
            }
        });
    }


    public void searchAction(View view) {
        if (searchEditText.getVisibility() == View.GONE) {
            searchEditText.setVisibility(View.VISIBLE);
            TPUtility.showSoftKeyboard(this, searchEditText);
        } else {
            searchEditText.setVisibility(View.GONE);
        }
    }


    public void pageSizeAction(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Results Limit");

        final CharSequence[] choiceList = {"Show 20 Results",
                "Show 50 Results",
                "Show 100 Results",
                "Show All"};

        builder.setItems(choiceList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        pageSize = 20;
                        break;

                    case 1:
                        pageSize = 50;
                        break;

                    case 2:
                        pageSize = 100;
                        break;

                    case 3:
                        pageSize = -1;
                        break;
                }

                initDatagrid(passportParkingZoneInfo);
            }
        })
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
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

    public void refreshAction(View view) {
        loadData(false);
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

    private void displaySpaceInfoMsg(final PassportParkingSpace passportParkingSpace) {
        try {
            StringBuffer message = new StringBuffer();
            StringBuffer values = new StringBuffer();


            message.append("Name" + "\n");
            values.append(": " + StringUtil.getDisplayString(passportParkingSpace.getZoneName()) + "\n");


            message.append("Number" + "\n");
            values.append(": " + StringUtil.getDisplayString(passportParkingSpace.getZoneNumber()) + "\n");


            message.append("Active LPN" + "\n");
            values.append(": " + StringUtil.getDisplayString(passportParkingSpace.getActivelpn()) + "\n");


            message.append("Status" + "\n");
            values.append(": " + StringUtil.getDisplayString(passportParkingSpace.getStatus()) + "\n");


            String expireStr = "";
            long expireInMs = Long.parseLong(passportParkingSpace.getExpirationTimeInSecs()) * 1000;
            if (expireInMs > 0) {
                long expireMinutes = expireInMs / (60 * 1000) % 60;
                long expireHours = expireInMs / (60 * 60 * 1000) % 24;
                long expireDays = expireInMs / (24 * 60 * 60 * 1000);

                if (expireDays > 0) {
                    expireStr =/*"in "+*/expireDays + " d " + expireHours + " h";
                } else if (expireHours > 0) {
                    expireStr =/*"in "+*/expireHours + " h " + expireMinutes + " m";
                } else if (expireMinutes > 0) {
                    expireStr =/*"in "+*/expireMinutes + " m";
                } else {
                    expireStr =/*"in "+*/passportParkingSpace.getExpirationTimeInSecs() + " s";
                }
            } else {
                expireStr = passportParkingSpace.getExpirationTimeInSecs();
            }

            message.append("Expire" + "\n");
            values.append(": " + StringUtil.getDisplayString(expireStr) + "\n");
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = (TextView) view.findViewById(R.id.headerTV);
            TextView valueTV = (TextView) view.findViewById(R.id.valueTV);

            headerTV.setText(message.toString());
            valueTV.setText(values.toString());
            /*WebView wv = new WebView(getBaseContext());
            wv.loadData(message.toString(), "text/html", "utf-8");
            wv.setBackgroundColor(android.graphics.Color.BLACK);
            wv.getSettings().setDefaultTextEncodingName("utf-8");*/

            Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setView(view);
            dialog.setTitle("Zone Info");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            if (expireInMs <= 0) {
                dialog.setNegativeButton("Write Ticket", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Ticket ticket = TPApp.createNewTicket();
                        ticket.setPlate(passportParkingSpace.getActivelpn());

                        TPApp.setActiveTicket(ticket);

                        Intent intent = new Intent();
                        intent.setClass(PassportParkingZoneInfoActivity.this, WriteTicketActivity.class);
                        startActivity(intent);
                    }
                });
            }

            dialog.show();

        } catch (Exception e) {
            Log.e("TicketPRO", "Passport Zone Info " + e.getMessage());
        }
    }


    private void displayVehicleInfoMsg(final PassportParkingVehicle passportParkingVehicle) {
        try {
            StringBuffer message = new StringBuffer();
            StringBuffer values = new StringBuffer();

            message.append("Name"+ "\n");
            values.append(": " + StringUtil.getDisplayString(passportParkingVehicle.getZoneName())+ "\n");


            message.append("Number"+ "\n");
            values.append(": " + StringUtil.getDisplayString(passportParkingVehicle.getZoneNumber())+ "\n");


            message.append("Plate"+ "\n");
            values.append(": " + StringUtil.getDisplayString(passportParkingVehicle.getLicensePlateNumber())+ "\n");


            message.append("State"+ "\n");
            values.append(": " + StringUtil.getDisplayString(passportParkingVehicle.getStateCode())+ "\n");


            message.append("Entry"+ "\n");
            values.append(": " + StringUtil.getDisplayString(passportParkingVehicle.getEntryTime())+ "\n");


            message.append("Exit"+ "\n");
            values.append(": " + StringUtil.getDisplayString(passportParkingVehicle.getExitTime())+ "\n");


            String expireStr = "";
            long expireInMs = Long.parseLong(passportParkingVehicle.getExpirationTimeInSecs()) * 1000;
            if (expireInMs > 0) {
                long expireMinutes = expireInMs / (60 * 1000) % 60;
                long expireHours = expireInMs / (60 * 60 * 1000) % 24;
                long expireDays = expireInMs / (24 * 60 * 60 * 1000);

                if (expireDays > 0) {
                    expireStr =/*"in "+*/expireDays + " d " + expireHours + " h";
                } else if (expireHours > 0) {
                    expireStr =/*"in "+*/expireHours + " h " + expireMinutes + " m";
                } else if (expireMinutes > 0) {
                    expireStr =/*"in "+*/expireMinutes + " m";
                } else {
                    expireStr =/*"in "+*/passportParkingVehicle.getExpirationTimeInSecs() + " s";
                }
            } else {
                expireStr = passportParkingVehicle.getExpirationTimeInSecs();
            }

            message.append("Expire"+ "\n");
            values.append(": " + StringUtil.getDisplayString(expireStr)+ "\n");

            /*WebView wv = new WebView(getBaseContext());
            wv.loadData(message.toString(), "text/html", "utf-8");
            wv.setBackgroundColor(android.graphics.Color.BLACK);
            wv.getSettings().setDefaultTextEncodingName("utf-8");*/

            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = (TextView) view.findViewById(R.id.headerTV);
            TextView valueTV = (TextView) view.findViewById(R.id.valueTV);

            headerTV.setText(message.toString());
            valueTV.setText(values.toString());

            Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setView(view);
            dialog.setTitle("Zone Info");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            if (expireInMs <= 0) {
                dialog.setNegativeButton("Write Ticket", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Ticket ticket = TPApp.createNewTicket();
                        ticket.setPlate(passportParkingVehicle.getLicensePlateNumber());
                        ticket.setStateCode(passportParkingVehicle.getStateCode());

                        TPApp.setActiveTicket(ticket);

                        Intent intent = new Intent();
                        intent.setClass(PassportParkingZoneInfoActivity.this, WriteTicketActivity.class);
                        startActivity(intent);
                    }
                });
            }

            dialog.show();

        } catch (Exception e) {
            Log.e("TicketPRO", "Passport Zone Info " + e.getMessage());
        }
    }

    private void refreshAndDisplaySpaceInfoMsg(int index, PassportParkingSpace spaceInfo) {
        try {
            spaceNumber = spaceInfo.getNumber();
            loadData(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshAndDisplayVehicleInfoMsg(int index, PassportParkingVehicle plateInfo) {
        try {
            plateNumber = plateInfo.getLicensePlateNumber();
            loadData(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
