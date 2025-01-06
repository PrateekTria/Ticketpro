package com.ticketpro.vendors;

import android.annotation.SuppressLint;
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


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ticketpro.api.ApiRequest;
import com.ticketpro.model.Feature;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.model.ZonePoleModel;
import com.ticketpro.model.ZonePoleResponse;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.parking.adapter.ZonePoleAdapter;
import com.ticketpro.util.Preference;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.ticketpro.parking.OnItemClickListener;

import org.json.JSONObject;

public class ZonePoleList extends BaseActivityImpl {
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private ZonePoleAdapter zonePoleAdapter;
    private List<ZonePoleModel> zoneList;
    private Preference preference;



    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_pole_list);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        preference = Preference.getInstance(ZonePoleList.this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        recyclerView.addItemDecoration(dividerItemDecoration);

        try {
            getZonePoleList();
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

//    private void __zonePoleSelect(String accToken, ProgressDialog progressDialog) {
//
//        try {
//            if (isServiceAvailable) {
//                if (!(TPApp.enableZonePole && Feature.isFeatureAllowed(Feature.PARK_ZONEPOLE))) {
//                    return;
//                }
//                if (isServiceAvailable) {
//
////                    VendorService vendorService = VendorService.getServiceByName(VendorService.CURBSENSE_ZONE);
////                    if (TPConstant.IS_DEVELOPMENT_BUILD) {
////                        if (vendorService.getTestURL() != null && !vendorService.getTestURL().isEmpty()) {
////                            zoneServiceerviceURL = vendorService.getTestURL();
////                        }
////                    } else {
////                        if (vendorService.getProdURL() != null && !vendorService.getProdURL().isEmpty()) {
////                            zoneServiceerviceURL = vendorService.getProdURL();
////                        }
////                    }
//                    ApiRequest service = ServiceGeneratorZonePole.createService(ApiRequest.class, accToken);
//                    service.getCurveSenseZoneList(zoneServiceerviceURL).enqueue(new Callback<List<ZonePoleModel>>() {
//                        @Override
//                        public void onResponse(Call<List<ZonePoleModel>> call, Response<List<ZonePoleModel>> response) {
//                            progressDialog.dismiss();
//
//                            if (response.isSuccessful() && response.body() != null) {
//
//                            } else {
//                                Log.e("MainActivity", "Response error: " + response.code());
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<List<ZonePoleModel>> call, Throwable t) {
//                            progressDialog.dismiss();
//                        }
//                    });
//                }else {
//                    progressDialog.dismiss();
//                }
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            progressDialog.dismiss();
//        }
//    }
//


    public void getZonePoleList() throws IOException {
        progressDialog = new ProgressDialog(ZonePoleList.this);
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
            if (!(TPApp.enableZonePole && Feature.isFeatureAllowed(Feature.PARK_ZONEPOLE))) {
                return;
            }

//            final VendorServiceConfig config = VendorService.getServiceConfig(VendorService.CURBSENSE_LOGIN, TPApp.deviceId, "/");
//            Map<String, String> paramsMap = config.getParamsMap();
//            String user = paramsMap.get("username");
//            String password = paramsMap.get("password");
//            String api = paramsMap.get("API");
//            String result = api.split("=")[1];
//            serviceURL = config.getServiceURL();
//            curveSenseTokenRequest = new CurveSenseTokenRequest();
//            curveSenseTokenRequest.setUsername(user);
//            curveSenseTokenRequest.setPassword(password);
//            curveSenseTokenRequest.setAPI(result);

        }


        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://genetecapidev.ticketproweb.com/api/") // Replace with your base URL
                .addConverterFactory(GsonConverterFactory.create()) // Use Gson converter for JSON parsing
                .build();

        // Create an instance of the API service
        ApiRequest apiService = retrofit.create(ApiRequest.class);

        // Make the API call to get zone data
        Call<List<ZonePoleModel>> call = apiService.getZonePoleList();

        // Enqueue the call (this is asynchronous)
        call.enqueue(new Callback<List<ZonePoleModel>>() {
            @Override
            public void onResponse(Call<List<ZonePoleModel>> call, Response<List<ZonePoleModel>> response) {
                if (response.isSuccessful()) {
                    // If the response is successful, get the data and update the RecyclerView
                    zoneList = response.body();  // This will be the list of ZonePoleModel objects
                    zonePoleAdapter = new ZonePoleAdapter(ZonePoleList.this,zoneList);
                    recyclerView.setAdapter(zonePoleAdapter);
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                } else {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    // If the response is not successful, show an error message
                   // Toast.makeText(MainActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ZonePoleModel>> call, Throwable t) {
                // If the network request fails, show a failure message
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Log.e("MainActivity", "Error: " + t.getMessage());
            //    Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
