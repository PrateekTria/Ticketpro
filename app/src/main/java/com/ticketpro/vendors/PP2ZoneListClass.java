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

import androidx.annotation.NonNull;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGeneratorPass2;
import com.ticketpro.model.Feature;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.vendors.passport2_model.TokenRequest;
import com.ticketpro.vendors.passport2_model.PP2TokenResponse;
import com.ticketpro.vendors.passport2_model.zonelist.Datum;
import com.ticketpro.vendors.passport2_model.zonelist.PP2ZoneList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PP2ZoneListClass extends BaseActivityImpl {
    private ProgressDialog progressDialog;
    private ListView listView;
    private EditText searchEditText;
    private ArrayList<Datum> zoneItems;
    private ArrayList<Datum> filteredItems = new ArrayList<>();
    private Preference preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_list);

        preference = Preference.getInstance(PP2ZoneListClass.this);
        listView = (ListView) findViewById(R.id.zone_listview);
        listView.setScrollbarFadingEnabled(false);
        listView.setFastScrollEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long arg3) {
                Intent intent = new Intent();
                intent.setClass(PP2ZoneListClass.this, PP2ZoneListInfo.class);
                intent.putExtra("ZoneName", filteredItems.get(pos).getName());
                intent.putExtra("opId", filteredItems.get(pos).getOperatorId());
                intent.putExtra("zoneId", filteredItems.get(pos).getId());
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
                        for (Datum zoneInfo : zoneItems) {
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
        getTokenPP2();
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

    private void __pp2GetZoneList(String accToken, ProgressDialog progressDialog) {

        try {
            if (isServiceAvailable) {
                if (!(TPApp.enablePassportParking2 && Feature.isFeatureAllowed(Feature.PASSPORT_PARKING2))) {
                    return;
                }
                final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.PP2_ZONE_LIST,
                        TPApp.deviceId);
                if (config != null && isServiceAvailable) {

                    String serviceURL = config.getServiceURL();
                    String params = config.getParams();
                    //https://api.us.passportinc.com/v3/shared/zones?operator_id=869f5996-c1b7-4618-8707-3b5859206a09
                    String URL = serviceURL + params;
                    ApiRequest service = ServiceGeneratorPass2.createService(ApiRequest.class, accToken);
                    service.getPP2ZoneList(URL).enqueue(new Callback<PP2ZoneList>() {
                        @Override
                        public void onResponse(Call<PP2ZoneList> call, Response<PP2ZoneList> response) {
                            progressDialog.dismiss();

                            if (response.isSuccessful()&& response.code()==200){
                                List<Datum> data = response.body().getData();
                                if (data!=null && !data.isEmpty()){
                                    zoneItems = new ArrayList<>(data.size());
                                    zoneItems.addAll(data);
                                    displayItems(zoneItems);
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<PP2ZoneList> call, Throwable t) {
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

    private void displayItems(ArrayList<Datum> zoneItems) {
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

        filteredItems = (ArrayList<Datum>) zoneItems.clone();

        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(PP2ZoneListClass.this, fillMaps, R.layout.search_list_item, from, to);
        listView.setAdapter(adapter);
    }

    public void getTokenPP2() {
        progressDialog = new ProgressDialog(PP2ZoneListClass.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();

        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setGrantType("client_credentials");
        tokenRequest.setAudience("public.api.passportinc.com");
        tokenRequest.setClientId("Y9h6IjcZxNBU0EzkmZdxVuWFBNUbNjhd");
        tokenRequest.setClientSecret("FrTFITvPWKQD_ApL-R89cJymRO2cobdH7C5I7LynvDFZItjpR17nzNm4wa8F-PIO");
        //ApiRequest apiRequest = ServiceGeneratorPass2.getApiService();
        // Call<TokenResponse> tokenForPassportParking2 = apiRequest.getTokenForPassportParking2(tokenRequest);
        ApiRequest service = ServiceGeneratorPass2.createService(ApiRequest.class);
        Call<PP2TokenResponse> tokenForPassportParking21 = service.getTokenForPassportParking2(tokenRequest);
        tokenForPassportParking21.enqueue(new retrofit2.Callback<PP2TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<PP2TokenResponse> call, @NonNull Response<PP2TokenResponse> response) {

                if (response.isSuccessful() && response.code() == 200) {
                    assert response.body() != null;
                    String accessToken = response.body().getAccessToken();
                    if (accessToken != null && !accessToken.isEmpty()) {
                       // __searchSpacePP2(spaceStr, accessToken, progressDialog);
                        preference.putString("PP2_TOKEN",accessToken);
                        __pp2GetZoneList(accessToken,progressDialog);

                    }
                } else {
                    progressDialog.dismiss();
                   // logpm.info("PP2 Message: " + response.message());

                }

            }

            @Override
            public void onFailure(@NonNull Call<PP2TokenResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                //logpm.error("PP2 Message: " + t.getMessage());

            }
        });
    }

}
