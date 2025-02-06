package com.ticketpro.vendors;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.ticketpro.api.ApiRequest;
import com.ticketpro.api.ServiceGenerator;
import com.ticketpro.api.TokenGenerate;
import com.ticketpro.exception.TPException;
import com.ticketpro.model.Feature;
import com.ticketpro.model.GenetecPatrollerActivities;
import com.ticketpro.model.GenetecPatrollerActivitiesResponse;
import com.ticketpro.model.GenetecPatrollers;
import com.ticketpro.model.Params;
import com.ticketpro.model.RequestPOJO;
import com.ticketpro.model.User;
import com.ticketpro.model.UserResponse;
import com.ticketpro.model.Vendor;
import com.ticketpro.model.VendorItem;
import com.ticketpro.model.VendorService;
import com.ticketpro.model.VendorServiceConfig;
import com.ticketpro.parking.R;
import com.ticketpro.parking.activity.BaseActivityImpl;
import com.ticketpro.parking.activity.HomeActivity;
import com.ticketpro.parking.activity.LoginPasswordActivity;
import com.ticketpro.parking.activity.LoginSelectUserActivity;
import com.ticketpro.parking.activity.LoginUserPswdActivity;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.activity.WriteTicketActivity;
import com.ticketpro.parking.bl.LoginBLProcessor;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.parking.service.TPAsyncTask;
import com.ticketpro.util.Preference;
import com.ticketpro.util.StringUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPTask;
import com.ticketpro.util.TPUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenetecPatrollerActivity extends BaseActivityImpl {
    private ArrayList<GenetecPatrollers> getPratrollerId;
    private ArrayList<GenetecPatrollers> filteredPatrollers = new ArrayList<>();
    private Handler dataLoadHandler;
    private ListView listView;
    private EditText searchEditText;
    private CheckBox keyboardPopupChk;
    private SharedPreferences mPreferences;
    private ProgressDialog progressDialog;
    String formattedDate;

    /**
     * Entry point of the Activity
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.patrollers_list);
            setLogger(LoginSelectUserActivity.class.getName());
            setLogger(HomeActivity.class.getName());
            setBLProcessor(new LoginBLProcessor());

            isNetworkInfoRequired = true;
            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

            listView = findViewById(R.id.patrollers_listview);


            listView.setScrollbarFadingEnabled(false);
            listView.setFastScrollAlwaysVisible(true);
            listView.setFastScrollEnabled(true);


            listView.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
                 int j = arg2+1;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

                // Get the current date
                Date currentDate = new Date();
                // Format the current date
                 formattedDate = sdf.format(currentDate);
                updateGenetecPatrollerActivities(filteredPatrollers.get(arg2).getPatrollerId(),filteredPatrollers.get(arg2).getVehicleName());


            });

            searchEditText = findViewById(R.id.searchText);
            searchEditText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    // Abstract Method of TextWatcher Interface.
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Abstract Method of TextWatcher Interface.
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String searchText = searchEditText.getText().toString();
                    filteredPatrollers.clear();
                    searchText = searchText.toLowerCase();
                    if (searchText.length() == 0) {
                        filteredPatrollers.addAll(getPratrollerId);
                    } else {
                        for (GenetecPatrollers genetecPatrollers : getPratrollerId) {
                            if (!StringUtil.isEmpty(genetecPatrollers.getPatrollerId())) {
                                if (genetecPatrollers.getPatrollerId().toLowerCase().contains(searchText)) {
                                    filteredPatrollers.add(genetecPatrollers);
                                }
                            }
                        }
                    }
                    updateListItems(filteredPatrollers);
                }
            });


            dataLoadHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (getPratrollerId == null) {
                        return;
                    }
                    filteredPatrollers = new ArrayList<>();
                    filteredPatrollers.addAll(getPratrollerId);
                    updateListItems(filteredPatrollers);
                    if (progressDialog != null) {
                        progressDialog.hide();
                    }

                }
            };
            getPratrollerId = GenetecPatrollers.getPatrollers(TPConstant.MODULE_NAME);
            dataLoadHandler.sendEmptyMessage(1);

            //bindDataAtLoadingTime();
     //       setKeyboardStatus();
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public void bindDataAtLoadingTime() throws Exception {
        // Show soft keyboard selection
        try {
            if (TPUtility.isN5ServiceAvailable(this)) {
                InputMethodManager imeManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(INPUT_METHOD_SERVICE);
                imeManager.showInputMethodPicker();
            }

            try {
                if (Feature.isFeatureAllowed(Feature.TRANSACTION_TIMEOUT)) {
                    TPApplication.getInstance().transactionTimeout = Integer
                            .parseInt(Feature.getFeatureValue(Feature.TRANSACTION_TIMEOUT));
                }
            } catch (Exception e) {
                log.error(TPUtility.getPrintStackTrace(e));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateListItems(ArrayList<GenetecPatrollers> genetecPatrollers) {
        if (genetecPatrollers!=null && !genetecPatrollers.isEmpty()) {
            String[] from = new String[]{"vehicleName"};
            int[] to = new int[]{R.id.user_name};

            List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < genetecPatrollers.size(); i++) {
                int j = i+1;
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("vehicleName", genetecPatrollers.get(i).getVehicleName());
                fillMaps.add(map);
            }

            // fill in the grid_item layout
            SimpleAdapter adapter = new SimpleAdapter(GenetecPatrollerActivity.this, fillMaps, R.layout.user_list_item, from, to);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {

    }



    public void backAction(View view) {
        Intent i = new Intent(this,WriteTicketActivity.class);
        startActivity(i);
        finish();
    }

    public void searchAction(View view) {
        if (searchEditText.getVisibility() == View.GONE) {
            searchEditText.setVisibility(View.VISIBLE);
            TPUtility.showSoftKeyboard(this, searchEditText);
        } else {
            searchEditText.setVisibility(View.GONE);
        }
    }

    @Override
    public void handleVoiceInput(String text) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleVoiceMode(boolean voiceMode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleNetworkStatus(boolean connected, boolean isFastConnection) {
        // TODO Auto-generated method stub
    }


    public void updateGenetecPatrollerActivities(String patrollerId , String message) {
        progressDialog = ProgressDialog.show(GenetecPatrollerActivity.this, "", "Please wait...");

        if (!isServiceAvailable) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            return; // Early exit if service is not available
        }

        filteredPatrollers.clear();

        try {
            RequestPOJO requestPOJO = new RequestPOJO();
            Params params = new Params();
            params.setCustId(TPApp.custId);
            params.setUserId(TPApp.userId);
            params.setDeviceId(TPApp.deviceId);
            params.setPatrollerId(patrollerId);
            params.setIsActive(1);
            requestPOJO.setParams(params);
            requestPOJO.setMethod("updatePatrollersActivityData");

            ApiRequest api = ServiceGenerator.createRxService(ApiRequest.class);
            Call<GenetecPatrollerActivitiesResponse> genetecPatrollerActivitiesResponseCall = api.updatePatrollerActivities(requestPOJO);

            genetecPatrollerActivitiesResponseCall.enqueue(new Callback<GenetecPatrollerActivitiesResponse>() {
                @Override
                public void onResponse(Call<GenetecPatrollerActivitiesResponse> call, Response<GenetecPatrollerActivitiesResponse> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    if (response.body() != null) {
                        ParkingDatabase db = ParkingDatabase.getInstance(GenetecPatrollerActivity.this);
                        GenetecPatrollerActivities activity = new GenetecPatrollerActivities();

                        activity.createdOn = formattedDate; // Set the current time
                        activity.custId = TPApp.custId; // Customer ID
                        activity.patrollerId = patrollerId;
                        activity.userId = String.valueOf(TPApp.userId);
                        activity.deviceId = TPApp.deviceId; // Device ID
                        activity.isActive = "Y";

                        new Thread(() -> db.genetecPatrollerActivitiesDao().insert(activity)).start();

                        Intent intent = new Intent(GenetecPatrollerActivity.this, WriteTicketActivity.class);
                        intent.putExtra("patrollerId", message);
                        startActivity(intent);
                        finish();
                    } else {
                        // Handle case where response body is null
                        Toast.makeText(GenetecPatrollerActivity.this, "Failed to update activities.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GenetecPatrollerActivitiesResponse> call, Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    // Log the error
                    log.error(t.getMessage());
                    Toast.makeText(GenetecPatrollerActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            e.printStackTrace();
        }
    }


}
