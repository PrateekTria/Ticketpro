package com.ticketpro.vendors;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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

import androidx.annotation.RequiresApi;

import com.ticketpro.model.Feature;
import com.ticketpro.model.ParkeonZoneInfo;
import com.ticketpro.model.Ticket;
import com.ticketpro.model.UserSetting;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPUtility;

import org.apache.commons.io.IOUtils;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ParkeonZoneActivityInfo extends BaseActivityImpl {

    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    private ProgressDialog progressDialog;
    private TableLayout tableLayout;
    private ArrayList<ParkeonZoneInfo> infoArrayList;
    private ArrayList<ParkeonZoneInfo> filteredItems = new ArrayList<>();
    private int sortBy = 3;
    private int sortOrder = 1;

    private Handler lookupHandler;
    private String zoneName;
    private String zoneCode;
    private EditText searchEditText;

    private CheckBox expiredCheckbox;
    private Button pageSizeButton;
    private int pageSize = -1;
    private int count = 0;
    private boolean nintySecond = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("HandlerLeak")
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
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        _initState(zoneCode, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                        for (ParkeonZoneInfo parking : infoArrayList) {
                            String plateNumber = parking.getPlate_number();

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
                    String key = "Parkeon-" + zoneCode;
                    try {
                        if (infoArrayList != null && infoArrayList.size() > 0) {
                            //Merge expired results
                            CachedResult cachedResult = (CachedResult) TPApp.cachedMap.getResults(key);
                            if (cachedResult != null && !cachedResult.hasExpired()) {
                                ArrayList<ParkeonZoneInfo> expiredParkings = (ArrayList<ParkeonZoneInfo>) cachedResult.getResults();
                                expiredParkings = getExpired(expiredParkings);
                                if (expiredParkings.size() > 0) {

                                    infoArrayList.addAll(expiredParkings);
                                }
                            }

                            TPApp.cachedMap.setResults(key, new CachedResult(infoArrayList));
                        }

                        Collections.sort(infoArrayList, new ParkeonZoneInfo.ExpireComparator());

                        ArrayList<ParkeonZoneInfo> parkeonZoneInfos = ParkeonZoneInfo._removeDuplicateValueFromArray(infoArrayList);

                        initDatagrid(parkeonZoneInfos);


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

            _initState(zoneCode, false);
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void _initState(String zoneCode, boolean processCached) throws IOException {

        if (processCached) {
            String key = "Parkeon-" + zoneCode;
            CachedResult cachedResult = (CachedResult) TPApp.cachedMap.getResults(key);
            if (cachedResult != null && !cachedResult.hasExpired()) {
                infoArrayList.clear();
                infoArrayList = (ArrayList<ParkeonZoneInfo>) cachedResult.getResults();
                initDatagrid(infoArrayList);
                return;
            }
        } else {
            if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                return;
            }
            if (isNetworkConnected()) {
                new ParkeonZoneInfoService(zoneCode).execute();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void bindDataAtLoadingTime() throws Exception {

    }

    @Override
    public void handleVoiceInput(String text) {

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {

    }

    private void initDatagrid(final ArrayList<ParkeonZoneInfo> parkings) {
        try {
            if (parkings == null) {
                progressDialog.dismiss();
                return;
            }
            if (parkings.get(0).getStart_date() == null) {
                progressDialog.dismiss();
                return;
            }
            try {
                tableLayout.removeAllViews();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
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
                    Collections.sort(infoArrayList, new ParkeonZoneInfo.PlateComparator());

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
            infoColumn.setText("Purchase");
           /* infoColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Collections.sort(infoArrayList,new ParkMobileParkingRight.SpaceComparator());

                    //Update Sorting Order
                    if(sortBy!=2){
                        sortOrder=ASC_ORDER;
                    }
                    else if(sortOrder==ASC_ORDER){
                        sortOrder=DESC_ORDER;
                        Collections.reverse(infoArrayList);
                    }
                    else{
                        sortOrder=ASC_ORDER;
                    }

                    sortBy=2;
                    initDatagrid(infoArrayList);
                }
            });
*/
            TextView statusColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
            statusColumn.setText("Expire");
            statusColumn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {
                        Collections.sort(infoArrayList, new ParkeonZoneInfo.ExpireComparator());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                        infoColumn.setText("Purchase \u25BC");
                    else
                        infoColumn.setText("Purchase \u25B2");
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
            for (ParkeonZoneInfo item : parkings) {
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
                        ParkeonZoneInfo parking = parkings.get(index);
                        displayParkingInfoMsg(parking);
                    }
                });

                tableRow.setTag(R.id.ListIndex, index);
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = ((Integer) v.getTag(R.id.ListIndex)).intValue();
                        ParkeonZoneInfo parking = parkings.get(index);
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

                ((TextView) tableRow.findViewById(R.id.tr_header1)).setText(item.getPlate_number());
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(Math.abs(DateUtil.getTimeDiffHours(item.getStart_date(), item.getEnd_date())) + " H");
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
            e.printStackTrace();
            log.error(TPUtility.getPrintStackTrace(e));
        }
        progressDialog.dismiss();
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
    public void onBackPressed() {
        backAction(null);
    }

    public void backAction(View view) {
        finish();
    }

    private ArrayList<ParkeonZoneInfo> getExpired(ArrayList<ParkeonZoneInfo> parkings) {
        ArrayList<ParkeonZoneInfo> expiredParkings = new ArrayList<>();
        for (ParkeonZoneInfo parking : parkings) {
            if (parking.getExpireInfo().isExpired()) {
                expiredParkings.add(parking);
            }
        }
        return expiredParkings;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void refreshAction(View view) {
        try {
            _initState(zoneCode, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayParkingInfoMsg(final ParkeonZoneInfo parking) {
        try {
            StringBuffer message = new StringBuffer();
            StringBuffer values = new StringBuffer();

            message.append("Zone ID" + "\n");
            message.append("Zone name" + "\n");
            message.append("Plate" + "\n");
            message.append("Start time" + "\n");
            message.append("End time" + "\n");
            message.append("Expire" + "\n");
            message.append("Purchased" + "\n");


            values.append(": " + StringUtil.getDisplayString(parking.getZone_id()) + "\n");
            values.append(": " + StringUtil.getDisplayString(zoneName) + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getPlate_number()) + "\n");
            values.append(": " + StringUtil.getDisplayString(DateUtil.getConvertedDateParkeon(parking.getStart_date())) + "\n");
            values.append(": " + StringUtil.getDisplayString(DateUtil.getConvertedDateParkeon(parking.getEnd_date())) + "\n");
            values.append(": " + StringUtil.getDisplayString(parking.getExpireInfo().getExpireMsg()) + "\n");
            values.append(": " + StringUtil.getDisplayString(Math.abs(DateUtil.getTimeDiffHours(parking.getStart_date(), parking.getEnd_date())) + " H") + "\n");
            // values.append(": $" + StringUtil.getDisplayString("" + parking.getAmount()) + "\n");

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
                        ticket.setPlate(parking.getPlate_number());
                        TPApp.setActiveTicket(ticket);

                        Intent intent = new Intent();
                        intent.setClass(ParkeonZoneActivityInfo.this, WriteTicketActivity.class);
                        intent.putExtra("PARKEON", true);
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

    private class ParkeonZoneInfoService extends AsyncTask<Void, Void, Void> {
        String zoneCode;
        private String response;


        public ParkeonZoneInfoService(String zoneCode) {
            this.zoneCode = zoneCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ParkeonZoneActivityInfo.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            VendorServiceConfig config = VendorService.getServiceConfigCale(VendorService.PARKEON, TPApp.deviceId, "/");

            if (config != null) {

                Map<String, String> paramsMap = config.getParamsMap();
                String user = paramsMap.get("User");
                String password = paramsMap.get("Password");

                String serviceURL = config.getServiceURL();
                String[] split = serviceURL.split(";");

                final String SOAP_ACTION = split[1];
                final String METHOD_NAME = "get";
                final String NAMESPACE = split[2];
                final String URL = split[0];

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("type", "tickets");
                Request.addProperty("condition", "zone_id=" + zoneCode);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(Request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.debug = true;

                try {
                    List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                    String authString = user + ":" + password;
                    headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode(authString.getBytes())));
                    androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
                    response = (String) envelope.getResponse();

                    /*try {
                        MobileNowLog log = new MobileNowLog();
                        log.setCustId(TPApp.custId);
                        log.setDeviceId(TPApp.deviceId);
                        log.setUserId(TPApp.userId);
                        log.setRequestDate(new Date());
                        log.setRequestParams("Parkeon Request: " + SOAP_ACTION + "/" + envelope + "/" + headerList);
                        log.setServiceMode(config.getRequestMode());
                        log.setResponseText(response);

                        DatabaseHelper.getInstance().openWritableDatabase();
                        DatabaseHelper.getInstance().insertOrReplace(log.getContentValues(), "mobile_now_logs");
                        DatabaseHelper.getInstance().closeWritableDb();

                        ArrayList<MobileNowLog> logs = new ArrayList<>();
                        logs.add(log);

                        ((CommonBLProcessor) screenBLProcessor).sendMobileNowLog(logs);
                    } catch (Exception e) {
                        log.error("Error " + TPUtility.getPrintStackTrace(e));
                        e.printStackTrace();
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                    nintySecond = true;
                    progressDialog.dismiss();
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                nintySecond = false;
                parseZoneXML();
            }
        }

        private void parseZoneXML() {

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser parser = null;
            infoArrayList = new ArrayList<>();
            try {
                parser = saxParserFactory.newSAXParser();
                try {
                    parser.parse(IOUtils.toInputStream(response), new DefaultHandler() {
                        protected boolean plate_number;
                        protected boolean zone_id;
                        protected boolean start_date;
                        protected boolean end_date;
                        protected boolean received_date;
                        ParkeonZoneInfo parkeonZoneInfo;
                        private StringBuilder data = null;

                        @Override
                        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                            super.startElement(uri, localName, qName, attributes);

                            if (qName.equalsIgnoreCase("row")) {
                                parkeonZoneInfo = new ParkeonZoneInfo();
                                if (infoArrayList == null)
                                    infoArrayList = new ArrayList<>();
                            } else if (qName.equalsIgnoreCase("plate_number")) {
                                plate_number = true;
                            } else if (qName.equalsIgnoreCase("zone_id")) {
                                zone_id = true;
                            } else if (qName.equalsIgnoreCase("start_date")) {
                                start_date = true;
                            } else if (qName.equalsIgnoreCase("end_date")) {
                                end_date = true;
                            } else if (qName.equalsIgnoreCase("received_date")) {
                                received_date = true;
                            }

                            data = new StringBuilder();

                        }

                        @Override
                        public void characters(char[] ch, int start, int length) throws SAXException {
                            super.characters(ch, start, length);
                            data.append(new String(ch, start, length));

                        }

                        @Override
                        public void endElement(String uri, String localName, String qName) throws SAXException {
                            super.endElement(uri, localName, qName);
                            if (plate_number) {
                                parkeonZoneInfo.setPlate_number(data.toString());
                                plate_number = false;
                            } else if (zone_id) {
                                parkeonZoneInfo.setZone_id(data.toString());
                                zone_id = false;
                            } else if (start_date) {
                                parkeonZoneInfo.setStart_date(data.toString());
                                start_date = false;
                            } else if (end_date) {
                                parkeonZoneInfo.setEnd_date(data.toString());
                                end_date = false;
                            } else if (received_date) {
                                parkeonZoneInfo.setReceived_date(data.toString());
                                received_date = false;
                            }

                            if (qName.equalsIgnoreCase("row")) {

                                infoArrayList.add(parkeonZoneInfo);
                            }
                        }
                    });

                    if (infoArrayList.size() > 0) {

                        initDatagrid(infoArrayList);
                        Message msg = lookupHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = response;

                        lookupHandler.sendMessage(msg);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

        }
    }

}
