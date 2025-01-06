package com.ticketpro.vendors.dpt;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import com.ticketpro.model.Ticket;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.parking.service.RequestLogTask;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPUtility;
import com.ticketpro.vendors.ParkingExpireInfo;
import com.ticketpro.vendors.dpt.PlateInfoService.ExpiredPlateInfoByRegionRequest;
import com.ticketpro.vendors.dpt.PlateInfoService.IWsdl2CodeEvents;
import com.ticketpro.vendors.dpt.PlateInfoService.PlateInfoByRegionRequest;
import com.ticketpro.vendors.dpt.PlateInfoService.PlateInfoService;
import com.ticketpro.vendors.dpt.PlateInfoService.PlateInfoType;
import com.ticketpro.vendors.dpt.PlateInfoService.VectorPlateInfoType;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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

public class DigitalPaytechZoneInfoActivity extends BaseActivityImpl {

    private TableLayout tableLayout;
    private ArrayList<PlateInfoType> parkings;
    private ArrayList<PlateInfoType> filteredItems = new ArrayList<PlateInfoType>();

    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    private int sortBy = 3;
    private int sortOrder = 1;

    private CheckBox expiredCheckbox;
    private Button pageSizeButton;
    private ProgressDialog progressDialog;
    private Handler lookupHandler;
    private String regionName;
    private int pageSize = -1;

    private VectorPlateInfoType vectorPlateInfoType = null;
    private EditText searchEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.zone_logs);
            setActiveScreen(this);

            setLogger(DigitalPaytechZoneInfoActivity.class.getName());
            setBLProcessor(new CommonBLProcessor((TPApplication) getApplicationContext()));
            tableLayout = (TableLayout) findViewById(R.id.logs_4_table_view);

            pageSizeButton = (Button) findViewById(R.id.pageSizeBtn);
            pageSizeButton.setVisibility(View.VISIBLE);

            expiredCheckbox = (CheckBox) findViewById(R.id.expiredChk);
            expiredCheckbox.setVisibility(View.VISIBLE);

            expiredCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    refreshAction(buttonView);
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
                        for (PlateInfoType parking : parkings) {
                            String plateNumber = parking.plateNumber;

                            if (!StringUtil.isEmpty(plateNumber)) {
                                if (plateNumber.toLowerCase().startsWith(searchText)) {
                                    filteredItems.add(parking);
                                }
                            }
                        }
                    }

                    initDatagrid(filteredItems, false);
                }
            });

            lookupHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    progressDialog = ProgressDialog.show(DigitalPaytechZoneInfoActivity.this, "", "Updating List...");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Collections.sort(parkings, new PlateInfoType.ExpireComparator());
                                if (expiredCheckbox.isChecked()) {
                                    Collections.reverse(parkings);
                                }

                                initDatagrid(parkings, false);

                            } catch (Exception e) {
                                log.error(TPUtility.getPrintStackTrace(e));
                            }
                        }

                    }, 100);

                }
            };

            regionName = getIntent().getStringExtra("RegionName");
            if (regionName != null) {
                ((TextView) findViewById(R.id.zone_info_text)).setText(regionName);
            }

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void initDatagrid() {
        initDatagrid(parkings, true);
    }

    private void initDatagrid(final ArrayList<PlateInfoType> parkings, boolean showLoading) {
        if (parkings == null) {
            return;
        }

        if (showLoading) {
            progressDialog = ProgressDialog.show(this, "", "Updating List...");
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateListing(parkings);

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        }, 100);
    }

    private void updateListing(final ArrayList<PlateInfoType> parkings) {
        try {
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
                    Collections.sort(parkings, new PlateInfoType.PlateComparator());

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
                    initDatagrid();
                }
            });

            TextView infoColumn = ((TextView) headerRow.findViewById(R.id.tr_header2));
            infoColumn.setText("Purchase Date");
            infoColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(parkings, new PlateInfoType.PurchaseDateComparator());

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
                    initDatagrid();
                }
            });

            TextView statusColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
            statusColumn.setText("Expiry");
            statusColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(parkings, new PlateInfoType.ExpireComparator());

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
                    initDatagrid();
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
                        infoColumn.setText("Purchased Date \u25BC");
                    else
                        infoColumn.setText("Purchased Date \u25B2");
                    break;

                case 3:
                    if (sortOrder == ASC_ORDER)
                        statusColumn.setText("Expiry \u25BC");
                    else
                        statusColumn.setText("Expiry \u25B2");
                    break;
            }

            tableLayout.addView(headerRow);

            int index = 0;
            for (PlateInfoType item : parkings) {
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
                        PlateInfoType parking = parkings.get(index);
                        displayDigitalPayTechInfoMsg(parking);
                    }
                });

                tableRow.setTag(R.id.ListIndex, index);
                tableRow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = ((Integer) v.getTag(R.id.ListIndex)).intValue();
                        PlateInfoType parking = parkings.get(index);
                        displayDigitalPayTechInfoMsg(parking);
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

                ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(item.plateNumber);
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(DateUtil.getStringFromDateShortYear(item.getPurchasedDate()));
                ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(expireInfo.getExpireMsg());

                if ((index % 2) == 0) {
                    tableRow.setBackgroundResource(R.drawable.tablerow_even);
                } else {
                    tableRow.setBackgroundResource(R.drawable.tablerow_odd);
                }

                if (!expiredCheckbox.isChecked() || item.getExpireInfo().isExpired()) {
                    tableLayout.addView(tableRow);
                    index++;
                }

                if (pageSize > 0 && index >= pageSize) {
                    break;
                }
            }

            progressDialog.dismiss();

        } catch (Exception e) {
            progressDialog.dismiss();
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {
        try {
            final VendorServiceConfig config = VendorService.getServiceConfigT2(VendorService.DIGITALPAYTECH_ZONEINFO, TPApp.deviceId);
            if (config != null) {
                progressDialog = ProgressDialog.show(this, "", "Checking Region Info...");
                new Thread(() -> {
                    PlateInfoService service = new PlateInfoService(new IWsdl2CodeEvents() {
                        @Override
                        public void Wsdl2CodeStartedRequest() {

                        }

                        @Override
                        public void Wsdl2CodeFinishedWithException(Exception ex) {
                            log.error("PlateInfoService Error " + TPUtility.getPrintStackTrace(ex));
                        }

                        @Override
                        public void Wsdl2CodeFinished(String methodName, Object Data) {

                        }

                        @Override
                        public void Wsdl2CodeEndedRequest() {

                        }
                    });

                    service.addAuthHeader(config.getUsername(), config.getPassword());
                    service.setUrl(config.getServiceURL());

                    if (expiredCheckbox.isChecked()) {
                        ExpiredPlateInfoByRegionRequest request = new ExpiredPlateInfoByRegionRequest();
                        request.token = config.getParamsMap().get("token");
                        request.region = regionName;
                        request.gracePeriod = 0;

                        vectorPlateInfoType = service.getExpiredPlatesByRegion(request);

                    } else {

                        PlateInfoByRegionRequest request = new PlateInfoByRegionRequest();
                        request.token = config.getParamsMap().get("token");
                        request.region = regionName;
                        request.gracePeriod = 0;

                        vectorPlateInfoType = service.getValidPlatesByRegion(request);
                    }

                    String responseData = vectorPlateInfoType != null ? StringUtil.escapeQuotes(vectorPlateInfoType.toString()) : "";
                    RequestLogTask task = new RequestLogTask(config.getParams(), config.getRequestMode(), responseData);
                    task.execute();

                    parkings = new ArrayList<>();
                    if (vectorPlateInfoType != null) {
                        Enumeration<PlateInfoType> enumParking = vectorPlateInfoType.elements();
                        while (enumParking.hasMoreElements()) {
                            PlateInfoType plateInfoType = (PlateInfoType) enumParking.nextElement();
                            parkings.add(plateInfoType);
                        }
                    }

                    Message msg = lookupHandler.obtainMessage();
                    msg.what = 1;

                    progressDialog.dismiss();
                    lookupHandler.sendMessage(msg);
                }).start();
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
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

                initDatagrid();
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

    public void searchAction(View view) {
        if (searchEditText.getVisibility() == View.GONE) {
            searchEditText.setVisibility(View.VISIBLE);
            TPUtility.showSoftKeyboard(this, searchEditText);
        } else {
            searchEditText.setVisibility(View.GONE);
        }
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
        bindDataAtLoadingTime();
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

    public void displayDigitalPayTechInfoMsg(final PlateInfoType parking) {
        try {
            StringBuffer message = new StringBuffer();
            StringBuffer values = new StringBuffer();


            message.append("Status" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.status.toString()) + "\n");


            message.append("Region" + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.regionName) + "\n");


            if (parking.getExpireInfo().isExpired()) {
                message.append("Expired" + "\n");
                values.append(": " + StringUtil.getDisplayString(parking.getExpireInfo().getExpireMsg()) + "\n");
            } else {
                message.append("Expire" + "\n");
                values.append(": " + StringUtil.getDisplayString(parking.getExpireInfo().getExpireMsg()) + "\n");
            }
			
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
            dialog.setTitle("Parking Info - " + parking.plateNumber);
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
                        ticket.setPlate(parking.plateNumber);

                        TPApp.setActiveTicket(ticket);

                        Intent intent = new Intent();
                        intent.setClass(DigitalPaytechZoneInfoActivity.this, WriteTicketActivity.class);
                        startActivity(intent);
                    }
                });
            }

            TPUtility.applyButtonStyles(dialog.show());

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }
}
