package com.ticketpro.vendors;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */
public class ParkMobileZoneInfoActivity extends BaseActivityImpl {
    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    private TableLayout tableLayout;
    private ArrayList<ParkMobileParkingRight> parkings;
    private ArrayList<ParkMobileParkingRight> filteredItems = new ArrayList<ParkMobileParkingRight>();
    private int sortBy = 3;
    private int sortOrder = 1;

    private Handler lookupHandler;
    private String zoneName;
    private String zoneCode;
    private EditText searchEditText;

    private CheckBox expiredCheckbox;
    private Button pageSizeButton;
    private int pageSize = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.zone_logs);
            setActiveScreen(this);

            setLogger(ParkMobileZoneInfoActivity.class.getName());
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

                    filteredItems.clear();

                    if (searchText.length() == 0) {
                        filteredItems.addAll(parkings);
                    } else {
                        searchText = searchText.toLowerCase();
                        for (ParkMobileParkingRight parking : parkings) {
                            String plateNumber = parking.getLpn();

                            if (!StringUtil.isEmpty(plateNumber)) {
                                if (plateNumber.toLowerCase().startsWith(searchText)) {
                                    filteredItems.add(parking);
                                }
                            }
                        }
                    }

                    initDatagrid(filteredItems);
                }
            });

            lookupHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    String responseJSON = (String) msg.obj;
                    String key = "ParkMobile-" + zoneCode;
                    try {
                        parkings = ParkMobileParser.getParkingRights(responseJSON);
                        if (parkings != null && parkings.size() > 0) {
                            //Merge expired results
                            CachedResult cachedResult = (CachedResult) TPApp.cachedMap.getResults(key);
                            if (cachedResult != null && !cachedResult.hasExpired()) {
                                ArrayList<ParkMobileParkingRight> expiredParkings = (ArrayList<ParkMobileParkingRight>) cachedResult.getResults();
                                expiredParkings = getExpired(expiredParkings);
                                if (expiredParkings.size() > 0) {
                                    parkings.addAll(expiredParkings);
                                }
                            }
                            TPApp.cachedMap.setResults(key, new CachedResult(parkings));
                        }

                        Collections.sort(parkings, new ParkMobileParkingRight.ExpireComparator());

                        initDatagrid(parkings);

                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            };

            zoneName = getIntent().getStringExtra("ZoneName");
            zoneCode = getIntent().getStringExtra("ZoneCode");

            if (zoneName != null) {
                ((TextView) findViewById(R.id.zone_info_text)).setText(zoneName + " (" + zoneCode + ")");
            }

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void initDatagrid(final ArrayList<ParkMobileParkingRight> parkings) {
        try {
            if (parkings == null) {
                return;
            }
            try {
                tableLayout.removeAllViews();
            } catch (Exception e) {
                e.printStackTrace();
            }
            LayoutInflater inflater = LayoutInflater.from(this);

            if (pageSize > 0) {
                pageSizeButton.setText("Show " + pageSize);
            } else {
                pageSizeButton.setText("Show All");
            }

            //adding Header

            View headerRow = inflater.inflate(R.layout.table_row_parkmobile, null);
            TextView spc = headerRow.findViewById(R.id.tr_header0);
            spc.setText("SPC");
            spc.setClickable(true);
            spc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(parkings, new ParkMobileParkingRight.SpaceComparator());

                    //Update Sorting Order
                    if (sortBy != 4) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(parkings);
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 4;
                    initDatagrid(parkings);
                }
            });



            TextView plateColumn = headerRow.findViewById(R.id.tr_header1);
            plateColumn.setText("LPN");
            plateColumn.setClickable(true);
            plateColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(parkings, new ParkMobileParkingRight.PlateComparator());

                    //Update Sorting Order
                    if (sortBy != 1) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(parkings);
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 1;
                    initDatagrid(parkings);
                }
            });

            TextView infoColumn = ((TextView) headerRow.findViewById(R.id.tr_header2));
            infoColumn.setText("End Time");
            infoColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(parkings, new ParkMobileParkingRight.ExpireComparator());

                    //Update Sorting Order
                    if (sortBy != 2) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(parkings);
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 2;
                    initDatagrid(parkings);
                }
            });

            TextView statusColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
            statusColumn.setText("Expire");
            statusColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(parkings, new ParkMobileParkingRight.ExpireComparator());

                    //Update Sorting Order
                    if (sortBy != 3) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(parkings);
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 3;
                    initDatagrid(parkings);
                }
            });

            switch (sortBy) {
                case 1:
                    if (sortOrder == ASC_ORDER)
                        plateColumn.setText("LPN \u25BC");
                    else
                        plateColumn.setText("LPN \u25B2");
                    break;

                case 2:
                    if (sortOrder == ASC_ORDER)
                        infoColumn.setText("End Date \u25BC");
                    else
                        infoColumn.setText("End Date \u25B2");
                    break;

                case 3:
                    if (sortOrder == ASC_ORDER)
                        statusColumn.setText("Expire \u25BC");
                    else
                        statusColumn.setText("Expire \u25B2");
                    break;

                case 4:
                    if (sortOrder == ASC_ORDER)
                        statusColumn.setText("SPC \u25BC");
                    else
                        statusColumn.setText("SPC \u25B2");
                    break;
            }

            tableLayout.addView(headerRow);

            int index = 0;
            for (ParkMobileParkingRight item : parkings) {
                View tableRow = inflater.inflate(R.layout.table_row_parkmobile_info, null);

                ImageView statusIcon = ((ImageView) tableRow.findViewById(R.id.tr_status_img));
                statusIcon.setBackgroundResource(R.drawable.color_green);

                ImageView actionIcon = ((ImageView) tableRow.findViewById(R.id.tr_action));
                actionIcon.setBackgroundResource(R.drawable.info);
                actionIcon.setVisibility(View.GONE);
                actionIcon.setTag(R.id.ListIndex, index);
                actionIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = (Integer) v.getTag(R.id.ListIndex);
                        ParkMobileParkingRight parking = parkings.get(index);
                        displayParkingInfoMsg(parking);
                    }
                });

                tableRow.setTag(R.id.ListIndex, index);
                tableRow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = (Integer) v.getTag(R.id.ListIndex);
                        ParkMobileParkingRight parking = parkings.get(index);
                        displayParkingInfoMsg(parking);
                    }
                });

                ParkingExpireInfo expireInfo = item.getExpireInfo();
                if (expireInfo.isExpired()) {
                    statusIcon.setBackgroundResource(R.drawable.color_red);
                    //((TextView)tableRow.findViewById(R.id.tr_header1)).setTextColor(android.graphics.Color.RED);
                    //((TextView)tableRow.findViewById(R.id.tr_header2)).setTextColor(android.graphics.Color.RED);
                    //((TextView)tableRow.findViewById(R.id.tr_header3)).setTextColor(android.graphics.Color.RED);
                } else if (expireInfo.getDiffMinutes() <= 3 && expireInfo.getDiffHrs() == 0 && expireInfo.getDiffDays() == 0) {
                    statusIcon.setBackgroundResource(R.drawable.color_yellow);
                }

                ((TextView) tableRow.findViewById(R.id.tr_header0)).setText(item.getSpaceNumber());
                ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(item.getLpn());
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(StringUtil.getDisplayString(DateUtil.getStringFromDate2(item.getEndDateLocal())));
                ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(expireInfo.getExpireMsg());

                if ((index % 2) == 0) {
                    tableRow.setBackgroundResource(R.drawable.tablerow_even);
                } else {
                    tableRow.setBackgroundResource(R.drawable.tablerow_odd);
                }

                if (!expiredCheckbox.isChecked() || expireInfo.isExpired()) {
                    tableLayout.addView(tableRow);
                    index++;
                }

                if (pageSize > 0 && index >= pageSize) {
                    break;
                }
            }

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {
        loadData(true);
    }

    public void loadData(final boolean processCached) {
        TPAsyncTask task = new TPAsyncTask(this, "Checking Zone Info...");
        task.execute(new TPTask() {
            @Override
            public void execute() {
                VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PARK_MOBILE_ZONEINFO, TPApp.userId, "/");
                if (config != null) {
                    if (processCached) {
                        String key = "ParkMobile-" + zoneCode;
                        CachedResult cachedResult = (CachedResult) TPApp.cachedMap.getResults(key);
                        if (cachedResult != null && !cachedResult.hasExpired()) {
                            if (parkings != null && parkings.size() > 0) {
                                parkings.clear();
                                parkings = (ArrayList<ParkMobileParkingRight>) cachedResult.getResults();
                                runOnUiThread(() -> initDatagrid(parkings));
                                return;
                            }
                        }
                    }

                    String params = config.getParams();
                    params = params.replaceAll("\\{ZONE_ID\\}", zoneCode);

                    try {
                        String response = TPUtility.getURLResponse(config.getServiceURL() + "/" + params + "?format=json", config.getUsername(), config.getPassword());
                        Message msg = lookupHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = response;

                        lookupHandler.sendMessage(msg);

                    } catch (IOException e) {
                        log.error(TPUtility.getPrintStackTrace(e));
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

                initDatagrid(parkings);
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

    private ArrayList<ParkMobileParkingRight> getExpired(ArrayList<ParkMobileParkingRight> parkings) {
        ArrayList<ParkMobileParkingRight> expiredParkings = new ArrayList<ParkMobileParkingRight>();
        for (ParkMobileParkingRight parking : parkings) {
            if (parking.getExpireInfo().isExpired()) {
                expiredParkings.add(parking);
            }
        }

        return expiredParkings;
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

    private void displayParkingInfoMsg(final ParkMobileParkingRight parking) {
        try {
            StringBuffer message = new StringBuffer();
            StringBuffer values = new StringBuffer();


            message.append("Name" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getProductDescription()) + "\n");

            message.append("Space#" + "\n");
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
            values.append(": " + StringUtil.getDisplayString(DateUtil.getStringFromDate2(parking.getStartDateLocal())) + "\n");

            message.append("End Date" + "\n");
            values.append(": " + StringUtil.getDisplayString(DateUtil.getStringFromDate2(parking.getEndDateLocal())) + "\n");

			
			/*WebView wv = new WebView (getBaseContext());
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
            dialog.setTitle("Parking Info");
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
                        ticket.setTimeMarked(parking.getEndDateLocal());

                        TPApp.setActiveTicket(ticket);

                        Intent intent = new Intent();
                        intent.setClass(ParkMobileZoneInfoActivity.this, WriteTicketActivity.class);
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
}
