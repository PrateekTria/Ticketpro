package com.ticketpro.vendors;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.model.ArrayOfEnforcementZone;
import com.ticketpro.model.EnforcementZone;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CaleZoneActivity extends BaseActivityImpl {
    private ProgressDialog progressDialog;
    private ListView listView;
    private EditText searchEditText;
    private ArrayList<EnforcementZone> zoneItems;
    private ArrayList<EnforcementZone> filteredItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_list);
        listView = (ListView) findViewById(R.id.zone_listview);
        listView.setScrollbarFadingEnabled(false);
        listView.setFastScrollEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
                Intent intent = new Intent();
                intent.setClass(CaleZoneActivity.this, CaleZoneInfoActivity.class);
                intent.putExtra("ZoneName", filteredItems.get(pos).getName());
                intent.putExtra("ZoneCode", "");
                intent.putExtra("LocationCode", "");
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
                    if (zoneItems!=null && zoneItems.size()>0) {
                        for (EnforcementZone zoneInfo : zoneItems) {
                            zoneName = zoneInfo.getDescription();
                            if (!StringUtil.isEmpty(zoneName)) {
                                if (!TPApp.getUserSettings().isSearchContains() && zoneName.toLowerCase().startsWith(searchText)) {
                                    filteredItems.add(zoneInfo);
                                } else if (zoneName.toLowerCase().contains(searchText)) {
                                    filteredItems.add(zoneInfo);
                                }
                            }

                            zoneName = zoneInfo.getName();
                            if (!StringUtil.isEmpty(zoneName)) {
                                if (!TPApp.getUserSettings().isSearchContains() && zoneName.toLowerCase().startsWith(searchText)) {
                                    filteredItems.add(zoneInfo);
                                } else if (zoneName.toLowerCase().contains(searchText)) {
                                    filteredItems.add(zoneInfo);
                                }
                            }
                        }
                    }
                }

                displayItems(filteredItems);
            }
        });


        _caleGetZone();

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

    private void _caleGetZone() {

        try {
            if (isNetworkConnected()) {
                VendorServiceConfig config = VendorService.getServiceConfigCale(VendorService.CALE_ZONE_LIST, TPApp.deviceId, "/");
                Map<String, String> paramsMap = config.getParamsMap();
                String user = paramsMap.get("User");
                String password = paramsMap.get("Password");
                String serviceURL = config.getServiceURL();

                if (config!=null) {

                    progressDialog = new ProgressDialog(CaleZoneActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    progressDialog.show();

                    ApiRequest service = ServiceGenerator.createService(ApiRequest.class,user,password);
                    service.getZone(serviceURL).enqueue(new Callback<ArrayOfEnforcementZone>() {
                        @Override
                        public void onResponse(Call<ArrayOfEnforcementZone> call, Response<ArrayOfEnforcementZone> response) {
                            if (response.isSuccessful()) {

                                List<EnforcementZone> enforcementZone = response.body().getEnforcementZone();
                                if (enforcementZone.size() > 0) {

                                    zoneItems = new ArrayList<>(enforcementZone.size());
                                    zoneItems.addAll(enforcementZone);
                                    displayItems(zoneItems);
                                }

                            }
                            CaleZoneActivity.this.progressDialog.isShowing();
                            CaleZoneActivity.this.progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ArrayOfEnforcementZone> call, Throwable t) {
                            CaleZoneActivity.this.progressDialog.isShowing();
                            CaleZoneActivity.this.progressDialog.dismiss();
                            System.out.println(t.getMessage());
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

    private void displayItems(ArrayList<EnforcementZone> zoneItems) {
        // create the grid item mapping
        String[] from = new String[]{"search_title"};
        int[] to = new int[]{R.id.search_textview};

        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < zoneItems.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("search_title", "" +  zoneItems.get(i).getName());//zoneItems.get(i).getDescription()
            fillMaps.add(map);
        }

        filteredItems = (ArrayList<EnforcementZone>) zoneItems.clone();

        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(CaleZoneActivity.this, fillMaps, R.layout.search_list_item, from, to);
        listView.setAdapter(adapter);
    }
}
