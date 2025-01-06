package com.ticketpro.parking.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.ticketpro.exception.TPException;
import com.ticketpro.model.Duty;
import com.ticketpro.model.Feature;
import com.ticketpro.model.Ticket;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.DayActivitiesBLProcessor;
import com.ticketpro.util.Preference;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class DayActivitiesActivity extends BaseActivityImpl {

    private ProgressDialog progressDialog;
    private Handler dataLoadHandler;
    private Handler errorHandler;
    private ArrayList<Duty> activityList;
    private SharedPreferences mPreferences;
    private Button backBtn;
    private Intent intent;
    private String fromWhere = "";

    private String stateValue;
    private String expValue;
    private String makeValue;
    private String bodyValue;
    private String colorValue;
    private String plateValue;
    private String vinValue;
    private String tmValue;
    private String locationValue;
    private String streetNumberValue;
    private String streetPrefixValue;
    private String streetSuffixValue;
    private String directionValue;
    private String permitValue;
    private String meterValue;
    private String space;

    private int stateId;
    private int bodyId;
    private int colorId;
    private int makeId;
    private String bodyCode;
    private String colorCode;
    private String makeCode;
    private Ticket activeTicket;
    private Preference preference;

    /**
     * Entry point of the Activity
     */
    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //int a = 1/0;
        try {
            setContentView(R.layout.day_activities);
            setLogger(DayActivitiesActivity.class.getName());
            setBLProcessor(new DayActivitiesBLProcessor((TPApplication) getApplicationContext()));
            isNetworkInfoRequired = true;
            preference = Preference.getInstance(this);

            /* Start of Session Persistence */
            mPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(TPConstant.PREFS_KEY_RESTORE_USERID, TPApp.userId);
            editor.putInt(TPConstant.PREFS_KEY_RESTORE_DEVICEID, TPApp.deviceId);
            editor.putInt(TPConstant.PREFS_KEY_RESTORE_CUSTID, TPApp.custId);
            editor.putBoolean(TPConstant.PREFS_KEY_IS_LOGGED_IN, true);
            editor.apply();
            /* End Session Persistence */

            backBtn = findViewById(R.id.back_btn);
            intent = getIntent();
            fromWhere = intent.getStringExtra("fromClass");

            if ((fromWhere != null) && fromWhere.matches("WriteTicket")) {
                try {
                    stateValue = intent.getStringExtra("STATE");
                    expValue = intent.getStringExtra("EXP");
                    makeValue = intent.getStringExtra("MAKE");
                    bodyValue = intent.getStringExtra("BODY");
                    colorValue = intent.getStringExtra("COLOR");
                    plateValue = intent.getStringExtra("PLATE");
                    vinValue = intent.getStringExtra("VIN");
                    tmValue = intent.getStringExtra("TM");
                    locationValue = intent.getStringExtra("Location");
                    streetNumberValue = intent.getStringExtra("StreetNumber");
                    streetPrefixValue = intent.getStringExtra("StreetPrefix");
                    streetSuffixValue = intent.getStringExtra("StreetSuffix");
                    directionValue = intent.getStringExtra("Direction");
                    permitValue = intent.getStringExtra("PERMIT");
                    meterValue = intent.getStringExtra("METER");

                    stateId = intent.getIntExtra("StateId", 0);
                    bodyId = intent.getIntExtra("BodyId", 0);
                    colorId = intent.getIntExtra("ColorId", 0);
                    makeId = intent.getIntExtra("MakeId", 0);

                    colorCode = intent.getStringExtra("ColorCode");
                    bodyCode = intent.getStringExtra("BodyCode");
                    makeCode = intent.getStringExtra("MakeCode");
                    space = intent.getStringExtra("space");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            dataLoadHandler = new Handler() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    ListView listView = findViewById(R.id.day_activities_list);
                    listView.setScrollbarFadingEnabled(false);
                    listView.setFastScrollAlwaysVisible(true);
                    listView.setFastScrollEnabled(true);
                    // create the grid item mapping
                    String[] from = new String[]{"activity"};
                    int[] to = new int[]{R.id.user_name};

                    // prepare the list of all records
                    List<HashMap<String, String>> fillMaps = new ArrayList<>();
                    for (int i = 0; i < activityList.size(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("activity", activityList.get(i).getTitle());
                        fillMaps.add(map);
                    }

                    // fill in the grid_item layout
                    SimpleAdapter adapter = new SimpleAdapter(DayActivitiesActivity.this, fillMaps, R.layout.user_list_item, from, to);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener((adapterView, view, pos, arg3) -> {
                        final Intent intent = new Intent();
                        Duty duty = TPApp.getActiveDutyInfo();
                        final int position = pos;

                        //Code by Sanjiv:
                        if (Feature.isFeatureAllowed(Feature.PAYBY_PHONE)){
                            String featureValue = Feature.getFeatureValue(Feature.PAYBY_PHONE);

                            if (featureValue!=null&& !featureValue.isEmpty()){
                                HashMap<Integer, Integer> vendorCode = TPUtility.getVendorCode(featureValue);
                                Integer integer = vendorCode.get(activityList.get(position).getId());
                                if (integer!=null){
                                    preference.putInt(TPConstant.VENDOR_CODE,integer);
                                    TPApplication.getInstance().isVendorCode = true;
                                }else {
                                    TPApplication.getInstance().isVendorCode = false;
                                }
                            }
                        }

                        if (duty != null) {
                            new Thread(() -> {
                                Looper.prepare();
                                ((DayActivitiesBLProcessor) screenBLProcessor).shiftActiveDuty(isServiceAvailable, TPApp.getActiveDutyReport());
                            }).start();

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }

                        if ((fromWhere != null) && fromWhere.matches("WriteTicket")) {
                            Duty setlectedDuty = activityList.get(pos);
                            if (setlectedDuty.getAllowTicket().equalsIgnoreCase("N")) {
                                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(DayActivitiesActivity.this);
                                confirmBuilder.setTitle("Alert")
                                        .setMessage("You have selected a duty/route that does not allow ticketing. Continue and discard ticket?").setCancelable(true)
                                        .setNegativeButton("No", (dialog, which) -> {
                                            dialog.dismiss();
                                            finish();
                                        }).setPositiveButton("Yes", (dialog, which) -> {

                                            intent.setClass(DayActivitiesActivity.this, WriteTicketActivity.class);
                                            intent.putExtra("activity", activityList.get(position).getTitle());
                                            intent.putExtra("isValidDuty", false);

                                            ((DayActivitiesBLProcessor) screenBLProcessor).initDayActivity(activityList.get(position));
                                            TPUtility.updateFB(DayActivitiesActivity.this,"dutyChange", null, log);
                                            // putExtra (intent);
                                            activeTicket = TPApp.getActiveTicket();
                                            activeTicket.setDutyId((activityList.get(position)).getId());
                                            TPApp.setDutyName((activityList.get(position)).getTitle());
                                            TPApp.setDutyId((activityList.get(position)).getId());
                                            // (activityList.get(pos)).getId();
                                            // startActivity(intent);
                                            finish();
                                        });

                                AlertDialog confirmAlert = confirmBuilder.create();
                                confirmAlert.show();
                            } else {
                                intent.setClass(DayActivitiesActivity.this, WriteTicketActivity.class);
                                intent.putExtra("activity", activityList.get(pos).getTitle());

                                intent.putExtra("isValidDuty", true);

                                ((DayActivitiesBLProcessor) screenBLProcessor).initDayActivity(activityList.get(pos));
                                TPUtility.updateFB(DayActivitiesActivity.this,"dutyChange", null, log);
                                // putExtra (intent);
                                activeTicket = TPApp.getActiveTicket();
                                activeTicket.setDutyId((activityList.get(pos)).getId());
                                TPApp.setDutyName((activityList.get(position)).getTitle());
                                TPApp.setDutyId((activityList.get(position)).getId());
                                // (activityList.get(pos)).getId();
                                // startActivity(intent);
                                finish();
                            }
                        } else {
                            intent.setClass(DayActivitiesActivity.this, MainActivity.class);
                            intent.putExtra("activity", activityList.get(pos).getTitle());
                            ((DayActivitiesBLProcessor) screenBLProcessor).initDayActivity(activityList.get(pos));
                            TPUtility.updateFB(DayActivitiesActivity.this,"dutyChange", null, log);
                            TPApp.setDutyName((activityList.get(position)).getTitle());
                            TPApp.setDutyId((activityList.get(position)).getId());
                            startActivity(intent);
                            finish();
                        }

                    });

                    //Auto Select if the number of duties is only 1
                    if (activityList.size() == 1) {
                        backBtn.setVisibility(View.GONE);

                        final Handler handler = new Handler();
                        final ListView view = listView;
                        handler.postDelayed(() -> view.performItemClick(view, 0, 0), 500);
                    }

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            };

            errorHandler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Toast.makeText(DayActivitiesActivity.this, "System Error while initialing day activity.", Toast.LENGTH_SHORT).show();
                }
            };

            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Duty duty = TPApp.getActiveDutyInfo();
        if (duty == null)
            return;

        TextView title = findViewById(R.id.day_activty_title_textview);
        title.setText("Please select new activity. \n" + duty.getTitle());
    }

    public void bindDataAtLoadingTime() {
        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread() {
            public void run() {
                try {
                    TPApp.dbConfigured = true;
                    activityList = ((DayActivitiesBLProcessor) screenBLProcessor).getActivities();
                    dataLoadHandler.sendEmptyMessage(1);

                } catch (TPException ae) {
                    log.error(ae.getMessage());
                    errorHandler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backAction(null);
    }

    public void backAction(View view) {
        Duty duty = TPApp.getActiveDutyInfo();
        if (duty != null) {
            if (fromWhere == null || !fromWhere.matches("WriteTicket")) {
                log.info("Not from ticket screen");
            }/* else {
				Ticket ticket=TPApp.createNewTicket();
				TPApp.setActiveTicket(ticket);
				Intent i = new Intent();
				i.setClass(DayActivitiesActivity.this, WriteTicketActivity.class);
                putExtra(i);
                startActivity(i);
            }*/
            finish();
            return;
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(TPConstant.EXTRA_END_SHIFT, true);
        intent.setClass(DayActivitiesActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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


    private void putExtra(Intent intent) {
        try {
            intent.putExtra("fromClass", "DayActivitiesActivity");
            intent.putExtra("STATE", stateValue);
            intent.putExtra("EXP", expValue);
            intent.putExtra("MAKE", makeValue);
            intent.putExtra("BODY", bodyValue);
            intent.putExtra("COLOR", colorValue);
            intent.putExtra("PLATE", plateValue);
            intent.putExtra("VIN", vinValue);
            intent.putExtra("TM", tmValue);
            intent.putExtra("Location", locationValue);
            intent.putExtra("StreetNumber", streetNumberValue);
            intent.putExtra("StreetPrefix", streetPrefixValue);
            intent.putExtra("StreetSuffix", streetSuffixValue);
            intent.putExtra("Direction", directionValue);
            intent.putExtra("PERMIT", permitValue);
            intent.putExtra("METER", meterValue);
            intent.putExtra("space", space);


            intent.putExtra("StateId", stateId);
            intent.putExtra("BodyId", bodyId);
            intent.putExtra("ColorId", colorId);
            intent.putExtra("MakeId", makeId);

            intent.putExtra("BodyCode", bodyCode);
            intent.putExtra("ColorCode", colorCode);
            intent.putExtra("MakeCode", makeCode);

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

    }

}
