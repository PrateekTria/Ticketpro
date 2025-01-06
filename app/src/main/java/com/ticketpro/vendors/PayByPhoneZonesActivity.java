package com.ticketpro.vendors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ticketpro.model.VendorItem;
import com.ticketpro.model.VendorService;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.service.TPAsyncTask;
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

public class PayByPhoneZonesActivity extends BaseActivityImpl {
    private Handler dataLoadHandler;
    private Handler errorHandler;

    private ListView listView;
    private EditText searchEditText;
    private ArrayList<PayByPhoneZoneItem> zoneItems;
    private ArrayList<PayByPhoneZoneItem> filteredItems = new ArrayList<PayByPhoneZoneItem>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.zone_list);
            setLogger(PayByPhoneZonesActivity.class.getName());

            listView = findViewById(R.id.zone_listview);
            listView.setScrollbarFadingEnabled(false);
            listView.setFastScrollEnabled(true);
            listView.setOnItemClickListener((adapterView, view, pos, arg3) -> {
                Intent intent = new Intent();
                intent.setClass(PayByPhoneZonesActivity.this, PayByPhoneZoneInfoActivity.class);
                intent.putExtra("ZoneName", filteredItems.get(pos).getZoneName());
                intent.putExtra("ZoneCode", filteredItems.get(pos).getZoneNumber());
                startActivity(intent);
                TPApplication.getInstance().isVendorCode = false;
            });

            searchEditText = findViewById(R.id.searchText);
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
                        for (PayByPhoneZoneItem zoneInfo : zoneItems) {
                            zoneName = zoneInfo.getZoneName();
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

                    Toast.makeText(PayByPhoneZonesActivity.this, "Error loading zones information.", Toast.LENGTH_SHORT).show();
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
                    zoneItems = new ArrayList<PayByPhoneZoneItem>();
                    VendorService service = VendorService.getServiceByName(VendorService.PAYBYPHONE_ZONEINFO);
                    ArrayList<VendorItem> items = VendorItem.getVendorZones(service.getVendorId());
                    for (VendorItem item : items) {
                        PayByPhoneZoneItem zoneItem = new PayByPhoneZoneItem();
                        zoneItem.setZoneName(item.getItemName());
                        zoneItem.setZoneNumber(item.getItemCode());
                        zoneItems.add(zoneItem);
                    }

                    dataLoadHandler.sendEmptyMessage(1);

                } catch (Exception ae) {
                    log.error(ae.getMessage());
                    errorHandler.sendEmptyMessage(0);
                }
            }
        });

        TPUtility.hideSoftKeyboard(this);
    }


    private void displayItems(ArrayList<PayByPhoneZoneItem> zoneItems) {
        // create the grid item mapping
        String[] from = new String[]{"search_title"};
        int[] to = new int[]{R.id.search_textview};

        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < zoneItems.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("search_title", zoneItems.get(i).getZoneName());
            fillMaps.add(map);
        }

        filteredItems = (ArrayList<PayByPhoneZoneItem>) zoneItems.clone();

        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.search_list_item, from, to);
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
