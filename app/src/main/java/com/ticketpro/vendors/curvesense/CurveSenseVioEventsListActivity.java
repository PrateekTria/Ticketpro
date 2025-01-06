package com.ticketpro.vendors.curvesense;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGeneratorCurveSense;
import com.ticketpro.model.CurveSenseZoneItemSelectedList;
import com.ticketpro.model.CurveSenseZoneList;
import com.ticketpro.model.Feature;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VioEventsBean;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.adapter.ViolationEventAdapter;
import com.ticketpro.util.Preference;
import com.ticketpro.util.TPConstant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurveSenseVioEventsListActivity extends BaseActivityImpl {

    private static final String BASE_URL = "https://pems.pemsportal.com/ImportTran/api/"; // Update with your base URL
    private RecyclerView recyclerView;
    private ViolationEventAdapter adapter;
    private Intent intent;
    String zoneName,zoneCode;
    private Preference preference;
    private ProgressDialog progressDialog;
    TextView curvesense_info_text;
    EditText searchText;
    String zoneServiceerviceURL;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curvesense_list);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        preference = Preference.getInstance(CurveSenseVioEventsListActivity.this);
        curvesense_info_text = (TextView) findViewById(R.id.curvesense_info_text);
        intent = getIntent();
        zoneName = intent.getStringExtra("Name");
        zoneCode = String.valueOf(intent.getIntExtra("Id",0));
        Log.d("Zone", "Id: " + zoneCode);
        curvesense_info_text.setText(zoneName);
        recyclerView = findViewById(R.id.recyclerView); // Replace with your actual RecyclerView ID
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchText = (EditText) findViewById(R.id.searchText);
       String token =  preference.getString("CURVESENSE_TOKEN");
        Log.d("token", "token: " + token);
       try{

           __curveSenseZoneList(zoneCode,token);
       }catch (Exception e){
           e.printStackTrace();
       }


        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString()); // Call the filter method in your adapter
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    private void __curveSenseZoneList(String zoneCode, String accToken) {
        progressDialog = new ProgressDialog(CurveSenseVioEventsListActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();

        try {
            // Check if feature is enabled
            if (!(TPApp.enableCurbsense && Feature.isFeatureAllowed(Feature.PARK_CURBSENSE))) {
                progressDialog.dismiss();
                return;
            }

            VendorService vendorService = VendorService.getServiceByName(VendorService.CURBSENSE_PLATEINFO);
            if (TPConstant.IS_DEVELOPMENT_BUILD) {
                if (vendorService.getTestURL() != null && !vendorService.getTestURL().isEmpty()) {
                    zoneServiceerviceURL = vendorService.getTestURL();
                }
            } else {
                if (vendorService.getProdURL() != null && !vendorService.getProdURL().isEmpty()) {
                    zoneServiceerviceURL = vendorService.getProdURL();
                }
            }

            // Construct the URL
          //  String serviceURL = "https://pems.pemsportal.com/ImportTran/api/v1/bollard/liveviostatus/";
            String completeURL = zoneServiceerviceURL  + "/"+zoneCode + "/"; // Complete URL

            // Create Retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://pems.pemsportal.com/") // Base URL must be set to the API's base
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create the API service
            ApiRequest apiService = retrofit.create(ApiRequest.class);

            // Make the call with the complete URL
            Call<CurveSenseZoneItemSelectedList> call = apiService.getVioEvents("Bearer " + accToken, completeURL);
            call.enqueue(new Callback<CurveSenseZoneItemSelectedList>() {
                @Override
                public void onResponse(Call<CurveSenseZoneItemSelectedList> call, Response<CurveSenseZoneItemSelectedList> response) {
                    progressDialog.dismiss(); // Dismiss the progress dialog here

                    if (response.isSuccessful() && response.body() != null) {
                        List<VioEventsBean> violationEvents = response.body().getVio_events();
                        adapter = new ViolationEventAdapter(CurveSenseVioEventsListActivity.this,violationEvents);

                        recyclerView.setAdapter(adapter);
                        setupHeaderClicks();
                    } else {
                        Log.e("API_ERROR", "Response Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<CurveSenseZoneItemSelectedList> call, Throwable t) {
                    progressDialog.dismiss(); // Dismiss the progress dialog on failure
                    Log.e("API_FAILURE", t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss(); // Dismiss the progress dialog in case of an exception
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void bindDataAtLoadingTime() throws Exception {

    }

    @Override
    public void handleVoiceInput(String text) throws Exception {

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {

    }


    private void setupHeaderClicks() {
        TextView headerSpaceName = findViewById(R.id.header_spaceName);
        TextView headerVioType = findViewById(R.id.header_vioType);
        TextView headerPlateNo = findViewById(R.id.header_plateNo);
        TextView headerDate = findViewById(R.id.header_date);

        headerSpaceName.setOnClickListener(v -> {
            adapter.sortBySpaceName();
            updateHeaderText(headerSpaceName, "Space");
        });

        headerPlateNo.setOnClickListener(v -> {
            adapter.sortByPlateNo();
            updateHeaderText(headerPlateNo, "Plate");
        });

        headerVioType.setOnClickListener(v -> {
            adapter.sortByVioType();
            updateHeaderText(headerVioType, "Viol");
        });

        headerDate.setOnClickListener(v -> {
            adapter.sortByDate();
            updateHeaderText(headerDate, "Date");
        });


    }


    private void updateHeaderText(TextView header, String baseText) {
        // Example logic to toggle the sort direction
        if (header.getTag() == null || header.getTag().equals("desc")) {
            header.setText(baseText + "\u25BC"); // Ascending
            header.setTag("asc");
        } else {
            header.setText(baseText + "\u25B2"); // Descending
            header.setTag("desc");
        }
    }
}
