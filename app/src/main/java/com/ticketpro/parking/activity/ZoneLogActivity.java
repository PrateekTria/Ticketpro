package com.ticketpro.parking.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.Feature;
import com.ticketpro.model.MobileNowLog;
import com.ticketpro.model.MobileNowZoneInfo;
import com.ticketpro.model.MobileNowZoneItem;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.api.WriteTicketNetworkCalls;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.util.CSVUtility;
import com.ticketpro.util.MobileNowParser;
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

public class ZoneLogActivity extends BaseActivityImpl {

    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    private TableLayout tableLayout;
    private MobileNowZoneInfo zoneInfo;
    private int sortBy = 0;
    private int sortOrder = 0;

    private ProgressDialog progressDialog;
    private Handler lookupHandler;
    private String zoneName;
    private String zoneCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.zone_logs);
            setActiveScreen(this);

            setLogger(ZoneLogActivity.class.getName());
            setBLProcessor(new CommonBLProcessor((TPApplication) getApplicationContext()));
            tableLayout = (TableLayout) findViewById(R.id.logs_4_table_view);

            lookupHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    String response = (String) msg.obj;
                    zoneInfo = MobileNowParser.getZoneInfo(response);
                    initDatagrid();
                }
            };

            zoneName = getIntent().getStringExtra("ZoneName");
            zoneCode = getIntent().getStringExtra("ZoneCode");

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void initDatagrid() {
        try {
            if (zoneInfo == null) {
                return;
            }

            if (zoneName != null) {
                ((TextView) findViewById(R.id.zone_info_text)).setText("Zone Info - " + zoneName);
            }

            tableLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

            //adding Header
            View headerRow = inflater.inflate(R.layout.table_row_3_coulm_status_view, null);

            TextView plateColumn = ((TextView) headerRow.findViewById(R.id.tr_header1));
            plateColumn.setText("Plate/Space");
            plateColumn.setClickable(true);
            plateColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(zoneInfo.getZoneItems(), new MobileNowZoneItem.ItemComparator());

                    //Update Sorting Order
                    if (sortBy != 1) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(zoneInfo.getZoneItems());
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 1;
                    initDatagrid();
                }
            });

            TextView dateTime = ((TextView) headerRow.findViewById(R.id.tr_header2));
            dateTime.setText("Info");
            dateTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(zoneInfo.getZoneItems(), new MobileNowZoneItem.ResponseTypeComparator());

                    //Update Sorting Order
                    if (sortBy != 2) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(zoneInfo.getZoneItems());
                    } else {
                        sortOrder = ASC_ORDER;
                    }

                    sortBy = 2;
                    initDatagrid();
                }
            });

            TextView statusColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
            statusColumn.setText("Expire");
            statusColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(zoneInfo.getZoneItems(), new MobileNowZoneItem.EndDateComparator());

                    //Update Sorting Order
                    if (sortBy != 3) {
                        sortOrder = ASC_ORDER;
                    } else if (sortOrder == ASC_ORDER) {
                        sortOrder = DESC_ORDER;
                        Collections.reverse(zoneInfo.getZoneItems());
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
                        plateColumn.setText("Plate/Space \u25BC");
                    else
                        plateColumn.setText("Plate/Space \u25B2");
                    break;

                case 2:
                    if (sortOrder == ASC_ORDER)
                        dateTime.setText("Info \u25BC");
                    else
                        dateTime.setText("Info \u25B2");
                    break;

                case 3:
                    if (sortOrder == ASC_ORDER)
                        statusColumn.setText("Expire \u25BC");
                    else
                        statusColumn.setText("Expire \u25B2");
                    break;
            }

            tableLayout.addView(headerRow);

            int i = 0;
            for (MobileNowZoneItem item : zoneInfo.getZoneItems()) {
                View tableRow = inflater.inflate(R.layout.table_row_3_coulm_status_view, null);

                ImageView statusIcon = ((ImageView) tableRow.findViewById(R.id.tr_status_img));
                statusIcon.setBackgroundResource(R.drawable.color_green);

                String expireStr = "";
                long diffMinutes, diffHours, diffDays;
                long expiredDiff = zoneInfo.getSysDate().getTime() - item.getEndTime().getTime();
                if (expiredDiff > 0) {
                    diffMinutes = expiredDiff / (60 * 1000) % 60;
                    diffHours = expiredDiff / (60 * 60 * 1000) % 24;
                    diffDays = expiredDiff / (24 * 60 * 60 * 1000);
                    if (diffDays >= 1) {
                        expireStr = diffDays + " days " + diffHours + " hrs ago";

                    } else if (diffHours >= 1) {
                        expireStr = diffHours + " hrs " + diffMinutes + " mins ago";

                    } else {
                        expireStr = diffMinutes + " mins ago";
                    }

                    statusIcon.setBackgroundResource(R.drawable.color_red);
                    ((TextView) tableRow.findViewById(R.id.tr_header1)).setTextColor(Color.RED);
                    ((TextView) tableRow.findViewById(R.id.tr_header2)).setTextColor(Color.RED);
                    ((TextView) tableRow.findViewById(R.id.tr_header3)).setTextColor(Color.RED);

                } else {
                    expiredDiff = Math.abs(expiredDiff);
                    diffMinutes = expiredDiff / (60 * 1000) % 60;
                    diffHours = expiredDiff / (60 * 60 * 1000) % 24;
                    diffDays = expiredDiff / (24 * 60 * 60 * 1000);

                    if (diffDays >= 1) {
                        expireStr = "in " + diffDays + " days " + diffHours + " hrs";

                    } else if (diffHours >= 1) {
                        expireStr = "in " + diffHours + " hrs " + diffMinutes + " mins";

                    } else {
                        expireStr = "in " + diffMinutes + " mins";
                    }

                    if (diffMinutes <= 3) {
                        statusIcon.setBackgroundResource(R.drawable.color_yellow);
                    }
                }

                ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(item.getItem() + "");
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(item.getResponseType() + "");
                ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(expireStr);

                if ((i % 2) == 0)
                    tableRow.setBackgroundResource(R.drawable.tablerow_even);
                else
                    tableRow.setBackgroundResource(R.drawable.tablerow_odd);

                tableLayout.addView(tableRow);
                i++;
            }

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {
        try {
            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.MOBILE_NOW_ZONE_CHECK, TPApp.userId);
            if (config != null) {
                progressDialog = ProgressDialog.show(this, "", "Checking Zone Info...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String params = config.getParams();
                        params = params.replaceAll("\\{ZONE\\}", zoneCode);

                        try {
                            String response = TPUtility.getURLResponse(config.getServiceURL() + "?" + params, true);

                            Message msg = lookupHandler.obtainMessage();
                            msg.what = 1;
                            msg.obj = response;

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
                                    CSVUtility.writeMobileLogCSV(log);
                                    /*DatabaseHelper.getInstance().openWritableDatabase();
                                    DatabaseHelper.getInstance().insertOrReplace(log.getContentValues(), "mobile_now_logs");
                                    DatabaseHelper.getInstance().closeWritableDb();*/

                                    ArrayList<MobileNowLog> logs = new ArrayList<MobileNowLog>();
                                    logs.add(log);
                                    WriteTicketNetworkCalls.sendMobileNogLogs(logs);
                                    /*((CommonBLProcessor) screenBLProcessor).sendMobileNowLog(logs);*/

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            lookupHandler.sendMessage(msg);

                        } catch (IOException e) {
                            log.error("IOException " + TPUtility.getPrintStackTrace(e));
                        }

                        progressDialog.dismiss();
                    }
                }).start();
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
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

}
