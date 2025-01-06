package com.ticketpro.parking.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.ticketpro.gpslibrary.ADLocation;
import com.ticketpro.gpslibrary.GetLocation;
import com.ticketpro.gpslibrary.MyTracker;
import com.ticketpro.model.Feature;
import com.ticketpro.model.SpecialActivitiesLocation;
import com.ticketpro.model.SpecialActivity;
import com.ticketpro.model.SpecialActivityDisposition;
import com.ticketpro.model.SpecialActivityPicture;
import com.ticketpro.model.SpecialActivityReport;
import com.ticketpro.model.SyncData;
import com.ticketpro.parking.R;
import com.ticketpro.parking.bl.SpecialActivityBLProcessor;

import com.ticketpro.util.DateUtil;
import com.ticketpro.util.TPConstant;
import com.ticketpro.util.TPUtility;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;

/**
 * Home screen (Ref. Mockups).
 *
 * @author : TicketPRO
 * @version : 1.0
 * @since : 1.0
 */

public class SpecialActivityActivity extends BaseActivityImpl implements MyTracker.ADLocationListener {

    public final int REQUEST_LOOKUP = 1;
    private EditText addressEditText;
    private EditText location;
    private EditText caseNumberEditText;
    private EditText notesEditText;
    private TextView activitySpinner;
    private TextView dispositionSpinner;
    private ArrayList<String> activities;
    private ArrayList<String> dispositions;
    private Button startBtn;
    private Button endBtn;

    private ProgressDialog progressDialog;
    private Handler dataLoadingHandler;
    private SpecialActivityReport specialActivityReport;
    private Button gpsButton;
    private TextView elapsedTime;
    private TextView startTime;
    private String[] activity;
    private String[] disposition;
    private int ACTIVITY_START = 0;
    private Button btnTakePic;

    long startTimer = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTimer;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            elapsedTime.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };
    private Handler handlerActivityP;

    /**
     * Entry point of the Activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            // Check if special activity is allowed on not.
            if (!Feature.isFeatureAllowed(Feature.SPECIAL_ACTIVITY)) {
                Toast.makeText(this, "This feature is disabled.", Toast.LENGTH_LONG).show();
                finish();
            }

            setContentView(R.layout.special_activity);
            setLogger(SpecialActivityActivity.class.getName());
            setActiveScreen(this);

            startTime = (TextView) findViewById(R.id.start_time);
            elapsedTime = (TextView) findViewById(R.id.elapsed_time);
            addressEditText = (EditText) findViewById(R.id.special_activity_address_textview);
            location = (EditText) findViewById(R.id.et_location);
            caseNumberEditText = (EditText) findViewById(R.id.special_activity_casenumber_textview);
            notesEditText = (EditText) findViewById(R.id.special_activity_notes_textview);
            activitySpinner = findViewById(R.id.special_activity_spinner);
            dispositionSpinner = findViewById(R.id.special_activity_disposition_spinner);
            startBtn = (Button) findViewById(R.id.special_activity_start_btn);
            endBtn = (Button) findViewById(R.id.special_activity_end_btn);
            gpsButton = (Button) findViewById(R.id.write_ticket_gps_btn2);
            btnTakePic = (Button) findViewById(R.id.btnTakePic);

            specialActivityReport = TPApp.getReport();
            specialActivityReport.setReportId(SpecialActivityReport.getLastInsertId() + 1);

            dataLoadingHandler = new Handler() {
                @Override
                public void handleMessage(@NotNull Message msg) {
                    super.handleMessage(msg);

                    String[] activityArray = (String[]) activities.toArray(new String[0]);
                    activity = activityArray;
                    //_activitySelection(activityArray);

                    String[] dispositionsArray = (String[]) dispositions.toArray(new String[0]);
                    disposition = dispositionsArray;
                    //ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(SpecialActivityActivity.this, android.R.layout.simple_spinner_item, dispositionsArray);
                    //dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //dispositionSpinner.setAdapter(dataAdapter2);

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            };
            handlerActivityP = new Handler(){
                /**
                 * Subclasses must implement this to receive messages.
                 *
                 * @param msg
                 */
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        finish();
                    }
                }
            };
            btnTakePic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent i = new Intent();
                    i.setClass(SpecialActivityActivity.this, SpecialActivityPhotoView.class);
                    startActivityForResult(i, 0);
                    return true;
                }
            });
            activitySpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SpecialActivityActivity.this);
                    dialog.setTitle("SELECT ACTIVITY");
                    dialog.setItems(activity, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            activitySpinner.setText(activity[position]);
                        }

                    });
                    dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = dialog.create();
                    alert.show();
                }
            });

            dispositionSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SpecialActivityActivity.this);
                    dialog.setTitle("SELECT DISPOSITION");
                    dialog.setItems(disposition, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            dispositionSpinner.setText(disposition[position]);
                        }

                    });
                    dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = dialog.create();
                    alert.show();
                }
            });
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _openLocationList();
                }
            });
            bindDataAtLoadingTime();

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    private void _openLocationList() {

        SpecialActivitiesLocation locationList = new SpecialActivitiesLocation();
        ArrayList<String> specialLocation = locationList.getSpecialLocation(TPApp.custId);
        final String locations[] = specialLocation.toArray(new String[specialLocation.size()]);

        AlertDialog.Builder dialog = new AlertDialog.Builder(SpecialActivityActivity.this);
        dialog.setTitle("SELECT LOCATION");
        dialog.setItems(locations, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                location.setText(locations[position]);
            }

        });
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialog.create();
        alert.show();

    }


    public void selectLocation(View view) {
        Intent i = new Intent();
        i.setClass(SpecialActivityActivity.this, SearchLookupActivity.class);
        i.putExtra("LIST_TYPE", TPConstant.SELECT_LOCATION_LIST);
        startActivityForResult(i, REQUEST_LOOKUP);
        return;
    }


    private void findLoc() {
        //new MyTracker(SpecialActivityActivity.this, this).track();
        new GetLocation(SpecialActivityActivity.this, this).track();
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnTakePic.setText("Take Picture"+"("+SpecialActivityPicture.getListOfImage(SpecialActivityReport.getLastInsertId()+1).size()+")" );

    }

    public void gpsAction(View view) {
        findLoc();
    }

    public void bindDataAtLoadingTime() {

        progressDialog = ProgressDialog.show(this, "", "Loading...");
        new Thread() {
            public void run() {
                SpecialActivityBLProcessor blProcessor = new SpecialActivityBLProcessor();
                try {
                    activities = blProcessor.getSpecialActivities();
                    dispositions = blProcessor.getSpecialDispositions();
                    dataLoadingHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    log.error(TPUtility.getPrintStackTrace(e));
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        }.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (data == null)
                return;
        if (resultCode == RESULT_OK) {
            if (data.hasExtra("PicturePath")) {
                String pictureImage = (String) data.getStringExtra("PicturePath");
                    //specialActivityReport.setPhoto1(pictureImage);

                    btnTakePic.setText("Take Picture"+"("+SpecialActivityPicture.getListOfImage(SpecialActivityReport.getLastInsertId()+1).size()+")" );
                }
            }

            if (data.hasExtra("Location")) {
                String location = data.getStringExtra("Location");

                addressEditText.setText(location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takePictureAction(View view) {
        ArrayList<SpecialActivityPicture> listOfImage = SpecialActivityPicture.getListOfImage(SpecialActivityReport.getLastInsertId()+1);

            int maxPhotos = 0;
            try {
                if (Feature.isFeatureAllowed(Feature.MAX_PHOTOS)) {
                    try {
                        maxPhotos = Integer.parseInt(Feature.getFeatureValue(Feature.MAX_PHOTOS));
                    } catch (Exception e) {
                    }
                }
                if (maxPhotos > 0) {
                    if (listOfImage.size() >= maxPhotos) {
                        displayErrorMessage("Exceeded max photos limit.");
                        return;
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent i = new Intent();
        i.setClass(this, TakePictureActivity.class);
        i.putExtra("NewSpecialPicture", true);
        i.putExtra("ReportId", SpecialActivityReport.getLastInsertId() + 1);
        startActivityForResult(i, 0);
        return;
    }

    public void startActivityAction(View view) {
        Toast.makeText(this, "Starting Special Activity.", Toast.LENGTH_SHORT).show();
        ACTIVITY_START++;
        specialActivityReport.setCaseNumber(caseNumberEditText.getText().toString());
        specialActivityReport.setStartDate(DateUtil.getCurrentDateTime1());
        specialActivityReport.setStartTime(DateUtil.getCurrentDateTimeActivity());
        specialActivityReport.setNotes(notesEditText.getText().toString());
        specialActivityReport.setStreetAddress(addressEditText.getText().toString());
        specialActivityReport.setUserId(TPApp.userId);
        specialActivityReport.setCustId(TPApp.custId);
        //specialActivityReport.setActivityName("SPECIAL ACTIVITY");
        startTime.setText(DateUtil.getCurrentTimeSA());

        startTimer = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
        //specialActivityReport.setAutoSelect(TPApp.activeDutyInfo.getAllowTicket());
        try {
            specialActivityReport.setActivityId(SpecialActivity.getActivityIdByName(activitySpinner.getText().toString()));
            specialActivityReport.setActivityName(activitySpinner.getText().toString());
            specialActivityReport.setDispositionId(SpecialActivityDisposition.getSpecialActivityDispositionIdByName(dispositionSpinner.getText().toString()));
        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }

        startBtn.setVisibility(View.INVISIBLE);
        endBtn.setVisibility(View.VISIBLE);
        gpsButton.setClickable(false);
        caseNumberEditText.setEnabled(false);
        addressEditText.setEnabled(false);
        activitySpinner.setEnabled(false);
        dispositionSpinner.setEnabled(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void endActivityAction(View view) {
        _showDialog();
    }

    private void _endActivity() {
        try {
            long timeDiffHoursForSA = DateUtil.getTimeDiffHoursForSA(startTime.getText().toString(), DateUtil.getCurrentTimeSA());
            System.out.println(timeDiffHoursForSA);
            specialActivityReport.setActivityName(activitySpinner.getText().toString());
            specialActivityReport.setEndDate(DateUtil.getCurrentDateTime1());
            specialActivityReport.setEndTime(DateUtil.getCurrentDateTimeActivity());
            specialActivityReport.setNotes(notesEditText.getText().toString());
            specialActivityReport.setCreatedDate(DateUtil.getCurrentDate());
            specialActivityReport.setDeviceId(TPApp.deviceId);
            specialActivityReport.setDuration(String.valueOf(specialActivityReport.getExpireInfo().getExpireMsg()));
            specialActivityReport.setLocation(location.getText().toString());
            SpecialActivityReport.insertSpecialActivityReport(specialActivityReport);
            //DatabaseHelper.getInstance().openWritableDatabase();
            //DatabaseHelper.getInstance().insertOrReplace(specialActivityReport.getContentValues(), "special_activity_reports");
            if (isServiceAvailable) {
                progressDialog = ProgressDialog.show(this, "", "Wait...",false,false);

                new Thread(){
                    /**
                     * If this thread was constructed using a separate
                     * <code>Runnable</code> run object, then that
                     * <code>Runnable</code> object's <code>run</code> method is called;
                     * otherwise, this method does nothing and returns.
                     * <p>
                     * Subclasses of <code>Thread</code> should override this method.
                     *
                     * @see #start()
                     * @see #stop()
                     */
                    @Override
                    public void run() {
                        super.run();
                        try {
                            SpecialActivityBLProcessor blProcessor = new SpecialActivityBLProcessor();
                            JSONArray activityReports = new JSONArray();
                            activityReports.put(specialActivityReport.getJSONObject());
                            blProcessor.updateActivity(activityReports);

                            ArrayList<SpecialActivityPicture> listOfImage = SpecialActivityPicture.getListOfImage(SpecialActivityReport.getLastInsertId());
                            if (listOfImage.size()>0){
                                JSONArray jr = new JSONArray();
                                ArrayList<String> image = new ArrayList<>();
                                for (SpecialActivityPicture picture: listOfImage) {
                                    jr.put(picture.getJSONObject());
                                    image.add(picture.getImagePath());
                                }
                                blProcessor.updateActivityPicture(jr,image);
                            }

                            handlerActivityP.sendEmptyMessage(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error(TPUtility.getPrintStackTrace(e));
                            handlerActivityP.sendEmptyMessage(1);
                        }
                    }
                }.start();

            } else {
            SyncData syncData = new SyncData();
            syncData.setActivity("INSERT");
            syncData.setPrimaryKey(specialActivityReport.getReportId() + "");
            syncData.setActivityDate(new Date());
            syncData.setCustId(TPApp.custId);
            syncData.setTableName(TPConstant.TABLE_SPECIAL_ACTIVITY_REPORTS);
            syncData.setStatus("Pending");
                SyncData.insertSyncData(syncData).subscribe();
            finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to create special activity report. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    public void backAction(View view) {
        if (ACTIVITY_START == 0) {
            try {
                SpecialActivityPicture.removeSPAPictureAll(SpecialActivityReport.getLastInsertId()+1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        } else {

            _showDialog();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ACTIVITY_START == 0) {
                try {
                    SpecialActivityPicture.removeSPAPictureAll(SpecialActivityReport.getLastInsertId()+1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            } else {

                _showDialog();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (ACTIVITY_START == 0) {
            try {
                SpecialActivityPicture.removeSPAPictureById(SpecialActivityReport.getLastInsertId()+1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        finish();
        } else {

            _showDialog();
        }
    }

    private void _showDialog() {
        new iOSDialogBuilder(SpecialActivityActivity.this)
                .setSubtitle("Current activity will end.")
                .setPositiveListener("End Activity", new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        _endActivity();
                        dialog.dismiss();
                    }
                })
                .setNegativeListener("Cancel", new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
    }
                })
                .build().show();
    }


    @Override
    public void onClick(View v) {

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

    @Override
    public void whereIAM(ADLocation loc) {
        if (loc != null) {
            specialActivityReport.setLongitude(String.valueOf(loc.longi));
            specialActivityReport.setLatitude(String.valueOf(loc.lat));
            System.out.println(loc.lat + "=====" + loc.longi);
            final String address = loc.address;
            addressEditText.setText(address);
        }
    }
}
