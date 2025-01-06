package com.ticketpro.vendors;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGeneratorPass2;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.UserSetting;
import com.ticketpro.model.ValidParkingData1;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPUtility;
import com.ticketpro.vendors.passport2_model.PP2Plate;
import com.ticketpro.vendors.passport2_model.PlateResponse;
import com.ticketpro.vendors.passport2_model.zoneInfo.Datum;
import com.ticketpro.vendors.passport2_model.zoneInfo.PP2ZoneInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PP2ZoneListInfo extends BaseActivityImpl {

    private ProgressDialog progressDialog;
    private TableLayout tableLayout;
    private ArrayList<Datum> parkings;
    private ArrayList<Datum> filteredItems = new ArrayList<>();
    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    private int sortBy = 3;
    private int sortOrder = 1;

    private Handler lookupHandler;
    private String zoneName;
    private String zoneCode;
    private EditText searchEditText;

    private CheckBox expiredCheckbox;
    private Button pageSizeButton;
    private int pageSize = -1;
    private String opId;
    private Preference preference;

    String accessToken;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.zone_logs);
            setActiveScreen(this);

            preference = Preference.getInstance(this);
            accessToken = preference.getString("PP2_TOKEN");
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
                    // loadData(true);
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
                        for (Datum parking : parkings) {
                            String plateNumber = parking.getVehicle().getVehiclePlate();

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


            zoneName = getIntent().getStringExtra("ZoneName");
            zoneCode = getIntent().getStringExtra("zoneId");
            opId = getIntent().getStringExtra("opId");

            if (zoneName != null) {
                ((TextView) findViewById(R.id.zone_info_text)).setText(zoneName);
            }

            // bindDataAtLoadingTime();

            _initState(zoneName);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }


    @Override
    public void bindDataAtLoadingTime() throws Exception {


    }


    private void _initState(String s) {


        try {
            if (isNetworkConnected()) {

                final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PP2_ZONE_LIST_INFO,
                        TPApp.deviceId);

                if (config != null) {

                    progressDialog = new ProgressDialog(PP2ZoneListInfo.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    progressDialog.show();
                    //https://api.us.passportinc.com/v4/enforcement/parking-rights?operator_id=869f5996-c1b7-4618-8707-3b5859206a09&zone_id=c68ddd0c-05df-4881-a0c9-135869fa199d
                    String serviceURL = config.getServiceURL();
                    String params = config.getParams();
                    String URL = serviceURL + params+"&zone_id="+zoneCode;
                    ApiRequest service = ServiceGeneratorPass2.createService(ApiRequest.class, accessToken);
                    service.getPP2ZoneListInfo(URL).enqueue(new Callback<PP2ZoneInfo>() {
                        @Override
                        public void onResponse(Call<PP2ZoneInfo> call, Response<PP2ZoneInfo> response) {
                            progressDialog.isShowing();
                            progressDialog.dismiss();
                            call.cancel();
                            if (response.isSuccessful() && response.code()==200) {
                                List<Datum> data = response.body().getData();
                                if (data!=null && data.size()>0){
                                    parkings = new ArrayList<>(data.size());
                                    parkings.addAll(data);
                                    initDatagrid(parkings);
                                }

                            }


                        }

                        @Override
                        public void onFailure(Call<PP2ZoneInfo> call, Throwable t) {
                            progressDialog.isShowing();
                            progressDialog.dismiss();
                            call.cancel();
                        }
                    });

                }

            } else {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
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
            plateColumn.setText("LPN");
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
           /* infoColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(parkings,new ParkMobileParkingRight.SpaceComparator());

                    //Update Sorting Order
                    if(sortBy!=2){
                        sortOrder=ASC_ORDER;
                    }
                    else if(sortOrder==ASC_ORDER){
                        sortOrder=DESC_ORDER;
                        Collections.reverse(parkings);
                    }
                    else{
                        sortOrder=ASC_ORDER;
                    }

                    sortBy=2;
                    initDatagrid(parkings);
                }
            });
*/
            TextView statusColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
            statusColumn.setText("Expire");
            statusColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    //Collections.sort(parkings,new ParkMobileParkingRight.ExpireComparator());

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
                        infoColumn.setText("Space \u25BC");
                    else
                        infoColumn.setText("Space \u25B2");
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
                        displayParkingInfoMsg(parking,index);
                    }
                });

                tableRow.setTag(R.id.ListIndex, index);
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = ((Integer) v.getTag(R.id.ListIndex)).intValue();
                        Datum parking = parkings.get(index);
                        displayParkingInfoMsg(parking, index);
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

                ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(item.getVehicle().getVehiclePlate());
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(zoneName);
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

    private ArrayList<ValidParkingData1> getExpired(ArrayList<ValidParkingData1> parkings) {
        ArrayList<ValidParkingData1> expiredParkings = new ArrayList<>();
        for (ValidParkingData1 parking : parkings) {
            if (parking.getExpireInfo().isExpired()) {
                expiredParkings.add(parking);
            }
        }

        return expiredParkings;
    }

    public void refreshAction(View view) {
        if (zoneName!=null) {
            _initState(zoneName);
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

    private void displayParkingInfoMsg(final Datum parking, int index) {
        try {
            StringBuffer message = new StringBuffer();
            StringBuffer values = new StringBuffer();

            message.append("Zone" + "\n");
            message.append("Plate" + "\n");
            message.append("Start time" + "\n");
            message.append("End time" + "\n");
            message.append("Expire" + "\n");
            message.append("State" + "\n");


            values.append(": " + StringUtil.getDisplayString(zoneName) + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getVehicle().getVehiclePlate()) + "\n");
            values.append(": " + DateUtil.getPP2DateFromUTCString1(parking.getStartTime()) + "\n");
            values.append(": " + DateUtil.getPP2DateFromUTCString1(parking.getEndTime()) + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getExpireInfo().getExpireMsg()) + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getVehicle().getVehicleState()) + "\n");

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
                        ticket.setPlate(parking.getVehicle().getVehiclePlate());


                        TPApp.setActiveTicket(ticket);

                        Intent intent = new Intent();
                        intent.setClass(PP2ZoneListInfo.this, WriteTicketActivity.class);
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
