package com.ticketpro.vendors;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGeneratorJson;
import com.ticketpro.model.Datum;
import com.ticketpro.model.Feature;
import com.ticketpro.model.LotwiseApi;
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
import com.ticketpro.util.CSVUtility;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SamtransZoneActiviyInfo extends BaseActivityImpl {

    private ProgressDialog progressDialog;
    private TableLayout tableLayout;
    private ArrayList<Datum> filteredItems = new ArrayList<>();
    private ArrayList<Datum> infoArrayList;
    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    private int sortBy = 2;
    private int sortOrder = 1;

    private Handler lookupHandler;
    private String zoneName;
    private String zoneCode;
    private EditText searchEditText;

    private CheckBox expiredCheckbox;
    private Button pageSizeButton;
    private int pageSize = -1;
    private Preference preference;
    private String samtransToken;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.zone_logs);
            setActiveScreen(this);
            preference = Preference.getInstance(this);

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
                    // _initState(zoneName,true);
                    _getFreshData(zoneName);
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
                        filteredItems.addAll(infoArrayList);
                    } else {
                        searchText = searchText.toLowerCase();
                        for (Datum parking : infoArrayList) {
                            String plateNumber = parking.getLotName();

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
                    String key = "Samtrans-" + zoneName;
                    try {

                        if (infoArrayList != null && infoArrayList.size() > 0) {
                            //Merge expired results
                            CachedResult cachedResult = (CachedResult) TPApp.cachedMap.getResults(key);
                            if (cachedResult != null && !cachedResult.hasExpired()) {
                                ArrayList<Datum> expiredParkings = (ArrayList<Datum>) cachedResult.getResults();
                                expiredParkings = getExpired(expiredParkings);
                                if (expiredParkings.size() > 0) {
                                    infoArrayList.addAll(expiredParkings);
                                }
                            }

                            TPApp.cachedMap.setResults(key, new CachedResult(infoArrayList));
                        }

                        Collections.sort(infoArrayList, new Datum.ExpireComparator());

                        initDatagrid(infoArrayList);

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
            // bindDataAtLoadingTime();
            // _initState(zoneName,true);
            _getFreshData(zoneName);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }


    @Override
    public void bindDataAtLoadingTime() throws Exception {


    }

    private void _initState(String zoneName, boolean processCached) throws IOException {

        if (processCached) {
            String key = "Samtrans-" + zoneName;
            CachedResult cachedResult = (CachedResult) TPApp.cachedMap.getResults(key);
            if (cachedResult != null && !cachedResult.hasExpired()) {
                infoArrayList = (ArrayList<Datum>) cachedResult.getResults();
                initDatagrid(infoArrayList);
                return;
            }
        } else {
            if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                return;
            }
            if (isNetworkConnected()) {
                // new ParkeonZoneInfoService(zoneCode).execute();
                _getFreshData(zoneName);
            }
        }
    }

    private void _getFreshData(final String zoneName) {
        try {
            samtransToken = preference.getString(TPConstant.SAMTRANS_TOKEN);
            if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                return;
            }
            if (isNetworkConnected() && samtransToken != null) {

                final VendorServiceConfig config = VendorService.getServiceConfigCale(VendorService.SAMTRANS_BASE_URL, TPApp.deviceId, "/");
                Map<String, String> paramsMap = config.getParamsMap();
                //String user = paramsMap.get("User");
                //String password = paramsMap.get("Password");

                progressDialog = new ProgressDialog(SamtransZoneActiviyInfo.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                progressDialog.show();
                //String url = config.getServiceURL() + "/" + zoneName + "/20190501140552/20190712153200";
                //final String url = config.getServiceURL() + "/" + zoneName + "/20200819120100/20200820120100";
                final String url = config.getServiceURL() + "/" + zoneName + "/" + DateUtil.getStartDateSamtrans() + "/" + DateUtil.getEndDateSamtrans();
                ApiRequest service = ServiceGeneratorJson.createService(ApiRequest.class, samtransToken);
                service._getDataBylotname(url).enqueue(new Callback<LotwiseApi>() {
                    @Override
                    public void onResponse(@NotNull Call<LotwiseApi> call, @NotNull Response<LotwiseApi> response) {
                        if (response.isSuccessful()) {
                            List<Datum> data = null;
                            if (response.body() != null) {
                                data = response.body().getData();
                            }
                            if (data != null) {
                                if (Feature.isFeatureAllowed(Feature.PARK_SEARCH_TRACK)) {
                                    try {
                                        MobileNowLog log = new MobileNowLog();
                                        log.setCustId(TPApp.custId);
                                        log.setDeviceId(TPApp.deviceId);
                                        log.setUserId(TPApp.userId);
                                        log.setRequestDate(new Date());
                                        log.setRequestParams(url);
                                        log.setServiceMode(config.getRequestMode());
                                        log.setResponseText(new Gson().toJson(response.body()));
                                        MobileNowLog.insertMobileNowLog(log);
                                        CSVUtility.writeMobileLogCSV(log);
                                        ArrayList<MobileNowLog> logs = new ArrayList<>();
                                        logs.add(log);
                                        WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                    } catch (Exception e) {
                                        log.error(TPUtility.getPrintStackTrace(e));
                                    }
                                }
                                if (data.size() > 0) {
                                    try {
                                        ArrayList<Datum> noRepeat = Datum._removeDuplicateValueFromArray(data);
                                        Collections.sort(noRepeat, new Datum.SpaceComparator());
//                                            Collections.reverse(noRepeat);
                                        infoArrayList = Datum.removeSpaceByRecentPurchaed(noRepeat);
                                        initDatagrid(infoArrayList);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            progressDialog.isShowing();
                            progressDialog.dismiss();
                        } else {

                            if (response.code() == 401) {
                                SamtransZoneActivity.getInstanc()._getToken();
                                progressDialog.isShowing();
                                progressDialog.dismiss();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        _getFreshData(zoneName);
                                    }
                                }, 4000);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<LotwiseApi> call, @NotNull Throwable t) {
                        progressDialog.isShowing();
                        progressDialog.dismiss();

                    }
                });


            } else {
                Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDatagrid(final ArrayList<Datum> parkings) {
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
            View headerRow = inflater.inflate(R.layout.table_row_parking, null);

            TextView plateColumn = ((TextView) headerRow.findViewById(R.id.tr_header1));
            plateColumn.setText("Loc");
            plateColumn.setClickable(true);
            plateColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // Collections.sort(parkings,new ParkMobileParkingRight.PlateComparator());

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
            infoColumn.setText("Space");
            infoColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(parkings, new Datum.SpaceComparator());
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
                    Collections.sort(parkings, new Datum.ExpireComparator1());
                    //Update Sorting Order
                    if (sortBy != 3) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        // Collections.reverse(parkings);
                    } else {
                        sortOrder = ASC_ORDER;
                        Collections.reverse(parkings);
                    }

                    sortBy = 3;
                    initDatagrid(parkings);
                }
            });

            switch (sortBy) {
                case 1:
                    if (sortOrder == ASC_ORDER)
                        plateColumn.setText("LOC \u25BC");
                    else
                        plateColumn.setText("LOC \u25B2");
                    break;

                case 2:
                    if (sortOrder == ASC_ORDER)
                        infoColumn.setText("Space \u25BC");
                    else
                        infoColumn.setText("Space \u25B2");
                    break;

                case 3:

                    if (sortOrder == ASC_ORDER) {
                        statusColumn.setText("Expire \u25BC");

                    } else {
                        statusColumn.setText("Expire \u25B2");
                    }
                    break;
            }

            tableLayout.addView(headerRow);

            int index = 0;
            for (Datum item : parkings) {
                View tableRow = inflater.inflate(R.layout.table_row_parking, null);

                ImageView statusIcon = ((ImageView) tableRow.findViewById(R.id.tr_status_img));
                statusIcon.setBackgroundResource(R.drawable.color_green);

                ImageView actionIcon = ((ImageView) tableRow.findViewById(R.id.tr_action));
                actionIcon.setBackgroundResource(R.drawable.info);
                actionIcon.setVisibility(View.GONE);
                actionIcon.setTag(R.id.ListIndex, index);
                actionIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = ((Integer) v.getTag(R.id.ListIndex)).intValue();
                        Datum parking = parkings.get(index);
                        displayParkingInfoMsg(parking);
                    }
                });

                tableRow.setTag(R.id.ListIndex, index);
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = ((Integer) v.getTag(R.id.ListIndex)).intValue();
                        Datum parking = parkings.get(index);
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

                ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(item.getLotName());
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(item.getSpaceNumber());
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

                initDatagrid(infoArrayList);
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

    private ArrayList<Datum> getExpired(ArrayList<Datum> parkings) {
        ArrayList<Datum> expiredParkings = new ArrayList<>();
        for (Datum parking : parkings) {
            if (parking.getExpireInfo().isExpired()) {
                expiredParkings.add(parking);
            }
        }

        return expiredParkings;
    }

    public void refreshAction(View view) {
        if (zoneName != null) {
            //_initState(zoneName,false);
            _getFreshData(zoneName);
        }
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

    private void displayParkingInfoMsg(final Datum parking) {
        try {
            StringBuffer message = new StringBuffer();
            StringBuffer values = new StringBuffer();

            message.append("Lot name" + "\n");
            message.append("Space Number" + "\n");
            message.append("Purchased Date" + "\n");
            message.append("Expiry Date" + "\n");
            message.append("Expire" + "\n");
            //message.append("Payed" + "\n");

            values.append(": " + StringUtil.getDisplayString(parking.getLotName()) + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getSpaceNumber()) + "\n");
            values.append(": " + StringUtil.getDisplayString(DateUtil.getConvertedDateSamtrans(parking.getDatePurchased().trim())) + "\n");
            values.append(": " + StringUtil.getDisplayString(DateUtil.getConvertedDateSamtrans(parking.getExpiryDate().trim())) + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getExpireInfo().getExpireMsg()) + "\n");
            //  values.append(": $" + StringUtil.getDisplayString("" + parking.getAmount()) + "\n");

            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = (TextView) view.findViewById(R.id.headerTV);
            TextView valueTV = (TextView) view.findViewById(R.id.valueTV);

            headerTV.setText(message.toString());
            valueTV.setText(values.toString());

          /*  WebView wv = new WebView (getBaseContext());
            wv.loadData(message.toString(), "text/html", "utf-8");
            wv.setBackgroundColor(android.graphics.Color.BLACK);
            wv.getSettings().setDefaultTextEncodingName("utf-8");*/

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setView(view);
            dialog.setTitle("Space Info");
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
                        //ticket.setPlate(parking.getCode());

                        TPApp.setActiveTicket(ticket);

                        Intent intent = new Intent();
                        intent.setClass(SamtransZoneActiviyInfo.this, WriteTicketActivity.class);
                        intent.putExtra("SAMTRANS", true);
                        intent.putExtra("LOC", zoneName);
                        startActivity(intent);
                    }
                });
            }
            dialog.show();
        } catch (Exception e) {
            Log.e("TicketPRO", "Passport Zone Info " + e.getMessage());
        }
    }

    @Override
    protected void onStop() {
        System.gc();
        super.onStop();
    }
}
