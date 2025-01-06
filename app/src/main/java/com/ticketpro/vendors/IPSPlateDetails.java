package com.ticketpro.vendors;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ticketpro.model.Feature;
import com.ticketpro.model.VendorItem;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.bl.CommonBLProcessor;
import com.ticketpro.parking.service.TPAsyncTask;
import com.ticketpro.util.DateUtil;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */
public class IPSPlateDetails extends BaseActivityImpl {
    private final int ASC_ORDER = 1;
    private final int DESC_ORDER = 2;
    private TableLayout tableLayout;
    private TextView expiringTextView;
    private ArrayList<IPSParkingPlate> parkings;
    private ArrayList<IPSParkingPlate> parkingSpaces;
    private ArrayList<IPSParkingPlate> filteredItems = new ArrayList<>();
    private int sortBy = 2;
    private int sortOrder = 1;

    private Handler lookupHandler;
    private String zoneName;
    private String zoneCode;
    private EditText searchEditText;
    private TextView expiredTextView;

    private CheckBox expiredCheckbox;
    private Button pageSizeButton;
    private int pageSize = -1;
    private String lotNumber = "";
    private String lotDesc = "";
    private Dialog lookupDialog;
    private TextView lotNameTv;
    private EditText inputText;
    private Dialog dialog;
    private String defaultExpiryMinute = "0";
    private CheckBox hourMinuteToggle;
    private boolean hourMinuteToggleChecked;
    private String spaceGroup;
    private String expiryDuration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.zone_logs_multispace);
            setActiveScreen(this);

            setLogger(IPSLotDetails.class.getName());
            setBLProcessor(new CommonBLProcessor((TPApplication) getApplicationContext()));
            tableLayout = (TableLayout) findViewById(R.id.logs_4_table_view);

            spaceGroup = getIntent().getStringExtra("spaceGroup");
            pageSizeButton = (Button) findViewById(R.id.pageSizeBtn);
            pageSizeButton.setVisibility(View.VISIBLE);

            if (spaceGroup.equalsIgnoreCase("spaceGroup")) {
                pageSizeButton.setText("Change Lot");
            } else
                pageSizeButton.setText("Change Area");

            expiredCheckbox = (CheckBox) findViewById(R.id.expiredChk);
            expiredTextView = (TextView) findViewById(R.id.expiredTextView);
            expiringTextView = (TextView) findViewById(R.id.expiringTextView);
            expiryDuration = Feature.getFeatureValue(Feature.IPS_MULTISPACE_EXPIRY_MIN);

            expiredCheckbox.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (Feature.isFeatureAllowed(Feature.IPS_MULTISPACE_EXPIRY_MIN)) {
                        String expiryDuration = Feature.getFeatureValue(Feature.IPS_MULTISPACE_EXPIRY_MIN);
                        editExpiryTimes(expiryDuration);
                    }
                    return false;
                }
            });

            expiredTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Feature.isFeatureAllowed(Feature.IPS_MULTISPACE_EXPIRY_MIN)) {
                        expiredCheckbox.setChecked(true);
                        editExpiryTimes(expiryDuration);
                    }
                }
            });

            expiredCheckbox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expiredCheckbox.isChecked()) {
                        if (Feature.isFeatureAllowed(Feature.IPS_MULTISPACE_EXPIRY_MIN)) {
                            String expiryDuration = Feature.getFeatureValue(Feature.IPS_MULTISPACE_EXPIRY_MIN);
                            editExpiryTimes(expiryDuration);
                        } else {
                            loadData();
                        }
                    } else {
                        hourMinuteToggleChecked = false;
                        defaultExpiryMinute = "";
                        expiringTextView.setText("");
                        bindDataAtLoadingTime();
                    }
                }
            });

            searchEditText = (EditText) findViewById(R.id.searchText);
            searchEditText.setVisibility(View.GONE);
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
                        for (IPSParkingPlate parking : parkings) {
                            String spaceNumber = parking.getLicensePlateNumber();

                            if (!StringUtil.isEmpty(spaceNumber)) {
                                if (spaceNumber.toLowerCase().startsWith(searchText)) {
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
                        IPSParkingPlate ipsParkingSpace = new IPSParkingPlate();
                        parkings = ipsParkingSpace.getIPSParkingPlateResult(responseJSON);
                        parkingSpaces = ipsParkingSpace.getIPSParkingPlateResult(responseJSON);
                        if (parkings == null) {
                            Toast.makeText(getApplicationContext(), "Not able to fetch data...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            if (!StringUtil.isEmpty(defaultExpiryMinute)) {
                                if (hourMinuteToggleChecked) {
                                    inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                                    hourMinuteToggle.setText(getResources().getText(R.string.sortByHour));
                                    hourMinuteToggle.setChecked(true);
                                    inputText.setText(expiryDuration);
                                    hourMinuteToggleChecked = true;
                                } else {
                                    inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
                                    hourMinuteToggle.setText(getResources().getText(R.string.sortByMinute));
                                    inputText.setText(expiryDuration);
                                    hourMinuteToggle.setChecked(false);
                                    hourMinuteToggleChecked = false;
                                }


                                ipsParkingSpace = new IPSParkingPlate();
                                if (hourMinuteToggle.isChecked()) {
                                    parkings = ipsParkingSpace.sortIPSResultByInputHour(parkingSpaces, Long.parseLong(defaultExpiryMinute));
                                    expiringTextView.setText("(" + defaultExpiryMinute + " H)");
                                    hourMinuteToggleChecked = true;
                                } else {
                                    parkings = ipsParkingSpace.sortIPSResultByInputMinutes(parkingSpaces, Long.parseLong(defaultExpiryMinute));
                                    expiringTextView.setText("(" + defaultExpiryMinute + " M)");
                                    hourMinuteToggleChecked = false;
                                }
                                if (parkings.size() < 1) {
                                    initDatagrid(parkings);
                                    //Toast.makeText(getApplicationContext(), "No Plate Found", Toast.LENGTH_SHORT).show();
                                } else
                                    initDatagrid(parkings);
                                if (dialog.isShowing())
                                    dialog.dismiss();
                            } else {
                                //Toast.makeText(IPSLotDetails.this, "Enter value to sort", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Collections.sort(parkings, new IPSParkingPlate.PlateComparator());
                    } catch (Exception e) {
                        log.error(TPUtility.getPrintStackTrace(e));
                    } finally {
                        initDatagrid(parkings);
                    }
                }
            };

            lotNumber = getIntent().getStringExtra(TPConstant.LOT_NAME);
            lotDesc = getIntent().getStringExtra(TPConstant.LOT_DESC);

            //zoneCode = getIntent().getStringExtra("ZoneCode");
            lotNameTv = (TextView) findViewById(R.id.zone_info_text);
            if (lotNumber != null) {
                lotNameTv.setText(lotDesc);
            }

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void initDatagrid(final ArrayList<IPSParkingPlate> parkings) {
        try {
            try {
                tableLayout.removeAllViews();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (parkings == null) {
                return;
            }
            if (parkings.size() == 0) {
                return;
            }
            LayoutInflater inflater = LayoutInflater.from(this);

            //adding Header
            View headerRow = inflater.inflate(R.layout.table_row_parking_multiplate, null);

            TextView infoColumn = ((TextView) headerRow.findViewById(R.id.tr_header2));
            infoColumn.setText("Plate");
            infoColumn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {
                        Collections.sort(parkings, new IPSParkingPlate.PlateComparator());

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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            TextView statusColumn = ((TextView) headerRow.findViewById(R.id.tr_header3));
            statusColumn.setText("Start Time");
            statusColumn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {
                        Collections.sort(parkings, new IPSParkingPlate.StartTimeComparator());

                        //Update Sorting Order
                        if (sortBy != 3) {
                            sortOrder = ASC_ORDER;
                        } else if (sortOrder == ASC_ORDER) {
                            sortOrder = DESC_ORDER;
                            //Collections.sort(parkings, new IPSParkingPlate.ExpireComparator());
                            Collections.reverse(parkings);
                        } else {
                            sortOrder = ASC_ORDER;
                        }

                        sortBy = 3;
                        initDatagrid(parkings);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            TextView expColumn = ((TextView) headerRow.findViewById(R.id.tv_lot));
            //lotColumn.setVisibility(View.GONE);

            expColumn.setText("End Time");
            expColumn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {
                        Collections.sort(parkings, new IPSParkingPlate.ExpireComparator());

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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            switch (sortBy) {
                case 1:
/*
					if(sortOrder==ASC_ORDER)
						plateColumn.setText("LPN \u25BC");
					else
						plateColumn.setText("LPN \u25B2");
*/
                    break;

                case 2:
                    if (sortOrder == ASC_ORDER)
                        infoColumn.setText("Plate \u25BC");
                    else
                        infoColumn.setText("Plate \u25B2");
                    break;

                case 3:
                    if (sortOrder == ASC_ORDER)
                        statusColumn.setText("Start Time \u25BC");
                    else
                        statusColumn.setText("Start Time \u25B2");
                    break;

                case 4:
                    if (sortOrder == ASC_ORDER)
                        expColumn.setText("End Time \u25BC");
                    else
                        expColumn.setText("End Time \u25B2");
                    break;
            }

            tableLayout.addView(headerRow);

            int index = 0;
            for (IPSParkingPlate item : parkings) {
                View tableRow = inflater.inflate(R.layout.table_row_parking_multiplate, null);

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
                        IPSParkingPlate parking = parkings.get(index);
                        displayParkingInfoMsg(parking);
                    }
                });

                tableRow.setTag(R.id.ListIndex, index);
                tableRow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = (Integer) v.getTag(R.id.ListIndex);
                        IPSParkingPlate parking = parkings.get(index);
                        displayParkingInfoMsg(parking);
                    }
                });

                try {
                    if (item.getParkingExpiryTime() != null) {
                        ParkingExpireInfo expireInfo = item.getExpireInfo(DateUtil.getDateFromUTCStringMultiSpace(item.getParkingExpiryTime()));
                        if (expireInfo.isExpired()) {
                            statusIcon.setBackgroundResource(R.drawable.color_red);
                        } else if (expireInfo.getDiffMinutes() <= 3 && expireInfo.getDiffHrs() == 0 && expireInfo.getDiffDays() == 0) {
                            statusIcon.setBackgroundResource(R.drawable.color_yellow);
                        }
                        ((TextView) tableRow.findViewById(R.id.tr_header3)).setText(DateUtil.getDateInMOnthDayFormat(item.getParkingStartTime()));
                    } else {
                        log.error("no expiry for space" + item.getLicensePlateNumber());
                    }
                } catch (Exception e) {
                    log.error(e);
                    e.printStackTrace();
                }

                //((TextView) tableRow.findViewById(R.id.tr_header3)).setText(DateUtil.getDateInMOnthDayFormat(item.getParkingStartTime()));
                ((TextView) tableRow.findViewById(R.id.tr_header2)).setText(item.getLicensePlateNumber());
                ((TextView) tableRow.findViewById(R.id.tv_lot)).setText(DateUtil.getDateInMOnthDayFormat(item.getParkingExpiryTime()));

                if ((index % 2) == 0) {
                    tableRow.setBackgroundResource(R.drawable.tablerow_even);
                } else {
                    tableRow.setBackgroundResource(R.drawable.tablerow_odd);
                }

                //if(expiredCheckbox.isChecked()==false || expireInfo.isExpired()){
                tableLayout.addView(tableRow);
                index++;
                //}
	        	/*if(pageSize > 0 && index >= pageSize){
	        		break;
	        	}*/
            }
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {
        loadData();
    }

    public void loadData() {
        TPAsyncTask task = new TPAsyncTask(this, "Checking IPS Lot Info...");
        task.execute(new TPTask() {
            @Override
            public void execute() {
                VendorServiceConfig config = null;
                String ipsValue = Feature.getFeatureValue(Feature.IPS_GROUP);
                if (ipsValue == null) {
                    ipsValue = "";
                }
                if (spaceGroup == null)
                    return;
                if (spaceGroup.equalsIgnoreCase("spaceGroup")) {
                    config = VendorService.getServiceConfig(VendorService.IPS_SPACEINFO, TPApp.deviceId);
                } else if (ipsValue.equalsIgnoreCase("plate")) {
                    config = VendorService.getServiceConfig(VendorService.IPS_PLATEBYSUBAREA, TPApp.deviceId);
                } else
                    config = VendorService.getServiceConfig(VendorService.IPS_MULTISPACE_GROUP, TPApp.deviceId);
                if (config != null) {
                    try {
                        String url = config.getServiceURL();
                        String token = config.getParamsMap().get("token");
                        if (parkings != null)
                            parkings.clear();
                        String response;
                        if (spaceGroup.equalsIgnoreCase("spaceGroup"))
                            response = IPSQuery.getSpaceGroupStatus(url, token, TPApp.IPSSpaceGroup);
                        else if (ipsValue.equalsIgnoreCase("plate")) {
                            response = IPSQuery.getPlatesBySubArea(url, token, lotNumber);
                        } else
                            response = IPSQuery.getMultiSpaceStatus(url, token, lotNumber);
                        Message msg = lookupHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = response;

                        lookupHandler.sendMessage(msg);
                    } catch (Exception e) {
                        //Toast.makeText(IPSPlateDetails.this, "Please click on refresh to try again...", Toast.LENGTH_SHORT).show();
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
        Builder builder = new Builder(this);
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
        bindDataAtLoadingTime();
        //loadData(false);
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

    private void displayParkingInfoMsg(final IPSParkingPlate parking) {
        try {
            StringBuilder message = new StringBuilder();
            StringBuilder values = new StringBuilder();

            message.append("Amount" + "\n");
            values.append(": ").append(StringUtil.getDisplayString(parking.getAmount())).append("\n");


            //message.append("SubArea" + "\n");
            //message.append("Time Purch" + "\n");


            //values.append(": " + StringUtil.getDisplayString(parking.getSubAreaName()) + "\n");
            //values.append(": " + StringUtil.getDisplayString(parking.getParkingStartTime()) + "\n");

            boolean isExpiredTicket = false;

            if (parking.getParkingExpiryTime() != null) {
                isExpiredTicket = true;
                message.append("Start Time" + "\n");
                message.append("End Time" + "\n");
                values.append(": ").append(parking.getParkingStartTime()).append("\n");
                values.append(": ").append(parking.getParkingExpiryTime()).append("\n");
            }

            if (parking.getParkingExpiryTime() == null) {
                message.append("Remaining" + "\n");
                values.append(": " + "" + "\n");
            } else
                message.append("Remaining" + "\n");
            values.append(": ").append(StringUtil.getDisplayString(parking.getExpireInfo(DateUtil.getDateFromUTCStringMultiSpace(parking.getParkingExpiryTime())).getExpireMsg())).append("\n");

            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_view, null);
            TextView headerTV = (TextView) view.findViewById(R.id.headerTV);
            TextView valueTV = (TextView) view.findViewById(R.id.valueTV);
            headerTV.setText(message.toString());
            valueTV.setText(values.toString());

            Builder dialog = new Builder(this);
            dialog.setCancelable(false);
            //dialog.setView(wv);
            dialog.setView(view);
            dialog.setTitle("Plate Info - " + StringUtil.getDisplayString(parking.getLicensePlateNumber()));
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            if (parking.getParkingExpiryTime() != null) {
                if (parking.getExpireInfo(DateUtil.getDateFromUTCStringMultiSpace(parking.getParkingExpiryTime())).isExpired()) {
                    dialog.setNegativeButton("Write Ticket", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

				        /*Ticket ticket=TPApp.createNewTicket();
				        ticket.setSpace(parking.getSpace());

				        TPApp.setActiveTicket(ticket);*/

                            Intent intent = new Intent();
                            intent.putExtra(TPConstant.PLATE, true);
                            intent.putExtra(TPConstant.SPACE, parking.getLicensePlateNumber());
                            intent.putExtra(TPConstant.LOCATION, lotDesc);
                            intent.putExtra(TPConstant.LOT_DESC, lotNameTv.getText().toString());

                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                }
            }
            TPUtility.applyButtonStyles(dialog.show());
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void changeLot(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View headerView = LayoutInflater.from(this).inflate(R.layout.dialog_header, null);
        final TextView titleView = (TextView) headerView.findViewById(R.id.header_title);
        if (spaceGroup == null)
            return;
        if (spaceGroup.equalsIgnoreCase("spaceGroup"))
            titleView.setText("Select Lot");
        else
            titleView.setText("Select Area");

        TextView groupName = new TextView(this);
        groupName.setText(TPApp.IPSSpaceGroup);

        LinearLayout actionLayout = (LinearLayout) headerView.findViewById(R.id.header_actions);
        actionLayout.addView(groupName);
        builder.setCustomTitle(headerView);

        try {

            VendorServiceConfig config;
            if (spaceGroup.equalsIgnoreCase("spaceGroup"))
                config = VendorService.getServiceConfig(VendorService.IPS_SPACEINFO, TPApp.deviceId);
            else
                config = VendorService.getServiceConfig(VendorService.IPS_MULTISPACE_GROUP, TPApp.deviceId);

            if (config != null) {
                final ArrayList<VendorItem> groups;
                if (spaceGroup.equalsIgnoreCase("spaceGroup"))
                    groups = VendorItem.getVendorItems(config.getVendorId(), "SpaceGroup");
                else
                    groups = VendorItem.getVendorItems(config.getVendorId(), "SubArea");

                Collections.sort(groups, new SortByName());
                ListView listView = new ListView(this);
                String[] from = new String[]{"search_title"};
                int[] to = new int[]{R.id.search_textview};

                List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
                for (VendorItem item : groups) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("search_title", item.getItemName());
                    fillMaps.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.search_list_item, from, to);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> viewAdapter, View view, int pos, long arg3) {
                        //expiredCheckbox.setChecked(false);
                        expiringTextView.setText("");
                        VendorItem item = groups.get(pos);
                        if (spaceGroup.equalsIgnoreCase("spaceGroup"))
                            TPApp.IPSSpaceGroup = item.getItemCode();
                        else
                            TPApp.IPSSubArea = item.getItemCode();
                        lotNumber = item.getItemCode();
                        lotNameTv.setText(item.getItemName());
                        if (TPApp.isServiceAvailable()) {
                            if (parkings != null)
                                parkings.clear();
                            bindDataAtLoadingTime();
                        } else {
                            Toast.makeText(getApplicationContext(), "Network not available. Please Retry", Toast.LENGTH_SHORT).show();
                        }
                        lookupDialog.dismiss();
                    }
                });

                builder.setView(listView);
            }

            lookupDialog = builder.create();
            lookupDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void expiryTimePrompt(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View headerView = LayoutInflater.from(this).inflate(R.layout.dialog_header, null);
        final TextView titleView = (TextView) headerView.findViewById(R.id.header_title);
        titleView.setText("Select Lot");

        TextView groupName = new TextView(this);
        groupName.setText(TPApp.IPSSpaceGroup);

        LinearLayout actionLayout = (LinearLayout) headerView.findViewById(R.id.header_actions);
        actionLayout.addView(groupName);
        builder.setCustomTitle(headerView);

        try {
            VendorServiceConfig config = VendorService.getServiceConfig(VendorService.IPS_SPACEINFO, TPApp.deviceId);
            if (config != null) {
                final ArrayList<VendorItem> groups = VendorItem.getVendorItems(config.getVendorId(), "SpaceGroup");
                ListView listView = new ListView(this);
                String[] from = new String[]{"search_title"};
                int[] to = new int[]{R.id.search_textview};

                List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
                for (VendorItem item : groups) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("search_title", item.getItemName());
                    fillMaps.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.search_list_item, from, to);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> viewAdapter, View view, int pos, long arg3) {
                        VendorItem item = groups.get(pos);
                        if (spaceGroup.equalsIgnoreCase("spaceGroup"))
                            TPApp.IPSSpaceGroup = item.getItemCode();
                        else
                            TPApp.IPSSubArea = item.getItemCode();
                        lotNumber = item.getItemCode();
                        lotNameTv.setText(item.getItemName());
                        bindDataAtLoadingTime();
                        lookupDialog.dismiss();
                    }
                });

                builder.setView(listView);
            }

            lookupDialog = builder.create();
            lookupDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editExpiryTimes(final String previousSetting) {
        try {
            dialog = new Dialog(this);
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            final View inputDlgView = layoutInflater.inflate(R.layout.ips_multispace_input_dialog, null, false);
            dialog.setTitle("Set Expiry Time");
            dialog.setContentView(inputDlgView);
            dialog.show();

            Button enterBtn = (Button) inputDlgView.findViewById(R.id.add_location_input_dialog_enter_btn);
            Button clearButton = (Button) inputDlgView.findViewById(R.id.clearButton);
            clearButton.setVisibility(View.VISIBLE);
            inputText = (EditText) inputDlgView.findViewById(R.id.add_location_input_dialog_text_field);
            inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
            hourMinuteToggle = (CheckBox) inputDlgView.findViewById(R.id.hourMinuteToggle);

            if (hourMinuteToggleChecked) {
                inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                hourMinuteToggle.setText(getResources().getText(R.string.sortByHour));
                hourMinuteToggle.setChecked(true);
                inputText.setText(expiryDuration);
                hourMinuteToggleChecked = true;
            } else {
                inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
                hourMinuteToggle.setText(getResources().getText(R.string.sortByMinute));
                inputText.setText(expiryDuration);
                hourMinuteToggle.setChecked(false);
                hourMinuteToggleChecked = false;
            }

            hourMinuteToggle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hourMinuteToggle.isChecked()) {
                        inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                        hourMinuteToggle.setText(getResources().getText(R.string.sortByHour));
                        //inputText.setText("1");
                        hourMinuteToggleChecked = true;
                    } else {
                        inputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
                        hourMinuteToggle.setText(getResources().getText(R.string.sortByMinute));
                        //inputText.setText(Feature.getFeatureValue(Feature.IPS_MULTISPACE_EXPIRY_MIN));
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            inputText.setSelection(inputText.getText().length());
                        }
                    }, 50);
                }
            });

            inputText.setText(previousSetting);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    inputText.setSelection(previousSetting.length());
                }
            }, 50);
            inputText.setHint("Enter minutes/hour exipring in");
            inputText.setInputType(InputType.TYPE_CLASS_NUMBER);

            inputText.requestFocus();
            inputText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    inputText.setText("");
                    return false;
                }
            });
            clearButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputText.setText("");
                }
            });
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            //imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
            enterBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    defaultExpiryMinute = inputText.getText().toString();
                    expiryDuration = inputText.getText().toString();
                    if (!StringUtil.isEmpty(defaultExpiryMinute)) {
                        IPSParkingPlate ipsParkingSpace = new IPSParkingPlate();
                        if (hourMinuteToggle.isChecked()) {
                            parkings = ipsParkingSpace.sortIPSResultByInputHour(parkingSpaces, Long.parseLong(defaultExpiryMinute));
                            expiringTextView.setText("(" + defaultExpiryMinute + " H)");
                            hourMinuteToggleChecked = true;
                        } else {
                            parkings = ipsParkingSpace.sortIPSResultByInputMinutes(parkingSpaces, Long.parseLong(defaultExpiryMinute));
                            expiringTextView.setText("(" + defaultExpiryMinute + " M)");
                            hourMinuteToggleChecked = false;
                        }
                        if (parkings.size() < 1) {
                            initDatagrid(parkings);
                            //Toast.makeText(getApplicationContext(), "No Plate Found", Toast.LENGTH_SHORT).show();
                        } else {
                            Collections.sort(parkings, new IPSParkingPlate.ExpireComparator());
                            initDatagrid(parkings);
                        }
                        if (imm.isActive()) {
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
                        }
                        if (dialog.isShowing())
                            dialog.dismiss();
                    } else {
                        //Toast.makeText(IPSLotDetails.this, "Enter value to sort", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Button cancelBtn = (Button) inputDlgView.findViewById(R.id.add_location_input_dialog_cancel_btn);
            cancelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (imm.isActive()) {
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
                        }
                        if (dialog.isShowing())
                            dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void backAction(View view) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(IPSPlateDetails.this);
        confirmBuilder.setTitle("IPS MultiPlate").setMessage("Are you sure you want to go back?")
                .setCancelable(true).setNegativeButton(" No ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(" Yes ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                setResult(RESULT_CANCELED);
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog confirmAlert = confirmBuilder.create();
        confirmAlert.show();
    }

    class SortByName implements Comparator<VendorItem> {
        public int compare(VendorItem a, VendorItem b) {
            return a.getItemName().compareTo(b.getItemName());
        }
    }
}
