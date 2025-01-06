package com.ticketpro.vendors;

import android.annotation.SuppressLint;
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

import com.google.gson.Gson;
import com.ticketpro.model.Feature;
import com.ticketpro.model.MobileNowLog;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.UserSetting;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.parking.api.WriteTicketNetworkCalls;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.parking.service.TPAsyncTask;
import com.ticketpro.util.CSVUtility;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class PayByPhoneZoneInfoActivity extends BaseActivityImpl {

    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    private TableLayout tableLayout;
    private ArrayList<PayByPhoneParking> parkings;
    private ArrayList<PayByPhoneParking> filteredItems = new ArrayList<PayByPhoneParking>();
    private int sortBy = 3;
    private int sortOrder = 1;

    private Handler lookupHandler;
    private String zoneName;
    private String zoneCode;
    private EditText searchEditText;

    private CheckBox expiredCheckbox;
    private Button pageSizeButton;
    private int pageSize = -1;

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.zone_logs);
            setActiveScreen(this);

            setLogger(PayByPhoneZoneInfoActivity.class.getName());
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
                        for (PayByPhoneParking parking : parkings) {
                            String plateNumber = parking.getPlate();

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
                    try {
                        String key = "PayByPhone-" + zoneCode;
                        PayByPhonePlateParser parser = new Gson().fromJson(responseJSON, PayByPhonePlateParser.class);

                        parkings = PayByPhoneParser.getParkings(parser.getPlatesResponse());
                        /*if (parkings.size() > 0) {
                            //Merge expired results
                            CachedResult cachedResult = (CachedResult) TPApp.cachedMap.getResults(key);
                            if (cachedResult != null && !cachedResult.hasExpired()) {
                                ArrayList<PayByPhoneParking> expiredParkings = (ArrayList<PayByPhoneParking>) cachedResult.getResults();
                                expiredParkings = getExpired(expiredParkings);
                                if (expiredParkings.size() > 0) {
                                    parkings.addAll(expiredParkings);
                                }
                            }
                            TPApp.cachedMap.setResults(key, new CachedResult(parkings));
                        }*/

                        Collections.sort(parkings, new PayByPhoneParking.ExpireComparator());

                        initDatagrid(parkings);

                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    }
                }
            };

            zoneName = getIntent().getStringExtra("ZoneName");
            zoneCode = getIntent().getStringExtra("ZoneCode");

            if (zoneName != null) {
                ((TextView) findViewById(R.id.zone_info_text)).setText(zoneName);
            }

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }


    private void initDatagrid(final ArrayList<PayByPhoneParking> parkings) {
        try {
            if (parkings == null) {
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
            View headerRow = inflater.inflate(R.layout.table_row_parking, null);

            TextView plateColumn = ((TextView) headerRow.findViewById(R.id.tr_header1));
            plateColumn.setText("Plate");
            plateColumn.setClickable(true);
            plateColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(parkings, new PayByPhoneParking.PlateComparator());

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
            infoColumn.setText("Stall");
            infoColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(parkings, new PayByPhoneParking.ZoneComparator());

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
                    Collections.sort(parkings, new PayByPhoneParking.ExpireComparator());

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
                        plateColumn.setText("Plate \u25BC");
                    else
                        plateColumn.setText("Plate \u25B2");
                    break;

                case 2:
                    if (sortOrder == ASC_ORDER)
                        infoColumn.setText("Stall \u25BC");
                    else
                        infoColumn.setText("Stall \u25B2");
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
            for (PayByPhoneParking item : parkings) {
                View tableRow = inflater.inflate(R.layout.table_row_parking, null);

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
                        PayByPhoneParking parking = parkings.get(index);
                        displayPayByPhoneInfoMsg(parking);
                    }
                });

                tableRow.setTag(R.id.ListIndex, index);
                tableRow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = ((Integer) v.getTag(R.id.ListIndex)).intValue();
                        PayByPhoneParking parking = parkings.get(index);
                        displayPayByPhoneInfoMsg(parking);
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

                ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(item.getPlate());
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(item.getStallNumber());
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

    private ArrayList<PayByPhoneParking> getExpired(ArrayList<PayByPhoneParking> parkings) {
        ArrayList<PayByPhoneParking> expiredParkings = new ArrayList<PayByPhoneParking>();
        for (PayByPhoneParking parking : parkings) {
            if (parking.getExpireInfo().isExpired()) {
                expiredParkings.add(parking);
            }
        }

        return expiredParkings;
    }

    public void bindDataAtLoadingTime() {
        loadData(true);
    }

    public void loadData(final boolean processCached) {

        TPAsyncTask task = new TPAsyncTask(this, "Checking zone info...");
        task.execute(new TPTask() {
            @Override
            public void execute() {

                final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PAYBYPHONE_ZONEINFO, TPApp.userId);
                if (config != null) {
                    /*if (processCached) {
                        String key = "PayByPhone-" + zoneCode;
                        CachedResult cachedResult = (CachedResult) TPApp.cachedMap.getResults(key);
                        if (cachedResult != null && cachedResult.hasExpired() == false) {
                            parkings = (ArrayList<PayByPhoneParking>) cachedResult.getResults();
                            initDatagrid(parkings);
                            return;
                        }
                    }*/

                    String params = config.getParams();
                    params = params.replaceAll("\\{ZONE_ID\\}", zoneCode);

                    try {
                        String response = TPUtility.getURLResponse(config.getServiceURL() + "?" + params,config.getUsername(),config.getPassword());
                        if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                            try {
                                MobileNowLog log = new MobileNowLog();
                                log.setCustId(TPApp.custId);
                                log.setDeviceId(TPApp.deviceId);
                                log.setUserId(TPApp.userId);
                                log.setRequestDate(new Date());
                                log.setRequestParams(params);
                                log.setServiceMode(config.getRequestMode());
                                log.setResponseText(response);
                                MobileNowLog.insertMobileNowLog(log);
                                /*DatabaseHelper.getInstance().openWritableDatabase();
                                DatabaseHelper.getInstance().insertOrReplace(log.getContentValues(), "mobile_now_logs");
                                DatabaseHelper.getInstance().closeWritableDb();*/
                                CSVUtility.writeMobileLogCSV(log);
                                ArrayList<MobileNowLog> logs = new ArrayList<>();
                                logs.add(log);
                                WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                /*((CommonBLProcessor) screenBLProcessor).sendMobileNowLog(logs);*/
                            } catch (Exception e) {
                                log.error("Error " + TPUtility.getPrintStackTrace(e));
                                e.printStackTrace();
                            }
                        }

                        Message msg = lookupHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = response;

                        lookupHandler.sendMessage(msg);

                    } catch (IOException e) {
                        log.error("IOException " + TPUtility.getPrintStackTrace(e));
                        e.printStackTrace();
                    } catch (Exception e1) {
                        log.error("Exception " + TPUtility.getPrintStackTrace(e1));
                        e1.printStackTrace();
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

    public void displayPayByPhoneInfoMsg(final PayByPhoneParking parking) {
        try {
            StringBuffer message = new StringBuffer();
            StringBuffer values = new StringBuffer();


            message.append("Plate" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getPlate()) + "\n");


            message.append("State" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getState()) + "\n");


            message.append("Stall Number" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getStallNumber()) + "\n");


            message.append("Expire" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getExpireInfo().getExpireMsg()) + "\n");

			
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
            dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
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
                        ticket.setPlate(parking.getPlate());
                        ticket.setStateCode(parking.getState());

                        TPApp.setActiveTicket(ticket);

                        Intent intent = new Intent();
                        intent.setClass(PayByPhoneZoneInfoActivity.this, WriteTicketActivity.class);
                        startActivity(intent);
                    }
                });
            }


            TPUtility.applyButtonStyles(dialog.show());

        } catch (Exception e) {
            Log.e("TicketPRO", "PayByPhone Zone Info " + e.getMessage());
        }
    }
}
