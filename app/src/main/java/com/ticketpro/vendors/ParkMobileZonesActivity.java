package com.ticketpro.vendors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.service.TPAsyncTask;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ParkMobileZonesActivity extends BaseActivityImpl {
    private Handler dataLoadHandler;
    private Handler errorHandler;

    private ListView listView;
    private EditText searchEditText;
    private ArrayList<ParkMobileZoneInfo> zoneItems;
    private ArrayList<ParkMobileZoneInfo> filteredItems = new ArrayList<ParkMobileZoneInfo>();
    private Preference preference;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.zone_list);
            setLogger(ParkMobileZonesActivity.class.getName());
            preference = Preference.getInstance(this);
            listView = (ListView) findViewById(R.id.zone_listview);
            listView.setScrollbarFadingEnabled(false);
            listView.setFastScrollEnabled(true);
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
                    Intent intent = new Intent();
                    intent.setClass(ParkMobileZonesActivity.this, ParkMobileZoneInfoActivity.class);
                    intent.putExtra("ZoneName", filteredItems.get(pos).getDescription());
                    intent.putExtra("ZoneCode", filteredItems.get(pos).getSignageZoneCode());
                    intent.putExtra("LocationCode", filteredItems.get(pos).getLocationCode());

                    preference.putString("ZoneCode", filteredItems.get(pos).getSignageZoneCode());
                    startActivity(intent);
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
                    String zoneName;
                    filteredItems.clear();
                    if (searchText.length() == 0) {
                        filteredItems.addAll(zoneItems);
                    } else {
                        searchText = searchText.toLowerCase();
                        for (ParkMobileZoneInfo zoneInfo : zoneItems) {
                            zoneName = zoneInfo.getDescription();
                            if (!StringUtil.isEmpty(zoneName)) {
                                if (!TPApp.getUserSettings().isSearchContains() && zoneName.toLowerCase().startsWith(searchText)) {
                                    filteredItems.add(zoneInfo);
                                } else if (zoneName.toLowerCase().contains(searchText)) {
                                    filteredItems.add(zoneInfo);
                                }
                            }

                            zoneName = zoneInfo.getSignageZoneCode();
                            if (!StringUtil.isEmpty(zoneName)) {
                                if (!TPApp.getUserSettings().isSearchContains() && zoneName.toLowerCase().startsWith(searchText)) {
                                    filteredItems.add(zoneInfo);
                                } else if (zoneName.toLowerCase().contains(searchText)) {
                                    filteredItems.add(zoneInfo);
                                }
                            }
                        }
                    }

                    displayItems(filteredItems);
                }
            });


            dataLoadHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    displayItems(zoneItems);
                }
            };

            errorHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    Toast.makeText(ParkMobileZonesActivity.this, "Error loading zones information.", Toast.LENGTH_SHORT).show();
                }
            };

            bindDataAtLoadingTime();
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() {

        TPAsyncTask task = new TPAsyncTask(this, "Loading...");
        task.execute(new TPTask() {
            @Override
            public void execute() {
                try {
                    VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PARK_MOBILE_ZONELIST, TPApp.deviceId, "/");
                    if (config != null) {
                        String responseJSON = TPUtility.getURLResponse(config.getServiceURL() + "/" + config.getParams() + "?format=json", config.getUsername(), config.getPassword().trim());
                        zoneItems = ParkMobileParser.getZones(responseJSON);
                        dataLoadHandler.sendEmptyMessage(1);
                    } else {
                        errorHandler.sendEmptyMessage(0);
                    }
                } catch (Exception ae) {
                    log.error(ae.getMessage());
                    errorHandler.sendEmptyMessage(0);
                }
            }
        });


        TPUtility.hideSoftKeyboard(this);
    }

    private void displayItems(ArrayList<ParkMobileZoneInfo> zoneItems) {
        // create the grid item mapping
        String[] from = new String[]{"search_title"};
        int[] to = new int[]{R.id.search_textview};

        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < zoneItems.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("search_title", zoneItems.get(i).getDescription() + " (" + zoneItems.get(i).getSignageZoneCode() + ") ");
            fillMaps.add(map);
        }

        filteredItems = (ArrayList<ParkMobileZoneInfo>) zoneItems.clone();

        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(ParkMobileZonesActivity.this, fillMaps, R.layout.search_list_item, from, to);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        backAction(null);
    }

    public void backAction(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backAction(null);
            return false;
        }

        return false;
    }

    @Override
    public void handleVoiceInput(String text) {

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        // TODO Auto-generated method stub
    }
}
