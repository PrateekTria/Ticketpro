package com.ticketpro.vendors.curvesense;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGeneratorCurveSense;
import com.ticketpro.api.ServiceGeneratorPass2;
import com.ticketpro.model.CurveSenseTokenRequest;
import com.ticketpro.model.CurveSenseZoneList;
import com.ticketpro.model.CurvesenseLoginTokenResponse;
import com.ticketpro.model.Feature;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.vendors.PP2ZoneListInfo;
import com.ticketpro.vendors.passport2_model.TokenRequest;
import com.ticketpro.vendors.passport2_model.PP2TokenResponse;
import com.ticketpro.vendors.passport2_model.zonelist.Datum;
import com.ticketpro.vendors.passport2_model.zonelist.PP2ZoneList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurveSenseActivity extends BaseActivityImpl {
    private ProgressDialog progressDialog;
    private ListView listView;
    private EditText searchEditText;
    private ArrayList<CurveSenseZoneList> zoneItems = new ArrayList<>();
    private ArrayList<CurveSenseZoneList> filteredItems = new ArrayList<>();
    private Preference preference;
    CurveSenseTokenRequest curveSenseTokenRequest;
    String serviceURL,zoneServiceerviceURL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curvesense_list);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        preference = Preference.getInstance(CurveSenseActivity.this);
        listView = (ListView) findViewById(R.id.curvesense_listview);
        listView.setScrollbarFadingEnabled(false);
        listView.setFastScrollEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
                if (zoneItems != null && pos < zoneItems.size()) {
                    Intent intent = new Intent(CurveSenseActivity.this, CurveSenseVioEventsListActivity.class);
                    intent.putExtra("Name", zoneItems.get(pos).getName());
                    intent.putExtra("Id", zoneItems.get(pos).getId());
                    startActivity(intent);
                } else {
                    Log.e("CurveSenseActivity", "zoneItems is null or position is out of bounds.");
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
                String zoneName;

                filteredItems.clear();

                if (searchText.isEmpty()) {
                    if (zoneItems != null) {
                        filteredItems.addAll(zoneItems);
                    }
                } else {
                    searchText = searchText.toLowerCase();
                    if (zoneItems!=null && !zoneItems.isEmpty()) {
                        for (CurveSenseZoneList zoneInfo : zoneItems) {
                            zoneName = zoneInfo.getName();
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
        try {
            getTokenCurveSense();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private void __curveSenseZoneList(String accToken, ProgressDialog progressDialog) {

        try {
            if (isServiceAvailable) {
                if (!(TPApp.enableCurbsense && Feature.isFeatureAllowed(Feature.PARK_CURBSENSE))) {
                    return;
                }
                if (isServiceAvailable) {

                    VendorService vendorService = VendorService.getServiceByName(VendorService.CURBSENSE_ZONE);
                    if (TPConstant.IS_DEVELOPMENT_BUILD) {
                        if (vendorService.getTestURL() != null && !vendorService.getTestURL().isEmpty()) {
                            zoneServiceerviceURL = vendorService.getTestURL();
                        }
                    } else {
                        if (vendorService.getProdURL() != null && !vendorService.getProdURL().isEmpty()) {
                            zoneServiceerviceURL = vendorService.getProdURL();
                        }
                    }
                    ApiRequest service = ServiceGeneratorCurveSense.createService(ApiRequest.class, accToken);
                    service.getCurveSenseZoneList(zoneServiceerviceURL).enqueue(new Callback<List<CurveSenseZoneList>>() {
                        @Override
                        public void onResponse(Call<List<CurveSenseZoneList>> call, Response<List<CurveSenseZoneList>> response) {
                            progressDialog.dismiss();

                            if (response.isSuccessful() && response.body() != null) {
                                zoneItems = new ArrayList<>(response.body());
                                List<CurveSenseZoneList> zoneItems = response.body();
                                for (CurveSenseZoneList zone : zoneItems) {
                                    Log.d("Zone", "Id: " + zone.getId() + ", Name: " + zone.getName());
                                }
                                displayItems(zoneItems); // Make sure you have this method defined
                            } else {
                                Log.e("MainActivity", "Response error: " + response.code());
                            }

                        }

                        @Override
                        public void onFailure(Call<List<CurveSenseZoneList>> call, Throwable t) {
                            progressDialog.dismiss();
                        }
                    });
                }else {
                    progressDialog.dismiss();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    private void displayItems(List<CurveSenseZoneList> zoneItems) {
        // Create the grid item mapping
        String[] from = new String[]{"search_title"};
        int[] to = new int[]{R.id.search_textview};

// Prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<>();
        for (CurveSenseZoneList zone : zoneItems) {
            HashMap<String, String> map = new HashMap<>();
            map.put("search_title", zone.getName());
            fillMaps.add(map);
        }


// Fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(CurveSenseActivity.this, fillMaps, R.layout.search_list_item, from, to);
        listView.setAdapter(adapter);



    }

    public void getTokenCurveSense() throws IOException {
        progressDialog = new ProgressDialog(CurveSenseActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();
        if (isNetworkConnected()) {
            if (!(TPApp.enableCurbsense && Feature.isFeatureAllowed(Feature.PARK_CURBSENSE))) {
                return;
            }

            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.CURBSENSE_LOGIN, TPApp.deviceId, "/");
            Map<String, String> paramsMap = config.getParamsMap();
            String user = paramsMap.get("username");
            String password = paramsMap.get("password");
            String api = paramsMap.get("API");
            String result = api.split("=")[1];
            serviceURL = config.getServiceURL();
            curveSenseTokenRequest = new CurveSenseTokenRequest();
            curveSenseTokenRequest.setUsername(user);
            curveSenseTokenRequest.setPassword(password);
            curveSenseTokenRequest.setAPI(result);

        }


        ApiRequest service = ServiceGeneratorCurveSense.createService(ApiRequest.class);
        Call<CurvesenseLoginTokenResponse> tokenForCurveSense = service.getTokenForCurveSense(serviceURL,curveSenseTokenRequest);
        tokenForCurveSense.enqueue(new retrofit2.Callback<CurvesenseLoginTokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<CurvesenseLoginTokenResponse> call, @NonNull Response<CurvesenseLoginTokenResponse> response) {

                if (response.isSuccessful() && response.code() == 200) {
                    assert response.body() != null;
                    String accessToken = response.body().getToken();
                    if (accessToken != null && !accessToken.isEmpty()) {

                        preference.putString("CURVESENSE_TOKEN",accessToken);
                        __curveSenseZoneList(accessToken,progressDialog);

                    }
                } else {
                    progressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(@NonNull Call<CurvesenseLoginTokenResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();


            }
        });
    }

}
