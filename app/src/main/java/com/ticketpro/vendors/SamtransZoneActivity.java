package com.ticketpro.vendors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ticketpro.api.TokenGenerate;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Vendor;
import com.ticketpro.model.VendorItem;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SamtransZoneActivity extends BaseActivityImpl {
    private ProgressDialog progressDialog;
    private ListView listView;
    private EditText searchEditText;
    private ArrayList<VendorItem> zoneItems;
    private ArrayList<VendorItem> filteredItems = new ArrayList<>();
    private Preference preference;

    public static SamtransZoneActivity samtransZoneActivity;

    public static SamtransZoneActivity getInstanc() {
        if (samtransZoneActivity == null) {
            samtransZoneActivity = new SamtransZoneActivity();
        }
        return samtransZoneActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_list);
        samtransZoneActivity = this;
        preference = Preference.getInstance(this);
        listView = (ListView) findViewById(R.id.zone_listview);
        listView.setScrollbarFadingEnabled(false);
        listView.setFastScrollEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
                Intent intent = new Intent();
                intent.setClass(SamtransZoneActivity.this, SamtransZoneActiviyInfo.class);
                intent.putExtra("ZoneName", filteredItems.get(pos).getItemName());
                intent.putExtra("ZoneCode", "");
                intent.putExtra("LocationCode", "");
                preference.putString(TPConstant.CURRENT_SAMTRANS_SPACE,filteredItems.get(pos).getItemName());
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
                    for (VendorItem zoneInfo : zoneItems) {
                        zoneName = zoneInfo.getItemName();
                        if (!StringUtil.isEmpty(zoneName)) {
                            if (!TPApp.getUserSettings().isSearchContains() && zoneName.toLowerCase().startsWith(searchText)) {
                                filteredItems.add(zoneInfo);
                            } else if (zoneName.toLowerCase().contains(searchText)) {
                                filteredItems.add(zoneInfo);
                            }
                        }

                        zoneName = zoneInfo.getItemName();
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


        _samTransGetZone();

    }

    @Override
    public void onClick(View v) {
        backAction(null);
    }

    public void backAction(View view) {
        finish();
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

    private void _samTransGetZone() {
        try {
            if (preference.getString(TPConstant.SAMTRANS_TOKEN)==null){
                _getToken();
            }
            //_getToken();

            Vendor samtrans = Vendor.getVendorByName("Samtrans");
            if (samtrans.getVendorId()>0){

                filteredItems = VendorItem.getVendorSamtrans(samtrans.getVendorId());
                zoneItems = filteredItems;
                displayItems(filteredItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void _getToken() {

        try {
            if (Feature.isFeatureAllowed(Feature.TOGGLE_NET_ON_OFF) && TPApplication.getInstance().getNetOnOff().equals("Y")){
                return;
            }
            if (isNetworkConnected()) {
                VendorServiceConfig config = VendorService.getServiceConfigCale(VendorService.SAMTRANS_TOKEN, TPApp.deviceId, "/");
                Map<String, String> paramsMap = config.getParamsMap();
                String user = paramsMap.get("User");
                String password = paramsMap.get("Password");
                String serviceURL = config.getServiceURL();

                new TokenGenerate(this, serviceURL,user,password).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.gc();
    }

    private void displayItems(ArrayList<VendorItem> zoneItems) {
        // create the grid item mapping
        String[] from = new String[]{"search_title"};
        int[] to = new int[]{R.id.search_textview};

        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < zoneItems.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("search_title", "" +  zoneItems.get(i).getItemName());//zoneItems.get(i).getDescription()
            fillMaps.add(map);
        }

        filteredItems = (ArrayList<VendorItem>) zoneItems.clone();

        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(SamtransZoneActivity.this, fillMaps, R.layout.search_list_item, from, to);
        listView.setAdapter(adapter);
    }

}
